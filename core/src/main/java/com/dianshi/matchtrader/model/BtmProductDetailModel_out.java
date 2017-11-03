package com.dianshi.matchtrader.model;

import java.util.List;

/**
 * Created by Administrator on 2016/5/22.
 */
public class BtmProductDetailModel_out extends ModelOutBase {
    private double IPOPrice;
    private double TW_Close;
    private double TopPrice;
    private double BtmPrice;
    private double Price;
    private double UpDown;
    private List<DeputeModels> BuyDeputeList;
    private List<DeputeModels> SellDeputeList;

    public List<DeputeModels> getBuyDeputeList() {
        return BuyDeputeList;
    }

    public void setBuyDeputeList(List<DeputeModels> buyDeputeList) {
        BuyDeputeList = buyDeputeList;
    }

    public List<DeputeModels> getSellDeputeList() {
        return SellDeputeList;
    }

    public void setSellDeputeList(List<DeputeModels> sellDeputeList) {
        SellDeputeList = sellDeputeList;
    }

    public double getIPOPrice() {
        return IPOPrice;
    }

    public void setIPOPrice(double IPOPrice) {
        this.IPOPrice = IPOPrice;
    }

    public double getTW_Close() {
        return TW_Close;
    }

    public void setTW_Close(double TW_Close) {
        this.TW_Close = TW_Close;
    }

    public double getTopPrice() {
        return TopPrice;
    }

    public void setTopPrice(double topPrice) {
        TopPrice = topPrice;
    }

    public double getBtmPrice() {
        return BtmPrice;
    }

    public void setBtmPrice(double btmPrice) {
        BtmPrice = btmPrice;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public double getUpDown() {
        return UpDown;
    }

    public void setUpDown(double upDown) {
        UpDown = upDown;
    }
}
