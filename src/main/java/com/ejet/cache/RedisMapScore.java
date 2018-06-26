package com.ejet.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.RedisMapUtil;
import com.ejet.core.util.StringUtils;
import com.ejet.core.util.constant.Global;
import com.zkxltech.domain.Answer;
import com.zkxltech.domain.Score;
import com.zkxltech.domain.StudentInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 *评分相关缓存
 * @author zkxl
 *
 */
public class RedisMapScore {
	private static final Logger logger = LoggerFactory.getLogger(RedisMapScore.class);
	/**
	 * 当前评分主题id
	 */
	private static String scoreInfoId;
	/**
	 * 评分主题信息缓存
	 */
	public static Map<String, Object> scoreInfoMap = Collections.synchronizedMap(new HashMap<String, Object>());
	/**
	 * 评分详情信息缓存
	 */
	public static Map<String, Object> scoreDetailInfoMap = Collections.synchronizedMap(new HashMap<String, Object>());
	/**
	 * 评分详情信息缓存
	 */
	public static Map<String, Object> barMap = Collections.synchronizedMap(new HashMap<String, Object>());
	
	private static String[] keyScoreInfoMap = {"uuid"};
	
	private static String[] keyScoreDetailInfoMap = {"uuid","questionId","iclicker"};
	
	private static String[] keyBarMap = {"questionId"};
	
	/**
	 * 清空缓存
	 */
	public static void clearMap(){
		scoreInfoMap.clear();
		scoreDetailInfoMap.clear();
		barMap.clear();
	}
	
	/**
	 * 添加主题
	 * @param score
	 */
	public static void addScoreInfo(Score score){
		scoreInfoId =  StringUtils.getUUID();
		keyScoreInfoMap[0] = scoreInfoId;
		RedisMapUtil.setRedisMap(scoreInfoMap, keyScoreInfoMap, 0, score);
    }
	/**
	 * 获取主题
	 */
	public static Score getScoreInfo(){
		keyScoreInfoMap[0] = scoreInfoId;
		Score score =  (Score) RedisMapUtil.getRedisMap(scoreInfoMap, keyScoreInfoMap, 0);
		return score;
	}
	
	/**
	 * 添加评分详情
	 * @param score
	 */
	public static void addscoreDetailInfo(String jsonData){
		keyScoreDetailInfoMap[0] = scoreInfoId; //主题编号
		JSONArray jsonArray = JSONArray.fromObject(jsonData); 
        for (int  i= 0; i < jsonArray.size(); i++) {
        	JSONObject jsonObject = jsonArray.getJSONObject(i); //，每个学生的作答信息
        	String carId = jsonObject.getString("card_id"); //答题器编号
        	if (verifyCardId(carId)) {
        		JSONArray answers =  JSONArray.fromObject(jsonObject.get("answers"));
        		for (int j = 0; j < answers.size(); j++) {
        			JSONObject answeJSONObject = answers.getJSONObject(j);
        			String num = answeJSONObject.getString("id");//节目编号(题目编号)
//        			String answer = answeJSONObject.getString("answer");//答案
        			keyScoreDetailInfoMap[1] = num;
        			keyScoreDetailInfoMap[2] = carId;
        			Answer answer = (Answer) JSONObject.toBean((JSONObject) RedisMapUtil.getRedisMap(scoreDetailInfoMap, keyScoreDetailInfoMap, 0), Answer.class);
        			if (answer != null) {
        				continue;
					}
        			RedisMapUtil.setRedisMap(scoreDetailInfoMap, keyScoreDetailInfoMap, 0, answeJSONObject);
				}
			}
        }
    }
	
	
	/**
	 * 转换成柱状图相关数据
	 * @param score
	 */
	public static void dealBarInfo(){
		for (String uuid : scoreDetailInfoMap.keySet()) {
			Map<String, Object> map1 =  (Map<String, Object>) scoreDetailInfoMap.get(uuid);
			for (int i = 0; i < getScoreInfo().getPrograms().size(); i++) {
				if (map1.containsKey(String.valueOf(i+1))) {
					Map<String, Object> map2 = (Map<String, Object>) map1.get(String.valueOf(i));
					keyBarMap[0] = String.valueOf(i+1); //题号
					int total = 0;//总分
					int peopleSum = 0; //人数
					for (String iclickerId : map2.keySet()) {
						Answer answer = (Answer) JSONObject.toBean((JSONObject) map2.get(iclickerId), Answer.class);
						total = total + Integer.parseInt(answer.getAnswer());
						peopleSum ++;
					}
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("total", total);
					jsonObject.put("peopleSum", peopleSum);
					RedisMapUtil.setRedisMap(barMap, keyBarMap, 0, jsonObject);
				}
			}
			
			/*for (String questionId : map1.keySet()) {
				Map<String, Object> map2 =  (Map<String, Object>)	map1.get(questionId);
				keyBarMap[0] = questionId; //题号
				int total = 0;//总分
				int peopleSum = 0; //人数
				for (String iclickerId : map2.keySet()) {
					Answer answer = (Answer) JSONObject.toBean((JSONObject) map2.get(iclickerId), Answer.class);
					total = total + Integer.parseInt(answer.getAnswer());
					peopleSum ++;
				}
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("total", total);
				jsonObject.put("peopleSum", peopleSum);
				RedisMapUtil.setRedisMap(barMap, keyBarMap, 0, jsonObject);
			}*/
		}	
    }
	
	/**
	 * 获取柱状图需要的数据
	 * @param score
	 */
	public static String getScoreInfoBar(){
		return JSONObject.fromObject(barMap).toString();
    }
	
	public static void main(String[] args) {
		List<StudentInfo> studentInfos = new ArrayList<StudentInfo>();
		StudentInfo studentInfo = new StudentInfo();
		studentInfo.setIclickerId("0000001");
		studentInfos.add(studentInfo);
		StudentInfo studentInfo2 = new StudentInfo();
		studentInfo2.setIclickerId("0000002");
		studentInfos.add(studentInfo2);
		Global.setStudentInfos(studentInfos);
		
		JSONArray jsonData = new JSONArray();
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("fun", "update_answer_list");
		jsonObject.put("card_id", "0000001");
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("type", "d");
		jsonObject2.put("id", "1");
		jsonObject2.put("answer", "2");
		jsonArray.add(jsonObject2);
		JSONObject jsonObject3 = new JSONObject();
		jsonObject3.put("type", "d");
		jsonObject3.put("id", "2");
		jsonObject3.put("answer", "5");
		jsonArray.add(jsonObject3);
		jsonObject.put("answers", jsonArray);
		
		
		JSONObject jsonObject_1 = new JSONObject();
		jsonObject_1.put("fun", "update_answer_list");
		jsonObject_1.put("card_id", "0000002");
		JSONArray jsonArray_1 = new JSONArray();
		JSONObject jsonObject2_1 = new JSONObject();
		jsonObject2_1.put("type", "d");
		jsonObject2_1.put("id", "3");
		jsonObject2_1.put("answer", "9");
		jsonArray_1.add(jsonObject2_1);
		JSONObject jsonObject3_1 = new JSONObject();
		jsonObject3_1.put("type", "d");
		jsonObject3_1.put("id", "2");
		jsonObject3_1.put("answer", "9");
		jsonArray_1.add(jsonObject3_1);
		jsonObject_1.put("answers", jsonArray_1);
		
		jsonData.add(jsonObject);
		jsonData.add(jsonObject_1);
		System.out.println(jsonData);
		
		addscoreDetailInfo(jsonData.toString());
		dealBarInfo();
		
		System.out.println(getScoreInfoBar());
	}
	
	/**
	 * 判断该答题器编号是否属于当前班级
	 */
	public static boolean verifyCardId(String cardId){
		for (int i = 0; i < Global.studentInfos.size(); i++) {
    		if (cardId.equals(Global.studentInfos.get(i).getIclickerId())) { //是否属于当前班级
				return true;
			}
		}
		return false;
		
	}
}