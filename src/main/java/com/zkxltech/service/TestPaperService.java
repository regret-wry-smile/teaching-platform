package com.zkxltech.service;

import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;

public interface TestPaperService {
	/**
	 * 新增试卷
	 * @param object
	 * @return
	 */
	Result insertTestPaper(Object object);
	/**
	 * 导入试卷
	 * @param object 文件路径
	 * @return
	 */
	Result importTestPaper(Object object);
	/**
	 * 查询试卷
	 * @param object
	 * @return
	 */
	Result selectTestPaper(Object object);
	/**
	 * 删除试卷
	 * @param object
	 * @return
	 */
	Result deleteTestPaper(Object object);
	/**
	 * 编辑试卷
	 * @param object
	 * @return
	 */
	Result updateTestPaper(Object object);
}
