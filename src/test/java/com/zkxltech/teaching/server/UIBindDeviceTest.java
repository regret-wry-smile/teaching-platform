package com.zkxltech.teaching.server;

import org.junit.Before;
import org.junit.Test;

import com.zkxltech.teaching.device.DeviceComm;


/**
 * 界面发起帮卡请求
 * 
 * @author ShenYijie
 *
 */
public class UIBindDeviceTest {
	
	
	@Before
	public void init() {
		
	}

	/**
	 * 开始绑定卡
	 */
	@Test
	public void startBindDevice(){
		
		//开启帮卡
		DeviceComm.startBindCard();
		
		
		
	}
	
	
	/**
	 * 关闭绑定卡
	 */
	@Test
	public void stopBindDevice(){
		
		//开启帮卡
		DeviceComm.stopBindCard();
		
		
	}
	
	
}
