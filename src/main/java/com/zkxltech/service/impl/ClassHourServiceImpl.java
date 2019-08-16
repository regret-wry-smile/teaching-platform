package com.zkxltech.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.cache.RedisMapScore;
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

import net.sf.json.JSONArray;

public class ClassHourServiceImpl implements ClassHourService{
	private static final Logger logger = LoggerFactory.getLogger(ClassHourServiceImpl.class);
	private static Result result;
	private ClassHourSql classHourSql = new ClassHourSql();

	@Override
	public Result selectClassInfo(Object classId, Object subjectName) {
		result = new Result();
		try {
			result = classHourSql.selectClassHour((String)classId, (String)subjectName);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("Successfully querying the course list!");
			}else {
				result.setMessage("Failed to query the course list！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("Failed to query the course list！");
			result.setDetail(IOUtils.getError(e));
			logger.error(IOUtils.getError(e));
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
				result.setMessage("Add courses successful!");
			}else {
				result.setMessage("Add courses failed！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("Add courses failed！");
			result.setDetail(IOUtils.getError(e));
			logger.error(IOUtils.getError(e));
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
				result.setMessage("Delete course successful!");
			}else {
				result.setMessage("Delete course failed！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("Delete course failed！");
			result.setDetail(IOUtils.getError(e));
			logger.error(IOUtils.getError(e));
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
				result.setMessage("Delete course successful!");
			}else {
				result.setMessage("Delete course failed！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("Delete course failed！");
			result.setDetail(IOUtils.getError(e));
			logger.error(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result startClass(Object classHourObj) {
		try {
			
			ClassHour classHour = (ClassHour) StringUtils.parseJSON(classHourObj, ClassHour.class);
			
			Global.setClassId(classHour.getClassId());
			
			Global.setClassHour(classHour);
			
			result = refreshGload();
			
			if (Constant.ERROR.equals(result.getRet())) {
				return result;
			}
			
			result.setRet(Constant.SUCCESS);
			result.setMessage("Class begin！");
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("Class failed！");
			logger.error(IOUtils.getError(e));
		}
		return result;
	}
	
	/**
	 * 更新当前学生信息和班级信息
	 * @param classId
	 * @return
	 */
	public static Result refreshGload(){
		result = new Result();
		result.setRet(Constant.SUCCESS);
		if (!StringUtils.isEmpty(Global.classId)) {
			ClassInfo classInfo = new ClassInfo();
			classInfo.setClassId(Global.classId);
			result = new ClassInfoServiceImpl().selectClassInfo(classInfo);
			if (Constant.SUCCESS.equals(result.getRet())) {
				List<ClassInfo>  classInfos = (List<ClassInfo>) result.getItem();
				if (classInfos != null && classInfos.size()>0) {
					Global.setClassInfo(classInfos.get(0));
				}else {
					result.setMessage("Class information not found！");
					result.setRet(Constant.ERROR);
					return result;
				}
			}else {
				result.setMessage("Failed to query class information！");
				result.setRet(Constant.ERROR);
				return result;
			}
			
			StudentInfo studentInfo = new StudentInfo();
			studentInfo.setClassId(Global.classId);
			result = new StudentInfoServiceImpl().selectStudentInfo(studentInfo);
			if (Constant.SUCCESS.equals(result.getRet())) {
				if (StringUtils.isEmptyList(result.getItem())) {
					result.setMessage("No students in the class！");
					result.setRet(Constant.ERROR);
					return result;
				}
				List<StudentInfo> studentInfos = (List<StudentInfo>)result.getItem();
				List<StudentInfo> bindStudentInfos = new ArrayList<StudentInfo>();
//				List<StudentInfo> allStudentInfos = new ArrayList<StudentInfo>();
				for (int i=0;i<studentInfos.size();i++){
					StudentInfo stu = studentInfos.get(i);
					if("1".equals(stu.getStatus())){
						bindStudentInfos.add(stu);
					}
//					if ("************".equals(stu.getIclickerId()) || StringUtils.isEmpty(stu.getIclickerId())){
//						stu.setIclickerId("*********"+i);
//					}
//					allStudentInfos.add(stu);
				}
				Global.setStudentInfos(bindStudentInfos);
			}else {
				result.setMessage("Failed to query student information！");
				result.setRet(Constant.ERROR);
				return result;
			}
		}
		logger.info("当前班级学生信息>>>"+JSONArray.fromObject(Global.studentInfos));
		return result;
	}

	public static boolean isStartClass() {
		return !StringUtils.isEmpty(Global.classId);
	}

	@Override
	public Result endClass() {
		result = new Result();
		result.setRet(Constant.SUCCESS);
		try {
			Global.setClassHour(null);
			Global.setClassId(null);
			Global.setClassInfo(null);
			Global.setStudentInfos(null);
			
			result.setRet(Constant.SUCCESS);
			result.setMessage("Class end！");
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("Class failed！");
			logger.error(IOUtils.getError(e));
		}
		return result;
	}

	@Override
	public Result getClassInfo() {
		result = new Result();
		result.setRet(Constant.SUCCESS);
		try {
			result.setItem(Global.getClassInfo());
			result.setRet(Constant.SUCCESS);
			result.setMessage("Get the current class information successfully！");
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("Get the current class information failed！");
			logger.error(IOUtils.getError(e));
		}
		return result;
	}

}
