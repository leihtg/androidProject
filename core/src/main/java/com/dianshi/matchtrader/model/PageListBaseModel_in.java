package com.dianshi.matchtrader.model;

import android.util.Log;

import com.dianshi.matchtrader.model.ModelInBase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 列表类数据的基类model
 */
public class PageListBaseModel_in extends ModelInBase {
    private int Page;
    private int PageSize;
    private String StartDate;
    private String EndDate;

    /**
     * 构造方法 设置加载的时间区间
     * 现在是限制加载一个月的数据
     */
    public PageListBaseModel_in(){

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.MONTH,-1);
        Date start = lastDate.getTime();
        this.StartDate = df.format(start);
        this.EndDate = df.format(now);

    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
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

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }
}
