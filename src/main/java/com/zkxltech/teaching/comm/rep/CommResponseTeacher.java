package com.zkxltech.teaching.comm.rep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.cache.BrowserManager;
import com.ejet.cache.TeachingCache;
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
		//首先从已经绑定名单查询
		String ichickerId = rep.getIclickerId();
		
		//获取缓存中已经绑定的名单
		StudentInfo stu = TeachingCache.getBindStudentByIchickerId(ichickerId);
		if(stu!=null) {
			logger.info("教师端答题器已经绑定:{}", ichickerId);
			//可以页面弹窗提示
			
		} else {//从未绑定缓存中顺序取
			stu = TeachingCache.getStudentForBind();
		}
		if(stu==null) { //测试用
			BrowserManager.retResult("教师端未找学生信息， 请导入学生名单!");
			return;
		}
		stu.setIclickerId(rep.getIclickerId());
		EchoRequest req = new EchoRequest();
		req.setId(stu.getId()+"");
        req.setIclickerId(stu.getIclickerId()); //答题ID
        req.setStudentName(stu.getStudentName()); //名字
        req.setClassId(stu.getClassId());  //班级
        
        //回显答题器对应名字信息
		echoName(req);
		
		//消息是否需要同步到远端
        if(Global.isSynToTeacher()) {
        	LiveAnswerMsg msg = new LiveAnswerMsg();
	    	msg.setType(LiveMsgType.BIND_CARD_REP);
	    	msg.setContent(rep);
	    	LiveNettyClientHelper.send(msg);
	    }
        
        //添加到帮卡缓存
        TeachingCache.addBindStudent(stu);
        logger.info("教师端绑卡: 答题器ID：{},  主键id： {}, 学生名字：{} " , 
        		stu.getIclickerId(),  stu.getId(), stu.getStudentName());
        
        BrowserManager.bindCard(req);
        
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
		if(Global.isStopAnswer()) {
			logger.info("【教师端已经停止答题，请求无效】");
			return;
		}
		//保存答题信息
		try {
			//添加答题信息到缓存，成功后才同步
			boolean ret = TeachingCache.addAnswerRep(vo);
		} catch (Exception e) {
			logger.error("【教师端上报保存答题信息】", e);
		}
	}
	

}
