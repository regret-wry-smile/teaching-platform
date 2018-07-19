package com;

import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.SerialPortManager;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.config.ConfigConstant;
import com.zkxltech.config.Global;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.service.impl.StudentInfoServiceImpl;

/**
 *
 */
public class App {
	
	private static final Logger logger = LoggerFactory.getLogger(App.class);
	/**
	 * 启动服务端
	 * @throws Exception 
	 * @throws InstantiationException 
	 */
	public static void startNettyServer() throws Exception {
		//Netty默认调用启动方法
//		logger.info("服务端启动{}:{}........", ConfigConstant.teacherServerConf.getServer_bind_ip(),
//				ConfigConstant.teacherServerConf.getServer_bind_port());
//		LiveNettyServer server = new LiveNettyServer(ConfigConstant.teacherServerConf.getServer_bind_ip(),
//				ConfigConstant.teacherServerConf.getServer_bind_port());
//		server.bind();
		
	}
	
	/**
	 * 启动程序
	 */
	public static void startNetty() {
        try {
        	//加载log4j日志配置
    		PropertyConfigurator.configure(ConfigConstant.log4jFile);
    		logger.info("======================= {} =======================", Global.VERSION );
			//程序启动模式，服务端还是客户端
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						SerialPortManager.openPort("COM4",1152000);
					} catch (Exception e) {
						logger.error("初始化端口错误", e);
					}
				}
			}).start();
			
        } catch (Exception e) {
			logger.error("程序启动异常", e);
		}
	}
	
	public static void queryBindInfo() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				//查询已经绑定未绑定信息进入缓存
		    	StudentInfoServiceImpl service = new StudentInfoServiceImpl();
		    	StudentInfo query = new StudentInfo();
		    	Result rs = service.selectStudentInfo(query);
		    	List<StudentInfo> list = (List<StudentInfo>) rs.getItem();
		    	//放入到缓存中。
		    	/*if(list!=null && list.size()>0) {
		    		for(StudentInfo item : list) {
		    			if(item.getStatus()!=null && item.getStatus().equals("1")) { //已经绑定
		    				TeachingCache.addBindStudent(item);
		    			} else  {
		    				TeachingCache.addNoBindStudent(item);
		    			}
		    		}
		    	}
		    	logger.info("人员绑定信息：已经绑定{}, 未绑定:{}",  TeachingCache.getBindSize(), TeachingCache.getUnBindSize());*/
			}
		}).start();
	}
	
	public static void startCommunication() {
		//启动netty，作为服务端还是作为客户端
    	startNetty();
    	
    	//查询设备绑定信息
    	if(!Global.isTeacher()) { //本地端，启动查询绑定信息
    		queryBindInfo();
    	}
    	
		//主函数中增加捕获异常，并打印日志
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				logger.error("程序异常退出，重启程序!!!" + IOUtils.getError(e));
				startCommunication();
			}
		});
		
	}
	
	
//	
//	public static void main(String[] args) {
//		startCommunication();
//	}
	

}
