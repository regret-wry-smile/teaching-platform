package com.zkxltech.teaching.server;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.App;
import com.ejet.cache.TeachingCache;
import com.ejet.netty.client.LiveNettyClient;
import com.zkxltech.config.ConfigConstant;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.teaching.device.DeviceComm;
import com.zkxltech.teaching.service.impl.StudentInfoServiceImpl;

public class LocalClientTest {
	private final static Logger logger = LoggerFactory.getLogger(App.class);
	
	@Before
	public void init() {
		try {
	        //加载log4j日志配置
			System.out.println(ConfigConstant.log4jFile);
			PropertyConfigurator.configure(ConfigConstant.log4jFile);
			
			
			StudentInfo stu1 = new StudentInfo();
			stu1.setStudentId("123456");
			stu1.setStudentName("罗正");
			
			StudentInfo stu2 = new StudentInfo();
			stu2.setStudentId("12345678");
			stu2.setStudentName("申毅杰");
			
			StudentInfo stu3 = new StudentInfo();
			stu3.setStudentId("123456789");
			stu3.setStudentName("段国斌");
			
			
		} catch (Exception e) {
			logger.error("程序启动异常", e);
		}
	}
	
	/**
	 * 启动客户端
	 * @throws InstantiationException
	 * @throws Exception
	 */
	public static void startClient(){
		//默认调用启动方法
		logger.info("客户端启动{}:{}........", 
				ConfigConstant.clientLocalConf.getRemote_server_ip(),
				ConfigConstant.clientLocalConf.getRemote_server_port());
		
		LiveNettyClient client = new LiveNettyClient(ConfigConstant.clientLocalConf.getRemote_server_ip(),
				ConfigConstant.clientLocalConf.getRemote_server_port());
		client.start();
		
	}
	@Test
	public void main() {
		// TODO Auto-generated method stub
//		
//		//启动设备通信
//    	DeviceComm.init();
//		
//		startClient();
//		
//		while(true) {
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
		
		//查询已经绑定未绑定信息进入缓存
    	StudentInfoServiceImpl service = new StudentInfoServiceImpl();
    	StudentInfo query = new StudentInfo();
    	Result rs = service.selectStudentInfo(query);
    	System.out.println(rs);
		
		
	}
	
	

}
