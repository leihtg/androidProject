/**
 *
 */
package com.anser.model;

import com.anser.model.base.ModelInBase;

/**
 * 文件上传参数
 *
 * @author leihuating
 * @time 2018年1月22日 下午1:51:41
 */
public class FileTransfer_in extends ModelInBase {
    private FileModel model;
    private long pos;
    private byte[] buf;

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
