package com.dianshi.matchtrader.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/5/13.
 */
public class DeputyModel_out extends ModelOutBase {
    private int Id ;

    /**
     * 下单时间
     */
    private String SysCreateTime;

    /**
     * 产品编码
     */
    private String ProductCode;

    /**
     * 产品名称
     */
    private String ProductName ;

    /**
     * 买卖类型
     */
    private String TradeType;

    /**
     * 价格
     */
    private double Price;

    /**
     * 数量
     */
    private int Count;

    /**
     * 状态
     */
    private String Status;

    /**
     * 未成交数量
     */
    private int DeputingCount;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getSysCreateTime() {
        return SysCreateTime;
    }

    public void setSysCreateTime(String sysCreateTime) {
        SysCreateTime = sysCreateTime;
    }

    public String getTimeStr(){
        String ret = "";
        try{
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat df2 = new SimpleDateFormat("HH:mm:ss");
            Date dt = df.parse(SysCreateTime.replace('T',' '));
            ret = df2.format(dt);
        }catch (Exception ex){
        }
        return ret;
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

    public String getTradeType() {
        return TradeType;
    }

    public void setTradeType(String tradeType) {
        TradeType = tradeType;
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getDeputingCount() {
        return DeputingCount;
    }

    public void setDeputingCount(int deputingCount) {
        DeputingCount = deputingCount;
    }
}
