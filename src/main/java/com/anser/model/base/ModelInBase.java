package com.anser.model.base;

/**
 * 向服务器传送数据model的基类 将几乎所有接口访问需要上传的接口的参数
 * 
 * @author leihuating
 * @time 2018年1月17日16:52:18
 */
public class ModelInBase {
	private String uuid;
	private int busType;// 业务类型

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getBusType() {
		return busType;
	}

	public void setBusType(int busType) {
		this.busType = busType;
	}

}
