package com.dianshi.matchtrader.Utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 钱智慧[Email:qzhforthelife@163.com]
 * @date 2014-10-22 下午5:31:51
 */
public class ReflectUtil {
	/**
	 * 反射得到一个对象的所有字段的值，以名值对的形式返回
	 * 目前只支持到直接父类
	 * @param
	 * @return
	 * @author 钱智慧[Email:qzhforthelife@163.com]
	 * @date 2014-10-22 下午5:30:40
	 */
	public static Map<String,String> reflectToMap(Object obj){
		Map<String,String> hashMap=new HashMap<String, String>();
		Field[] fields = obj.getClass().getDeclaredFields();
		Field[] baseFields=obj.getClass().getSuperclass().getDeclaredFields();
		Field[] fArr=new Field[fields.length+baseFields.length];
		System.arraycopy(fields, 0, fArr, 0, fields.length);
		System.arraycopy(baseFields, 0, fArr, fields.length, baseFields.length);
		for(Field f:fArr){
			f.setAccessible(true);
			hashMap.put(f.getName(), getValue(f, obj));
		}
		return hashMap;
	}
	/**
	 * 反射得到一个Obj的字段f的Value，目前只支持三种字段类型:String,int(不支持Integer),bool(不支持Boolean)
	 * @param
	 * @return
	 * @author 钱智慧[Email:qzhforthelife@163.com]
	 * @date 2014-10-22 下午5:27:44
	 */
	private static String getValue(Field f, Object obj) {
		String val = "";
		String typeName = f.getType().getName();
		try {
			if (typeName.equals(String.class.getName())) {// String type
				val = (String) f.get(obj);
			} else if (typeName.equals("int")) {//int
				val = f.getInt(obj) + "";
			} else if (typeName.equals("boolean")) {//boolean
				val = f.getBoolean(obj)==true ? "true" : "false";
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return val;
	}
}
