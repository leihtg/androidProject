package com.dianshi.matchtrader.kLineModel;

/**
 * 均线参数model
 */
public class Parameter {
    /**
     *  名称
     */

    private String name;


    /**
     * 类型
     * <p>ma5---5
     * <p>ma30--30
     * <p>ma60---60
     */
    private int type;
    /**
     *   最小值
     */

    private double min;
    /**
     *   最大值
     */

    private double max;




    /**
     *   默认值
     */

    private double defaultValue;
    /**
     * 均线的值
     * <p>ma5
     * <p>ma30
     * <p>ma60
     */

    private double value;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(double defaultValue) {
        this.defaultValue = defaultValue;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
