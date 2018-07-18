package com.ejet.core.util;

import com.ejet.core.util.constant.Constant;
import com.zkxltech.domain.Result;

import net.sf.json.JSONObject;

public class EquipmentUtils {
	public static Result parseResult(String str){
		Result result = new Result();
		try {
			JSONObject jsonObject = JSONObject.fromObject(str);
			String retCode = jsonObject.getString("result");
			if (!"0".equals(retCode)) {
				result.setRet(Constant.ERROR);
				result.setMessage("指令发送失败");
				return result;
			}
			result.setRet(Constant.SUCCESS);
			result.setMessage("指令发送成功");
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("指令发送失败");
			return result;
		}
	}
}
