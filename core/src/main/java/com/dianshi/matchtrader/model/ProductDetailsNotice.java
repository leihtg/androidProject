package com.dianshi.matchtrader.model;

import java.util.List;

/**
 * 产品的五档详情
 */
public class ProductDetailsNotice {

    private int Id;

    /// <summary>
    /// Ticks列表
    /// </summary>

    /**
     * Ticks列表
     */
    private List<TickNotice> TL;

    /**
     * 委买行情
     */
    private List<DeputeModel> BL;

    /**
     * 委卖行情
     */
    private List<DeputeModel> SL;




    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public List<TickNotice> getTL() {
        return TL;
    }

    public void setTL(List<TickNotice> TL) {
        this.TL = TL;
    }

    public List<DeputeModel> getBL() {
        return BL;
    }

    public void setBL(List<DeputeModel> BL) {
        this.BL = BL;
    }

    public List<DeputeModel> getSL() {
        return SL;
    }

    public void setSL(List<DeputeModel> SL) {
        this.SL = SL;
    }
}
