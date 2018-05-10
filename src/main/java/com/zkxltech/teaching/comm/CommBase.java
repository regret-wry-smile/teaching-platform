package com.zkxltech.teaching.comm;

import com.zkxltech.teaching.device.DeviceComm;
import com.zkxltech.teaching.msg.EchoRequest;

public abstract class CommBase {
	/**
	 * 开始绑卡
	 */
	//@Override
	public void startBind() {
		DeviceComm.startBindCard();
	}
	
	/**
	 * 停止绑卡
	 */
	//@Override
	public void stopBind() {
		
	}
	/**
	 * 回显名字
	 */
	//@Override
	public void echoName(EchoRequest req) {
		req.setClassId(req.getClassId());
		DeviceComm.echoName(req);
	}
	/**
	 * 答题类型
	 * 
	 * @param type
	 * @return
	 */
	public String format(String type) {
		String str = "NULL";
		if(type==null || type.equals("")) {
			return str;
		}
		switch (type) {
		case "1":
			str = "单题单选";
			break;
		case "2":
			str = "是非判断";
			break;
		case "3":
			str = "抢红包";
			break;
		case "4":
			str = "单题多选";
			break;
		case "5":
			str = "多题单选";
			break;
		case "6":
			str = "通用数据类型";
			break;
		case "7":
			str = "6键单选";
			break;
		default:
			str = "未知";
			break;
		}
		return str;
	}

}
