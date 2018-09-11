package com.zkxltech.teaching.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;

import com.ejet.core.util.SerialPortManager;
import com.ejet.core.util.constant.EquipmentConstant;
import com.zkxltech.config.ConfigConstant;
import com.zkxltech.domain.Answer;

public class TestUSB {
	public static void main(String[] args) {
		//加载log4j日志配置
		PropertyConfigurator.configure(ConfigConstant.log4jFile);
		List<String> list = SerialPortManager.findPort();
		for (int i = 0; i < list.size(); i++) {
			try {
				SerialPortManager.openPort(list.get(i),1152000);
				break;
			} catch (Exception e) {
				System.out.println("初始化串口失败。");
			}
		}
//        SerialPortManager.sendToPort("{'fun': 'get_device_info'}"); //获取设备信息
        SerialPortManager.sendToPort(" {'fun': 'set_channel','tx_ch': '90','rx_ch': '110'}"); 
       
//        SerialPortManager.sendToPort(EquipmentConstant.WIRELESS_BIND_START_CODE); //开始绑定
//        SerialPortManager.sendToPort(EquipmentConstant.SET_CHANNEL_CODE(90, 110)); //设置信道
//      List<Answer> answers = new ArrayList<Answer>(); 
//      Answer answer = new Answer();
//      answer.setType("s");
//      answer.setId("1");
//      answer.setRange("A-D");
//      answers.add(answer);
//      SerialPortManager.sendToPort(EquipmentConstant.ANSWER_START_CODE(answers));
      
      
//        new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				while (true) {
//					SerialPortManager.sendToPort("{'fun': 'get_device_info'}");
//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		}).start();
	}
}
