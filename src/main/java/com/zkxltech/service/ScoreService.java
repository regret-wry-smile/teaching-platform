package com.zkxltech.service;

import java.util.List;

import com.zkxltech.domain.AnswerInfo;
import com.zkxltech.domain.Result;

/**
 * 评分
 */
public interface ScoreService {
	/**
	 * 开始评分
	 * @param object scored对象
	 * @return
	 */
	Result startScore(Object object);
	
}
