package com.ejet.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.constant.Constant;
import com.zkxltech.domain.Answer;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.service.impl.EquipmentServiceImpl;
import com.zkxlteck.scdll.QuickThread;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 答题相关
 * @author zhouwei
 *
 */
public class RedisMapAnswer {
	private static final Logger logger = LoggerFactory.getLogger(RedisMapAnswer.class);
	private static Map<String,String> singleAnswerMap = Collections.synchronizedMap(new HashMap<>());
	private static Map<String,String> multipleAnswerMap = Collections.synchronizedMap(new HashMap<>());
	/**学生信息*/
	private static Map<String,StudentInfo> studentInfoMap = new HashMap<>(); 
	private static Answer answer;
    public static void addAnswer(String jsonData){
        String type = answer.getType();
//        if (type.equals(EquipmentServiceImpl.)) {
//            
//        }
    }
    public static Map<String, StudentInfo> getStudentInfoMap() {
        return studentInfoMap;
    }
    public static void setStudentInfoMap(Map<String, StudentInfo> studentInfoMap) {
        RedisMapAnswer.studentInfoMap = studentInfoMap;
    }
    public static void clearStudentInfoMap() {
        studentInfoMap.clear();
    }
    public static Map<String, String> getSingleAnswerMap() {
        return singleAnswerMap;
    }
    public static void setSingleAnswerMap(Map<String, String> singleAnswerMap) {
        RedisMapAnswer.singleAnswerMap = singleAnswerMap;
    }
    public static Map<String, String> getMultipleAnswerMap() {
        return multipleAnswerMap;
    }
    public static void setMultipleAnswerMap(Map<String, String> multipleAnswerMap) {
        RedisMapAnswer.multipleAnswerMap = multipleAnswerMap;
    }
    public static void clearMultipleAnswerMap() {
        multipleAnswerMap.clear();
    }
    public static void clearSingleAnswerMap() {
        singleAnswerMap.clear();
    }
    public static Answer getAnswer() {
        return answer;
    }
    public static void setAnswer(Answer answer) {
        RedisMapAnswer.answer = answer;
    }
    public static Logger getLogger() {
        return logger;
    }
}
