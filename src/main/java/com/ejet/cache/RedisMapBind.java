package com.ejet.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.constant.Constant;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.service.impl.StudentInfoServiceImpl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author: ZhouWei
 * @date:2018年6月25日 下午5:18:24
 */
public class RedisMapBind {
    private static final Logger logger = LoggerFactory.getLogger(RedisMapBind.class);
    /** 一键配对缓存 */
    private static Map<String, Object> bindMap = Collections.synchronizedMap(new HashMap<String, Object>());
    /**答题器id对应的学生*/
    private static Map<String, StudentInfo> studentInfoMap = Collections.synchronizedMap(new HashMap<>());
    /**绑定状态*/
    private static final String STATE_BIND = "1";
    /***/
    private static final StudentInfoServiceImpl SIS= new StudentInfoServiceImpl();
    /**数据库中所有卡对应的状态*/
    private static Map<String,String> cardIdMap = new HashMap<>();
    public static void addBindMap(String jsonData){
        JSONArray jsonArray = JSONArray.fromObject(jsonData);
        for (Object object : jsonArray) {
            JSONObject jo = JSONObject.fromObject(object);
            String cardId = jo.getString("card_id");
            //如果不包含,表示该卡不是本班的学生,跳过
            if (!studentInfoMap.containsKey(cardId)) {
                continue;
            }
            //判断该学生是否已经绑定了,如果绑定了就不用再绑了
            StudentInfo studentInfo = studentInfoMap.get(cardId);
            if (Constant.BING_YES.equals(studentInfo.getStatus())) {
                continue;
            }
            studentInfo.setStatus(STATE_BIND);
            Integer accomplish = (Integer)bindMap.get("accomplish");
            ++accomplish;
            Integer notAccomplish = (Integer)bindMap.get("notAccomplish");
            --notAccomplish;
            bindMap.put("studentName", studentInfo.getStudentName()); //
            bindMap.put("accomplish", accomplish);
            bindMap.put("notAccomplish",notAccomplish);
            SIS.updateStudentById(studentInfo);
        }
        BrowserManager.refreshBindCard();
    }
    
	public static String getBindMapValue(){
		return JSONObject.fromObject(bindMap).toString();
    }
	
    public static Map<String, Object> getBindMap() {
        return bindMap;
    }

    public static Map<String, StudentInfo> getStudentInfoMap() {
        return studentInfoMap;
    }
    public static void clearStudentInfoMap(){
        studentInfoMap.clear();
    }

    public static void setStudentInfoMap(Map<String, StudentInfo> studentInfoMap) {
        RedisMapBind.studentInfoMap = studentInfoMap;
    }

    public static Map<String,String> getCardIdSet() {
        return cardIdMap;
    }

    public static void setCardIdSet(Map<String,String> cardIdMap) {
        RedisMapBind.cardIdMap = cardIdMap;
    }
    public static void clearCardIdMap() {
        cardIdMap.clear();
    }

    public static void setBindMap(Map<String, Object> bindMap) {
        RedisMapBind.bindMap = bindMap;
    }
    public static void clearBindMap() {
        bindMap.clear();
    }
}
