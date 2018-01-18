package com.anser.business;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;

import com.anser.annotation.BusinessType;
import com.anser.contant.MsgType;

/**
 * 文件查询业务
 * 
 * @author leihuating
 * @time 2018年1月18日 下午1:15:02
 */
public class FileQuery {
	@BusinessType(msgType = MsgType.FETCH_DIR)
	public void call(@BusinessType(msgType=3) Date a,String s) {

	}

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IOException {
		ClassLoader loader = FileQuery.class.getClassLoader();
		Enumeration<URL> rs = loader.getResources("com/anser/annotation");
		while(rs.hasMoreElements()) {
			System.out.println(rs.nextElement());
		}
		System.out.println();
	}
	
	static void printType(AnnotatedType[] a) {
		for (AnnotatedType aa : a) {
			System.out.println(aa.getType());
		}
	}

	static void print(Annotation[] a) {
		for (Annotation aa : a) {
			System.out.println(aa);
		}
	}
}
