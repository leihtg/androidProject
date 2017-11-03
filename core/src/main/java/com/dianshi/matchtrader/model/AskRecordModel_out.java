package com.dianshi.matchtrader.model;

/**
 * 申购委托结果
 */
public class AskRecordModel_out extends ModelOutBase{


    private int Id ;

    private String ProductName ;

    private String Code ;
    private int Count ;

    private double AskMoney ;

    private int Status ;

    private String StatusName ;

    private int SuccCount ;

    private double UsedMoney ;

    private double ReturnMoney ;

    private String Note ;


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

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public double getAskMoney() {
        return AskMoney;
    }

    public void setAskMoney(double askMoney) {
        AskMoney = askMoney;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getStatusName() {
        return StatusName;
    }

    public void setStatusName(String statusName) {
        StatusName = statusName;
    }

    public int getSuccCount() {
        return SuccCount;
    }

    public void setSuccCount(int succCount) {
        SuccCount = succCount;
    }

    public double getUsedMoney() {
        return UsedMoney;
    }

    public void setUsedMoney(double usedMoney) {
        UsedMoney = usedMoney;
    }

    public double getReturnMoney() {
        return ReturnMoney;
    }

    public void setReturnMoney(double returnMoney) {
        ReturnMoney = returnMoney;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}
