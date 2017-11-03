package com.dianshi.matchtrader.model;

/**
 * Created by Administrator on 2016/5/12.
 */
public class PositionModel_out extends ModelOutBase {
    private int Id ;

    private String ProductCode ;
    private String ProductName;

    /**
     * 持仓
     */
    private int Count;

    /**
     * 总持仓
     */
    private int AllCount;

    private int ProductId ;
    /**
     * 均价
     */
    private String AvgPrice ;
    /**
     * 收益
     */
    private String HoldProfit;
    /**
     * 收益率
     */
    private String ProfitRate;

    /**
     * 市值
     */
    private String MarketValue ;



    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public int getAllCount() {
        return AllCount;
    }

    public void setAllCount(int allCount) {
        AllCount = allCount;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getAvgPrice() {
        return AvgPrice;
    }

    public void setAvgPrice(String avgPrice) {
        AvgPrice = avgPrice;
    }

    public String getHoldProfit() {
        return HoldProfit;
    }

    public void setHoldProfit(String holdProfit) {
        HoldProfit = holdProfit;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public String getProfitRate() {
        return ProfitRate;
    }

    public void setProfitRate(String profitRate) {
        ProfitRate = profitRate;
    }

    public String getMarketValue() {
        return MarketValue;
    }

    public void setMarketValue(String marketValue) {
        MarketValue = marketValue;
    }



}
