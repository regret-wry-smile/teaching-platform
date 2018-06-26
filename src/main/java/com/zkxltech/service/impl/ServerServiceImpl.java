package com.zkxltech.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ejet.core.util.OkHttpUtils;
import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.config.ConfigConstant;
import com.zkxltech.domain.Result;
import com.zkxltech.service.ServerService;

public class ServerServiceImpl implements ServerService{
	
	 private String urlString  = ConfigConstant.serverDbConfig.getServer_url();

	@Override
	public Result getTestInfoFromServer(String classId,String subjectName) {
		Result result = new Result();
		try {
			List<JSONObject> testList = new ArrayList<JSONObject>();//保存所有的试卷信息
			//获取服务器上的试卷信息
			StringBuilder params1 = new StringBuilder();
			params1.append("Code=1006&V={\"bjID\":\""+classId+"\",\"SubName\":\""+subjectName+"\"}");
			//[{"id":6845,"xmid":"2Y0002","xm":"test"}]
			String testInfo =  OkHttpUtils.postData(urlString, params1.toString());
			if ("0".equals(testInfo)) {
				result.setRet(Constant.ERROR);
				result.setMessage("从服务器中获取试卷失败！");
				return result;
			}else {
				JSONArray jsonArray = JSONArray.parseArray(testInfo);
				String[] items = new String[jsonArray.size()];
				for (int j = 0; j < jsonArray.size(); j++) {
					JSONObject jsonObject = (JSONObject) jsonArray.get(j);
					items[j] = jsonObject.getString("xmid");
					testList.add(jsonObject);
				}
			}
			result.setRet(Constant.SUCCESS);
			result.setMessage("从服务器中获取试卷！");
			result.setItem(testList);
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("从服务器中获取试卷失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
		
		
	}
	
//	public static void main(String[] args) {
//		Result result = new ServerServiceImpl().getTestInfoFromServer("705","语文");
//		System.out.println(JSONObject.toJSONString(result));
//	}

}
