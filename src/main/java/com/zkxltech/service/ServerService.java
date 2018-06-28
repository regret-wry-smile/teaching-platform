package com.zkxltech.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.zkxltech.domain.Result;

/**
 * 服务器调用接口
 * @author zkxl
 *
 */
public interface ServerService {
	/**
	 * 获取服务器上的试卷信息
	 */
	Result getTestInfoFromServer(String classId,String subjectName);
	/**
	 * 获取标准答案
	 * @param ResponseTestPaper 对象
	 * @return
	 */
	Result getQuestionInfoFromServer(Object object);
	
	/**
	 * 上传服务器
	 */
	Result uploadServer(Object testId,Object subject,Object classId);
}
