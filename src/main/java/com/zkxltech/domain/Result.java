package com.zkxltech.domain;

public class Result {
	private Integer code;
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
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
    @Override
    public String toString() {
        return "Result [code=" + code + ", ret=" + ret + ", message=" + message + ", detail=" + detail + ", item="
                + item + "]";
    }
}
