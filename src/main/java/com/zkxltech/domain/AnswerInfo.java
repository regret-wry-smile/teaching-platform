package com.zkxltech.domain;

/**
 * 答题信息
 *
 */
public class AnswerInfo {
	/*主键*/
	private Integer id;
	/*学生id*/
	private String studentId;
	/*学生名字*/
	private String studentName;
	/*选择答案*/
	private String answer;
	/*答题结果*/
	private String result;
	/*答题时间*/
	private String answerDatetime;
	/*试卷id*/
	private String testId;
	/*题目编号*/
	private String questionId;
	/*题目类型*/
	private String qusetionType;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	public String getAnswerDatetime() {
		return answerDatetime;
	}
	public void setAnsweDatetime(String answerDatetime) {
		this.answerDatetime = answerDatetime;
	}
	public String getTestId() {
		return testId;
	}
	public void setTestId(String testId) {
		this.testId = testId;
	}
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getQusetionType() {
		return qusetionType;
	}
	public void setQusetionType(String qusetionType) {
		this.qusetionType = qusetionType;
	}
	
}
