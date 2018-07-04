package com.ejet.cache;

import java.math.BigDecimal;
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
import com.zkxltech.domain.ClassHour;
import com.zkxltech.domain.ClassTestVo;
import com.zkxltech.domain.QuestionInfo;
import com.zkxltech.domain.Record;
import com.zkxltech.domain.ResponseAnswer;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.service.impl.AnswerInfoServiceImpl;
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
	 * -答题器编号
	 * 			-题号
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
	
	private static List<QuestionInfo> questionInfosList = new ArrayList<QuestionInfo>();
	
	/**
	 * 每个人的作答进度信息
	 */
	private static List<ClassTestVo> classTestVos = new ArrayList<ClassTestVo>();
	
	/**
	 * 某人的作答详情
	 */
//	private static List<Record>  records= new ArrayList<Record>();
	
	/**
	 * 开始作答
	 * @param testIdStr 试卷id
	 * @throws BusinessException 
	 */
	public static void  startClassTest(List<QuestionInfo> questionInfos) throws BusinessException{
		everyAnswerMap.clear();
		questionInfoMap.clear();
		classTestVos.clear();
		addQuestionIfo(questionInfos);

		questionInfosList = questionInfos;
	}
	
	/**
	 * 将题目信息保存到缓存
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
	public static void addRedisMapClassTestAnswer1(String jsonData){
		try {
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
					if (StringUtils.isEmpty(answer.getAnswer())) {
						continue;
					}
					String questionId = answer.getId(); //题号
					
					QuestionInfo questionInfo = questionInfoMap.get(questionId);
					keyEveryAnswerMap[1] = questionId;
					
					Record record = new Record();
					record.setClassId(Global.getClassHour().getClassId());
					record.setSubject(Global.getClassHour().getSubjectName());
					record.setClassHourId(Global.getClassHour().getClassHourId());
					record.setTestId(questionInfo.getTestId());
					record.setAnswer(answer.getAnswer());
					record.setScore(questionInfo.getScore());
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
					record.setTrueAnswer(questionInfo.getTrueAnswer());
					
					RedisMapUtil.setRedisMap(everyAnswerMap, keyEveryAnswerMap, 0, record);
					
				}
			}
			BrowserManager.refreClassTest();
		} catch (Exception e) {
			logger.error(IOUtils.getError(e));
			BrowserManager.showMessage(false, "解析作答数据失败！");
		}
		
	}
	
	/**
	 * 每个人的作答信息
	 */
	public static String getEverybodyAnswerInfo(){
		try {
			List<ClassTestVo> classTestVos = new ArrayList<ClassTestVo>();
			for (int i = 0; i < Global.studentInfos.size(); i++) {
				StudentInfo studentInfo= Global.studentInfos.get(i);
				ClassTestVo classTestVo = new ClassTestVo();
				classTestVo.setStudentId(studentInfo.getStudentId());
				classTestVo.setStudentName(studentInfo.getStudentName());
				int answercount = 0;
				if (everyAnswerMap.containsKey(studentInfo.getIclickerId())) {
					Map<String, Object> map =  (Map<String, Object>)everyAnswerMap.get(studentInfo.getIclickerId());
					for (String questionId : map.keySet()) {
						Record record = (Record) map.get(questionId);
						if (!StringUtils.isEmpty(record.getAnswer())) {
							answercount++;
						}
					}
					classTestVo.setAnswerCount(answercount);
					BigDecimal decimal = new BigDecimal(answercount).divide(new BigDecimal(questionInfoMap.size()), 2 ,BigDecimal.ROUND_HALF_UP);
					classTestVo.setPercent(decimal.doubleValue());
				}else {
					classTestVo.setAnswerCount(0);
					classTestVo.setPercent(0.0);
				}
				classTestVos.add(classTestVo);
			}
			logger.info("每个人的作答情况："+JSONArray.fromObject(classTestVos).toString());
			return JSONArray.fromObject(classTestVos).toString();
		} catch (Exception e) {
			logger.error(IOUtils.getError(e));
			BrowserManager.showMessage(false, "获取作答据失败！");
			return null;
		}
		
	}
	/**
	 * 将缓存中的数据转换为record对象list(客观题)
	 * @return
	 */
	public static List<Record> getObjectiveRecordList(){
		try {
			List<Record> records = new ArrayList<Record>();
			List<ClassTestVo> classTestVos = new ArrayList<ClassTestVo>();
			for (int i = 0; i < Global.studentInfos.size(); i++) { //遍历学生
				StudentInfo studentInfo= Global.studentInfos.get(i);
				keyEveryAnswerMap[0] = studentInfo.getIclickerId();
				for (int j = 0; j < questionInfosList.size(); j++) {
					QuestionInfo questionInfo = questionInfosList.get(j); 
					keyEveryAnswerMap[1] = questionInfo.getQuestionId();
					Record record = (Record) RedisMapUtil.getRedisMap(everyAnswerMap, keyEveryAnswerMap, 0);
					if (record == null) {
						record = new Record();
						record.setClassId(studentInfo.getClassId());
						record.setClassHourId(Global.getClassHour().getClassHourId());
						record.setTestId(questionInfo.getTestId());
						record.setSubject(Global.getClassHour().getSubjectName());
						record.setQuestion(questionInfo.getQuestion());
						record.setQuestionId(questionInfo.getQuestionId());
						record.setQuestionType(questionInfo.getQuestionType());
						record.setStudentId(studentInfo.getStudentId());
						record.setStudentName(studentInfo.getStudentName());
						record.setScore(questionInfo.getScore());
						record.setResult("2");
						record.setTrueAnswer(questionInfo.getTrueAnswer());
					}
					records.add(record);
				}
				
			}
			logger.info("要保存的客观题作答记录："+JSONArray.fromObject(records));
			return records;
		} catch (Exception e) {
			logger.error(IOUtils.getError(e));
			BrowserManager.showMessage(false, "获取作答据失败！");
			return null;
		}
	}
	/**
	 * 将缓存中的数据转换为record对象list(主观题)
	 * @return
	 */
	public static List<Record> getSubjectiveRecordList(){
		try {
			List<Record> records = new ArrayList<Record>();
			List<ClassTestVo> classTestVos = new ArrayList<ClassTestVo>();
			for (int i = 0; i < Global.studentInfos.size(); i++) { //遍历学生
				StudentInfo studentInfo= Global.studentInfos.get(i);
				keyEveryAnswerMap[0] = studentInfo.getIclickerId();
				for (int j = 0; j < questionInfosList.size(); j++) {
					QuestionInfo questionInfo = questionInfosList.get(j); 
					keyEveryAnswerMap[1] = questionInfo.getQuestionId();
					Record record = (Record) RedisMapUtil.getRedisMap(everyAnswerMap, keyEveryAnswerMap, 0);
					if (record == null) {
						record = new Record();
						record.setClassId(studentInfo.getClassId());
						record.setClassHourId(Global.getClassHour().getClassHourId());
						record.setTestId(questionInfo.getTestId());
						record.setSubject(Global.getClassHour().getSubjectName());
						record.setQuestion(questionInfo.getQuestion());
						record.setQuestionId(questionInfo.getQuestionId());
						record.setQuestionType(questionInfo.getQuestionType());
						record.setStudentId(studentInfo.getStudentId());
						record.setStudentName(studentInfo.getStudentName());
						record.setScore(questionInfo.getScore());
					}
					records.add(record);
				}
				
			}
			logger.info("要保存的主观题作答记录："+JSONArray.fromObject(records));
			return records;
		} catch (Exception e) {
			logger.error(IOUtils.getError(e));
			BrowserManager.showMessage(false, "获取作答据失败！");
			return null;
		}
	}
	
//	/**
//	 * 获取答题详情
//	 * @param iclikerId 卡号
//	 */
//	public static String getEverybodyAnswerDetialInfo(String iclikerId){
//		records.clear(); //清空
//		List<ClassTestVo> classTestVos = new ArrayList<ClassTestVo>();
//		for (int i = 0; i < Global.studentInfos.size(); i++) {
//			StudentInfo studentInfo= Global.studentInfos.get(i);
//			Record record = new Record();
//			record.setQuestion(question);
//			record.setQuestionId(questionId);
//			record.setQuestionType(questionType);
//			record.setTrueAnswer(trueAnswer);
//			record.setResult(result);
//			classTestVo.setStudentId(studentInfo.getStudentId());
//			classTestVo.setStudentName(studentInfo.getStudentName());
//			if (everyAnswerMap.containsKey(studentInfo.getIclickerId())) {
//				Map<String, Object> map =  (Map<String, Object>)everyAnswerMap.get(studentInfo.getIclickerId());
//				classTestVo.setAnswerCount(map.size());
//				BigDecimal decimal = new BigDecimal(map.size()).divide(new BigDecimal(questionInfoMap.size()), 2 ,BigDecimal.ROUND_HALF_UP);
//				classTestVo.setPercent(decimal.doubleValue());
//			}else {
//				classTestVo.setAnswerCount(0);
//				classTestVo.setPercent(0.0);
//			}
//			classTestVos.add(classTestVo);
//		}
//		return JSONArray.fromObject(classTestVos).toString();
//	}
	
	/**
	 * 添加到缓存(主观题)
	 */
	public static void addRedisMapClassTestAnswer2(String jsonData){
		try {
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
					record.setClassId(studentInfo.getClassId());
					record.setSubject(Global.getClassHour().getSubjectName());
					record.setClassHourId(Global.getClassHour().getClassHourId());
					record.setAnswer(answer.getAnswer());
					record.setQuestion(questionInfo.getQuestion());
					record.setQuestionId(questionId);
					record.setQuestionType(questionInfo.getQuestionType());
					record.setScore(questionInfo.getScore());
					record.setStudentId(studentInfo.getStudentId());
					record.setStudentName(studentInfo.getStudentName());
					record.setTestId(questionInfo.getTestId());
					
					RedisMapUtil.setRedisMap(everyAnswerMap, keyEveryAnswerMap, 0, record);
				}
			}
			
			BrowserManager.refreClassTest();
		} catch (Exception e) {
			logger.error(IOUtils.getError(e));
			BrowserManager.showMessage(false, "解析作答数据失败！");
		}
	}
	
	/**
	 * 客观题测试
	 * @param args
	 */
//	public static void main(String[] args) {
//		/*模拟当前班级的学生信息*/
//		List<StudentInfo> studentInfos = new ArrayList<StudentInfo>();
//		StudentInfo studentInfo = new StudentInfo();
//		studentInfo.setIclickerId("0000001");
//		studentInfo.setStudentId("10001");
//		studentInfo.setStudentName("学号01");
//		studentInfo.setClassId("9999");
//		studentInfos.add(studentInfo);
//		StudentInfo studentInfo2 = new StudentInfo();
//		studentInfo2.setIclickerId("0000002");
//		studentInfo2.setStudentId("10002");
//		studentInfo2.setStudentName("学号02");
//		studentInfo2.setClassId("9999");
//		studentInfos.add(studentInfo2);
//		StudentInfo studentInfo3 = new StudentInfo();
//		studentInfo3.setIclickerId("0000003");
//		studentInfo3.setStudentId("10003");
//		studentInfo3.setStudentName("学号03");
//		studentInfo3.setClassId("9999");
//		studentInfos.add(studentInfo3);
//		Global.setStudentInfos(studentInfos);
//		
//		//模拟当前课程
//		ClassHour classHour = new ClassHour();
//		classHour.setClassHourId(StringUtils.getUUID());
//		classHour.setSubjectName("语文");
//		classHour.setClassId("9999");
//		Global.setClassHour(classHour);
//		
//		/*模拟答题器发送的数据*/
//		JSONArray jsonData = new JSONArray();
//		/*客观题*/
//		JSONObject jsonObjectStu = new JSONObject(); //学生1
//		jsonObjectStu.put("fun", "update_answer_list");
//		jsonObjectStu.put("card_id", "0000001");
//		JSONArray jsonArray = new JSONArray();
//		JSONObject jsonObject1 = new JSONObject(); //第一题
//		jsonObject1.put("type", "m");
//		jsonObject1.put("id", "1");
//		jsonObject1.put("answer", "ABC");
//		jsonArray.add(jsonObject1);
//		JSONObject jsonObject2 = new JSONObject(); //第二题
//		jsonObject2.put("type", "s");
//		jsonObject2.put("id", "2");
//		jsonObject2.put("answer", "B");
//		jsonArray.add(jsonObject2);
//		jsonObjectStu.put("answers", jsonArray);
//		JSONObject jsonObject3 = new JSONObject(); //第三题
//		jsonObject3.put("type", "d");
//		jsonObject3.put("id", "3");
//		jsonObject3.put("answer", "8");
//		jsonArray.add(jsonObject3);
//		jsonObjectStu.put("answers", jsonArray);
//	
//		JSONObject jsonObjectStu_1 = new JSONObject();  //学生2
//		jsonObjectStu_1.put("fun", "update_answer_list");
//		jsonObjectStu_1.put("card_id", "0000002");
//		JSONArray jsonArray_1 = new JSONArray();
//		JSONObject jsonObject1_1 = new JSONObject();
//		jsonObject1_1.put("type", "s");
//		jsonObject1_1.put("id", "1");
//		jsonObject1_1.put("answer", "D");
//		jsonArray_1.add(jsonObject1_1);
//		
//		JSONObject jsonObject1_2 = new JSONObject();
//		jsonObject1_2.put("type", "s");
//		jsonObject1_2.put("id", "2");
//		jsonObject1_2.put("answer", "C");
//		jsonArray_1.add(jsonObject1_2);
//		
//		jsonObjectStu_1.put("answers", jsonArray_1);
//		
//		jsonData.add(jsonObjectStu);
//		jsonData.add(jsonObjectStu_1);
//		
//		
//		try {
//			//获取试卷
//			QuestionInfo questionInfoParm = new QuestionInfo();
//			questionInfoParm.setTestId("4Y0001");
//			Result result = new QuestionServiceImpl().selectQuestion(questionInfoParm);	
//			if (result.getRet().equals(Constant.ERROR)) {
//				System.out.println("查询试卷题目失败!");
//			}
//			//筛选主观题
//			List<QuestionInfo> questionInfos = (List<QuestionInfo>)result.getItem();
//			List<QuestionInfo> questionInfos2 = new ArrayList<QuestionInfo>();
//			for (int i = 0; i < questionInfos.size(); i++) {
//				if (!Constant.ZHUGUANTI_NUM.equals(questionInfos.get(i).getQuestionType())) {
//					questionInfos2.add(questionInfos.get(i));
//				}
//			}
//			
//
//			startClassTest(questionInfos2);
//			
//			System.out.println("题目信息"+JSONObject.fromObject(questionInfoMap));
//			
//
//			addRedisMapClassTestAnswer1(jsonData.toString());
//			
//			System.out.println("作答信息"+JSONObject.fromObject(everyAnswerMap));
//			
//			
//			System.out.println("每个人的作答信息："+getEverybodyAnswerInfo());
//			
//			 System.out.println(JSONArray.fromObject(getRecordList()));
//			 
//			 //保存到数据库
//			 new AnswerInfoServiceImpl().stopObjectiveAnswer();
//		} catch (BusinessException e) {
//			System.out.println(e.getMessage());
//		}
//}
	
	/**
	 * 主观题测试
	 * @param args
	 */
//	public static void main(String[] args) {
//		/*模拟当前班级的学生信息*/
//		List<StudentInfo> studentInfos = new ArrayList<StudentInfo>();
//		StudentInfo studentInfo = new StudentInfo();
//		studentInfo.setIclickerId("0000001");
//		studentInfo.setStudentId("10001");
//		studentInfo.setStudentName("学号01");
//		studentInfo.setClassId("9999");
//		studentInfos.add(studentInfo);
//		StudentInfo studentInfo2 = new StudentInfo();
//		studentInfo2.setIclickerId("0000002");
//		studentInfo2.setStudentId("10002");
//		studentInfo2.setStudentName("学号02");
//		studentInfo2.setClassId("9999");
//		studentInfos.add(studentInfo2);
//		StudentInfo studentInfo3 = new StudentInfo();
//		studentInfo3.setIclickerId("0000003");
//		studentInfo3.setStudentId("10003");
//		studentInfo3.setStudentName("学号03");
//		studentInfo3.setClassId("9999");
//		studentInfos.add(studentInfo3);
//		Global.setStudentInfos(studentInfos);
//		
//		//模拟当前课程
//		ClassHour classHour = new ClassHour();
//		classHour.setClassHourId(StringUtils.getUUID());
//		classHour.setSubjectName("语文");
//		classHour.setClassId("9999");
//		Global.setClassHour(classHour);
//		
//		/*模拟答题器发送的数据*/
//		JSONArray jsonData = new JSONArray();
//		/*主观题*/
//		JSONObject jsonObjectStu = new JSONObject(); //学生1
//		jsonObjectStu.put("fun", "update_answer_list");
//		jsonObjectStu.put("card_id", "0000001");
//		JSONArray jsonArray = new JSONArray();
//		JSONObject jsonObject1 = new JSONObject(); //第六题
//		jsonObject1.put("type", "d");
//		jsonObject1.put("id", "6");
//		jsonObject1.put("answer", "5");
//		jsonArray.add(jsonObject1);
//		JSONObject jsonObject2 = new JSONObject(); //第七题
//		jsonObject2.put("type", "d");
//		jsonObject2.put("id", "7");
//		jsonObject2.put("answer", "4");
//		jsonArray.add(jsonObject2);
//		jsonObjectStu.put("answers", jsonArray);
//		JSONObject jsonObject3 = new JSONObject(); //第八题
//		jsonObject3.put("type", "d");
//		jsonObject3.put("id", "8");
//		jsonObject3.put("answer", "");
//		jsonArray.add(jsonObject3);
//		jsonObjectStu.put("answers", jsonArray);
//	
//		JSONObject jsonObjectStu_1 = new JSONObject();  //学生2
//		jsonObjectStu_1.put("fun", "update_answer_list");
//		jsonObjectStu_1.put("card_id", "0000002");
//		JSONArray jsonArray_1 = new JSONArray();
//		JSONObject jsonObject1_1 = new JSONObject();
//		jsonObject1_1.put("type", "d");
//		jsonObject1_1.put("id", "6");
//		jsonObject1_1.put("answer", "3");
//		jsonArray_1.add(jsonObject1_1);
//		
//		JSONObject jsonObject1_2 = new JSONObject();
//		jsonObject1_2.put("type", "d");
//		jsonObject1_2.put("id", "7");
//		jsonObject1_2.put("answer", "4");
//		jsonArray_1.add(jsonObject1_2);
//		
//		jsonObjectStu_1.put("answers", jsonArray_1);
//		
//		jsonData.add(jsonObjectStu);
//		jsonData.add(jsonObjectStu_1);
//		
//		try {
//			//获取试卷
//			QuestionInfo questionInfoParm = new QuestionInfo();
//			questionInfoParm.setTestId("4Y0001");
//			Result result = new QuestionServiceImpl().selectQuestion(questionInfoParm);	
//			if (result.getRet().equals(Constant.ERROR)) {
//				System.out.println("查询试卷题目失败!");
//			}
//			//筛选客观题
//			List<QuestionInfo> questionInfos = (List<QuestionInfo>)result.getItem();
//			List<QuestionInfo> questionInfos2 = new ArrayList<QuestionInfo>();
//			for (int i = 0; i < questionInfos.size(); i++) {
//				if (Constant.ZHUGUANTI_NUM.equals(questionInfos.get(i).getQuestionType())) {
//					questionInfos2.add(questionInfos.get(i));
//				}
//			}
//
//			startClassTest(questionInfos2);
//			
//			System.out.println("题目信息"+JSONObject.fromObject(questionInfoMap));
//			
//			addRedisMapClassTestAnswer2(jsonData.toString());
//			
//			System.out.println("作答信息"+JSONObject.fromObject(everyAnswerMap));
//			
//			
//			System.out.println("每个人的作答信息："+getEverybodyAnswerInfo());
//			
//			 System.out.println(JSONArray.fromObject(getRecordList()));
//			 
//			 //保存到数据库
//			 new AnswerInfoServiceImpl().stopObjectiveAnswer();
//		} catch (BusinessException e) {
//			System.out.println(e.getMessage());
//		}
//}
	
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
