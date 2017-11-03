package com.dianshi.matchtrader.model;

import java.util.List;

/**
 * 入金记录
 * Created by mac on 16/5/14.
 */
public class PageListMoneyInModel_out extends PageListBaseModel_out {

    private double AllMoney ;



    private List<MoneyInModel_out> EntityCollection ;

    public double getAllMoney() {
        return AllMoney;
    }

    public void setAllMoney(double allMoney) {
        AllMoney = allMoney;
    }

    public List<MoneyInModel_out> getEntityCollection() {
        return EntityCollection;
    }

    public void setEntityCollection(List<MoneyInModel_out> entityCollection) {
        EntityCollection = entityCollection;
    }
}
