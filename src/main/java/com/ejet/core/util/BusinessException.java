package com.ejet.core.util;

public class BusinessException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String ret;
	private String message;
	
	public BusinessException(String ret, String message) {
		super();
		this.ret = ret;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
