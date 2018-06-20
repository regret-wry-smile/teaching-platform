package com.zkxltech.teaching.comm.req;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.cache.BrowserManager;
import com.ejet.cache.TeachingCache;
import com.ejet.netty.client.LiveNettyClientHelper;
import com.zkxltech.config.Global;
import com.zkxltech.teaching.comm.CommBase;
import com.zkxltech.teaching.comm.CommRequestInterface;
import com.zkxltech.teaching.device.DeviceComm;
import com.zkxltech.teaching.msg.AnswerRequest;

/**
 * 通信请求总接口
 * 
 * @author ShenYijie
 *
 */
public class CommRequestLocal extends CommBase  implements CommRequestInterface {
	
	private static final Logger logger = LoggerFactory.getLogger(CommRequestLocal.class);
	/**
	 * 开始答题
	 */
	@Override
	public void startAnswer(AnswerRequest req) {
		logger.info("【本地端请求答题】{} " , format(req.getQuestionType()) );
		
	}
	
	/**
	 * 停止答题
	 */
	@Override
	public void stopAnswer() {
		logger.info("【本地端请求停止答题】" );
		
	}

	@Override
	public void selectAnswer(String answer) {
	}
	
	
}
