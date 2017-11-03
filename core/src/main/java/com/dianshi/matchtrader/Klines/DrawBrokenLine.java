package com.dianshi.matchtrader.Klines;

import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;

import com.dianshi.matchtrader.kLineModel.DrawValueModel;
import com.dianshi.matchtrader.model.ProductModel_out;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 绘制折线图
 */
public class DrawBrokenLine extends DrawBase {


    /**
     * 绘制折线图
     * @param canvas 画布
     * @param color 画笔的颜色
     * @param x x的坐标
     * @param lastX 上个X的坐标
     * @param keyCollection
     * @param value  Y坐标
     * @param lastValue 上个Y的坐标
     */
    @Override
    public void draw(Canvas canvas, int color, Double x, Double lastX, List<String> keyCollection, ConcurrentHashMap<String, DrawValueModel> value, ConcurrentHashMap<String, DrawValueModel> lastValue) {
        ChartBase chart = getChart();
        Section section = chart.getParent();




        for (String vk : keyCollection){
            if(lastValue.containsKey(vk)){

                Paint drawPaint = new Paint();
                drawPaint.setColor(color);
                drawPaint.setStyle(Paint.Style.STROKE);
                drawPaint.setStrokeWidth(KLineTextSize.maLineWidth);
                drawPaint.setAntiAlias(true);//抗锯齿


//
                Double x1 = lastX;
                Double y1 = lastValue.get(vk).getY();
                Double x2 = x;
                Double y2 = value.get(vk).getY();



//                //是线段之前的连接角度转折柔和一点,没法用,以为是两个点画一次,实例化一次
//                PathEffect pathEffect = new CornerPathEffect(10);
//                drawPaint.setPathEffect(pathEffect);



                canvas.drawLine(x1.floatValue(),y1.floatValue(),x2.floatValue(),y2.floatValue(),drawPaint);



            }
        }
    }
}
