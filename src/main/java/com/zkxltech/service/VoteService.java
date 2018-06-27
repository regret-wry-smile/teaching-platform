package com.zkxltech.service;

import java.util.List;

import com.zkxltech.domain.AnswerInfo;
import com.zkxltech.domain.Result;

/**
 * 投票
 */
public interface VoteService {
	/**
	 * 开始投票
	 * @param object scored对象
	 * @return
	 */
	Result startVote(Object object);
	/**
	 * 获取柱状图数据
	 * @return
	 */
	Result getVote();
	/**
	 * 获取投票主题
	 * @return
	 */
	Result getVoteTitleInfo();
}
