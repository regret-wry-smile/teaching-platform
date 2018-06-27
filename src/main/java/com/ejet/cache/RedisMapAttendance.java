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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 考勤相关
 * @author zhouwei
 *
 */
public class RedisMapAttendance {
	private static final Logger logger = LoggerFactory.getLogger(RedisMapAttendance.class);
	private static Map<String, Map<String,String>> attendanceMap = Collections.synchronizedMap(new HashMap<>());
	/**绑定时用来去除重复的提交*/
    private static Set<String> cardIdSet = new HashSet<>();
	public static void addAttendance(String jsonData){
        JSONArray jsonArray = JSONArray.fromObject(jsonData);
        for (int j = 0; j < jsonArray.size(); j++) {
            JSONObject jo = (JSONObject) jsonArray.get(j);
            String card_id = jo.getString("card_id");
            if (cardIdSet.contains(card_id)) {
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
    }
	public static String getAttendance(){
	    List<Map<String,String>> list = new ArrayList<>();
	    Set<String> keySet = attendanceMap.keySet();
	    for (String key : keySet) {
            list.add(attendanceMap.get(key));
        }
	    return JSONArray.fromObject(list).toString();
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
