package com.zkxltech.teaching.server;

import java.lang.invoke.ConstantCallSite;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.App;
import com.zkxltech.config.ConfigConstant;
import com.zkxltech.ui.MainStart;

/**
 * 通信测试接口
 * 
 * @author ShenYijie
 *
 */
public class CommunicationServerTest {

	private final static Logger logger = LoggerFactory.getLogger(App.class);
	
	@Before
	public void init() {
		try {
	        //加载log4j日志配置
			System.out.println(ConfigConstant.log4jFile);
			PropertyConfigurator.configure(ConfigConstant.log4jFile);
			
			//教师端启动
			ConfigConstant.projectConf.setApp_type("T");
			
			
		} catch (Exception e) {
			logger.error("程序启动异常", e);
		}
	}

	
	/**
	 * 开始启动
	 */
	@Test
	public void start() {
		
		//启动答题器通信等接口
		new Thread(new Runnable() {
			@Override
			public void run() {
				App.startCommunication();
			}
		}).start();
		
		MainStart window = new MainStart();
		
	}
	
	
	
}
