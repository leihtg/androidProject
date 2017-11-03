package com.dianshi.matchtrader.model;

import java.util.List;

/**
 * Created by Administrator on 2016/5/22.
 */
public class RegProductDetailModel_out extends ModelOutBase {
    private int ProductId;
    private List<KLineModel> KList;
    private List<DeputeModels> BuyDeputeList;
    private List<DeputeModels> SellDeputeList;
    private List<TickModel> TickList;

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public List<KLineModel> getKList() {
        return KList;
    }

    public void setKList(List<KLineModel> KList) {
        this.KList = KList;
    }

    public List<DeputeModels> getBuyDeputeList() {
        return BuyDeputeList;
    }

    public void setBuyDeputeList(List<DeputeModels> buyDeputeList) {
        BuyDeputeList = buyDeputeList;
    }

    public List<DeputeModels> getSellDeputeList() {
        return SellDeputeList;
    }

    public void setSellDeputeList(List<DeputeModels> sellDeputeList) {
        SellDeputeList = sellDeputeList;
    }

    public List<TickModel> getTickList() {
        return TickList;
    }

    public void setTickList(List<TickModel> tickList) {
        TickList = tickList;
    }
}
