package com.dianshi.matchtrader.model;

/**
 * 通知的类型
 */
public class NoticeMsgType {
    /***
     * 远程登录
     */
    public static final int RemotingLogin = 0;
    /**
     * 用户自己的订单发生变化
     */
    public static final int SelfDeputeChange = 1;
    /**
     * 用户自己的持仓发生变化
     */
    public static final int SelfPostionChange = 2;
    /**
     * 用户自己的余额发生变化
     */
    public static final int SelfMoneyChange = 3;
    /**
     * 系统清盘
     */
    public static final int Clear = 4;
    /**
     * 商品发生变化
     */
    public static final int ProductChange = 5;
    /**
     * 公告
     */
    public static final int News = 6;


    /**
     * 商品价格发生变化
     */
    public static final int ProductPriceChange = 7;
    /**
     * 商品详情发生变化
     */
    public static final int ProductDetailChange = 8;
    /**
     * 商品五档信息发生变化
     */
    public static final int ProductDeputeChange = 9;
}
