package com.zkxltech.teaching.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;
import com.ejet.core.util.io.ImportExcelUtils;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.teaching.service.StudentInfoService;
import com.zkxltech.teaching.sql.StudentInfoSql;

import net.sf.json.JSONObject;

public class StudentInfoServiceImpl implements StudentInfoService{
	private Result result;
	private StudentInfoSql studentInfoSql = new StudentInfoSql();
	
	@Override
	public Result importStudentInfo(Object object) {
		result = new Result();
		try {
			String fileName = String.valueOf(object);
			studentInfoSql.deleteStudent(new StudentInfo());
			List<List<Object>> list = ImportExcelUtils.getBankListByExcel(new FileInputStream(new File(fileName)), fileName);
			result = studentInfoSql.importStudent(list);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("导入成功!");
			}else {
				Thread.sleep(1000);
				result.setMessage("导入学生失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("导入学生失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result selectStudentInfo(Object param) {
		result = new Result();
		try {
			StudentInfo studentInfo =  (StudentInfo) JSONObject.toBean(JSONObject.fromObject(param), StudentInfo.class);
			result = studentInfoSql.selectStudentInfo(studentInfo);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("查询学生信息成功!");
			}else {
				result.setMessage("查询学生信息失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("查询学生信息失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result insertStudentInfo(Object param) {
		try {
			StudentInfo studentInfo =  (StudentInfo) JSONObject.toBean(JSONObject.fromObject(param), StudentInfo.class);
			result = studentInfoSql.insertStudentInfo(studentInfo);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("新增学生信息成功!");
			}else {
				result.setMessage("新增学生信息失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("新增学生信息失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result deleteStudentInfo(Object param) {
		try {
			StudentInfo studentInfo =  (StudentInfo) JSONObject.toBean(JSONObject.fromObject(param), StudentInfo.class);
			result = studentInfoSql.deleteStudent(studentInfo);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("删除学生信息成功!");
			}else {
				result.setMessage("删除学生信息失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("删除学生信息失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result updateStudentInfo(Object param) {
		try {
			StudentInfo studentInfo =  (StudentInfo) JSONObject.toBean(JSONObject.fromObject(param), StudentInfo.class);
			result = studentInfoSql.updateStudent(studentInfo);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("修改学生信息成功!");
			}else {
				result.setMessage("修改学生信息失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("修改学生信息失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

}
