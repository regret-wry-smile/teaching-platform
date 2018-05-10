package com.zkxltech.teaching.msg;

/**
 * 下发答题器信息
 * 
 * @author ShenYijie
 *
 */
public class EchoRequest extends BaseRequestMessage {
	/** 
	 * 主键id 
	 * */
	private String id;
	/** 
	 * 答题器id 
	 * */
	private String iclickerId;
	/**
	 * 班级信息
	 */
	private String classId;
	/**
	 * 显示学生姓名
	 */
	private String studentName;
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIclickerId() {
		return iclickerId;
	}
	public void setIclickerId(String iclickerId) {
		this.iclickerId = iclickerId;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	

	
}
