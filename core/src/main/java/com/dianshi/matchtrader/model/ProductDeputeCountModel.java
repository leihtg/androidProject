package com.dianshi.matchtrader.model;

/**
 * 下单的产品的信息变化?
 * 为什么要特意器更新买一和卖一价
 */
public class ProductDeputeCountModel {

    /**
     * 买单的下单量
     */
    private int DBC;

    /**
     * 卖单的下单量
     */
    private int DSC;
    /**
     * 买一量
     */
    private int BFC ;

    /**
     * 卖一量
     */
    private int SFC;
    /**
     * 买一价
     */
    private double BFP;

    /**
     * 卖一价
     */
    private double SFP ;




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

    public int getBFC() {
        return BFC;
    }

    public void setBFC(int BFC) {
        this.BFC = BFC;
    }

    public double getBFP() {
        return BFP;
    }

    public void setBFP(double BFP) {
        this.BFP = BFP;
    }

    public int getSFC() {
        return SFC;
    }

    public void setSFC(int SFC) {
        this.SFC = SFC;
    }

    public double getSFP() {
        return SFP;
    }

    public void setSFP(double SFP) {
        this.SFP = SFP;
    }
}
