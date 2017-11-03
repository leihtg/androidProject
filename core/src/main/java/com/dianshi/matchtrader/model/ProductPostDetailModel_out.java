package com.dianshi.matchtrader.model;

/**
 * Created by Administrator on 2017/1/3.
 */
public class ProductPostDetailModel_out extends ModelOutBase {
    private int Id;

    private int ProductId;

    private String ProductName;

    private String CategoryName;

    private int AllCount;

    private String StartTime;

    private String EndTime;

    private boolean IsFrozen;

    private double FrozenRate;

    // 冻结类型
    // 0：到固定年月日截止
    // 1：冻结多少天
    private int FrozenType;

    private String FrozenTime;

    private int FrozenDays;

    // 接受申购的类型
    // 0：申购时间优先
    // 1：不限制数量，开放申购
    private int AcceptAskType;

    // 最小申购
    private int PerMinCount;

    // 最大申购量
    private int PerMaxCount;

    private double Price;

    //申购记录是否允许撤消
    private boolean IsAllowedCancel;
    // 备注
    private String Note;

    //允许申购的最低金额
    private double PerMinLastMoney;

    private double Charge;


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public int getAllCount() {
        return AllCount;
    }

    public void setAllCount(int allCount) {
        AllCount = allCount;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public boolean isFrozen() {
        return IsFrozen;
    }

    public void setFrozen(boolean frozen) {
        IsFrozen = frozen;
    }

    public double getFrozenRate() {
        return FrozenRate;
    }

    public void setFrozenRate(double frozenRate) {
        FrozenRate = frozenRate;
    }

    public int getFrozenType() {
        return FrozenType;
    }

    public void setFrozenType(int frozenType) {
        FrozenType = frozenType;
    }

    public String getFrozenTime() {
        return FrozenTime;
    }

    public void setFrozenTime(String frozenTime) {
        FrozenTime = frozenTime;
    }

    public int getFrozenDays() {
        return FrozenDays;
    }

    public void setFrozenDays(int frozenDays) {
        FrozenDays = frozenDays;
    }

    public int getAcceptAskType() {
        return AcceptAskType;
    }

    public void setAcceptAskType(int acceptAskType) {
        AcceptAskType = acceptAskType;
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

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
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
}
