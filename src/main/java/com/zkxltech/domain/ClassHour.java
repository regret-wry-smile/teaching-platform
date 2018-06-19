package com.zkxltech.domain;
/**
 * 课时
 * @author zkxl
 *
 */
public class ClassHour {
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
	 * 科目id
	 */
	private String subjectId;
	/**
	 * 科目
	 */
	private String subjectName;
	/**
	 * 课时
	 */
	private String classHourId;
	/**
	 * 课时
	 */
	private String classHourName;
	/**
	 * 开始时间
	 */
	private String startTime;
	/**
	 * 结束时间
	 */
	private String endTime;
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
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getClassHourId() {
		return classHourId;
	}
	public void setClassHourId(String classHourId) {
		this.classHourId = classHourId;
	}
	public String getClassHourName() {
		return classHourName;
	}
	public void setClassHourName(String classHourName) {
		this.classHourName = classHourName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}
