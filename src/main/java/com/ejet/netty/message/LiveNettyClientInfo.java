package com.ejet.netty.message;

/**
 * 客户端连接信息
 * 
 * @author ShenYijie
 *
 */
public class LiveNettyClientInfo {
	/**
	 * 客户端IP
	 */
	private String ip;
	/**
	 * 客户端
	 */
	private String hostName;
	/**
	 * 客户端端口
	 */
	private Integer port;
	/**
	 * 客户端名称
	 */
	private String className;
	/**
	 * 客户端id
	 */
	private String clientId;
	/**
	 * 连接状态信息 1：正常 0：断开
	 */
	private int state;
	
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	

}
