package com.dianshi.matchtrader.model;

/**
 * Created by mac on 16/5/14.
 */
public class MoneyLogModel_out extends ModelOutBase {

    private int Id;

    /// <summary>
    /// 变动前金额
    /// </summary>
    private double LastMoney;

    private double Money ;

    /// <summary>
    /// 变动后的金额
    /// </summary>
    public double NewMoney ;

    public String Note;

    public String IP ;

    public String SysCreateTime ;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public double getLastMoney() {
        return LastMoney;
    }

    public void setLastMoney(double lastMoney) {
        LastMoney = lastMoney;
    }

    public double getMoney() {
        return Money;
    }

    public void setMoney(double money) {
        Money = money;
    }

    public double getNewMoney() {
        return NewMoney;
    }

    public void setNewMoney(double newMoney) {
        NewMoney = newMoney;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getSysCreateTime() {
        return SysCreateTime;
    }

    public void setSysCreateTime(String sysCreateTime) {
        SysCreateTime = sysCreateTime;
    }
}
