package com.zkxltech.service;

import com.zkxltech.domain.Result;

public interface AnswerInfoService {
	/**
	 * 开始多选答题
	 * @param object 答题范围
	 * @return
	 */
	Result startMultipleAnswer(Object object);
	
	/**
	 * 开始客观答题
	 * @param answerInfos
	 * @return
	 */
	Result startObjectiveAnswer(Object testId);
	
	/**
	 * 停止客观题答题
	 */
	Result stopObjectiveAnswer();

    Result singleAnswer(Object param);

    Result stopSingleAnswer();
}
