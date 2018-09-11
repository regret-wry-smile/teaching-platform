package com.zkxltech.teaching.ui;

/**
 * 模拟答题器发送答案
 * @author zkxl
 *
 */
public class TestMachineThread {
//	public static void main(String[] args) {
//		String url = "http://192.168.1.138:8844/answer";
//		
//		
//		//替换server线程
//		//		String method = "attend";
//					String method = "answer";
//	//					String method = "score";
//				//	String method = "abc";
//					//								String method = "vote";
//							String type = "字母题";
//							//				String type = "多选题";
////										String type = "数字题";
//										//						String type = "判断题";
//		Thread t = new MyThread(url,method,1,type);
//		t.start();
////		NettyServerHandler.map.put("thread", t);
//	}
	
	public static void test(){
		String url = "http://192.168.1.138:8844/answer";
		//替换server线程
		//		String method = "attend";
					String method = "answer";
	//					String method = "score";
				//	String method = "abc";
					//								String method = "vote";
							String type = "字母题";
//												String type = "多选题";
//										String type = "数字题";
										//						String type = "判断题";
		Thread t = new MyThread(url,method,1,type);
		t.start();
	}
		
}
