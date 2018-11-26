package com.anser.model;

import com.anser.model.base.ModelOutBase;

/**
 * @auth leihtg
 * @date 2018/11/26 11:31
 */
public class FileTransfer_out extends ModelOutBase {
    private String path;
    private String data;

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
