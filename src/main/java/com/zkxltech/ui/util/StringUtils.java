package com.zkxltech.ui.util;

import java.util.List;

import net.sf.json.JSONObject;

public class StringUtils {
	/**
	 * 参数转换
	 * object 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static <T> T stringToBean(Object object,Class clazz) throws InstantiationException, IllegalAccessException{
		return (T) JSONObject.toBean(JSONObject.fromObject(object), clazz);
	}
	
	/**
	 * 字符串判空
	 */
	public static boolean isEmpty(String str){
		return str == null || str.length() == 0;
		
	}
	/**
	 * 数字判空
	 */
	public static boolean isEmpty(Integer i){
		return i == null;
	}
	/**
	 * 数组判空
	 */
	public static boolean isEmpty(List<Object> list){
		return list == null || list.size() == 0;
	}
}
