package com.ejet.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.RedisMapUtil;
import com.zkxltech.config.Global;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.teaching.AnswerVO;
import com.zkxltech.teaching.msg.AnswerResponse;

public class TeachingCache {
	private static final Logger logger = LoggerFactory.getLogger(TeachingCache.class);
	/**
	 *  统计单个班级答题信息
	 */
	private static Map<String, Object> global = new HashMap<String,Object>();
	/**
	 * 答案对象（各个答题答案进行区分）
	 */
	private  static Map<String, Object> answerMap = Collections.synchronizedMap(new HashMap<String, Object>());
	/**
	 * 答题最快的对象(3)
	 */
	private static List<AnswerVO> fastMap = new ArrayList<AnswerVO>(3);
	/**
	 * 未绑定的名单
	 */
	private static Map<String, Object> unBindMap = Collections.synchronizedMap(new HashMap<String, Object>());
	/**
	 * 已经绑定的名单
	 */
	private static Map<String, Object> bindMap = Collections.synchronizedMap(new HashMap<String, Object>()); 
	/**
	 * 校验是否重复RSSI指令信息
	 */
	private static Map<String, Object> rssiMap = Collections.synchronizedMap(new HashMap<String, Object>()); 
	/**
	 * 修改答案keys
	 */
	private static String[] keys = {"answerType", "classId", "studentId", "questionId" };
	private static String[] answerKey = {"answerType", "answer"};  // 根据答题类型、答案
	/**
	 * 获取所有绑定的人员信息
	 */
	private static Map<String, Integer> allBind = Collections.synchronizedMap(new HashMap<String, Integer>());
	/**
	 * 保存答题信息
	 * 
	 * @param node
	 */
	public static void addAnswer(AnswerResponse node) {
		String rssi = node.getIclickerId();
		if(rssiMap.containsKey(rssi)) {
			logger.info("答题器指令重复! id: {}", node.getIclickerId());
			return;
		}
		//保存所有答题信息
		keys[0] = node.getAnswerType();
		keys[1] = node.getClassId();
		keys[2] = node.getIclickerId();
		keys[3] = node.getQuestionId();
		RedisMapUtil.setRedisMap(global, keys, 0, node);
		
		AnswerVO vo = new AnswerVO();
		vo.setAnswerType(node.getAnswerType());
		vo.setAnswer(node.getAnswer());
		vo.setAnswerDateTime(node.getAnswerDateTime());
		vo.setIclickerId(node.getIclickerId());
		
		StudentInfo info = getBindStudentByIchickerId(node.getIclickerId());
		if(info==null) {
			logger.info("答题器平台未绑定! id: {}", node.getIclickerId());
			return;
		}
		vo.setClassName(info.getClassName());
		vo.setStudentName(info.getStudentName());
		
		//保存rssi中
		rssiMap.put(rssi, vo);
		
		//根据答案保存答题信息
		answerKey[0] = node.getAnswerType();
		answerKey[1] = node.getAnswer();
		List<AnswerVO> list = (List<AnswerVO>) RedisMapUtil.getRedisMap(answerMap, answerKey, 0);
		if(list==null) {
			list = new ArrayList<AnswerVO>();
		}
		list.add(vo);
		RedisMapUtil.setRedisMap(answerMap, answerKey, 0, list);
		
		if(fastMap==null || fastMap.size()<3) {
			fastMap.add(vo);
			Collections.sort(fastMap);
		} else {
			if (fastMap.get(2).getAnswerDateTime().compareTo(vo.getAnswerDateTime())>0) {
				fastMap.set(2,vo);
			};
		}
		
		logger.info("排名：" + fastMap);
		logger.info("答案：" + answerMap);
		
	}
	
	
	/**
	 * 保存答题信息
	 * @param node
	 */
	public static boolean addAnswerRep(AnswerVO node) {
		String rssi = node.getIclickerId();
		if(rssiMap.containsKey(rssi)) {
			logger.info("答题器指令重复! id: {}", node.getIclickerId());
			return false;
		}
		StudentInfo stu = null;
		if(!Global.isTeacher()) {
			stu = getBindStudentByIchickerId(node.getIclickerId());
			if(stu==null) {
				logger.info("答题器平台未绑定! id: {}", node.getIclickerId());
				return false;
			}
			node.setClassName(stu.getClassName());
			node.setStudentName(stu.getStudentName());
		}
		//保存所有答题信息
		keys[0] = node.getAnswerType();
		keys[1] = node.getClassId();
		keys[2] = node.getIclickerId();
		keys[3] = node.getQuestionId();
		RedisMapUtil.setRedisMap(global, keys, 0, node);
		
		//保存rssi中
		rssiMap.put(rssi, node);
		
		//根据答案保存答题信息
		answerKey[0] = node.getAnswerType();
		answerKey[1] = node.getAnswer();
		List<AnswerVO> list = (List<AnswerVO>) RedisMapUtil.getRedisMap(answerMap, answerKey, 0);
		if(list==null) {
			list = new ArrayList<AnswerVO>();
		}
		list.add(node);
		RedisMapUtil.setRedisMap(answerMap, answerKey, 0, list);
		
		if(fastMap==null || fastMap.size()<3) {
			fastMap.add(node);
			Collections.sort(fastMap);
		} else {
			if (fastMap.get(2).getAnswerDateTime().compareTo(node.getAnswerDateTime())>0) {
				fastMap.set(2,node);
			};
		}
		logger.info("排名：" + fastMap);
		logger.info("答案：" + answerMap);
		
		node.setBindSize(bindMap.size()); //答案中包含绑定人员名单数量
		
		if(Global.isTeacher()) { //如果是教师端
			allBind.put(node.getClassId(), node.getBindSize());
		}
		return true;
	}
	
	/**
	 * 未绑定的名单
	 * 
	 * @param studentInfo
	 */
	public static void addNoBindStudent(StudentInfo studentInfo) {
		String stuId = studentInfo.getStudentId();
		unBindMap.put(stuId, studentInfo);
	}
	/**
	 * 绑定名单信息缓存
	 */
	public static void addBindStudent(StudentInfo stu) {
		if(stu!=null && stu.getIclickerId()!=null) {
			bindMap.put(stu.getIclickerId(), stu);
		}
	}
	
	/**
	 * 取已经绑定(根据答题器编号)
	 */
	public static StudentInfo getBindStudentByIchickerId(String ichickerId) {
		StudentInfo stu = null;
		if(ichickerId!=null) {
			stu = (StudentInfo) bindMap.get(ichickerId);
		}
		return stu;
	}
	
	/**
	 * 获取名单进行绑定
	 */
	public static StudentInfo getStudentForBind() {
		Iterator it = unBindMap.entrySet().iterator();
		StudentInfo value = null;
		while(it.hasNext()) {
			Map.Entry<String, StudentInfo> entry = (Entry<String, StudentInfo>) it.next();
			String key = entry.getKey();
			value = (StudentInfo) unBindMap.get(key);
			it.remove();
			if(value!=null) {
				break;
			}
		}
		return value;
	}
	
	/**
	 * 获得学生总人数
	 * 
	 * @return
	 */
	public static int getTotalStudent() {
		int size = unBindMap.size();
		if(Global.isTeacher()) { //如果是教师端
			Iterator it = allBind.entrySet().iterator();
			Integer value = 0;
			while(it.hasNext()) {
				Map.Entry<String, Integer> entry = (Entry<String, Integer>) it.next();
				String key = entry.getKey();
				value += allBind.get(key);
			}
			size = value;
		}
		return size;
	}

	/**
	 * 清空缓存信息
	 */
	public static void cleanAnswerCache() {
		answerMap.clear();
		fastMap.clear();
		global.clear();
		rssiMap.clear();
	}
	
	/**
	 * 清空绑定缓存信息
	 */
	public static void cleanBindCache() {
		unBindMap.clear();
		bindMap.clear();
	}
	
	/**
	 * 获取已答题人数
	 */
	public static int getAnswerNum(){
		int size = 0;
		for (String answerType: answerMap.keySet()) {
			Map<String, Object> map = (Map<String, Object>) answerMap.get(answerType);
			for (String answer : map.keySet()) {
				size = size + ((List<Object>)map.get(answer)).size();
			}
		}
		return	size;
	}
	
	/**
	 * 获得绑定人数
	 * 
	 * @return
	 */
	public static int getBindSize() {
		return bindMap.size();
	}
	
	/**
	 * 获得未绑定人数
	 * 
	 * @return
	 */
	public static int getUnBindSize() {
		return unBindMap.size();
	}
	
	
	/**
	 * 获得答题信息缓存
	 * 
	 * @return
	 */
	public static Map<String, Object> getAnswerMap() {
		return answerMap;
	}
	
	/**
	 * 获得答题信息缓存
	 * 
	 * @return
	 */
	public static List<AnswerVO> getFastMap() {
		return fastMap;
	}
	
	
	
}
