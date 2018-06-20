package com.zkxltech.teaching.comm.rep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zkxltech.teaching.AnswerVO;
import com.zkxltech.teaching.comm.CommBase;
import com.zkxltech.teaching.comm.CommResponseInterface;
import com.zkxltech.teaching.msg.AnswerResponse;
import com.zkxltech.teaching.msg.DeviceBindResponse;
import com.zkxltech.teaching.msg.EchoRequest;

/**
 * 通信返回总接口
 * 
 * @author ShenYijie
 *
 */
public class CommResponseTeacher extends CommBase implements CommResponseInterface {
	
	private static final Logger logger = LoggerFactory.getLogger(CommResponseTeacher.class);
	
	/**
	 * 回显
	 */
	@Override
	public void echoDataRep(EchoRequest rep) {
		// TODO Auto-generated method stub
		logger.info("教师端本地端回显回调信息......");
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
		logger.info("【教师端答题上报】");
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
		
	}
	

}
