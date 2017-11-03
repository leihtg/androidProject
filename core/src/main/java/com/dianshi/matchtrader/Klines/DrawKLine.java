package com.dianshi.matchtrader.Klines;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.dianshi.matchtrader.kLineModel.DrawValueModel;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 绘制蜡烛图
 * Created by Administrator on 2016/6/14.
 */
public class DrawKLine extends DrawBase {


    /**
     * 通过高开低收数据 绘制蜡烛图
     * @param canvas
     * @param color
     * @param x  X轴坐标
     * @param lastX
     * @param keyCollection
     * @param value
     * @param lastValue
     */
    @Override
    public void draw(Canvas canvas, int color, Double x, Double lastX, List<String> keyCollection, ConcurrentHashMap<String, DrawValueModel> value, ConcurrentHashMap<String, DrawValueModel> lastValue) {



        //得到高开低收的Y值
        Double openY = value.get("open").getY();
        Double highY = value.get("high").getY();
        Double lowY = value.get("low").getY();
        Double closeY = value.get("close").getY();

        //得到高开低收的价格
        double open = value.get("open").getValue();
        double high = value.get("high").getValue();
        double low = value.get("low").getValue();
        double close = value.get("close").getValue();


        //初始化画笔
        Paint fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);

        Paint borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(2);

        //收盘价>开盘价
        if(open < close){
            borderPaint.setColor(KLineColor.increaseColor);
            fillPaint.setColor(KLineColor.increaseColor);
        }else if(open > close){
            borderPaint.setColor(KLineColor.decreaseColor);
            fillPaint.setColor(KLineColor.decreaseColor);
        }else if(open == close){
            //针对1字跌停的
            if(lastValue != null && lastValue.size()>0){
                double lastClose = lastValue.get("close").getValue();
                if(close >= lastClose){
                    borderPaint.setColor(KLineColor.increaseColor);
                    fillPaint.setColor(KLineColor.increaseColor);
                }else {
                    borderPaint.setColor(KLineColor.decreaseColor);
                    fillPaint.setColor(KLineColor.decreaseColor);
                }
            }else{
                borderPaint.setColor(KLineColor.increaseColor);
                fillPaint.setColor(KLineColor.increaseColor);
            }
        }

        //最低价到最高价之间的竖线
        canvas.drawLine(x.floatValue(),lowY.floatValue(),x.floatValue(),highY.floatValue(),borderPaint);
        ChartBase chart = getChart();

        if(chart.getParent().getkWidth() > 1){

            //收盘价等于开盘价,画横线,组成十字星
            if(closeY == openY || Math.abs(openY - closeY)<=1){
                //画一条横线,长度为kWidth
                canvas.drawLine((float)(x-chart.getParent().getkWidth()/2),openY.floatValue(),(float)(x + chart.getParent().getkWidth()/2),openY.floatValue(),borderPaint);
            }else{//绘制蜡烛图中的矩形
                float x1 = (float)(x - (chart.getParent().getkWidth()/2));
                float x2 = (float)(x + (chart.getParent().getkWidth()/2));
                float y1 = openY.floatValue();
                float y2 = closeY.floatValue();
                if(open <= close){
                    y1 = closeY.floatValue();
                    y2 = openY.floatValue();
                }


//                canvas.drawRect(x1,y1,x2,y2,borderPaint);
                canvas.drawRect(x1,y1,x2,y2,fillPaint);
//                canvas.drawRect(x1+1,y1+1,x2,y2,fillPaint);

            }
        }
    }
}
