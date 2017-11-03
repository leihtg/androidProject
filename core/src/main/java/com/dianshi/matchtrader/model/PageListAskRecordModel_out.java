package com.dianshi.matchtrader.model;

import java.util.List;

/**
 *
 * 申购结果
 */
public class PageListAskRecordModel_out extends ModelOutBase {
    private double AllMoney ;
    private int AllCount ;
    private int AllPage;
    private int Page ;
    private int PageSize ;

    public List<AskRecordModel_out> getEntityCollection() {
        return EntityCollection;
    }

    public void setEntityCollection(List<AskRecordModel_out> entityCollection) {
        EntityCollection = entityCollection;
    }

    private List<AskRecordModel_out> EntityCollection ;

    public double getAllMoney() {
        return AllMoney;
    }

    public void setAllMoney(double allMoney) {
        AllMoney = allMoney;
    }

    public int getAllCount() {
        return AllCount;
    }

    public void setAllCount(int allCount) {
        AllCount = allCount;
    }

    public int getAllPage() {
        return AllPage;
    }

    public void setAllPage(int allPage) {
        AllPage = allPage;
    }

    public int getPage() {
        return Page;
    }

    public void setPage(int page) {
        Page = page;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int pageSize) {
        PageSize = pageSize;
    }



}
