package com.zkxltech.config;

/**
 * 工程配置文件信息
 * 
 * @author ShenYijie
 *
 */
public class ProjectConfig {
	/**
	 * 程序类型： T：表示讲师端  S:端表示本地端
	 */
	private String app_type;
	/**
	 * 本地数据是否同步到服务端
	 */
	private String app_sync;
	/**
	 * 测试模式
	 *  true:是 false:否
	 */
	private String app_test;
	/**
	 * 登录id
	 */
	private String login_id;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 默认发送信道
	 */
	private String tx_ch;
	/**
	 * 默认接收信道
	 */
	private String rx_ch;
	/**
	 * 默认发送功率
	 */
	private String power;

	public String getApp_type() {
		return app_type;
	}

	public void setApp_type(String app_type) {
		this.app_type = app_type;
	}

	public String getApp_sync() {
		return app_sync;
	}

	public void setApp_sync(String app_sync) {
		this.app_sync = app_sync;
	}

	public String getApp_test() {
		return app_test;
	}

	public void setApp_test(String app_test) {
		this.app_test = app_test;
	}

	public String getLogin_id() {
		return login_id;
	}

	public void setLogin_id(String login_id) {
		this.login_id = login_id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTx_ch() {
		return tx_ch;
	}

	public void setTx_ch(String tx_ch) {
		this.tx_ch = tx_ch;
	}

	public String getRx_ch() {
		return rx_ch;
	}

	public void setRx_ch(String rx_ch) {
		this.rx_ch = rx_ch;
	}

	public String getPower() {
		return power;
	}

	public void setPower(String power) {
		this.power = power;
	}
	
	

}
