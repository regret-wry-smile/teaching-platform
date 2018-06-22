package com.zkxltech.config;
/**
 * 服务器数据库配置
 * @author zkxl
 *
 */
public class ServerDbConfig {
	/**
	 * 服务器端口
	 */
	private String http_server_port;
	/**
	 * 服务器地址
	 */
	private String url;
	/**
	 * 用户名
	 */
	private String user_name;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 驱动
	 */
	private String driver;
	/**
	 * 服务器上传地址
	 */
	private String server_url;
	public String getHttp_server_port() {
		return http_server_port;
	}
	public void setHttp_server_port(String http_server_port) {
		this.http_server_port = http_server_port;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getServer_url() {
		return server_url;
	}
	public void setServer_url(String server_url) {
		this.server_url = server_url;
	}

}
