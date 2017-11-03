package com.dianshi.matchtrader.model;

import java.util.List;

/**
 * 公告
 * Created by Administrator on 2016/5/14.
 */
public class PageListNewsModel_out extends PageListBaseModel_out {
    private double AllMoney;
    private List<NewsModel_out> EntityCollection;

    public double getAllMoney() {
        return AllMoney;
    }

    public void setAllMoney(double allMoney) {
        AllMoney = allMoney;
    }


    public List<NewsModel_out> getEntityCollection() {
        return EntityCollection;
    }

    public void setEntityCollection(List<NewsModel_out> entityCollection) {
        EntityCollection = entityCollection;
    }
}
