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
import com.zkxltech.service.impl.StudentInfoServiceImpl;
import com.zkxlteck.thread.QuickThread;

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
	private static boolean flag = false;
    public static void addQuickAnswer(String jsonData){
        if (flag) {
            JSONArray jsonArray = JSONArray.fromObject(jsonData);
            for (Object object : jsonArray) {
                JSONObject jo = JSONObject.fromObject(object);
                String card_id = jo.getString("card_id");
                StudentInfo studentInfo = studentInfoMap.get(card_id);
                quickMap.put("studentName", studentInfo.getStudentName());
                if (StudentInfoServiceImpl.getThread()!=null && StudentInfoServiceImpl.getThread() instanceof QuickThread) {
                    QuickThread qt= (QuickThread)StudentInfoServiceImpl.getThread();
                    qt.setFLAG(false);
                    logger.info("抢答线程停止成功");
                }else{
                    logger.error("抢答线程停止失败");
                }
                EquipmentServiceImpl.getInstance().answer_stop();
                flag = false ;
            }
        }
    }
    public static String getQuickAnswer(){
        return JSONObject.fromObject(quickMap).toString();
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
    public static String setFlagStartQuick(){
        flag = true;
        Result r = new Result();
        r.setRet(Constant.SUCCESS);
        r.setMessage("设置抢答成功");
        return JSONObject.fromObject(r).toString();
    }
}
