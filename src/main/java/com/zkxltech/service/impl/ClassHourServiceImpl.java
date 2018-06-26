package com.zkxltech.service.impl;

import java.util.Date;
import java.util.List;

import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.constant.Global;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.domain.ClassHour;
import com.zkxltech.domain.ClassInfo;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.domain.TestPaper;
import com.zkxltech.service.ClassHourService;
import com.zkxltech.sql.ClassHourSql;
import com.zkxltech.ui.util.StringUtils;

public class ClassHourServiceImpl implements ClassHourService{
	private Result result;
	private ClassHourSql classHourSql = new ClassHourSql();

	@Override
	public Result selectClassInfo(Object classId, Object subjectName) {
		result = new Result();
		try {
			result = classHourSql.selectClassHour((String)classId, (String)subjectName);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("查询课程列表成功!");
			}else {
				result.setMessage("查询课程列表失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("查询课程列表失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result insertClassInfo(Object object) {
		result = new Result();
		try {
			ClassHour classHour =  (ClassHour) StringUtils.parseJSON(object, ClassHour.class);
			classHour.setClassHourId(com.ejet.core.util.StringUtils.getUUID());
			classHour.setStartTime(com.ejet.core.util.StringUtils.formatDateTime(new Date()));
			result = classHourSql.insertClassHour(classHour);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("新增课程成功!");
			}else {
				result.setMessage("新增课程失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("新增课程失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result deleteClassInfo(Object object) {
		result = new Result();
		try {
			ClassHour classHour =  (ClassHour) StringUtils.parseJSON(object, ClassHour.class);
			result = classHourSql.deleteAnswerInfo(classHour);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("删除课程成功!");
			}else {
				result.setMessage("删除课程失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("删除课程失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result updateClassInfoTime(Object object) {
		result = new Result();
		try {
			ClassHour classHour =  (ClassHour) StringUtils.parseJSON(object, ClassHour.class);
			classHour.setEndTime(com.ejet.core.util.StringUtils.formatDateTime(new Date()));
			result = classHourSql.updateTestPaper(classHour);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("删除课程成功!");
			}else {
				result.setMessage("删除课程失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("删除课程失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result startClass(Object classId, Object classHour) {
		ClassInfo classInfo = new ClassInfo();
		classInfo.setClassId((String)classId);
		Result result = new ClassInfoServiceImpl().selectClassInfo(classInfo);
		if (Constant.SUCCESS.equals(result.getRet())) {
			List<ClassInfo>  classInfos = (List<ClassInfo>) result.getItem();
			if (classInfos != null && classInfos.size()>0) {
				Global.setClassInfo(classInfos.get(0));
			}else {
				result.setMessage("未找到班级信息！");
				return result;
			}
		}else {
			result.setMessage("查询班级信息失败！");
			return result;
		}
		
		StudentInfo studentInfo = new StudentInfo();
		studentInfo.setClassId((String)classId);
		result = new StudentInfoServiceImpl().selectStudentInfo(studentInfo);
		if (Constant.SUCCESS.equals(result.getRet())) {
			Global.setStudentInfos((List<StudentInfo>)result.getItem());
		}else {
			result.setMessage("查询学生信息失败！");
			return result;
		}
		
		Global.setClassHour((ClassHour) StringUtils.parseJSON(classHour, ClassHour.class));
		
		return null;
	}

}
