package com.ejet.netty.message;

import java.io.Serializable;

public class LiveBaseMsg implements Serializable {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 消息类型(默认业务消息)
     */
	private LiveMsgType type = LiveMsgType.ANSWER_REQ;
	/**
	 * 客户端id
	 */
	private String clientId;
	/**
	 * 客户端名称
	 */
	private String clientName;
	
	public LiveMsgType getType() {
		return type;
	}
	public void setType(LiveMsgType type) {
		this.type = type;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
    
    
    
}
