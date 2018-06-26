package com.ejet.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.constant.Constant;
import com.zkxltech.domain.Result;
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
	public static Map<String,String> quickMap = Collections.synchronizedMap(new HashMap<>());
	/**学生信息*/
	public static Map<String,String> studentInfoMap = new HashMap<>(); 
    public static void addQuickAnswer(String jsonData){
        JSONArray jsonArray = JSONArray.fromObject(jsonData);
        for (Object object : jsonArray) {
            JSONObject jo = JSONObject.fromObject(object);
            String card_id = jo.getString("card_id");
            String studentName = studentInfoMap.get(card_id);
            quickMap.put("studentName", studentName);
            EquipmentServiceImpl instance = EquipmentServiceImpl.getInstance();
            Result r = instance.answerStop();
            if (instance.t!=null && instance.t instanceof QuickThread) {
                QuickThread qt= (QuickThread)instance.getInstance().t;
                qt.setFLAG(false);
            }
            if (Constant.ERROR.equals(r.getRet())) {
                logger.error("-------- 停止答题失败 --------");
            }
        }
    }
}
