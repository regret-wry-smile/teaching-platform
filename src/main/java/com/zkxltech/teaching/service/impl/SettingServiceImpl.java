package com.zkxltech.teaching.service.impl;

import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.config.ConfigConstant;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.Setting;
import com.zkxltech.domain.User;
import com.zkxltech.teaching.service.SettingService;
import com.zkxltech.ui.enums.SettingEnum;
import com.zkxltech.ui.util.StringUtils;

public class SettingServiceImpl implements SettingService{
	private Result result;
	
	@Override
	public Result getAllName() {
		result = new Result();
		result.setItem(SettingEnum.getAllName());
		result.setRet(Constant.SUCCESS);
		return result;
	}
	
	@Override
	public Result login(Object object) {
		try {
			result = new Result();
			User user = StringUtils.parseJSON(object, User.class);
			String loginId = ConfigConstant.projectConf.getLogin_id();
			String password = ConfigConstant.projectConf.getPassword();
			if (loginId.equals(user.getLoginId())&& password.equals(user.getPassword())) {
				result.setRet(Constant.SUCCESS);
				return result;
			}
			result.setRet(Constant.ERROR);
			result.setMessage("账号密码错误！");
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("系统错误!");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}
	@Override
	public Result readSetting() {
		try {
			result = new Result();
			//TODO 1创建线程向硬件发送请求2页面显示正在执行提示（无法进行其他操作）3返回执行结果
			//发送信道
			//接收信道
			//发送功率
			Setting setting = new Setting();
			//FIXME 
			setting.setName("第二组");
			setting.setPower(4);
			result.setItem(setting);
			result.setRet(Constant.SUCCESS);
			result.setMessage("读取成功！");
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("读取失败!");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}
	@Override
	public Result set(Object object) {
		try {
			result = new Result();
			Setting setting = StringUtils.parseJSON(object, Setting.class);
			String name = setting.getName();
			//发送信道
			Integer tx_ch = SettingEnum.getRxchByName(name);
			//接收信道
			Integer rx_ch = SettingEnum.getRxchByName(name);
			//发送功率
			Integer power = setting.getPower();
			//TODO 创建线程向硬件发送请求
			
			result.setRet(Constant.SUCCESS);
			result.setMessage("设置成功！");
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("设置失败!");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result setDefault() {
		try {
			result = new Result();
			//默认发送信道
			Integer tx_ch =Integer.parseInt(ConfigConstant.projectConf.getTx_ch());
			//默认接收信道
			Integer rx_ch =Integer.parseInt(ConfigConstant.projectConf.getRx_ch());
			//默认发送功率
			Integer power = Integer.parseInt(ConfigConstant.projectConf.getPower());
			//TODO 创建线程向硬件发送请求
			
			result.setRet(Constant.SUCCESS);
			result.setMessage("设置成功！");
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("设置失败!");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}
	

}
