package com.zkxltech.teaching.server;

import org.junit.Before;
import org.junit.Test;

import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.teaching.service.StudentInfoService;
import com.zkxltech.teaching.service.impl.StudentInfoServiceImpl;

import net.sf.json.JSONObject;

public class StudentInfoServiceTest {
	private StudentInfoService studentInfoService;
	
	@Before
	public void innit() {
		studentInfoService = new StudentInfoServiceImpl();
	}
	/*学生名单导入*/
	@Test
	public void testImportStudent(){
		String fileName = "D:/Users/zkxl/Desktop/excel表/双师答题器模板/双师学生导入模板.xlsx";
		Result result = studentInfoService.importStudentInfo(fileName);
		System.out.println(JSONObject.fromObject(result));
	}
	
	/*查询学生*/
	@Test
	public void testSelectStudentInfo(){
		StudentInfo studentInfo = new StudentInfo();
		studentInfo.setClassId("1160");
		studentInfo.setStudentId("100000010");
		studentInfo.setId(102);
		Result result = studentInfoService.selectStudentInfo(studentInfo);
		System.out.println(JSONObject.fromObject(result));
	}
	
	/*新增学生*/
	@Test
	public void testInsertStudentInfo(){
		StudentInfo studentInfo = new StudentInfo();
		studentInfo.setClassId("1160");
		studentInfo.setClassName("601班");
		studentInfo.setStudentId("999999999");
		studentInfo.setId(99999);
		studentInfo.setStudentName("张三");
		studentInfo.setStatus("0");
		studentInfo.setIclickerId("99999999");
		Result result = studentInfoService.insertStudentInfo(studentInfo);
		System.out.println(JSONObject.fromObject(result));
	}
	
	/*删除学生*/
	@Test
	public void testDeleteStudentInfo(){
		StudentInfo studentInfo = new StudentInfo();
		studentInfo.setClassId("1160");
		studentInfo.setClassName("601班");
		studentInfo.setStudentId("999999999");
		studentInfo.setId(99999);
		studentInfo.setStudentName("张三");
		studentInfo.setStatus("0");
		studentInfo.setIclickerId("99999999");
		Result result = studentInfoService.deleteStudentInfo(studentInfo);
		System.out.println(JSONObject.fromObject(result));
	}
}
