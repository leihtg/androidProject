package dianshi.matchtrader.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dianshi.matchtrader.server.FuncCall;
import com.dianshi.matchtrader.model.ModelInBase;
import com.dianshi.matchtrader.model.DeputeModels;
import com.dianshi.matchtrader.model.IDModel_in;
import com.dianshi.matchtrader.model.ProductModel_out;
import com.dianshi.matchtrader.model.RegProductDetailModel_out;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import dianshi.matchtrader.R;
import dianshi.matchtrader.adapter.TradeMoneyAdapter;
import dianshi.matchtrader.database.SQLiteHelper;
import dianshi.matchtrader.model.TradeMoneyModel;
import dianshi.matchtrader.toolbar.ToolBarActivity;
import dianshi.matchtrader.util.ScreenUtils;
import dianshi.matchtrader.dialog.MyAlertDialog;
import dianshi.matchtrader.view.MyViewPager;
import dianshi.matchtrader.ppw.StockBuyPopup;
import dianshi.matchtrader.ppw.StockSellPopup;

/**
 * KLine图详情
 * 带滑动切换商品效果
 * <p>
 * Created by Administrator on 2016/5/11 0011.
 */
public class ListStockDetailActivity extends ToolBarActivity implements View.OnClickListener {


    ArrayList<View> viewArray;
    MyViewPager viewPager;

    int KlineTypeIndex = 6;
    LinearLayout layout_toggle;
    LinearLayout slideLayout;
    LinearLayout layout_data;
    RelativeLayout.LayoutParams lp;
    ImageView img_toggle;
    TextView tv_name;
    TextView tv_price;
    TextView tv_tw_close;
    TextView tv_max_price;
    TextView tv_min_price;
    TextView tv_up_down;
    TextView tv_up_downRate;


    boolean isOpen = false;
    int width;

    ProductModel_out productModel_out;
    int product_id;

    //SQLite数据库
    SQLiteDatabase db;
    //SQLite数据库操作类
    SQLiteHelper sqlHelper;

    Context context;
    View currentView;
    int product_position;
    ArrayList<ProductModel_out> array;
    boolean Scrollleft, Scrollright;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        context = ListStockDetailActivity.this;

        //初始化上个页面传输过来的数据
        initIntentDate();
        //初始化ViewPager
        initViewPager();
        //初始化数据显示
        //主要的更新操作都在这个函数里
        switchProduct();


    }

    /**
     * 初始化页面传过来的数据
     */
    public void initIntentDate() {
        sqlHelper = new SQLiteHelper(ListStockDetailActivity.this);
        //数据库可执行对象
        db = sqlHelper.getWritableDatabase();

        //得到上个页面传递过来的数据
        Intent intent = getIntent();
        array = (ArrayList<ProductModel_out>) intent.getSerializableExtra("product_array");
        product_position = intent.getIntExtra("product_position", -1);
        productModel_out = array.get(product_position);
        product_id = productModel_out.getId();


    }

    /**
     * 初始化ViewPager
     */
    public void initViewPager() {
        viewPager = (MyViewPager) findViewById(R.id.viewPager_stockDetail);
        viewArray = new ArrayList<>();

        for (int i = 0; i < array.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.layout_content_stock_detail, null);
            viewArray.add(view);
        }


        //ViewPager设置适配器
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(product_position);


        //Viewpager设置滑动监听
        viewPager.setChangeViewCallback(new MyViewPager.ChangeViewCallback() {
            @Override
            public void changeView(boolean left, boolean right) {
                //判断viewpager的滑动方向
                Scrollleft = left;
                Scrollright = right;
            }

            @Override
            public void getCurrentPageIndex(int position) {
                //当在首页和末页时弹出提示
                if (position == array.size() - 1 && Scrollleft) {
                    Toast.makeText(context, "最后一页", Toast.LENGTH_SHORT).show();
                }
                if (position == 0 && Scrollright) {
                    Toast.makeText(context, "第一页", Toast.LENGTH_SHORT).show();
                }
                product_position = position;

                //加载商品数据
                switchProduct();

            }
        });


    }

    /**
     * ViewPager适配器
     */
    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return viewArray.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = viewArray.get(position);
            container.addView(view);
            return view;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewArray.get(position));
        }
    };

    /**
     * 商品切换时的数据加载
     */
    public void switchProduct() {

        currentView = viewArray.get(viewPager.getCurrentItem());
        productModel_out = array.get(product_position);
        product_id = productModel_out.getId();
        tv_name.setText(productModel_out.getName());


        //侧滑
        initSlidelayout();
        //头部控件绑定ID
        initHeaderControl();
        //加载商品详情
        regProductDetail();
        //头部控件加载数据
        setHeaderData();
        //底部按钮
        initBottomBtn();
    }


    /**
     * 初始化侧滑的视图
     */
    public void initSlidelayout() {

        //綁定id
        layout_toggle = (LinearLayout) currentView.findViewById(R.id.layout_stockDetail_toggle);
        layout_data = (LinearLayout) currentView.findViewById(R.id.layout_stockDetail_data);
        img_toggle = (ImageView) currentView.findViewById(R.id.img_stockDetail_toggle);
        slideLayout = (LinearLayout) currentView.findViewById(R.id.slidelayout_stockDetail);

        //显示数据的列表设置宽度
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout_data.getLayoutParams();
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
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) slideLayout.getLayoutParams();

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
     * 初始化底部按钮的监听事件
     */
    public void initBottomBtn() {
        //加自选
        TextView tv_addChoose = (TextView) currentView.findViewById(R.id.tv_stockDetail_addChoose);
        TextView tv_goTrade = (TextView) currentView.findViewById(R.id.tv_stockDetail_goTrade);
        TextView tv_fastBuy = (TextView) currentView.findViewById(R.id.tv_stockDetail_fastBuy);
        TextView tv_fastSell = (TextView) currentView.findViewById(R.id.tv_stockDetail_fastSell);
        tv_addChoose.setOnClickListener(this);
        tv_goTrade.setOnClickListener(this);
        tv_fastBuy.setOnClickListener(this);
        tv_fastSell.setOnClickListener(this);
    }

    /**
     * 初始化头部
     */
    private void initHeaderControl() {

        tv_price = (TextView) currentView.findViewById(R.id.tv_stockDetail_nowPrice);
        tv_tw_close = (TextView) currentView.findViewById(R.id.tv_stockDetail_lastestPrice);
        tv_max_price = (TextView) currentView.findViewById(R.id.tv_stockDetail_upAndDown);
        tv_min_price = (TextView) currentView.findViewById(R.id.tv_stockDetail_topPrice);
        tv_up_down = (TextView) currentView.findViewById(R.id.tv_stockDetail_2);
        tv_up_downRate = (TextView) currentView.findViewById(R.id.tv_stockDetail_header_3);

    }

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
                //判断是否插入过
                //判断数据表是否存在，不存在就创建
                if (!sqlHelper.isTableExist(db)) {
                    sqlHelper.onCreate(db);
                }
                if (sqlHelper.queryById(db, product_id)) {
                    Toast.makeText(context, "已添加过该条数据", Toast.LENGTH_SHORT).show();
                } else {
                    //将此数据插入数据库的表中
                    sqlHelper.insert(db, productModel_out.getId());
                    Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.tv_stockDetail_fastBuy:
                //弹出购买popWindow
                StockBuyPopup stockBuyPopup = new StockBuyPopup(context, product_id);
                stockBuyPopup.showAtLocation(ListStockDetailActivity.this.findViewById(R.id.rootLayout_stockDetail), Gravity.CENTER, 0, 0);

                break;

            case R.id.tv_stockDetail_fastSell:
                //弹出卖出popWindow
                StockSellPopup stockSellPopup = new StockSellPopup(context, product_id);
                stockSellPopup.showAtLocation(ListStockDetailActivity.this.findViewById(R.id.rootLayout_stockDetail), Gravity.CENTER, 0, 0);

                break;

            case R.id.tv_stockDetail_goTrade:
                //跳转到交易activity
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("upActivity", "StockDetailActivity");
                startActivity(intent);
                break;
        }
    }


    /**
     * 初始化listview
     */
    public void initListView(List<DeputeModels> buyDeputes, List<DeputeModels> sellDeputes) {
        ListView listView_1 = (ListView) currentView.findViewById(R.id.listview_stockDetail_sell);
        ListView listView_2 = (ListView) currentView.findViewById(R.id.listview_stockDetail_buy);

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
                model.setPrice("-");
                model.setTotal("-");
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
                model.setPrice("-");
                model.setTotal("-");
            }
            arrayBuy.add(model);
        }
        TradeMoneyAdapter adapterBuy = new TradeMoneyAdapter(this, arrayBuy);
        TradeMoneyAdapter adapterSell = new TradeMoneyAdapter(this, arraySell);
        listView_1.setAdapter(adapterSell);
        listView_2.setAdapter(adapterBuy);
    }


    /**
     * 获得头部的价格
     */
    private void setHeaderData() {
        if (productModel_out != null) {
            double price = productModel_out.getPrice();
            double tw_close = productModel_out.getTW_Close();
            double max_price = productModel_out.getMaxPrice();
            double min_price = productModel_out.getMinPrice();
            double upDown = price > 0 && tw_close > 0 ? (price - tw_close) : 0;
            double upDownRate = price > 0 && tw_close > 0 ? upDown / tw_close * 100 : 0;

            setHeaderData(tv_price, price, price, tw_close, false, "");
            setHeaderData(tv_tw_close, tw_close, tw_close, tw_close, false, "");
            setHeaderData(tv_max_price, max_price, max_price, tw_close, false, "");
            setHeaderData(tv_min_price, min_price, min_price, tw_close, false, "");
            setHeaderData(tv_up_down, upDown, upDown, 0, price > 0, "");
            setHeaderData(tv_up_downRate, upDownRate, upDownRate, 0, price > 0, "%");
        }
    }

    /**
     * 通过头部的价格，显示相应的颜色
     */
    private void setHeaderData(TextView textView, double value, double price, double twPrice, boolean isCanZero, String endStr) {
        if (isCanZero || value > 0) {
            BigDecimal valDec = BigDecimal.valueOf(value);
            valDec = valDec.setScale(2, BigDecimal.ROUND_HALF_UP);
            textView.setText(valDec.toString() + endStr);
        } else {
            textView.setText("-");
        }
        if (price > twPrice) {
            textView.setTextColor(ContextCompat.getColor(this, R.color.priceRedColor));
        } else if (price < twPrice) {
            textView.setTextColor(ContextCompat.getColor(this, R.color.priceRedColor));
        } else {
            textView.setTextColor(ContextCompat.getColor(this, R.color.priceGaryColor));
        }
    }


    /**
     * 通过商品ID加载商品数据
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

    /**
     * 加载成功
     */
    private Handler regProductSuccHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            RegProductDetailModel_out result = (RegProductDetailModel_out) msg.obj;
            initListView(result.getBuyDeputeList(), result.getSellDeputeList());
        }
    };
    /**
     * 加载失败
     */
    private Handler regProductFailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //显示提示对话框
            MyAlertDialog dialog = new MyAlertDialog(ListStockDetailActivity.this);
            dialog.tipDialog("加载数据出错!");
        }
    };

    /**
     * 获取详情失败
     */
    private Handler productDetailChangeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (productModel_out != null) {
                initListView(productModel_out.getBuyDeputeList(), productModel_out.getSellDeputeList());
            }
        }
    };


}
