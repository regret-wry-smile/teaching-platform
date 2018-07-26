package com.zkxltech.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.SerialListener;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.ui.util.StringUtils;

public class MsgThread2 extends Thread{
	private static final Logger logger = LoggerFactory.getLogger(MsgThread2.class);
	private String type;
	
	public MsgThread2(String type) {
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
				str = SerialListener.getRetCode();
				if (str != null && !"".equals(str)) {
					if (!StringUtils.isEmpty(str)) {
						flag = false;
					}else {
						if (!(time>0)) {
							flag = false;
						}else {
							time -- ;
						}
						Thread.sleep(100);
					}
				}else {
					time -- ;
					Thread.sleep(100);
				}
				
				
			}
		} catch (Exception e) {
			logger.error(IOUtils.getError(e));
		}
		
	}
	
}
