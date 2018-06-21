package com.zkxltech.teaching.ui;

import org.junit.Test;

import com.zkxltech.domain.Result;
import com.zkxltech.service.SettingService;
import com.zkxltech.service.impl.SettingServiceImpl;
import com.zkxltech.ui.enums.SettingEnum;

import net.sf.json.JSONObject;

public class TestDemo01 {
	@Test
	public void test_enum() {
		System.out.println("获取发送信道："+SettingEnum.getTxchByName("第九组"));
		System.out.println("获取接收信道："+SettingEnum.getRxchByName("第九组"));
		System.out.println("获取组名："+SettingEnum.getNameByTxchAndRxch(2, 52));
	}
	@Test
	public void login() {
		SettingService service = new SettingServiceImpl();
		Object param = "{'loginId':'luozheng','password':'123'}";
		Result result = service.login(param);
		System.out.println(JSONObject.fromObject(result));
	}
}
