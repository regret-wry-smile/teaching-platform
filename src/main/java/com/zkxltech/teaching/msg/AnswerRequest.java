package com.zkxltech.teaching.msg;

import java.util.List;

/**
 * 下发答题器信息
 * 
 * @author ShenYijie
 *
 */
public class AnswerRequest extends BaseRequestMessage {
	/** 是否广播 */
	private boolean isBroadcast;
	/** 试卷Id */
	private String examPaperId;
	/** 题目Id */
	private String questionId;
	/** 
	 * (硬件上的)题目类型
	 * 1：单题单选
	 * 2: 是非判断
	 * 3: 抢红包
	 * 4: 单题多选
	 * 5: 多题单选
	 * 6: 通用数据类型（所有按键都可以按，按下之后立刻提交）
	 * 7: 6键单选题（对错键复⽤EF键）
	 *  */
	private String questionType;
	/**
	 * (业务上的)题目类型
	 * 1:客观题
	 * 2:主观题
	 * 3:判断题
	 *
	 */
	private String businessQusetionType; //业务上的答题类型
	
	/** 答题器id （如果广播，则此项无效） */
	private List<String> iclickerId;

	public boolean isBroadcast() {
		return isBroadcast;
	}

	public void setBroadcast(boolean isBroadcast) {
		this.isBroadcast = isBroadcast;
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

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public List<String> getIclickerId() {
		return iclickerId;
	}

	public void setIclickerId(List<String> iclickerId) {
		this.iclickerId = iclickerId;
	}

	public String getBusinessQusetionType() {
		return businessQusetionType;
	}

	public void setBusinessQusetionType(String businessQusetionType) {
		this.businessQusetionType = businessQusetionType;
	}
	
	
}
