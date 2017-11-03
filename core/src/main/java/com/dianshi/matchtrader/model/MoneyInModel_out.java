package com.dianshi.matchtrader.model;

/**
 * Created by mac on 16/5/14.
 */
public class MoneyInModel_out extends ModelOutBase {



    private int Id;
    private String BankName;
    private String BankDetail;
    private String BankUserName ;
    private String BankCard ;
    private double Money;
    private String Status ;
    private String FinishTime;
    private String Note ;


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getBankDetail() {
        return BankDetail;
    }

    public void setBankDetail(String bankDetail) {
        BankDetail = bankDetail;
    }

    public String getBankUserName() {
        return BankUserName;
    }

    public void setBankUserName(String bankUserName) {
        BankUserName = bankUserName;
    }

    public String getBankCard() {
        return BankCard;
    }

    public void setBankCard(String bankCard) {
        BankCard = bankCard;
    }

    public double getMoney() {
        return Money;
    }

    public void setMoney(double money) {
        Money = money;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getFinishTime() {
        return FinishTime;
    }

    public void setFinishTime(String finishTime) {
        FinishTime = finishTime;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}
