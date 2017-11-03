package com.dianshi.matchtrader.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/11.
 */
public class ProductModel_out extends ModelOutBase {


    private  int Id;

    /**
     * 分类的Id
     */
    private int CategoryId;

    /**
     * 名称
     */
    private String Name;

    /**
     * 编码
     */
    private String Code;

    /**
     * 状态（不显示)
     */
    private  int Status;

    /**
     * 交易状态不显示
     */
    private int TradeStatus;

    /**
     * 最小买入手续费
     */
    private double BuyChargeMin;

    /**
     * 买入手续费率
     */
    private double BuyCharge;

    /**
     * 卖出手续费率
     */
    private double SellCharge;

    /**
     * 卖出最小手续费
     */
    private double SellChargeMin;

    /**
     * 最小交易数量
     */
    private double PerMinTradeCount;

    /**
     * 最大交易数量
     */
    public int PerMaxTradeCount;

    /**
     * 总数
     */
    private int AllCount;

    /**
     * 已发行数量
     */
    private int HasPostCount;

    /**
     * 最大涨幅
     */
    private double TopRate;

    /**
     * 最大跌幅
     */
    private double BottomRate;

    /**
     * 每份积分
     */
    private double PerCountJifen;

    /**
     * 买入后多少天可以出售
     */
    private int TradeAfterDays;

    /**
     * 开盘价
     */
    private double Open;

    /**
     * 成交量
     */
    private int DayAllTrade;

    /**
     * 现价
     */
    private double Price;

    /**
     * 昨收
     */
    private double TW_Close;

    /**
     * IPO价格
     */
    private double IPOPrice;

    /**
     * IPO最大涨幅
     */
    private double IPOTopRate;

    /**
     * IPO最大跌幅
     */
    private double IPOBottomRate;

    /**
     * 买一价
     */
    private double BuyFirstPrice;

    /**
     * 卖一价
     */
    private double SellFirstPrice;

    /**
     * 买量
     */
    private int BuyVolume;

    /**
     * 卖量
     */
    private  int SellVolume;

    /**
     * 卖一量
     */
    private int SellFirstCount;

    /**
     * 买一量
     */
    private int BuyFirstCount;

    /**
     * 刷新时间的Unix字符
     */
    private int RefreshTime;

    /**
     * 刷新时间
     */
    private String RefreshTime2;

    /**
     * 成交总价格
     */
    private double AllMoney;

    /**
     * 最高
     */
    private double MaxPrice;

    /**
     * 最低
     */
    private double MinPrice;

    /**
     * 现在成交价
     */
    private double NowMoney;

    /**
     * 现在成交量
     */
    private int NowCount;

    /**
     * 委买量
     */
    private int DeputeBCount;

    /**
     * 委卖量
     */
    private int DeputeSCount;


    /**
     * 买五档
     */
    private ArrayList<DeputeModels> BuyDeputeList = new ArrayList<>();
    /**
     * 卖五档
     */
    private ArrayList<DeputeModels> SellDeputeList = new ArrayList<>();
    /**
     * 明细
     */
    private ArrayList<TickModel> TickQueue = new ArrayList<>();

    /**
     * K线缓存
     */
    private ArrayList<KLineModel> cacheKline = new ArrayList<KLineModel>();





    public ArrayList<DeputeModels> getBuyDeputeList() {
        return BuyDeputeList;
    }

    public void setBuyDeputeList(ArrayList<DeputeModels> buyDeputeList) {
        BuyDeputeList = buyDeputeList;
    }

    public ArrayList<DeputeModels> getSellDeputeList() {
        return SellDeputeList;
    }

    public void setSellDeputeList(ArrayList<DeputeModels> sellDeputeList) {
        SellDeputeList = sellDeputeList;
    }

    public ArrayList<TickModel> getTickQueue() {
        return TickQueue;
    }

    public void setTickQueue(ArrayList<TickModel> tickQueue) {
        TickQueue = tickQueue;
    }

    public ArrayList<KLineModel> getCacheKline() {
        return cacheKline;
    }

    public void setCacheKline(ArrayList<KLineModel> cacheKline) {
        this.cacheKline = cacheKline;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getTradeStatus() {
        return TradeStatus;
    }

    public void setTradeStatus(int tradeStatus) {
        TradeStatus = tradeStatus;
    }

    public double getBuyChargeMin() {
        return BuyChargeMin;
    }

    public void setBuyChargeMin(double buyChargeMin) {
        BuyChargeMin = buyChargeMin;
    }

    public double getBuyCharge() {
        return BuyCharge;
    }

    public void setBuyCharge(double buyCharge) {
        BuyCharge = buyCharge;
    }

    public double getSellCharge() {
        return SellCharge;
    }

    public void setSellCharge(double sellCharge) {
        SellCharge = sellCharge;
    }

    public double getSellChargeMin() {
        return SellChargeMin;
    }

    public void setSellChargeMin(double sellChargeMin) {
        SellChargeMin = sellChargeMin;
    }

    public double getPerMinTradeCount() {
        return PerMinTradeCount;
    }

    public void setPerMinTradeCount(double perMinTradeCount) {
        PerMinTradeCount = perMinTradeCount;
    }

    public int getAllCount() {
        return AllCount;
    }

    public void setAllCount(int allCount) {
        AllCount = allCount;
    }

    public int getHasPostCount() {
        return HasPostCount;
    }

    public void setHasPostCount(int hasPostCount) {
        HasPostCount = hasPostCount;
    }

    public double getTopRate() {
        return TopRate;
    }

    public void setTopRate(double topRate) {
        TopRate = topRate;
    }

    public double getBottomRate() {
        return BottomRate;
    }

    public void setBottomRate(double bottomRate) {
        BottomRate = bottomRate;
    }

    public double getPerCountJifen() {
        return PerCountJifen;
    }

    public void setPerCountJifen(double perCountJifen) {
        PerCountJifen = perCountJifen;
    }

    public int getTradeAfterDays() {
        return TradeAfterDays;
    }

    public void setTradeAfterDays(int tradeAfterDays) {
        TradeAfterDays = tradeAfterDays;
    }

    public double getOpen() {
        return Open;
    }

    public void setOpen(double open) {
        Open = open;
    }

    public int getDayAllTrade() {
        return DayAllTrade;
    }

    public void setDayAllTrade(int dayAllTrade) {
        DayAllTrade = dayAllTrade;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getPerMaxTradeCount() {
        return PerMaxTradeCount;
    }

    public void setPerMaxTradeCount(int perMaxTradeCount) {
        PerMaxTradeCount = perMaxTradeCount;
    }

    public double getTW_Close() {
        return TW_Close;
    }

    public void setTW_Close(double TW_Close) {
        this.TW_Close = TW_Close;
    }

    public double getIPOPrice() {
        return IPOPrice;
    }

    public void setIPOPrice(double IPOPrice) {
        this.IPOPrice = IPOPrice;
    }

    public double getIPOTopRate() {
        return IPOTopRate;
    }

    public void setIPOTopRate(double IPOTopRate) {
        this.IPOTopRate = IPOTopRate;
    }

    public double getIPOBottomRate() {
        return IPOBottomRate;
    }

    public void setIPOBottomRate(double IPOBottomRate) {
        this.IPOBottomRate = IPOBottomRate;
    }

    public double getBuyFirstPrice() {
        return BuyFirstPrice;
    }

    public void setBuyFirstPrice(double buyFirstPrice) {
        BuyFirstPrice = buyFirstPrice;
    }

    public double getSellFirstPrice() {
        return SellFirstPrice;
    }

    public void setSellFirstPrice(double sellFirstPrice) {
        SellFirstPrice = sellFirstPrice;
    }

    public int getBuyVolume() {
        return BuyVolume;
    }

    public void setBuyVolume(int buyVolume) {
        BuyVolume = buyVolume;
    }

    public int getSellVolume() {
        return SellVolume;
    }

    public void setSellVolume(int sellVolume) {
        SellVolume = sellVolume;
    }

    public int getSellFirstCount() {
        return SellFirstCount;
    }

    public void setSellFirstCount(int sellFirstCount) {
        SellFirstCount = sellFirstCount;
    }

    public int getBuyFirstCount() {
        return BuyFirstCount;
    }

    public void setBuyFirstCount(int buyFirstCount) {
        BuyFirstCount = buyFirstCount;
    }

    public int getRefreshTime() {
        return RefreshTime;
    }

    public void setRefreshTime(int refreshTime) {
        RefreshTime = refreshTime;
    }

    public String getRefreshTime2() {
        return RefreshTime2;
    }

    public void setRefreshTime2(String refreshTime2) {
        RefreshTime2 = refreshTime2;
    }

    public double getAllMoney() {
        return AllMoney;
    }

    public void setAllMoney(double allMoney) {
        AllMoney = allMoney;
    }

    public double getMaxPrice() {
        return MaxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        MaxPrice = maxPrice;
    }

    public double getMinPrice() {
        return MinPrice;
    }

    public void setMinPrice(double minPrice) {
        MinPrice = minPrice;
    }

    public double getNowMoney() {
        return NowMoney;
    }

    public void setNowMoney(double nowMoney) {
        NowMoney = nowMoney;
    }

    public int getNowCount() {
        return NowCount;
    }

    public void setNowCount(int nowCount) {
        NowCount = nowCount;
    }

    public int getDeputeBCount() {
        return DeputeBCount;
    }

    public void setDeputeBCount(int deputeBCount) {
        DeputeBCount = deputeBCount;
    }

    public int getDeputeSCount() {
        return DeputeSCount;
    }

    public void setDeputeSCount(int deputeSCount) {
        DeputeSCount = deputeSCount;
    }



}
