package com.dianshi.matchtrader.model;

/**
 * Created by Administrator on 2017/1/3.
 */
public class ProductPostAskModel_out extends ModelOutBase{

    private boolean IsCanAsk ;

    private String Msg ;

    private int Id ;

    private String ProductName ;

    private int AllCount ;



    /// <summary>
    /// 最小申购
    /// </summary>
    private int PerMinCount ;

    /// <summary>
    /// 最大申购量
    /// </summary>
    private int PerMaxCount ;


    private double Price ;


    /// <summary>
    /// 申购记录是否允许撤消
    /// </summary>
    private boolean IsAllowedCancel ;


    /// <summary>
    /// 允许申购的最低金额
    /// </summary>
    private double PerMinLastMoney ;

    private double Charge ;

    private int HasAsk ;

    public boolean isCanAsk() {
        return IsCanAsk;
    }

    public void setCanAsk(boolean canAsk) {
        IsCanAsk = canAsk;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getAllCount() {
        return AllCount;
    }

    public void setAllCount(int allCount) {
        AllCount = allCount;
    }

    public int getPerMinCount() {
        return PerMinCount;
    }

    public void setPerMinCount(int perMinCount) {
        PerMinCount = perMinCount;
    }

    public int getPerMaxCount() {
        return PerMaxCount;
    }

    public void setPerMaxCount(int perMaxCount) {
        PerMaxCount = perMaxCount;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public boolean isAllowedCancel() {
        return IsAllowedCancel;
    }

    public void setAllowedCancel(boolean allowedCancel) {
        IsAllowedCancel = allowedCancel;
    }

    public double getPerMinLastMoney() {
        return PerMinLastMoney;
    }

    public void setPerMinLastMoney(double perMinLastMoney) {
        PerMinLastMoney = perMinLastMoney;
    }

    public double getCharge() {
        return Charge;
    }

    public void setCharge(double charge) {
        Charge = charge;
    }

    public int getHasAsk() {
        return HasAsk;
    }

    public void setHasAsk(int hasAsk) {
        HasAsk = hasAsk;
    }
}
