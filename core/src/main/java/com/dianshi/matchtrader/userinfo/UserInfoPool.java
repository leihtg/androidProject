package com.dianshi.matchtrader.userinfo;

import android.os.Handler;


import com.dianshi.matchtrader.model.CustomerInfoModel_out;
import com.dianshi.matchtrader.model.ProductCategoryModel_out;
import com.dianshi.matchtrader.model.ProductModel_out;

import java.util.ArrayList;
import java.util.List;


/**
 * 存储用户信息的单例类
 */
public class UserInfoPool {


    /**
     * 单例模式
     * @return
     */

    private static UserInfoPool instance = new UserInfoPool();
    public static UserInfoPool CreateInstance(){
        return instance;
    }
    private List<Handler> handlerList = new ArrayList<>();

    /**用户信息*/
    private CustomerInfoModel_out customerInfoModel_out;

    private String UserName;
    private String Type ;
    private String PhoneNum;
    private String RoleName;
    //客户资金信息
    private Double Money;
    private Double Jifen ;
    private Double FrozenMoney;

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

    public Double getMoney() {
        return Money;
    }



    public void setMoney(Double money) {
        Money = money;
    }

    public Double getJifen() {
        return Jifen;
    }

    public void setJifen(Double jifen) {
        Jifen = jifen;
    }

    public Double getFrozenMoney() {
        return FrozenMoney;
    }

    public void setFrozenMoney(Double frozenMoney) {
        FrozenMoney = frozenMoney;
    }

    public void RegHandler(Handler handler){
        handlerList.add(handler);
    }


    /**
     * 添加用户信息
     * @param customerInfoModel_out
     */
    public  void AddUserInfo(CustomerInfoModel_out customerInfoModel_out){
        this.customerInfoModel_out = customerInfoModel_out;

        setUserName(customerInfoModel_out.getUserName());
        setRoleName(customerInfoModel_out.getRoleName());
        setFrozenMoney(Double.valueOf(customerInfoModel_out.getFrozenMoney()));
        setJifen(Double.valueOf(customerInfoModel_out.getJifen()));
        setMoney(Double.valueOf(customerInfoModel_out.getMoney()));
        setPhoneNum(customerInfoModel_out.getPhoneNum());
        setType(customerInfoModel_out.getType());
    }

    /**
     * 得到用户信息
     * @return
     */
    public  CustomerInfoModel_out  getUserInfo(){
        return customerInfoModel_out;
    }


    /**
     * 修改用户信息
     * @param handler
     */
    public void modifyUserInfo(Handler handler){
        UserInfoLoader userInfoLoader = new UserInfoLoader();
        userInfoLoader.loadSuccHandler = handler;
        userInfoLoader.load();
    }
}
