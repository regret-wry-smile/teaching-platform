package com.zkxltech.service;

import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;

public interface StudentInfoService {
	/**
	 * 学生名单导入
	 * @param object excel文件路径
	 * @return
	 */
	Result importStudentInfo(Object object);
	/**
	 * 服务器导入
	 * @param object 班级名称
	 * @return
	 */
	Result importStudentInfoByServer(Object object);
	/**
	 * 新增学生
	 * @param object StudentInfo对象
	 * @return
	 */
	Result insertStudentInfo(Object object);
	/**
	 * 查询学生信息
	 * @param object StudentInfo对象
	 * @return
	 */
	Result selectStudentInfo(Object object);
	/**
	 * 
	 * @param object 主键集合
	 * @return
	 */
	Result deleteStudentById(Object object);
	/**
	 * 修改学生
	 * @param object StudentInfo对象
	 * @return
	 */
	Result updateStudentById(Object object);
}
