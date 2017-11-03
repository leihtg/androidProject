package com.dianshi.matchtrader.model;

import java.util.List;

/**
 * Created by Administrator on 2016/5/13.
 */
public class ListDeputeModel_out extends ModelOutBase {
    public List<DeputyModel_out> getItemCollection() {
        return ItemCollection;
    }

    public void setItemCollection(List<DeputyModel_out> itemCollection) {
        ItemCollection = itemCollection;
    }

    private List<DeputyModel_out> ItemCollection;
}
