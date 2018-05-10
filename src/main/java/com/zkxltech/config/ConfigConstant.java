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
	 * 教师端配置文件
	 */
	private static final String teacherServerFile = USER_DIR +"/conf/techer_server.properties";
	/**
	 * 学生本地端配置文件
	 */
	private static final String clientLocalFile = USER_DIR +"/conf/client_local.properties";
	
	public static ProjectConfig projectConf = null;
	public static TeacherServerConfig teacherServerConf = null;
	public static ClientLocalConfig clientLocalConf = null;
	
	static  {
		projectConf = new ProjectConfig();
		teacherServerConf = new TeacherServerConfig();
		clientLocalConf = new ClientLocalConfig();
		try {
			projectConf = PropertyUtils.loadProperty(projectFile, ProjectConfig.class);
			teacherServerConf = PropertyUtils.loadProperty(teacherServerFile, TeacherServerConfig.class);
			clientLocalConf = PropertyUtils.loadProperty(clientLocalFile, ClientLocalConfig.class);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static String getProjectfile() {
		return projectFile;
	}

	public static String getTeacherserverfile() {
		return teacherServerFile;
	}

	public static String getClientlocalfile() {
		return clientLocalFile;
	}
	
	
	
//	/*
//	 *	重新加载配置文件 
//	 */
//	public static void reLoadConfig(){
//		projectConf = new ProjectConfig();
//		teacherServerConf = new TeacherServerConfig();
//		clientLocalConf = new ClientLocalConfig();
//		try {
//			projectConf = PropertyUtils.loadProperty(projectFile, ProjectConfig.class);
//			teacherServerConf = PropertyUtils.loadProperty(teacherServerFile, TeacherServerConfig.class);
//			clientLocalConf = PropertyUtils.loadProperty(clientLocalFile, ClientLocalConfig.class);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	
}
