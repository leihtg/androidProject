package dianshi.matchtrader.activity;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dianshi.matchtrader.server.FuncCall;
import com.dianshi.matchtrader.model.ModifySelfPasswordModel_in;
import com.dianshi.matchtrader.model.ResultModel_out;

import java.util.concurrent.ConcurrentHashMap;

import dianshi.matchtrader.R;
import dianshi.matchtrader.toolbar.ToolBarActivity;
import dianshi.matchtrader.dialog.MyAlertDialog;

/**
 * Created by Administrator on 2016/5/7 0007.
 */
public class ResetPasswordActivity extends ToolBarActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Button btn = (Button) findViewById(R.id.btn_resetPassword_enSure);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyPassword();
            }
        });

    }

    @Override
    public void setTitle(TextView tv) {
        super.setTitle(tv);
        tv.setText(R.string.resetPassword_resetPassword);
    }

    private void modifyPassword() {
        TextView tv_oldPassword = (TextView) findViewById(R.id.edt_resetPassword_inputOldPassword);
        TextView tv_password = (TextView) findViewById(R.id.edt_resetPassword_inputNewPassword);
        TextView tv_password2 = (TextView) findViewById(R.id.edt_resetPassword_inputNewPasswordAgain);

        String oldPassword = tv_oldPassword.getText().toString();
        String newPassword = tv_password.getText().toString();
        String newPassword2 = tv_password2.getText().toString();

        if (oldPassword == null || "".equals(oldPassword)) {

            //显示提示对话框
            MyAlertDialog dialog = new MyAlertDialog(context);
            dialog.tipDialog("请输入旧密码！");
            return;
        }

        if (newPassword == null || "".equals(newPassword)) {
            //显示提示对话框
            MyAlertDialog dialog = new MyAlertDialog(context);
            dialog.tipDialog("请输入新密码！");
            return;
        }
        if (!newPassword.equals(newPassword2)) {
            //显示提示对话框
            MyAlertDialog dialog = new MyAlertDialog(context);
            dialog.tipDialog("两次输入新密码不一致!");
            return;
        }

        ConcurrentHashMap<String, String> dict = new ConcurrentHashMap<>();
        ModifySelfPasswordModel_in model_in = new ModifySelfPasswordModel_in();

        model_in.setOldPassword(oldPassword);
        model_in.setPassword(newPassword);
        model_in.setPassword2(newPassword2);

        FuncCall<ModifySelfPasswordModel_in, ResultModel_out> funcCall = new FuncCall<>();
        funcCall.FuncErrHandler = loadFailHandler;
        funcCall.FuncResultHandler = loadSuccHandler;
        funcCall.Call("ModifySelfPassword", model_in, ResultModel_out.class, dict);
    }

    private Handler loadSuccHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ResultModel_out result = (ResultModel_out) msg.obj;

            //显示提示对话框
            MyAlertDialog dialog = new MyAlertDialog(context);
            dialog.tipDialog(result.getMsg());
            if (result.getIsSucc()) {
                finish();
            }
        }
    };
    private Handler loadFailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            //显示提示对话框
            MyAlertDialog dialog = new MyAlertDialog(context);
            dialog.tipDialog("密码修改失败！");
        }
    };
}
