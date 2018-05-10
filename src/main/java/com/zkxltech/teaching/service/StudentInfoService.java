package com.zkxltech.teaching.service;

import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;

public interface StudentInfoService {
	/*学生名单导入*/
	Result importStudentInfo(String fileName);
	/*保存学生*/
	Result insertStudentInfo(StudentInfo studentInfo);
	/*查询学生信息*/
	Result selectStudentInfo(StudentInfo studentInfo);
	/*删除学生*/
	Result deleteStudentInfo(StudentInfo studentInfo);
	/*修改学生*/
	Result updateStudentInfo(StudentInfo studentInfo);
}
