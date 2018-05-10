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
	
	private String app_test;
	
	private String file_name;

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

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	
	

}
