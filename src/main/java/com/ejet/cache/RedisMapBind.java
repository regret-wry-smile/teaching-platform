package com.ejet.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.comm.ListUtils;
import com.zkxltech.domain.StudentInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author: ZhouWei
 * @date:2018年6月25日 下午5:18:24
 */
public class RedisMapBind {
    private static final Logger logger = LoggerFactory.getLogger(RedisMapBind.class);
    
    /**
     * 一键配对缓存
     */
    private  static Map<String, Object> bindMap = Collections.synchronizedMap(new HashMap<String, Object>());
//  private static String[] keyClassTestAnswerMap = {"iclickerId","questionId"};
    /**答题器id对应的学生*/
    public static Map<Object, List<StudentInfo>> studentInfoMap = null ;
    
    public static void addBindMap(String jsonData){
        
        JSONArray jsonArray = JSONArray.fromObject(jsonData);
        for (Object object : jsonArray) {
            JSONObject jo = JSONObject.fromObject(object);
            String cardId = jo.getString("card_id");
            List<StudentInfo> list = studentInfoMap.get(cardId);
            if (!ListUtils.isEmpty(list)) {
                //此处list肯定只有一个元素,可以直接get(0);配对前已对数据进行了检查
                Integer accomplish = (Integer)bindMap.get("accomplish");
                if (accomplish == null) {
                    accomplish = 0;
                }
                ++accomplish;
                bindMap.put("studentName", list.get(0).getStudentName()); //
                bindMap.put("accomplish", accomplish);
                bindMap.put("notAccomplish",studentInfoMap.size()-accomplish);
            }
        }
    }
}
