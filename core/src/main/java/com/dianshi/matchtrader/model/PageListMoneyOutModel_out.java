package com.dianshi.matchtrader.model;

import java.util.List;

/**
 * 出金记录
 * Created by mac on 16/5/15.
 */
public class PageListMoneyOutModel_out extends PageListBaseModel_out {
    private double AllMoney ;
    private List<MoneyOutModel_out> EntityCollection ;

    public double getAllMoney() {
        return AllMoney;
    }

    public void setAllMoney(double allMoney) {
        AllMoney = allMoney;
    }

    public List<MoneyOutModel_out> getEntityCollection() {
        return EntityCollection;
    }

    public void setEntityCollection(List<MoneyOutModel_out> entityCollection) {
        EntityCollection = entityCollection;
    }
}
