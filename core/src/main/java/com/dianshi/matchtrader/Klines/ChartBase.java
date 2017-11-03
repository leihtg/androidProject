package com.dianshi.matchtrader.Klines;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.dianshi.matchtrader.Utils.MathUtil;
import com.dianshi.matchtrader.kLineModel.DrawLineModel;
import com.dianshi.matchtrader.kLineModel.DrawTypeModel;
import com.dianshi.matchtrader.kLineModel.DrawValueModel;
import com.dianshi.matchtrader.kLineModel.DrawWordModel;
import com.dianshi.matchtrader.kLineModel.OutResult;
import com.dianshi.matchtrader.kLineModel.Parameter;
import com.dianshi.matchtrader.model.KLineModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 图表的基类
 * 用来统一绘图方式
 * 并将数据放入父类来统一管理
 * draw()方法使用DrawFactory代理绘制
 */
public class ChartBase {

    private boolean isMainSection = false;
    //柱形图的原点坐标Y像素值
    private double zeroY;
    //绘制的图形名称
    private String name;

    private String displayName;


    private Section parent;


    protected List<Parameter> _ParameterList = new ArrayList<>();

    //最大值和最小值之间的差距
    protected double _MaxMinStep = 0.01;
    //移动的价格最小值?
    protected double _MinMinValue = 0;


    //保留小数的位数
    public int DecimalPlace = 2;


    private Paint hLinePaint;
    private Paint hWordPaint;
    private Paint drawWordPaint;


    /**
     * 构造方法
     */
    public ChartBase() {
        //初始化画笔
        initPaint();
        //初始化参数
        initParameter();
    }


    /**
     * 初始化画笔
     */
    private void initPaint() {
        hLinePaint = new Paint();
        hLinePaint.setStrokeWidth(1);
        hLinePaint.setColor(Color.WHITE);
        hLinePaint.setStyle(Paint.Style.STROKE);

        hWordPaint = new Paint();
        hWordPaint.setStrokeWidth(1);
        hWordPaint.setColor(Color.WHITE);
        hWordPaint.setStyle(Paint.Style.STROKE);

        drawWordPaint = new Paint(Paint.FAKE_BOLD_TEXT_FLAG);
        drawWordPaint.setTextSize(KLineTextSize.priceTxtSize);
        drawWordPaint.setStyle(Paint.Style.FILL);
        drawWordPaint.setColor(Color.WHITE);
    }

    /**
     * 得到参数
     *
     * @return
     */
    public List<Parameter> get_ParameterList() {
        List<Parameter> result = new ArrayList<>();
        synchronized (this) {
            for (Parameter item : _ParameterList) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * 设置参数
     *
     * @param value
     */
    public void set_ParameterList(List<Parameter> value) {
        synchronized (this) {
            _ParameterList.clear();
            for (Parameter item : value) {
                _ParameterList.add(item);
            }
        }
    }

    /**
     *
     */
    protected List<OutResult> _ResultList = new ArrayList<>();

    /**
     * 得到结果列表
     *
     * @return
     */
    public List<OutResult> get_ResultList() {
        List<OutResult> results = new ArrayList<>();
        synchronized (this) {
            for (OutResult item : _ResultList) {
                results.add(item);
            }
        }
        return results;
    }

    /**
     * 设置结果列表
     *
     * @param value
     */
    public void set_ResultList(List<OutResult> value) {
        synchronized (this) {
            _ResultList.clear();
            for (OutResult item : value) {
                _ResultList.add(item);
            }
        }
    }

    /**
     * 用来存储K线高开低收的价格值列表
     */

    protected List<ConcurrentHashMap<String, Double>> ComputeList = new ArrayList<>();
    /**
     * 用来存储K线高开低收键名,绘制线类型,绘制颜色
     */
    protected List<List<DrawTypeModel>> DrawTypeCollection = new ArrayList<>();

    /**
     * 用来存储K线高开低收键名,价格值,绘制颜色
     */

    protected List<List<DrawWordModel>> DrawWordCollection = new ArrayList<>();

    /**
     * 用来存储绘制的X值,Y值
     */
    protected List<DrawLineModel> DrawLineCollection = new ArrayList<>();

    /**
     * 用来存储绘制成交量的Y值
     */
    protected List<Double> HLineCollection = new ArrayList<>();
    protected List<Double> DrawHLineCollection = new ArrayList<>();


    /**
     * 计算K线中最大值和最小值
     */
    public void computeMaxMin() {

        synchronized (this) {
            //得到所有KLine的数据
            List<KLineModel> kCollection = parent.getKlineSafeCollection().GetAll();
            //计算显示K线的数目,得到showNumber
            int start = kCollection.size() - parent.getShowKNum() - parent.getSkipNum();
            int endNum = ComputeList.size() - parent.getSkipNum();


            Log.i("cccccccc", "----computeMaxMin---ShowKNum=" + parent.getShowKNum() + "----SkipNum=" + parent.getSkipNum() + "---startNum=" + start + "---endNum=" + endNum + "----kCollection=" + kCollection.size() + "----ComputeList==" + ComputeList.size());

            double Min = parent.getMinValue(), Max = parent.getMaxValue();

            if (start < 0) {
                start = 0;
            }



            /*
             * 遍历整个列表,计算出最大和最小值
             */
            for (int i = start; i < endNum; i++) {
                Set<String> vKeys = ComputeList.get(i).keySet();
                for (String k : vKeys) {
                    double v = ComputeList.get(i).get(k);
                    Min = Math.min(Min, v);
                    Max = Math.max(Max, v);
                }
            }


            /*
            如果最小值大于最大值
            最小值 ,最大值交换
             */
            if (Min > Max && Min != Double.MAX_VALUE && Max != Double.MIN_VALUE) {
                double temp = Max;
                Max = Min;
                Min = temp;
            }


            /*
            如果最大值小于最小值,或者二者之间的差距小于_MaxMinStep
            最大值 = 最小值 +_MaxMinStep
             */
            if (Max - Min < _MaxMinStep) {
                Max = Min + _MaxMinStep;

            }
            //设定最大值和最小值
            parent.setMinValue(Min);
            parent.setMaxValue(Max);
        }


    }


    /**
     * 计算绘制的数据值
     */
    public void computeDraw() {
        synchronized (this) {
            //得到K线全部数据
            List<KLineModel> kCollection = parent.getKlineSafeCollection().GetAll();

            //现改为从最小值开始
            zeroY = parent.valueToY(parent.getMinValue());


            //清空之前的数据
            DrawLineCollection.clear();

            /*
            计算能显示的K线的个数 此地发现数组越界的情况 这里要确保K线的 startNumber 和 endNum 的长度范围都在ComputeList内
             */
            int start = kCollection.size() - parent.getShowKNum() - parent.getSkipNum();
            int endNum = ComputeList.size() - parent.getSkipNum();



            //这里的判断非常必要,因为showNumber = width/Kwidth+Kspace ,所以有可能比 kCollection.size()还要大,所以start有可能为负
            if (start < 0) {
                start = 0;
            }

            if (endNum < 0) {
                endNum = 0;
            }

            if (ComputeList.size() == DrawTypeCollection.size()) {

                for (int i = start; i < endNum; i++) {

                    ConcurrentHashMap<String, DrawValueModel> valDict = new ConcurrentHashMap<>();

                    //得到ComputeList中某条K线的所有键值 高开低收,ma均线,成交量,
                    Set<String> vKeys = ComputeList.get(i).keySet();

                    //遍历整个键值集合
                    for (String k : vKeys) {
                        if (!valDict.containsKey(k)) {//如果不存在,就添加
                            valDict.put(k, new DrawValueModel());
                        }
                        //存入Y轴坐标值和Value
                        double v = ComputeList.get(i).get(k);
                        valDict.get(k).setY(parent.valueToY(v));
                        valDict.get(k).setValue(v);
                    }

                    //得到绘制的数据表
                    DrawLineModel dl = new DrawLineModel();

                    dl.setX(parent.timeToX(kCollection.get(i).getTime())); //所有图标得到由对应时间转换的统一X坐标值
                    dl.setValueDictionary(valDict); //K线value数据统一字典表
                    dl.setDrawTypeCollection(DrawTypeCollection.get(i)); //所属的图形绘制类型等
                    DrawLineCollection.add(dl);
                }

                //绘制高度线的集合清空
                DrawHLineCollection.clear();

                //遍历HLineCollection,数据给DrawHLineCollection,HLineCollection是在哪里给的数据
                for (Double hItem : HLineCollection) {
                    DrawHLineCollection.add(parent.valueToY(hItem));
                }
            }

        }
    }

    public void setParamter() {

    }


    /**
     * 初始化参数
     * 用来留给子类重写的参数
     */
    public void initParameter() {
    }

    /**
     * 计算
     * 用来留给子类重写的参数
     *
     * @param startNum
     */
    public void compute(int startNum) {

    }

    /**
     * 绘制
     *
     * @param canvas
     */
    public void draw(Canvas canvas) {
        synchronized (this) {
            for (int i = 0; i < DrawLineCollection.size(); i++) {
                DrawLineModel draw = DrawLineCollection.get(i);
                double lastX = -1;
                ConcurrentHashMap<String, DrawValueModel> lastArray = new ConcurrentHashMap<>();


                if (i > 0) {//获取上一个的X位置和value列表,主要K线的一字跌停判断条件
                    lastArray = DrawLineCollection.get(i - 1).getValueDictionary();
                    lastX = DrawLineCollection.get(i - 1).getX();
                }

                //遍历DrawTypeCollection,开始绘制K线图,柱形图,或者函数曲线
                for (int j = 0; j < draw.getDrawTypeCollection().size(); j++) {
                    DrawTypeModel item = draw.getDrawTypeCollection().get(j);

                    int start = ComputeList.size() - parent.getShowKNum() - parent.getSkipNum();
                    if (start < 0) {
                        start = 0;
                    }

                    if (item.getName().equals("BrokenLine")) {

                        if ((start + i) - item.getMaNumber() > 0) {//控制ma均线前面的部分被隐藏
                            DrawFactory.Draw(this, item.getName(), canvas, item.getDrawColor(), draw.getX(), lastX, item.getKeyCollection(), draw.getValueDictionary(), lastArray);
                        }

                    } else {
                        DrawFactory.Draw(this, item.getName(), canvas, item.getDrawColor(), draw.getX(), lastX, item.getKeyCollection(), draw.getValueDictionary(), lastArray);

                    }
                }


                for (int j = 0; j < DrawHLineCollection.size(); j++) {

                    int x1 = parent.getLeft() + parent.getLeftOffSet();
                    float y = DrawHLineCollection.get(j).floatValue();
                    int x2 = parent.getLeft() + parent.getWidth();
                    String value = getValueFormat(HLineCollection.get(j));


//                    canvas.drawLine(x1,y,x2,y,hLinePaint);//画平行于X轴的一条直线,坐标为Y,长度为X轴的长度
//                    canvas.drawText(value,x1,y,hWordPaint);//绘制value,在Y轴附近?
                }
            }
        }
    }

    /**
     * 得到格式化后值
     * 指定小数位
     *
     * @param value
     * @return
     */
    private String getValueFormat(double value) {
        return MathUtil.numberToW(value, DecimalPlace);
    }

    /**
     * 绘制文字 高开低收 MA5等收录到DrawWordCollection的值
     *
     * @param canvas
     * @param index
     */
    public void drawWord(Canvas canvas, int index) {

        synchronized (this) {

            //防止数组越界
            if (index < 0) {
                index = 0;
            } else if (index >= DrawWordCollection.size()) {
                index = DrawWordCollection.size() - 1;
            }

            if (index < DrawWordCollection.size() && index >= 0) {
                List<DrawWordModel> words = DrawWordCollection.get(index);


                for (int i = 0; i < words.size(); i++) {
                    DrawWordModel item = words.get(i);
                    //得到需要绘制出来的字符串,例如xx:xx;
                    String str = null;

                    //控制ma均线未显示之前的线无数据
                    if (index < item.getMaNumber() - 1) {
                        str = item.getName() + ":" + "--";
                    } else {
                        str = item.getName() + ":" + getValueFormat(item.getValue());
                    }


                    //以这个字符串的高和得到一个矩形
                    Rect rect = new Rect();
                    drawWordPaint.setColor(item.getDrawColor());
                    drawWordPaint.getTextBounds(str, 0, str.length(), rect);

                    //得到文字的宽度
                    int wordWidth = rect.width();
                    //如果绘制的长度超过了K线显示的宽度,就把字体变小
                    //这里处理的额有问题,因为处理之前的字体大小无法改变
                    if ((parent.getWordLeftOffset() + wordWidth) > parent.getWidth()) {
                        KLineTextSize.priceTxtSize = KLineTextSize.priceTxtSize - 2;
                        drawWordPaint.setTextSize(KLineTextSize.priceTxtSize);
                        parent.drawWord(canvas, index);
                    } else {
//                        KLineTextSize.priceTxtSize = 24;
                    }
                    //画文字,画在上一个文字的后面
                    canvas.drawText(str, parent.getWordLeftOffset(), parent.getTop() + 40, drawWordPaint);
                    //将文字的宽度 传给parent
                    parent.setWordLeftOffset(parent.getWordLeftOffset() + wordWidth + 10);


                }
            }
        }
    }

    /**
     * 从指定位置移除计算后的列表
     *
     * @param startNum
     */
    protected void removeComputeList(int startNum) {
        for (int i = startNum; i < ComputeList.size(); i++) {
            ComputeList.remove(i);
            i--;
        }
    }

    /**
     * 从指定位置移除绘制类型列表
     *
     * @param startNum
     */
    protected void removeDrawTypeCollection(int startNum) {
        for (int i = startNum; i < DrawTypeCollection.size(); i++) {
            DrawTypeCollection.remove(i);
            i--;
        }
    }

    /**
     * 从指定位置移除绘制文字列表
     *
     * @param startNum
     */
    protected void removeDrawWordCollection(int startNum) {
        for (int i = startNum; i < DrawWordCollection.size(); i++) {
            DrawWordCollection.remove(i);
            i--;
        }
    }

    /**
     * 设置是否是MainSection,也就是K线图
     *
     * @param value
     */
    public void setMainSection(boolean value) {
        this.isMainSection = value;
    }

    public boolean getMainSection() {
        return this.isMainSection;
    }

    public double getZeroY() {
        return zeroY;
    }

    public void setZeroY(double zeroY) {
        this.zeroY = zeroY;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Section getParent() {
        return parent;
    }

    public void setParent(Section parent) {
        this.parent = parent;
    }
}
