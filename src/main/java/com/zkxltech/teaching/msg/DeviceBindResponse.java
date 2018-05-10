package com.zkxltech.teaching.msg;

/**
 * 答题器,答题回复对象信息
 * 
 * @author ShenYijie
 *
 */
public class DeviceBindResponse extends BaseRequestMessage {
	/** 答题器id */
	private String iclickerId;
	/** 
	 * 被替换的设备id 
	 * 如果没有答题器被替换，则REP_UID的值为全0x00
	 * */
	private String replaceIclickerId;
	
	public String getIclickerId() {
		return iclickerId;
	}
	public void setIclickerId(String iclickerId) {
		this.iclickerId = iclickerId;
	}
	public String getReplaceIclickerId() {
		return replaceIclickerId;
	}
	public void setReplaceIclickerId(String replaceIclickerId) {
		this.replaceIclickerId = replaceIclickerId;
	}
	
	
	

}
