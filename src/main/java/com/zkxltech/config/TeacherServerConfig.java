package com.zkxltech.config;

/**
 * 教师端配置信息
 * 
 * @author ShenYijie
 *
 */
public class TeacherServerConfig {
	
	private String server_bind_ip;
	private int server_bind_port = 8800;
	public String getServer_bind_ip() {
		return server_bind_ip;
	}
	public void setServer_bind_ip(String server_bind_ip) {
		this.server_bind_ip = server_bind_ip;
	}
	public int getServer_bind_port() {
		return server_bind_port;
	}
	public void setServer_bind_port(int server_bind_port) {
		this.server_bind_port = server_bind_port;
	}
	
	
	
}
