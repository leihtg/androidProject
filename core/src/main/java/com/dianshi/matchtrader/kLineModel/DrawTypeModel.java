package com.dianshi.matchtrader.kLineModel;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/13.
 */
public class DrawTypeModel {
    //均线的类型
    private int maNumber;


    private String name;
    private int drawColor;
    private List<String> keyCollection = new ArrayList<>();



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

    public int getDrawColor() {
        return drawColor;
    }

    public void setDrawColor(int drawColor) {
        this.drawColor = drawColor;
    }

    public List<String> getKeyCollection() {
        return keyCollection;
    }

    public void setKeyCollection(List<String> keyCollection) {
        this.keyCollection = keyCollection;
    }
}
