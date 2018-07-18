package com.ejet.core.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.io.IOUtils;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import net.sf.json.JSONObject;

public class SerialListener  implements SerialPortEventListener {
	private static final Logger logger = LoggerFactory.getLogger(SerialPortEventListener.class);
	private static String command = ""; //指令
	private SerialPort serialport ;
	
	private static Map<String, Object> dataMap = Collections.synchronizedMap(new HashMap<String, Object>());
	
    public SerialListener(SerialPort serialport) {
		super();
		this.serialport = serialport;
	}
    
    /**
     * 清空缓存
     */
    public static void clearMap(){
    	dataMap.clear();
    }
    /**
     * 获取缓存中的数据
     */
    public static String getDataMap(String type){
    	return (String) dataMap.get(type);
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
            String data = null;
            try {
                if (serialport == null) {
                	logger.error("串口对象为空！监听失败！");
                } else {
                    // 读取串口数据
                	data = SerialPortManager.readFromPort();
                	logger.info("【串口接收到的数据】"+data);
                	JSONObject jsonObject = JSONObject.fromObject(data);
                	dataMap.put((String) jsonObject.get("fun"), jsonObject.toString());
                }
            } catch (Exception e) {
            	logger.error(IOUtils.getError(e));
            }
            break;
        }
    }
}
