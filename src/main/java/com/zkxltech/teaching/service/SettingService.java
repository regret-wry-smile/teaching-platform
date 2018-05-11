package com.zkxltech.teaching.service;

import com.zkxltech.domain.Result;

/**
 * 【设置】
 */
public interface SettingService {
	/**
	 * 获取所有组名
	 */
	Result getAllName();
	
	/**
	 * 登录
	 * @param loginId 登录账号
	 * @param password 密码
	 */
	Result login(Object object); 
	/**
	 * 读取设置
	 */
	Result readSetting();
	/**
	 * 设置
	 * @param name 组名
	 * @param power 功率
	 */
	Result set(Object object);
	
	/**
	 * 设置默认值
	 * @param name 组名
	 * @param power 功率
	 */
	Result setDefault();
}
