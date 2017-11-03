package dianshi.matchtrader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/*
 *
 * 一个视图容器控件
 * 阻止 拦截 ontouch事件传递给其子控件
 * */
public class InterceptScrollContainer extends LinearLayout {

    public InterceptScrollContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public InterceptScrollContainer(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
//
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		// TODO Auto-generated method stub
//		//return super.dispatchTouchEvent(ev);
//		Log.i("pdwy","ScrollContainer dispatchTouchEvent");
//		return true;
//	}

    /**
     * 将子View的touch事件给拦截了
     * 原因在于：
     * listview的item包含horizonScrollView,他们各自都可以滑动，而且各不影响，导致的结果就会很乱，队列不整齐，列名和数据可能是对不上号的
     * 所以我们要传递给它的父布局，来统一管理listview的hcrollView 滑动
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        //return super.onInterceptTouchEvent(ev);
        Log.i("pdwy", "ScrollContainer onInterceptTouchEvent");
        return true;
//		return false;
        //return super.onInterceptTouchEvent(ev);
    }

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		Log.i("pdwy","ScrollContainer onTouchEvent");
//		return true;
//	}
}

