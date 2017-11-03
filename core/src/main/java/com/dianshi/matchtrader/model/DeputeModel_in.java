package com.dianshi.matchtrader.model;

/**
 * Created by Administrator on 2016/5/12.
 */
public class DeputeModel_in extends ModelInBase {

    private int ProductId;
    private boolean IsDepute;
    private boolean IsBuy;
    private double Price;
    private long Count;

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public boolean isDepute() {
        return IsDepute;
    }

    public void setDepute(boolean depute) {
        IsDepute = depute;
    }

    public boolean isBuy() {
        return IsBuy;
    }

    public void setBuy(boolean buy) {
        IsBuy = buy;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public long getCount() {
        return Count;
    }

    public void setCount(long count) {
        Count = count;
    }
}
