package com.dianshi.matchtrader.Klines;

import android.util.Log;

/**
 * 图表工厂
 */
public class ChartFactory {
    public static ChartBase ProduceChart(String name) {


        ChartBase result = null;
        switch (name){
            case "KChart"://K线图
                result = new KChart();
                break;
            case "VolChart"://成交量柱形图
                result = new VolChart();
                break;
            case "MAChart"://MA
                result = new MAChart();
                break;
            case "TimePriceChart"://分时图
                result = new TimePriceChart();
                break;
            case "MA5Chart"://分时图-ma5均线图
                result = new MA5Chart();
                break;
            case "VolMA5Chart"://成交量-ma5均线图
                result = new VolMA5Chart();
                break;
            case "VolMAChart"://成交量-MA
                result = new VolMAChart();
                break;
            case "EMAChart"://EMA
                result = new EMAChart();
                break;
            case "EMA5Chart"://EMA5
                result = new EMA5Chart();
                break;
            case "VolEMAChart"://成交量-EMA
                result = new VolEMAChart();
                break;
            case "VolEMA5Chart"://成交量-EMA5
                result = new VolEMA5Chart();
                break;
            case "MACDChart"://MACD-MACD
                result = new MACDChart();
                break;
            case "DIFFChart"://MACD-DIFF
                result = new DIFFChart();
                break;
            case "DEAChart"://MACD-DEA
                result = new DEAChart();
                break;
            case "KDJChart"://KDJ
                result = new KDJChart();
                break;
        }
        return result;
    }
}
