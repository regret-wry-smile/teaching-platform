package com.zkxltech.service;

import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;

public interface ClassHourService {
	/**
	 * 查询课时列表
	 * @param object
	 * @return
	 */
	Result selectClassInfo(Object classId, Object subjectName);
	/**
	 * 新增课时
	 * @param object classhour对象
	 * @return
	 */
	Result insertClassInfo(Object object);
}
