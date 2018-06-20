package com.zkxltech.teaching.msg;

/**
 * 答题器,答题回复对象信息
 * 
 * @author ShenYijie
 *
 */
public class AnswerResponse extends BaseRequestMessage {
	/**
	 * 班级信息
	 */
	private String classId;
	/** 答题器id */
	private String iclickerId;
	
	/** 试卷Id */
	private String examPaperId;
	/** 题目Id */
	private String questionId;
	/** 
	 * 题目类型
	 * 1：单题单选
	 * 2: 是非判断
	 * 3: 抢红包
	 * 4: 单题多选
	 * 5: 多题单选
	 * 6: 通用数据类型（所有按键都可以按，按下之后立刻提交）
	 * 7: 6键单选题（对错键复⽤EF键）
	 *  */
	private String answerType;
	/** 答题器时间 （指令接收器时间）*/
	private String answerDateTime;
	/**
	 * 答案
	 */
	private String answer;
	/**
	 * 教师端接收到的时间
	 */
	private String recvTime;
	/**
	 * 指令ID防止重复提交
	 */
	private Integer rssi;
	
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getIclickerId() {
		return iclickerId;
	}
	public void setIclickerId(String iclickerId) {
		this.iclickerId = iclickerId;
	}
	public String getExamPaperId() {
		return examPaperId;
	}
	public void setExamPaperId(String examPaperId) {
		this.examPaperId = examPaperId;
	}
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getAnswerType() {
		return answerType;
	}
	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}
	public String getAnswerDateTime() {
		return answerDateTime;
	}
	public void setAnswerDateTime(String answerDateTime) {
		this.answerDateTime = answerDateTime;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getRecvTime() {
		return recvTime;
	}
	public void setRecvTime(String recvTime) {
		this.recvTime = recvTime;
	}
	public Integer getRssi() {
		return rssi;
	}
	public void setRssi(Integer rssi) {
		this.rssi = rssi;
	}
	
	
	
	

}
