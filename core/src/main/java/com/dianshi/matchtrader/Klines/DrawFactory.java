package com.dianshi.matchtrader.Klines;

import android.graphics.Canvas;
import android.graphics.Path;
import android.util.Log;

import com.dianshi.matchtrader.kLineModel.DrawValueModel;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 绘制工厂
 */
public class DrawFactory {




    /**
     *绘制图形
     * @param chart 图表
     * @param name 绘制类型的名称
     * @param canvas 画布
     * @param color 画笔的颜色
     * @param x x的坐标
     * @param lastX 上个X的坐标
     * @param keyCollection
     * @param value  Y坐标
     * @param lastValue 上个Y的坐标
     */
    public static void Draw(ChartBase chart, String name, Canvas canvas, int color, double x, double lastX, List<String> keyCollection,
                            ConcurrentHashMap<String, DrawValueModel> value, ConcurrentHashMap<String,DrawValueModel> lastValue)
    {

        //得到图形的绘制实例
        DrawBase draw = CreateDrawBase(name);

        if(draw != null){
            //设置图形参数
            draw.setChart(chart);
            //开始绘制
            draw.draw(canvas,color,x,lastX,keyCollection,value,lastValue);
        }
    }


    /**
     * 通过参数确定绘制何种图形,并返回对应图形的绘制实例
     * @param name
     * @return
     */
    private static DrawBase CreateDrawBase(String name)
    {
        DrawBase result = null;
        //将名称转化成小写
        name = name.toLowerCase();
        switch (name)
        {
            case "drawkline"://绘制K线蜡烛图
                result = new DrawKLine();
                break;
            case "drawvol"://绘制成交量柱形图
                result = new DrawVolLine();
                break;
            case "brokenline"://绘制均线图
                result = new DrawBrokenLine();
                break;
            case "drawmacd"://绘制macd的柱状图
                result = new DrawMACD();
                break;
        }
        return result;
    }
}
