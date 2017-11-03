package com.dianshi.matchtrader.model;

/**
 * 向服务器传送数据model的基类
 * 将几乎所有接口访问需要上传的接口的参数
 */
public class ModelInBase {
    private String Sys_Controller;
    private int Sys_CustomerId;
    private String Sys_UserName;
    private String Sys_Key;
    private String Sys_Timestamp;

    public String getSys_Controller() {
        return Sys_Controller;
    }

    public void setSys_Controller(String sys_Controller) {
        Sys_Controller = sys_Controller;
    }

    public int getSys_CustomerId() {
        return Sys_CustomerId;
    }

    public void setSys_CustomerId(int sys_CustomerId) {
        Sys_CustomerId = sys_CustomerId;
    }

    public String getSys_UserName() {
        return Sys_UserName;
    }

    public void setSys_UserName(String sys_UserName) {
        Sys_UserName = sys_UserName;
    }

    public String getSys_Key() {
        return Sys_Key;
    }

    public void setSys_Key(String sys_Key) {
        Sys_Key = sys_Key;
    }

    public String getSys_Timestamp() {
        return Sys_Timestamp;
    }

    public void setSys_Timestamp(String sys_Timestamp) {
        Sys_Timestamp = sys_Timestamp;
    }
}
