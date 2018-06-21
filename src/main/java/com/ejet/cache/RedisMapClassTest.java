package com.ejet.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.RedisMapUtil;
/**
 * 随堂检测相关缓存
 * @author zkxl
 *
 */
public class RedisMapClassTest {
	private static final Logger logger = LoggerFactory.getLogger(RedisMapClassTest.class);
	
	/**
	 * 随堂检测作答缓存
	 */
	private  static Map<String, Object> classTestAnswerMap = Collections.synchronizedMap(new HashMap<String, Object>());
	/**
	 * eg: 
	 * 张三：
	 * 		第一题：AnswerResponse
	 * 		第二题：AnswerResponse
	 * 李四：
	 *		第一题：AnswerResponse
	 * 		第二题：AnswerResponse
	 */
	private static String[] keyClassTestAnswerMap = {"iclickerId","questionId"};
	
//	public static void addAnswer(AnswerResponse answerResponse){
//		keyClassTestAnswerMap[0] = answerResponse.getIclickerId();
//		keyClassTestAnswerMap[1] = answerResponse.getQuestionId();
//		List<AnswerResponse> list = (List<AnswerResponse>) RedisMapUtil.getRedisMap(classTestAnswerMap, keyAnswerMap, 0);
//		if(list==null) {
//			list = new ArrayList<AnswerResponse>();
//		}
//		list.add(answerResponse);
//		RedisMapUtil.setRedisMap(classTestAnswerMap, keyClassTestAnswerMap, 0, answerResponse);
//	}
}
