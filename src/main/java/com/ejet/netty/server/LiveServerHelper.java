package com.ejet.netty.server;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ejet.netty.NettyConstant;
import com.ejet.netty.message.LiveBaseMsg;
import com.ejet.netty.message.LiveNettyClientInfo;

import io.netty.channel.Channel;

/**
 * 服务端帮助类
 * 
 * @author ShenYijie
 *
 */
public class LiveServerHelper {
	
	public static Map<Channel, LiveNettyClientInfo> channels = Collections.synchronizedMap(new HashMap<Channel, LiveNettyClientInfo>());
	public static Map<String, LiveNettyClientInfo> channelCache = Collections.synchronizedMap(new HashMap<String, LiveNettyClientInfo>());
	/**
	 * 获取所有客户端连接
	 * 
	 * @return
	 */
	public static List<LiveNettyClientInfo> getClients() {
//		List<LiveNettyClientInfo> list = new ArrayList<>();
//		Iterator<Entry<Channel, LiveNettyClientInfo>> it = channels.entrySet().iterator();
//		LiveNettyClientInfo value = null;
//		while(it.hasNext()) {
//			Map.Entry<Channel, LiveNettyClientInfo> entry = (Entry<Channel, LiveNettyClientInfo>) it.next();
//			Channel key = entry.getKey();
//			value = channels.get(key);
//			if(value!=null) {
//				list.add(value);
//			}
//		}
//		return list;
		return getClient();
	}
	
	public static List<LiveNettyClientInfo> getClient() {
		List<LiveNettyClientInfo> list = new ArrayList<>();
		Iterator<Entry<String, LiveNettyClientInfo>> it = channelCache.entrySet().iterator();
		LiveNettyClientInfo value = null;
		while(it.hasNext()) {
			Map.Entry<String, LiveNettyClientInfo> entry = (Entry<String, LiveNettyClientInfo>) it.next();
			String key = entry.getKey();
			value = channelCache.get(key);
			if(value!=null) {
				list.add(value);
			}
		}
		return list;
	}
	
	/**
	 * 增加客户端
	 * 
	 * @param ch
	 * @param message 
	 */
	public static void addClient(Channel ch, LiveBaseMsg message) {
		//获取客户端key
		String classId = message.getClientId(); //启动的唯一ID
		if(channelCache.containsKey(classId) || channels.containsKey(ch)) {
			return;
		}
		
		LiveNettyClientInfo client = new LiveNettyClientInfo();
		InetSocketAddress insocket = (InetSocketAddress)ch.remoteAddress();
		String ip = insocket.getAddress().getHostAddress();
		client.setState(1);
		client.setIp(ip);
		client.setClientId(message.getClientId());
		client.setClassName(message.getClientName());
		
		channelCache.put(classId, client);
		channels.put(ch, client);
	}
	
	public static void removeClient(Channel ch) {
		if(ch!=null) {
			LiveNettyClientInfo client = channels.remove(ch);
			if(client!=null) {
				String classId = client.getClientId();
				channelCache.remove(classId);
			}
		}
	}
	

	
	
}
