package com.zkxltech.config;

import com.ejet.core.util.PropertyUtils;

/**
 * 常量配置
 * @author EJet
 */
public final class ConfigConstant {
	
	/**
	 */
	public static final String USER_DIR = System.getProperty("user.dir").replaceAll("\\\\", "/");
	/**
	 * log4j日志配置文件
	 */
	public static final String log4jFile =  USER_DIR +"/conf/log4j.properties";
	/**
	 * 线程池配置文件
	 */
	public static final String ThreadpoolFile =  USER_DIR +"/conf/Threadpool.properties";
	/**
	 * 工程配置文件
	 */
	private static final String projectFile = USER_DIR +"/conf/project.properties";
	/**
	 * 服务器数据库配置文件
	 */
	private static final String serverDbFile = USER_DIR +"/conf/server_db.properties";
	public static ProjectConfig projectConf = null;
	public static ServerDbConfig serverDbConfig = null;
	
	static  {
		projectConf = new ProjectConfig();
		serverDbConfig = new ServerDbConfig();
		try {
			projectConf = PropertyUtils.loadProperty(projectFile, ProjectConfig.class);
			serverDbConfig = PropertyUtils.loadProperty(serverDbFile, ServerDbConfig.class);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static String getProjectfile() {
		return projectFile;
	}

	public static String getServerdbfile() {
		return serverDbFile;
	}
	

	
}
