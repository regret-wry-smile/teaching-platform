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
	 * @param classId 班级id
	 * @param codeId	考试项目ID号
	 * @param subjectName 科目名称
	 * @return
	 */
	Result getQuestionInfoFromServer(String classId, String codeId, String subjectName);
}
