package com.zkxltech.service;

import com.zkxltech.domain.Result;

public interface QuestionService {
	/*题目导入*/
	Result importQuestion(Object object);
	/*新增题目*/
	Result insertQuestion(Object object);
//	/**
//	 * 从缓存中查询所有题目信息
//	 * @return
//	 */
//	Result selectQuestionedis(Object object);
	/**
	 * 从数据库中查询题目信息
	 * @param object question对象
	 * @return
	 */
	Result selectQuestion(Object object);
	/**
	 * 删除题目
	 * @param object 主键id集合
	 * @return
	 */
	Result deleteQuestionByIds(Object object);
//	/**
//	 * 删除缓存中题目
//	 */
//	Result deleteQuestionRedis(Object object);
	/**
	 * 修改题目
	 * @param object
	 * @return
	 */
	Result updateQuestion(Object object);
}
