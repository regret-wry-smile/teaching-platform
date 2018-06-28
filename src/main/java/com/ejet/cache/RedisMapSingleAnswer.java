package com.ejet.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.StringUtils;
import com.ejet.core.util.constant.Constant;
import com.zkxltech.domain.Answer;
import com.zkxltech.domain.StudentInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 多选
 * @author zkxl
 *
 */
public class RedisMapSingleAnswer {
	private static final Logger logger = LoggerFactory.getLogger(RedisMapSingleAnswer.class);
    private static Map<String,Integer> singleAnswerMap = Collections.synchronizedMap(new HashMap<>());
    /**学生信息*/
    private static Map<String,StudentInfo> studentInfoMap = new HashMap<>(); 
    private static Answer answer;
    public static final String CHAR_A = "A";
    public static final String CHAR_B = "B";
    public static final String CHAR_C = "C";
    public static final String CHAR_D = "D";
    public static final String NUMBER_1 = "1";
    public static final String NUMBER_2 = "2";
    public static final String NUMBER_3 = "3";
    public static final String NUMBER_4 = "4";
    public static final String NUMBER_5 = "5";
    public static final String NUMBER_6 = "6";
    public static final String NUMBER_7 = "7";
    public static final String NUMBER_8 = "8";
    public static final String NUMBER_9 = "9";
    public static final String JUDGE_TRUE = "true";
    public static final String JUDGE_FALSE = "false";
    
    public static void addAnswer(String jsonData){
        JSONArray jsonArray= JSONArray.fromObject(jsonData);
        String type = answer.getType();
        for (Object object : jsonArray) {
            JSONObject jsonObject = JSONObject.fromObject(object);
            JSONArray answers =  JSONArray.fromObject(jsonObject.get("answers"));
            for (Object answer : answers) {
                JSONObject answerJO = JSONObject.fromObject(answer);
                String result = answerJO.getString("answer");
                switch (type) {
                    case Constant.ANSWER_CHAR_TYPE:
                        if (StringUtils.isEmpty(result)) {
                            continue;
                        }
                        setCharCount(result);
                        break;
                    case Constant.ANSWER_NUMBER_TYPE:
                        setNumberCount(result);
                        break;
                    case Constant.ANSWER_JUDGE_TYPE:
                        setJudgeCount(result);
                        break;
                }
            }
        }
    }
    public static String getSingleAnswerValue(){
        return singleAnswerMap.toString();
    }
    private static void setJudgeCount(String result) {
        switch (result) {
            case JUDGE_TRUE:
                Integer countTrue =  singleAnswerMap.containsKey(JUDGE_TRUE) ? singleAnswerMap.get(JUDGE_TRUE)+1:1;
                singleAnswerMap.put(JUDGE_TRUE, countTrue);
                break;
            case JUDGE_FALSE:
                Integer countFalse =  singleAnswerMap.containsKey(JUDGE_FALSE) ? singleAnswerMap.get(CHAR_B)+1:1;
                singleAnswerMap.put(JUDGE_FALSE, countFalse);
                break;
        }
    }
    private static void setNumberCount(String result) {
        switch (result) {
            case NUMBER_1:
                Integer number1 =  singleAnswerMap.containsKey(NUMBER_1) ? singleAnswerMap.get(NUMBER_1)+1:1;
                singleAnswerMap.put(NUMBER_1, number1);
                break;
            case NUMBER_2:
                Integer number2 =  singleAnswerMap.containsKey(NUMBER_2) ? singleAnswerMap.get(NUMBER_2)+1:1;
                singleAnswerMap.put(NUMBER_2, number2);
                break;
            case NUMBER_3:
                Integer number3 =  singleAnswerMap.containsKey(NUMBER_3) ? singleAnswerMap.get(NUMBER_3)+1:1;
                singleAnswerMap.put(NUMBER_3, number3);
                break;
            case NUMBER_4:
                Integer number4 =  singleAnswerMap.containsKey(NUMBER_4) ? singleAnswerMap.get(NUMBER_4)+1:1;
                singleAnswerMap.put(NUMBER_4, number4);
                break;
            case NUMBER_5:
                Integer number5 =  singleAnswerMap.containsKey(NUMBER_5) ? singleAnswerMap.get(NUMBER_5)+1:1;
                singleAnswerMap.put(NUMBER_5, number5);
                break;
            case NUMBER_6:
                Integer number6 =  singleAnswerMap.containsKey(NUMBER_6) ? singleAnswerMap.get(NUMBER_6)+1:1;
                singleAnswerMap.put(NUMBER_6, number6);
                break;
            case NUMBER_7:
                Integer number7 =  singleAnswerMap.containsKey(NUMBER_7) ? singleAnswerMap.get(NUMBER_7)+1:1;
                singleAnswerMap.put(NUMBER_7, number7);
                break;
            case NUMBER_8:
                Integer number8 =  singleAnswerMap.containsKey(NUMBER_8) ? singleAnswerMap.get(NUMBER_8)+1:1;
                singleAnswerMap.put(NUMBER_8, number8);
                break;
            case NUMBER_9:
                Integer number9 =  singleAnswerMap.containsKey(NUMBER_9) ? singleAnswerMap.get(NUMBER_9)+1:1;
                singleAnswerMap.put(NUMBER_9, number9);
                break;
        }
    }
    private static void setCharCount(String result) {
        switch (result) {
            case CHAR_A:
                Integer countA =  singleAnswerMap.containsKey(CHAR_A) ? singleAnswerMap.get(CHAR_A)+1:1;
                singleAnswerMap.put(CHAR_A, countA);
                break;
            case CHAR_B:
                Integer countB =  singleAnswerMap.containsKey(CHAR_B) ? singleAnswerMap.get(CHAR_B)+1:1;
                singleAnswerMap.put(CHAR_B, countB);
                break;
            case CHAR_C:
                Integer countC =  singleAnswerMap.containsKey(CHAR_C) ? singleAnswerMap.get(CHAR_C)+1:1;
                singleAnswerMap.put(CHAR_C, countC);
                break;
            case CHAR_D:
                Integer countD =  singleAnswerMap.containsKey(CHAR_D) ? singleAnswerMap.get(CHAR_D)+1:1;
                singleAnswerMap.put(CHAR_D, countD);
                break;
        }
    }
    public static Map<String, StudentInfo> getStudentInfoMap() {
        return studentInfoMap;
    }
    public static void setStudentInfoMap(Map<String, StudentInfo> studentInfoMap) {
        RedisMapSingleAnswer.studentInfoMap = studentInfoMap;
    }
    public static void clearStudentInfoMap() {
        studentInfoMap.clear();
    }
    public static Map<String, Integer> getSingleAnswerMap() {
        return singleAnswerMap;
    }
    public static void setSingleAnswerMap(Map<String, Integer> singleAnswerMap) {
        RedisMapSingleAnswer.singleAnswerMap = singleAnswerMap;
    }
    public static void clearSingleAnswerMap() {
        singleAnswerMap.clear();
    }
    public static Answer getAnswer() {
        return answer;
    }
    public static void setAnswer(Answer answer) {
        RedisMapSingleAnswer.answer = answer;
    }
    public static Logger getLogger() {
        return logger;
    }
}
