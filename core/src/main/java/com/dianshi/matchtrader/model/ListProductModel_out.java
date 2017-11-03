package com.dianshi.matchtrader.model;

import java.util.List;

/**
 * Created by Administrator on 2016/5/12.
 */
public class ListProductModel_out extends ModelOutBase {
    private List<ProductModel_out> ItemCollection;

    public void setItemCollection(List<ProductModel_out> itemCollection) {
        ItemCollection = itemCollection;
    }

    public List<ProductModel_out> getItemCollection() {
        return ItemCollection;
    }
}
