package com.ejet.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 考勤相关
 * @author zhouwei
 *
 */
public class RedisMapAttendance {
	private static final Logger logger = LoggerFactory.getLogger(RedisMapAttendance.class);
	/**卡号为key  value为 学生名称和考勤的状态*/
	private static Map<String, Map<String,String>> attendanceMap = Collections.synchronizedMap(new HashMap<>());
	/**绑定时用来去除重复的提交,代表当前提交的人*/
    private static Set<String> cardIdSet = new HashSet<>();
	public static void addAttendance(String jsonData){
		try {
			logger.info("【签到接收到的数据】"+jsonData);
			JSONArray jsonArray = JSONArray.fromObject(jsonData);
	        for (int j = 0; j < jsonArray.size(); j++) {
	            JSONObject jo = (JSONObject) jsonArray.get(j);
	            String card_id = jo.getString("card_id");
	            /*如果attendanceMap里没有该卡号,表示不是本班学生,如果cardIdSet里有值表示已经提交过了*/
	            if (!attendanceMap.containsKey(card_id) || cardIdSet.contains(card_id)) {
	                continue;
	            }
	            cardIdSet.add(card_id);
	            Map<String, String> map = attendanceMap.get(card_id);
	            for (String key : map.keySet()) {
	                if (key.equals("status")) {
	                    map.put(key, Constant.ATTENDANCE_YES);
	                }
	            }
	            BrowserManager.refresAttendance();
	        }
		} catch (Exception e) {
			logger.info("【签到】"+IOUtils.getError(e));
		}
        
    }
	/*获取当前班级中所有人的考勤状态*/
	public static String getAttendance(){
	    List<Map<String,String>> list = new ArrayList<>();
	    Set<String> keySet = attendanceMap.keySet();
	    for (String key : keySet) {
            list.add(attendanceMap.get(key));
        }
	    return JSONArray.fromObject(list).toString();
	}
	/*获取当前班级提交的人数*/
	public static Integer getSubmitNum(){
	    return cardIdSet.size();
	}
    public static Map<String, Map<String, String>> getAttendanceMap() {
        return attendanceMap;
    }
    public static void setAttendanceMap(Map<String, Map<String, String>> attendanceMap) {
        RedisMapAttendance.attendanceMap = attendanceMap;
    }
    public static Set<String> getCardIdSet() {
        return cardIdSet;
    }
    public static void setCardIdSet(Set<String> cardIdSet) {
        RedisMapAttendance.cardIdSet = cardIdSet;
    }
    public static void clearCardIdSet() {
        cardIdSet.clear();
    }
    public static void clearAttendanceMap() {
        attendanceMap.clear();
    }
}
