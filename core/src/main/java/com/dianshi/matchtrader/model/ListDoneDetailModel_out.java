package com.dianshi.matchtrader.model;

import java.util.List;

/**
 * Created by Administrator on 2016/5/14.
 */
public class ListDoneDetailModel_out extends ModelOutBase {
    private List<DoneDetailModel_out> ItemCollection;

    public List<DoneDetailModel_out> getItemCollection() {
        return ItemCollection;
    }

    public void setItemCollection(List<DoneDetailModel_out> itemCollection) {
        ItemCollection = itemCollection;
    }
}
