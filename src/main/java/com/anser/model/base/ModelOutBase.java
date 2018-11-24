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

    /**
     * @see MsgType
     */
    public int msgType = MsgType.SUCC;//默认为succ

    public String msg;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
