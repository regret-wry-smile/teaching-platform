package com.ejet.netty.client;

import com.ejet.netty.message.LiveAnswerMsg;
import com.ejet.netty.message.LiveBaseMsg;
import com.ejet.netty.message.LiveMsgType;
import com.zkxltech.teaching.CommunicationRequest;
import com.zkxltech.teaching.msg.AnswerRequest;

import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * netty socket客户端实现类
 * 
 * @author ShenYijie
 *
 */
public class LiveClientBusinessHandler extends LiveClientBaseHandler {
	
	public LiveClientBusinessHandler(String name) {
		super(name);
	}

	@Override
    public void handleData(ChannelHandlerContext ctx, Object evt) throws Exception {
		logger.info("[客户端处理消息实现类] {} 处理消息!", name);
		LiveBaseMsg message = (LiveBaseMsg) evt; // (1)
		if(message.getType()==LiveMsgType.ANSWER_REQ) { //请求答题
			LiveAnswerMsg msg = (LiveAnswerMsg)message;
			CommunicationRequest.startAnswer((AnswerRequest)msg.getContent());
		}
		if(message.getType()==LiveMsgType.ANSWER_STOP) { //停止答题
			CommunicationRequest.stopAnswer();
		}
		if(message.getType()==LiveMsgType.ANSWER_SELECT) { //选择答案
			LiveAnswerMsg msg = (LiveAnswerMsg)message;
			CommunicationRequest.selectAnswer((String)msg.getContent());
		}
		
    }
	
	
}
