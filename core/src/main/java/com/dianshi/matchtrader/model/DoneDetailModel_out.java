package com.dianshi.matchtrader.model;

/**
 * Created by Administrator on 2016/5/14.
 */

/**
 * 当日成交明细
 */
public class DoneDetailModel_out extends ModelOutBase {
    private int CustomerId ;

    private String CustomerName ;

    private String TradeType ;

    private String ProductCode ;

    private String ProductName ;

    private int ProductId ;

    private String Price;

    private int Count;

    private String CostPrice ;

    private String Charge;

    private String Inputtime ;

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getTradeType() {
        return TradeType;
    }

    public void setTradeType(String tradeType) {
        TradeType = tradeType;
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

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public String getCostPrice() {
        return CostPrice;
    }

    public void setCostPrice(String costPrice) {
        CostPrice = costPrice;
    }

    public String getCharge() {
        return Charge;
    }

    public void setCharge(String charge) {
        Charge = charge;
    }

    public String getInputtime() {
        return Inputtime;
    }

    public void setInputtime(String inputtime) {
        Inputtime = inputtime.replace('T',' ');
    }
}
