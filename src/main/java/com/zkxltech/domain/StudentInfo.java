package com.zkxltech.domain;

public class StudentInfo {
	/*主键*/
	private Integer id;
	/*班级id*/
	private String classId;
	/*班级名称*/
	private String className;
	/*答题器id*/
	private String iclickerId;
	/*学生id*/
	private String studentId;
	/*学生名字*/
	private String studentName;
	/*绑定状态 0未绑定 1已绑定*/
	private String status;
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
	public String getIclickerId() {
		return iclickerId;
	}
	public void setIclickerId(String iclickerId) {
		this.iclickerId = iclickerId;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	

}
