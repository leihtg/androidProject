package dianshi.matchtrader.view;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;

import dianshi.matchtrader.R;
import dianshi.matchtrader.util.ScreenUtils;

/**
 * 整体下拉刷新的布局
 * 兼容ListView
 * 但是限制Listview是PullRefreshView布局中的第三个控件，
 * 如果需要改动，请自行到canScroll()方法中定义
 *
 * @author DR
 */
public class PullRefreshView extends LinearLayout {
    Context context;

    //自定义接口对象
    public PullRefreshListener pullRefreshListener;

    //滑动条对象
    Scroller scroller;

    //代表刷新的头标布局中的参数
    View refreshView;
    ImageView img_arrow;
    ImageView img_loading;
    TextView tv_refreshState;
    TextView tv_refreshTime;

    int refreshTop = -50;

    //touch事件中的参数
    int lastY;
    final int MIN_PULL_DISTANCE = 20;
    boolean isRefreshing = false;

    int listviewPosotion = 2;


    /**
     * 设置Listview所在的位置
     *
     * @param listviewPosotion
     */
    public void setListviewPosotion(int listviewPosotion) {
        this.listviewPosotion = listviewPosotion;
    }

    /**
     * 是否处在正在刷新的状态
     */
    public boolean isRefreshing() {
        return isRefreshing;
    }

    /**
     * 直接进入刷新状态
     */
    public void startRefresh() {
        refresh();
    }

    /**
     * 构造方法
     *
     * @param context
     * @param attrs
     */
    public PullRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        scroller = new Scroller(context);

        // 将下拉刷新的view的margin值转换成px,(主要目的为兼容)
        refreshTop = ScreenUtils.dip2px(context, refreshTop);

        //初始化布局
        initRefreshView();
    }

    /**
     * 初始化显示刷新的头标布局，并添加到PullRefreshView中
     */
    private void initRefreshView() {
        refreshView = LayoutInflater.from(context).inflate(R.layout.header_pull_refresh, null);
        img_arrow = (ImageView) refreshView.findViewById(R.id.img_pullRefresh_arrow);
        img_loading = (ImageView) refreshView.findViewById(R.id.img_pullRefresh_loading);
        tv_refreshState = (TextView) refreshView.findViewById(R.id.tv_pullRefresh_refreshState);
        tv_refreshTime = (TextView) refreshView.findViewById(R.id.tv_pullRefresh_refreshTime);

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, -refreshTop);
        lp.topMargin = refreshTop;
        lp.gravity = Gravity.CENTER;
        refreshView.setLayoutParams(lp);

        setBackgroundColor(getResources().getColor(R.color.black));
        addView(refreshView);
    }

    /**
     * onInterceptTouchEvent判断是否将这个事件传递给子控件，即ScrollView
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int touchY = (int) ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = touchY;
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = touchY - lastY;
                // 判断拉动的距离是不是超过了最小距离,并且是向上拉动的,然后屏蔽子项
                if (moveY > MIN_PULL_DISTANCE && canScroll()) {
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                break;

            default:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 触摸事件监听，改变布局状态
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = touchY;
                break;
            case MotionEvent.ACTION_MOVE:
                // y移动坐标
                int moveY = touchY - lastY;
                //改变头标布局参数
                dropDown(moveY);
                // 将本次按下的值作为下次的开始
                this.lastY = touchY;

                break;
            case MotionEvent.ACTION_UP:
                fling();
                break;

            default:
                break;
        }

        return true;
    }

    /**
     * 判断Listview是否已经拉到了最上边
     * 已经拉到了最上边，即未滑动，则不用往下传递事件，返回true
     * 否则，要往下传递事件返回false
     * <p>
     * <p>
     * 此处是为了兼容Listview，和某一种特定的布局
     * 所以getChildAt()里面的参数是2
     * 如果布局结构改变，或者要兼容其他类型的控件，都可以在上面添加改动
     *
     * @return
     */
    private boolean canScroll() {
        View childView = null;
        if (getChildCount() > 1) {
            childView = getChildAt(listviewPosotion);//得到Listview
            if (childView instanceof ListView) { // 此处写可以兼容ListView

                if (((ListView) childView).getFirstVisiblePosition() == 0) { // 说明Listview已经能看到第一行,但是有可能listview的第一列只显示了一半

                    View firstItemView = ((ListView) childView).getChildAt(0);

                    if (firstItemView == null || (firstItemView != null && firstItemView.getTop() == 0)) {//第一列全部显示完毕
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * 通过下拉的距离改变下拉刷新的布局显示位置及参数
     *
     * @param moveY
     */
    private void dropDown(int moveY) {

        LayoutParams lp = (LayoutParams) refreshView.getLayoutParams();
        float f1 = lp.topMargin;
        float f2 = moveY * 0.2f;
        int i = (int) (f1 + f2); // 将Margin不断变大,能够看到

        if (i <= ScreenUtils.dip2px(context, 80))
            lp.topMargin = i;

        //修改后刷新
        refreshView.setLayoutParams(lp);

        refreshView.invalidate();//进行子View重新绘制
        invalidate(); // 进行整个控件的重新绘制

        //当把下拉刷新的布局全部显示出来时====可松手刷新
        //否则===仍显示正在下拉刷新
        if (lp.topMargin > 0) {
            //可松手刷新
            releaseToRefresh();
        } else {
            //下拉刷新
            pullToRefresh();

        }

    }

    /**
     * 下拉松开后,调用此函数,用来判断是刷新还是复位
     */
    private void fling() {
        LayoutParams lp = (LayoutParams) refreshView.getLayoutParams();
        if (lp.topMargin > 0) {
            //正在刷新
            refresh();
        } else {
            //松开返回原位
            recoverView();
        }
    }

    /**
     * 下拉刷新
     */
    private void pullToRefresh() {
        isRefreshing = false;
        img_arrow.setVisibility(View.VISIBLE);
        img_loading.clearAnimation();
        img_loading.setVisibility(View.GONE);
        tv_refreshState.setText(R.string.pullRefresh_pull);
        if (tv_refreshState.getText().equals(R.string.pullRefresh_release)) {
            ObjectAnimator.ofFloat(img_arrow, "rotation", 180, 0).setDuration(500).start();
        }
    }

    /**
     * 释放立刻刷新
     */
    private void releaseToRefresh() {
        isRefreshing = false;

        img_arrow.setVisibility(View.VISIBLE);
        img_loading.clearAnimation();
        img_loading.setVisibility(View.GONE);
        tv_refreshState.setText(R.string.pullRefresh_release);
        ObjectAnimator.ofFloat(img_arrow, "rotation", 180, 0).setDuration(500).start();
    }

    /**
     * 松开刷新
     */
    private void refresh() {
        isRefreshing = true;

        //将头布局完全展示出来
        LayoutParams lp = (LayoutParams) this.refreshView.getLayoutParams();
        int startY = lp.topMargin;
        //隐藏箭头，显示正在加载图片
        img_arrow.setVisibility(View.GONE);
        img_loading.setVisibility(View.VISIBLE);
        //给图片添加动态效果
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.loading_dialog_progressbar);
        img_loading.setAnimation(anim);
        //添加状态说明文字
        tv_refreshState.setText(R.string.pullRefresh_refreshing);


        //开始滚动
        scroller.startScroll(0, startY, 0, 0 - startY);
        invalidate();


        //触发自定义接口触发
        if (pullRefreshListener != null) {
            pullRefreshListener.onRefresh(this);
        }

    }

    /**
     * 松开恢复原位
     */
    private void recoverView() {
        isRefreshing = false;
        LayoutParams lp = (LayoutParams) this.refreshView.getLayoutParams();
        int startY = lp.topMargin;
        scroller.startScroll(0, startY, 0, refreshTop); //调用startScroll将会触发computeScroll函数
        invalidate();

    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {//scroll 动作还未结束
            int i = this.scroller.getCurrY();
            LayoutParams lp = (LayoutParams) refreshView.getLayoutParams();
            int k = Math.max(i, refreshTop); //返回2个值中最大的一个
            lp.topMargin = k;
            this.refreshView.setLayoutParams(lp);
            postInvalidate();
        }
        super.computeScroll();
    }

    /**
     * 刷新结束
     */
    public void finishRefresh() {

        isRefreshing = false;
        tv_refreshState.setText(R.string.pullRefresh_success);
        img_loading.setVisibility(View.GONE);

        LayoutParams lp = (LayoutParams) this.refreshView
                .getLayoutParams();
        int startY = lp.topMargin;

        scroller.startScroll(0, startY, 0, refreshTop);
        invalidate();
    }

    /**
     * 自定义事件
     */

    public void setOnRefreshListener(PullRefreshListener listener) {
        this.pullRefreshListener = listener;
    }


    /**
     * 自定义 接口
     */
    public interface PullRefreshListener {
        public void onRefresh(PullRefreshView view);
    }


}
