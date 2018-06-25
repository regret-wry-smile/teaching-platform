package com.zkxltech.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.cache.BrowserManager;
import com.ejet.core.util.ICallBack;
import com.ejet.core.util.StringUtils;
import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;
import com.ejet.core.util.io.ImportExcelUtils;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.service.StudentInfoService;
import com.zkxltech.sql.StudentInfoSql;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class StudentInfoServiceImpl implements StudentInfoService{
	private static final Logger log = LoggerFactory.getLogger(StudentInfoServiceImpl.class);
	private Result result;
	private StudentInfoSql studentInfoSql = new StudentInfoSql();
	
	@Override
	public Result importStudentInfo2(Object object,ICallBack icallback) {
		result = new Result();
		try {
			String fileName = String.valueOf(object);
			studentInfoSql.deleteStudent(new StudentInfo());
			List<List<Object>> list = ImportExcelUtils.getBankListByExcel(new FileInputStream(new File(fileName)), fileName);
			result = studentInfoSql.importStudent(list);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("导入成功!");
//				BrowserManager.refreshClass();
//				BrowserManager.selectClass(classId);
//				BrowserManager.refreshStudent(classId);
			}else {
//				icallback.onResult(-1, "导入学生失败", "");
				result.setMessage("导入学生失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("导入学生失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}finally {
			icallback.onResult(StringUtils.StringToInt(result.getRet()), result.getMessage(),  result.getRemak());
		}
	}
	
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
	public Result importStudentInfoByServer(Object object) {
		result = new Result();
		try {
			String classId = (String) object;
			result = copeServerStudentData(classId);
			if(Constant.SUCCESS.equals(result.getRet())){
//				BrowserManager.refreshStudent(className);
//				BrowserManager.showMessage(true,"从服务器中获取学生信息成功！");
//				getBindInfoAndRefresh(className);
			}else{
//				BrowserManager.showMessage(false,"从服务器中获取学生信息失败！");
			}
		} catch (Exception e) {
//			BrowserManager.showMessage(false,"从服务器中获取学生信息失败！");
		}finally {
//			BrowserManager.removeLoading();
		}
		return result;
	}
	@Override
	public Result selectStudentInfo(Object param) {
		result = new Result();
		try {
			StudentInfo studentInfo =  (StudentInfo) StringUtils.parseToBean(JSONObject.fromObject(param), StudentInfo.class);
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
			
			StudentInfo studentInfo =  (StudentInfo) StringUtils.parseToBean(JSONObject.fromObject(param), StudentInfo.class);
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
	public Result deleteStudentById(Object param) {
		try {
			JSONArray jsonArray = JSONArray.fromObject(param);
			List<Integer> ids = new ArrayList<Integer>();
			for (int i = 0; i < jsonArray.size(); i++) {
				ids.add(jsonArray.getInt(i));
			}
			result = studentInfoSql.deleteStudentById(ids);
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
	public Result updateStudentById(Object param) {
		try {
			StudentInfo studentInfo =  (StudentInfo) StringUtils.parseToBean(JSONObject.fromObject(param), StudentInfo.class);
			result = studentInfoSql.updateStudentById(studentInfo);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("修改学生信息成功!");
				BrowserManager.refreshBindCard();
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
	
	/**
	 * 服务器中获取学生
	 */
	public Result copeServerStudentData(String classId) {
		Result result = new Result();
		try {
			result = studentInfoSql.getServerStudent(classId);//获取服务器学生
			if ("ERROR".equals(result.getRet())) {
				return result;
			}
			List<Map<String, Object>> listMaps = (List<Map<String, Object>>) result.getItem();
			StudentInfo studentInfo = new StudentInfo();
			studentInfo.setClassId(classId);
			studentInfoSql.deleteStudent(studentInfo); //清除本地该班学生信息
			result = studentInfoSql.saveStudentByGroup(listMaps); //保存服务器上该班的学生信息
			result.setMessage("服务器中获取学生成功！");
			return result;
		} catch (Exception e) {
			log.error(IOUtils.getError(e));
			result.setRet(Constant.ERROR);
			result.setDetail(IOUtils.getError(e));
			result.setMessage("服务器中获取学生失败！");
			return result;
		}
	}
}
