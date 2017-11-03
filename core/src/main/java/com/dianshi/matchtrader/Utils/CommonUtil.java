package com.dianshi.matchtrader.Utils;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import android.content.Context;

/**
 * MD5
 * 
 * @author 钱智慧[Email:qzhforthelife@163.com]
 * @date 2014-10-22 下午3:03:03
 */
public class CommonUtil {
	/**
	 * 对Map按Key升序排序
	 * 
	 * @param oriMap 要排序的map
	 * @return
	 */
	public static Map<String, String> sortMapByKey(Map<String, String> oriMap) {
		if (oriMap == null || oriMap.isEmpty()) {
			return null;
		}
		Map<String, String> sortedMap = new TreeMap<String, String>(
				new Comparator<String>() {
					@Override
					public int compare(String lhs, String rhs) {
						return lhs.compareToIgnoreCase(rhs);
					}
				});
		sortedMap.putAll(oriMap);
		return sortedMap;
	}

	/**
	 * 将名称值对转为url后缀查询串
	 * 
	 * @param
	 * @return
	 * @author 钱智慧[Email:qzhforthelife@163.com]
	 * @date 2014-10-22 下午5:35:51
	 */
	public static String map2QueryStr(Map<String, String> map) {
		String queryStr = "";
		for (String key : map.keySet()) {
			queryStr += (key + "=" + map.get(key));
			queryStr += "&";
		}
		if (queryStr != "") {
			queryStr = queryStr.substring(0, queryStr.length() - 1);
		}

		return queryStr;
	}



	public static boolean isInputInRange(int from, int to, String input) {
		boolean isIn = true;
		try {
			int r = Integer.parseInt(input);
			if (r < from || r > to) {
				isIn = false;
			}
		} catch (Exception e) {
			isIn = false;
		}
		return isIn;
	}

	public static boolean isInputInRange(int from, int to, int input) {
		boolean isIn = true;
		try {
			int r = input;
			if (r < from || r > to) {
				isIn = false;
			}
		} catch (Exception e) {
			isIn = false;
		}
		return isIn;
	}

	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
    

}
