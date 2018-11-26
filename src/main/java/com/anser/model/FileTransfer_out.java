package com.anser.model;

import com.anser.model.base.ModelOutBase;

/**
 * @auth leihtg
 * @date 2018/11/26 11:31
 */
public class FileTransfer_out extends ModelOutBase {
    private FileModel model;
    private long pos;
    private byte[] buf;
    private String data;

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public FileModel getModel() {
        return this.model;
    }

    public void setModel(FileModel model) {
        this.model = model;
    }

    public long getPos() {
        return this.pos;
    }

    public void setPos(long pos) {
        this.pos = pos;
    }

    public byte[] getBuf() {
        return this.buf;
    }

    public void setBuf(byte[] buf) {
        this.buf = buf;
    }
}
