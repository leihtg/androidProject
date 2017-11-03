package com.dianshi.matchtrader.kLineModel;

import android.graphics.Color;

/**
 * K线相关参数绘制model
 */
public class DrawWordModel {
    /**
     * 参数的类型
     */
    private String name;
    /**
     * 绘制的颜色
     */
    private int drawColor;
    /**
     * 参数的值
     */
    private double value;

    /**
     * ma的值
     */
    private double maNumber;


    public double getMaNumber() {
        return maNumber;
    }

    public void setMaNumber(double maNumber) {
        this.maNumber = maNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawColor() {
        return drawColor;
    }

    public void setDrawColor(int drawColor) {
        this.drawColor = drawColor;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
