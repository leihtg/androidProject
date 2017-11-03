package dianshi.matchtrader.model;

import java.util.List;

/**
 * 获取交易所信息的model
 */
public class ExchangeInfoModel {


    /**
     * 交易所名称
     */
    private String TradeCenterName;

    /**
     * 公司的全称
     */
    private String CompanyName;

    /**
     * 软件的版本，主要用于ios版本更新
     */
    private String Version;

    /**
     * 交易所IP地址
     */
    private String Ip;

    /**
     * 端口号
     */
    private String Port;

    /**
     * 交流接口
     */
    private String CommunicationUrl;

    /**
     * 咨询接口
     */
    private String ConsultationUrl;

    /**
     * 开户接口
     */
    private String CreateAccountUrl;

    /**
     * 客服电话
     */
    private String CusServTel;

    /**
     * 交易所Logo
     */
    private String LogoImg;

    /**
     * 出金链接
     */
    private String MoneyInUrl;

    /**
     * 入金链接
     */
    private String MoneyOutUrl;

    /**
     * 官网链接
     */
    private String SiteUrl;

    /**
     * 学堂链接
     */
    private String StudyUrl;

    /**
     * 轮播图
     */
    private List BannerImgs;

    /**
     * 是否可交易
     */
    private boolean IsShow;

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getTradeCenterName() {
        return TradeCenterName;
    }

    public void setTradeCenterName(String tradeCenterName) {
        TradeCenterName = tradeCenterName;
    }

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }

    public String getPort() {
        return Port;
    }

    public void setPort(String port) {
        Port = port;
    }

    public String getCommunicationUrl() {
        return CommunicationUrl;
    }

    public void setCommunicationUrl(String communicationUrl) {
        CommunicationUrl = communicationUrl;
    }

    public String getConsultationUrl() {
        return ConsultationUrl;
    }

    public void setConsultationUrl(String consultationUrl) {
        ConsultationUrl = consultationUrl;
    }

    public String getCreateAccountUrl() {
        return CreateAccountUrl;
    }

    public void setCreateAccountUrl(String createAccountUrl) {
        CreateAccountUrl = createAccountUrl;
    }

    public String getCusServTel() {
        return CusServTel;
    }

    public void setCusServTel(String cusServTel) {
        CusServTel = cusServTel;
    }

    public String getLogoImg() {
        return LogoImg;
    }

    public void setLogoImg(String logoImg) {
        LogoImg = logoImg;
    }

    public String getMoneyInUrl() {
        return MoneyInUrl;
    }

    public void setMoneyInUrl(String moneyInUrl) {
        MoneyInUrl = moneyInUrl;
    }

    public String getMoneyOutUrl() {
        return MoneyOutUrl;
    }

    public void setMoneyOutUrl(String moneyOutUrl) {
        MoneyOutUrl = moneyOutUrl;
    }

    public String getSiteUrl() {
        return SiteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        SiteUrl = siteUrl;
    }

    public String getStudyUrl() {
        return StudyUrl;
    }

    public void setStudyUrl(String studyUrl) {
        StudyUrl = studyUrl;
    }

    public List getBannerImgs() {
        return BannerImgs;
    }

    public void setBannerImgs(List bannerImgs) {
        BannerImgs = bannerImgs;
    }

    public boolean isShow() {
        return IsShow;
    }

    public void setShow(boolean show) {
        IsShow = show;
    }
}
