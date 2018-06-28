//package com.zkxltech.teaching.server;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import com.zkxltech.domain.AnswerInfo;
//import com.zkxltech.domain.Result;
//import com.zkxltech.service.AnswerInfoService;
//import com.zkxltech.service.impl.AnswerInfoServiceImpl;
//
//import net.sf.json.JSONObject;
//
//public class AnswerInfoServiceTest {
//	private AnswerInfoService answerInfoService;
//	
//	@Before
//	public void innit() {
//		answerInfoService = new AnswerInfoServiceImpl();
//	}
//	/*批量插入答题信息*/
//	@Test
//	public void testinsertAnswerInfo(){
//		List<AnswerInfo> answerInfos = new ArrayList<AnswerInfo>();
//		AnswerInfo answerInfo = new AnswerInfo();
//		answerInfo.setStudentId("9999");
//		answerInfo.setQuestionId("99");
//		answerInfo.setQusetionType("主观题");
//		answerInfo.setResult("正确");
//		answerInfo.setAnsweDatetime("2017-12-10 18:20:50");
//		answerInfos.add(answerInfo);
//		AnswerInfo answerInfo2 = new AnswerInfo();
//		answerInfo2.setStudentId("9999");
//		answerInfo2.setQusetionType("主观题");
//		answerInfo2.setResult("正确");
//		answerInfo2.setQuestionId("98");
//		answerInfo2.setAnsweDatetime("2017-12-10 18:20:50");
//		answerInfos.add(answerInfo2);
//		Result result = answerInfoService.insertAnswerInfos(answerInfos);
//		System.out.println(JSONObject.fromObject(result));
//	}
//	
//	/*查询答题信息*/
//	@Test
//	public void testSelectStudentInfo(){
//		AnswerInfo answerInfo = new AnswerInfo();
//		answerInfo.setStudentId("9999");
//		answerInfo.setQuestionId("98");
//		answerInfo.setQusetionType("主观题");
//		answerInfo.setResult("正确");
//		answerInfo.setAnsweDatetime("2017-12-10 18:20:50");
//		Result result = answerInfoService.selectAnswerInfo(answerInfo);
//		System.out.println(JSONObject.fromObject(result));
//	}
//
//	/*删除答题信息*/
//	@Test
//	public void testDeleteStudentInfo(){
//		AnswerInfo answerInfo = new AnswerInfo();
//		answerInfo.setStudentId("9999");
//		answerInfo.setQuestionId("99");
//		answerInfo.setQusetionType("主观题");
//		answerInfo.setResult("正确");
//		answerInfo.setAnsweDatetime("2017-12-10 18:20:50");
//		Result result = answerInfoService.deleteAnswerInfo(answerInfo);
//		System.out.println(JSONObject.fromObject(result));
//	}
//}
