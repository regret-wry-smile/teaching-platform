package com.zkxltech.domain;
/**
 * 试卷题目信息
 * @author zkxl
 *
 */
public class QuestionInfo {
	/**
	 * 主键
	 */
	private Integer id;
	/**
	 * 试卷id
	 */
	private String testId;
	/**
	 * 题目id
	 */
	private String questionId;
	/**
	 * 题目
	 */
	private String question;
	/**
	 * 题目类型
	 * 1单选；2多选；3判断；4数字；5主观题
	 */
	private String questionType;
	/**
	 * 正确答案
	 */
	private String trueAnswer;
	/**
	 * 答案范围
	 */
	private String range;
	/**
	 * 分数
	 */
	private String score;
	/**
	 * 部分得分
	 */
	private String partScore;
	/**
	 * 最高分
	 */
	private String highScore;
	/**
	 * 最低分
	 */
	private String downScore;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getTrueAnswer() {
		return trueAnswer;
	}
	public void setTrueAnswer(String trueAnswer) {
		this.trueAnswer = trueAnswer;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getPartScore() {
		return partScore;
	}
	public void setPartScore(String partScore) {
		this.partScore = partScore;
	}
	public String getHighScore() {
		return highScore;
	}
	public void setHighScore(String highScore) {
		this.highScore = highScore;
	}
	public String getDownScore() {
		return downScore;
	}
	public void setDownScore(String downScore) {
		this.downScore = downScore;
	}
}
