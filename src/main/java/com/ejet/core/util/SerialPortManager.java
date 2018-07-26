package com.ejet.core.util;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.io.IOUtils;
import com.zkxltech.service.impl.ClassHourServiceImpl;

public class SerialPortManager {
	private static final Logger logger = LoggerFactory.getLogger(SerialPortManager.class);
	private static SerialPort serialPort;
	/**
	 * 查找所有可用端口
	 * 
	 * @return 可用端口名称列表
	 */
	@SuppressWarnings("unchecked")
	public static final ArrayList<String> findPort() {
		// 获得当前所有可用串口
		Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
		ArrayList<String> portNameList = new ArrayList<String>();
		// 将可用串口名添加到List并返回该List
		while (portList.hasMoreElements()) {
			String portName = portList.nextElement().getName();
			portNameList.add(portName);
		}
		return portNameList;
	}

	/**
	 * 打开串口
	 * 
	 * @param portName
	 *            端口名称
	 * @param baudrate
	 *            波特率
	 * @return 串口对象
	 * @throws PortInUseException 
	 * @throws NoSuchPortException 
	 * @throws UnsupportedCommOperationException 
	 * @throws SerialPortParameterFailure
	 *             设置串口参数失败
	 * @throws NotASerialPort
	 *             端口指向设备不是串口类型
	 * @throws NoSuchPort
	 *             没有该端口对应的串口设备
	 * @throws PortInUse
	 *             端口已被占用
	 */
	public static final SerialPort openPort(String portName, int baudrate) throws PortInUseException, NoSuchPortException, UnsupportedCommOperationException{
		// 通过端口名识别端口
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		// 打开端口，并给端口名字和一个timeout（打开操作的超时时间）
		CommPort commPort = portIdentifier.open(portName, 2000);
		// 判断是不是串口
		if (commPort instanceof SerialPort) {
			serialPort = (SerialPort) commPort;
			// 设置一下串口的波特率等参数
			serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_2,
					SerialPort.PARITY_NONE);
			addListener(serialPort, new SerialListener(serialPort));

			logger.info("开启usb端口...");
			return serialPort;
		} else {
			// 不是串口
//						throw new NotASerialPort();
		}
		return serialPort;
	}

	/**
	 * 关闭串口
	 * 
	 * @param serialport
	 *            待关闭的串口对象
	 */
	public static void closePort() {
		if (serialPort != null) {
			serialPort.close();
			serialPort = null;
		}
		logger.info("关闭usb端口...");
	}
	
//	public String toBinary( String str){
//	    char[] strChar=str.toCharArray();
//	    String result="";
//	    for(int i=0;i<strChar.length;i++){
//	        result +=Integer.toBinaryString(strChar[i])+ " ";
//	    }
//	    return result;
//	}
	
	public static byte[] strToByteArray(String str) {
	    if (str == null) {
	        return null;
	    }
	    byte[] byteArray = str.getBytes();
	    return byteArray;
	}

	/**
	 * 往串口发送数据
	 * 
	 * @param serialPort
	 *            串口对象
	 * @param order
	 *            待发送数据
	 * @throws SendDataToSerialPortFailure
	 *             向串口发送数据失败
	 * @throws SerialPortOutputStreamCloseFailure
	 *             关闭串口对象的输出流出错
	 */
	public static boolean sendToPort(String s){
		byte[] order = strToByteArray(s);
		OutputStream out = null;
		try {
			out = serialPort.getOutputStream();
			out.write(order);
			out.flush();
			return true;
		} catch (IOException e) {
			logger.error(IOUtils.getError(e));
			return false;
		} finally {
			try {
				if (out != null) {
					out.close();
					out = null;
				}
			} catch (IOException e) {
				logger.error(IOUtils.getError(e));
			}
		}
	}

	/**
	 * 从串口读取数据
	 * 
	 * @param serialPort
	 *            当前已建立连接的SerialPort对象
	 * @return 读取到的数据
	 */
	public static String readFromPort() {

        InputStream in = null;
        String str = "";

        try {
            in = serialPort.getInputStream();
//            int bufflenth = in.available();        //获取buffer里的数据长度
            byte[] bytes = new byte[1];
//            while (bufflenth != 0) {                             
//            	bytes = new byte[bufflenth];    //初始化byte数组为buffer中数据的长度
//                in.read(bytes);
//                bufflenth = in.read();
//            } 
//            str = new String(bytes);
            int bytesRead = in.read(bytes);
    	    while (bytesRead != 0) {
    	    	str += new String(bytes).trim();
    	        bytesRead = in.read(bytes);
    	    }
        } catch (IOException e) {
//        	SerialPortManager.closePort();
//        	logger.info("【串口中断3s后重连..】");
//        	try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e1) {
//				logger.error(IOUtils.getError(e));
//			}
//        	SerialPortManager.openPort("COM4",1152000);
        } finally {
            try {
                if (in != null) {
                    in.close();
                    in = null;
                }
            } catch(IOException e) {
            	logger.error(IOUtils.getError(e));
            }

        }
        return str;
	}

	/**
	 * 添加监听器
	 * 
	 * @param port
	 *            串口对象
	 * @param listener
	 *            串口监听器
	 * @throws TooManyListeners
	 *             监听类对象过多
	 */
	public static void addListener(SerialPort port, SerialPortEventListener listener){
		try {
			// 给串口添加监听器
			port.addEventListener(listener);
			// 设置当有数据到达时唤醒监听接收线程
			port.notifyOnDataAvailable(true);
			// 设置当通信中断时唤醒中断线程
			port.notifyOnBreakInterrupt(true);
		} catch (TooManyListenersException e) {
		}
	}
}