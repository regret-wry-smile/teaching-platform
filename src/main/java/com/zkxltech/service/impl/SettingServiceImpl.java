package com.zkxltech.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.config.ConfigConstant;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.Setting;
import com.zkxltech.domain.User;
import com.zkxltech.service.SettingService;
import com.zkxltech.ui.enums.SettingEnum;
import com.zkxltech.ui.util.StringUtils;

import net.sf.json.JSONObject;

public class SettingServiceImpl implements SettingService{
    private static final Logger log = LoggerFactory.getLogger(SettingServiceImpl.class);
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
			result.setMessage("Wrong account password！");
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("System Error!");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}
	@Override
	public Result readSetting() {
		try {
			result = new Result();
			//TODO 1创建线程向硬件发送请求2页面显示正在执行提示（无法进行其他操作）3返回执行结果
			//getNameByTxchAndRxch
			//发送信道
			//接收信道
			//发送功率
			Result get_device_info = EquipmentServiceImpl.getInstance().get_device_info();
//			Result get_device_info = EquipmentServiceImpl2.getInstance().get_device_info();
			Object item = get_device_info.getItem();
			if (StringUtils.isEmpty(item)) {
                result.setRet(Constant.ERROR);
                result.setMessage("Equipment malfunction, please restart");
                log.error("获取设备信息指令发送失败");
                return result;
            }
			JSONObject jo = JSONObject.fromObject(item);
			String tx_ch = jo.getString("tx_ch");
			String rx_ch = jo.getString("rx_ch");
			String tx_power = jo.getString("tx_power");
			Setting setting = new Setting();
			String name = SettingEnum.getNameByTxchAndRxch(Integer.parseInt(tx_ch), Integer.parseInt(rx_ch));
			setting.setName(name);
			setting.setPower(Integer.parseInt(tx_power));
			//FIXME 
//			setting.setName("第二组");
//			setting.setPower(4);
			result.setItem(setting);
			result.setRet(Constant.SUCCESS);
			result.setMessage("Read Success！");
			log.info("读取成功");
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("Read Failed!");
			result.setDetail(IOUtils.getError(e));
			log.error("读取异常", e);
			return result;
		}
	}
	@Override
	public Result set(Object object) {
		try {
			result = new Result();
			result.setRet(Constant.ERROR);
			Setting setting = StringUtils.parseJSON(object, Setting.class);
			String name = setting.getName();
			if (StringUtils.isEmpty(name)) {
                result.setMessage("Missing parameter, system channel name is empty");
                return result;
            }
			Integer tx_power = setting.getPower();
			if (tx_power == null) {
                result.setMessage("Missing parameter, sending power is empty");
                return result;
            }
			//发送信道
			Integer tx_ch = SettingEnum.getTxchByName(name);
			//接收信道
			Integer rx_ch = SettingEnum.getRxchByName(name);
			//发送功率
			if (tx_ch == null || rx_ch == null) {
                result.setMessage("By channel name: "+name+" ,The corresponding send and receive channel values were not queried");
                return result;
            }
			
			result = EquipmentServiceImpl.getInstance().set_channel(tx_ch, rx_ch);
			if (result.getRet() == Constant.ERROR) {
			    result.setMessage("Channel setting failed");
                return result;
            }
			result = EquipmentServiceImpl.getInstance().set_tx_power(tx_power);
			if (result.getRet() == Constant.ERROR) {
			    result.setMessage("Setting power failed");
			    log.error("设置功率失败");
			    return result;
            }
			result.setRet(Constant.SUCCESS);
			result.setMessage("Setting Success！");
			return result;
		} catch (Exception e) {
			result.setMessage("setting failed!");
			result.setDetail(IOUtils.getError(e));
			log.error("Channel and Power setting failed", e);
			return result;
		}
	}

	@Override
	public Result setDefault() {
		try {
			result = new Result();
			result.setRet(Constant.ERROR);
			//默认发送信道
			Integer tx_ch =Integer.parseInt(ConfigConstant.projectConf.getTx_ch());
			//默认接收信道
			Integer rx_ch =Integer.parseInt(ConfigConstant.projectConf.getRx_ch());
			//默认发送功率
			Integer tx_power = Integer.parseInt(ConfigConstant.projectConf.getPower());
			//TODO 创建线程向硬件发送请求
			if (tx_ch == null || rx_ch == null) {
                result.setMessage("Read configuration file \"system channel \"default Settings Failed");
                return result;
            }
			if (tx_power == null) {
			    result.setMessage("Read configuration file \"Answering machine sends power \"default Settings Failed");
                return result;
            }
			result=EquipmentServiceImpl.getInstance().set_channel(tx_ch, rx_ch);
			if (Constant.ERROR == result.getRet()) {
                result.setMessage("System channel setup failed. Please try again or restart the device");
                return result;
            }
			result = EquipmentServiceImpl.getInstance().set_tx_power(tx_power);
			if (Constant.ERROR == result.getRet()) {
                result.setMessage("Failed to set up the sending power of the questioner. Please try again or restart the device");
                return result;
            }
			result.setRet(Constant.SUCCESS);
			result.setMessage("Setting Success！");
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("Default setting failed!");
			result.setDetail(IOUtils.getError(e));
			log.error("设置默认值失败!",e);
			return result;
		}
	}
	

}
