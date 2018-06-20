package com.zkxltech.teaching.server;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zkxltech.domain.AnswerInfo;
import com.zkxltech.domain.Result;
import com.zkxltech.teaching.service.QuestionService;
import com.zkxltech.teaching.service.impl.QuestionServiceImpl;

import net.sf.json.JSONObject;

public class QuestionInfoServiceTest {
	/*导入试卷*/
	@Test
	public void testImportTestPaper(){
		QuestionService questionService = new QuestionServiceImpl();
		Result result = questionService.importQuestion("D:/Users/zkxl/Desktop/excel表/中科答题器模板/试卷表_120题测试.xlsx");
		System.out.println(JSONObject.fromObject(result));
	}
	
}
