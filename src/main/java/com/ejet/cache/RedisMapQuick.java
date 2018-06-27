package com.ejet.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.constant.Constant;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.service.impl.EquipmentServiceImpl;
import com.zkxlteck.scdll.QuickThread;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 抢答相关
 * @author zhouwei
 *
 */
public class RedisMapQuick {
	private static final Logger logger = LoggerFactory.getLogger(RedisMapQuick.class);
	private static Map<String,String> quickMap = Collections.synchronizedMap(new HashMap<>());
	/**学生信息*/
	private static Map<String,StudentInfo> studentInfoMap = new HashMap<>(); 
	
    public static void addQuickAnswer(String jsonData){
        JSONArray jsonArray = JSONArray.fromObject(jsonData);
        for (Object object : jsonArray) {
            JSONObject jo = JSONObject.fromObject(object);
            String card_id = jo.getString("card_id");
            StudentInfo studentInfo = studentInfoMap.get(card_id);
            quickMap.put("studentName", studentInfo.getStudentName());
            EquipmentServiceImpl instance = EquipmentServiceImpl.getInstance();
            Result r = instance.answerStop();
            if (r.getRet().equals(Constant.ERROR)) {
                logger.error("抢答环节:关闭停止答题失败");
            }
            if (instance.t!=null && instance.t instanceof QuickThread) {
                QuickThread qt= (QuickThread)instance.t;
                qt.setFLAG(false);
            }
            if (Constant.ERROR.equals(r.getRet())) {
                logger.error("-------- 停止答题线程失败 --------");
            }
        }
    }
    public static String getQuickAnswer(){
        return quickMap.toString();
    }
    public static Map<String, String> getQuickMap() {
        return quickMap;
    }
    public static void setQuickMap(Map<String, String> quickMap) {
        RedisMapQuick.quickMap = quickMap;
    }
    public static Map<String, StudentInfo> getStudentInfoMap() {
        return studentInfoMap;
    }
    public static void setStudentInfoMap(Map<String, StudentInfo> studentInfoMap) {
        RedisMapQuick.studentInfoMap = studentInfoMap;
    }
    public static void clearQuickMap() {
        quickMap.clear();
    }
    public static void clearStudentInfoMap() {
        studentInfoMap.clear();
    }
}
