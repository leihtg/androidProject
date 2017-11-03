package dianshi.matchtrader.model;

import com.dianshi.matchtrader.model.ModelOutBase;

/**
 * Created by Administrator on 2016/5/11.
 */
public class LoginResultModel_out extends ModelOutBase {
    private boolean IsSucc;
    private String Msg;
    private int CustomerId;
    private String CustomerUserName;
    private String TrueName;
    private String SignStr;
    private String KLineDownLoadWebSite;

    public boolean isSucc() {
        return IsSucc;
    }

    public void setSucc(boolean succ) {
        IsSucc = succ;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public String getCustomerUserName() {
        return CustomerUserName;
    }

    public void setCustomerUserName(String customerUserName) {
        CustomerUserName = customerUserName;
    }

    public String getTrueName() {
        return TrueName;
    }

    public void setTrueName(String trueName) {
        TrueName = trueName;
    }

    public String getSignStr() {
        return SignStr;
    }

    public void setSignStr(String signStr) {
        SignStr = signStr;
    }

    public String getKLineDownLoadWebSite() {
        return KLineDownLoadWebSite;
    }

    public void setKLineDownLoadWebSite(String KLineDownLoadWebSite) {
        this.KLineDownLoadWebSite = KLineDownLoadWebSite;
    }
}
