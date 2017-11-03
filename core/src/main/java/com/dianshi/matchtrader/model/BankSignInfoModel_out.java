package com.dianshi.matchtrader.model;

/**
 * Created by Administrator on 2016/5/16.
 */
public class BankSignInfoModel_out extends ModelOutBase{

    private boolean IsSigned;
    private String SignedSettingName;
    /**
     * 已签约银行代码
     */
    private String SignedBankName;

    /**
     * 已签约手机号
     */
    private String SignedPhone;

    /**
     * 已签约银行卡号
     */
    private String SignedBankNo;

    private String RealName ;


    public boolean isSigned() {
        return IsSigned;
    }

    public void setSigned(boolean signed) {
        IsSigned = signed;
    }

    public String getSignedSettingName() {
        return SignedSettingName;
    }

    public void setSignedSettingName(String signedSettingName) {
        SignedSettingName = signedSettingName;
    }

    public String getSignedBankName() {
        return SignedBankName;
    }

    public void setSignedBankName(String signedBankName) {
        SignedBankName = signedBankName;
    }

    public String getSignedPhone() {
        return SignedPhone;
    }

    public void setSignedPhone(String signedPhone) {
        SignedPhone = signedPhone;
    }

    public String getSignedBankNo() {
        return SignedBankNo;
    }

    public void setSignedBankNo(String signedBankNo) {
        SignedBankNo = signedBankNo;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }
}
