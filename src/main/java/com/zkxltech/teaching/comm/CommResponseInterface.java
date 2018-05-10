package com.zkxltech.teaching.comm;

import com.zkxltech.teaching.AnswerVO;
import com.zkxltech.teaching.msg.AnswerResponse;
import com.zkxltech.teaching.msg.DeviceBindResponse;
import com.zkxltech.teaching.msg.EchoRequest;

/**
 * 通信返回总接口
 * 
 * @author ShenYijie
 *
 */
public interface CommResponseInterface {
	/**
	 * 回显接口
	 * @param rep
	 */
	public void echoDataRep(EchoRequest rep);
	
	/**
	 * 绑卡返回
	 * @param rep
	 */
	public void bindCardRep(DeviceBindResponse rep);
	
	/**
	 * 答题上报
	 * 
	 * @param rep
	 */
	public void answerRep(AnswerResponse rep);
	
	/**
	 * 答题信息上传
	 * 
	 * @param rep
	 */
	public void answerUpload(AnswerVO vo);
	/**
	 * 停止答题
	 * 
	 * @param rep
	 */
	public void stopAnswerRep(AnswerResponse rep);

}
