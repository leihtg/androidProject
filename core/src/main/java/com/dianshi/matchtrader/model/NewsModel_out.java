package com.dianshi.matchtrader.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/14.
 */
public class NewsModel_out extends ModelOutBase implements Serializable {

    /**
     * 公告id
     */
    private int Id;
    /**
     * 发布时间
     */
    private String SysCreateTime;
    /**
     * 标题
     */
    private String Title;
    /**
     * 内容
     */
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getSysCreateTime() {
        return SysCreateTime;
    }

    public void setSysCreateTime(String sysCreateTime) {
        SysCreateTime = sysCreateTime;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
