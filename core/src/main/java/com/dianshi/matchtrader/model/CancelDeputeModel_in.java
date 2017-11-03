package com.dianshi.matchtrader.model;

/**
 * 交易撤单输入model 参数
 */
public class CancelDeputeModel_in extends ModelInBase{
    private int DeputeId;

    public int getDeputeId() {
        return DeputeId;
    }

    public void setDeputeId(int deputeId) {
        DeputeId = deputeId;
    }
}
