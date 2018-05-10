package com.ejet.core.util;

public class StringUtils {
	/**
	 * (硬件上的)答题类型转换  中文转对应类型编号
	 * @param type
	 * @return
	 *  * 题目类型
	 * 1：单题单选
	 * 2: 是非判断
	 * 3: 抢红包
	 * 4: 单题多选
	 * 5: 多题单选
	 * 6: 通用数据类型（所有按键都可以按，按下之后立刻提交）
	 * 7: 6键单选题（对错键复⽤EF键）
	 */
	public static String changeQusetionTypeToNum(String type){
		String retString = "";
		switch (type) {
		case "主观题":
			retString = "2";
			break;
		case "客观题":
			retString = "1";
			break;
		case "判断题":
			retString = "2";
			break;
		}
		return retString;
	}
	/**
	 * (业务上的)答题类型转换  中文转对应类型编号
	 * @param type
	 * @return
	 *  * 题目类型
	 * 1:客观题
	 * 2:主观题
	 * 3:判断题
	 */
	public static String changeBusinessQusetionTypeToNum(String type){
		String retString = "";
		switch (type) {
		case "客观题":
			retString = "1";
			break;
		case "主观题":
			retString = "2";
			break;
		case "判断题":
			retString = "3";
			break;
		}
		return retString;
	}
	/**
	 * 将页面选中的答案转换为缓存中对应的答案类型
	 * @param type
	 * @return
	 * 选A --->A
	 * 选B --->B
	 * 选C --->C
	 * 选D --->D
	 * 懂     --->Y
	 * 不懂 --->N
	 * 对     --->Y
	 * 错     --->N
	 */
	public static String changeAnswer(String type){
		String retString = "";
		switch (type) {
		case "选A":
			retString = "A";
			break;
		case "选B":
			retString = "B";
			break;
		case "选C":
			retString = "C";
			break;
		case "选D":
			retString = "D";
			break;
		case "懂":
			retString = "Y";
			break;
		case "不懂":
			retString = "N";
			break;
		case "对":
			retString = "Y";
			break;
		case "错":
			retString = "N";
			break;
		}
		return retString;
	}
}
