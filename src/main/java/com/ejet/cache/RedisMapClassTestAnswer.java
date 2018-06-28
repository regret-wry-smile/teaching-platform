package com.ejet.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.BusinessException;
import com.ejet.core.util.RedisMapUtil;
import com.ejet.core.util.StringUtils;
import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.constant.Global;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.domain.Answer;
import com.zkxltech.domain.QuestionInfo;
import com.zkxltech.domain.Record;
import com.zkxltech.domain.ResponseAnswer;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.service.impl.QuestionServiceImpl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 随堂检测
 * @author zkxl
 *
 */
public class RedisMapClassTestAnswer {
	private static final Logger logger = LoggerFactory.getLogger(RedisMapClassTestAnswer.class);
	
	/**
	 * 每条作答记录缓存
	 */
	private static Map<String, Object> everyAnswerMap = Collections.synchronizedMap(new HashMap<String, Object>());
	
	
	private static String[] keyEveryAnswerMap = {"iclickerId","questionId"};
	
//	/**
//	 * 试卷id
//	 */
//	private static String testId;
	
	/**
	 * 试卷题目信息
	 */
	private static Map<String, QuestionInfo> questionInfoMap = Collections.synchronizedMap(new HashMap<String, QuestionInfo>());
	
	
	
	/**
	 * 开始作答
	 * @param testIdStr 试卷id
	 */
	public static void  startClassTest(){
		questionInfoMap.clear();
		
	}
	
	/**
	 * 将题目信息保存到缓存(客观题)
	 * @throws BusinessException 
	 */
	public static void addQuestionIfo(List<QuestionInfo> questionInfos) throws BusinessException{
		try {
			for (int i = 0; i < questionInfos.size(); i++) {
				questionInfoMap.put(questionInfos.get(i).getQuestionId(), questionInfos.get(i));
			}
		} catch (Exception e) {
			logger.error(IOUtils.getError(e));
			throw new BusinessException(Constant.ERROR, "将题目信息保存到缓存失败！");
		}
		
	}
	
	
	/**
	 * 添加到缓存(客观题)
	 */
	public static void addRedisMapClassTestAnswer(String jsonData){
		List<ResponseAnswer> responseAnswers = (List<ResponseAnswer>) JSONArray.toCollection(JSONArray.fromObject(jsonData), ResponseAnswer.class);
		for (int i = 0; i < responseAnswers.size(); i++) {
			ResponseAnswer responseAnswer = responseAnswers.get(i);

			keyEveryAnswerMap[0] = responseAnswer.getCard_id();
			/*判断是否属于该班*/
			StudentInfo studentInfo = verifyCardId(responseAnswer.getCard_id());
			if (studentInfo == null) {
				continue;
			}

			List<Answer> answers = (List<Answer>) JSONArray.toCollection(JSONArray.fromObject(responseAnswer.getAnswers()), Answer.class);;
			for (int j = 0; j < answers.size(); j++) {
				Answer answer = answers.get(j);
				String questionId = answer.getId(); //题号
				
				QuestionInfo questionInfo = questionInfoMap.get(questionId);
				keyEveryAnswerMap[1] = questionId;
				
				Record record = new Record();
				record.setClassHourId(Global.getClassHour().getClassHourId());
				record.setAnswer(answer.getAnswer());
				record.setQuestion(questionInfo.getQuestion());
				record.setQuestionId(questionId);
				record.setQuestionType(questionInfo.getQuestionType());
				if (questionInfo.getTrueAnswer().equals(answer.getAnswer())) {
					record.setResult("1");
				}else {
					record.setResult("2");
				}
				record.setStudentId(studentInfo.getStudentId());
				record.setStudentName(studentInfo.getStudentName());
				record.setTestId(questionInfo.getTestId());
				record.setTrueAnswer(questionInfo.getTrueAnswer());
				
				RedisMapUtil.setRedisMap(everyAnswerMap, keyEveryAnswerMap, 0, record);
				
			}
		}
	}
	
	
	public static void main(String[] args) {
		/*模拟当前班级的学生信息*/
		List<StudentInfo> studentInfos = new ArrayList<StudentInfo>();
		StudentInfo studentInfo = new StudentInfo();
		studentInfo.setIclickerId("0000001");
		studentInfos.add(studentInfo);
		StudentInfo studentInfo2 = new StudentInfo();
		studentInfo2.setIclickerId("0000002");
		studentInfos.add(studentInfo2);
		StudentInfo studentInfo3 = new StudentInfo();
		studentInfo3.setIclickerId("0000003");
		studentInfos.add(studentInfo3);
		Global.setStudentInfos(studentInfos);
		
		/*模拟答题器发送的数据*/
		JSONArray jsonData = new JSONArray();
		
		JSONObject jsonObjectStu = new JSONObject(); //学生1
		jsonObjectStu.put("fun", "update_answer_list");
		jsonObjectStu.put("card_id", "0000001");
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject1 = new JSONObject(); //第一题
		jsonObject1.put("type", "m");
		jsonObject1.put("id", "1");
		jsonObject1.put("answer", "ABC");
		jsonArray.add(jsonObject1);
		JSONObject jsonObject2 = new JSONObject(); //第二题
		jsonObject2.put("type", "s");
		jsonObject2.put("id", "2");
		jsonObject2.put("answer", "B");
		jsonArray.add(jsonObject2);
		jsonObjectStu.put("answers", jsonArray);
		JSONObject jsonObject3 = new JSONObject(); //第三题
		jsonObject3.put("type", "d");
		jsonObject3.put("id", "3");
		jsonObject3.put("answer", "8");
		jsonArray.add(jsonObject3);
		jsonObjectStu.put("answers", jsonArray);
	
		JSONObject jsonObjectStu_1 = new JSONObject();  //学生2
		jsonObjectStu_1.put("fun", "update_answer_list");
		jsonObjectStu_1.put("card_id", "0000002");
		JSONArray jsonArray_1 = new JSONArray();
		JSONObject jsonObject1_1 = new JSONObject();
		jsonObject1_1.put("type", "s");
		jsonObject1_1.put("id", "1");
		jsonObject1_1.put("answer", "D");
		jsonArray_1.add(jsonObject1_1);
		
		JSONObject jsonObject1_2 = new JSONObject();
		jsonObject1_2.put("type", "s");
		jsonObject1_2.put("id", "2");
		jsonObject1_2.put("answer", "C");
		jsonArray_1.add(jsonObject1_2);
		
		jsonObjectStu_1.put("answers", jsonArray_1);
		
		jsonData.add(jsonObjectStu);
		jsonData.add(jsonObjectStu_1);
		
		
		try {
			//获取试卷
			QuestionInfo questionInfoParm = new QuestionInfo();
			questionInfoParm.setTestId("4Y0001");
			Result result = new QuestionServiceImpl().selectQuestion(questionInfoParm);	
			if (result.getRet().equals(Constant.ERROR)) {
				System.out.println("查询试卷题目失败!");
			}
			
			//筛选主观题
			List<QuestionInfo> questionInfos = (List<QuestionInfo>)result.getItem();
			List<QuestionInfo> questionInfos2 = new ArrayList<QuestionInfo>();
			for (int i = 0; i < questionInfos.size(); i++) {
				if (!Constant.ZHUGUANTI_NUM.equals(questionInfos.get(i).getQuestionType())) {
					questionInfos2.add(questionInfos.get(i));
				}
			}
			
			addQuestionIfo(questionInfos2); //添加到题目缓存
			
			System.out.println("题目信息"+JSONObject.fromObject(questionInfoMap));
			

			addRedisMapClassTestAnswer(jsonData.toString());
			
			System.out.println("作答信息"+JSONArray.fromObject(everyAnswerMap));
			
		} catch (BusinessException e) {
			System.out.println(e.getMessage());
		}
}
	
	
	/**
	 * 判断该答题器编号是否属于当前班级
	 */
	public static StudentInfo verifyCardId(String cardId){
		for (int i = 0; i < Global.studentInfos.size(); i++) {
    		if (cardId.equals(Global.studentInfos.get(i).getIclickerId())) { //是否属于当前班级
				return Global.studentInfos.get(i);
			}
		}
		return null;
		
	}
}
