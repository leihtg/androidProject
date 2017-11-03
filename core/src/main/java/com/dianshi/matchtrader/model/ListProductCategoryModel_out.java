package com.dianshi.matchtrader.model;

import java.util.List;

/**
 * Created by Administrator on 2016/5/12.
 */
public class ListProductCategoryModel_out extends ModelOutBase {

    private List<ProductCategoryModel_out> ItemCollection;

    public void setItemCollection(List<ProductCategoryModel_out> itemCollection) {
        ItemCollection = itemCollection;
    }

    public List<ProductCategoryModel_out> getItemCollection() {
        return ItemCollection;
    }
}
