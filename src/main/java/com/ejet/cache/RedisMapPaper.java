package com.ejet.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.RedisMapUtil;
import com.ejet.core.util.StringUtils;
import com.zkxltech.domain.Answer;
import com.zkxltech.domain.QuestionInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class RedisMapPaper {
	private static final Logger logger = LoggerFactory.getLogger(RedisMapPaper.class);
	
	/**
	 * 添加试卷的题目缓存
	 */
	private  static Map<String, Object> questionList = Collections.synchronizedMap(new HashMap<String, Object>());

	private static String[] keyQuestionList = {"questionId"};
	
	/**
	 * 添加题目缓存
	 * @param obj
	 */
	public static void addQuestion(Object obj){
		QuestionInfo questionInfo = StringUtils.parseToBean(obj, QuestionInfo.class);
		keyQuestionList[0] = String.valueOf(questionList.size()+1);
		RedisMapUtil.setRedisMap(questionList, keyQuestionList, 0, questionInfo);
    }
	
	/**
	 * 获取所有题目信息
	 * @return
	 */
	public static Map<String, Object> getQuestions(){
		return questionList;
    }
	
	/**
	 * 删除题目信息
	 * @return
	 */
	public static void deleteQuestion(Object obj){
		QuestionInfo questionInfo = StringUtils.parseToBean(obj, QuestionInfo.class);
		questionList.remove(String.valueOf(questionInfo.getId()));
    }
	
	/**
	 * 修改题目信息
	 * @return
	 */
	public static void updateQuestion(Object obj){
		QuestionInfo questionInfo = StringUtils.parseToBean(obj, QuestionInfo.class);
		if (questionList.containsKey(String.valueOf(questionInfo.getId()))) {
			keyQuestionList[0] = String.valueOf(questionInfo.getId());
			RedisMapUtil.setRedisMap(questionList, keyQuestionList, 0, questionInfo);
		}
    }
	
	
//	public static void main(String[] args) {
//		QuestionInfo questionInfo = new QuestionInfo();
//		questionInfo.setQuestion("第一题");
//		addQuestion(questionInfo);
//		QuestionInfo questionInfo2 = new QuestionInfo();
//		questionInfo2.setQuestion("第二题");
//		addQuestion(questionInfo2);
//		
//		QuestionInfo questionInfo3 = new QuestionInfo();
//		questionInfo3.setId(1);
//		deleteQuestion(questionInfo3);
//		System.out.println(JSONArray.fromObject(getQuestions()));
//	}
}
