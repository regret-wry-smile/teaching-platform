package com.zkxltech.domain;

public class ClassInfo {
	/**
	 * 主键
	 */
	private Integer id;
	/**
	 * 班级id
	 */
	private String classId;
	/**
	 * 班级名称
	 */
	private String className;
	/**
	 * 类型 
	 * 0:本地创建;1:服务器导入
	 */
	private String atype;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getAtype() {
		return atype;
	}
	public void setAtype(String atype) {
		this.atype = atype;
	}
	
}
