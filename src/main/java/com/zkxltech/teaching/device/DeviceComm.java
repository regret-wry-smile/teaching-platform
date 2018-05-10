package com.zkxltech.teaching.device;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.TimeUtils;
import com.spark.teaching.model.CloseBindCard;
import com.spark.teaching.model.Echo;
import com.spark.teaching.model.EchoRep;
import com.spark.teaching.model.OpenBindCard;
import com.spark.teaching.model.ReceiveAnswer;
import com.spark.teaching.model.ReportBindCard;
import com.spark.teaching.model.SendQuestion;
import com.spark.teaching.usb4java.DataPackageInterface;
import com.spark.teaching.usb4java.LibUsb4Java;
import com.zkxltech.config.ConfigConstant;
import com.zkxltech.teaching.CommunicationResponse;
import com.zkxltech.teaching.msg.AnswerRequest;
import com.zkxltech.teaching.msg.AnswerResponse;
import com.zkxltech.teaching.msg.DeviceBindResponse;
import com.zkxltech.teaching.msg.EchoRequest;

/**
 * 设备通信接口
 * 
 * @author ShenYijie
 *
 */
public class DeviceComm {
	private static final Logger logger = LoggerFactory.getLogger(DeviceComm.class);
	/**
	 * 设备信息初始化
	 */
	public static void init() {
		logger.info("====== 开启hid设备通信初始化......");
		LibUsb4Java.initInterface(new DataPackageInterface() {
            @Override
            public void OperateReceiveAnswerDataPackage(ReceiveAnswer receiveAnswer) {
                logger.info("答题信息报===》" + receiveAnswer);
                AnswerResponse rep = new AnswerResponse();
                rep.setAnswerDateTime(TimeUtils.formatDateByFormat(receiveAnswer.getTime(), "yyyyMMddHHmmssSSS"));
                rep.setAnswer(receiveAnswer.getAnswer());
                rep.setAnswerType( (receiveAnswer.getType() & 0xff) +"");
                rep.setIclickerId(receiveAnswer.getUid()+"");
                rep.setRssi(receiveAnswer.getRssi()&0xff);
                
                CommunicationResponse.answerRep(rep);
            }
            @Override
            public void OperateReportBindCardDataPackage(ReportBindCard reportBindCard) {
            	logger.info("绑定数据报===》" + reportBindCard);
                DeviceBindResponse rep = new DeviceBindResponse();
                rep.setIclickerId(reportBindCard.getUid()+"");
                rep.setReplaceIclickerId(reportBindCard.getPre_uid()+"");
                
                CommunicationResponse.bindCardRep(rep);
            }
            
			@Override
			public void OperateReportEchoDataPackage(EchoRep echoRep) {
				// TODO Auto-generated method stub
				logger.info("绑定数据报===》" + echoRep);
			}
            
			
        });
        LibUsb4Java.start();
	}
	
	/**
	 * 开始绑卡
	 * 
	 * @param req
	 * @param call
	 */
	public static void startBindCard() {
		OpenBindCard openBindCard = new OpenBindCard();
		LibUsb4Java.send(openBindCard);
	}
	
	/**
	 * 关闭绑卡
	 */
	public static void stopBindCard() {
		CloseBindCard closeBindCard = new CloseBindCard();
		LibUsb4Java.send(closeBindCard);
	}
	
	/**
	 * 停止答题（广播）
	 * @param req
	 * @param call
	 */
	public static void stopAnswer() {
		// TODO Auto-generated method stub
		SendQuestion send = new SendQuestion();
		send.setQuestionType((byte)0x80);
		send.setTime(getDate());
		LibUsb4Java.send(send);
	}
	
	/**
	 * 开始答题（广播）
	 * @param req
	 * @param call
	 */
	public static void startAnswer(AnswerRequest req) {
		// TODO Auto-generated method stub
		SendQuestion send = new SendQuestion();
		send.setTime(getDate());
		send.setQuestionType(formatType(req.getQuestionType()));
		LibUsb4Java.send(send);
	}
	
	/**
	 * 回显名字
	 */
	public static void echoName(EchoRequest req) {
		Echo send = new Echo();
		send.setUid(Long.valueOf(req.getIclickerId()));
		String showInfo = req.getStudentName()+"[" + req.getClassId()+"]";
		if(showInfo.length()>24) {
			showInfo = showInfo.substring(0, 24);
		}
		send.setShow_info(showInfo);
		LibUsb4Java.send(send);
	}
	
	
	public static byte formatType(String type) {
		int value = Integer.parseInt(type);
		byte qtype = (byte) value;
		return qtype;
	}
	
	private static Date getDate() {
		Date date = new Date();
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = dateFormater.format(new Date());
		try {
			date = dateFormater.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	
}
