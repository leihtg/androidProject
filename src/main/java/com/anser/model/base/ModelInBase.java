package com.anser.model.base;

import com.anser.enums.ActionType;

/**
 * 向服务器传送数据model的基类 将几乎所有接口访问需要上传的接口的参数
 * 
 * @author leihuating
 * @time 2018年1月17日16:52:18
 */
public class ModelInBase {
	private String uuid;
	private ActionType busType;// 业务类型

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public ActionType getBusType() {
		return busType;
	}

	public void setBusType(ActionType busType) {
		this.busType = busType;
	}

}
