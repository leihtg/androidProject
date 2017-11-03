package com.dianshi.matchtrader.model;

/**
 * Created by Administrator on 2017/1/6 0006.
 */
public class PageListBaseModel_out extends ModelOutBase{

    private int AllCount ;

    private int AllPage;

    private int Page ;

    private int PageSize ;

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
