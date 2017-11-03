package com.dianshi.matchtrader.model;

import java.util.List;

/**
 *
 * 新品申购
 */
public class PageListProductPostModel_out extends PageListBaseModel_out {
    private double AllMoney ;
    private List<ProductPostModel_out> EntityCollection ;

    public double getAllMoney() {
        return AllMoney;
    }

    public void setAllMoney(double allMoney) {
        AllMoney = allMoney;
    }

    public List<ProductPostModel_out> getEntityCollection() {
        return EntityCollection;
    }

    public void setEntityCollection(List<ProductPostModel_out> entityCollection) {
        EntityCollection = entityCollection;
    }
}
