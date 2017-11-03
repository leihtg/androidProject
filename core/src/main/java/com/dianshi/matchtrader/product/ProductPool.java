package com.dianshi.matchtrader.product;

import android.os.Handler;

import com.dianshi.matchtrader.Utils.MathUtil;
import com.dianshi.matchtrader.model.DeputeModel;
import com.dianshi.matchtrader.model.DeputeModels;
import com.dianshi.matchtrader.model.MapProductDeputeCountModel;
import com.dianshi.matchtrader.model.MapProductDetailsNotice;
import com.dianshi.matchtrader.model.MapProductNotice;
import com.dianshi.matchtrader.model.PositionModel_out;
import com.dianshi.matchtrader.model.ProductCategoryModel_out;
import com.dianshi.matchtrader.model.ProductDeputeCountModel;
import com.dianshi.matchtrader.model.ProductDetailsNotice;
import com.dianshi.matchtrader.model.ProductModel_out;
import com.dianshi.matchtrader.model.ProductNotice;
import com.dianshi.matchtrader.model.TickModel;
import com.dianshi.matchtrader.model.TickNotice;
import com.dianshi.matchtrader.server.GlobalSingleton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/5/12.
 */
public class ProductPool {

    private List<String> productCodeList;
    /**
     * 商品分类的列表
     */
    private ArrayList<ProductCategoryModel_out> categoryList;
    /**
     * 持仓商品的列表
     */
    private ArrayList<PositionModel_out> positionList;
    /**
     * 所有商品的列表
     */
    private ArrayList<ProductModel_out> productList;


    private ConcurrentHashMap<Integer, List<ProductModel_out>> productCateDict = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, ProductModel_out> productDict = new ConcurrentHashMap<>();
    /**
     * 存储持仓的列表
     */
    private ConcurrentHashMap<Integer, PositionModel_out> positionDict = new ConcurrentHashMap<>();
    /**
     * 单例模式
     */
    static ProductPool instance = new ProductPool();

    public static ProductPool CreateInstance() {
        return instance;
    }


    /**
     * 添加分类
     *
     * @param categories
     */
    public void AddCategory(List<ProductCategoryModel_out> categories) {
        categoryList = new ArrayList<ProductCategoryModel_out>();
        categoryList.addAll(categories);
    }

    /**
     * 添加持仓列表
     *
     * @param
     */
    public void AddPositionProduct(List<PositionModel_out> products) {
        positionList = new ArrayList<>();
        positionDict.clear();

        //以商品ID为Key,将持仓列表添加到列表中
        for (PositionModel_out position : products) {
            int productId = position.getProductId();
            positionDict.put(productId, position);
        }

        positionList.addAll(products);
    }

    /**
     * 添加商品
     *
     * @param products
     */
    public void AddProduct(List<ProductModel_out> products) {
        productCodeList = new ArrayList<>();
        productList = new ArrayList<>();
        productCateDict.clear();
        productDict.clear();

        for (ProductModel_out product : products) {
            productList.add(product);
            int cateId = product.getCategoryId();
            int productId = product.getId();

            if (!productCateDict.containsKey(cateId)) {
                productCateDict.put(cateId, new ArrayList<ProductModel_out>());
            }
            productCodeList.add(product.getCode());
            productCateDict.get(cateId).add(product);
            productDict.put(productId, product);
        }
    }

    /**
     * 通过分类ID,获取该分类的所有商品
     *
     * @param id
     * @return
     */
    public List<ProductModel_out> getProductByCateId(int id) {
        if (productCateDict.containsKey(id)) {
            return productCateDict.get(id);
        } else {
            return new ArrayList<>();
        }
    }

    public List<ProductCategoryModel_out> getProductCategory() {
        return categoryList;
    }

    /**
     * 获取所有商品列表
     *
     * @return
     */
    public List<ProductModel_out> getProduct() {
        return productList;
    }

    public List<String> getAllCode() {
        return productCodeList;
    }

    /**
     * 根据商品ID得到商品
     *
     * @param id
     * @return
     */
    public ProductModel_out getProductById(int id) {
        if (productDict.containsKey(id)) {
            return productDict.get(id);
        }
        return null;
    }


    /**
     * 根据商品ID列表得到商品列表
     *
     * @param array
     * @return
     */
    public List<ProductModel_out> getProductList(List<Integer> array) {

        List<ProductModel_out> result = new ArrayList<>();
        for (Integer id : array) {
            if (productDict.containsKey(id)) {
                result.add(productDict.get(id));
            }
        }

        return result;
    }


    /**
     * 根据商品ID得到该商品持仓信息
     *
     * @param id
     * @return
     */
    public PositionModel_out getPositionById(int id) {

        if (!isPositionProduct(id)) {
            return null;
        }


        for (PositionModel_out model : positionList) {
            if (model.getProductId() == id) {
                return model;
            }
        }

        return null;
    }


    /**
     * 判断是否包含持仓商品
     * @param maps
     * @return
     */
    public boolean isContainPositionProduct( MapProductNotice maps) {


        //判断商品是否有更新
        for (Map.Entry<String, JsonObject> item : maps.entrySet()) {
            String key = item.getKey();
            int productId = Integer.valueOf(key);

            if (positionDict != null) {
                //更新对应的商品
                PositionModel_out position = positionDict.get(productId);
                ProductNotice pn = null;
                try {
                    pn = new Gson().fromJson(item.getValue().toString(), ProductNotice.class);
                } catch (Exception ex) {
                }
                if (position != null && pn != null) {
                    return true;
                }
            }
        }
        return false;

    }
    /**
     * 根据商品ID判断是否是持仓列表
     *
     * @param id
     * @return
     */
    public boolean isPositionProduct(int id) {

        if (positionList == null) {
            return false;
        }

        for (PositionModel_out model : positionList) {
            if (model.getProductId() == id) {
                return true;
            }
        }

        return false;

    }


    /**
     * 得到这个商品的建议销售价格
     *
     * @param product_id
     * @return
     */
    public double getSuggestPrice(int product_id, ProductOperate productOperate) {

        double price = 0;
        ProductModel_out productModel_out = getProductById(product_id);

        if ( productModel_out == null ||productOperate == null ) {
            return price;
        }
        if (productOperate == ProductOperate.BUY) {
            //卖一价
            price = productModel_out.getSellFirstPrice();
        } else if (productOperate == ProductOperate.SELL) {
            //买一价
            price = productModel_out.getBuyFirstPrice();
        }

        if (price <= 0) {
            //现价
            price = productModel_out.getPrice();
        }
        if (price <= 0) {
            //昨收价
            price = productModel_out.getTW_Close();
        }
        if (price <= 0) {
            //发行价
            price = productModel_out.getIPOPrice();
        }

        return price;
    }


    /**
     * 得到商品的涨停价
     *
     * @param product_id
     * @return
     */
    public double getTopPrice(int product_id) {
        double price = 0;
        double topPrice = 0;
        ProductModel_out productModel_out = getProductById(product_id);

        if ( productModel_out == null ) {
            return price;
        }
        //昨收价
        price = productModel_out.getTW_Close();
        if (price <= 0) {
            //发行价
            price = productModel_out.getIPOPrice();
            //计算涨跌
            topPrice = price * (1 + productModel_out.getIPOTopRate());
        }else
        {
            //计算涨跌
            topPrice = price * (1 + productModel_out.getTopRate());
        }




        return topPrice;
    }
    /**
     * 得到商品的跌停价
     *
     * @param product_id
     * @return
     */
    public double getBottomPrice(int product_id) {
        double price = 0;
        double bottomPrice = 0;
        ProductModel_out productModel_out = getProductById(product_id);

        if ( productModel_out == null ) {
            return price;
        }
        //昨收价
        price = productModel_out.getTW_Close();
        if (price <= 0) {
            //发行价
            price = productModel_out.getIPOPrice();
            //计算涨跌
            bottomPrice = price * (1 - productModel_out.getIPOBottomRate());
        }
        else {
            //计算涨跌
            bottomPrice = price * (1 - productModel_out.getBottomRate());
        }

        return bottomPrice;
    }
    /**
     * 得到商品的最大可卖数量
     *
     * @param product_id
     * @return
     */
    public BigInteger getCanSellCount(int product_id) {
        //初始值设置为-1,设置为null显示的时候要加判断,否则容易发生空指针异常
        BigInteger count = BigInteger.valueOf(-1);
        if (product_id != -1 && isPositionProduct(product_id)) {
            //该商品的持仓量
            count = BigInteger.valueOf(getPositionById(product_id).getCount());
        }


        return count;
    }


    /**
     * 得到商品的最大可买数量
     *
     * @return
     */
    public BigInteger getCanBuyCount(int product_id, String priceStr) {
        BigInteger count = BigInteger.valueOf(-1);

        if (priceStr == null || priceStr.equals("")|| !MathUtil.isNumericDecimal(priceStr)){
            return count;
        }

        double price = Double.valueOf(priceStr);

        ProductModel_out productModel_out = getProductById(product_id);
        if (productModel_out != null && price > 0) {
            //得到余额和价格计算而来可买数量
            double computeCount = GlobalSingleton.CreateInstance().UserInfoPool.getMoney() /((1 +  productModel_out.getBuyCharge())* price);


            //得到购买该商品的的手续费
            double chargeMoney = computeCount*price *productModel_out.getSellCharge();
            //是否大于最低的手续费
            if(chargeMoney < productModel_out.getBuyChargeMin()){
                if (GlobalSingleton.CreateInstance().UserInfoPool.getMoney() != null){
                    computeCount = (GlobalSingleton.CreateInstance().UserInfoPool.getMoney().doubleValue()-productModel_out.getSellChargeMin())/price;
                }
            }

            BigInteger count_money = BigDecimal.valueOf(computeCount).setScale(0, BigDecimal.ROUND_DOWN).toBigInteger();

            //如果最大交易数量是0的话,代表无限制
            if (productModel_out.getPerMaxTradeCount() >0){
                //得到最大可购买的数量
                count_money = BigInteger.valueOf(Math.min(count_money.longValue(),productModel_out.getPerMaxTradeCount()));
            }

            count = count_money;
        }
        return count;
    }


    /**
     * 获取持仓列表
     *
     * @return
     */
    public List<PositionModel_out> getpositionList() {
        return positionList;
    }


    /**
     * 重新加载持仓
     */
    public void modifyPosition(Handler handler) {
        ProductLoader productLoader = new ProductLoader();
        productLoader.loadPositionHandler = handler;
        productLoader.loadPositionProduct();
        return;
    }


    /**
     * 修改持仓商品的市值和盈亏
     *
     * @param maps
     */
    public void modifyPosition(MapProductNotice maps) {

        //判断商品是否有更新
        for (Map.Entry<String, JsonObject> item : maps.entrySet()) {
            int productId = Integer.valueOf(item.getKey());

            if (positionDict != null) {
                //更新对应的商品
                PositionModel_out position = positionDict.get(productId);
                ProductNotice pn = null;
                try {
                    pn = new Gson().fromJson(item.getValue().toString(), ProductNotice.class);

                } catch (Exception ex) {
                }
                if (position != null && pn != null) {

                    synchronized (position) {//加锁
                        //没有交易的时候，返回的商品信息除了ID不为空，其他的数据都是0,所以在这个地方我们不更新单例类的数据
                        if(pn.getP() <= 0){
                            continue;
                        }
                        int count = Integer.valueOf(position.getAllCount());
                        double averagePrice = Double.valueOf(position.getAvgPrice());
                        double newPrice = pn.getP();
                        double allPay = averagePrice * count;
                        double marketValue = newPrice * count;
                        double getOrLoss = marketValue - allPay;
                        double getorLossRate = 0;

                        if (allPay > 0) {
                            getorLossRate = (marketValue - allPay) / allPay;
                        }

                        position.setMarketValue(String.valueOf(marketValue));
                        position.setHoldProfit(String.valueOf(getOrLoss));
                        position.setProfitRate(String.valueOf(getorLossRate));
                    }

                }
            }
        }

    }

    /**
     * 更新商品----基本信息
     *
     * @param maps
     */
    public void modifyProduct(MapProductNotice maps) {
        for (Map.Entry<String, JsonObject> item : maps.entrySet()) {
            int key = Integer.valueOf(item.getKey());
            JsonObject value = item.getValue();
            ProductNotice pn = null;
            try {
                pn = new Gson().fromJson(item.getValue().toString(), ProductNotice.class);

            } catch (Exception ex) {

            }
            ProductModel_out product = this.getProductById(key);
            if (product != null && pn != null) {
                synchronized (product) {
                    //没有交易的时候，返回的商品信息除了ID不为空，其他的数据都是0,所以在这个地方我们不更新单例类的数据
                    if(pn.getP() <= 0){
                        continue;
                    }
                    if (product.getOpen() == 0) {
                        product.setOpen(pn.getP());
                    }
                    product.setPrice(pn.getP());
                    product.setNowCount(pn.getNC());
                    product.setSellFirstPrice(pn.getSFP());
                    product.setSellFirstCount(pn.getSFC());
                    product.setBuyFirstPrice(pn.getBFP());
                    product.setBuyFirstCount(pn.getBFC());
                    product.setDayAllTrade(pn.getAC());
                    product.setMaxPrice(pn.getMxP());
                    product.setMinPrice(pn.getMiP());
                    product.setAllMoney(pn.getAM());
                    product.setDeputeSCount(pn.getDSC());
                    product.setDeputeBCount(pn.getDBC());
                    product.setBuyVolume(pn.getBC());
                    product.setSellVolume(pn.getSC());
                }
            }
        }
    }

    /**
     * 更新商品----买五档,卖五档和明细
     *
     * @param maps
     */
    public void modifyProduct(MapProductDetailsNotice maps) {

        for (Map.Entry<String, JsonObject> item : maps.entrySet()) {

            int productId = Integer.valueOf(item.getKey());

            ProductDetailsNotice pn = null;
            //获得该id商品的商品model
            ProductModel_out product = getProductById(productId);
            try {
                pn = new Gson().fromJson(item.getValue().toString(), ProductDetailsNotice.class);

            } catch (Exception ex) {

            }

            //去该model中修改商品的五档信息
            if (product != null && pn != null) {
                modifyProductDetail(product, pn, false);
            }
        }
    }

    /**
     * 更新商品----买五档,卖五档和明细
     *
     * @param productModel_out
     * @param details
     * @param isInit
     */
    public void modifyProductDetail(ProductModel_out productModel_out, ProductDetailsNotice details, boolean isInit) {
        synchronized (this) {
            if (isInit) {
                productModel_out.getTickQueue().clear();
            }
            productModel_out.getBuyDeputeList().clear();
            productModel_out.getSellDeputeList().clear();

            ArrayList<TickModel> TickQueue = new ArrayList<>();
            ArrayList<DeputeModels> BuyDeputeList = new ArrayList<>();
            ArrayList<DeputeModels> SellDeputeList = new ArrayList<>();


            if (isInit) {
                for (TickNotice t : details.getTL()) {
                    TickModel tModel = new TickModel();
                    tModel.setCount(t.getC());
                    tModel.setDateTime(t.getT());
                    tModel.setPrice(t.getP());
                    tModel.setType(t.getTP());
                    TickQueue.add(0, tModel);
                }
            } else {
                for (TickNotice t : details.getTL()) {
                    TickModel tModel = new TickModel();
                    tModel.setCount(t.getC());
                    tModel.setDateTime(t.getT());
                    tModel.setPrice(t.getP());
                    tModel.setType(t.getTP());
                    TickQueue.add(tModel);
                }
            }

            for (DeputeModel d : details.getBL()) {
                DeputeModels bModel = new DeputeModels();
                bModel.setPrice(d.getP());
                bModel.setCount(d.getC());

                BuyDeputeList.add(bModel);
            }

            for (DeputeModel d : details.getSL()) {
                DeputeModels sModel = new DeputeModels();
                sModel.setPrice(d.getP());
                sModel.setCount(d.getC());
                SellDeputeList.add(sModel);
            }


            productModel_out.setTickQueue(TickQueue);
            productModel_out.setSellDeputeList(SellDeputeList);
            productModel_out.setBuyDeputeList(BuyDeputeList);
        }
    }


    /**
     * 更新商品----委买,委卖数量,买一档和卖一档信息
     *
     * @param maps
     */
    public void modifyProduct(MapProductDeputeCountModel maps) {
        for (Map.Entry<String, JsonObject> item : maps.entrySet()) {
            String key = item.getKey();
            JsonObject value = item.getValue();

            int productId = Integer.valueOf(key);
            ProductDeputeCountModel pn = null;
            ProductModel_out product = this.getProductById(productId);
            try {
                pn = new Gson().fromJson(value.toString(), ProductDeputeCountModel.class);
            } catch (Exception ex) {
            }
            if (product != null && pn != null) {
                synchronized (product) {
                    product.setSellFirstCount(pn.getSFC());
                    product.setSellFirstPrice(pn.getSFP());
                    product.setBuyFirstCount(pn.getBFC());
                    product.setBuyFirstPrice(pn.getBFP());
                    product.setDeputeBCount(pn.getDBC());
                    product.setDeputeSCount(pn.getDSC());
                }
            }

        }
    }
}
