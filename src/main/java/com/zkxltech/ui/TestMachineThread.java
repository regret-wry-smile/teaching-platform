package com.zkxltech.ui;

/**
 * 模拟答题器发送答案
 * @author zkxl
 *
 */
public class TestMachineThread {
	
	private static Thread thread;
	/**
	 * 
	 * @param questionSum 题目个数
	 * @param type 作答类型
	 */
	public static void startThread(int questionSum, String type){
////		String type = "字母题";
//			String type = "多选题";
////		String type = "数字题";
//			//String type = "判断题";
		thread = new MyThread(questionSum,type,true);
		thread.start();
	}
	
	public static void stopThread(){
		MyThread.setFlag(false);
	}
		
}
