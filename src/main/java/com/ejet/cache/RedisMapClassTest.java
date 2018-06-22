package com.ejet.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.RedisMapUtil;
import com.zkxltech.domain.Answer;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
//	private static String[] keyClassTestAnswerMap = {"iclickerId","questionId"};
	
	public static void addAnswer(String jsonData){
        // Map<String, Map<String, Answer>> cardAnswerMap = new HashMap<>();
        JSONArray jsonArray = JSONArray.fromObject(jsonData);
        for (int j = 0; j < jsonArray.size(); j++) {
            JSONObject jo = (JSONObject) jsonArray.get(j);
            String card_id = jo.getString("card_id");
            String answers = jo.getString("answers");
            JSONArray array = JSONArray.fromObject(answers);
            Map<String, Answer> answerMap = new HashMap<>();
            for (Object object : array) {
                Answer answer = (Answer) com.zkxltech.ui.util.StringUtils.parseJSON(object, Answer.class);
                answerMap.put(answer.getId(), answer);
            }
            classTestAnswerMap.put(card_id, answerMap);
        }
    }
}
