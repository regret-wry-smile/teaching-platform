package com.zkxltech.teaching.service;

import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;

public interface ClassInfoService {
	/*新增班级*/
	Result insertClassInfo(Object object);
	/*查询班级*/
	Result selectClassInfo(Object object);
	/*删除班级*/
	Result deleteClassInfo(Object object);
	/*修改班级*/
	Result updateClassInfo(Object object);
}
