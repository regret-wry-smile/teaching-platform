package com.zkxltech.config;

/**
 * 学生本地端配置信息
 * 
 * @author ShenYijie
 *
 */
public class ClientLocalConfig {
	
	private String remote_server_ip;
	private int remote_server_port = 8800;
	private int connect_timeout = 5;
	private int reconnect_interval_time = 3;
	private String class_name = "教室A班";
	
	
	public String getRemote_server_ip() {
		return remote_server_ip;
	}
	public void setRemote_server_ip(String remote_server_ip) {
		this.remote_server_ip = remote_server_ip;
	}
	public int getRemote_server_port() {
		return remote_server_port;
	}
	public void setRemote_server_port(int remote_server_port) {
		this.remote_server_port = remote_server_port;
	}
	public int getConnect_timeout() {
		return connect_timeout;
	}
	public void setConnect_timeout(int connect_timeout) {
		this.connect_timeout = connect_timeout;
	}
	public int getReconnect_interval_time() {
		return reconnect_interval_time;
	}
	public void setReconnect_interval_time(int reconnect_interval_time) {
		this.reconnect_interval_time = reconnect_interval_time;
	}
	public String getClass_name() {
		return class_name;
	}
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	
	
	
}
