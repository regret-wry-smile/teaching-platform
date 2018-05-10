package com.zkxltech.domain;

/**
 * 页面配置传的参数
 * {"num1":"1","num2":"2","num3":"3","num4":"4","port":"",
 * "timeout":"","time":"",
 * "classname":"","configType":1,"configUser":"1"}
 */
public class ConfigParams {
	/**
	 * ip地址    num1.num2.num3.num4
	 */
	private String num1; 
	private String num2;
	private String num3;
	private String num4;
	/**
	 * 端口
	 */
	private String port;
	/**
	 * 连接超时
	 */
	private String timeOut;
	/**
	 * 连接次数
	 */
	private String time;
	/**
	 * 班级名称
	 */
	private String className;
	/**
	 * 0:单机
	 * 1:联机
	 */
	private String configType;
	/**
	 * 0:学生端
	 * 1:老师端
	 */
	private String configUser;
	public String getNum1() {
		return num1;
	}
	public void setNum1(String num1) {
		this.num1 = num1;
	}
	public String getNum2() {
		return num2;
	}
	public void setNum2(String num2) {
		this.num2 = num2;
	}
	public String getNum3() {
		return num3;
	}
	public void setNum3(String num3) {
		this.num3 = num3;
	}
	public String getNum4() {
		return num4;
	}
	public void setNum4(String num4) {
		this.num4 = num4;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getConfigType() {
		return configType;
	}
	public void setConfigType(String configType) {
		this.configType = configType;
	}
	public String getConfigUser() {
		return configUser;
	}
	public void setConfigUser(String configUser) {
		this.configUser = configUser;
	}
	
	
}
