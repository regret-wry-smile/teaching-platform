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
		//首先从已经绑定名单查询
		String ichickerId = rep.getIclickerId();
		
		//获取缓存中已经绑定的名单
		StudentInfo stu = TeachingCache.getBindStudentByIchickerId(ichickerId);
		if(stu!=null) {
			logger.info("学生端答题器已经绑定:{}", ichickerId);
			//可以页面弹窗提示
			
		} else {//从未绑定缓存中顺序取
			stu = TeachingCache.getStudentForBind();
		}
		if(stu==null) { //测试用
			BrowserManager.retResult("学生端未找学生信息， 请导入学生名单!");
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
        logger.info("学生端绑卡: 答题器ID：{},  主键id： {}, 学生名字：{} " , 
        		stu.getIclickerId(),  stu.getId(), stu.getStudentName());
        
        StudentInfoService studentInfoService = new StudentInfoServiceImpl();
		StudentInfo studentInfo = new StudentInfo();
		studentInfo.setId(Integer.parseInt(req.getId()));
		studentInfo.setIclickerId(req.getIclickerId());
		studentInfo.setStatus(Constant.BING_YES);
		studentInfoService.updateStudentInfo(studentInfo);
        
	}

	
	/**
	 * 答题上报
	 */
	@Override
	public void answerRep(AnswerResponse rep) {
		if(Global.isStopAnswer()) {
			logger.info("【学生端已经停止答题，请求无效】");
			return;
		}
		if (!rep.getAnswerType().equals(Global.getAnswerType())) {
			logger.info("【答题类型不匹配，请求无效】");
			return;
		}
		//保存答题信息
		try {
			//增加到缓存中
			AnswerVO vo = new AnswerVO();
			vo.setAnswerType(rep.getAnswerType());
			vo.setAnswer(rep.getAnswer());
			vo.setAnswerDateTime(rep.getAnswerDateTime());
			vo.setIclickerId(rep.getIclickerId());
			
			//添加答题信息到缓存，成功后才同步
			boolean ret = TeachingCache.addAnswerRep(vo);
			
			 //消息是否需要同步到远端
			if(ret && Global.isSynToTeacher()) {
				LiveAnswerMsg msg = new LiveAnswerMsg();
	        	msg.setType(LiveMsgType.ANSWER_REP);
	        	msg.setContent(vo);
	        	LiveNettyClientHelper.send(msg);
			}
		
		} catch (Exception e) {
			logger.error("【学生端上报保存答题信息】", e);
		}
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
