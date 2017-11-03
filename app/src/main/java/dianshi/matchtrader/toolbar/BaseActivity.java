package dianshi.matchtrader.toolbar;


import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.dianshi.matchtrader.Utils.AppManager;
import com.umeng.analytics.MobclickAgent;

/**
 * 应用程序Activity的基类
 * <p>1.手动将activity加载列表中
 * <p>2.友盟统计
 *
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-9-18
 */
public class BaseActivity extends AppCompatActivity {

    // 是否允许全屏
    private boolean allowFullScreen = true;

    // 是否允许销毁
    private boolean allowDestroy = true;

    private View view;

    //子类的名称
    String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allowFullScreen = true;
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
        //得到继承该父类的子类的名称
        className = getClass().getSimpleName();


    }


    /**
     * 在activity结束之前移除所有依附于本activity的窗口View
     * 防止内存泄露
     * <p>
     * 注意:
     * 要放到destory中
     * 放到finish()中会在页面切换的时候有白屏状态
     */
    @Override
    protected void onDestroy() {

        //移除所有依附于本activity的窗口View
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();

        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);

        super.onDestroy();
    }

    public boolean isAllowFullScreen() {
        return allowFullScreen;
    }

    /**
     * 设置是否可以全屏
     *
     * @param allowFullScreen
     */
    public void setAllowFullScreen(boolean allowFullScreen) {
        this.allowFullScreen = allowFullScreen;
    }

    public void setAllowDestroy(boolean allowDestroy) {
        this.allowDestroy = allowDestroy;
    }

    public void setAllowDestroy(boolean allowDestroy, View view) {
        this.allowDestroy = allowDestroy;
        this.view = view;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && view != null) {
            view.onKeyDown(keyCode, event);
            if (!allowDestroy) {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 暂停状态
     */
    @Override
    protected void onPause() {
        super.onPause();
        //保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPageEnd(className); //统计页面
        MobclickAgent.onPause(this);          //统计时长
    }

    /**
     * 初始化数据交互状态
     */
    @Override
    protected void onResume() {
        super.onResume();
        //保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPageStart(className); //统计页面
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
