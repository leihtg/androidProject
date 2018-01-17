package com.anser.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;


/**
 * 传输过程中参数
 * 
 * @author lht
 * @time 2018年1月14日 下午6:11:40
 */
public class ParamHandle {
	private static final Gson mapper = new Gson();

	private static ParamHandle param = new ParamHandle();

	private ParamHandle() {
	}

	public static ParamHandle getInstance() {
		return param;
	}

	public FileParam transfer(InputStream is) {
		FileParam tp = null;
		if (null != is) {
			try {
				tp = mapper.fromJson(new InputStreamReader(is, "UTF-8"), FileParam.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return tp;
	}

}
