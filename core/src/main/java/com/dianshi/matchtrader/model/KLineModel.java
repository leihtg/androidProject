package com.dianshi.matchtrader.model;

/**
 * K线主要数据model
 */
public class KLineModel {

    /**
     * 时间
     */
    private int Time;
    /**
     * 开盘价
     */
    private double Open;
    /**
     * 最高价
     */
    private double High;
    /**
     * 最低价
     */
    private double Low;
    /**
     * 收盘价
     */
    private double Close;
    /**
     * 成交量
     */
    private int Volume;

    public int getTime() {
        return Time;
    }

    public void setTime(int time) {
        Time = time;
    }

    public double getOpen() {
        return Open;
    }

    public void setOpen(double open) {
        Open = open;
    }

    public double getHigh() {
        return High;
    }

    public void setHigh(double high) {
        High = high;
    }

    public double getLow() {
        return Low;
    }

    public void setLow(double low) {
        Low = low;
    }

    public double getClose() {
        return Close;
    }

    public void setClose(double close) {
        Close = close;
    }

    public int getVolume() {
        return Volume;
    }

    public void setVolume(int volume) {
        Volume = volume;
    }
}
