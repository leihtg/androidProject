package dianshi.matchtrader.catchexception;

import android.app.Application;

import org.xutils.x;

/**
 * 收集手机全局崩溃时的exception,并log到本地
 */
public class CrashApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //全局异常捕获
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

        //初始化XUtils3
        x.Ext.init(this);
        x.Ext.setDebug(false); //输出debug日志，开启会影响性能


    }
}