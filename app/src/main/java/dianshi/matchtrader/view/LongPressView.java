package dianshi.matchtrader.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.lang.ref.WeakReference;


/**
 * 长按连续响应的Button
 * Created by admin on 15-6-1.
 */
public class LongPressView extends ImageView {
    /**
     * 长按连续响应的监听，长按时将会多次调用该接口中的方法直到长按结束
     */
    private LongClickRepeatListener repeatListener;
    /**
     * 间隔时间（ms）
     */
    private long intervalTime;

    private MyHandler handler;

    /**
     * view的值
     */
    static View view;

    public LongPressView(Context context) {
        super(context);

        init();
    }

    public LongPressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public LongPressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    /**
     * 初始化监听
     */
    private void init() {
        handler = new MyHandler(this);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                view = v;
                new Thread(new LongClickThread()).start();
                return true;
            }
        });
    }

    /**
     * 长按时，该线程将会启动
     */
    private class LongClickThread implements Runnable {
        private int num;

        @Override
        public void run() {
            while (LongPressView.this.isPressed()) {
                num++;
                if (num % 5 == 0) {
                    handler.sendEmptyMessage(1);
                }

                SystemClock.sleep(intervalTime / 5);
            }
        }
    }

    /**
     * 通过handler，使监听的事件响应在主线程中进行
     */
    private static class MyHandler extends Handler {
        private WeakReference ref;

        MyHandler(LongPressView longPressView) {
            ref = new WeakReference<>(longPressView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            LongPressView longPressView = (LongPressView) ref.get();
            if (longPressView != null && longPressView.repeatListener != null) {
                longPressView.repeatListener.repeatAction(view);
            }
        }
    }

    /**
     * 设置长按连续响应的监听和间隔时间，长按时将会多次调用该接口中的方法直到长按结束
     *
     * @param listener     监听
     * @param intervalTime 间隔时间（ms）
     */
    public void setLongClickRepeatListener(LongClickRepeatListener listener, long intervalTime) {
        this.repeatListener = listener;
        this.intervalTime = intervalTime;
    }

    /**
     * 设置长按连续响应的监听（使用默认间隔时间100ms），长按时将会多次调用该接口中的方法直到长按结束
     *
     * @param listener 监听
     */
    public void setLongClickRepeatListener(LongClickRepeatListener listener) {
        setLongClickRepeatListener(listener, 100);
    }


    /**
     * 定义的长按接口
     */
    public interface LongClickRepeatListener {
        void repeatAction(View v);
    }
}
