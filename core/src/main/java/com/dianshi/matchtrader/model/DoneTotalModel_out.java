package com.dianshi.matchtrader.model;

/**
 * Created by Administrator on 2016/5/14.
 */
public class DoneTotalModel_out extends ModelOutBase {
    private String ProductCode;
    private String ProductName;
    private int BuyCount;
    private String BuyCharge ;
    private String BuyAvgPrice ;
    private int SellCount ;
    private String SellCharge ;
    private String SellAvgPrice ;
    private int TotalCount ;
    private String TotalCharge;
    private String CollectDate;

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

    public int getBuyCount() {
        return BuyCount;
    }

    public void setBuyCount(int buyCount) {
        BuyCount = buyCount;
    }

    public String getBuyCharge() {
        return BuyCharge;
    }

    public void setBuyCharge(String buyCharge) {
        BuyCharge = buyCharge;
    }

    public String getBuyAvgPrice() {
        return BuyAvgPrice;
    }

    public void setBuyAvgPrice(String buyAvgPrice) {
        BuyAvgPrice = buyAvgPrice;
    }

    public int getSellCount() {
        return SellCount;
    }

    public void setSellCount(int sellCount) {
        SellCount = sellCount;
    }

    public String getSellCharge() {
        return SellCharge;
    }

    public void setSellCharge(String sellCharge) {
        SellCharge = sellCharge;
    }

    public String getSellAvgPrice() {
        return SellAvgPrice;
    }

    public void setSellAvgPrice(String sellAvgPrice) {
        SellAvgPrice = sellAvgPrice;
    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }

    public String getTotalCharge() {
        return TotalCharge;
    }

    public void setTotalCharge(String totalCharge) {
        TotalCharge = totalCharge;
    }

    public String getCollectDate() {
        return CollectDate;
    }

    public void setCollectDate(String collectDate) {
        CollectDate = collectDate;
    }
}
