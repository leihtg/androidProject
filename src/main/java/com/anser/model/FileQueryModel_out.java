package com.anser.model;

import java.util.List;

import com.anser.model.base.ModelOutBase;

/**
 * 返回参数
 * 
 * @author leihuating
 * @time 2018年1月17日 下午5:49:27
 */

public class FileQueryModel_out extends ModelOutBase {
	private List<FileModel> list;

	public List<FileModel> getList() {
		return list;
	}

	public void setList(List<FileModel> list) {
		this.list = list;
	}

}
