package com.dianshi.matchtrader.model;

/**
 * Created by Administrator on 2016/5/19.
 */
public class TickModel {
    private String DateTime ;
    private double Price;
    private int Count;
    private int Type;

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }
}
