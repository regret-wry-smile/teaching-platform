package com.ejet.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.poi.ss.formula.functions.T;

import com.ejet.core.util.constant.Constant;

import net.sf.json.JSONObject;

public class StringUtils {
	
	/**  
     * 自动生成32位的UUid，对应数据库的主键id进行插入用。  
     * @return  
     */  
    public static String getUUID() {  
        return UUID.randomUUID().toString().replace("-", "");  
    }  
    
    /**  
     * json转换对象  
     * @return  
     */  
    public static  <T>  T parseToBean(Object json,Class<T> clazz) {  
    	if (json == null || "".equals(json)) {
    		json = "{}";
		}
        return (T)JSONObject.toBean(JSONObject.fromObject(json), clazz);  
    } 
    
    /**  
     * int 转换 boolean
     * 1 返回true
     * 其它值为false
     * @return  
     */  
    public static  boolean intToBoolen(int data) {  
        return data == 1;  
    } 
    
    /**  
     * int 转换 boolean
     * SUCCESS 返回 1
     * 其它值为 0
     * @return  
     */  
    public static int StringToInt(String str) {  
        return Constant.SUCCESS.equals(str) ? 1 : 0;  
    } 
    
    /**  
     * int 转换 boolean
     * SUCCESS 返回 true
     * 其它值为 false
     * @return  
     */  
    public static boolean StringToBoolen(String str) {  
        return Constant.SUCCESS.equals(str);  
    } 
    /**
     * 字符串是否为空
     */
    public static boolean isEmpty(String str){
		return str == null || str.length() == 0;
	}
    
    /**
     * 字符串是否为空
     */
    public static boolean isEmpty(Integer num){
		return num == null;
	}
    
    /**
     * object是否为空
     */
    public static boolean isEmpty(Object obj){
    	if(obj == null) return true;
    	if (obj instanceof String) {
    		return isEmpty((String) obj);
		}
    	if (obj instanceof Integer) {
    		return isEmpty((Integer) obj);
		}
    	return false;
	}
    
    /**
     * 时间转换string格式
     */
    public static String formatDateTime(Date date){
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(date);
	}
    
    /**
     * 时间转换string格式
     */
    public static String formatDateTime(Date date,String pattern){
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}
    /**
     * string转换时间格式
     * @throws ParseException 
     */
    public static Date parseDateTime(String str) throws ParseException{
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    	
		return simpleDateFormat.parse(str);
	}
}
