package com.zkxltech.teaching;

import com.zkxltech.config.Global;
import com.zkxltech.teaching.comm.CommRequestInterface;
import com.zkxltech.teaching.comm.req.CommRequestLocal;
import com.zkxltech.teaching.comm.req.CommRequestTeacher;
import com.zkxltech.teaching.msg.AnswerRequest;
import com.zkxltech.teaching.msg.EchoRequest;

/**
 * 通信请求总接口
 * 
 * @author ShenYijie
 *
 */
public class CommunicationRequest {
	
	private static CommRequestInterface request = null;
	
	static {
		if(Global.isTeacher()) {
			request = new CommRequestTeacher();
		} else {
			request = new CommRequestLocal();
		}
	}
	
	/**
	 * 开始答题
	 */
	public static void startAnswer(AnswerRequest req) {
		Global.setAnswerType(req.getQuestionType());
		request.startAnswer(req);
	}
	/**
	 * 停止答题
	 */
	public static void stopAnswer() {
		request.stopAnswer();
	}
	
	/**
	 * 开始绑卡
	 */
	public static void startBind() {
		request.startBind();
	}
	
	/**
	 * 停止绑卡
	 */
	public static void stopBind() {
		request.stopBind();
	}
	
	/**
	 * 回显名字
	 */
	public static void echoName(EchoRequest req) {
		request.echoName(req);
	}
	
	/**
	 * 选择答案
	 */
	public static void selectAnswer(String answer) {
		request.selectAnswer(answer);
	}
	
	
}
