package com.dianshi.matchtrader.model;

/**
 * Created by Administrator on 2016/5/11.
 */
public class ProductCategoryModel_out extends ModelOutBase {

    private int Id;
    private String Name;
    private String Note;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}
