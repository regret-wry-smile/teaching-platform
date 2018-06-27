package com.zkxltech.domain;

import java.util.List;

/***
 * 评分
 * @author zkxl
 *
 */
public class Vote {
	/**
	 * 投票主题编号
	 */
	private String id;
	/**
	 * 投票主题
	 */
	private String  title;
	/**
	 * 投票描述
	 */
	private String  describe;
	/**
	 * 投票节目个数
	 * @return
	 */
	private List<String> programs;
	
	public String getTitle() {
		return title;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public List<String> getPrograms() {
		return programs;
	}
	public void setPrograms(List<String> programs) {
		this.programs = programs;
	}

	
}
