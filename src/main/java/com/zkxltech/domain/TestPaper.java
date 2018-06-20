package com.zkxltech.domain;

public class TestPaper {
	/**
	 * 主键
	 */
	private Integer id;
	/**
	 * 科目
	 */
	private String subject;
	/**
	 * 试卷id
	 */
	private String testId;
	/**
	 * 试卷名称
	 */
	private String testName;
	/**
	 * 试卷描述
	 */
	private String describe;
	/**
	 * 试卷备注(保留字段)
	 */
	private String remark;
	/**
	 * 试卷类型
	 * 0:本地添加;1:服务器导入
	 */
	private String atype;
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
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAtype() {
		return atype;
	}
	public void setAtype(String atype) {
		this.atype = atype;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
}
