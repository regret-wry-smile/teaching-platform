package com.zkxltech.service.impl;

import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.domain.ClassInfo;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.domain.TestPaper;
import com.zkxltech.service.ClassInfoService;
import com.zkxltech.sql.ClassInfoSql;
import com.zkxltech.sql.StudentInfoSql;
import com.zkxltech.ui.util.StringUtils;

import net.sf.json.JSONObject;

public class ClassInfoServiceImpl implements ClassInfoService{
	private Result result;
	private ClassInfoSql classInfoSql = new ClassInfoSql();
	private StudentInfoSql studentInfoSql = new StudentInfoSql();
	
	@Override
	public Result insertClassInfo(Object object) {
		result = new Result();
		try {
			ClassInfo classInfo =  (ClassInfo) StringUtils.parseJSON(object, ClassInfo.class);
			result = classInfoSql.insertClassInfo(classInfo);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("新增班级成功!");
			}else {
				result.setMessage("新增班级失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("新增班级失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result selectClassInfo(Object object) {
		result = new Result();
		try {
			ClassInfo classInfo =  (ClassInfo) StringUtils.parseJSON(object, ClassInfo.class);
			result = classInfoSql.selectClassInfo(classInfo);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("查询班级成功!");
			}else {
				result.setMessage("查询班级失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("查询班级失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result deleteClassInfo(Object object) {
		result = new Result();
		try {
			ClassInfo classInfo =  (ClassInfo) StringUtils.parseJSON(object, ClassInfo.class);
			result = classInfoSql.deleteClassInfo(classInfo); //删除班级
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("删除班级成功!");
			}else {
				result.setMessage("删除班级失败！");
				return result;
			}
			StudentInfo studentInfo = new StudentInfo();
			studentInfo.setClassId(classInfo.getClassId());  //删除学生
			studentInfoSql.deleteStudent(studentInfo);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("删除班级成功!");
			}else {
				result.setMessage("删除班级失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("删除班级失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result updateClassInfo(Object object) {
		result = new Result();
		try {
			ClassInfo classInfo =  (ClassInfo) StringUtils.parseJSON(object, ClassInfo.class);
			result = classInfoSql.updateClassInfo(classInfo);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("修改班级成功!");
			}else {
				result.setMessage("修改班级失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("修改班级失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

}
