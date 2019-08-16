package com.zkxltech.service;

import com.ejet.core.util.ICallBack;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;

public interface StudentInfoService {
	/**
	 * 学生名单导入(异步)
	 * @param object excel文件路径
	 * @return
	 */
	Result importStudentInfo2(Object fileNameObj,Object classInfoObj,ICallBack icallback);
	/**
	 * 学生名单导入(同步)
	 * @param fileNameObj excel文件路径
	 * @param classInfoObj 班级信息
	 * @return 
	 */
	Result importStudentInfo(Object fileNameObj,Object classInfoObj);
	
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
	/**
	 * 解绑学生
	 * @param object 主键id数组
	 * @return
	 */
	Result clearStudentByIds(Object object);
    //Result singleAnswer(Object param);
    Result signInStart(Object param);
    Result signInStop();
    Result quickAnswer(Object param);
    Result stopQuickAnswer();

	Result updateStudent(StudentInfo studentInfo);
}
