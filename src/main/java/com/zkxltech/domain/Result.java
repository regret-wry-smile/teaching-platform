package com.zkxltech.domain;

import java.util.List;
import java.util.Map;

public class Result {
	/*返回执行结果*/
	private String ret;
	/*业务信息提示*/
	private String message;
	/*详细错误信息*/
	private String detail;
	/*返回查询结果*/
	private Object item;
	public String getRet() {
		return ret;
	}
	public void setRet(String ret) {
		this.ret = ret;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getItem() {
		return item;
	}
	public void setItem(Object item) {
		this.item = item;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
}
