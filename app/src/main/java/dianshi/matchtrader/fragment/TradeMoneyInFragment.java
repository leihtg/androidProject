package dianshi.matchtrader.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dianshi.matchtrader.Utils.MathUtil;
import com.dianshi.matchtrader.model.DeputeModel_in;
import com.dianshi.matchtrader.model.DeputeModels;
import com.dianshi.matchtrader.model.ErrorModel_out;
import com.dianshi.matchtrader.model.IDModel_in;
import com.dianshi.matchtrader.model.MapProductDetailsNotice;
import com.dianshi.matchtrader.model.ModelInBase;
import com.dianshi.matchtrader.model.ProductModel_out;
import com.dianshi.matchtrader.model.RegProductDetailModel_out;
import com.dianshi.matchtrader.model.ResultModel_out;
import com.dianshi.matchtrader.product.ProductOperate;
import com.dianshi.matchtrader.server.FuncCall;
import com.dianshi.matchtrader.server.GlobalSingleton;
import com.dianshi.matchtrader.userinfo.UserInfoLoader;
import com.dianshi.matchtrader.userinfo.UserInfoPool;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import dianshi.matchtrader.R;
import dianshi.matchtrader.adapter.ProductAutoComplateAdapter;
import dianshi.matchtrader.adapter.TradeMoneyAdapter;
import dianshi.matchtrader.constant.APPFinal;
import dianshi.matchtrader.dialog.DetailDialog;
import dianshi.matchtrader.dialog.MyAlertDialog;
import dianshi.matchtrader.dialog.WaitingDialog;
import dianshi.matchtrader.model.TradeMoneyModel;
import dianshi.matchtrader.toolbar.BaseFragment;
import dianshi.matchtrader.util.KeyBoardUtils;

/**
 * Created by Administrator on 2016/5/9 0009.
 */
public class TradeMoneyInFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    View view;
    LinearLayout layout_info;
    LinearLayout layout_listview;
    //商品信息编辑
    AutoCompleteTextView edt_productName;
    EditText edt_productPrice, edt_productAmount;
    ImageView img_clear;
    //可用余额和可买数量
    TextView tv_canTradeMoney, tv_canTradeMoneyName, tv_canTradeAmount, tv_canTradeAmountName;
    TextView tv_addText, tv_subText, tv_downLimitPrice, tv_upLimitPrice;
    Button btn_trade;
    RadioGroup radioGroup;
    LinearLayout layout_downLimitPrice, layout_upLimitPrice;
    ListView listView_sell, listView_buy;

    //选择的商品ID
    int product_id;
    //存储信息的单例类
    UserInfoPool userInfoPool = GlobalSingleton.CreateInstance().UserInfoPool;
    //买五档适配器
    TradeMoneyAdapter adapterBuy;
    //卖五档适配器
    TradeMoneyAdapter adapterSell;
    //等待框
    WaitingDialog waitingDialog;

    double topPrice, bottomPrice;

    @Override
    protected void lazyLoad() {
        getMoney();
        //初始化页面传过来的数据
        initIntent();
        //初始化商品名称
        initView();
        //加载五档信息
        regProduct();
        super.lazyLoad();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trade_operate_in, null);

        //控件绑定ID
        findViewById();

        //初始化页面接收过来的数据
        initIntent();

        //初始化和商品ID紧密结合的一些控件的数据
        initView();

        //初始化一些视图,使二个布局处于相同高度
        initLayout();

        //加载用户余额
        getMoney();

        //初始化五档listview
        initListView(new ArrayList<DeputeModels>(), new ArrayList<DeputeModels>(), true);

        //初始化购买商品名称
        initEdtProductName();

        //初始化购买单价
        initEdtProductPrice();

        //初始化选择数量的radioGroup
        initRadioGroup();


        //检测商品五档变化
        GlobalSingleton.CreateInstance().ServerPushHandler.regProductDetailHandler(productDetailHandler);
        //监听用户余额变化
        GlobalSingleton.CreateInstance().ServerPushHandler.regMoneyHandler(moneyChangeHandler);
        return view;
    }


    /**
     * 初始化和商品ID紧密结合的一些控件的数据
     */
    public void initView() {


        ProductModel_out productModel_out = GlobalSingleton.CreateInstance().ProductPool.getProductById(product_id);
        if (productModel_out != null) {
            edt_productName.setText(productModel_out.getName() + "【" + productModel_out.getCode() + "】");

            //初始化商品数量
            edt_productAmount.setText("1");

            //初始化商品价格
            double price = GlobalSingleton.CreateInstance().ProductPool.getSuggestPrice(product_id, ProductOperate.BUY);
            edt_productPrice.setText(price + "");
            //得到商品可购买数量
            getAmount(String.valueOf(price));

            //计算跌停和涨停价
            topPrice = GlobalSingleton.CreateInstance().ProductPool.getTopPrice(productModel_out.getId());
            bottomPrice = GlobalSingleton.CreateInstance().ProductPool.getBottomPrice(productModel_out.getId());
            tv_upLimitPrice.setText(MathUtil.toBigDecimal(topPrice, 2, BigDecimal.ROUND_HALF_UP).toString());
            tv_downLimitPrice.setText(MathUtil.toBigDecimal(bottomPrice, 2, BigDecimal.ROUND_HALF_UP).toString());

            //加载五档信息
            regProduct();
        } else {
            //edt_productPrice显示和选择仓的控制
            getAmount("");
            edt_productName.setText("");
            edt_productAmount.setText("");
            edt_productPrice.setText("");
            tv_upLimitPrice.setText("--");
            tv_downLimitPrice.setText("--");

        }
    }


    /**
     * 绑定控件ID
     */
    private void findViewById() {
        listView_sell = (ListView) view.findViewById(R.id.listview_tradeOperate_inAndOut_sell);
        listView_buy = (ListView) view.findViewById(R.id.listview_tradeOperate_inAndOut_buy);
        edt_productName = (AutoCompleteTextView) view.findViewById(R.id.edt_tradeOperate_inAndOut_collectName);
        edt_productPrice = (EditText) view.findViewById(R.id.edt_tradeOperate_tradeOperate_inAndOut_price);
        edt_productAmount = (EditText) view.findViewById(R.id.edt_tradeOperate_tradeOperate_inAndOut_amount);
        tv_canTradeMoney = (TextView) view.findViewById(R.id.tv_tradeOperat_inAndOut_canTradeMoney);
        tv_canTradeAmount = (TextView) view.findViewById(R.id.tv_tradeOperate_inAndOut_canTradeAmount);
        tv_canTradeAmountName = (TextView) view.findViewById(R.id.tv_tradeOperate_inAndOut_canTradeAmountName);
        tv_canTradeMoneyName = (TextView) view.findViewById(R.id.tv_tradeOperate_inAndOut_canTradeMoney_name);
        layout_info = (LinearLayout) view.findViewById(R.id.layout_tradeOperate_inAndOut_info);
        layout_listview = (LinearLayout) view.findViewById(R.id.layout_tradeOperate_inAndOut_listview);
        tv_addText = (TextView) view.findViewById(R.id.tv_tradeOperate_tradeOperate_inAndOut_add);
        tv_subText = (TextView) view.findViewById(R.id.tv_tradeOperate_tradeOperate_inAndOut_sub);
        btn_trade = (Button) view.findViewById(R.id.btn_tradeOperate_tradeOperate_inAndOut_buy);
        tv_downLimitPrice = (TextView) view.findViewById(R.id.tv_tradeOperate_inAndOut_downLimitPrice);
        tv_upLimitPrice = (TextView) view.findViewById(R.id.tv_tradeOperate_inAndOut_upLimitPrice);
        layout_downLimitPrice = (LinearLayout) view.findViewById(R.id.layout_tradeOperate_inAndOut_downLimitPrice);
        layout_upLimitPrice = (LinearLayout) view.findViewById(R.id.layout_tradeOperate_inAndOut_upLimitPrice);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup_tradeOperate_tradeOperate_inAndOut);

        tv_subText.setOnClickListener(this);
        tv_addText.setOnClickListener(this);
        btn_trade.setOnClickListener(this);
        layout_downLimitPrice.setOnClickListener(this);
        layout_upLimitPrice.setOnClickListener(this);
    }


    /**
     * 初始化页面接收过来的数据
     */
    private void initIntent() {

        //取出存储的商品信息
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(APPFinal.ShAERD_FILE_PRODUCT, Context.MODE_PRIVATE);
        product_id = sharedPreferences.getInt("product_id", -1);

    }


    /**
     * 存储改变后的商品id
     *
     * @param product_id
     */
    public void changeProductId(int product_id) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(APPFinal.ShAERD_FILE_PRODUCT, Context.MODE_PRIVATE);

        //存入选择的商品信息
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("product_id", product_id);
        editor.commit();

        sharedPreferences.getInt("product_id", -1);
    }

    /**
     * 初始化选择数量的radioGroup
     */
    private void initRadioGroup() {


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = 0;
                switch (checkedId) {
                    case R.id.rbtn_tradeOperate_tradeOperate_inAndOut_allCount:
                        index = 1;
                        break;
                    case R.id.rbtn_tradeOperate_tradeOperate_inAndOut_halfCount:
                        index = 2;
                        break;
                    case R.id.rbtn_tradeOperate_tradeOperate_inAndOut_oneOfThreeCount:
                        index = 3;
                        break;
                    case R.id.rbtn_tradeOperate_tradeOperate_inAndOut_oneOfFourCount:
                        index = 4;
                        break;
                }
                String priceStr = edt_productPrice.getText().toString().trim();
                if (priceStr != null && !priceStr.equals("")) {
                    BigInteger bigInteger = GlobalSingleton.CreateInstance().ProductPool.getCanBuyCount(product_id, priceStr);

                    if (bigInteger.compareTo(BigInteger.ZERO) >= 0) {
                        bigInteger = bigInteger.divide(BigInteger.valueOf(index));
                        edt_productAmount.setText(bigInteger.toString());
                    }
                }

            }
        });
    }


    /**
     * 初始化购买单价
     */
    private void initEdtProductPrice() {

        //动态计算购买数量
        edt_productPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {


                //计算购买的数量
                getAmount(s.toString().trim());


            }
        });
    }


    /**
     * 计算可购买余额
     */
    public void getMoney() {

        if (view != null) {
            double money = userInfoPool.getMoney() == null ? 0 : userInfoPool.getMoney();
            tv_canTradeMoney.setText(MathUtil.toBigDecimal(money, 2).toString());
            //计算可购买数量
            getAmount(edt_productPrice.getText().toString());

        }


    }

    /**
     * 通过单价计算可购买的数量
     */
    public void getAmount(String priceStr) {

        RadioButton rbtn_all = (RadioButton) view.findViewById(R.id.rbtn_tradeOperate_tradeOperate_inAndOut_allCount);
        RadioButton rbtn_half = (RadioButton) view.findViewById(R.id.rbtn_tradeOperate_tradeOperate_inAndOut_halfCount);
        RadioButton rbtn_three = (RadioButton) view.findViewById(R.id.rbtn_tradeOperate_tradeOperate_inAndOut_oneOfThreeCount);
        RadioButton rbtn_four = (RadioButton) view.findViewById(R.id.rbtn_tradeOperate_tradeOperate_inAndOut_oneOfFourCount);

        BigInteger count = GlobalSingleton.CreateInstance().ProductPool.getCanBuyCount(product_id, priceStr);
        if (count.compareTo(BigInteger.ZERO) < 0) {
            tv_canTradeAmountName.setVisibility(View.GONE);
            tv_canTradeAmount.setText("--");
            rbtn_all.setClickable(false);
            rbtn_half.setClickable(false);
            rbtn_three.setClickable(false);
            rbtn_four.setClickable(false);


        } else {
            tv_canTradeAmountName.setVisibility(View.VISIBLE);
            tv_canTradeAmount.setText(count.toString());
            radioGroup.setFocusable(true);
            if (count.compareTo(BigInteger.ZERO) > 0) {
                rbtn_all.setClickable(true);
                rbtn_half.setClickable(true);
                rbtn_three.setClickable(true);
                rbtn_four.setClickable(true);
            }

        }


    }


    /**
     * 监听单击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        ProductModel_out productModel_out = GlobalSingleton.CreateInstance().ProductPool.getProductById(product_id);

        switch (v.getId()) {
            case R.id.img_tradeOperate_inAndOut_clearName:
                edt_productName.setText(" ");
                break;
            case R.id.edt_tradeOperate_inAndOut_collectName:
                edt_productName.setText(" ");
                break;
            case R.id.tv_tradeOperate_tradeOperate_inAndOut_add:
                changePrice(0.01);
                break;
            case R.id.tv_tradeOperate_tradeOperate_inAndOut_sub:
                changePrice(-0.01);
                break;
            case R.id.layout_tradeOperate_inAndOut_downLimitPrice:
                //添加跌停价
                edt_productPrice.setText(MathUtil.toBigDecimal(bottomPrice, 2, BigDecimal.ROUND_HALF_UP).toString());
                break;
            case R.id.layout_tradeOperate_inAndOut_upLimitPrice:
                //添加涨停价
                edt_productPrice.setText(MathUtil.toBigDecimal(topPrice, 2, BigDecimal.ROUND_HALF_UP).toString());
                break;
            case R.id.btn_tradeOperate_tradeOperate_inAndOut_buy:
                if (!edt_productName.getText().toString().contains("【")) {
                    AlertMsg("请输入有效的" + GlobalSingleton.CreateInstance().ProductTypeName + "代码或名称");
                    return;
                }
                if (productModel_out == null) {
                    AlertMsg("请选择要买入的商品！");
                } else {
                    if (edt_productPrice.getText() == null || edt_productPrice.getText().toString().isEmpty() || "".equals(edt_productPrice.getText().toString())) {
                        AlertMsg("请输入合适的价格！");
                    } else if (edt_productAmount.getText() == null || edt_productAmount.getText().toString().trim().isEmpty() || edt_productAmount.getText().toString().trim().equals("")) {
                        AlertMsg("请输入合适的数量！");
                    } else {
                        //初始化对话框
                        initDialog();


                    }
                }
                break;
        }
    }

    /**
     * 对话框的监听事件
     */
    MyAlertDialog.DialogCallBack dialogCallBack = new MyAlertDialog.DialogCallBack() {
        @Override
        public void onEnSure() {
            detailDialog.dismiss();
            waitingDialog.show(getActivity().getResources().getString(R.string.tradeOperate_in_waiting));
            Buy();
        }

        @Override
        public void onCancel() {
            //关闭对话框
            detailDialog.dismiss();
        }
    };


    DetailDialog detailDialog;

    /**
     * 提示对话框
     */
    public void initDialog() {

        String[] keys = getActivity().getResources().getStringArray(R.array.SellBuy_dialog);
        ProductModel_out productModel_out = GlobalSingleton.CreateInstance().ProductPool.getProductById(product_id);
        String[] values = {
                productModel_out.getName(),
                productModel_out.getCode(),
                edt_productPrice.getText().toString(),
                edt_productAmount.getText().toString(),
        };

        detailDialog = new DetailDialog(getActivity(), "交易信息", "确认", "取消", keys, values, dialogCallBack);
        detailDialog.show();
    }


    /**
     * 改动价格
     *
     * @param changes
     */
    private void changePrice(double changes) {
        String priceStr = edt_productPrice.getText().toString();
        if (priceStr == null || "".equals(priceStr)) {
            edt_productPrice.setText("");
        } else {

            double price = Double.valueOf(priceStr).doubleValue();
            price = price + changes;

            BigDecimal p = BigDecimal.valueOf(price);
            p = p.setScale(2, BigDecimal.ROUND_HALF_UP);

            if (p.doubleValue() < 0.00) {
                edt_productPrice.setText(String.valueOf(0));
            } else {
                edt_productPrice.setText(String.valueOf(p).toString());
            }

        }
    }

    /**
     * 初始化一些视图,使二个布局处于相同高度
     */
    public void initLayout() {

        //加载等待框初始化
        waitingDialog = new WaitingDialog(getActivity());

        layout_info.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //
                int height = layout_info.getMeasuredHeight();
                ViewGroup.LayoutParams lp = layout_listview.getLayoutParams();
                lp.height = height;
                layout_listview.setLayoutParams(lp);
            }
        });
    }


    /**
     * 初始化五档明细Listview
     *
     * @param buyDeputes
     * @param sellDeputes
     */
    public void initListView(List<DeputeModels> buyDeputes, List<DeputeModels> sellDeputes, boolean isFirst) {

        ArrayList<TradeMoneyModel> arrayBuy = new ArrayList<TradeMoneyModel>();
        ArrayList<TradeMoneyModel> arraySell = new ArrayList<>();

        String[] bTitles = {"买一", "买二", "买三", "买四", "买五"};
        String[] sTitles = {"卖一", "卖二", "卖三", "卖四", "卖五"};

        for (int i = 4; i >= 0; i--) {
            TradeMoneyModel model = new TradeMoneyModel();
            model.setSellKind(sTitles[i]);
            if (sellDeputes.size() > i) {
                DeputeModels item = sellDeputes.get(i);
                double price = item.getPrice();
                BigDecimal priceDec = BigDecimal.valueOf(price);
                priceDec = priceDec.setScale(2, BigDecimal.ROUND_HALF_UP);
                model.setPrice(priceDec.toString());
                model.setTotal(String.valueOf(item.getCount()));


            } else {
                model.setPrice("--");
                model.setTotal("--");
            }
            arraySell.add(model);
        }

        for (int i = 0; i < 5; i++) {
            TradeMoneyModel model = new TradeMoneyModel();
            model.setSellKind(bTitles[i]);

            if (buyDeputes.size() > i) {
                DeputeModels item = buyDeputes.get(i);
                double price = item.getPrice();
                BigDecimal priceDec = BigDecimal.valueOf(price);
                priceDec = priceDec.setScale(2, BigDecimal.ROUND_HALF_UP);
                model.setPrice(priceDec.toString());
                model.setTotal(String.valueOf(item.getCount()));


            } else {
                model.setPrice("--");
                model.setTotal("--");
            }
            arrayBuy.add(model);
        }

        if (isFirst) {
            adapterBuy = new TradeMoneyAdapter(getActivity(), arrayBuy);
            adapterSell = new TradeMoneyAdapter(getActivity(), arraySell);
            listView_sell.setAdapter(adapterSell);
            listView_buy.setAdapter(adapterBuy);
            listView_sell.setOnItemClickListener(this);
            listView_buy.setOnItemClickListener(this);
        } else {
            if (adapterBuy != null) {
                adapterBuy.updateData(arrayBuy);
            }
            if (adapterSell != null) {
                adapterSell.updateData(arraySell);
            }
        }


    }


    /**
     * 藏品的自动补全列表
     */
    public void initEdtProductName() {
        final List<ProductModel_out> array = GlobalSingleton.CreateInstance().ProductPool.getProduct();
        ProductAutoComplateAdapter adapter = new ProductAutoComplateAdapter(getActivity(), array);
        edt_productName.setAdapter(adapter);
        edt_productName.setThreshold(0);//设置用户至少输入几个字符才会提示
        edt_productName.setHint(GlobalSingleton.CreateInstance().ProductTypeName + getActivity().getResources().getString(R.string.tradeOperate_in_collectName));
        img_clear = (ImageView) view.findViewById(R.id.img_tradeOperate_inAndOut_clearName);


        //监听单击事件
        img_clear.setOnClickListener(this);
        edt_productName.setOnClickListener(this);
        edt_productName.setOnItemClickListener(onItemClickListener);

    }

    /**
     * 自动补全列表的点击的点击事件
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            ProductModel_out product = (ProductModel_out) adapterView.getItemAtPosition(position);
            edt_productName.setText(product.getName() + "【" + product.getCode() + "】");
            product_id = product.getId();

            //存储改变后的商品ID
            changeProductId(product_id);

            //初始化View
            initView();

            if (KeyBoardUtils.isOpen(getActivity())) {
                KeyBoardUtils.closeKeyboard(edt_productName, getActivity());
            }
        }
    };

    /**
     * 五档的点击事件
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        TradeMoneyModel model = null;
        switch (adapterView.getId()) {
            case R.id.listview_tradeOperate_inAndOut_sell://卖
                model = (TradeMoneyModel) adapterView.getItemAtPosition(i);
                if (!MathUtil.isNumericDecimal(model.getPrice())) {
                    edt_productPrice.setText("0.00");
                } else {
                    edt_productPrice.setText(model.getPrice());
                }

                break;
            case R.id.listview_tradeOperate_inAndOut_buy://买
                model = (TradeMoneyModel) adapterView.getItemAtPosition(i);
                if (!MathUtil.isNumericDecimal(model.getPrice())) {
                    edt_productPrice.setText("0.00");
                } else {
                    edt_productPrice.setText(model.getPrice());
                }
                break;

        }


    }

    /**
     * 购买
     */
    private void Buy() {


        String priceStr = edt_productPrice.getText().toString().trim();
        String numStr = edt_productAmount.getText().toString().trim();

        ConcurrentHashMap<String, String> errorDict = new ConcurrentHashMap<>();
        FuncCall<DeputeModel_in, ResultModel_out> funcCall = new FuncCall<>();

        DeputeModel_in model_in = new DeputeModel_in();

        model_in.setBuy(true);
        model_in.setCount(Long.valueOf(numStr));
        model_in.setProductId(product_id);
        model_in.setDepute(true);
        model_in.setPrice(Double.valueOf(priceStr).doubleValue());


        funcCall.FuncErrHandler = deputeFailHandler;
        funcCall.FuncResultHandler = deputeSuccHandler;
        funcCall.Call("Depute", model_in, ResultModel_out.class, errorDict);

    }

    /**
     * 下单成功消息机制
     */
    private Handler deputeSuccHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ResultModel_out result = (ResultModel_out) msg.obj;
            //显示提示对话框
            AlertMsg(result.getMsg());

            if (result.getIsSucc()) {

                //刷新用户余额
                UserInfoLoader userInfoLoader = new UserInfoLoader();
                userInfoLoader.load();
                userInfoLoader.loadSuccHandler = moneyChangeHandler;
            }

        }
    };
    /**
     * 下单失败消息机制
     */
    private Handler deputeFailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ErrorModel_out result = (ErrorModel_out) msg.obj;
            //显示提示对话框
            AlertMsg(result.getErrorMsg());
        }
    };


    /**
     * 网络加载五档
     */
    private void regProduct() {
        ProductModel_out productModel_out = GlobalSingleton.CreateInstance().ProductPool.getProductById(product_id);

        if (productModel_out != null) {
            ConcurrentHashMap<String, String> dict = new ConcurrentHashMap<>();
            IDModel_in model_in = new IDModel_in();
            model_in.setId(product_id);
            FuncCall<ModelInBase, RegProductDetailModel_out> funcCall = new FuncCall<>();
            funcCall.FuncResultHandler = regSuccHandler;
            funcCall.FuncErrHandler = regFailHandler;
            funcCall.Call("RegProductDetail", model_in, RegProductDetailModel_out.class, dict);
        }

    }

    /**
     * 五档加载成功
     */
    private Handler regSuccHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            RegProductDetailModel_out result = (RegProductDetailModel_out) msg.obj;
            //初始化五档明细Listview
            initListView(result.getBuyDeputeList(), result.getSellDeputeList(), true);
        }
    };

    private Handler regFailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //显示提示对话框
            AlertMsg("数据加载失败");
        }
    };


    /**
     * 商品五档发生变化
     */
    private Handler productDetailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            ArrayList<String> idArr = new ArrayList<>();
            MapProductDetailsNotice notice = (MapProductDetailsNotice) msg.obj;
            for (String id : notice.keySet()) {
                idArr.add(id);
            }
            if (product_id == -1) {
                return;
            }
            String productIdStr = String.valueOf(product_id);
            boolean isNewPrice = idArr.contains(productIdStr);


            //判断推送的更新消息里面,是否有当前页面的商品
            if (isNewPrice) {
                ProductModel_out productModel_out = GlobalSingleton.CreateInstance().ProductPool.getProductById(product_id);
                if (productModel_out != null && listView_buy != null && listView_sell != null) {
                    //更新五档详情
                    initListView(productModel_out.getBuyDeputeList(), productModel_out.getSellDeputeList(), false);
                }


            }
        }
    };

    /**
     * 用户信息发生变化
     */
    private Handler moneyChangeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.arg1 == 1) {
                getMoney();
            }
        }
    };

    private void AlertMsg(String msg) {

        if (waitingDialog != null) {
            waitingDialog.dismiss();
        }
        MyAlertDialog dialog = new MyAlertDialog(getActivity());
        dialog.tipDialog(msg);
    }


}
