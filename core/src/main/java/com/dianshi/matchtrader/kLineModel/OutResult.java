package com.dianshi.matchtrader.kLineModel;

import android.graphics.Color;

/**
 * MA5
 * 输出结果的model
 */
public class OutResult {


    /**
     * 名称
     */
    private String name;
    /**
     * 展示的名称
     */
    private String displayName;
    /**
     * 颜色
     */
    private int Color;

    /**
     * 均线的数目
     * Ma5--5
     * MA10--10
     */
    private int maNumber;


    public int getMaNumber() {
        return maNumber;
    }

    public void setMaNumber(int maNumber) {
        this.maNumber = maNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getColor() {
        return Color;
    }

    public void setColor(int color) {
        Color = color;
    }
}
