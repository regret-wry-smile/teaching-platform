package com.zkxltech.teaching;

import com.zkxltech.teaching.msg.AnswerResponse;

/**
 * 答题信息对象
 * 
 * @author ShenYijie
 *
 */
public class AnswerVO extends AnswerResponse implements Comparable <AnswerVO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*班级名称*/
	private String className;
	/*学生id*/
	private String studentId;
	/*学生名字*/
	private String studentName;
	/**
	 * 本地端保存的绑定个数
	 */
	private Integer bindSize;
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
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
	
	
	public Integer getBindSize() {
		return bindSize;
	}
	public void setBindSize(Integer bindSize) {
		this.bindSize = bindSize;
	}
	@Override
	public int compareTo(AnswerVO o) {
		return getAnswerDateTime().compareTo(o.getAnswerDateTime());
	}
	
	
	

}
