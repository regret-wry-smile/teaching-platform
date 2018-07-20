package com.ejet.core.util.constant;

import java.util.Date;
import java.util.List;

import com.ejet.core.util.StringUtils;
import com.zkxltech.domain.Answer;

public class EquipmentConstant {
	/**
	 * 开始答题
	 */
	public static String ANSWER_START = "answer_start";
	/**
	 * 答题信息
	 */
	public static String UPDATE_ANSWER_LIST = "update_answer_list";
	
	/**
	 * 停止答题
	 */
	public static String ANSWER_STOP = "answer_stop";
	/**
	 * 清除白名单
	 */
	public static String CLEAR_WL = "clear_wl";
	/**
	 * 设置信道
	 */
	public static String SET_CHANNEL = "set_channel";
	/**
	 * 设置频率
	 */
	public static String SET_TX_POWER = "set_tx_power";
	
	/**
	 * 获取设备信息
	 */
	public static String GET_DEVICE_INFO = "get_device_info";
	/**
	 * 开始绑定
	 */
	public static String WIRELESS_BIND_START = "wireless_bind_start";
	/**
	 * 获取绑定信息
	 */
	public static String UPDATE_WIRELESS_CARD_INFO = "update_wireless_card_info";
	
	/**
	 * 停止绑定
	 */
	public static String WIRELESS_BIND_STOP = "wireless_bind_stop";
	
	/**
	 * 开始答题硬件指令
	 * @param answers 题目信息
	 * @return
	 */
	public static String ANSWER_START_CODE(List<Answer> answers) {
		StringBuilder s = new StringBuilder();
		s.append("{'fun': 'answer_start','time': '");
		s.append(StringUtils.formatDateTime(new Date(),"yyyy-MM-dd HH:mm:ss:S"));
		s.append("', 'questions': [ ");
		int questionNum = answers.size();
		for (int i = 0; i < questionNum; i++) {
			Answer answer = answers.get(i);
			s.append("{");
			s.append("'type':'"+answer.getType()+"',");
			s.append("'id':'"+answer.getId()+"',");
			s.append("'range':'"+answer.getRange()+"'");
			s.append("}");
			if (i!=questionNum-1) {
				s.append(",");
			}
		}
		s.append("]}");
		return s.toString();
	}
	/**
	 * 开始答题硬件指令
	 * @param answers 题目信息
	 * @return
	 */
	public static String ANSWER_START_CODE(String answers) {
		StringBuilder s = new StringBuilder();
		s.append("{'fun': 'answer_start','time': '");
		s.append(StringUtils.formatDateTime(new Date(),"yyyy-MM-dd HH:mm:ss:S"));
		s.append("', 'questions': ");
		s.append(answers);
		s.append("}");
		return s.toString();
	}
	/**
	 * 停止答题硬件指令
	 */
	public static String ANSWER_STOP_CODE = "{'fun': 'answer_stop'}";
	/**
	 * 清除白名单硬件指令
	 */
	public static String CLEAR_WL_CODE = "{'fun': 'clear_wl'}";
	/**
	 * 设置信道硬件指令
	 * @param tx_ch 答题器的发送信道 
	 * @param rx_ch 答题器的接收信道
	 * @return
	 */
	public static String SET_CHANNEL_CODE(int tx_ch,int rx_ch) {
		StringBuilder s = new StringBuilder();
		s.append("{'fun': 'set_channel','tx_ch': '"+tx_ch+"','rx_ch': '"+rx_ch+"'}");
		
		return s.toString();
	}
	/**
	 * 设置频率硬件指令
	 * @param tx_power 答题器的发送功率
	 * @return
	 */
	public static String SET_TX_POWER_CODE(int tx_power) {
		StringBuilder s = new StringBuilder();
		s.append("{'fun': 'set_tx_power','tx_power': '"+tx_power+"'}");
		return s.toString();
	}
	/**
	 * 获取设备信息硬件指令
	 */
	public static String GET_DEVICE_INFO_CODE = "{'fun': 'get_device_info'}";
	/**
	 * 开始绑定硬件指令
	 */
	public static String WIRELESS_BIND_START_CODE = "{'fun': 'wireless_bind_start','mode': '1', 'uids': []}";
	/**
	 * 停止绑定硬件指令
	 */
	public static String WIRELESS_BIND_STOP_CODE = "{'fun': 'wireless_bind_stop'}";
	
	
	
}
