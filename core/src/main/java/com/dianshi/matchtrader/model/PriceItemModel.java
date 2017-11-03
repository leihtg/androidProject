package com.dianshi.matchtrader.model;

/**
 * Created by Administrator on 2016/5/18.
 */
public class PriceItemModel {
    /// <summary>
    /// Price
    /// </summary>
    private double P ;

    /// <summary>
    /// Time
    /// </summary>
    private int T ;

    /**
     * count
     */
    private int C ;

    public double getP() {
        return P;
    }

    public void setP(double p) {
        P = p;
    }

    public int getT() {
        return T;
    }

    public void setT(int t) {
        T = t;
    }

    public int getC() {
        return C;
    }

    public void setC(int c) {
        C = c;
    }
}
