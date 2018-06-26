package com.ejet.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.RedisMapUtil;
import com.ejet.core.util.StringUtils;
import com.ejet.core.util.constant.Constant;
import com.zkxltech.domain.Answer;
import com.zkxltech.domain.QuestionInfo;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.TestPaper;
import com.zkxltech.service.impl.QuestionServiceImpl;

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
	public static void addQuestion(QuestionInfo questionInfo){
		keyQuestionList[0] = String.valueOf(questionInfo.getId());
		RedisMapUtil.setRedisMap(questionList, keyQuestionList, 0, questionInfo);
    }
	
	/**
	 * 将数据库中的题目添加至缓存
	 */
	public static void addQuestions(TestPaper testPaper){
		//从数据库中获取该试卷的题目信息
		QuestionInfo questionInfo = new QuestionInfo();
		Result result = new QuestionServiceImpl().selectQuestion(questionInfo);
		if (Constant.SUCCESS.equals(result.getRet())) {
			List<QuestionInfo> questionInfos = (List<QuestionInfo>) result.getItem();
			//将查询到的题目信息添加至缓存
			for (int i = 0; i < questionInfos.size(); i++) {
				keyQuestionList[0] = String.valueOf(questionInfos.get(i).getId());
				RedisMapUtil.setRedisMap(questionList, keyQuestionList, 0, questionInfos.get(i));
			}
		}else {
			BrowserManager.showMessage(false, "查询题目失败！");
		}
	}
	
	/**
	 * 获取所有题目信息
	 * @return
	 */
	public static List<QuestionInfo> getQuestions(QuestionInfo questionInfo){
		
		List<QuestionInfo> retQuestionInfos = new ArrayList<QuestionInfo>();
		for (String key : questionList.keySet()) {
			retQuestionInfos.add((QuestionInfo)questionList.get(key));
		}
		return retQuestionInfos;
    }
	
	/**
	 * 删除题目信息
	 * @return
	 */
	public static void deleteQuestion(QuestionInfo questionInfo){
		questionList.remove(String.valueOf(questionInfo.getId()));
    }
	
	/**
	 * 修改题目信息
	 * @return
	 */
	public static void updateQuestion(QuestionInfo questionInfo){
		if (questionList.containsKey(String.valueOf(questionInfo.getId()))) {
			keyQuestionList[0] = String.valueOf(questionInfo.getId());
			RedisMapUtil.setRedisMap(questionList, keyQuestionList, 0, questionInfo);
		}
    }
	
	/**
	 * 清空题目信息缓存
	 * @return
	 */
	public static void clearRedis(){
		questionList.clear();
    }

}
