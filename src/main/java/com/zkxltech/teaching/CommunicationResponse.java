package com.zkxltech.teaching;

import com.zkxltech.config.Global;
import com.zkxltech.teaching.comm.CommResponseInterface;
import com.zkxltech.teaching.comm.rep.CommResponseLocal;
import com.zkxltech.teaching.comm.rep.CommResponseTeacher;
import com.zkxltech.teaching.msg.AnswerResponse;
import com.zkxltech.teaching.msg.DeviceBindResponse;

/**
 * 通信返回总接口
 * 
 * @author ShenYijie
 *
 */
public class CommunicationResponse {
	
	private static CommResponseInterface response = null;
	
	static {
		if(Global.isTeacher()) {
			response = new CommResponseTeacher();
		} else {
			response = new CommResponseLocal();
		}
	}
	
	/**
	 * 答题上报
	 * 
	 * @param rep
	 */
	public static void answerRep(AnswerResponse rep) {
		response.answerRep(rep);
	}
	/**
	 * 停止答题
	 * 
	 * @param rep
	 */
	public static void stopAnswerRep(AnswerResponse rep) {
		response.stopAnswerRep(rep);
	}
	
	/**
	 * 绑卡返回
	 */
	public static void bindCardRep(DeviceBindResponse rep) {
		response.bindCardRep(rep);
	}
	
	/**
	 * 答题上报
	 * 
	 * @param vo
	 */
	public static void answerUpload(AnswerVO vo) {
		response.answerUpload(vo);
	}
	

}
