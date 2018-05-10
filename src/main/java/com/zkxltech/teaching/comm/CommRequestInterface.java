package com.zkxltech.teaching.comm;

import com.zkxltech.teaching.msg.AnswerRequest;
import com.zkxltech.teaching.msg.EchoRequest;

/**
 * 请求通信接口
 * 
 * @author ShenYijie
 *
 */
public interface CommRequestInterface {
	/**
	 * 开始答题
	 */
	public void startAnswer(AnswerRequest req);
	/**
	 * 停止答题
	 */
	public void stopAnswer();
	/**
	 * 开始绑卡
	 */
	public void startBind();
	/**
	 * 停止绑定
	 */
	public void stopBind();
	/**
	 * 回显名字
	 */
	public void echoName(EchoRequest req);
	/**
	 * 选择答案
	 */
	public void selectAnswer(String answer);
	
}
