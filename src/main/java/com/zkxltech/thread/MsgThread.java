package com.zkxltech.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.SerialListener;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.ui.util.StringUtils;

public class MsgThread extends Thread{
	private static final Logger logger = LoggerFactory.getLogger(MsgThread.class);
	private String type;
	
	public MsgThread(String type) {
		super();
		this.type = type;
	}

	@Override
	public void run() {
		try {
			boolean flag = true;
			int time = 3;
			String str = "";
			while (flag) { //硬件指令等待响应
				if (SerialListener.getDataMap() != null && SerialListener.getDataMap().size()>0) {
					str = SerialListener.getDataMap().get(0);
					if (!StringUtils.isEmpty(str)) {
						flag = false;
					}else {
						if (!(time>0)) {
							flag = false;
						}else {
							time -- ;
						}
						Thread.sleep(500);
					}
				}else {
					if (!(time>0)) {
						flag = false;
					}else {
						time -- ;
						Thread.sleep(500);
					}
				}
				
				
			}
		} catch (Exception e) {
			logger.error(IOUtils.getError(e));
		}
		
	}
	
}
