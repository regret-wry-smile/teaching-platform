//package com.zkxltech.teaching.server;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.junit.Test;
//
//import com.ejet.cache.TeachingCache;
//
//import net.sf.json.JSONObject;
//
//public class DataTest {
//	/**
//	 * 每个答案选择详情
//	 */
//	public static void main(String[] args) {
//			new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				List<String> answerInfos = new ArrayList<String>();
//
//				answerInfos.add("张三");
//				while (true) {
//					TeachingCache.answerMap.put("A", answerInfos);
//					TeachingCache.answerMap.put("B", answerInfos);
//					TeachingCache.answerMap.put("C", answerInfos);
//					TeachingCache.answerMap.put("D", answerInfos);
//					System.out.println("当前缓存数据:"+JSONObject.fromObject(TeachingCache.answerMap));
//					try {
//						Thread.sleep(2000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		}).start();
//	}
//	
//	
//
//}
