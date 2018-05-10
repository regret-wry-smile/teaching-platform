package com.zkxltech.teaching.comm.req;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.cache.TeachingCache;
import com.ejet.netty.message.LiveAnswerMsg;
import com.ejet.netty.message.LiveMsgType;
import com.ejet.netty.server.LiveChannelGroups;
import com.zkxltech.config.Global;
import com.zkxltech.teaching.comm.CommBase;
import com.zkxltech.teaching.comm.CommRequestInterface;
import com.zkxltech.teaching.msg.AnswerRequest;

/**
 * 教师端实现类
 * 
 * @author ShenYijie
 *
 */
public class CommRequestTeacher extends CommBase implements CommRequestInterface {
	
	private static final Logger logger = LoggerFactory.getLogger(CommRequestTeacher.class);
	/**
	 * 开始答题
	 * @param req
	 */
	@Override
	public void startAnswer(AnswerRequest req) {
		//发送答题广播消息
		logger.info("【教师端请求答题信息广播】{} " , format(req.getQuestionType()) );
		LiveAnswerMsg msg = new LiveAnswerMsg();
		msg.setType(LiveMsgType.ANSWER_REQ);
		msg.setContent(req);
		LiveChannelGroups.broadcast(msg);
		
		//清空缓存
		TeachingCache.cleanAnswerCache();
		Global.setStopAnswer(false);
	}
	
	
	@Override
	public void stopAnswer() {
		logger.info("【教师端请求停止答题信息广播】" );
		LiveAnswerMsg msg = new LiveAnswerMsg();
		msg.setType(LiveMsgType.ANSWER_STOP);
		LiveChannelGroups.broadcast(msg);
		
		Global.setStopAnswer(true);
	}


	@Override
	public void selectAnswer(String answer) {
		// TODO Auto-generated method stub
		logger.info("【教师端请求选择答案信息广播】" );
		LiveAnswerMsg msg = new LiveAnswerMsg();
		msg.setType(LiveMsgType.ANSWER_SELECT);
		msg.setContent(answer);
		LiveChannelGroups.broadcast(msg);
	}
	

}
