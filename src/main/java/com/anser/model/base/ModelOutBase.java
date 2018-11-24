package com.anser.model.base;

import com.anser.contant.MsgType;
import com.anser.contant.ReceiveData;

/**
 * 服务器返回数据基类
 *
 * @author leihuating
 * @time 2018年1月17日16:53:28
 */
public class ModelOutBase {
    private String uuid;

    private ReceiveData data;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public ReceiveData getData() {
        return this.data;
    }

    public void setData(ReceiveData data) {
        this.data = data;
    }
}
