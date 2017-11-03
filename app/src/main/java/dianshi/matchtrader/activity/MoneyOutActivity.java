package dianshi.matchtrader.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dianshi.matchtrader.model.BankSignInfoModel_out;
import com.dianshi.matchtrader.model.DoMoneyOutModel_in;
import com.dianshi.matchtrader.model.ErrorModel_out;
import com.dianshi.matchtrader.model.ModelInBase;
import com.dianshi.matchtrader.model.ResultModel_out;
import com.dianshi.matchtrader.server.FuncCall;
import com.dianshi.matchtrader.server.GlobalSingleton;

import java.util.concurrent.ConcurrentHashMap;

import dianshi.matchtrader.R;
import dianshi.matchtrader.dialog.MyAlertDialog;
import dianshi.matchtrader.dialog.WaitingDialog;
import dianshi.matchtrader.toolbar.ToolBarActivity;
import dianshi.matchtrader.view.RingView;

/**
 * Created by Administrator on 2016/5/8 0008.
 */
public class MoneyOutActivity extends ToolBarActivity implements View.OnClickListener {

    RingView ringView;
    Context context;
    String bankName;
    String signedPhone;
    String bankCardNo;
    String realName;
    WaitingDialog waitingDialog;
    TextView tv_sendCode;
    TextView tv_countTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_out);
        context = this;

        //资产百分比统计图
        //loadBankInfo();


        //绑定控件
        initView();


    }


    private void initView() {

        Button btn_outMoney = (Button) findViewById(R.id.btn_moneyOut_moneyOut);
        tv_sendCode = (TextView) findViewById(R.id.tv_money_out_send);
        tv_countTimer = (TextView) findViewById(R.id.tv_money_out_countTimer);


        btn_outMoney.setOnClickListener(this);
        tv_sendCode.setOnClickListener(this);


        //加载等待框初始化
        waitingDialog = new WaitingDialog(context);


    }

    /**
     * 各类控件的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_moneyOut_moneyOut:
                waitingDialog.show(getResources().getString(R.string.moneyOut_waitingOutMoney));
                //出金
                sendMoneyOut();
                break;
            case R.id.tv_money_out_send:
                waitingDialog.show(getResources().getString(R.string.moneyOut_waitingSendCode));
                //发送验证码
                sendCheckCode();
                break;
        }
    }


    /**
     * toolbar标题设置
     *
     * @param tv
     */
    @Override
    public void setTitle(TextView tv) {
        super.setTitle(tv);
        tv.setText(R.string.moneyOut_moneyOut);
    }

    /**
     * 发送验证码
     */
    private void sendCheckCode() {
        ConcurrentHashMap<String, String> dict = new ConcurrentHashMap<>();
        FuncCall<ModelInBase, ResultModel_out> funcCall = new FuncCall<>();
        funcCall.FuncErrHandler = sendFailHandler;
        funcCall.FuncResultHandler = sendSuccHandler;
        funcCall.Call("SendPhoneCode", new ModelInBase(), ResultModel_out.class, dict);
    }

    /**
     * 验证码倒计时
     */
    CountDownTimer timer = new CountDownTimer(30000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            int time = (int) millisUntilFinished / 1000;
            tv_countTimer.setText("(" + time + "s)" + getResources().getString(R.string.moneyOut_CountTime));
        }

        @Override
        public void onFinish() {

            tv_countTimer.setVisibility(View.GONE);
            tv_sendCode.setVisibility(View.VISIBLE);
        }
    };

    /**
     * 发送验证码成功
     */
    private Handler sendSuccHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ResultModel_out result = (ResultModel_out) msg.obj;
            //显示提示对话框
            AlertMessage(result.getMsg());

            //验证码倒计时开始
            tv_countTimer.setVisibility(View.VISIBLE);
            tv_sendCode.setVisibility(View.GONE);
            timer.start();

        }
    };
    /**
     * 发送验证码失败
     */
    private Handler sendFailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            ErrorModel_out result = (ErrorModel_out) msg.obj;
            if (result == null || result.getErrorMsg().equals("")) {
                AlertMessage(getResources().getString(R.string.moneyOut_gainVerifyCodeFail));
            } else {
                AlertMessage(result.getErrorMsg());

            }
        }
    };

    private void showBankInfo() {


        TextView tv_bankCard = (TextView) findViewById(R.id.tv_moneyIn_bankCardard);
        TextView tv_bankName = (TextView) findViewById(R.id.tv_moneyIn_bankName);
        TextView tv_realName = (TextView) findViewById(R.id.tv_moneyIn_realname);
        TextView tv_phone = (TextView) findViewById(R.id.tv_moneyIn_phoneNumber);

        tv_bankCard.setText(bankCardNo);
        tv_bankName.setText(bankCardNo);
        tv_realName.setText(realName);
        tv_phone.setText(signedPhone);
    }


    /**
     * 加载银行信息
     */
    private void loadBankInfo() {
        ConcurrentHashMap<String, String> dict = new ConcurrentHashMap<>();
        FuncCall<ModelInBase, BankSignInfoModel_out> funcCall = new FuncCall<>();
        funcCall.FuncErrHandler = loadInfoFailHandler;
        funcCall.FuncResultHandler = loadInfoSucc;
        funcCall.Call("LoadSignInfo", new ModelInBase(), BankSignInfoModel_out.class, dict);
    }

    /**
     * 加载银行信息成功
     */
    private Handler loadInfoSucc = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            BankSignInfoModel_out result = (BankSignInfoModel_out) msg.obj;
            if (result.isSigned()) {
                bankCardNo = result.getSignedBankNo();
                bankName = result.getSignedBankName();
                signedPhone = result.getSignedPhone();
                realName = result.getRealName();
                showBankInfo();
            } else {
                //显示提示对话框
                MyAlertDialog dialog = new MyAlertDialog(context);
                dialog.tipDialog("您的银行账户尚未签约，请到电脑端进行签约");
            }
        }
    };

    /**
     * 加载银行信息失败
     */
    private Handler loadInfoFailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //显示提示对话框
            MyAlertDialog dialog = new MyAlertDialog(context);
            dialog.tipDialog("银行签约信息获取失败");
        }
    };


    /**
     * 发送出金请求
     */
    private void sendMoneyOut() {
        TextView tv_money = (TextView) findViewById(R.id.edt_moneyOut_moneyOutput);
        TextView tv_code = (TextView) findViewById(R.id.edt_moneyOut_verifyCode);

        String moneyStr = tv_money.getText().toString();
        String code = tv_code.getText().toString();

        if (moneyStr == null || "".equals(moneyStr)) {
            //显示提示对话框
            AlertMessage(getResources().getString(R.string.moneyOut_PleaseInputMoneyCount));
            return;
        }
        if (code == null || "".equals(code)) {
            //显示提示对话框
            AlertMessage(getResources().getString(R.string.moneyOut_PleaseInputVerifyCode));
            return;
        }

        double money = 0;
        try {
            money = Double.valueOf(moneyStr);
        } catch (Exception ex) {

        }

        ConcurrentHashMap<String, String> dict = new ConcurrentHashMap<>();
        DoMoneyOutModel_in model_in = new DoMoneyOutModel_in();
        model_in.setMoney(money);
        model_in.setPhoneCode(code);
        FuncCall<DoMoneyOutModel_in, ResultModel_out> funcCall = new FuncCall<>();
        funcCall.FuncErrHandler = moneyOutFailHandler;
        funcCall.FuncResultHandler = moneyOutSuccHandler;

        if (GlobalSingleton.MoneyOutStyle != null) {
            if (GlobalSingleton.MoneyOutStyle.toLowerCase().equals("cmcc")) {
                funcCall.Call("CMCC_DoMoneyOut", model_in, ResultModel_out.class, dict);

            } else if (GlobalSingleton.MoneyOutStyle.toLowerCase().equals("normal")) {

                funcCall.Call("DoMoneyOut", model_in, ResultModel_out.class, dict);
            } else {
                AlertMessage("出金接口获取失败");

            }
        } else {

            AlertMessage("出金接口获取失败");
        }


    }


    /**
     * 出金成功
     */
    private Handler moneyOutSuccHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ResultModel_out result = (ResultModel_out) msg.obj;
            if (result.getIsSucc()) {
                AlertMessage("出金成功");
            } else {
                //显示提示对话框
                AlertMessage(result.getMsg());
            }
        }
    };

    /**
     * 出金失败
     */
    private Handler moneyOutFailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ErrorModel_out result = (ErrorModel_out) msg.obj;
            if (result == null || result.getErrorMsg().equals("")) {
                AlertMessage(getResources().getString(R.string.moneyOut_outMoneyFail));
            } else {
                AlertMessage(result.getErrorMsg());

            }

        }
    };


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
