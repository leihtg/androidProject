package dianshi.matchtrader.model;

/**
 * Created by Administrator on 2016/5/9 0009.
 */
public class TradeKillOrderModel {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 名称
     */
    private String name;
    /**
     * 时间
     */
    private String time;
    /**
     * 均价
     */
    private String price;

    /**
     * 委托
     */
    private String commission;
    /**
     * 成交
     */
    private String trade;
    /**
     * 类型
     */
    private String type;


    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
