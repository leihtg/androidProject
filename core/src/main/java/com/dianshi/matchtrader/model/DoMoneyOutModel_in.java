package com.dianshi.matchtrader.model;

/**
 * Created by Administrator on 2016/5/16.
 */
public class DoMoneyOutModel_in extends ModelInBase {
    private String  PhoneCode;
    private  double Money;

    public String getPhoneCode() {
        return PhoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        PhoneCode = phoneCode;
    }

    public double getMoney() {
        return Money;
    }

    public void setMoney(double money) {
        Money = money;
    }
}
