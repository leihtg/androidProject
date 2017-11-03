package com.dianshi.matchtrader.model;

/**
 * Created by Administrator on 2016/5/16.
 */
public class ModifySelfPasswordModel_in extends ModelInBase {
    private String OldPassword;
    private String Password;
    private String Password2;

    public String getOldPassword() {
        return OldPassword;
    }

    public void setOldPassword(String oldPassword) {
        OldPassword = oldPassword;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPassword2() {
        return Password2;
    }

    public void setPassword2(String password2) {
        Password2 = password2;
    }
}
