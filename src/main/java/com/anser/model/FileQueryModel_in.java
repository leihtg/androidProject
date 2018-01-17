package com.anser.model;

import com.anser.model.base.ModelInBase;

/**
 * 查询文件列表的参数
 * 
 * @author lht
 * @time 2018年1月14日 下午6:18:19
 */
public class FileQueryModel_in extends ModelInBase {
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
