package com.zkxltech.domain;
/**
 * 作答记录
 * @author zkxl
 *
 */
public class Record {
	/**
	 * 主键
	 */
	private Integer id;
	/**
	 * 课时表对应的主键
	 */
	private String classHourId;
	/**
	 * 试卷id
	 */
	private String testId;
	/**
	 * 题目id
	 */
	private String questionId;
	/**
	 * 题目名称
	 */
	private String question;
	/**
	 * 题目类型
	 */
	private String questionType;
	/**
	 * 学生id
	 */
	private String studentId;
	/**
	 * 学生名称
	 */
	private String studentName;
	/**
	 * 正确答案
	 */
	private String trueAnswer;
	/**
	 * 作答答案
	 */
	private String answer;
	/**
	 * 作答结果
	 */
	private String result;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getClassHourId() {
		return classHourId;
	}
	public void setClassHourId(String classHourId) {
		this.classHourId = classHourId;
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
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
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
	public String getTrueAnswer() {
		return trueAnswer;
	}
	public void setTrueAnswer(String trueAnswer) {
		this.trueAnswer = trueAnswer;
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
	
}
