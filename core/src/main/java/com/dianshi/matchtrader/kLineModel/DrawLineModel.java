package com.dianshi.matchtrader.kLineModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *用来画线的model
 */
public class DrawLineModel {
    //X轴坐标值
    private double x;
    //价格列表
    private ConcurrentHashMap<String,DrawValueModel> valueDictionary = new ConcurrentHashMap<>();
    //绘制的类型
    private List<DrawTypeModel> DrawTypeCollection = new ArrayList<>();



    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public ConcurrentHashMap<String, DrawValueModel> getValueDictionary() {
        return valueDictionary;
    }

    public void setValueDictionary(ConcurrentHashMap<String, DrawValueModel> valueDictionary) {
        this.valueDictionary = valueDictionary;
    }

    public List<DrawTypeModel> getDrawTypeCollection() {
        return DrawTypeCollection;
    }

    public void setDrawTypeCollection(List<DrawTypeModel> drawTypeCollection) {
        DrawTypeCollection = drawTypeCollection;
    }
}
