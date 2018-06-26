package com.ejet.core.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RedisMapUtil {
	//线程安全
	public static Map<String, Object> redisMap = Collections.synchronizedMap(new HashMap<String, Object>());
	/**
	 * @deprecated
	 * 获取缓存
	 * @param key
	 * @return
	 */
	public static Object getRedisMap(String key){
		return redisMap.containsKey(key) ? redisMap.get(key) : null;
	};
	/**
	 * 获取缓存
	 * @param map 缓存
	 * @param keys 缓存对应节点
	 * @param index 起始节点索引
	 * @return
	 */
	public static Object getRedisMap(Map<String, Object> map,String[] keys,int index){
		Object obj= map.get(keys[index]);
			if (index != keys.length -1) {
				if (obj instanceof Map) {
					map = (Map<String, Object>)obj;
				}
				return getRedisMap(map,keys, ++index);
			}
		return obj;
	};
	/**
	 * @deprecated
	 * @param keys 缓存节点关键字
	 * @param value 要改为的值
	 */
	public static void setRedisMap(String[] keys,Object value){
		Object object = getRedisMap(keys[0]);
		Object[] objects = new Object[keys.length-1];
		int index = 1;
		while(object != null && index != keys.length) {
			objects[index-1] = object;
			Map<String,Object> map = null;
			if (object instanceof Map) {
				map = (Map<String, Object>) object;
				if (index == keys.length-1) {
					map.put(keys[index], value);
				}else {
					object = map.get(keys[index]);
				}
			}else {
				break;
			}
			index ++;
		}
	}
	/**
	 * 修改缓存对应的值
	 * @param map 缓存对象 
	 * @param keys 缓存对应节点
	 * @param index 相对于keys开始的节点索引
	 * @param value 要改为的值
	 * @return
	 */
	public static Map<String, Object> setRedisMap(Map<String, Object> map ,String[] keys,int index,Object value){
		Map<String, Object> retMap = null;
		if (index != keys.length) {
			Object obj =  map.get(keys[index]);
			if (obj == null ) { //map有key没有对应的值
				setRedisMapOrNull(map,keys,index,value);
//				throw new Exception("this key of '"+ keys[index] +"' no vlaue！");
			}else {
				if (obj instanceof Map) {
					retMap = (Map<String, Object>) obj;
				}
				if (index != keys.length - 2) {
					setRedisMap(retMap, keys, ++index, value);
				}else {
					retMap.put(keys[++index], value);
				}
			}
		}
		
		return retMap;
	}
	
	public static Map<String, Object> setRedisMapOrNull(Map<String, Object> map ,String[] keys,int index,Object value){
		Map<String, Object> retMap = map;
		if (index != keys.length-1) {
//			System.out.println(keys[index]);
			retMap.put(keys[index], new HashMap<String, Object>());
			retMap = (Map<String, Object>) retMap.get(keys[index]);
			setRedisMapOrNull(retMap,keys,++index,value);
//			System.out.println(JSONObject.fromObject(retMap));
		}else {
			retMap.put(keys[index], value);
//			System.out.println(JSONObject.fromObject(retMap));
		}
		return retMap;
	}

	
//	public static void main(String[] args) {
//		Map<String, Map<String, Map<String, Object>>> map = new HashMap<String, Map<String,Map<String,Object>>>();
//		Map<String, Map<String, Object>> map2 = new HashMap<String, Map<String,Object>>();
//		Map<String,Object> map3 = new HashMap<String, Object>();
//		map3.put("第一题", "1+1=?");
//		map3.put("第二题", "1+2=?");
//		Map<String,Object> map4 = new HashMap<String, Object>();
//		map4.put("第一题", "1+1=?");
//		map4.put("第二题", "1+2=?");
//		map2.put("学生1001", map3);
//		map2.put("学生1002", map4);
//		map.put("三班", map2);
//		redisMap.put("客观题", map);
//		String[] keys = {"客观题","三班","学生1001","第二题"};
//		System.out.println(getRedisMap(redisMap, keys, 1));
//	}
	
	public static void main(String[] args) {
		Map<String, Map<String, Map<String, Object>>> map = new HashMap<String, Map<String,Map<String,Object>>>();
		Map<String, Map<String, Object>> map2 = new HashMap<String, Map<String,Object>>();
		Map<String,Object> map3 = new HashMap<String, Object>();
		map3.put("第一题", "1+1=?");
		map3.put("第二题", "1+2=?");
		Map<String,Object> map4 = new HashMap<String, Object>();
		map4.put("第一题", "1+1=?");
		map4.put("第二题", "1+2=?");
		map2.put("学生1001", map3);
		map2.put("学生1002", map4);
		map.put("三班", map2);
		redisMap.put("客观题", map);
		String[] keys = {"客观题","三班","学生1002","第二题"};
		String[] keys2 = {"客观题","三班","学生1003","第二题"};
		String[] keys4 = {"客观题","第二题"};
		setRedisMap(redisMap,keys,0,"改为第四题"); 
		System.out.println(redisMap);
		setRedisMap(redisMap,keys2,0,"改为第四题");
		System.out.println(redisMap);

		System.out.println(getRedisMap(redisMap, keys2, 0));;
//		System.out.println(JSONObject.fromObject(redisMap));
	}
}
