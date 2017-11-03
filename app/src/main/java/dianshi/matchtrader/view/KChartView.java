package dianshi.matchtrader.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.dianshi.matchtrader.Klines.ChartBase;
import com.dianshi.matchtrader.Klines.KLineColor;
import com.dianshi.matchtrader.Klines.KLineTextSize;
import com.dianshi.matchtrader.Klines.KLineTimeFormat;
import com.dianshi.matchtrader.Klines.KlineSafeCollection;
import com.dianshi.matchtrader.Klines.Section;
import com.dianshi.matchtrader.Utils.Util;
import com.dianshi.matchtrader.model.KLineModel;
import com.dianshi.matchtrader.model.ProductModel_out;
import com.dianshi.matchtrader.server.GlobalSingleton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dianshi.matchtrader.util.ScreenUtils;

/**
 * Created by Administrator on 2016/5/24.
 */
public class KChartView extends View {

    Context context;
    /**
     * K线信息的列表
     */
    List<KLineModel> array;

    /**
     * 时间线边框绘制画笔
     */
    Paint timeLineBorderPaint;

    /**
     * 时间线文字绘制画笔
     */
    Paint timeLineWordPaint;


    //绘制加载文字的画笔
    private Paint loadingPaint;
    //控件总宽度
    private int totalWidth;
    //控件总高度
    private int totalHight;

    //是否需要十字架定位
    private boolean isCross = false;
    //十字交叉的X值
    private int crossX = 0;
    //十字交叉的Y值
    private int crossY = 0;


    private Section mainSection;
    private Section otherSection;
    private Section quotaSection;

    private int mainSectionH = 0;
    private int otherSectionH = 0;
    private int quotaSectionH = 0;

    //margin
    private int headHight = ScreenUtils.dip2px(getContext(), 15);
    private int sectionWidth = 0;

    private int sectionLeft = ScreenUtils.dip2px(getContext(), 15);
    private int sectionTop = 1;
    private int sectionRight = ScreenUtils.dip2px(getContext(), 15);


    //padding
    private int leftOffSet = ScreenUtils.dip2px(getContext(), 10);
    private int rightOffSet = ScreenUtils.dip2px(getContext(), 10);
    private int topOffSet = ScreenUtils.dip2px(getContext(), 10);
    private int bottomOffSet = ScreenUtils.dip2px(getContext(), 10);


    //时间线高度
    private int timeLineHeight = 60;
    //两个section之间距离,y轴方向
    private int sectionYDistance = ScreenUtils.dip2px(getContext(), 0);


    //是否需要下载
    private boolean isLoading = false;

    /*
     *窗体设置
     */
    private int allKWidth = 80;
    private int kMaxWidth = 40;
    private int kMaxSpace = 30;
    private int kMinWidth = 40;
    private int kMinSpace = 30;
    //K线的宽度
    private int kWidth = 10;
    //K线之间的距离
    private int kSpace = 5;
    //开始绘制的K线数
    int startNum = 0;
    //显示K线的数据
    private int showKNum = 200;

    //跳过的未绘制的K线数目,方向是从后往前
    private int skipNum = 0;
    private int otherSectionTop = 0;
    private int quotaSectionTop = 0;


    //商品
    private ProductModel_out productModels;

    //记录两点触控之间距离值,作为缩放比较基点
    private float baseValue = 0;
    //缩放开始的最小值
    private int minMoveDistance = 15;

    //记录是否发生横竖屏切换
    private boolean isSizeChange = false;

    //是否有指标图
    boolean isHaveQuotaSection = false;

    //是否是给k线第一次传数据
    boolean isFirst = true;


    /**
     * 构造方法
     *
     * @param context
     */
    public KChartView(Context context) {
        this(context, null);
    }

    /**
     * 构造方法
     *
     * @param context
     * @param attrs
     */
    public KChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public KChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        mGestureDetector = new GestureDetector(context, new MyGestureListener());
    }


    /**
     * 设置是否是第一次绘制,保持默认的K线参数
     *
     * @param first
     */
    public void setFirst(boolean first) {
        isFirst = first;
    }


    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        this.context = context;

        //单击事件监控
        this.setOnClickListener(onClickListener);
        //长按事件监控
        this.setOnLongClickListener(onLongClickListener);
        //触摸事件监控
        this.setOnTouchListener(onTouchListener);

        //边框画笔
        timeLineBorderPaint = new Paint();
        timeLineBorderPaint.setStyle(Paint.Style.STROKE);
        timeLineBorderPaint.setStrokeWidth(1);
        timeLineBorderPaint.setColor(Color.GRAY);

        //文本画笔
        timeLineWordPaint = new Paint();
        timeLineWordPaint.setStyle(Paint.Style.FILL);
        timeLineWordPaint.setTextSize(KLineTextSize.XYTxtSize);
        timeLineWordPaint.setColor(Color.GRAY);

        //K线图
        mainSection = new Section(sectionWidth, mainSectionH, sectionLeft, sectionTop, leftOffSet, rightOffSet, topOffSet, bottomOffSet, sectionRight);
        mainSection.setMain(true);//在主K线图中

        //柱形图
        otherSection = new Section(sectionWidth, otherSectionH, sectionLeft, mainSectionH + sectionYDistance + sectionTop, leftOffSet, rightOffSet, topOffSet, bottomOffSet, sectionRight);


        //指标图
        quotaSection = new Section(sectionWidth, quotaSectionH, sectionLeft, mainSectionH + otherSectionH + 2 * sectionYDistance + sectionTop, leftOffSet, rightOffSet, topOffSet, bottomOffSet, sectionRight);


    }


    /**
     * 添加指标图
     * 改变Section的一些设置
     */
    public void setQuotaSection(boolean isHaveQuotaSection, KLineQuotaType kLineQuotaType) {

        this.isHaveQuotaSection = isHaveQuotaSection;

        if (isHaveQuotaSection) {

            quotaSection.getChartList().clear();
            quotaSection.setChartList(new ArrayList<ChartBase>());

            switch (kLineQuotaType) {
                case MACD:
                    quotaSection.addChart("MACDChart");
                    quotaSection.addChart("DIFFChart");
                    quotaSection.addChart("DEAChart");
                    break;
                case KDJ:
                    quotaSection.addChart("KDJChart");
                    break;
            }
        }

        int allSectionHight = totalHight - timeLineHeight - sectionYDistance;

        if (isHaveQuotaSection) {
            //K线图的高度 2/3
            mainSectionH = allSectionHight / 2;
            //柱形图的高度  1/3
            otherSectionH = allSectionHight / 4;
            //柱形图的高度  1/3
            quotaSectionH = allSectionHight / 4;
        } else {
            //K线图的高度 2/3
            mainSectionH = allSectionHight * 2 / 3;
            //柱形图的高度  1/3
            otherSectionH = allSectionHight / 3;
        }


        //柱形图的top
        otherSectionTop = mainSectionH + sectionTop + sectionYDistance + timeLineHeight;
        //指标图的top
        quotaSectionTop = otherSectionTop + otherSectionH + sectionYDistance;

        //宽度
        sectionWidth = totalWidth - 2;


        //重新绘制
        startDraw();

    }

    /**
     * 关闭指标图
     * 改变Section的一些设置
     */
    public void deleteQuotaSection() {

        this.isHaveQuotaSection = false;

        int allSectionHight = totalHight - timeLineHeight - sectionYDistance;

        if (isHaveQuotaSection) {
            //K线图的高度 2/3
            mainSectionH = allSectionHight / 2;
            //柱形图的高度  1/3
            otherSectionH = allSectionHight / 4;
            //柱形图的高度  1/3
            quotaSectionH = allSectionHight / 4;
        } else {
            //K线图的高度 2/3
            mainSectionH = allSectionHight * 2 / 3;
            //柱形图的高度  1/3
            otherSectionH = allSectionHight / 3;
        }


        //柱形图的top
        otherSectionTop = mainSectionH + sectionTop + sectionYDistance + timeLineHeight;
        //指标图的top
        quotaSectionTop = otherSectionTop + otherSectionH + sectionYDistance;

        //宽度
        sectionWidth = totalWidth - 2;


        //重新绘制
        startDraw();

    }

    /**
     * 重写测量函数
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //计算控件在布局中占据的高和宽
        totalHight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        totalWidth = getDefaultSize(getSuggestedMinimumHeight(), widthMeasureSpec);

        int allSectionHight = totalHight - timeLineHeight - sectionYDistance;


        if (isHaveQuotaSection) {
            //K线图的高度 2/3
            mainSectionH = allSectionHight / 2;
            //柱形图的高度  1/3
            otherSectionH = allSectionHight / 4;
            //柱形图的高度  1/3
            quotaSectionH = allSectionHight / 4;
        } else {
            //K线图的高度 2/3
            mainSectionH = allSectionHight * 2 / 3;
            //柱形图的高度  1/3
            otherSectionH = allSectionHight / 3;
        }


        //柱形图的top
        otherSectionTop = mainSectionH + sectionTop + sectionYDistance + timeLineHeight;
        //指标图的top
        quotaSectionTop = otherSectionTop + otherSectionH + sectionYDistance;


        //宽度
        sectionWidth = totalWidth - 2;


        //初始化参数
        initSection();


        //如果发生尺寸变化切换,计算尺寸,重新绘制
        if (isSizeChange) {
            computeNew();
        }

    }


    /**
     * 重写绘制函数
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(KLineColor.backgroundColor);

        if (array != null && isLoading && productModels != null) {

            //画时间线
            drawTimeLine(canvas);

            //画边界
            mainSection.drawBorder(canvas);
            otherSection.drawBorder(canvas);

            //画内容图
            mainSection.draw(canvas);
            otherSection.draw(canvas);


            if (isHaveQuotaSection) {
                quotaSection.drawBorder(canvas);
                quotaSection.draw(canvas);
            }             

            /*
             * 定位十字线
             */
            if (isCross) {
                mainSection.drawCross(canvas, crossX, crossY);
                otherSection.drawCross(canvas, crossX, crossY);
                //画价格标签
//              mainSection.drawPriceNote(canvas, crossX, crossY);

                //画时间标签
                mainSection.drawTimeNote(canvas, crossX, crossY);

                if (isHaveQuotaSection) {
                    quotaSection.drawCross(canvas, crossX, crossY);
                }
            } else {

                int startNum = array.size() - mainSection.getShowKNum() - mainSection.getSkipNum();
                int endNum = array.size() - mainSection.getSkipNum() - 1;


                //显示在最新数据
                int showX = 0;
                isMoveToLeft = false;
                if (isMoveToLeft) {
                    showX = mainSection.indexToX(startNum);
                } else {
                    showX = mainSection.indexToX(endNum);
                }


                mainSection.drawCross(canvas, showX, crossY);
                otherSection.drawCross(canvas, showX, crossY);
                //画价格标签
//              mainSection.drawPriceNote(canvas, showX, crossY);

                //画时间标签
                mainSection.drawTimeNote(canvas, showX, crossY);

                if (isHaveQuotaSection) {
                    quotaSection.drawCross(canvas, showX, crossY);
                }

            }


        } else {
            //K线列表为空,显示加载中字样在画布最中间
            if (array == null || array.size() == 0) {
                Rect rect = new Rect();
                String str = "K线加载中……";
                loadingPaint.getTextBounds(str, 0, str.length(), rect);
                canvas.drawText(str, (totalWidth - rect.width()) / 2, (this.totalHight - rect.height()) / 2, loadingPaint);
            }
        }
    }


    /**
     * 画时间线
     *
     * @param canvas
     */
    private void drawTimeLine(Canvas canvas) {
        int start = array.size() - showKNum - skipNum;
        if (start < 0) {
            start = 0;
        }


        //计算X轴所在的Y坐标值
        int y = mainSectionH;
        int startX = sectionLeft + leftOffSet;
        int endX = totalWidth - sectionRight - rightOffSet;

        //画X时间轴
        canvas.drawLine(startX, y, endX, y, timeLineBorderPaint);

        //刻度和刻度之间的距离
        int number = (endX - startX) / (kWidth + kSpace);


        //限制最小step是1
        if (number <= 0) {
            number = 1;
        }


        for (int i = start; i < array.size(); i += number / 5) {


            int x = mainSection.indexToX(i);

            //画刻度线
            canvas.drawLine(x, y, x, y + 5, timeLineBorderPaint);

            //转换Unix时间值
            Date date = Util.UnixToTime(array.get(i).getTime());
            //得到格式化后的时间
            DateFormat df = new SimpleDateFormat(KLineTimeFormat.timeFormat(GlobalSingleton.CreateInstance().PeriodTypeIndex));
            canvas.drawText(df.format(date), x, y + 35, timeLineWordPaint);
        }
    }


    //初始化Section  全局窗体参数设置
    private void initSection() {

        if (isFirst) {

            //最大和最小值
            int KViewAllWidth = (sectionWidth - rightOffSet - leftOffSet - sectionLeft - sectionRight);
            kMaxWidth = KViewAllWidth / 40;
            kMaxSpace = KViewAllWidth / 120;
            kMinWidth = KViewAllWidth / 300;
            kMinSpace = KViewAllWidth / 900;

            //初始值
            kWidth = KViewAllWidth / 80;
            kSpace = KViewAllWidth / 240;

            skipNum = 0;
        }

        isFirst = false;
        //显示K线的数目
        showKNum = (sectionWidth - rightOffSet - leftOffSet - sectionLeft - sectionRight) / (kWidth + kSpace);

        /*
        mainSection
         */
        mainSection.setWidth(sectionWidth);
        mainSection.setHeight(mainSectionH);
        mainSection.setYGridNumber(4);

        //margin
        mainSection.setHeadHight(headHight);
        mainSection.setLeft(sectionLeft);
        mainSection.setTop(sectionTop);
        mainSection.setRight(sectionRight);

        //padding
        mainSection.setLeftOffSet(leftOffSet);
        mainSection.setRightOffSet(rightOffSet);
        mainSection.setTopOffSet(topOffSet);
        mainSection.setBottomOffSet(bottomOffSet);

        //K线相关参数
        mainSection.setkWidth(kWidth);
        mainSection.setkSpace(kSpace);
        mainSection.setShowKNum(showKNum);
        mainSection.setSkipNum(skipNum);

        /*
        otherSection
         */

        otherSection.setWidth(sectionWidth);
        otherSection.setHeight(otherSectionH);
        otherSection.setYGridNumber(2);

        otherSection.setHeadHight(headHight);
        otherSection.setLeft(sectionLeft);
        otherSection.setTop(otherSectionTop);
        otherSection.setRight(sectionRight);

        otherSection.setLeftOffSet(leftOffSet);
        otherSection.setRightOffSet(rightOffSet);
        otherSection.setTopOffSet(topOffSet);
        otherSection.setBottomOffSet(bottomOffSet);

        otherSection.setkWidth(kWidth);
        otherSection.setkSpace(kSpace);
        otherSection.setSkipNum(skipNum);
        otherSection.setShowKNum(showKNum);

        /*
        quotaSection
         */

        if (isHaveQuotaSection) {

            quotaSection.setWidth(sectionWidth);
            quotaSection.setHeight(quotaSectionH);
            quotaSection.setYGridNumber(2);

            quotaSection.setHeadHight(headHight);
            quotaSection.setLeft(sectionLeft);
            quotaSection.setTop(quotaSectionTop);
            quotaSection.setRight(sectionRight);

            quotaSection.setLeftOffSet(leftOffSet);
            quotaSection.setRightOffSet(rightOffSet);
            quotaSection.setTopOffSet(topOffSet);
            quotaSection.setBottomOffSet(bottomOffSet);

            quotaSection.setkWidth(kWidth);
            quotaSection.setkSpace(kSpace);
            quotaSection.setSkipNum(skipNum);
            quotaSection.setShowKNum(showKNum);
        }


        //初始化画笔
        initPaint();
    }


    /**
     * 初始化画笔
     */
    private void initPaint() {

        //画K线加载中字样的画笔
        loadingPaint = new Paint(Paint.FAKE_BOLD_TEXT_FLAG);
        loadingPaint.setColor(Color.RED);
        loadingPaint.setStyle(Paint.Style.STROKE);
        loadingPaint.setTextSize(ScreenUtils.dip2px(getContext(), 20));
    }

    /**
     * 绘制分时图
     *
     * @param arrays
     * @param product
     * @param isTimeChart
     */
    public void setKlineCollection(final List<KLineModel> arrays, final ProductModel_out product, boolean isTimeChart, KLineQuotaType quotaType) {


        //清空mainSection中绘制的数据类型
        mainSection.getChartList().clear();
        mainSection.setChartList(new ArrayList<ChartBase>());
        otherSection.getChartList().clear();
        otherSection.setChartList(new ArrayList<ChartBase>());


        mainSection.setMain(true);//在主K线图中
        otherSection.addChart("VolChart");//成交量图


        //如果是分时图
        if (isTimeChart) {
            mainSection.addChart("TimePriceChart");//分时图
            if (quotaType != null) {
                //绘制不同类型的均线图
                switch (quotaType) {
                    case MA:
                        mainSection.addChart("MA5Chart");//MA均线图
                        otherSection.addChart("VolMA5Chart");
                        break;
                    case EMA:
                        mainSection.addChart("EMA5Chart");//EMA均线图
                        otherSection.addChart("VolEMA5Chart");
                        break;

                }
            }

        } else {

            if (quotaType != null) {
                mainSection.addChart("KChart");//蜡烛图

                //绘制不同类型的均线图
                switch (quotaType) {
                    case MA:
                        mainSection.addChart("MAChart");//MA均线图
                        otherSection.addChart("VolMAChart");
                        break;
                    case EMA:
                        mainSection.addChart("EMAChart");//EMA均线图
                        otherSection.addChart("VolEMAChart");
                        break;

                }
            }

        }

        //传递给全局变量
        this.array = arrays;
        this.productModels = product;


        startDraw();


    }

    /**
     * 开始绘制
     */
    private void startDraw() {

        //不加载
        isLoading = false;

        //得到K线的集合数据
        KlineSafeCollection kList = new KlineSafeCollection();
        kList.Set(array);

        //设置商品数据
        mainSection.setProduct(productModels);
        otherSection.setProduct(productModels);

        //设置K线数据
        mainSection.setKlineSafeCollection(kList);
        otherSection.setKlineSafeCollection(kList);

        if (isHaveQuotaSection) {
            quotaSection.setProduct(productModels);
            quotaSection.setKlineSafeCollection(kList);
        }

        //申请加载新的K线
        computeNew();
    }


    /**
     * 申请加载新的K线
     */
    private void computeNew() {

        //改变section的值
        initSection();

         /*
           这个地方要注意一定要先计算绘制的数据,然后再去计算绘制的位置
           表现在:
           先调用computeData(startNum);
           再调用computeDraw();
           否则会有可能出现数组越界
           例如 日K 切换到月K
           K线的个数变少
           未重新计算,导致KlineCollection和computeList 个数不等
                     */

        mainSection.computeData(startNum);
        mainSection.computeDraw();

        otherSection.computeData(startNum);
        otherSection.computeDraw();

        if (isHaveQuotaSection) {
            quotaSection.computeData(startNum);
            quotaSection.computeDraw();

        }

        isLoading = true;
        //重绘
        invalidate();

    }


    /**
     * K线左移
     *
     * @return
     */
    public int left(int step) {

        isMoveToLeft = true;

        if (array == null || (skipNum + step) == (array.size() - mainSection.getShowKNum())) {
            return 0;
        }
        if (skipNum + step < (array.size() - mainSection.getShowKNum())) {
            skipNum = skipNum + step;

        } else {
            skipNum = array.size() - mainSection.getShowKNum();
        }

        skipNum = skipNum > array.size() ? array.size() : skipNum;
        skipNum = skipNum < 0 ? 0 : skipNum;

        computeNew();
        return 1;


    }

    /**
     * K线右移
     *
     * @return
     */
    public int right(int step) {

        isMoveToLeft = false;
        if (array == null || mainSection.getSkipNum() - step == 0) {
            return 0;
        }

        skipNum = (mainSection.getSkipNum() - step > 0 ? (skipNum - step) : 0);


        skipNum = skipNum > array.size() ? array.size() : skipNum;
        skipNum = skipNum < 0 ? 0 : skipNum;

        computeNew();
        return 1;
    }

    /**
     * K线放大
     *
     * @return
     */
    public int zoomBig(int step) {

        if (kWidth == kMaxWidth && kSpace == kMaxSpace) {
            return 0;
        }

        int newWidth = kWidth + step;
        int newSpace = kSpace + step;

        zoom(newWidth, newSpace);

        return 1;
    }

    /**
     * K线缩小
     *
     * @return
     */
    public int zoomSmall(int step) {

        if (kWidth == kMinWidth && kSpace == kMinSpace) {
            return 0;
        }

        int newWidth = kWidth - step;
        int newSpace = kSpace - step;

        zoom(newWidth, newSpace);

        return 1;
    }


    /**
     * 点击事件
     */
    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    /**
     * 长按事件
     */
    OnLongClickListener onLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    };

    /**
     * * 判断是否有长按动作发生 * @param lastX 按下时X坐标 * @param lastY 按下时Y坐标 *
     *
     * @param thisX         移动时X坐标 *
     * @param thisY         移动时Y坐标 *
     * @param lastDownTime  按下时间 *
     * @param thisEventTime 移动时间 *
     *                      //     * @param longPressTime
     *                      //     *            判断长按时间的阀值
     */
    static boolean isLongPressed(float lastX, float lastY, float thisX,
                                 float thisY, long lastDownTime, long thisEventTime
    ) {

        float offsetX = Math.abs(thisX - lastX);
        float offsetY = Math.abs(thisY - lastY);
        long intervalTime = thisEventTime - lastDownTime;
        long longPressTime = 800;
        if (offsetX <= 10 && offsetY <= 10 && intervalTime >= longPressTime) {
            return true;
        }
        return false;
    }

    boolean isMove;
    boolean notCross;
    float lastMoveX, lastMoveY;
    boolean isMoveToLeft = false;


    /**
     * 触摸事件
     */
    private OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN://按下
                    baseValue = 0;
                    isMove = true;
                    notCross = false;
                    isCross = false;

                    lastMoveX = event.getX();
                    lastMoveY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE://移动
                    move(event);
                    break;
                case MotionEvent.ACTION_UP://抬起
                    isCross = false;
                    lastMoveX = 0;
                    lastMoveY = 0;
                    invalidate();

                    break;

            }


            return true;
        }
    };


    /**
     * 手指移动时候的操作
     *
     * @param event
     */
    public void move(MotionEvent event) {

        if (event.getPointerCount() == 2) {//两个触控点
            //非十字触控
            isCross = false;
            //计算两个点X Y之间的间距 取其对角线长度
            float x = Math.abs(event.getX(0) - event.getX(1));
            float y = Math.abs(event.getY(0) - event.getY(1));

            float value = (float) Math.sqrt(x * x + y * y);


            if (baseValue == 0) {
                baseValue = value;//记录上一个长度值
            } else {

                if (Math.abs(value - baseValue) >= minMoveDistance) {
                    //计算放大过缩小了多少
                    float rate = (value - baseValue) / Math.min(value, baseValue);
                    //计算得到缩放后的K线宽度和k线之间的距离
                    int newWidth = Math.abs(kWidth + (int) (rate));
                    int newSpace = Math.abs(kSpace + (int) (rate));
                    zoom(newWidth, newSpace);
                }
            }
        } else if (event.getPointerCount() == 1) { //一个触控点


            float nowX = event.getX();
            float nowY = event.getY();
            boolean isLongPressed = isLongPressed(lastMoveX, lastMoveY, nowX, nowY, event.getDownTime(), event.getEventTime());


            //十字光标
            if (isLongPressed && !notCross) {//长按
                isMove = false;
                isCross = true;
                //获取按下的X值和Y值
                crossX = (int) event.getX();
                crossY = (int) event.getY();
                invalidate();
            }
            //左右移动
            if (isMove) {
                isCross = false;
                //滑动
                int skipNumber = (int) ((nowX - lastMoveX)) / (kSpace + kWidth);


                //初次控制一下
                if (Math.abs(skipNumber) >= 1) {
                    notCross = true;
                }

                if (notCross) {
                    skipNumber = (int) ((nowX - lastMoveX) * 1.5) / (kSpace + kWidth);
                    if (skipNumber > 0) {//右滑--左移
                        left(skipNumber);
                    } else {//左滑--右移
                        right(Math.abs(skipNumber));
                    }
                }

            }

            lastMoveX = nowX;
            lastMoveY = nowY;

        }


    }


    /**
     * 缩放
     *
     * @param newWidth
     * @param newSpace
     */
    private void zoom(int newWidth, int newSpace) {


        //保证  kMinWidth<=newWidth<=kMaxWidth
        newWidth = newWidth >= kMinWidth ? newWidth : kMinWidth;
        newWidth = newWidth <= kMaxWidth ? newWidth : kMaxWidth;
        //保证  kMinSpace<=newSpace<=kMaxSpace
        newSpace = newSpace >= kMinSpace ? newSpace : kMinSpace;
        newSpace = newSpace <= kMaxSpace ? newSpace : kMaxSpace;

        //将计算后的值传给全局变量
        kWidth = newWidth;
        kSpace = newSpace;

        isFirst = false;
        //计算的值发生变化,重新计算绘制
        computeNew();


    }


    /**
     * 横竖屏切换
     *
     * @param newConfig
     */
    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //切换尺寸
        isSizeChange = true;
        Log.e("MSG", "onConfigurationChanged");
    }


    GestureDetector mGestureDetector;

    /**
     * 手势监听
     */
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        public MyGestureListener() {

        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

//            //滑动
//            int skipNum = (int) distanceX / (kSpace + kWidth);
//            if (skipNum > 0) {
//                right();
//            } else {
//                left();
//            }

            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

            isCross = true;
            if (isCross) {
                //获取按下的X值和Y值
                crossX = (int) e.getX();
                crossY = (int) e.getY();
                invalidate();
            }

            super.onLongPress(e);
        }
    }


}
