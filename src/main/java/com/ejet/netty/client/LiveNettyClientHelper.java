package com.ejet.netty.client;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.netty.message.LiveBaseMsg;
import com.ejet.netty.message.LiveMsgType;
import com.zkxltech.config.ConfigConstant;

/**
 * 
 * 客户端连接管理类
 * 
 * @author ShenYijie
 *
 */
public class LiveNettyClientHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(LiveNettyClientHelper.class);
	/**
	 * 待发送缓存，发送失败，或者链接失败时，缓存，下次发送
	 */
	private  static Map<LiveMsgType, LiveBaseMsg> sendCache = Collections.synchronizedMap(new HashMap<LiveMsgType, LiveBaseMsg>());
	
	/**
	 * 单实例
	 */
	private static LiveNettyClientHelper instance = null;
	/**
	 * 客户端连接管理
	 */
	private static WeakReference<LiveNettyClient> weakClient = null;
	
	private LiveNettyClientHelper() {
		
	}
	/**
	 * 发送消息
	 * 
	 * @param msg
	 * @return
	 */
	public static int send(LiveBaseMsg msg) {
		int length = 0;
		logger.info("发送服务端:" + msg.toString());
		try {
			LiveNettyClient cc = weakClient.get();
			if(cc!=null) {
				cc.sendData(msg);
			} else {
				sendCache.put(msg.getType(), msg);
				startNettyClient();
			}
		} catch (Exception e) {
			sendCache.put(msg.getType(), msg);
		}
		return length;
	}

	
	
	
	/**
	 * 启动客户端
	 * @throws InstantiationException
	 * @throws Exception
	 */
	public static void startNettyClient() {
		//默认调用启动方法
		logger.info("客户端启动{}:{}........", ConfigConstant.clientLocalConf.getRemote_server_ip(),
											  ConfigConstant.clientLocalConf.getRemote_server_port());
		LiveNettyClient client = new LiveNettyClient(ConfigConstant.clientLocalConf.getRemote_server_ip(),
				ConfigConstant.clientLocalConf.getRemote_server_port());
		setClient(client);
		client.start();
		
	}

	/**
	 * 启动客户端重连
	 * @throws InstantiationException
	 * @throws Exception
	 */
	public static void reConnected() {
		//默认调用启动方法
		logger.info("客户端重连{}:{}........", ConfigConstant.clientLocalConf.getRemote_server_ip(),
											  ConfigConstant.clientLocalConf.getRemote_server_port());
		LiveNettyClient cc = weakClient.get();
		if(cc!=null) {
			cc.doConnect();
		} 
	}
	
	public static void setClient(LiveNettyClient client) {
		weakClient = new WeakReference<LiveNettyClient>(client);
	}
	
	public static void cleanSendFailedCache() {
		logger.info("学生端清空待发送缓存........");
		sendCache.clear();
	}
	
	/**
	 * 重新发送
	 */
	public static void reSendCache() {
		Iterator it = sendCache.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<LiveMsgType, LiveBaseMsg> entry = (Entry<LiveMsgType, LiveBaseMsg>) it.next();
			LiveMsgType key = entry.getKey();
			send(sendCache.get(key));
		}
	}
	

}
