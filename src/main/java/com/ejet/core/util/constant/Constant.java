package com.ejet.core.util.constant;

public class Constant {
	/*成功*/
	public static String SUCCESS = "success";
	/*失败*/
	public static String ERROR = "error";
	/*主观题(中文)*/
	public static String ZHUGUANTI = "主观题";
	/*客观题(中文)*/
	public static String KEGUANGTI = "客观题";
	/*判断题(中文)*/
	public static String PANDUANTI = "判断题";
	/*单选(编号)*/
	public static String DANXUANTI_NUM = "0";
	/*多选(编号)*/
	public static String DUOXUANTI_NUM = "1";
	/*判断(编号)*/
	public static String PANDUANTI_NUM = "2";
	/*数字(编号)*/
	public static String SHUZITI_NUM = "3";
	/*主观题(编号)*/
	public static String ZHUGUANTI_NUM = "4";
	/*主观题(数字)*/
	public static String QUSETIONTYPEONE = "qusetiontypeone";
	/*客观题(英文)*/
	public static String QUSETIONTYPETWO = "qusetiontypetwo";
	/*判断题(英文)*/
	public static String QUSETIONTYPETHREE = "qusetiontypethree";
	/*已绑定状态*/
	public static String BING_YES = "1";
	/*未绑定状态*/
	public static String BING_NO = "0";
	/*已考勤*/
	public static final String ATTENDANCE_YES = "YES";
	/*未考勤*/
    public static final String ATTENDANCE_NO = "NO";
    
	public static final int SEND_SUCCESS = 0 ;//
	
    public static final int SEND_ERROR = -1 ; //
    /* 字符题型*/
    public static final String ANSWER_CHAR_TYPE = "char";
    /* 数字题型*/
    public static final String ANSWER_NUMBER_TYPE = "number";
    /* 判断答案*/
    public static final String ANSWER_JUDGE_TYPE = "judge";
    /* 多选答案*/
    public static final String ANSWER_MULTIPLE_TYPE = "multiple";
    /* 随堂检测客观题*/
    public static final String ANSWER_CLASS_TEST_OBJECTIVE = "class_test_objective";
    /* 随堂检测主观题*/
    public static final String ANSWER_CLASS_TEST_SUBJECTIVE = "class_test_subjective";
    /*字符题型*/
    public static final String SINGLE_ANSWER_CHAR = "[{'type':'s','id':'1','range':'A-D'}]";
    /* 数字题型*/
    public static final String SINGLE_ANSWER_NUMBER = "[{'type':'d','id':'1','range':'0-9'}]";
    /* 判断答案*/
    public static final String SINGLE_ANSWER_JUDGE = "[{'type':'j','id':'1','range':''}]";
    /*考勤抢答用*/
    public static final String ANSWER_STR = "[{'type':'g','id':'1','range':''}]";
    /*试卷题目  状态启用*/
    public static final String STATUS_ENABLED = "1";
    /*试卷题目   未启用*/
    public static final String STATUS_NOT_ENABLED = "0";
    
}
