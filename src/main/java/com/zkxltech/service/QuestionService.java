package com.zkxltech.service;

import com.zkxltech.domain.Result;

public interface QuestionService {
	/*题目导入*/
	Result importQuestion(Object object);
	/*新增题目*/
	Result insertQuestion(Object object);
	/*查询题目*/
	Result selectQuestion(Object object);
	/**
	 * 删除题目
	 * @param object 主键id集合
	 * @return
	 */
	Result deleteQuestionByIds(Object object);
	/*修改题目*/
	Result updateQuestion(Object object);
}
