package com.dianshi.matchtrader.model;

import java.util.List;

/**
 * 资金明细model
 * Created by mac on 16/5/14.
 */
public class PageListModelLogModel_out extends PageListBaseModel_out {

    private double AllMoney ;

    private List<MoneyLogModel_out> EntityCollection ;

    public double getAllMoney() {
        return AllMoney;
    }

    public void setAllMoney(double allMoney) {
        AllMoney = allMoney;
    }

    public List<MoneyLogModel_out> getEntityCollection() {
        return EntityCollection;
    }

    public void setEntityCollection(List<MoneyLogModel_out> entityCollection) {
        EntityCollection = entityCollection;
    }
}
