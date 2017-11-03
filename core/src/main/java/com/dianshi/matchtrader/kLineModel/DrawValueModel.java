package com.dianshi.matchtrader.kLineModel;

/**
 * 绘制价格
 */
public class DrawValueModel {

    //价格
    private double value;
    //y轴像素值
    private double y;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
