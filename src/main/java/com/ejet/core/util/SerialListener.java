package com.ejet.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.ui.util.StringUtils;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class SerialListener  implements SerialPortEventListener {
	private static final Logger logger = LoggerFactory.getLogger(SerialPortEventListener.class);
	private static String command = ""; //指令
	private SerialPort serialport ;
	private static String str = "\\{'fun";
	private static int index = 0; 
	private static String comName = "";
	
	public static void setComName(String comNameStr){
		comName = comNameStr;
	}
	/**
	 * 返回数据
	 */
	private static List<String> dataList = Collections.synchronizedList(new ArrayList<String>());	
	/**
	 * 指令返回结果
	 */
	private static String retData = "";	
	private static Object lock = new Object();
	
    public SerialListener(SerialPort serialport) {
		super();
		this.serialport = serialport;
	}
    
    private static StringBuffer stringBuffer = new StringBuffer();
    /**
     * 清空缓存
     */
    public static void clearStringBuffer(){
    	stringBuffer.setLength(0);
    }
    /**
     * 清空缓存
     */
    public static void clearMap(){
    	dataList.clear();
    }
    /**
     * 清空缓存
     */
    public static void clearRetCode(){
    	retData = "";
    }
    /**
     * 移除缓存
     */
    public static void removeList(List<String> list){
		Iterator<String> iterator = dataList.iterator();
    	while (iterator.hasNext()) {
    		String s =iterator.next();
    		for (int i = 0; i < list.size(); i++) {
				if (list.get(i).equals(s)) {
					iterator.remove();
				}
			}
		}
    }
    /**
     * 获取缓存中的数据
     */
    public static String getRetCode(){
    	return  retData;
    }
    /**
     * 获取缓存中的数据
     */
    public static List<String> getDataMap(){
    	synchronized (lock) {
    		return  dataList;
    	}
    }
    /**
     * 处理监控到的串口事件
     */
	@Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        switch (serialPortEvent.getEventType()) {

        case SerialPortEvent.BI: // 10 通讯中断
//            ShowUtils.errorMessage("与串口设备通讯中断");
            break;

        case SerialPortEvent.OE: // 7 溢位（溢出）错误

        case SerialPortEvent.FE: // 9 帧错误

        case SerialPortEvent.PE: // 8 奇偶校验错误

        case SerialPortEvent.CD: // 6 载波检测

        case SerialPortEvent.CTS: // 3 清除待发送数据

        case SerialPortEvent.DSR: // 4 待发送数据准备好了

        case SerialPortEvent.RI: // 5 振铃指示

        case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 输出缓冲区已清空
            break;

        case SerialPortEvent.DATA_AVAILABLE: // 1 串口存在可用数据
            try {
                if (serialport == null) {
                	logger.error("串口对象为空！监听失败！");
                } else {
                    // 读取串口数据
					String  data = SerialPortManager.readFromPort();
                	logger.info("【串口接收到的数据】"+data);
                	if (!StringUtils.isEmpty(data)) {
                		if (data.split("result").length>1) {
							retData = data;
						}else {
							String[] strs = data.split(str);
							for (int i = 0; i < strs.length; i++) {
								if (!StringUtils.isEmpty(strs[i])) {
									if (i != 0) {
										strs[i] = "{'fun" + strs[i];
									}
									try {
										JSONObject jsonObject = (JSONObject) JSONObject.parse(strs[i]);
										dataList.add(jsonObject.toString());
									} catch (Exception e) {
										if (stringBuffer.length()>0) {
											try {
												stringBuffer.append(strs[i]);
												JSONObject jsonObject = (JSONObject) JSONObject.parse(stringBuffer.toString());
												dataList.add(jsonObject.toString());
												logger.info("本次不完整数据与上次不完整数据拼接:"+stringBuffer.toString());
												stringBuffer.setLength(0);
											} catch (Exception e2) { //如果拼接后还是错误格式，就舍弃上次不完整的数据
												stringBuffer.setLength(0);
												stringBuffer.append(strs[i]);
											}
											
										}else {
											stringBuffer.append(strs[i]);
											logger.info("本次不完整数据:"+stringBuffer);
										}
									}
								}
							}
						}
                	}else {
						if (index > 1) {
							index = 0;
							SerialPortManager.closePort();	
							new Thread(new Runnable() {
								@Override
								public void run() {

									boolean flag = true;
									while (flag) {
										try {
											List<String> strings = SerialPortManager.findPort();
											for (int i = 0; i < strings.size(); i++) {
												if (comName.equals(strings.get(i))) {
													SerialPortManager.openPort(comName,1152000);
													flag = false;
												}
											}
										} catch (Exception e) {
											logger.info("串口连接失败");
										}
										try {
											Thread.sleep(3000);
										} catch (InterruptedException e) {
											logger.info(IOUtils.getError(e));
										}
									}
								}
							}).start();
						}else {
							index++;
						}
					}
                }
            } catch (Exception e) {
            	logger.error(IOUtils.getError(e));
            	try {
            		SerialPortManager.openPort("COM4",1152000);
				} catch (Exception e2) {
					logger.error(IOUtils.getError(e2));
				}
            }
            break;
        }
    }
}
