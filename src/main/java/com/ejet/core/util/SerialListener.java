package com.ejet.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.io.IOUtils;
import com.zkxltech.ui.util.StringUtils;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import net.sf.json.JSONObject;

public class SerialListener  implements SerialPortEventListener {
	private static final Logger logger = LoggerFactory.getLogger(SerialPortEventListener.class);
	private static String command = ""; //指令
	private SerialPort serialport ;
	private static String str = "\\{'fun";
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
	//FIXME 测试
//    private static List<String> lists = new ArrayList<String>();
//    private static int index = 0;
//    {
//    	lists.add("{'fun':'update_answer_list','card_id':'1822572044','rssi':'-43','update_time':'2018-07-2516:52:26:360','answers':[{'type':'m','id':'1','answer':''}]}{'fun':'update_answer_list','card_id':'3574606604','rssi':'-53','update_time':'2018-07-2516:52:26:367','answers':[{'type':'m','id':'1','answer':''}]}{'fun':'update_answer_list','card_id':'0369175308','rssi':'-53','update_time':");
//    	lists.add("'2018-07-2516:52:26:367','answers':[{'type':'m','id':'1','answer':''}]}");
//    }
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
                	new Thread(new Runnable() {
						
						@Override
						public void run() {
			                   // 读取串口数据
							String  data = SerialPortManager.readFromPort();
		                	logger.info("【串口接收到的数据】"+data);
		                	if (!StringUtils.isEmpty(data)) {
		                		if (data.split("result").length>1) {
									retData = data;
								}else {
									//FIXME 测试
//									data = lists.get(index);
//									index ++;
									String[] strs = data.split(str);
									for (int i = 0; i < strs.length; i++) {
										if (!StringUtils.isEmpty(strs[i])) {
											if (i != 0) {
												strs[i] = "{'fun" + strs[i];
											}
											try {
												JSONObject jsonObject = JSONObject.fromObject(strs[i]);
												dataList.add(jsonObject.toString());
											} catch (Exception e) {
												if (stringBuffer.length()>0) {
													stringBuffer.append(strs[i]);
													dataList.add(stringBuffer.toString());
													logger.info("本次不完整数据与上次不完整数据拼接:"+stringBuffer.toString());
													stringBuffer.setLength(0);
												}else {
													stringBuffer.append(strs[i]);
													logger.info("本次不完整数据:"+stringBuffer);
												}
											}
										}
									}
								}
		                	}
						}
					}).start();
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
