package com.dianshi.matchtrader.Klines;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;

import com.dianshi.matchtrader.Utils.MathUtil;
import com.dianshi.matchtrader.Utils.Util;
import com.dianshi.matchtrader.model.KLineModel;
import com.dianshi.matchtrader.model.ProductModel_out;
import com.dianshi.matchtrader.server.GlobalSingleton;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 坐标系中数据
 */
public class Section {
    private int decimalPlace = 0;
    private int periodIndex = 0;
    private boolean isMain;

    private double maxValue;
    private double minValue;
    private double rate;

    private int wordLeftOffset;
    private KlineSafeCollection klineSafeCollection;

    /**
     * 窗体设置
     */
    private int skipNum = 0;
    //显示的K线数目
    private int showKNum = 100;
    //K线坐标系距离控件上部的距离
    private int headHight = 0;
    //距离右侧的距离
    private int right;
    //K线的宽度
    private int kWidth = 4;
    private int kSpace = 2;
    //X轴长度
    private int width;
    //Y轴长度
    private double height;
    //左侧距离
    private int left;
    //顶部距离
    private int top;
    private int leftOffSet;
    private int rightOffSet;
    private int topOffSet;
    private int bottomOffSet;

    //网格线的条数
    private int YGridNumber;


    //XY轴的长度
    float XLength;
    float YLength;


    //Object类
    private Object chartObj = new Object();
    //K线图
    private KChart KChart = new KChart();
    //图形列表
    private List<ChartBase> chartList = new ArrayList<>();


    //商品
    private ProductModel_out product;


    public int getYGridNumber() {
        return YGridNumber;
    }

    public void setYGridNumber(int YGridNumber) {
        this.YGridNumber = YGridNumber;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public int getWordLeftOffset() {
        return wordLeftOffset;
    }

    public void setWordLeftOffset(int wordLeftOffset) {
        this.wordLeftOffset = wordLeftOffset;
    }

    public KlineSafeCollection getKlineSafeCollection() {
        return klineSafeCollection;
    }

    public void setKlineSafeCollection(KlineSafeCollection klineSafeCollection) {
        this.klineSafeCollection = klineSafeCollection;
    }

    public int getSkipNum() {
        return skipNum;
    }

    public void setSkipNum(int skipNum) {
        this.skipNum = skipNum;
    }

    public int getShowKNum() {
        return showKNum;
    }

    public void setShowKNum(int showKNum) {
        this.showKNum = showKNum;
    }

    public int getHeadHight() {
        return headHight;
    }

    public void setHeadHight(int headHight) {
        this.headHight = headHight;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getkWidth() {
        return kWidth;
    }

    public void setkWidth(int kWidth) {
        this.kWidth = kWidth;
    }

    public int getkSpace() {
        return kSpace;
    }

    public void setkSpace(int kSpace) {
        this.kSpace = kSpace;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getLeftOffSet() {
        return leftOffSet;
    }

    public void setLeftOffSet(int leftOffSet) {
        this.leftOffSet = leftOffSet;
    }

    public int getRightOffSet() {
        return rightOffSet;
    }

    public void setRightOffSet(int rightOffSet) {
        this.rightOffSet = rightOffSet;
    }

    public int getTopOffSet() {
        return topOffSet;
    }

    public void setTopOffSet(int topOffSet) {
        this.topOffSet = topOffSet;
    }

    public int getBottomOffSet() {
        return bottomOffSet;
    }

    public void setBottomOffSet(int bottomOffSet) {
        this.bottomOffSet = bottomOffSet;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getPeriodIndex() {
        return periodIndex;
    }

    public void setPeriodIndex(int periodIndex) {
        this.periodIndex = periodIndex;
    }

    public ProductModel_out getProduct() {
        return product;
    }

    public void setProduct(ProductModel_out product) {
        this.product = product;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public void setChartList(List<ChartBase> list) {
        synchronized (chartObj) {
            chartList = list;
        }
    }

    /**
     * 得到ChartBase的列表
     *
     * @return
     */
    public List<ChartBase> getChartList() {
        List<ChartBase> result = new ArrayList<>();
        synchronized (chartObj) {
            for (ChartBase item : chartList) {
                result.add(item);
            }
        }
        return result;
    }


    /**
     * 通过名称 添加图表
     *
     * @param chartName
     * @return
     */
    public AddChartResult addChart(String chartName) {
        AddChartResult result = AddChartResult.Enough;
        synchronized (chartObj) {
            /*
            K线图
            MA5,MA10,MA30,
            成交量柱形图
            分时-MA5
            分时
             */
            if (chartList.size() < 10) {//这个数量可以限制显示的图表数量


                //遍历图表列表
                for (ChartBase k : chartList) {
                    if (chartName.equals(k.getName())) {//判断图表是否已经存在
                        result = AddChartResult.AlreadyExist;
                        break;
                    }
                }

                //没存在
                if (result != AddChartResult.AlreadyExist) {


                    //创建一个图表
                    ChartBase chart = ChartFactory.ProduceChart(chartName);

                    if (chart != null) {
                        chart.setParent(this);
                        chartList.add(chart);
                        if (chartName.toLowerCase().equals("kchart")) {
                            KChart = (KChart) chart;
                        }
                        result = AddChartResult.Success;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 添加图表
     *
     * @param chart
     * @return
     */
    public AddChartResult addChart(ChartBase chart) {
        AddChartResult result = AddChartResult.Enough;
        synchronized (chartObj) {
            if (chartList.size() < 5) {
                for (ChartBase k : chartList) {
                    if (chart.getName().equals(k.getName())) {
                        result = AddChartResult.AlreadyExist;
                        break;
                    }
                }

                if (result != AddChartResult.AlreadyExist) {
                    if (chart != null) {
                        chart.setParent(this);
                        chartList.add(chart);

                        if (!chart.getName().isEmpty() && chart.getName().toLowerCase().equals("kchart")) {
                            KChart = (KChart) chart;
                        }
                        result = AddChartResult.Success;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 通过名称移除图表
     *
     * @param chartName
     */
    public void removeChart(String chartName) {
        List<ChartBase> charts = getChartList();
        for (ChartBase item : charts) {
            if (item.getName().equals(chartName)) {
                synchronized (chartObj) {
                    chartList.remove(item);
                }
                break;
            }
        }
    }


    /**
     * 构造方法
     *
     * @param width
     * @param height
     * @param left
     * @param top
     * @param leftOffSet
     * @param rightOffSet
     * @param topOffSet
     * @param bottomOffSet
     * @param right
     */
    public Section(int width, int height, int left, int top,
                   int leftOffSet, int rightOffSet, int topOffSet, int bottomOffSet, int right) {
        this.width = width;
        this.height = height;
        this.left = left;
        this.top = top;
        this.leftOffSet = leftOffSet;
        this.rightOffSet = rightOffSet;
        this.topOffSet = topOffSet;
        this.bottomOffSet = bottomOffSet;
        this.right = right;


        XLength = width - leftOffSet - left - right - rightOffSet;
        YLength = (float) (height - topOffSet - rightOffSet - headHight);

        //初始化画笔
        initPaint();
    }

    /**
     * 计算比例-计算大小
     */
    public boolean computeRate() {
        boolean result = false;
        maxValue = Double.MIN_VALUE;
        minValue = Double.MAX_VALUE;




        decimalPlace = 0;

        List<ChartBase> ChartList = getChartList();
        //计算要显示额这部分数据的最大值和最小值
        for (ChartBase c : ChartList) {
            //每一个图表都会定义自己的保留位数,这里作统一管理
            decimalPlace = Math.max(decimalPlace, c.DecimalPlace);
            c.computeMaxMin();
        }

        //对特殊情况下的的大小值进行维护 例如:没有K线数据的时候
        if(minValue == Double.MAX_VALUE || maxValue == Double.MIN_VALUE){
            maxValue = minValue =0;
        }


        //计算一个像素所在大小的比例
        YLength = (float) (height - topOffSet - rightOffSet - headHight);
        rate = YLength / (maxValue - minValue);

        result = true;


        return result;
    }

    /**
     * 计算数据,主要是给各个chart的Computelist,drawTypeList等赋值
     *
     * @param start
     */
    public void computeData(int start) {
        List<ChartBase> ChartList = getChartList();
        for (ChartBase c : ChartList) {
            c.compute(start);
        }

    }


    /**
     * 绘制
     */
    public void computeDraw() {
        List<ChartBase> ChartList = getChartList();
        if (computeRate()) {
            for (ChartBase c : ChartList) {
                c.computeDraw();
            }
        } else {
            for (ChartBase c : ChartList) {
                c.initParameter();
            }
        }
    }

    /**
     * 坐标系转化
     */
    public int indexToX(int index) {
        int result = 0;
        List<KLineModel> KLineCollection = getKlineSafeCollection().GetAll();
        int start = KLineCollection.size() - showKNum - skipNum;
        if (start < 0) {
            start = 0;
        }

        result = ((index - start) * (kWidth + kSpace) + leftOffSet + (kWidth + kSpace) / 2 + left);
        return result;
    }


    /**
     * 从触摸的x坐标计算出k线的下标
     * @param x
     * @return
     */
    public int xToIndex(int x) {
        int result = 0;

        List<KLineModel> KLineCollection = getKlineSafeCollection().GetAll();
        int start = KLineCollection.size() - showKNum - skipNum;
        int end = KLineCollection.size() -skipNum;

        if (start < 0) {
            start = 0;
        }

        int offsetX = x - leftOffSet - left - kWidth - kSpace / 2;
        if (offsetX > 0) {
            if (offsetX % (kWidth + kSpace) == 0) {
                result = offsetX / (kWidth + kSpace) + start;
            } else {
                result = offsetX / (kWidth + kSpace) + 1 + start;
            }
        }

        if (result >= end) {
            result =end - 1;
        }

        if (result <start){
            result =start;
        }
        return result;
    }

    /**
     * X轴坐标转换成时间
     *
     * @param x
     * @return
     */
    public int xToTime(int x) {
        int result = 0;
        int index = 0;
        int offsetX = x - leftOffSet - left - kWidth - kSpace / 2;

        if (offsetX > 0) {
            if (offsetX % (kWidth + kSpace) == 0) {
                index = offsetX / (kWidth + kSpace);
            } else {
                index = offsetX / (kWidth + kSpace) + 1;
            }
        }

        List<KLineModel> KLineCollection = getKlineSafeCollection().GetAll();
        int start = KLineCollection.size() - showKNum - skipNum;
        if (start < 0) {
            start = 0;
        }

        if (start + index < KLineCollection.size()) {
            result = KLineCollection.get(start + index).getTime();
        } else {
            result = KLineCollection.get(KLineCollection.size() - 1).getTime();
        }
        return result;
    }


    /**
     * 时间转换成X轴坐标
     *
     * @param time
     * @return
     */
    public int timeToX(int time) {
        int result = 0;
        int index = 0;
        List<KLineModel> KLineCollection = getKlineSafeCollection().GetAll();
        int start = KLineCollection.size() - showKNum - skipNum;
        if (start < 0) {
            start = 0;
        }
        for (int i = start; i < KLineCollection.size(); i++) {
            if (KLineCollection.get(i).getTime() == time) {
                index = i;
                break;
            }
        }

        result = (index - start) * (kWidth + kSpace) + leftOffSet + left + kWidth / 2;
        return result;
    }


    /**
     * 时间转换为index,这是是该时间点显示的K线在集合中的下标
     *
     * @param time
     * @return
     */
    public int timeToIndex(int time) {
        int result = 0;
        List<KLineModel> KLineCollection = getKlineSafeCollection().GetAll();
        int start = KLineCollection.size() - skipNum - showKNum;
        if (start < 0) {
            start = 0;
        }

        for (int i = start; i < KLineCollection.size(); i++) {
            if (KLineCollection.get(i).getTime() == time) {
                result = i;
                break;
            }
        }
        return result;
    }

    /**
     * 价值转换为Y轴坐标
     *
     * @param value
     * @return
     */
    public double valueToY(double value) {


        double result = 0;
        if (minValue != Double.MAX_VALUE && maxValue != Double.MIN_VALUE) {


            if (maxValue > minValue) {
                result = (double) height + top - bottomOffSet - (value - minValue) * rate;
            }
        }


        return result;


    }


    /**
     * Y轴坐标转换为价值
     *
     * @param y
     * @return
     */
    public double yToValue(int y) {
        double result = -1;

        if (maxValue > minValue && rate != 0) {
            BigDecimal b1 = BigDecimal.valueOf((top + height - bottomOffSet - y));
            BigDecimal b2 = BigDecimal.valueOf(rate);
            b1 = b1.divide(b2, 4);
            result = b1.doubleValue() + minValue;

        }


        return result;
    }

    /**
     * 绘制图形的各个画笔
     */
    private Paint borderPaint;
    private Paint fillPaint;
    private Paint borderValuePaint;
    private Paint gridPaint;
    private Paint crossPaint;
    private Paint crossFillPaint;
    private Paint crossTipPaint;
    private Paint priceNotePaint;
    private Paint priceNoteFillPaint;
    private Paint priceNoteTitlePaint;
    private Paint priceNoteBodyPaint;
    private Paint priceNoteWordPaint;
    private Paint priceNoteWordRedPaint;
    private Paint priceNoteWordGreenPaint;

    public void initPaint() {

        /**
         * borderPaint 边界画笔
         */

        //空心,用于画边框
        borderPaint = new Paint();
        borderPaint.setColor(Color.GRAY);
        borderPaint.setStrokeWidth(1.2f);
        borderPaint.setStyle(Paint.Style.STROKE);

        //实心画笔,用于画背景
        fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(Color.BLACK);

        //用于画文字
        borderValuePaint = new Paint();
        borderValuePaint.setColor(KLineColor.mainTextColor);
        borderValuePaint.setTextSize(KLineTextSize.XYTxtSize);
        borderValuePaint.setStyle(Paint.Style.FILL);
        /**
         * gridPaint 网格画笔
         */
        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        gridPaint.setColor(KLineColor.gridColor);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(1);
        PathEffect effects = new DashPathEffect(new float[]{5, 5}, 1);
        gridPaint.setPathEffect(effects);


        /**
         * crossPaint
         */
        crossPaint = new Paint();
        crossPaint.setStrokeWidth(KLineTextSize.crossLineWidth);
        crossPaint.setColor(KLineColor.crossLineColor);
        crossPaint.setStyle(Paint.Style.STROKE);

        /**
         * 十字定位的那个方块图
         */
        crossFillPaint = new Paint();
        crossFillPaint.setColor(Color.BLUE);
        crossFillPaint.setStyle(Paint.Style.FILL);

        /**
         * 十字定位随X轴移动提示的价格值
         */
        crossTipPaint = new Paint();
        crossTipPaint.setTextSize(KLineTextSize.crossYValueTxtSize);
        crossTipPaint.setColor(Color.WHITE);

        /**
         * priceNote  价格标签画笔
         */
        //整体颜色
        priceNotePaint = new Paint();
        priceNotePaint.setColor(KLineColor.ma30Color);
        priceNotePaint.setStyle(Paint.Style.STROKE);

        //标题背景颜色
        priceNoteFillPaint = new Paint();
        priceNoteFillPaint.setColor(KLineColor.ma30Color);
        priceNoteFillPaint.setStyle(Paint.Style.FILL);

        //标题文字背景颜色
        priceNoteTitlePaint = new Paint();
        priceNoteTitlePaint.setTextSize(KLineTextSize.priceNoteTxtSize);
        priceNoteTitlePaint.setColor(Color.WHITE);
        priceNoteTitlePaint.setStyle(Paint.Style.STROKE);

        //下方价格背景颜色
        priceNoteBodyPaint = new Paint();
        priceNoteBodyPaint.setStyle(Paint.Style.FILL);
        priceNoteBodyPaint.setColor(KLineColor.priceNoteBackgroundColor);

        priceNoteWordPaint = new Paint();
        priceNoteWordPaint.setStyle(Paint.Style.STROKE);
        priceNoteWordPaint.setColor(KLineColor.increaseColor);
        priceNoteWordPaint.setTextSize(20);

        priceNoteWordRedPaint = new Paint();
        priceNoteWordRedPaint.setStyle(Paint.Style.STROKE);
        priceNoteWordRedPaint.setColor(KLineColor.increaseColor);
        priceNoteWordRedPaint.setTextSize(20);

        priceNoteWordGreenPaint = new Paint();
        priceNoteWordGreenPaint.setStyle(Paint.Style.STROKE);
        priceNoteWordGreenPaint.setColor(KLineColor.decreaseColor);
        priceNoteWordGreenPaint.setTextSize(KLineTextSize.priceNoteTxtSize);


    }

    /**
     * 画边界和画Y轴刻度
     *
     * @param canvas
     */
    public void drawBorder(Canvas canvas) {

        //画section边框--竖线
//        canvas.drawRect(0,top,width,(float)(height + top),borderPaint);

        //画背景,加1防止覆盖
//        canvas.drawRect(1,top+1,width-1,(float)(height + top)-1,fillPaint);


        XLength = width - leftOffSet - left - right - rightOffSet;
        YLength = (float) (height - topOffSet - rightOffSet - headHight);

        //Y轴的原点和最小值的像素值(这点计算的时候要注意 this.top的添加,因为othersection 的 起始位置不是0,而是this.top)
        float start = this.top + headHight + topOffSet;
        float end = (float) (this.top + height - bottomOffSet);

        //X轴的原点和最大值的像素值
        int x1 = this.left + this.leftOffSet;
        int x2 = this.width - this.rightOffSet - this.right;


        int minGrid = (int) ((end - start) / YGridNumber);

        //画显示图形边框--
//        canvas.drawRect(left+leftOffSet,start,(left+leftOffSet +XLength),end,borderPaint);


        /*
         * 画Y轴刻度
         */
        if (product != null && klineSafeCollection != null) {
            for (float i = 0; i <= YGridNumber; i++) {
                float y = start + i * minGrid;
                //网格线
                Path path = new Path();
                path.moveTo(x1, y);
                path.lineTo(x2, y);
                canvas.drawPath(path, gridPaint);

                if (i % 2 == 0) {

                    //通过像素值推算刻度上显示的价格数字
                    double v = yToValue((int) y);
                    /*
                    加此判断的意义在于因为minGrid精度的关系,会造成可能循环到最后一个值的时候和end有一点差距,所以获取对应的的值不是最小值
                    坐标轴两端应该显示最大值和最小值
                    这里就是保证显示的刻度是最小值
                     */
                    if (Math.abs((end - y)) < minGrid) {
                        v = minValue;
                    }

                    Rect rect = new Rect();
                    String value = getValueFormat(v);
                    int length = value.length();
                    borderValuePaint.getTextBounds(value, 0, length, rect);
                    canvas.drawText(value, 0, y+rect.height()/2, borderValuePaint);
//                    if (x1+rect.width()-5 >(left+leftOffSet)){
//                        canvas.drawText(getValueFormat(v), 0, i, borderValuePaint);
//                    }else{
//                        borderValuePaint.setTextSize(borderValuePaint.getTextSize()-5);
//                        canvas.drawText(getValueFormat(v), 0, i, borderValuePaint);
//                    }

                }

            }

        }

    }


    /**
     * 画数据图
     *
     * @param canvas
     */
    public void draw(Canvas canvas) {

        //有几个图表
        List<ChartBase> ChartList = getChartList();
        for (ChartBase c : ChartList) {
            c.draw(canvas);
        }
    }






    /**
     * 价格标签
     *
     * @param canvas
     * @param x
     * @param y
     */
    public void drawPriceNote(Canvas canvas, int x, int y) {
        if (isMain && KChart != null) {
            List<KLineModel> KLineCollection = getKlineSafeCollection().GetAll();
            int start = KLineCollection.size() - showKNum - skipNum;
            if (start < 0) {
                start = 0;
            }
            //计算触摸的X坐标轴位置,得到应该显示在该位置的K线下标
            int index = xToIndex(x);

            //得到触摸的Y坐标轴位置大概对应的价格
            double _min = yToValue(y - 2);
            double _max = yToValue(y + 2);

            if (_min > 0 && _max > 0 && index >= 0 && index < KLineCollection.size()) {
                KLineModel k = KLineCollection.get(index);

//                if(k != null && _min >= k.getLow() && _max <= k.getHigh()){
//                    PaintPriceNote(canvas,x,y,k);
//                }
                //只要竖线所在的范围有K线数据,就能查看该时刻的K线信息
                if (k != null) {
                    PaintPriceNote(canvas, x, y, k);
                }
            }
        }
    }

    private int priceNoteWidth = 260;
    private int priceNoteHeigh = 300;
    private int priceNoteHead = 35;
    private int wordOff = 35;

    /**
     * 绘制价格标签
     *
     * @param canvas
     * @param x
     * @param y
     * @param kline
     */
    private void PaintPriceNote(Canvas canvas, int x, int y, KLineModel kline) {

        //预留参数控制价格标签显示的位置
        boolean isLeft = true;

        //计算触摸的X坐标轴位置,得到应该显示在该位置的K线下标
        KLineModel kLineModel = klineSafeCollection.GetAll().get(xToIndex(x));
        //竖线只定位在k线上面
        int crossX = timeToX(kLineModel.getTime());
        if (crossX > (width + right + left) / 2) {
            isLeft = false;
        }

        //价格标签的位置
        int startX = left + leftOffSet;
        int startY = top + headHight + topOffSet;
        if (isLeft) {
            //显示在右边
            startX = width - priceNoteWidth - rightOffSet - right;
        }


        boolean isUp = kline.getClose() >= kline.getOpen();//是否是阳线

        //画矩形
        canvas.drawRect(startX, startY, startX + priceNoteWidth, startY + priceNoteHeigh, priceNotePaint);//整体颜色
        canvas.drawRect(startX, startY, startX + priceNoteWidth, startY + priceNoteHead, priceNoteFillPaint);//头部颜色
        //画标题下的黑色矩形
        canvas.drawRect(startX + 1, startY + priceNoteHead, startX + priceNoteWidth - 1, startY + priceNoteHeigh - 1, priceNoteBodyPaint);


        Date date = Util.UnixToTime(kline.getTime());
        DateFormat format = new SimpleDateFormat(KLineTimeFormat.timeFormat(GlobalSingleton.CreateInstance().PeriodTypeIndex));

        //画日期
        canvas.drawText(format.format(date), startX + 2, startY + priceNoteHead - 3, priceNoteTitlePaint);


        int nY = startY + priceNoteHead + 20;

        paintPriceWord(canvas, "开盘价", getValueFormat(kline.getOpen()), isUp, startX, nY);
        nY += wordOff;

        paintPriceWord(canvas, "最高价", getValueFormat(kline.getHigh()), isUp, startX, nY);
        nY += wordOff;

        paintPriceWord(canvas, "最低价", getValueFormat(kline.getLow()), isUp, startX, nY);
        nY += wordOff;

        paintPriceWord(canvas, "收盘价", getValueFormat(kline.getClose()), isUp, startX, nY);
        nY += wordOff;

        paintPriceWord(canvas, "成交量", getValueFormat(kline.getVolume()), isUp, startX, nY);
        nY += wordOff;

        double tw = kline.getOpen();
        if (GlobalSingleton.CreateInstance().PeriodTypeIndex == 6) {
            tw = product.getTW_Close();
            if (tw == 0) {
                tw = product.getIPOPrice();
            }
        }
        double upRate = 0;
        double znRate = 0;
        if (tw != 0) {
            upRate = (kline.getClose() - tw) / tw * 100;
        }
        if (kline.getLow() > 0) {
            znRate = (kline.getHigh() - kline.getLow()) / kline.getLow() * 100;
        }

        BigDecimal upRateDec = BigDecimal.valueOf(upRate);
        upRateDec = upRateDec.setScale(2, BigDecimal.ROUND_HALF_UP);

        BigDecimal znRateDec = BigDecimal.valueOf(znRate);
        znRateDec = znRateDec.setScale(2, BigDecimal.ROUND_HALF_UP);

        paintPriceWord(canvas, "涨跌", upRateDec.toString() + "%", isUp, startX, nY);
        nY += wordOff;

        paintPriceWord(canvas, "振幅", znRateDec.toString() + "%", isUp, startX, nY);
        nY += wordOff;
    }


    /**
     * 画价格标签中的文字
     *
     * @param canvas
     * @param wordTitle
     * @param word
     * @param isUp
     * @param startX
     * @param nY
     */
    private void paintPriceWord(Canvas canvas, String wordTitle, String word, boolean isUp, int startX, int nY) {
        canvas.drawText(wordTitle, startX + 2, nY, priceNoteWordPaint);
        Rect rect = new Rect();
        priceNoteWordPaint.getTextBounds(word, 0, word.length(), rect);
        canvas.drawText(word, startX + priceNoteWidth - rect.width() - 10, nY, isUp ? priceNoteWordRedPaint : priceNoteWordGreenPaint);
    }

    /**
     * 调整格式
     *
     * @param value
     * @return
     */
    private String getValueFormat(double value) {
        return MathUtil.numberToW(value,decimalPlace);
    }


    /**
     * 画时间标签
     * @param canvas
     * @param crossX
     * @param crossY
     */
    public void drawTimeNote(Canvas canvas, int crossX, int crossY){

        if (!isMain || klineSafeCollection.GetAll().size() <=0){
            return;
        }
        //计算触摸的X坐标轴位置,得到应该显示在该位置的K线下标
        KLineModel kLineModel = klineSafeCollection.GetAll().get(xToIndex(crossX));

        Date date = Util.UnixToTime(kLineModel.getTime());
        DateFormat format = new SimpleDateFormat(KLineTimeFormat.timeFormat(GlobalSingleton.CreateInstance().PeriodTypeIndex));

        String time = format.format(date);

        Rect rect = new Rect();
        priceNoteTitlePaint.getTextBounds(time,0,time.length(),rect);
        crossX = timeToX(kLineModel.getTime());

        float startX = crossX-rect.width()/2-10;
        float startY = (float) height;


        //新建圆角矩形
        RectF rectF = new RectF();
        rectF.left = startX-10;
        rectF.right = startX+rect.width()+20 ;
        rectF.top = startY+5;
        rectF.bottom = startY + rect.height()+25;

        //画圆角矩形
        canvas.drawRoundRect(rectF,10,10,priceNoteFillPaint);
        //画文字
        canvas.drawText(time, startX+5, startY + rect.height()+15, priceNoteTitlePaint);

    }

    /**
     * 画十字交叉线
     *
     * @param canvas
     * @param crossX
     * @param crossY
     */
    public void drawCross(Canvas canvas, int crossX, int crossY) {

        //数据为空时不显示
        if (klineSafeCollection.GetAll().size() <=0){
            return;
        }
        int index = xToIndex(crossX);

        //计算触摸的X坐标轴位置,得到应该显示在该位置的K线下标
        KLineModel kLineModel = klineSafeCollection.GetAll().get(index);
        //竖线只定位在k线上面
        crossX = timeToX(kLineModel.getTime());
        //十字中的竖线
        canvas.drawLine(crossX, (float) (this.top + this.headHight + this.topOffSet), crossX, (float) (this.top + this.height - bottomOffSet), crossPaint);


        if (isMain){
            //横线只定位在k线的收盘价上
            float Y = (float) valueToY(kLineModel.getClose());
            //横线
            canvas.drawLine((float) (this.left + leftOffSet), Y, (float) (this.width - rightOffSet - right), Y, crossPaint);

           /* //横线上的收盘价标
            double v = kLineModel.getClose();
            Rect rect = new Rect();
            crossTipPaint.getTextBounds(getValueFormat(v), 0, getValueFormat(v).length(), rect);
            //cross的左边
            canvas.drawText(getValueFormat(v), left + leftOffSet + 6, Y - 6, crossTipPaint);
            //cross的右边
            canvas.drawText(getValueFormat(v), this.width - rightOffSet - right - rect.width() - 10, Y - 6, crossTipPaint);*/


        }else{
           /* if(crossY > this.top+headHight+topOffSet && crossY < (this.top + this.height-bottomOffSet)){

                //十字中的横线,横线的位置只花在收盘价的地方
                canvas.drawLine((float)(this.left+leftOffSet),crossY,(float)(this.width-rightOffSet-right), crossY,crossPaint);
                double v = yToValue((int)crossY);
                if(v>0){
                    Rect rect = new Rect();
                    crossTipPaint.getTextBounds(getValueFormat(v),0,getValueFormat(v).length(),rect);
                    //cross的左边
                    canvas.drawText(getValueFormat(v),left+leftOffSet + 6,crossY - 6,crossTipPaint);
                    //cross的右边
                    canvas.drawText(getValueFormat(v),this.width-rightOffSet-right - rect.width()-10,crossY - 6,crossTipPaint);
                }
            }*/
        }

        //画价格,指标文字
        drawWord(canvas,index);

    }

    /**
     * 画价格,指标文字
     */
    public void  drawWord(Canvas canvas, int index){
        List<ChartBase> ChartList = getChartList();
        this.wordLeftOffset = this.left;
        for (ChartBase c : ChartList) {

            c.drawWord(canvas, index);
        }
    }




}
