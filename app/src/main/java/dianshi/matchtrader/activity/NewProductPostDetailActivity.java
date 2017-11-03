package dianshi.matchtrader.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dianshi.matchtrader.Utils.MathUtil;
import com.dianshi.matchtrader.model.ErrorModel_out;
import com.dianshi.matchtrader.model.IDModel_in;
import com.dianshi.matchtrader.model.ProductPostAskModel_in;
import com.dianshi.matchtrader.model.ProductPostAskModel_out;
import com.dianshi.matchtrader.model.ProductPostDetailModel_out;
import com.dianshi.matchtrader.model.ResultModel_out;
import com.dianshi.matchtrader.server.FuncCall;
import com.dianshi.matchtrader.server.GlobalSingleton;
import com.dianshi.matchtrader.userinfo.UserInfoLoader;
import com.dianshi.matchtrader.userinfo.UserInfoPool;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

import dianshi.matchtrader.R;
import dianshi.matchtrader.dialog.MyAlertDialog;
import dianshi.matchtrader.dialog.WaitingDialog;
import dianshi.matchtrader.toolbar.ToolBarActivity;
import dianshi.matchtrader.util.DateUtils;
import dianshi.matchtrader.util.StringUtils;
import dianshi.matchtrader.util.TimeZoneUtils;

/**
 * Created by Administrator on 2017/1/3.
 */
public class NewProductPostDetailActivity extends ToolBarActivity implements View.OnClickListener {

    Context context;
    int productPostId;
    TextView tv_name;
    TextView tv_category;
    TextView tv_price;
    TextView tv_charge;
    TextView tv_startAskTime;
    TextView tv_endAskTime;
    TextView tv_isFrozen;
    TextView tv_frozenEndTime;
    TextView tv_perMinCount;
    TextView tv_perMaxCount;
    TextView tv_isAllowedCancel;
    TextView tv_perMinLastMoney;
    TextView tv_canTradeMoney;
    TextView tv_canTradeCount;
    EditText edt_postCount;
    Button btn_askPost;
    ProductPostDetailModel_out result;

    WaitingDialog waitingDialog;
    boolean isCanAskPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product_post_detail);

        context = this;

        //初始化控件
        initView();

        //获取页面传过来的数据
        getIntentInfo();

        //检测用户金额
        GlobalSingleton.CreateInstance().ServerPushHandler.regMoneyHandler(moneyChangeHandler);


    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        tv_name.setText(getResources().getString(R.string.newProductPostDetail_title));
    }


    /**
     * 获取Intent信息
     */
    public void getIntentInfo() {
        Intent intent = getIntent();
        if (intent != null) {
            productPostId = intent.getIntExtra("productPostId", -1);
            if (waitingDialog == null) {
                waitingDialog = new WaitingDialog(context);
            }
            waitingDialog.show(getResources().getString(R.string.newProductPostDetail_loading_detail));
            loadProductPostDetail();
        }
    }

    /**
     * 初始化控件
     */
    public void initView() {
        tv_name = (TextView) findViewById(R.id.tv_newProductPostDetail_name);
        tv_category = (TextView) findViewById(R.id.tv_newProductPostDetail_category);
        tv_price = (TextView) findViewById(R.id.tv_newProductPostDetail_price);
        tv_charge = (TextView) findViewById(R.id.tv_newProductPostDetail_charge);
        tv_startAskTime = (TextView) findViewById(R.id.tv_newProductPostDetail_startAskTime);
        tv_endAskTime = (TextView) findViewById(R.id.tv_newProductPostDetail_endAskTime);
        tv_isFrozen = (TextView) findViewById(R.id.tv_newProductPostDetail_isFrozen);
        tv_frozenEndTime = (TextView) findViewById(R.id.tv_newProductPostDetail_frozenEndTime);
        tv_perMinCount = (TextView) findViewById(R.id.tv_newProductPostDetail_perMinCount);
        tv_perMaxCount = (TextView) findViewById(R.id.tv_newProductPostDetail_perMaxCount);
        tv_isAllowedCancel = (TextView) findViewById(R.id.tv_newProductPostDetail_isAllowedCancel);
        tv_perMinLastMoney = (TextView) findViewById(R.id.tv_newProductPostDetail_perMinLastMoney);
        tv_canTradeMoney = (TextView) findViewById(R.id.tv_newProductPostDetail_canTradeMoney);
        tv_canTradeCount = (TextView) findViewById(R.id.tv_newProductPostDetail_canTradeCount);
        edt_postCount = (EditText) findViewById(R.id.edt_newProductPostDetail_count);
        btn_askPost = (Button) findViewById(R.id.btn_newProductPostDetail_askPost);


        btn_askPost.setOnClickListener(this);

        changePostView(false, getResources().getString(R.string.newProductPostDetail_count));


    }

    /**
     * 判断是否满足申购的条件,显示不同的状态的编辑框
     *
     * @param enable
     */
    private void changePostView(boolean enable, String msg) {
        btn_askPost.setEnabled(enable);
        edt_postCount.setEnabled(enable);
        String hintStr = (msg == null || msg.equals("")) ? "数量" : msg;

        if (enable) {
            btn_askPost.setBackgroundColor(ContextCompat.getColor(context, R.color.priceRedColor));
            edt_postCount.setBackground(getResources().getDrawable(R.drawable.border_shape_red_stroke_money));
            edt_postCount.setHint(hintStr);
        } else {
            edt_postCount.setHint(hintStr);
            btn_askPost.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_btn_enable_false));
            edt_postCount.setBackground(getResources().getDrawable(R.drawable.border_shape_gray_stroke_money));
        }

    }

    /**
     * 验证是否在申购产品的开始和结束时间之内
     *
     * @param startDateStr
     * @param endDateStr
     * @return
     */
    private boolean checkPostDate(String startDateStr, String endDateStr, String pattern) {

        int start = DateUtils.compareNowDateStr(startDateStr, pattern);
        int end = DateUtils.compareNowDateStr(endDateStr, pattern);

        return (start <= 0 && end >= 0);
    }

    @Override
    public void setTitle(TextView tv) {
        super.setTitle(tv);
        tv.setText(getResources().getString(R.string.newProductPostDetail_title));

    }

    /**
     * 加载申购产品详情
     */
    private void loadProductPostDetail() {

        ConcurrentHashMap<String, String> errorDict = new ConcurrentHashMap<>();
        IDModel_in model_in = new IDModel_in();
        model_in.setId(productPostId);
        FuncCall<IDModel_in, ProductPostDetailModel_out> funcCall = new FuncCall<>();
        funcCall.FuncErrHandler = detailfuncErrHandler;
        funcCall.FuncResultHandler = detailfuncResultHandler;
        funcCall.Call("ProductPostDetail", model_in, ProductPostDetailModel_out.class, errorDict);

        if (errorDict.keys().hasMoreElements()) {
            AlertMessage(getResources().getString(R.string.newProductPostDetail_errorMsg_getDetailFail));
        }
    }


    /**
     * 详情获取失败
     */
    private Handler detailfuncErrHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ErrorModel_out errorModel_out = (ErrorModel_out) msg.obj;
            //显示提示对话框
            AlertMessage(errorModel_out.getErrorMsg());
        }
    };
    /**
     * 详情获取成功
     */
    private Handler detailfuncResultHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            result = (ProductPostDetailModel_out) msg.obj;
            if (result != null) {
                tv_name.setText(result.getProductName());
                tv_category.setText(result.getCategoryName());
                tv_price.setText(MathUtil.toBigDecimal(result.getPrice(), 2).toString());
                tv_charge.setText(MathUtil.toBigDecimal(result.getCharge(), 2).toString());

                //接口返回的时间有的时候包含"T"这个字母,为了正常转换时间,我们需要特殊处理一下
                String startTime = result.getStartTime().replace("T", " ");
                String endTime = result.getEndTime().replace("T", " ");
                String frozenTime = result.getFrozenTime().replace("T", " ");

                tv_startAskTime.setText(TimeZoneUtils.transfromTimeStr(startTime, "yyyy-MM-dd HH:mm:ss"));
                tv_endAskTime.setText(TimeZoneUtils.transfromTimeStr(endTime, "yyyy-MM-dd HH:mm:ss"));

                tv_isFrozen.setText(result.isFrozen() ? getResources().getString(R.string.newProductPostDetail_isFrozen_true) : getResources().getString(R.string.newProductPostDetail_isFrozen_false));
                tv_frozenEndTime.setText(result.isFrozen() ? TimeZoneUtils.transfromTimeStr(frozenTime, "yyyy-MM-dd HH:mm:ss") : getResources().getString(R.string.newProductPostDetail_isFrozen_true_time));

                tv_perMinCount.setText(String.valueOf(result.getPerMinCount()));
                tv_perMaxCount.setText(String.valueOf(result.getPerMaxCount()));
                tv_isAllowedCancel.setText(result.isAllowedCancel() ? getResources().getString(R.string.newProductPostDetail_isAllowedCancel_true) : getResources().getString(R.string.newProductPostDetail_isAllowedCancel_false));
                tv_perMinLastMoney.setText(getResources().getString(R.string.newProductPostDetail_perMinLastMoney_note) + MathUtil.toBigDecimal(result.getPerMinLastMoney(), 2).toString() + getResources().getString(R.string.newProductPostDetail_perMinLastMoney_unit));

                UserInfoPool userInfoPool = GlobalSingleton.CreateInstance().UserInfoPool;
                double money = userInfoPool.getMoney() == null ? 0 : userInfoPool.getMoney();
                BigDecimal bigDecimal = BigDecimal.valueOf(money);
                bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                tv_canTradeMoney.setText(bigDecimal.toString());
                tv_canTradeCount.setText("0");


                if (checkPostDate(startTime, endTime, "yyyy-MM-dd HH:mm:ss") && getCanTradeCount() > result.getPerMinCount()) {
                    if (waitingDialog == null) {
                        waitingDialog = new WaitingDialog(context);
                    }
                    waitingDialog.show(getResources().getString(R.string.newProductPostDetail_loading_postAsk));


                    //询问是否满足申购条件
                    postProductPre();

                } else {
                    if (waitingDialog != null && waitingDialog.isShowing()) {
                        waitingDialog.dismiss();
                    }


                    if (getCanTradeCount() == 0) {
                        changePostView(false, getResources().getString(R.string.newProductPostDetail_errorMsg_donnotHaveEnoughMoney));

                    } else {
                        changePostView(false, getResources().getString(R.string.newProductPostDetail_errorMsg_beforeStartAskTime));


                    }


                }


            }

        }
    };


    /**
     * 是否满足申购产品的需求
     */
    private void postProductPre() {

        ConcurrentHashMap<String, String> errorDict = new ConcurrentHashMap<>();
        IDModel_in model_in = new IDModel_in();
        model_in.setId(productPostId);

        FuncCall<IDModel_in, ProductPostAskModel_out> funcCall = new FuncCall<>();
        funcCall.FuncErrHandler = postProductPreFuncErrHandler;
        funcCall.FuncResultHandler = postProductPreFuncResultHandler;
        funcCall.Call("ProductPostAskPre", model_in, ProductPostAskModel_out.class, errorDict);


        if (errorDict.keys().hasMoreElements()) {
            //显示提示对话框
            AlertMessage(getResources().getString(R.string.newProductPostDetail_errorMsg_askPostFail));
        }
    }

    private Handler postProductPreFuncErrHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ErrorModel_out errorModel_out = (ErrorModel_out) msg.obj;
            //显示提示对话框
            AlertMessage(errorModel_out.getErrorMsg());

        }
    };
    private Handler postProductPreFuncResultHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ProductPostAskModel_out result = (ProductPostAskModel_out) msg.obj;
            //关闭等待框
            if (waitingDialog != null && waitingDialog.isShowing()) {
                waitingDialog.dismiss();
            }

            if (result != null) {
                isCanAskPost = result.isCanAsk();
                if (result.isCanAsk()) {
                    //显示申购页面
                    changePostView(true, getResources().getString(R.string.newProductPostDetail_count));
                    tv_canTradeCount.setText(String.valueOf(getCanTradeCount()));
                } else {
                    changePostView(false, result.getMsg());
                }


            } else {
                AlertMessage(getResources().getString(R.string.newProductPostDetail_errorMsg_askPostFail));
            }


        }
    };

    /**
     * 申购产品
     */
    private void postProduct() {

        ConcurrentHashMap<String, String> errorDict = new ConcurrentHashMap<>();
        ProductPostAskModel_in model_in = new ProductPostAskModel_in();
        model_in.setPostId(productPostId);
        model_in.setCount(Integer.parseInt(edt_postCount.getText().toString()));


        FuncCall<ProductPostAskModel_in, ResultModel_out> funcCall = new FuncCall<>();
        funcCall.FuncErrHandler = postProductFuncErrHandler;
        funcCall.FuncResultHandler = postProductFuncResultHandler;
        funcCall.Call("ProductPostAsk", model_in, ResultModel_out.class, errorDict);


        if (errorDict.keys().hasMoreElements()) {
            AlertMessage(getResources().getString(R.string.newProductPostDetail_errorMsg_postFail));
        }
    }

    /**
     * 申购失败
     */
    private Handler postProductFuncErrHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ErrorModel_out errorModel_out = (ErrorModel_out) msg.obj;
            //显示提示对话框
            AlertMessage(errorModel_out.getErrorMsg());

        }
    };


    /**
     * 申购成功
     */
    private Handler postProductFuncResultHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ResultModel_out result = (ResultModel_out) msg.obj;
            if (result != null) {
                if (result.getIsSucc()) {
                    isCanAskPost = false;
                    //刷新用户余额
                    UserInfoLoader userInfoLoader = new UserInfoLoader();
                    userInfoLoader.load();
                    userInfoLoader.loadSuccHandler = moneyChangeHandler;

                    if (waitingDialog != null) {
                        waitingDialog.dismiss();
                    }
                    //显示提示对话框
                    final MyAlertDialog dialog = new MyAlertDialog(context);
                    dialog.tipDialog(getResources().getString(R.string.newProductPostDetail_errorMsg_postSuccess), getResources().getString(R.string.dialog_ensure), "", new MyAlertDialog.DialogCallBack() {
                        @Override
                        public void onEnSure() {

                            if (dialog != null) {
                                dialog.dismiss();
                            }

                            //返回到申购列表
                            finish();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });


                } else {
                    AlertMessage(result.getMsg());
                }

            } else {

                AlertMessage(getResources().getString(R.string.newProductPostDetail_errorMsg_postFail));


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
                UserInfoPool userInfoPool = GlobalSingleton.CreateInstance().UserInfoPool;
                double money = userInfoPool.getMoney() == null ? 0 : userInfoPool.getMoney();
                BigDecimal bigDecimal = BigDecimal.valueOf(money);
                bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                tv_canTradeMoney.setText(bigDecimal.toString());


                if (isCanAskPost) {
                    tv_canTradeCount.setText(String.valueOf(getCanTradeCount()));
                } else {
                    tv_canTradeCount.setText("0");

                }

            }
        }
    };

    /**
     * 获取可申购的最大数量
     *
     * @return
     */
    private int getCanTradeCount() {
        int canTradeCount = 0;

        UserInfoPool userInfoPool = GlobalSingleton.CreateInstance().UserInfoPool;
        double money = userInfoPool.getMoney() == null ? 0 : userInfoPool.getMoney();

        if (result.getPerMinLastMoney() < money)//判断是否满足申购门槛
        {
            double count_money = money / result.getPrice();
            if (count_money > result.getPerMinCount())//判断是否大于最小的申购数量
            {
                canTradeCount = Math.min((int) count_money, result.getPerMaxCount());
            }
        }


        return canTradeCount;

    }


    /**
     * 按钮的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_newProductPostDetail_askPost:


                //判断非空
                if (edt_postCount.getText().toString() == null || edt_postCount.getText().toString().equals("")) {
                    AlertMessage(getResources().getString(R.string.newProductPostDetail_errorMsg_pleaseInputCount));
                }
                //判断是否是数字
                else if (!StringUtils.isOnlyNumber(edt_postCount.getText().toString())) {
                    AlertMessage(getResources().getString(R.string.newProductPostDetail_errorMsg_pleaseInputPositiveCount));
                }
                //判断是否在最小~最大可购数量之间
                else if (Integer.parseInt(edt_postCount.getText().toString()) > result.getPerMaxCount() || Integer.parseInt(edt_postCount.getText().toString()) < result.getPerMinCount()) {

                    AlertMessage(getResources().getString(R.string.newProductPostDetail_errorMsg_countIsOutLimit));
                }
                //判断是否在可购数量值内
                else if (Integer.parseInt(edt_postCount.getText().toString()) > getCanTradeCount()) {

                    AlertMessage(getResources().getString(R.string.newProductPostDetail_errorMsg_donnotHaveEnoughMoney));

                } else {
                    if (waitingDialog == null) {
                        waitingDialog = new WaitingDialog(context);
                    }
                    waitingDialog.show(getResources().getString(R.string.newProductPostDetail_loading_post));
                    //开始申购
                    postProduct();

                }
                break;

        }

    }


    /**
     * 弹出提示对话框
     *
     * @param message
     */
    public void AlertMessage(String message) {
        if (waitingDialog != null) {
            waitingDialog.dismiss();
        }
        //显示提示对话框
        MyAlertDialog dialog = new MyAlertDialog(context);
        dialog.tipDialog(message);
    }
}
