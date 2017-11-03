package dianshi.matchtrader.ppw;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dianshi.matchtrader.Utils.MathUtil;
import com.dianshi.matchtrader.model.DeputeModel_in;
import com.dianshi.matchtrader.model.ProductModel_out;
import com.dianshi.matchtrader.model.ResultModel_out;
import com.dianshi.matchtrader.product.ProductOperate;
import com.dianshi.matchtrader.server.FuncCall;
import com.dianshi.matchtrader.server.GlobalSingleton;
import com.dianshi.matchtrader.userinfo.UserInfoLoader;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;

import dianshi.matchtrader.R;
import dianshi.matchtrader.dialog.DetailDialog;
import dianshi.matchtrader.dialog.MyAlertDialog;
import dianshi.matchtrader.dialog.WaitingDialog;

/**
 * 邮币卡详情中弹出的买入popupWindow
 * Created by Administrator on 2016/5/8 0008.
 */
public class StockBuyPopup extends PopupWindow {

    Context context;
    View ppwView;
    EditText edt_price, edt_amount;
    ProductModel_out productModel_out;
    //等待框
    WaitingDialog waitingDialog;


    TextView tv_canTradeAmountName, tv_canTradeAmount, tv_downLimitPrice, tv_upLimitPrice;
    Button btn_add, btn_cancel;
    View line;
    LinearLayout layout_downLimitPrice, layout_upLimitPrice;

    double topPrice, bottomPrice;


    public StockBuyPopup(Context context, int product_id) {

        this.context = context;
        productModel_out = GlobalSingleton.CreateInstance().ProductPool.getProductById(product_id);
        //初始化PopupWindow设置
        initPopup();

        //初始化popupWindow的控件参数
        initView();

        //改变控件参数
        changeView();

        //监听用户余额变化
        GlobalSingleton.CreateInstance().ServerPushHandler.regMoneyHandler(moneyChangeHandler);


    }

    /**
     * 用户信息发生变化
     */
    private Handler moneyChangeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.arg1 == 1) {
                if (edt_price != null) {
                    getAmount(edt_price.getText().toString().toString());

                }
            }
        }
    };

    /**
     * 初始化PopupWindow设置
     */
    public void initPopup() {
        ppwView = LayoutInflater.from(context).inflate(R.layout.ppw_stock_detail, null);
        this.setContentView(ppwView);
        // 窗体添加布局
        this.setContentView(ppwView);
        // 窗体高铺满
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 窗体高自适应
        this.setHeight(LayoutParams.MATCH_PARENT);

        // 弹出窗体可点击
        this.setFocusable(true);
        // 弹出窗体动画效果
//		this.setAnimationStyle(R.style.PopupBottomAnimation);
//		this.setAnimationStyle(R.anim.push_bottom_out);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // 设置自适应屏幕
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // ppwView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        ppwView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = ppwView.findViewById(R.id.layout_stockDetail_ppw).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    /**
     * 因为快买快卖是同一个布局,所以要处理一下
     */
    public void changeView() {
        line.setBackgroundColor(context.getResources().getColor(R.color.priceRedColor));
        btn_add.setBackgroundColor(context.getResources().getColor(R.color.priceRedColor));
        btn_cancel.setTextColor(context.getResources().getColor(R.color.priceRedColor));

        tv_canTradeAmountName.setText("可买数量");
        btn_add.setText("买入");


    }

    /**
     * 计算可购买数量
     */
    public void getAmount(String priceStr) {
        BigInteger count = GlobalSingleton.CreateInstance().ProductPool.getCanBuyCount(productModel_out.getId(), priceStr);
        if (count.compareTo(BigInteger.ZERO) < 0) {
            tv_canTradeAmountName.setVisibility(View.GONE);
            tv_canTradeAmount.setText("--");

        } else {
            tv_canTradeAmountName.setVisibility(View.VISIBLE);
            tv_canTradeAmount.setText(count.toString());

        }
    }


    /**
     * 初始化popupWindow的控件参数
     */
    public void initView() {

        waitingDialog = new WaitingDialog(context);


        //绑定控件ID
        edt_price = (EditText) ppwView.findViewById(R.id.edt_stockDetail_ppw_price);
        edt_amount = (EditText) ppwView.findViewById(R.id.edt_stockDetail_ppw_amount);
        tv_canTradeAmountName = (TextView) ppwView.findViewById(R.id.tv_stockDetail_ppw_canTradeAmountName);
        tv_canTradeAmount = (TextView) ppwView.findViewById(R.id.tv_stockDetail_ppw_canTradeAmount);
        line = ppwView.findViewById(R.id.line_stockDetail_ppw);
        btn_cancel = (Button) ppwView.findViewById(R.id.btn_stockDetail_ppw_cancel);
        btn_add = (Button) ppwView.findViewById(R.id.btn_stockDetail_ppw_sure);
        tv_downLimitPrice = (TextView) ppwView.findViewById(R.id.tv_stockDetail_ppw_downLimitPrice);
        tv_upLimitPrice = (TextView) ppwView.findViewById(R.id.tv_stockDetail_ppw_upLimitPrice);
        layout_downLimitPrice = (LinearLayout) ppwView.findViewById(R.id.layout_stockDetail_ppw_downLimitPrice);
        layout_upLimitPrice = (LinearLayout) ppwView.findViewById(R.id.layout_stockDetail_ppw_upLimitPrice);

        //监听单击事件
        btn_cancel.setOnClickListener(onClickListener);
        btn_add.setOnClickListener(onClickListener);
        layout_downLimitPrice.setOnClickListener(onClickListener);
        layout_upLimitPrice.setOnClickListener(onClickListener);

        //计算建议价格
        GlobalSingleton globalSingleton = GlobalSingleton.CreateInstance();
        double price = globalSingleton.ProductPool.getSuggestPrice(productModel_out.getId(), ProductOperate.BUY);
        edt_price.setText(String.valueOf(price));

        //计算可买数量
        getAmount(String.valueOf(price));


        edt_amount.setText("1");

        //计算涨跌
        topPrice = GlobalSingleton.CreateInstance().ProductPool.getTopPrice(productModel_out.getId());
        bottomPrice = GlobalSingleton.CreateInstance().ProductPool.getBottomPrice(productModel_out.getId());
        /*
        服务器规定 ---跌停和涨停的精度都是四舍五入

         */
        tv_upLimitPrice.setText(MathUtil.toBigDecimal(topPrice, 2, BigDecimal.ROUND_HALF_UP).toString());
        tv_downLimitPrice.setText(MathUtil.toBigDecimal(bottomPrice, 2, BigDecimal.ROUND_HALF_UP).toString());


        //添写价格时,动态可买数量
        edt_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String priceStr = edt_price.getText().toString().trim();
                if (!priceStr.equals("")) {
                    getAmount(priceStr);
                }

            }
        });
    }


    /**
     * 监听单击事件
     */
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_stockDetail_ppw_cancel:
                    //关闭ppw
                    dismiss();
                    break;
                case R.id.layout_stockDetail_ppw_downLimitPrice:
                    //添加跌停价
                    edt_price.setText(MathUtil.toBigDecimal(bottomPrice, 2, BigDecimal.ROUND_HALF_UP).toString());
                    break;
                case R.id.layout_stockDetail_ppw_upLimitPrice:
                    //添加涨停价
                    edt_price.setText(MathUtil.toBigDecimal(topPrice, 2, BigDecimal.ROUND_HALF_UP).toString());
                    break;
                case R.id.btn_stockDetail_ppw_sure:
                    //弹出购买确认框

                    if (edt_price.getText() == null || edt_price.getText().toString().isEmpty() || "".equals(edt_price.getText().toString())) {
                        AlertMessage("请输入价格！");
                    } else if (edt_amount.getText() == null || edt_amount.getText().toString().isEmpty() || "".equals(edt_amount.getText().toString())) {
                        AlertMessage("请输入数量！");
                    } else {

                        //初始化对话框
                        initDialog();

                        break;
                    }
            }
        }
    };

    /**
     * 对话框的监听事件
     */
    MyAlertDialog.DialogCallBack dialogCallBack = new MyAlertDialog.DialogCallBack() {
        @Override
        public void onEnSure() {
            detailDialog.dismiss();
            waitingDialog.show(context.getResources().getString(R.string.tradeOperate_in_waiting));
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
     * 撤单提示对话框
     */
    public void initDialog() {

        String[] keys = context.getResources().getStringArray(R.array.SellBuy_dialog);
        String[] values = {productModel_out.getName(),
                productModel_out.getCode(),
                edt_price.getText().toString(),
                edt_amount.getText().toString()
        };

        detailDialog = new DetailDialog(context, "交易信息", "确认", "取消", keys, values, dialogCallBack);
        detailDialog.show();
    }


    /**
     * 购买
     */
    private void Buy() {
        String priceStr = edt_price.getText().toString();
        String numStr = edt_amount.getText().toString();

        ConcurrentHashMap<String, String> errorDict = new ConcurrentHashMap<>();
        FuncCall<DeputeModel_in, ResultModel_out> funcCall = new FuncCall<>();

        DeputeModel_in model_in = new DeputeModel_in();

        model_in.setBuy(true);
        model_in.setCount(Integer.valueOf(numStr).intValue());
        model_in.setProductId(productModel_out.getId());
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
            AlertMessage(result.getMsg());

            if (result.getIsSucc()) {

                //刷新用户余额以便于刷新用户可购买数量
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

            //显示提示对话框
            AlertMessage("下单失败!");

        }
    };

    private void AlertMessage(String msg) {

        if (waitingDialog != null) {
            waitingDialog.dismiss();
        }
        MyAlertDialog dialog = new MyAlertDialog(context);
        dialog.tipDialog(msg);
    }
}



