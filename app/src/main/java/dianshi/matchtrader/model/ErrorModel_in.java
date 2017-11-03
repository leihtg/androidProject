package dianshi.matchtrader.model;

/**
 * @author 钱智慧[Email:qzhforthelife@163.com]
 * @date 2014-10-28 上午10:44:34
 */
public class ErrorModel_in extends BaseModel_in {
    private String ErrorCode;

    private String ErrorMsg;

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.ErrorMsg = errorMsg;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        this.ErrorCode = errorCode;
    }

    public ErrorModel_in() {
    }
}
