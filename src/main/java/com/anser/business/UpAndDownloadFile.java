/**
 * 
 */
package com.anser.business;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.anser.annotation.BusinessType;
import com.anser.annotation.Scope;
import com.anser.business.inter.BusinessInter;
import com.anser.contant.Contant;
import com.anser.contant.ReceiveData;
import com.anser.enums.MsgType;
import com.anser.model.FileTransfer_in;
import com.anser.model.base.ModelOutBase;
import com.google.gson.JsonSyntaxException;

/**
 * @author leihuating
 * @time 2018年1月22日 下午1:37:36
 */
@Scope
public class UpAndDownloadFile implements BusinessInter {

	@Override
	@BusinessType(MsgType.DOWN_LOAD)
	public ModelOutBase call(ReceiveData rd) {

		return null;
	}

	RandomAccessFile raf = null;

	@BusinessType(MsgType.UP_LOAD)
	public ModelOutBase callUp(ReceiveData rd) {
		try {
			FileTransfer_in fi = gson.fromJson(rd.data, FileTransfer_in.class);
			if (raf == null) {
				File file = new File(Contant.HOME_DIR + File.separator + fi.getPath(), fi.getName());
				raf = new RandomAccessFile(file, "rw");
			}
			raf.seek(fi.getPos());
			raf.write(fi.getBuf());
			if (fi.getPos() + fi.getBuf().length == fi.getLength()) {
				raf.close();
				raf = null;
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
