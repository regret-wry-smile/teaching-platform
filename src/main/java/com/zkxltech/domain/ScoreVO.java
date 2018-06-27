package com.zkxltech.domain;

public class ScoreVO {
	/**
	 * 节目编号
	 */
	private String num;
	/**
	 * 节目名称
	 */
	private String program;
	/**
	 * 总分
	 */
	private Integer total;
	/**
	 * 评分人数
	 */
	private Integer peopleSum;
	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getProgram() {
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getPeopleSum() {
		return peopleSum;
	}
	public void setPeopleSum(Integer peopleSum) {
		this.peopleSum = peopleSum;
	}
	
}
