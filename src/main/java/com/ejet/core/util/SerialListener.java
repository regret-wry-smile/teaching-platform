package com.ejet.core.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.io.IOUtils;
import com.zkxltech.ui.util.StringUtils;

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
    
    private static StringBuffer stringBuffer = new StringBuffer();
    
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
                	dataMap.clear();
                    // 读取串口数据
                	data = SerialPortManager.readFromPort();
                	logger.info("【串口接收到的数据】"+data);
                	if (!StringUtils.isEmpty(data)) {
                		//判定是否为{}或[]格式
                		if (stringBuffer.length()>0) {
							stringBuffer.append(data);
							data = stringBuffer.toString();
							stringBuffer.setLength(0);
						}
                		switch (verfifyData(data)) {
						case 0:
							JSONObject jsonObject = JSONObject.fromObject(data);
                        	dataMap.put((String) jsonObject.get("fun"), jsonObject.toString());
							break;
						case 1: //如果是前部分
							stringBuffer.setLength(0);
							stringBuffer.append(data);
							break;
						default:
							break;
						}
					}
                }
            } catch (Exception e) {
            	logger.error(IOUtils.getError(e));
            	SerialPortManager.openPort("COM4",1152000);
            }
            break;
        }
    }
	
	private static int verfifyData(String data){
		if (data.length()>0) {
			char start = data.charAt(0);
			char end = data.charAt(data.length()-1);
			if (('{' == start && '}'!=end)||('['==start && ']' != end)) {
				//只有前部分数据
				return 1;
			}
			if (('{' != start && '}' == end)||('[' != start && ']' == end)) {
				//只有后部分数据
				return 2;
			}
			return 0;
		}else {
			return  -1;
		}
		
	}
	
//	public static void main(String[] args) {
//		String string  = "07-1915:45:28:627','answers':[{'type':'s','id':'1','answer':''},{'type':'m','id':'2','answer':''},{'type':'s','id':'3','answer':''},{'type':'s','id':'4','answer':''},{'type':'s','id':'5','answer':''},{'type':'s','id':'6','answer':''},{'type':'s','id':'7','answer':''},{'type':'s','id':'8','answer':''},{'type':'s','id':'9','answer':''},{'type':'s','id':'10','answer':''},{'type':'s','id':'11','answer':''},{'type':'s','id':'12','answer':''},{'type':'s','id':'13','answer':''},{'type':'s','id':'14','answer':''},{'type':'s','id':'15','answer':''},{'type':'s','id':'16','answer':''},{'type':'s','id':'17','answer':''},{'type':'s','id':'18','answer':''},{'type':'s','id':'19','answer':''},{'type':'s','id':'20','answer':''},{'type':'m','id':'21','answer':''},{'type':'m','id':'22','answer':''},{'type':'m','id':'23','answer':''},{'type':'m','id':'24','answer':''},{'type':'m','id':'25','answer':''},{'type':'m','id':'26','answer':''},{'type':'m','id':'27','answer':''},{'type':'m','id':'28','answer':''},{'type':'m','id':'29','answer':''},{'type':'m','id':'30','answer':''},{'type':'m','id':'31','answer':''},{'type':'m','id':'32','answer':''},{'type':'m','id':'33','answer':''},{'type':'m','id':'34','answer':''},{'type':'m','id':'35','answer':''},{'type':'m','id':'36','answer':''},{'type':'m','id':'37','answer':''},{'type':'m','id':'38','answer':''},{'type':'m','id':'39','answer':''},{'type':'m','id':'40','answer':''}]}{'fun':'update_answer_list','card_id':'3430069781','rssi':'-53','update_time':'2018-07-1915:45:28:654','answers':[{'type':'s','id':'1','answer':''},{'type':'m','id':'2','answer':''},{'type':'s','id':'3','answer':''},{'type':'s','id':'4','answer':''},{'type':'s','id':'5','answer':''},{'type':'s','id':'6','answer':''},{'type':'s','id':'7','answer':''},{'type':'s','id':'8','answer':''},{'type':'s','id':'9','answer':''},{'type':'s','id':'10','answer':''},{'type':'s','id':'11','answer':''},{'type':'s','id':'12','answer':''},{'type':'s','id':'13','answer':''},{'type':'s','id':'14','answer':''},{'type':'s','id':'15','answer':''},{'type':'s','id':'16','answer':''},{'type':'s','id':'17','answer':''},{'type':'s','id':'18','answer':''},{'type':'s','id':'19','answer':''},{'type':'s','id':'20','answer':''},{'type':'m','id':'21','answer':''},{'type':'m','id':'22','answer':''},{'type':'m','id':'23','answer':''},{'type':'m','id':'24','answer':''},{'type':'m','id':'25','answer':''},{'type':'m','id':'26','answer':''},{'type':'m','id':'27','answer':''},{'type':'m','id':'28','answer':''},{'type':'m','id':'29','answer':''},{'type':'m','id':'30','answer':''},{'type':'m','id':'31','answer':''},{'type':'m','id':'32','answer':''},{'type':'m','id':'33','answer':''},{'type':'m','id':'34','answer':''},{'type':'m','id':'35','answer':''},{'type':'m','id':'36','answer':''},{'type':'m','id':'37','answer':''},{'type':'m','id':'38','answer':''},{'type':'m','id':'39','answer':''},{'type':'m','id':'40','answer':''}]}{'fun':'update_answer_list','card_id':'3430198517','rssi':'-62','update_time':'2018-07-1915:45:28:674','answers':[{'type':'s','id':'1','answer':''},{'type':'m','id':'2','answer':''},{'type':'s','id':'3','answer':''},{'type':'s','id':'4','answer':''},{'type':'s','id':'5','answer':''},{'type':'s','id':'6','answer':''},{'type':'s','id':'7','answer':''},{'type':'s','id':'8','answer':''},{'type':'s','id':'9','answer':''},{'type':'s','id':'10','answer':''},{'type':'s','id':'11','answer':''},{'type':'s','id':'12','answer':''},{'type':'s','id':'13','answer':''},{'type':'s','id':'14','answer':''},{'type':'s','id':'15','answer':''},{'type':'s','id':'16','answer':''},{'type':'s','id':'17','answer':''},{'type':'s','id':'18','answer':''},{'type':'s','id':'19','answer':''},{'type':'s','id':'20','answer':''},{'type':'m','id':'21','answer':''},{'type':'m','id':'22','answer':''},{'type':'m','id':'23','answer':''},{'type':'m','id':'24','answer':''},{'type':'m','id':'25','answer':''},{'type':'m','id':'26','answer':''},{'type':'m','id':'27','answer':''},{'type':'m','id':'28','answer':''},{'type':'m','id':'29','answer':''},{'type':'m','id':'30','answer':''},{'type':'m','id':'31','answer':''},{'type':'m','id':'32','answer':''},{'type':'m','id':'33','answer':''},{'type':'m','id':'34','answer':''},{'type':'m','id':'35','answer':''},{'type':'m','id':'36','answer':''},{'type':'m','id':'37','answer':''},{'type':'m','id':'38','answer':''},{'type':'m','id':'39','answer':''},{'type':'m','id':'40','answer':''}]}{'fun':'update_answer_list','card_id':'3430209813','rssi':'-78','update_time':'2018-07-1915:45:28:719','answers':[{'type':'s','id':'1','answer':''},{'type':'m','id':'2','answer':''},{'type':'s','id':'3','answer':''},{'type':'s','id':'4','answer':''},{'type':'s','id':'5','answer':''},{'type':'s','id':'6','answer':''},{'type':'s','id':'7','answer':''},{'type':'s','id':'8','answer'}',{'type':'m','id':'25','answer':''},{'type':'m','id':'26','answer':''},{'type':'m','id':'27','answer':''},{'type':'m','id':'28','answer':''},{'type':'m','id':'29','answer':''},{'type':'m','id':'30','answer':''},{'type':'m','id':'31','answer':''},{'type':'m','id':'32','answer':''},{'type':'m','id':'33','answer':''},{'type':'m','id':'34','answer':''},{'type':'m','id':'35','answer':''},{'type':'m','id':'36','answer':''},{'type':'m','id':'37','answer':''},{'type':'m','id':'38','answer':''},{'type':'m','id':'39','answer':''},{'type':'m','id':'40','answer':''}]}{'fun':'update_answer_list','card_id':'3429551941','rssi':'-67','update_time':'2018-07-1915:45:28:829','answers':[{'type':'s','id':'1','answer':''},{'type':'m','id':'2','answer':''},{'type':'s','id':'3','answer':''},{'type':'s','id':'4','answer':''},{'type':'s','id':'5','answer':''},{'type':'s','id':'6','answer':''},{'type':'s','id':'7','answer':''},{'type':'s','id':'8','answer':''},{'type':'s','id':'9','answer':''},{'type':'s','id':'10','answer':''},{'type':'s','id':'11','answer':''},{'type':'s','id':'12','answer':''},{'type':'s','id':'13','answer':''},{'type':'s','id':'14','answer':''},{'type':'s','id':'15','answer':''},{'type':'s','id':'16','answer':''},{'type':'s','id':'17','answer':''},{'type':'s','id':'18','answer':''},{'type':'s','id':'19','answer':''},{'type':'s','id':'20','answer':''},{'type':'m','id':'21','answer':''},{'type':'m','id':'22','answer':''},{'type':'m','id':'23','answer':''},{'type':'m','id':'24','answer':''},{'type':'m','id':'25','answer':''},{'type':'m','id':'26','answer':''},{'type':'m','id':'27','answer':''},{'type':'m','id':'28','answer':''},{'type':'m','id':'29','answer':''},{'type':'m','id':'30','answer':''},{'type':'m','id':'31','answer':''},{'type':'m','id':'32','answer':''},{'type':'m','id':'33','answer':''},{'type':'m','id':'34','answer':''},{'type':'m','id':'35','answer':''},{'type':'m','id':'36','answer':''},{'type':'m','id':'37','answer':''},{'type':'m','id':'38','answer':''},{'type':'m','id':'39','answer':''},{'type':'m','id':'40','answer':''}]}{'fun':'update_answer_list','card_id':'3429936373','rssi':'-67','update_time':'2018-07-1915:45:28:850','answers':[{'type':'s','id':'1','answer':''},{'type':'m','id':'2','answer':''},{'type':'s','id':'3','answer':''},{'type':'s','id':'4','answer':''},{'type':'s','id':'5','answer':''},{'type':'s','id':'6','answer':''},{'type':'s','id':'7','answer':''},{'type':'s','id':'8','answer':''},{'type':'s','id':'9','answer':''},{'type':'s','id':'10','answer':''},{'type':'s','id':'11','answer':''},{'type':'s','id':'12','answer':''},{'type':'s','id':'13','answer':''},{'type':'s','id':'14','answer':''},{'type':'s','id':'15','answer':''},{'type':'s','id':'16','answer':''},{'type':'s','id':'17','answer':''},{'type':'s','id':'18','answer':''},{'type':'s','id':'19','answer':''},{'type':'s','id':'20','answer':''},{'type':'m','id':'21','answer':''},{'type':'m','id':'22','answer':''},{'type':'m','id':'23','answer':''},{'type':'m','id':'24','answer':''},{'type':'m','id':'25','answer':''},{'type':'m','id':'26','answer':''},{'type':'m','id':'27','answer':''},{'type':'m','id':'28','answer':''},{'type':'m','id':'29','answer':''},{'type':'m','id':'30','answer':''},{'type':'m','id':'31','answer':''},{'type':'m','id':'32','answer':''},{'type':'m','id':'33','answer':''},{'type':'m','id':'34','answer':''},{'type':'m','id':'35','answer':''},{'type':'m','id':'36','answer':''},{'type':'m','id':'37','answer':''},{'type':'m','id':'38','answer':''},{'type':'m','id':'39','answer':''},{'type':'m','id':'40','answer':''}]}";
//		System.out.println(verfifyData(string));
//	}
}
