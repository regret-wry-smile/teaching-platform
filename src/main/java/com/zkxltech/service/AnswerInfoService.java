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
	 * @param testId 试卷id
	 * @return
	 */
	Result startObjectiveAnswer(Object testId);
	
	/**
	 * 开始记分答题
	 */
	Result startSubjectiveAnswer(Object testId);
	
	
	/**
	 * 停止客观题答题
	 */
	Result stopObjectiveAnswer();
	
	/**
	 * 停止主观题答题
	 */
	Result stopSubjectiveAnswer();
	
//	/**
//	 * 开始记分答题
//	 */
//	Result stopSubjectiveAnswer();

    Result singleAnswer(Object param);

    Result stopSingleAnswer();
    
    Result getEverybodyAnswerInfo();
    
    Result stopMultipleAnswer();
}
