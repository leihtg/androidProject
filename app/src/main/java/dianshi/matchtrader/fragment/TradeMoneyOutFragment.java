package dianshi.matchtrader.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.dianshi.matchtrader.model.PositionModel_out;
import com.dianshi.matchtrader.model.ProductModel_out;
import com.dianshi.matchtrader.model.RegProductDetailModel_out;
import com.dianshi.matchtrader.model.ResultModel_out;
import com.dianshi.matchtrader.product.ProductLoader;
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
import dianshi.matchtrader.adapter.PositionAutoComplateAdapter;
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
public class TradeMoneyOutFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {

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

    //商品ID
    int product_id;

    //加载等待框
    WaitingDialog waitingDialog;
    //撤单详情框
    DetailDialog detailDialog;

    //买五档适配器
    TradeMoneyAdapter adapterBuy;
    //卖五档适配器
    TradeMoneyAdapter adapterSell;

    BigInteger canSellAmount = BigInteger.ZERO;

    //存储信息的单例类
    UserInfoPool userInfoPool = GlobalSingleton.CreateInstance().UserInfoPool;

    double topPrice, bottomPrice;
    ListView listView_sell, listView_buy;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trade_operate_out, null);


        //控件绑定ID
        findViewById();
        //加载用户余额
        getMoney();
        //初始化页面传过来的数据
        initIntent();
        //初始化和商品ID紧密结合的一些控件的数据
        initView();
        //初始化一些视图,使二个布局处于相同高度
        initlayout();

        //初始化商品名称
        initEdtProductName();

        //初始化五档listview
        initListView(new ArrayList<DeputeModels>(), new ArrayList<DeputeModels>(), true);

        //初始化选择数量的radioGroup
        initRadioGroup();

        //监听五档变化
        GlobalSingleton.CreateInstance().ServerPushHandler.regProductDetailHandler(productDetailHandler);
        //监听商品持仓变化
        GlobalSingleton.CreateInstance().ServerPushHandler.regPositionHandler(positionHandler);
        //监听用户余额变化
        GlobalSingleton.CreateInstance().ServerPushHandler.regMoneyHandler(moneyChangeHandler);
        return view;
    }

    @Override
    protected void lazyLoad() {
        getMoney();
        //初始化页面传过来的数据
        initIntent();

        //初始化和商品ID紧密结合的一些控件的数据
        initView();

        super.lazyLoad();
    }

    /**
     * 初始化和商品ID紧密结合的一些控件的数据
     */
    public void initView() {

        getCanSellCount();

        PositionModel_out positionModel_out = GlobalSingleton.CreateInstance().ProductPool.getPositionById(product_id);
        if (positionModel_out != null) {
            edt_productName.setText(positionModel_out.getProductName() + "【" + positionModel_out.getProductCode() + "】");

            //初始化商品数量
            edt_productAmount.setText("1");

            //初始化商品价格
            double price = GlobalSingleton.CreateInstance().ProductPool.getSuggestPrice(product_id, ProductOperate.SELL);
            edt_productPrice.setText(price + "");

            //计算跌停和涨停价
            topPrice = GlobalSingleton.CreateInstance().ProductPool.getTopPrice(product_id);
            bottomPrice = GlobalSingleton.CreateInstance().ProductPool.getBottomPrice(product_id);

            /*
                两者的保留精度不同,这个很重要
                涨停:BigDecimal.ROUND_DOWN---1.769-1.76 因为1.77已经高于涨停
                跌停:BigDecimal.ROUND_UP----1.453-1.46 因为1.45已经低于跌停
                 */
            tv_upLimitPrice.setText(MathUtil.toBigDecimal(topPrice, 2, BigDecimal.ROUND_HALF_UP).toString());
            tv_downLimitPrice.setText(MathUtil.toBigDecimal(bottomPrice, 2, BigDecimal.ROUND_HALF_UP).toString());

            //加载五档信息
            regProduct();
        } else {
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
    public void findViewById() {
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
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup_tradeOperate_tradeOperate_inAndOut);

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
                    BigInteger bigInteger = GlobalSingleton.CreateInstance().ProductPool.getCanSellCount(product_id);

                    if (bigInteger != null) {
                        bigInteger = bigInteger.divide(BigInteger.valueOf(index));
                        edt_productAmount.setText(bigInteger.toString());
                    }

                }

            }
        });
    }

    /**
     * 初始化页面传过来的数据
     */
    public void initIntent() {
        //存储商品信息
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(APPFinal.ShAERD_FILE_PRODUCT, Context.MODE_PRIVATE);
        product_id = sharedPreferences.getInt("product_id", -1);

    }

    /**
     * +
     * 计算可购买余额
     */
    public void getMoney() {
        if (view != null) {
            double money = userInfoPool.getMoney() == null ? 0 : userInfoPool.getMoney();
            tv_canTradeMoney.setText(MathUtil.toBigDecimal(money, 2).toString());
        }


    }


    /**
     * 初始化可卖数量
     */
    public void getCanSellCount() {
        RadioButton rbtn_all = (RadioButton) view.findViewById(R.id.rbtn_tradeOperate_tradeOperate_inAndOut_allCount);
        RadioButton rbtn_half = (RadioButton) view.findViewById(R.id.rbtn_tradeOperate_tradeOperate_inAndOut_halfCount);
        RadioButton rbtn_three = (RadioButton) view.findViewById(R.id.rbtn_tradeOperate_tradeOperate_inAndOut_oneOfThreeCount);
        RadioButton rbtn_four = (RadioButton) view.findViewById(R.id.rbtn_tradeOperate_tradeOperate_inAndOut_oneOfFourCount);

        PositionModel_out positionModel_out = GlobalSingleton.CreateInstance().ProductPool.getPositionById(product_id);
        if (positionModel_out == null) {
            tv_canTradeAmountName.setVisibility(View.GONE);
            tv_canTradeAmount.setText("--");
            rbtn_all.setClickable(false);
            rbtn_half.setClickable(false);
            rbtn_three.setClickable(false);
            rbtn_four.setClickable(false);
        } else {
            canSellAmount = GlobalSingleton.CreateInstance().ProductPool.getCanSellCount(product_id);
            tv_canTradeAmountName.setVisibility(View.VISIBLE);
            tv_canTradeAmount.setText(canSellAmount.toString());
            if (canSellAmount.compareTo(BigInteger.ZERO) > 0) {
                rbtn_all.setClickable(true);
                rbtn_half.setClickable(true);
                rbtn_three.setClickable(true);
                rbtn_four.setClickable(true);
            }
        }


    }


    /**
     * 初始化购买商品名称
     */
    public void initEdtProductName() {
        final List<PositionModel_out> array = GlobalSingleton.CreateInstance().ProductPool.getpositionList();
        PositionAutoComplateAdapter adapter = new PositionAutoComplateAdapter(getActivity(), array);

        edt_productName.setAdapter(adapter);
        edt_productName.setThreshold(0);//设置用户至少输入几个字符才会提示
        edt_productName.setOnItemClickListener(onItemClickListener);
        edt_productName.setOnClickListener(this);
        edt_productName.setHint(GlobalSingleton.CreateInstance().ProductTypeName + getActivity().getResources().getString(R.string.tradeOperate_in_collectName));


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

            PositionModel_out positionModel_out = (PositionModel_out) adapterView.getItemAtPosition(position);
            product_id = positionModel_out.getProductId();
            //改变控件
            initView();
            //存储商品ID
            changeProductId(product_id);

            if (KeyBoardUtils.isOpen(getActivity())) {
                KeyBoardUtils.closeKeyboard(edt_productName, getActivity());
            }
        }
    };

    /**
     * 改变控件
     *
     * @param product_id
     */
    public void changeProductId(int product_id) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(APPFinal.ShAERD_FILE_PRODUCT, Context.MODE_PRIVATE);

        //存入选择的商品信息
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("product_id", product_id);
        editor.commit();
    }

    /**
     * 监听行单击事件
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
                ;
                break;

        }

    }


    /**
     * 监听单击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                if (edt_productName.getText().toString().trim().equals("")) {
                    AlertMsg("请选择要卖出的产品！");
                } else {
                    if (edt_productPrice.getText() == null || edt_productPrice.getText().toString().trim().isEmpty() || "".equals(edt_productPrice.getText().toString())) {
                        AlertMsg("请输入价格！");
                    } else if (edt_productAmount.getText() == null || edt_productAmount.getText().toString().trim().isEmpty() || "".equals(edt_productAmount.getText().toString())) {
                        AlertMsg("请输入数量！");
                    } else if (canSellAmount.compareTo(BigInteger.ZERO) <= 0) {
                        AlertMsg("您的持仓不足");
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
            //加载等待框
            waitingDialog.show(getResources().getString(R.string.tradeOperate_out_waiting));
            //卖出
            Sell();
        }

        @Override
        public void onCancel() {
            //关闭对话框
            detailDialog.dismiss();
        }
    };


    /**
     * 撤单提示对话框
     */
    public void initDialog() {
        String[] keys = getActivity().getResources().getStringArray(R.array.SellBuy_dialog);
        PositionModel_out positionModel_out = GlobalSingleton.CreateInstance().ProductPool.getPositionById(product_id);
        if (positionModel_out != null) {
            String[] values = {
                    positionModel_out.getProductName(),
                    positionModel_out.getProductCode(),
                    edt_productPrice.getText().toString(),
                    edt_productAmount.getText().toString(),
            };

            detailDialog = new DetailDialog(getActivity(), "交易信息", "确认", "取消", keys, values, dialogCallBack);
            detailDialog.show();
        }


    }


    /**
     * 商品卖出
     */
    private void Sell() {
        String priceStr = edt_productPrice.getText().toString().trim();
        String numStr = edt_productAmount.getText().toString().trim();

        ConcurrentHashMap<String, String> errorDict = new ConcurrentHashMap<>();
        FuncCall<DeputeModel_in, ResultModel_out> funcCall = new FuncCall<>();

        DeputeModel_in model_in = new DeputeModel_in();

        model_in.setBuy(false);
        model_in.setCount(Integer.valueOf(numStr).intValue());
        model_in.setProductId(product_id);
        model_in.setDepute(true);
        model_in.setPrice(Double.valueOf(priceStr).doubleValue());


        funcCall.FuncErrHandler = deputeFailHandler;
        funcCall.FuncResultHandler = deputeSuccHandler;
        funcCall.Call("Depute", model_in, ResultModel_out.class, errorDict);


    }

    /**
     * 卖出成功
     */
    private Handler deputeSuccHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ResultModel_out result = (ResultModel_out) msg.obj;
            //显示提示对话框
            AlertMsg(result.getMsg());

            if (result.getIsSucc()) {
                //刷新持仓冻结数量
                ProductLoader productLoader = new ProductLoader();
                productLoader.loadPositionProduct();
                productLoader.loadPositionHandler = positionHandler;


                //刷新用户余额
                UserInfoLoader userInfoLoader = new UserInfoLoader();
                userInfoLoader.load();
                userInfoLoader.loadSuccHandler = moneyChangeHandler;
            }


        }
    };
    /**
     * 卖出失败
     */
    private Handler deputeFailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //显示提示对话框
            AlertMsg("下单失败!");
        }
    };


    private void changePrice(double changes) {
        String priceStr = edt_productPrice.getText().toString().trim();
        if (priceStr == null || "".equals(priceStr)) {
            edt_productPrice.setText("");
        } else {
            double price = Double.valueOf(priceStr).doubleValue();
            price = price + changes;

            BigDecimal p = BigDecimal.valueOf(price);
            p = p.setScale(2, BigDecimal.ROUND_HALF_UP);

            //价格不能为负
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
    public void initlayout() {

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
     * 初始化listview
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
     * 加载商品五档
     */
    private void regProduct() {
        boolean isPosition = GlobalSingleton.CreateInstance().ProductPool.isPositionProduct(product_id);
        if (isPosition) {
            ConcurrentHashMap<String, String> dict = new ConcurrentHashMap<>();
            IDModel_in model_in = new IDModel_in();
            model_in.setId(product_id);
            FuncCall<IDModel_in, RegProductDetailModel_out> funcCall = new FuncCall<>();
            funcCall.FuncErrHandler = regFailHandler;
            funcCall.FuncResultHandler = regSuccHandler;
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

            if (view != null) {
                //写入5档行情
                initListView(result.getBuyDeputeList(), result.getSellDeputeList(), true);
            }

        }
    };

    /**
     * 五档加载失败
     */
    private Handler regFailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //显示提示对话框
            ErrorModel_out errorModel_out = (ErrorModel_out) msg.obj;
            AlertMsg(errorModel_out.getErrorMsg());
        }
    };

    /**
     * 用户的持仓更新
     */
    private Handler positionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1) {

                //改变可卖数量
                getCanSellCount();

            }
        }
    };

    /**
     * 五档更新
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
