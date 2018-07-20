package com.zkxltech.ui.functions;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.ejet.core.util.constant.Constant;
import com.zkxltech.domain.Result;
import com.zkxltech.service.SettingService;
import com.zkxltech.service.impl.EquipmentServiceImpl2;
import com.zkxltech.service.impl.SettingServiceImpl;

import net.sf.json.JSONObject;

/**
 * 【设置模块页面调用方法】
 * 	get_all_name 获取所有组名
 *	login 登录
 *	read_setting 读取设置
 *	set	设置
 *	set_default 默认设置
 */
public class SetFunctionManage extends BrowserFunction{
	private SettingService settingService = new SettingServiceImpl();
	public SetFunctionManage(Browser browser, String name) {
		super(browser, name);
	}
	@Override
	public Object function(Object[] params) {
		Result result = new Result();
		if (params.length>0) {
			String method = (String) params[0]; //页面要调用的方法
			Object param = params.length == 2 ? params[1] : new Object(); //页面要调用该方法的参数
			switch (method) {
			case "get_all_name":
				result = settingService.getAllName();
				break;
			case "login":
				result = settingService.login(param);
				break;
			case "read_setting":
				result = settingService.readSetting();
				break;
			case "set":
				result = settingService.set(param);
				break;
			case "set_default":
				result = settingService.setDefault();
				break;
			case "equipment_database_synchronization":
			    /* 同步设备和数据库中卡对应的学生的绑定状态*/
//			    result = EquipmentServiceImpl.getInstance().equipmentDatabaseSynchronization();
			    result = EquipmentServiceImpl2.getInstance().equipmentDatabaseSynchronization();
                break;
			default:
				result.setRet(Constant.ERROR);
				result.setMessage("【"+method+"】未找到该指令！");
			}
		}else {
			result.setRet(Constant.ERROR);
			result.setMessage("参数不能为空！");
		}
		return JSONObject.fromObject(result).toString();
	}
}
