package com.ejet.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public static Map<String, Object> bindMap = Collections.synchronizedMap(new HashMap<String, Object>());
    /**答题器id对应的学生*/
    public static Map<Object, List<StudentInfo>> studentInfoMap = Collections.synchronizedMap(new HashMap<>());
    /**单例*/
    private static final RedisMapBind INSTANCE = new RedisMapBind();
    private RedisMapBind() {
    }
    public static RedisMapBind getInstance(){
        return INSTANCE;
    }
    /**绑定状态*/
    private static final String STATE_BIND = "1";
    /***/
    private static final StudentInfoServiceImpl SIS= new StudentInfoServiceImpl();
    /**绑定时用来去除重复的提交*/
    public static Set<String> cardIdSet = new HashSet<>();
    public void addBindMap(String jsonData){
        JSONArray jsonArray = JSONArray.fromObject(jsonData);
        for (Object object : jsonArray) {
            JSONObject jo = JSONObject.fromObject(object);
            String cardId = jo.getString("card_id");
            if (cardIdSet.contains(cardId)) {
                continue;
            }
            cardIdSet.add(cardId);
            //此处list肯定只有一个元素,可以直接get(0);配对前已对数据进行了检查
            StudentInfo studentInfo = studentInfoMap.get(cardId).get(0);
            if (studentInfo != null) {
                Integer accomplish = (Integer)bindMap.get("accomplish");
                if (accomplish == null) {
                    accomplish = 0;
                }
                ++accomplish;
                bindMap.put("studentName", studentInfo.getStudentName()); //
                bindMap.put("accomplish", accomplish);
                bindMap.put("notAccomplish",studentInfoMap.size()-accomplish);
            }
            studentInfo.setStatus(STATE_BIND);
            SIS.updateStudentById(studentInfo);
        }
        BrowserManager.refreshBindCard();
    }
    
	public static String getBindMap(){
		return JSONObject.fromObject(bindMap).toString();
    }
}
