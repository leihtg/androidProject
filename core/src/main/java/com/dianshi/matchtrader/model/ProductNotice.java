package com.dianshi.matchtrader.model;

import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
public class ProductNotice {
    private int Id ;

    /// <summary>
    /// 价格
    /// </summary>

    private double P ;

    /// <summary>
    /// NowCount
    /// 现量
    /// </summary>
    private int NC ;

    /// <summary>
    /// SellFistPrice
    /// 卖价
    /// </summary>
    private double SFP;

    /// <summary>
    /// SellFirstCount
    /// 一档卖量
    /// </summary>
    private int SFC;

    /// <summary>
    /// BuyFirstPrice
    /// </summary>
    private double BFP;

    /// <summary>
    /// BuyFirstCount
    /// 一档买量
    /// </summary>
    private int BFC ;

    /// <summary>
    /// AllCount
    /// </summary>
    private int AC;

    /// <summary>
    /// MaxPrice
    /// </summary>
    private double MxP;

    /// <summary>
    /// MinPrice
    /// </summary>
    private double MiP;

    /// <summary>
    /// AllMoney
    /// </summary>
    private double AM ;

    /// <summary>
    /// DeputeBuyCount
    /// </summary>
    private int DBC ;

    /// <summary>
    /// DeputeSellCount
    /// </summary>
    private int DSC ;

    /// <summary>
    /// BuyCount
    /// </summary>
    private int BC ;

    /// <summary>
    /// SellCount
    /// </summary>
    private int SC ;

    private List<PriceItemModel> PL;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public double getP() {
        return P;
    }

    public void setP(double p) {
        P = p;
    }

    public int getNC() {
        return NC;
    }

    public void setNC(int NC) {
        this.NC = NC;
    }

    public double getSFP() {
        return SFP;
    }

    public void setSFP(double SFP) {
        this.SFP = SFP;
    }

    public int getSFC() {
        return SFC;
    }

    public void setSFC(int SFC) {
        this.SFC = SFC;
    }

    public double getBFP() {
        return BFP;
    }

    public void setBFP(double BFP) {
        this.BFP = BFP;
    }

    public int getBFC() {
        return BFC;
    }

    public void setBFC(int BFC) {
        this.BFC = BFC;
    }

    public int getAC() {
        return AC;
    }

    public void setAC(int AC) {
        this.AC = AC;
    }

    public double getMxP() {
        return MxP;
    }

    public void setMxP(double mxP) {
        MxP = mxP;
    }

    public double getMiP() {
        return MiP;
    }

    public void setMiP(double miP) {
        MiP = miP;
    }

    public double getAM() {
        return AM;
    }

    public void setAM(double AM) {
        this.AM = AM;
    }

    public int getDBC() {
        return DBC;
    }

    public void setDBC(int DBC) {
        this.DBC = DBC;
    }

    public int getDSC() {
        return DSC;
    }

    public void setDSC(int DSC) {
        this.DSC = DSC;
    }

    public int getBC() {
        return BC;
    }

    public void setBC(int BC) {
        this.BC = BC;
    }

    public int getSC() {
        return SC;
    }

    public void setSC(int SC) {
        this.SC = SC;
    }

    public List<PriceItemModel> getPL() {
        return PL;
    }

    public void setPL(List<PriceItemModel> PL) {
        this.PL = PL;
    }
}
