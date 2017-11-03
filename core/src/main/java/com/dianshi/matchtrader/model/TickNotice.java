package com.dianshi.matchtrader.model;

/**
 * 产品下单明细
 */
public class TickNotice {
    /**
     * 下单时间
     */
    private String T;

    /**
     * 价格
     */
    private double P;

    /**
     * 成交价格
     */
    private int TP;

    /**
     * 数量
     */
    private int C;






    public String getT() {
        return T;
    }

    public void setT(String t) {
        T = t;
    }

    public double getP() {
        return P;
    }

    public void setP(double p) {
        P = p;
    }

    public int getC() {
        return C;
    }

    public void setC(int c) {
        C = c;
    }

    public int getTP() {
        return TP;
    }

    public void setTP(int TP) {
        this.TP = TP;
    }
}
