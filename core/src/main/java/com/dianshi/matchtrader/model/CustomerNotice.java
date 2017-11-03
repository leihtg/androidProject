package com.dianshi.matchtrader.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/17.
 */
public class CustomerNotice {
    private boolean IsNotice;
    private ArrayList<String> CodeList;
    private boolean IsRemoteLogin;
    private String RemoteIP;

    public boolean isNotice() {
        return IsNotice;
    }

    public void setNotice(boolean notice) {
        IsNotice = notice;
    }

    public ArrayList<String> getCodeList() {
        return CodeList;
    }

    public void setCodeList(ArrayList<String> codeList) {
        CodeList = codeList;
    }

    public boolean isRemoteLogin() {
        return IsRemoteLogin;
    }

    public void setRemoteLogin(boolean remoteLogin) {
        IsRemoteLogin = remoteLogin;
    }

    public String getRemoteIP() {
        return RemoteIP;
    }

    public void setRemoteIP(String remoteIP) {
        RemoteIP = remoteIP;
    }
}
