package dianshi.matchtrader.model;

/**
 * Created by Administrator on 2016/5/9 0009.
 */
public class TradePositionModel {

    /**
     * 名称
     */
    private String name;
    /**
     * 市值
     */
    private String marketValue;
    /**
     * 均价
     */
    private String averagePrice;
    /**
     * 现价
     */
    private String nowPrice;
    /**
     * 盈
     */
    private String profit;
    /**
     * 亏
     */
    private String profitRate;
    /**
     * 持仓
     */
    private String position;
    /**
     * 可用
     */
    private String available;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(String averagePrice) {
        this.averagePrice = averagePrice;
    }

    public String getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(String nowPrice) {
        this.nowPrice = nowPrice;
    }


    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(String profitRate) {
        this.profitRate = profitRate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(String marketValue) {
        this.marketValue = marketValue;
    }
}
