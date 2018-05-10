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
		DeviceComm.startAnswer(req);
		//答题类型，切换界面
		BrowserManager.selectAnswerType(req.getBusinessQusetionType());
		//清空缓存
		TeachingCache.cleanAnswerCache();
		Global.setStopAnswer(false);
		//本地待发送缓存清空
		LiveNettyClientHelper.cleanSendFailedCache();
	}
	
	/**
	 * 停止答题
	 */
	@Override
	public void stopAnswer() {
		logger.info("【本地端请求停止答题】" );
		//如果需要同步
		if(Global.isSynToTeacher()) { //只有本地需要同步时，才需要同步界面
			BrowserManager.stopAnswer();
		}
		Global.setStopAnswer(true);
		//本地待发送缓存清空
		LiveNettyClientHelper.cleanSendFailedCache();
	}

	@Override
	public void selectAnswer(String answer) {
		// TODO Auto-generated method stub
		BrowserManager.chartClick(answer);
	}
	
	
}
