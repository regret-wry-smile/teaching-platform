package com.zkxltech.teaching.service;

import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;

public interface TestPaperService {
	/*新增试卷*/
	Result insertTestPaper(Object object);
	/*查询试卷*/
	Result selectTestPaper(Object object);
	/*删除试卷*/
	Result deleteTestPaper(Object object);
	/*修改试卷*/
	Result updateTestPaper(Object object);
}
