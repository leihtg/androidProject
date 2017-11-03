package com.dianshi.matchtrader.Klines;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.dianshi.matchtrader.kLineModel.DrawValueModel;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 绘制成交量柱形图
 */
public class DrawVolLine extends DrawBase {
    @Override
    public void draw(Canvas canvas, int color, Double x, Double y, List<String> keyCollection, ConcurrentHashMap<String, DrawValueModel> value, ConcurrentHashMap<String, DrawValueModel> lastValue) {

        //得到成交量的Y像素值和柱形图原点像素值
        Double volY = value.get("vol").getY();
        Double zeroY = value.get("zero").getY();

        //得到绘制的类型
        int type = (int)value.get("Type").getValue();
        //得到成交量的值 也就是Y坐标值
        Double vol = value.get("vol").getValue();

        //初始化画笔
        Paint fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);

        Paint borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(1);


        if(type == 1){//阳线
            borderPaint.setColor(KLineColor.increaseColor);
            fillPaint.setColor(KLineColor.increaseColor);
        }else{//阴线
            borderPaint.setColor(KLineColor.decreaseColor);
            fillPaint.setColor(KLineColor.decreaseColor);
        }

        //柱形图的的宽度和K线图一样
        float x1 = (float)(x-getChart().getParent().getkWidth()/2);
        float x2 = (float)(x + getChart().getParent().getkWidth()/2);

        //得到
        float y1 = volY.floatValue();
        float y2 = zeroY.floatValue();


        //绘制矩形
        canvas.drawRect(x1,y1,x2,y2 ,fillPaint);

//        canvas.drawRect(x1,y1,x2,y2 ,borderPaint);
//        canvas.drawRect(x1+1,y1+1,x2,y2,fillPaint);
    }
}
