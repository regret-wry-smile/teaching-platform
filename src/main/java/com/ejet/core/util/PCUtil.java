package com.ejet.core.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class PCUtil {

	/*
	 * 获取主机名
	 */
	public static String getHostName() {
		String hostName = "";
		try {
			InetAddress inet = InetAddress.getLocalHost();
			hostName = inet.getHostName();
		} catch (UnknownHostException e) {
			//e.printStackTrace();
		}
		return hostName;
	}
	/*
	 * 获取主机地址
	 */
	public static String getHostAddress() {
		String hostAddress = "";
		try {
			InetAddress inet = InetAddress.getLocalHost();
			hostAddress = inet.getHostAddress();
		} catch (UnknownHostException e) {
			//e.printStackTrace();
		}
		return hostAddress;
	}

	/**
	 * 获取MAC地址
	 * 
	 * @throws SocketException
	 * @throws UnknownHostException
	 */
	public static String getMacAddress() {
		StringBuffer sb = new StringBuffer("");
		try {
			InetAddress ia = InetAddress.getLocalHost(); 			// 获取网卡，获取地址
			byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
			for (int i = 0; i < mac.length; i++) {
				if (i != 0) {
					sb.append("-");
				}
				// 字节转换为整数
				int temp = mac[i] & 0xff;
				String str = Integer.toHexString(temp);
				if (str.length() == 1) {
					sb.append("0" + str);
				} else {
					sb.append(str);
				}
			}
		} catch (UnknownHostException | SocketException e) {
			//e.printStackTrace();
		}
		return sb.toString();
	}

}
