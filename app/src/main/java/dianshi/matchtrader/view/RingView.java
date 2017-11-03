package dianshi.matchtrader.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.math.BigDecimal;

import dianshi.matchtrader.R;
import dianshi.matchtrader.util.ScreenUtils;


public class RingView extends View {

    private static final String TAG = "CircleBar";

    private RectF mColorWheelRectangle = new RectF();

    private Paint mDefaultWheelPaint;

    private Paint mColorWheelPaint;
    private float mColorWheelRadius;

    private float circleStrokeWidth;
    private float pressExtraStrokeWidth;

    /**
     * 表名所在比例的扇形图
     */
    private float mSweepAnglePer;
    private float mSweepAngle;

    /**
     * 默认的扇形图角度
     */
    private float mSweepAnglePerDefault = 360;
    private float mSweepAngleDefault = 360;

    private float temp;


    BarAnimation anim;

    public RingView(Context context) {
        super(context);
        init(null, 0);
    }

    public RingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }


    /**
     * 初始化
     *
     * @param attrs
     * @param defStyle
     */
    private void init(AttributeSet attrs, int defStyle) {

        //外面圆环的宽度
        circleStrokeWidth = ScreenUtils.dip2px(getContext(), 15);

        //按下时显示的圆环半径
        pressExtraStrokeWidth = ScreenUtils.dip2px(getContext(), 0);

        //比例扇形画笔颜色
        mColorWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColorWheelPaint.setColor(getResources().getColor(R.color.blue));
        mColorWheelPaint.setStyle(Paint.Style.STROKE);
        mColorWheelPaint.setStrokeWidth(circleStrokeWidth);

        //默认扇形画笔颜色
        mDefaultWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDefaultWheelPaint.setColor(getResources().getColor(R.color.priceRedColor));
        mDefaultWheelPaint.setStyle(Paint.Style.STROKE);
        mDefaultWheelPaint.setStrokeWidth(circleStrokeWidth);


        mSweepAngle = 0;

        //设定动画时间
        anim = new BarAnimation();
        anim.setDuration(1000);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        /**
         * 默认的圆
         */
//		canvas.drawArc(mColorWheelRectangle, 90, mSweepAnglePerDefault, false, mDefaultWheelPaint);

//		int dash = ScreenUtils.dip2px(getContext(),1);
        int dash = 0;

        //先画蓝色
        //再画红色

        /**
         * 蓝色
         * 负号是为了逆时针绘制的效果
         */
        canvas.drawArc(mColorWheelRectangle, 90 + dash, mSweepAnglePerDefault, false, mDefaultWheelPaint);
        //红色
        canvas.drawArc(mColorWheelRectangle, 90, -mSweepAnglePer, false, mColorWheelPaint);


        /**
         * 扇形圆---
         */


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = getDefaultSize(getSuggestedMinimumHeight(),
                heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        //得到控件宽度和高度两者的最小值
        int min = Math.min(width, height);

        setMeasuredDimension(min, min);

        mColorWheelRadius = min - circleStrokeWidth - pressExtraStrokeWidth;


        /**
         * 矩形的边长
         */
        mColorWheelRectangle.set(circleStrokeWidth + pressExtraStrokeWidth, circleStrokeWidth + pressExtraStrokeWidth,
                mColorWheelRadius, mColorWheelRadius);
    }


    /**
     * 开始动画
     */
    public void startCustomAnimation() {
        this.startAnimation(anim);
    }


    /**
     * 设置百分比
     *
     * @param numerator
     * @param denominator
     */
    public void setRadio(float numerator, float denominator) {

        if (denominator > 0) {
            //蓝色角度
            mSweepAngle = 360 * (numerator / denominator);
            //红色角度
            mSweepAngleDefault = 360 - mSweepAngle;
            //开始动画
            this.startAnimation(anim);

        }


    }

    /**
     * 设置百分比
     *
     * @param numerator   分子
     * @param denominator
     */
    public void setRadio(BigDecimal numerator, BigDecimal denominator) {

        if (denominator.intValue() > 0) {
            double angle = 360 * (numerator.doubleValue() / denominator.doubleValue());
            mSweepAngle = (float) angle;
            mSweepAngleDefault = 360 - mSweepAngle;
            //开始动画
            this.startAnimation(anim);
        }


    }


    /**
     * 动画设置
     *
     * @author Administrator
     */
    public class BarAnimation extends Animation {

        public BarAnimation() {

        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                mSweepAnglePer = interpolatedTime * mSweepAngle;
                mSweepAnglePerDefault = interpolatedTime * mSweepAngleDefault;
            } else {
                mSweepAnglePer = mSweepAngle;
                mSweepAnglePerDefault = mSweepAngleDefault;
            }

            //重绘
            postInvalidate();
        }
    }


}
