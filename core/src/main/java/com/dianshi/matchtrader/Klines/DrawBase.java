package com.dianshi.matchtrader.Klines;

import android.graphics.Canvas;
import android.graphics.Path;

import com.dianshi.matchtrader.kLineModel.DrawValueModel;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 绘制基类
 */
public class DrawBase {
    //绘制需要的一些数据数据
    private ChartBase chart;


    public ChartBase getChart() {
        return chart;
    }


    public void setChart(ChartBase chart) {
        this.chart = chart;
    }


    //绘制函数
    public void draw(Canvas canvas, int color, Double x, Double y, List<String> keyCollection,
                     ConcurrentHashMap<String,DrawValueModel> value, ConcurrentHashMap<String,DrawValueModel> lastValue){

    }
}
