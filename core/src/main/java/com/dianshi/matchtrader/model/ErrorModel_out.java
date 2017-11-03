package com.dianshi.matchtrader.model;

/**
 * Created by Administrator on 2016/4/22.
 */
public class ErrorModel_out extends ModelOutBase {
    private String ErrorCode;
    private String ErrorMsg;

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        ErrorMsg = errorMsg;
    }
}
