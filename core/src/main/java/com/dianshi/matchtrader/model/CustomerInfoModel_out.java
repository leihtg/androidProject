package com.dianshi.matchtrader.model;

/**
 * Created by Administrator on 2016/6/28.
 */
public class CustomerInfoModel_out extends ModelOutBase  {
    private String UserName;
    private String Type ;
    private String PhoneNum;
    private String RoleName;
    //客户资金信息
    private String Money;
    private String Jifen ;
    private String FrozenMoney;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getPhoneNum() {
        return PhoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        PhoneNum = phoneNum;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String roleName) {
        RoleName = roleName;
    }

    public String getMoney() {
        return Money;
    }

    public void setMoney(String money) {
        Money = money;
    }

    public String getJifen() {
        return Jifen;
    }

    public void setJifen(String jifen) {
        Jifen = jifen;
    }

    public String getFrozenMoney() {
        return FrozenMoney;
    }

    public void setFrozenMoney(String frozenMoney) {
        FrozenMoney = frozenMoney;
    }
}
