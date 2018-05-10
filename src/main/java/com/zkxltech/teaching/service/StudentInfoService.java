package com.zkxltech.teaching.service;

import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;

public interface StudentInfoService {
	/*学生名单导入*/
	Result importStudentInfo(Object object);
	/*保存学生*/
	Result insertStudentInfo(Object object);
	/*查询学生信息*/
	Result selectStudentInfo(Object object);
	/*删除学生*/
	Result deleteStudentInfo(Object object);
	/*修改学生*/
	Result updateStudentInfo(Object object);
}
