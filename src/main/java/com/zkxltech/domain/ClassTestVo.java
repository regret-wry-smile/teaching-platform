package com.zkxltech.domain;

/**
 * 随堂检测返回页面的视图对象
 * @author zkxl
 *
 */
public class ClassTestVo {
	/**
	 * 学生编号
	 */
	private String studentId;
	/**
	 * 学生姓名
	 */
	private String studentName;
	/**
	 * 作答题数
	 */
	private Integer answerCount;
	/**
	 * 作答进度
	 */
	private Double percent;

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

	public Integer getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(Integer answerCount) {
		this.answerCount = answerCount;
	}

	public Double getPercent() {
		return percent;
	}

	public void setPercent(Double percent) {
		this.percent = percent;
	}
	
	
}
