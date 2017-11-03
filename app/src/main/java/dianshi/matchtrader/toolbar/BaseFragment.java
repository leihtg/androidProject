package dianshi.matchtrader.toolbar;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;

/**
 * 碎片的基类，主要用于友盟的统计
 * Created by Administrator on 2016/5/19 0019.
 */
public class BaseFragment extends Fragment {

    String className;
    boolean isActivityCreated;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //得到继承该父类的子类的名称
        className = getClass().getSimpleName();


    }


    @Override
    public void onResume() {

        MobclickAgent.onPageStart(className); //统计页面
        super.onResume();

    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd(className);//统计页面
        super.onPause();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isActivityCreated = true;
        if (getUserVisibleHint()) {
            lazyLoad();
        }
    }


    /**
     * Fragment当前状态是否可见
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        //保证视图创建后加载此方法
        if (getUserVisibleHint() && isActivityCreated) {
            lazyLoad();
        }
    }


    /**
     * 延迟加载
     * 子类必须重写此方法-可以加抽象
     */
    protected void lazyLoad() {
    }

    ;
}
