package com.dianshi.matchtrader.model;

import java.util.List;

/**
 * Created by Administrator on 2016/5/12.
 */
public class ListPostionModel_out extends ModelOutBase {

    public void setItemCollection(List<PositionModel_out> itemCollection) {
        ItemCollection = itemCollection;
    }

    public List<PositionModel_out> getItemCollection() {

        return ItemCollection;
    }

    private List<PositionModel_out> ItemCollection;
}
