package com.zkxltech.teaching.ui;

import java.util.List;

import com.ejet.core.util.SerialPortManager;

public class TestUSB {
	public static void main(String[] args) {
		SerialPortManager usbTool = new SerialPortManager();
        usbTool.openPort("COM4",1152000);
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					SerialPortManager.sendToPort("{'fun': 'get_device_info'}");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
