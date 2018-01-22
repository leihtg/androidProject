/**
 * 
 */
package com.anser.model;

import com.anser.model.base.ModelInBase;

/**
 * 文件上传参数
 * 
 * @author leihuating
 * @time 2018年1月22日 下午1:51:41
 */
public class FileTransfer_in extends ModelInBase {
	private long length;
	private long pos;
	private String name;
	private String path;
	private byte[] buf;

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public long getPos() {
		return pos;
	}

	public void setPos(long pos) {
		this.pos = pos;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public byte[] getBuf() {
		return buf;
	}

	public void setBuf(byte[] buf) {
		this.buf = buf;
	}

}
