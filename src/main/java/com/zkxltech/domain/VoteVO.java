package com.zkxltech.domain;

public class VoteVO {
	/**
	 * 节目编号
	 */
	private String num;
	/**
	 * 节目名称
	 */
	private String program;
	/**
	 * 赞成人数
	 */
	private Integer agree;
	/**
	 * 反对人数
	 */
	private Integer disagree;
	
	/**
	 * 弃权人数
	 */
	private Integer waiver;
	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getProgram() {
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}
	public Integer getAgree() {
		return agree;
	}
	public void setAgree(Integer agree) {
		this.agree = agree;
	}
	public Integer getDisagree() {
		return disagree;
	}
	public void setDisagree(Integer disagree) {
		this.disagree = disagree;
	}
	public Integer getWaiver() {
		return waiver;
	}
	public void setWaiver(Integer waiver) {
		this.waiver = waiver;
	}
	
}
