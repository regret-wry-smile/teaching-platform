package com.zkxltech.domain;

import java.util.List;

/**
 * 硬件返回答案数据对象
 * @author zkxl
 *
 */
public class ResponseAnswer {
	private String fun;
	private String card_id;
	private String rssi;
	private String updateTime;
	private String raiseHand;
	private String attendance;
	private List<Answer> answers;
	public String getFun() {
		return fun;
	}
	public void setFun(String fun) {
		this.fun = fun;
	}

	
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getRssi() {
		return rssi;
	}
	public void setRssi(String rssi) {
		this.rssi = rssi;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getRaiseHand() {
		return raiseHand;
	}
	public void setRaiseHand(String raiseHand) {
		this.raiseHand = raiseHand;
	}
	public String getAttendance() {
		return attendance;
	}
	public void setAttendance(String attendance) {
		this.attendance = attendance;
	}
	public List<Answer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	
	
}
