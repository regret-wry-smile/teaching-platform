package com.zkxltech.service;

import java.util.List;

import com.zkxltech.domain.AnswerInfo;
import com.zkxltech.domain.Result;

public interface AnswerInfoService {
	/**
	 * 开始多选答题
	 * @param object 答题范围
	 * @return
	 */
	Result startMultipleAnswer(Object object);
	/**
	 * 批量插入答题信息
	 * @param answerInfos
	 * @return
	 */
	Result insertAnswerInfos(List<AnswerInfo> answerInfos);
	/**
	 * 删除答题信息
	 * @param answerInfos
	 * @return
	 */
	Result deleteAnswerInfo(AnswerInfo answerInfo);
	/**
	 * 查询答题信息
	 * @param answerInfos
	 * @return
	 */
	Result selectAnswerInfo(AnswerInfo answerInfo);
}
