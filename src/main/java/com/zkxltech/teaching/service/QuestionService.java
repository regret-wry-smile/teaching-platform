package com.zkxltech.teaching.service;

import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;

public interface QuestionService {
	/*题目导入*/
	Result importQuestion(Object object);
	/*新增题目*/
	Result insertQuestion(Object object);
	/*查询题目*/
	Result selectQuestion(Object object);
	/*删除题目*/
	Result deleteQuestion(Object object);
	/*修改题目*/
	Result updateQuestion(Object object);
}
