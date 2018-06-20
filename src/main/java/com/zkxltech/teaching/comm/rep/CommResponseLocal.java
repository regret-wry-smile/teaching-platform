package com.zkxltech.teaching.comm.rep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.cache.BrowserManager;
import com.ejet.cache.TeachingCache;
import com.ejet.core.util.constant.Constant;
import com.ejet.netty.client.LiveNettyClientHelper;
import com.ejet.netty.message.LiveAnswerMsg;
import com.ejet.netty.message.LiveMsgType;
import com.zkxltech.config.Global;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.teaching.AnswerVO;
import com.zkxltech.teaching.comm.CommBase;
import com.zkxltech.teaching.comm.CommResponseInterface;
import com.zkxltech.teaching.msg.AnswerRequest;
import com.zkxltech.teaching.msg.AnswerResponse;
import com.zkxltech.teaching.msg.DeviceBindResponse;
import com.zkxltech.teaching.msg.EchoRequest;
import com.zkxltech.teaching.service.StudentInfoService;
import com.zkxltech.teaching.service.impl.StudentInfoServiceImpl;

/**
 * 通信返回总接口
 * 
 * @author ShenYijie
 *
 */
public class CommResponseLocal extends CommBase implements CommResponseInterface {
	
	private static final Logger logger = LoggerFactory.getLogger(CommResponseLocal.class);
	
	/**
	 * 回显
	 */
	@Override
	public void echoDataRep(EchoRequest rep) {
		// TODO Auto-generated method stub
		logger.info("学生端本地端回显回调信息......");
	}

	/**
	 * 绑卡返回
	 */
	@Override
	public void bindCardRep(DeviceBindResponse rep) {
		
	}

	
	/**
	 * 答题上报
	 */
	@Override
	public void answerRep(AnswerResponse rep) {
		
	}

	/**
	 * 
	 * 停止答题
	 */
	@Override
	public void stopAnswerRep(AnswerResponse rep) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void answerUpload(AnswerVO vo) {
		logger.info("【学生端答题上报】");
	}

	
	

}
