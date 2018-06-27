package com.zkxltech.domain;

public class RequestVo {
	/**
	 * 题目编号
	 */
	private String id;
	/**
	 * 题目类型
	 * s单选，m多选,j判断，d评分，g通用
	 */
	private String type;
	/**
	 * 作答范围
	 */
	private String range;

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}
	
	
}
