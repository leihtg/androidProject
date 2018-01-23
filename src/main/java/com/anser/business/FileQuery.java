package com.anser.business;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.anser.annotation.BusinessType;
import com.anser.annotation.Scope;
import com.anser.business.inter.BusinessInter;
import com.anser.contant.Contant;
import com.anser.contant.ReceiveData;
import com.anser.enums.MsgType;
import com.anser.enums.ScopeType;
import com.anser.model.FileModel;
import com.anser.model.FileQueryModel_in;
import com.anser.model.FileQueryModel_out;
import com.anser.model.base.ModelOutBase;

/**
 * 文件查询业务
 * 
 * @author leihuating
 * @time 2018年1月18日 下午1:15:02
 */
@Scope(ScopeType.singleton)
public class FileQuery implements BusinessInter {

	@Override
	@BusinessType(MsgType.FETCH_DIR)
	public ModelOutBase call(ReceiveData rd) {
		FileQueryModel_in fm = gson.fromJson(rd.data, FileQueryModel_in.class);
		File file = new File(Contant.HOME_DIR, fm.getPath());
		FileQueryModel_out out = new FileQueryModel_out();
		out.setList(listFile(file));
		return out;
	}

	private List<FileModel> listFile(File file) {
		List<FileModel> dirs = new ArrayList<>();
		List<FileModel> files = new ArrayList<>();

		for (File f : file.listFiles()) {
			if (!f.canRead()) {
				continue;
			}
			FileModel m = new FileModel();
			m.setName(f.getName());
			m.setLastModified(f.lastModified());
			m.setLength(f.length());
			m.setDir(f.isDirectory());
			if (f.isDirectory()) {
				dirs.add(m);
			} else {
				files.add(m);
			}
		}
		dirs.addAll(files);
		return dirs;
	}

}
