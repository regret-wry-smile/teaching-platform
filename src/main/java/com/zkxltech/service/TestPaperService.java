package com.zkxltech.service;

import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;

public interface TestPaperService {
	/**
	 * 新增试卷
	 * @param testInfo 试卷信息
	 * @return
	 */
	Result insertTestPaper(Object testInfo);
	/**
	 * 导入试卷
	 * @param fileName 文件路径
	 * @return
	 */
	Result importTestPaper(Object fileName);
	
	/**
	 * 服务器导入试卷
	 * @param fileName 文件路径
	 * @return
	 */
//	Result importTestPaper(Object fileName);
	/**
	 * 查询试卷
	 * @param testInfo
	 * @return
	 */
	Result selectTestPaper(Object testInfo);
	
	/**
	 * 查询当前科目的试卷
	 * @param testInfo
	 * @return
	 */
	Result selectNowTestPaper(Object testInfo);
	
	/**
	 * 删除试卷
	 * @param testInfo
	 * @return
	 */
	Result deleteTestPaper(Object testInfo);
	/**
	 * 编辑试卷
	 * @param testInfo
	 * @return
	 */
	Result updateTestPaper(Object testInfo);
	/**
	 * 关联查询,查询作答记录的试卷信息
	 * @param object
	 * @return
	 */
    Result selectTestPaperByClassHourId(Object object);
}
