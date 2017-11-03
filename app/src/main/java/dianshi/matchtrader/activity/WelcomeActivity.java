package dianshi.matchtrader.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import dianshi.matchtrader.R;
import dianshi.matchtrader.constant.APPFinal;
import dianshi.matchtrader.toolbar.BaseActivity;

/**
 * Created by Administrator on 2016/5/9 0009.
 */
public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        //开启延时
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                SharedPreferences sharedPreferences = getSharedPreferences(APPFinal.ShAERD_FILE_FirstStart, Context.MODE_PRIVATE);
                if (sharedPreferences.getBoolean("isFirstStart", true)) {
                    //跳转介绍页面
                    Intent intent = new Intent(WelcomeActivity.this, IntroduceActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //跳转到登录和注册页面
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }


            }
        }.start();


    }

}
