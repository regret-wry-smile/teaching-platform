package com.config.util;

public class Constant {
	/**
	 * 单机版的值
	 */
	public static String CONFIG_TYPE_STANDALONE = "0";
	/**
	 * 学生端的值
	 */
	public static String TYPE_STUDENT = "0";
	
	/**
	 * 测试模式的值
	 */
	public static String IS_TEST = "true";
	/**
	 * 应用模式的值
	 */
	public static String IS_NO_TEST = "false";
	
	/**
	 * project配置文件字段名称  
	 * 	学生端|老师端
	 */
	public static String APP_TYPE = "app_type";
	
	/**
	 * project配置文件字段名称  
	 * 	是否上传
	 */
	public static String APP_SYNC = "app_sync";
	
	/**
	 * project配置文件字段名称  
	 * 是否为测试模式
	 */
	public static String APP_TEST = "app_test";
	
	/**
	 * clien_local配置文件字段名称  
	 * 连接次数
	 */
	public static String RECONNECT_INTERVAL_TIME = "reconnect_interval_time";
	
	/**
	 * clien_local配置文件字段名称  
	 * 连接超时
	 */
	public static String CONNECT_TIMEOUT = "connect_timeout";
	
	/**
	 * clien_local配置文件字段名称  
	 * 服务端id
	 */
	public static String REMOTE_SERVER_IP = "remote_server_ip";
	
	/**
	 * clien_local配置文件字段名称  
	 * 服务端端口
	 */
	public static String REMOTE_SERVER_PORT = "remote_server_port";
	
	/**
	 *clien_local 配置文件字段名称  
	 * 班级名称
	 */
	public static String CLASS_NAME = "class_name";
	
	/**
	 * techer_server配置文件字段名称  
	 * 服务端id
	 */
	public static String SERVER_BIND_IP = "server_bind_ip";
	
	/**
	 * techer_server配置文件字段名称  
	 * 服务端端口
	 */
	public static String SERVER_BIND_PORT = "server_bind_port";
}
