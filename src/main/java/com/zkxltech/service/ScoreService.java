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
	/**
	 * 获取柱状图数据
	 * @return
	 */
	Result getScore();
	
	/**
	 * 获取评分主题信息
	 * @return
	 */
	Result getScoreTitleInfo();
    Result stopScore();
    Result scoreStart(int size);
}
