package dianshi.matchtrader.model;

/**
 * Created by Administrator on 2016/5/9 0009.
 */
public class TradeMoneyModel {

    /**
     * 买入/卖出操作的种类
     */

    private String sellKind;
    /**
     * 买入/卖出的单价
     */

    private String price;
    /**
     * 买入/卖出操作的总价
     */

    private String total;


    public String getSellKind() {
        return sellKind;
    }

    public void setSellKind(String sellKind) {
        this.sellKind = sellKind;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
