package com.ejet.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
	/**单例*/
    private static final RedisMapAttendance INSTANCE = new RedisMapAttendance();
    private RedisMapAttendance() {
    }
    public static RedisMapAttendance getInstance(){
        return INSTANCE;
    }
	public static Map<String, Map<String,String>> attendanceMap = Collections.synchronizedMap(new HashMap<>());
	/**绑定时用来去除重复的提交*/
    public static Set<String> cardIdSet = new HashSet<>();
	public void addAttendance(String jsonData){
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
                map.put(key, Constant.ATTENDANCE_YES);
            }
        }
    }
}
