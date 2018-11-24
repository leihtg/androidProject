package com.anser.business;

import com.anser.annotation.BusinessType;
import com.anser.annotation.Scope;
import com.anser.business.inter.BusinessInter;
import com.anser.contant.ReceiveData;
import com.anser.enums.ActionType;
import com.anser.model.base.ModelOutBase;

/**
 * 心跳
 * 
 * @author lht
 * @time 2018年1月20日 下午6:02:42
 */
@Scope
public class BeatHeart implements BusinessInter {

	@Override
	@BusinessType(ActionType.BEAT_HEART)
	public ModelOutBase call(ReceiveData rd) {
		return new ModelOutBase();
	}

}
