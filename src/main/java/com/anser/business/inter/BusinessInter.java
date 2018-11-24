package com.anser.business.inter;

import com.anser.contant.ReceiveData;
import com.anser.model.base.ModelOutBase;
import com.google.gson.Gson;

/**
 * 业务接口
 *
 * @author lht
 * @time 2018年1月20日 下午12:30:49
 */
public interface BusinessInter {
    static Gson gson = new Gson();

    ModelOutBase call(ReceiveData rd);
}
