package com.zkxltech.ui.util;

import java.util.List;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessor;
import net.sf.json.util.JSONUtils;

public class StringUtils {
	/**
	 * 字符串判空
	 */
	public static boolean isEmpty(Object obj){
		return obj == null || "".equals(obj);
		
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
	
	/**
	 * 解析参数
	 */
	public static <T>  T parseJSON(Object jsonData , Class<T> clazz){
		if(StringUtils.isEmpty(jsonData)){
			return null ;
		}
		JSONObject jsonObject = JSONObject.fromObject(jsonData,StringUtils.IntegerIsNull()) ;
		net.sf.json.util.JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[]{"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm:ss"}));
		Object obj = JSONObject.toBean(jsonObject, clazz) ;
		return (T)obj ;
	}
	/**
	 * 解析数字时默认为空字符串
	 * @return
	 */
	public static JsonConfig IntegerIsNull(){
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerDefaultValueProcessor(Integer.class, new DefaultValueProcessor() {	
			@Override
			public Object getDefaultValue(Class type) {
				 return "";
			}
		});
		return jsonConfig;
	}
}
