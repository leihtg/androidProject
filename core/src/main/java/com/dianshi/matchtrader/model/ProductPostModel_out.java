package com.dianshi.matchtrader.model;

/**
 * 新品申购列表的输出model
 */
public class ProductPostModel_out {
    
    public int Id ;

    public String ProductName ;

    public String CategoryName ;

    public int ProductId ;

    public int AllCount ;

    public String StartAskTime ;

    public String EndAskTime ;

    public int AcceptAskType ;

    public int PerMinCount ;

    public int PerMaxCount ;

    public double Price ;

    public String Note ;

    public double PerMinLastMoney ;

    public String getStartAskTime() {
        return StartAskTime;
    }

    public void setStartAskTime(String startAskTime) {
        StartAskTime = startAskTime;
    }

    public String getEndAskTime() {
        return EndAskTime;
    }

    public void setEndAskTime(String endAskTime) {
        EndAskTime = endAskTime;
    }

    public double Charge ;


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

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

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
