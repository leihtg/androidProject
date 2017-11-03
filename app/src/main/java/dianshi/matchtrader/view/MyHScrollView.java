package dianshi.matchtrader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.HorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

import dianshi.matchtrader.util.ScreenUtils;

/*
 * 自定义的 滚动控件
 * 重载了 onScrollChanged（滚动条变化）,监听每次的变化通知给 观察(此变化的)观察者
 * 可使用 AddOnScrollChangedListener 来订阅本控件的 滚动条变化
 * */
public class MyHScrollView extends HorizontalScrollView {
    ScrollViewObserver mScrollViewObserver = new ScrollViewObserver();

    Context context;

    public MyHScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    public MyHScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    public MyHScrollView(Context context) {
        super(context);
        this.context = context;
        // TODO Auto-generated constructor stub
    }


    float y = 0.0f;
    float orilY = 0.0f;

    float x = 0.0f;
    float orilX = 0.0f;


    //接口声明
    private ICanClick iCanClick;

    /**
     * 自定义接口暴露方法
     *
     * @param iCanClick
     */
    public void setIOK(ICanClick iCanClick) {
        this.iCanClick = iCanClick;
    }


    /**
     * 自定义接口
     */
    public interface ICanClick {
        void canClick(boolean isCanClick);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean ff = false;
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            //记录手指按下时的坐标值
            orilY = ev.getY();
            orilX = ev.getX();
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            y = ev.getY();
            x = ev.getX();
            //记录是否有滑动发生
            float distanceY = Math.abs(y - orilY);
            float distanceX = Math.abs(x - orilX);


            //判断纵向的滑动
            if (distanceY <= ScreenUtils.dip2px(context, 3) && distanceX <= ScreenUtils.dip2px(context, 10)) {
                if (iCanClick != null) {
                    iCanClick.canClick(true);
                }
            }
        } else {
            if (iCanClick != null) {
                iCanClick.canClick(false);
            }


        }
        return super.onTouchEvent(ev);

    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        /*
         * 当滚动条移动后，引发 滚动事件。通知给观察者，观察者会传达给其他的。
		 */
        if (mScrollViewObserver != null /*&& (l != oldl || t != oldt)*/) {
            mScrollViewObserver.NotifyOnScrollChanged(l, t, oldl, oldt);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /*
     * 订阅 本控件 的 滚动条变化事件
     * */
    public void AddOnScrollChangedListener(OnScrollChangedListener listener) {
        mScrollViewObserver.AddOnScrollChangedListener(listener);
    }

    /*
     * 取消 订阅 本控件 的 滚动条变化事件
     * */
    public void RemoveOnScrollChangedListener(OnScrollChangedListener listener) {
        mScrollViewObserver.RemoveOnScrollChangedListener(listener);
    }

    /*
     * 当发生了滚动事件时
     */
    public interface OnScrollChangedListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    /*
     * 观察者
     */
    public static class ScrollViewObserver {
        List<OnScrollChangedListener> mList;


        public ScrollViewObserver() {
            super();
            mList = new ArrayList<OnScrollChangedListener>();
        }

        public void AddOnScrollChangedListener(OnScrollChangedListener listener) {
            mList.add(listener);
        }

        public void RemoveOnScrollChangedListener(
                OnScrollChangedListener listener) {
            mList.remove(listener);
        }

        public void NotifyOnScrollChanged(int l, int t, int oldl, int oldt) {
            if (mList == null || mList.size() == 0) {
                return;
            }
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i) != null) {
                    mList.get(i).onScrollChanged(l, t, oldl, oldt);
                }
            }
        }
    }
}
