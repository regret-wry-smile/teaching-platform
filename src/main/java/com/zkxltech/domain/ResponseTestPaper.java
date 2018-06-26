package com.zkxltech.domain;

/**
 * 服务器试卷对象
 * @author zkxl
 *
 */
public class ResponseTestPaper {
	/**
	 * 考试项目ID(CodeId) 
	 */
	private Integer id;
	/**
	 * 考试项目名称 (CodeName)   
	 */
	private String xm;
	/**
	 * 考试项目代号(Code)
	 */
	private String xmid;
	/**
	 * 科目
	 */
	private String subjectName;
	/**
	 * 班级id
	 */
	private String classId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getXm() {
		return xm;
	}
	public void setXm(String xm) {
		this.xm = xm;
	}
	public String getXmid() {
		return xmid;
	}
	public void setXmid(String xmid) {
		this.xmid = xmid;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	
}
