package dianshi.matchtrader.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dianshi.matchtrader.Utils.AppManager;
import com.dianshi.matchtrader.Utils.MathUtil;
import com.dianshi.matchtrader.model.DeputeModels;
import com.dianshi.matchtrader.model.IDModel_in;
import com.dianshi.matchtrader.model.KLineModel;
import com.dianshi.matchtrader.model.MapProductDetailsNotice;
import com.dianshi.matchtrader.model.MapProductNotice;
import com.dianshi.matchtrader.model.ModelInBase;
import com.dianshi.matchtrader.model.ProductModel_out;
import com.dianshi.matchtrader.model.RegProductDetailModel_out;
import com.dianshi.matchtrader.product.KLineProcesser;
import com.dianshi.matchtrader.server.FuncCall;
import com.dianshi.matchtrader.server.GlobalSingleton;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import dianshi.matchtrader.R;
import dianshi.matchtrader.adapter.TradeMoneyAdapter;
import dianshi.matchtrader.database.SQLiteHelper;
import dianshi.matchtrader.dialog.DetailDialog;
import dianshi.matchtrader.dialog.MyAlertDialog;
import dianshi.matchtrader.model.TradeMoneyModel;
import dianshi.matchtrader.ppw.StockBuyPopup;
import dianshi.matchtrader.ppw.StockPopup;
import dianshi.matchtrader.ppw.StockSellPopup;
import dianshi.matchtrader.toolbar.ToolBarActivity;
import dianshi.matchtrader.util.HandlerMsgType;
import dianshi.matchtrader.util.ScreenUtils;
import dianshi.matchtrader.view.KChartView;
import dianshi.matchtrader.view.KLineQuotaType;

/**
 * K线图详情
 * Created by Administrator on 2016/5/9 0009.
 */
public class StockDetailActivity extends ToolBarActivity implements View.OnClickListener {

    //默认加载日K
    int KlineTypeIndex = 6;
    Context context;

    LinearLayout layout_toggle;
    LinearLayout layout_header;
    LinearLayout slideLayout;
    LinearLayout layout_data, layout_btnFounction;
    ImageView img_toggle;
    TextView tv_name;
    TextView tv_price;
    TextView tv_tw_close;
    TextView tv_max_price;
    TextView tv_min_price;
    TextView tv_up_down;
    TextView tv_up_downRate;

    TextView tv_period, tv_quota;
    RadioGroup radioGroup;
    ListView listView_sell, listView_buy;


    boolean isOpen = false;
    boolean isFirst = true;
    boolean isTimePrice = false;
    int width;

    ProductModel_out productModel_out;
    int product_id;

    //SQLite数据库
    SQLiteDatabase db;
    //SQLite数据库操作类
    SQLiteHelper sqlHelper;

    TradeMoneyAdapter adapterBuy;
    TradeMoneyAdapter adapterSell;

    StockPopup periodPopup, quotaPopup;
    GlobalSingleton globalSingleton = GlobalSingleton.CreateInstance();
    List<KLineModel> kLineList = new ArrayList<>();
    KChartView kChartView;
    //是否加载K线
    boolean isLoading = true;
    //K线加载器
    KLineProcesser kLineProcesser = new KLineProcesser();
    //通知加载K线thread的handler
    Handler handler_Kline;

    //K线参数均线的类型,默认是ma参数
    KLineQuotaType kLineQuotaType = KLineQuotaType.MA;


    @Override
    protected void onDestroy() {
        isLoading = false;
        //关闭popwindow,防止内存泄露
        if (periodPopup != null && periodPopup.isShowing()) {
            periodPopup.dismiss();

        }
        if (quotaPopup != null && quotaPopup.isShowing()) {
            quotaPopup.dismiss();
        }
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        context = this;
        //初始化控件视图
        initView();
        //初始化页面传过来的数据
        initDate();
        //初始化侧滑视图
        initSlidelayout();
        //初始化listview
        initListView(new ArrayList<DeputeModels>(), new ArrayList<DeputeModels>(), true);
        //初始化RadioGroup
        initRadioGroup();
        //获取K线数据
        thread_Kline.start();
        //检测价格变化
        GlobalSingleton.CreateInstance().ServerPushHandler.regProductPriceHandler(priceChangeHandler);
        //检测五档变化
        GlobalSingleton.CreateInstance().ServerPushHandler.regProductDetailHandler(priceDetailChangeHandler);


    }


    /**
     * 加载K线的线程
     */
    Thread thread_Kline = new Thread(new Runnable() {
        @Override
        public void run() {
            while (isLoading) {

                //加载K线
                loadKData();

                Looper.prepare();//1、初始化Looper
                handler_Kline = new Handler() {//2、绑定handler到CustomThread实例的Looper对象
                    public void handleMessage(Message msg) {//3、定义处理消息的方法
                        if (isLoading) {
                            //加载K线
                            loadKData();
                        }

                    }
                };
                Looper.loop();//4、启动消息循环

            }


        }
    });

    /**
     * 网络请求加载K线
     */
    public void loadKData() {
        Message msg = KLineHandler.obtainMessage();

        try {
            //加载K线
            if (isTimePrice) {
                msg.obj = kLineProcesser.loadByTime(product_id);//分时

            } else {
                msg.obj = kLineProcesser.loadByNum(product_id, KlineTypeIndex, 200);

            }
            msg.what = HandlerMsgType.ReceivedData;
        } catch (Exception e) {
            //网络访问失败;
            msg.what = HandlerMsgType.NetBad;
            e.printStackTrace();
        } finally {
            isFirst = false;
            if (KLineHandler != null)
                KLineHandler.sendMessage(msg);
        }

    }


    /**
     * 接收K线数据的Handler
     */
    Handler KLineHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HandlerMsgType.ReceivedData) {
                kLineList.clear();
                List<KLineModel> datas = (List<KLineModel>) msg.obj;
                kLineList.addAll(datas);

                //给K线数据
                //得到该商品的数据
                ProductModel_out productModel_out = GlobalSingleton.CreateInstance().ProductPool.getProductById(product_id);

                if (productModel_out != null) {

                    kChartView.setKlineCollection(kLineList, productModel_out, isTimePrice, kLineQuotaType);
                }
            }
            if (msg.what == HandlerMsgType.NetBad) {
                isLoading = false;//停止K线加载
                AlertDialog("加载K线信息失败!");
            }
        }
    };


    /**
     * 初始化RadioGroup
     */
    private void initRadioGroup() {
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup_stockDetail);

        /**
         * 只要里面的radioBtn发生状态改变的时候,都会调用,并非是只有true的时候才会调用
         * 神奇的是
         * 如果是radiogroup 中选中A
         * A在外面被显示置为不选中
         * radiogroup还会被调用,但是选中状态认为true
         *
         * 处理这种方法:radiogroup.clearCheck()
         */

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {

                int type = -1;
                switch (id) {
                    case R.id.rbtn_stockDetail_time:
                        //分时
                        type = 0;
                        break;
                    case R.id.rbtn_stockDetail_dayK:
                        //日
                        type = 6;
                        break;
                    case R.id.rbtn_stockDetail_weekK:
                        //周
                        type = 7;
                        break;
                    case R.id.rbtn_stockDetail_monthK:
                        //月
                        type = 8;
                        break;


                }


                //只有当radioGroup中有radioButton选中的时候,才会调用
                //一分钟K线的分时的KlineTypeIndex一样,需要特别处理
                if (radioGroup.getCheckedRadioButtonId() != -1 && type != -1) {


                    kChartView.setFirst(true);

                    if (type == 0) {
                        isTimePrice = true;
                    } else {
                        isTimePrice = false;
                    }

                    //周期和指标的背景颜色变化
                    tv_quota.setBackgroundColor(getResources().getColor(R.color.bg_stock_btn_unchecked));
                    tv_quota.setTextColor(getResources().getColor(R.color.white));
                    tv_period.setBackgroundColor(getResources().getColor(R.color.bg_stock_btn_unchecked));
                    tv_period.setTextColor(getResources().getColor(R.color.white));
                    periodPopup.setItemChecked(-1);

                    //一直允许加载K线


                    isLoading = true;
                    //加载K线
                    KlineTypeIndex = type;
                    globalSingleton.PeriodTypeIndex = KlineTypeIndex;//给这个单例类PeriodTypeIndex赋值的好处是方便在KchartView里面控制不同类型的k线对应不同的时间格式

                    if (handler_Kline != null) {
                        handler_Kline.obtainMessage().sendToTarget();//发送消息到Thread_Kline实例
                    }
                }

            }
        });
    }


    /**
     * 初始化toolbar中的title
     *
     * @param tv
     */
    @Override
    public void setTitle(TextView tv) {
        super.setTitle(tv);
        tv_name = tv;
    }


    /**
     * 初始化页面传过来的数据
     */
    public void initDate() {


        //得到ql数据库
        sqlHelper = new SQLiteHelper(StockDetailActivity.this);
        //数据库可执行对象
        db = sqlHelper.getWritableDatabase();

        //得到上个页面传递过来的数据
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        //得到商品信息
        product_id = intent.getIntExtra("product_id", -1);

        productModel_out = GlobalSingleton.CreateInstance().ProductPool.getProductById(product_id);

        if (productModel_out == null) {
            return;
        }


        tv_name.setText(productModel_out.getName());

        //页面头部商品数据
        setHeaderData();

        //加载商品五档行情
        regProductDetail();


        //判断数据表是否存在，不存在就创建
        if (!sqlHelper.isTableExist(db)) {
            sqlHelper.onCreate(db);
        }
        //判断该商品是否添加自选
        if (sqlHelper.queryById(db, product_id)) {
            tv_addChoose.setText(R.string.stockDetail_cutChoose);
        }

    }

    /**
     * 初始化视图
     */
    public void initView() {


        //加自选
        tv_addChoose = (TextView) findViewById(R.id.tv_stockDetail_addChoose);
        TextView tv_goTrade = (TextView) findViewById(R.id.tv_stockDetail_goTrade);
        TextView tv_fastBuy = (TextView) findViewById(R.id.tv_stockDetail_fastBuy);
        TextView tv_fastSell = (TextView) findViewById(R.id.tv_stockDetail_fastSell);
        ImageView img_moreInfo = (ImageView) findViewById(R.id.img_stockDetail_moreInfo);
        tv_period = (TextView) findViewById(R.id.tv_stockDetail_period);
        tv_quota = (TextView) findViewById(R.id.tv_stockDetail_quota);

        listView_sell = (ListView) findViewById(R.id.listview_stockDetail_sell);
        listView_buy = (ListView) findViewById(R.id.listview_stockDetail_buy);


        //页面上方商品的价格信息
        layout_header = (LinearLayout) findViewById(R.id.stockHeadDetail);
        kChartView = (KChartView) findViewById(R.id.kchartview);
        tv_price = (TextView) findViewById(R.id.tv_stockDetail_nowPrice);
        tv_tw_close = (TextView) findViewById(R.id.tv_stockDetail_lastestPrice);
        tv_max_price = (TextView) findViewById(R.id.tv_stockDetail_upAndDown);
        tv_min_price = (TextView) findViewById(R.id.tv_stockDetail_topPrice);
        tv_up_down = (TextView) findViewById(R.id.tv_stockDetail_2);
        tv_up_downRate = (TextView) findViewById(R.id.tv_stockDetail_header_3);


        //设置单击监听事件
        tv_addChoose.setOnClickListener(this);
        tv_goTrade.setOnClickListener(this);
        tv_fastBuy.setOnClickListener(this);
        tv_fastSell.setOnClickListener(this);
        img_moreInfo.setOnClickListener(this);

        tv_quota.setOnClickListener(this);
        tv_period.setOnClickListener(this);

        periodPopup = new StockPopup(context, onItemClickListener_period, getResources().getStringArray(R.array.stockDetail_period));
        quotaPopup = new StockPopup(context, onItemClickListener_quota, getResources().getStringArray(R.array.stockDetail_quota));
        quotaPopup.setItemChecked(2);


    }

    //加自选
    TextView tv_addChoose;

    /**
     * 各类控件的单击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //将这个商品数据添加到自选中
            case R.id.tv_stockDetail_addChoose:
                if (tv_addChoose.getText().toString().equals(getResources().getString(R.string.stockDetail_addChoose))) {
                    //将此数据插入数据库的表中
                    sqlHelper.insert(db, productModel_out.getId());
                    tv_addChoose.setText(R.string.stockDetail_cutChoose);
                    Toast.makeText(context, "添加自选成功", Toast.LENGTH_SHORT).show();

                } else {
                    //将此数据从数据库的表中移除
                    sqlHelper.delete(db, productModel_out.getId());
                    tv_addChoose.setText(R.string.stockDetail_addChoose);
                    Toast.makeText(context, "取消自选成功", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tv_stockDetail_fastBuy:
                //弹出购买popWindow
                StockBuyPopup stockBuyPopup = new StockBuyPopup(StockDetailActivity.this, product_id);
                stockBuyPopup.showAtLocation(StockDetailActivity.this.findViewById(R.id.rootLayout_stockDetail), Gravity.CENTER, 0, 0);

                break;

            case R.id.tv_stockDetail_fastSell:
                //弹出卖出popWindow

                if (globalSingleton.ProductPool.isPositionProduct(product_id)) {//是否是持仓的商品

                    StockSellPopup stockSellPopup = new StockSellPopup(StockDetailActivity.this, product_id);
                    stockSellPopup.showAtLocation(StockDetailActivity.this.findViewById(R.id.rootLayout_stockDetail), Gravity.CENTER, 0, 0);
                } else {
                    MyAlertDialog myAlertDialog = new MyAlertDialog(context);
                    myAlertDialog.tipDialog("您未购买该商品,不能进行卖出");
                }


                break;

            case R.id.tv_stockDetail_goTrade:

                //跳转到交易activity
                Intent intent = new Intent(StockDetailActivity.this, MainActivity.class);
                intent.putExtra("upActivity", "StockDetailActivity");
                intent.putExtra("product_id", product_id);
                startActivity(intent);

                break;

            case R.id.img_stockDetail_moreInfo:
                //弹出详情提示框
                initDialog();
                break;
            case R.id.tv_stockDetail_period:
                //弹出选择K线计算时间周期提示框
                if (periodPopup.isShowing()) {
                    periodPopup.dismiss();
                } else {
                    periodPopup.showAsDropDown(findViewById(R.id.tv_stockDetail_period));
                }


                break;
            case R.id.tv_stockDetail_quota:
                //弹出选择指标提示框
                if (quotaPopup.isShowing()) {
                    quotaPopup.dismiss();
                } else {
                    quotaPopup.showAsDropDown(findViewById(R.id.tv_stockDetail_quota));
                }

                break;
        }
    }

    /**
     * 周期选择列表行单击事件
     */
    AdapterView.OnItemClickListener onItemClickListener_period = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //撤销radioGroup的选中状态
            radioGroup.clearCheck();

            kChartView.setFirst(true);

            if (isTimePrice == true || KlineTypeIndex != position) {

                //listview的选中效果
                tv_period.setBackgroundColor(getResources().getColor(R.color.bg_stock_btn_checked));
                tv_period.setTextColor(getResources().getColor(R.color.priceRedColor));
                tv_quota.setBackgroundColor(getResources().getColor(R.color.bg_stock_btn_unchecked));
                tv_quota.setTextColor(getResources().getColor(R.color.white));

                periodPopup.setItemChecked(position);

                //非分时图
                isTimePrice = false;
                //一直允许加载K线
                isLoading = true;
                //绘制相应类型的K线
                KlineTypeIndex = position;
                globalSingleton.PeriodTypeIndex = KlineTypeIndex;
                if (handler_Kline != null) {
                    handler_Kline.obtainMessage().sendToTarget();//发送消息到Thread_Kline实例
                }
            }


            //关闭pop
            periodPopup.dismiss();
        }
    };
    /**
     * 指标选择列表行单击事件
     */
    AdapterView.OnItemClickListener onItemClickListener_quota = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            quotaPopup.setItemChecked(position);

            kChartView.setFirst(true);
            switch (position) {

                case 0://macd参数
                    kChartView.setQuotaSection(true, KLineQuotaType.MACD);
                    break;
                case 1://KDJ参数
                    kChartView.setQuotaSection(true, KLineQuotaType.KDJ);
                    break;

                case 2://ma参数
                    if (kLineQuotaType != KLineQuotaType.MA) {
                        kLineQuotaType = KLineQuotaType.MA;
                        if (handler_Kline != null) {
                            handler_Kline.obtainMessage().sendToTarget();//发送消息到Thread_Kline实例
                        }
                    }

                    break;
                case 3://ema参数
                    if (kLineQuotaType != KLineQuotaType.EMA) {
                        kLineQuotaType = KLineQuotaType.EMA;
                        if (handler_Kline != null) {
                            handler_Kline.obtainMessage().sendToTarget();//发送消息到Thread_Kline实例
                        }
                    }
                    break;
                case 4://关闭指标图
                    kChartView.deleteQuotaSection();
                    break;
                default:
                    Toast.makeText(context, "此功能正在开发,敬请期待", Toast.LENGTH_SHORT).show();
                    break;
            }

            //关闭pop
            quotaPopup.dismiss();
        }

    };

    /**
     * 更多详情展示框
     */
    public void initDialog() {

        String[] keys = context.getResources().getStringArray(R.array.stockDetail_dialog_moreInfo);

        //计算总市值
        double totalMarketValue = productModel_out.getAllCount() * productModel_out.getPrice();
        //计算涨跌
        double topPrice = GlobalSingleton.CreateInstance().ProductPool.getTopPrice(productModel_out.getId());
        double bottomPrice = GlobalSingleton.CreateInstance().ProductPool.getBottomPrice(productModel_out.getId());
        //计算换手率
        double changRate = 0.00;

        //涉及到double运算的精度,要使用BigDecimal特殊处理,不然会丢失精度,变为0
        if (productModel_out.getHasPostCount() > 0) {

            double b1 = Double.valueOf(productModel_out.getDayAllTrade());
            double b2 = Double.valueOf(productModel_out.getHasPostCount());
            changRate = MathUtil.div(b1, b2, 6);
        }


        String[] values = {
                MathUtil.toBigDecimal(totalMarketValue, 2).toString(),
                MathUtil.toBigDecimal(productModel_out.getAllMoney(), 2).toString(),
                String.valueOf(productModel_out.getHasPostCount()),
                String.valueOf(productModel_out.getDayAllTrade()),
                MathUtil.toBigDecimal(topPrice, 2).toString(),
                MathUtil.toBigDecimal(bottomPrice, 2).toString(),
                MathUtil.strToPercentStr(String.valueOf(changRate), 2, BigDecimal.ROUND_HALF_UP),
                MathUtil.toBigDecimal(productModel_out.getIPOPrice(), 2).toString()
        };

        DetailDialog detailDialog = new DetailDialog(context, "更多信息", "关闭", "", keys, values, null);
        detailDialog.show();

    }


    /**
     * 初始化侧滑的视图
     */
    public void initSlidelayout() {

        //綁定id
        layout_toggle = (LinearLayout) findViewById(R.id.layout_stockDetail_toggle);
        layout_data = (LinearLayout) findViewById(R.id.layout_stockDetail_data);
        layout_btnFounction = (LinearLayout) findViewById(R.id.layout_stockDetail_btnFunction);
        img_toggle = (ImageView) findViewById(R.id.img_stockDetail_toggle);
        slideLayout = (LinearLayout) findViewById(R.id.slidelayout_stockDetail);

        //显示数据的列表设置宽度
        LayoutParams params = (LayoutParams) layout_data.getLayoutParams();
        width = ScreenUtils.getScreenWidth(this) / 3;
        params.width = width;
        layout_data.setLayoutParams(params);

        //侧滑的布局设置margin
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) slideLayout.getLayoutParams();
        lp.rightMargin = -width;
        slideLayout.setLayoutParams(lp);


        //控制侧滑的打开和关闭
        layout_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isOpen) {
                    //左移打开
                    ObjectAnimator.ofFloat(slideLayout, "TranslationX", 0, -width).setDuration(500).start();
                    ObjectAnimator.ofFloat(img_toggle, "rotation", 0, 180).setDuration(500).start();

                    isOpen = true;
                } else if (isOpen) {
                    //右移关闭
                    ObjectAnimator.ofFloat(slideLayout, "TranslationX", -width, 0).setDuration(500).start();
                    ObjectAnimator.ofFloat(img_toggle, "rotation", 180, 0).setDuration(500).start();
                    isOpen = false;

                }

            }
        });
    }


    /**
     * 初始化listview--交易五档明细
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
            adapterBuy = new TradeMoneyAdapter(this, arrayBuy);
            adapterSell = new TradeMoneyAdapter(this, arraySell);
            listView_sell.setAdapter(adapterSell);
            listView_buy.setAdapter(adapterBuy);
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
     * 顶部数据赋值
     */
    private void setHeaderData() {
        if (productModel_out != null) {
            double price = productModel_out.getPrice();
            double tw_close = productModel_out.getTW_Close();
            double max_price = productModel_out.getMaxPrice();
            double min_price = productModel_out.getMinPrice();
            double upDown = 0;
            double upDownRate = 0;

            if (price > 0 && tw_close > 0) {
                upDown = price - tw_close;
                upDownRate = upDown / tw_close * 100;

            } else if (price > 0 && productModel_out.getIPOPrice() > 0) {

                upDown = price - productModel_out.getIPOPrice();
                upDownRate = upDown / productModel_out.getIPOPrice() * 100;
            }


            tv_tw_close.setText(MathUtil.toBigDecimal(tw_close, 2).toString());

            setHeaderData(tv_price, price, price, tw_close, false, "");
            setHeaderData(tv_max_price, max_price, price, tw_close, false, "");
            setHeaderData(tv_min_price, min_price, price, tw_close, false, "");
            setHeaderData(tv_up_down, upDown, price, tw_close, price > 0, "");
            setHeaderData(tv_up_downRate, upDownRate, price, tw_close, price > 0, "%");
        }
    }

    /**
     * 根据数据判断涨跌显示不同的颜色
     *
     * @param textView
     * @param value
     * @param price
     * @param twPrice
     * @param isCanZero
     * @param endStr
     */
    private void setHeaderData(TextView textView, double value, double price, double twPrice, boolean isCanZero, String endStr) {
        if (isCanZero || value > 0) {
            BigDecimal valDec = BigDecimal.valueOf(value);
            valDec = valDec.setScale(2, BigDecimal.ROUND_HALF_UP);
            String valueStr = valDec.toString() + endStr;

            if (valueStr.length() >= 7) {
                textView.setTextSize(13);

            }
            textView.setText(valDec.toString() + endStr);
        } else {
            textView.setText("--");
        }
        if (price > twPrice) {
            textView.setTextColor(ContextCompat.getColor(this, R.color.priceRedColor));
        } else if (price < twPrice) {
            textView.setTextColor(ContextCompat.getColor(this, R.color.priceGreenColor));
        } else {
            textView.setTextColor(ContextCompat.getColor(this, R.color.priceGaryColor));
        }
    }

    /**
     * 加载五档
     */
    private void regProductDetail() {
        if (product_id > 0) {
            ConcurrentHashMap<String, String> dict = new ConcurrentHashMap<>();
            IDModel_in model_in = new IDModel_in();
            model_in.setId(product_id);
            FuncCall<ModelInBase, RegProductDetailModel_out> funcCall = new FuncCall<>();
            funcCall.FuncResultHandler = regProductSuccHandler;
            funcCall.FuncErrHandler = regProductFailHandler;
            funcCall.Call("RegProductDetail", model_in, RegProductDetailModel_out.class, dict);
        }
    }

    private Handler regProductSuccHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            RegProductDetailModel_out result = (RegProductDetailModel_out) msg.obj;
            initListView(result.getBuyDeputeList(), result.getSellDeputeList(), true);
        }
    };
    private Handler regProductFailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AlertDialog("加载五档信息失败!");
        }
    };


    /**
     * 提示信息对话框
     *
     * @param message
     */
    public void AlertDialog(String message) {
        //显示提示对话框
        MyAlertDialog dialog = new MyAlertDialog(AppManager.getAppManager().currentActivity());
        dialog.tipDialog(message);
    }


    /**
     * 横竖屏切换,控制
     *
     * @param newConfig
     */

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            layout_header.setVisibility(View.GONE);
            layout_btnFounction.setVisibility(View.GONE);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏

            layout_header.setVisibility(View.VISIBLE);
            layout_btnFounction.setVisibility(View.VISIBLE);

        }
    }

    /**
     * 服务器实时更新价格
     */
    private Handler priceChangeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            ArrayList<String> idArr = new ArrayList<>();
            MapProductNotice notice = (MapProductNotice) msg.obj;
            for (String id : notice.keySet()) {
                idArr.add(id);
            }

            String productIdStr = String.valueOf(product_id);
            boolean isNewPrice = idArr.contains(productIdStr);


            if (isNewPrice) {
                productModel_out = GlobalSingleton.CreateInstance().ProductPool.getProductById(product_id);
                //页面头部商品数据
                setHeaderData();
                //更新K线
                if (handler_Kline != null) {
                    handler_Kline.obtainMessage().sendToTarget();
                }
            }


        }
    };

    /**
     * 服务器实时更新五档
     */
    private Handler priceDetailChangeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            ArrayList<String> idArr = new ArrayList<>();
            MapProductDetailsNotice notice = (MapProductDetailsNotice) msg.obj;
            for (String id : notice.keySet()) {
                idArr.add(id);
            }
            String productIdStr = String.valueOf(product_id);
            boolean isNewPrice = idArr.contains(productIdStr);


            //判断推送的更新消息里面,是否有当前页面的商品
            if (isNewPrice) {
                productModel_out = GlobalSingleton.CreateInstance().ProductPool.getProductById(product_id);
                if (productModel_out != null && listView_buy != null && listView_sell != null) {
                    //更新五档详情
                    initListView(productModel_out.getBuyDeputeList(), productModel_out.getSellDeputeList(), false);
                }


            }


        }

    };


}
