package com.zkxltech.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ejet.cache.RedisMapAttendance;
import com.ejet.cache.RedisMapClassTestAnswer;
import com.ejet.cache.RedisMapMultipleAnswer;
import com.ejet.cache.RedisMapSingleAnswer;
import com.ejet.core.util.constant.Constant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

class MyThread extends Thread {
	private String jsonData;
	private String type;
	private int studentNum;
	private int questionNum;
	
	private List<String> answers ;
	
	private List<Map<String, String>> studentInfos;
	
	private static boolean flag = true;
	
	
	
	public static void setFlag(boolean flag) {
		MyThread.flag = flag;
	}
	public MyThread(int questionNum,String type,boolean flag) {
		this.questionNum = questionNum;
		this.type = type;
		this.flag = flag;
	}
	/**
	 * 初始化学生数据
	 */
	private void initStudent(){
		studentInfos = new ArrayList<Map<String, String>>();
		int studentId = 10000001,iclickerId = 1;
		
		for (int i = 0; i < 120; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("studentId", String.valueOf(studentId+i)); //初始化学生id 10000001-10000120
			map.put("iclickerId","666666"+String.format("%04d", iclickerId+i));//初始化答题器id 6666660001-6666660120
			studentInfos.add(map);
		}
	}
	
	private void initAnswer(String type){
		answers = new ArrayList<String>();
		switch (type) {
		case "字母题":
			answers.add("");
			answers.add("A");
			answers.add("B");
			answers.add("C");
			answers.add("D");
			break;
		case "判断题":
			answers.add("true");
			answers.add("false");
			answers.add("");
			break;
		case "多选题":
			answers.add("");
			answers.add("AB");
			answers.add("BD");
			answers.add("CF");
			answers.add("DE");
			answers.add("AC");
			answers.add("BE");
			answers.add("CD");
			answers.add("BC");
			break;	
		case "数字题":
//			answers.add("");
			answers.add("1");
			answers.add("2");
			answers.add("3");
			answers.add("4");
			answers.add("5");
			answers.add("6");
			answers.add("7");
			answers.add("8");
			answers.add("9");
			break;
		default:
			break;
		}
		
		
		
		
	}
	
	/*随机生成答案*/
	private String randomData(){
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("method", method);
		// String jsonData =
		// {"method":"answer","data":[{"fun":"update_answer_list","card_id":"0691725178","update_time":"2018-04-13
		// 14:19:37:175",
		// "answers":[{"type":"s","id":"1","answer":"C"},{"type":"s","id":"2","answer":"B"},{"type":"s","id":"3","answer":""},{"type":"s","id":"4","answer":""},{"type":"s","id":"5","answer":""},{"type":"s","id":"6","answer":""},{"type":"s","id":"7","answer":""},{"type":"s","id":"8","answer":""},{"type":"s","id":"9","answer":""},{"type":"s","id":"10","answer":""},{"type":"s","id":"11","answer":""},{"type":"s","id":"12","answer":""},{"type":"s","id":"13","answer":""},{"type":"s","id":"14","answer":""},{"type":"s","id":"15","answer":""},{"type":"s","id":"16","answer":""},{"type":"s","id":"17","answer":""},{"type":"s","id":"18","answer":""},{"type":"s","id":"19","answer":""},{"type":"s","id":"20","answer":""},{"type":"m","id":"21","answer":""},{"type":"m","id":"22","answer":""},{"type":"m","id":"23","answer":""},{"type":"m","id":"24","answer":""},{"type":"m","id":"25","answer":""},{"type":"m","id":"26","answer":""},{"type":"m","id":"27","answer":""},{"type":"m","id":"28","answer":""},{"type":"m","id":"29","answer":""},{"type":"m","id":"30","answer":""},{"type":"m","id":"31","answer":""},{"type":"m","id":"32","answer":""},{"type":"m","id":"33","answer":""},{"type":"m","id":"34","answer":""},{"type":"m","id":"35","answer":""},{"type":"m","id":"36","answer":""},{"type":"m","id":"37","answer":""},{"type":"m","id":"38","answer":""},{"type":"m","id":"39","answer":""},{"type":"m","id":"40","answer":""},{"type":"j","id":"41","answer":""},{"type":"j","id":"42","answer":""},{"type":"j","id":"43","answer":""},{"type":"j","id":"44","answer":""},{"type":"j","id":"45","answer":""},{"type":"j","id":"46","answer":""},{"type":"j","id":"47","answer":""},{"type":"j","id":"48","answer":""},{"type":"j","id":"49","answer":""},{"type":"j","id":"50","answer":""},{"type":"j","id":"51","answer":""},{"type":"j","id":"52","answer":""},{"type":"j","id":"53","answer":""},{"type":"j","id":"54","answer":""},{"type":"j","id":"55","answer":""},{"type":"j","id":"56","answer":""},{"type":"j","id":"57","answer":""},{"type":"j","id":"58","answer":""},{"type":"j","id":"59","answer":""},{"type":"j","id":"60","answer":""},{"type":"d","id":"61","answer":""},{"type":"d","id":"62","answer":""},{"type":"d","id":"63","answer":""},{"type":"d","id":"64","answer":""},{"type":"d","id":"65","answer":""},{"type":"d","id":"66","answer":""},{"type":"d","id":"67","answer":""},{"type":"d","id":"68","answer":""},{"type":"d","id":"69","answer":""},{"type":"d","id":"70","answer":""},{"type":"d","id":"71","answer":""},{"type":"d","id":"72","answer":""},{"type":"d","id":"73","answer":""},{"type":"d","id":"74","answer":""},{"type":"d","id":"75","answer":""},{"type":"d","id":"76","answer":""},{"type":"d","id":"77","answer":""},{"type":"d","id":"78","answer":""},{"type":"d","id":"79","answer":""},{"type":"d","id":"80","answer":""}]}]}
		JSONArray data = new JSONArray();
		
		
		studentNum = ToolHelper.randomInt(1, 120); //随机生成同时上传的学生个数最大120
		
		initStudent(); //初始化学生数据
		initAnswer(type);
		
		//随机生成上传的学生
		int[] students = ToolHelper.randomCommon(0, studentInfos.size(), studentNum);

		for (int i = 0; i < studentNum; i++) {
			Map<String, String> studentInfo = studentInfos.get(ToolHelper.randomInt(1, studentInfos.size())-1);
			JSONObject answer = new JSONObject();
			answer.put("fun", "update_answer_list");
			answer.put("card_id", studentInfo.get("iclickerId"));
			answer.put("update_time", "2018-04-13 14:19:37:175");
			JSONArray jsonArray = new JSONArray(); // 每个答题器的所有答案信息
			for (int j = 0; j < questionNum; j++) {
				JSONObject answerDetail = new JSONObject();
				answerDetail.put("type", "d");
				answerDetail.put("id", String.valueOf(j + 1));
				answerDetail.put("answer", answers.get(ToolHelper.randomInt(1, answers.size())-1));
				jsonArray.add(answerDetail);
			}
			answer.put("answers", jsonArray);
			data.add(answer);
		}
		
		

//		jsonObject.put("data", data);
		
		return data.toString();
	}
	
	@Override
	public void run() {
		while (flag) {
			jsonData = randomData();

			System.out.println("同时上传学生个数:"+studentNum);
			try {
				 Thread.sleep(50);
			} catch (InterruptedException e) {
			}
			// 循环发送数据
			if (!"".equals(jsonData)) {
				switch (type) {
				case "多选题":
					/*多选*/
					RedisMapMultipleAnswer.addEveryAnswerInfo(jsonData);
					break;
				case "字母题":
//					RedisMapClassTestAnswer.addRedisMapClassTestAnswer1(jsonData); //随堂检测
					 jsonData = "[{'fun':'update_answer_list','card_id':'3429830485','rssi':'-59','update_time':'2018-07-13 16:10:37:816','answers':[{'type':'m','id':'1','answer':''}]},{'fun':'update_answer_list','card_id':'3429555765','rssi':'-75','update_time':'2018-07-13 16:10:37:826','answers':[{'type':'m','id':'1','answer':''}]},{'fun':'update_answer_list','card_id':'3429555829','rssi':'-80','update_time':'2018-07-13 16:10:37:851','answers':[{'type':'m','id':'1','answer':''}]},{'fun':'update_answer_list','card_id':'3430198741','rssi':'-77','update_time':'2018-07-13 16:10:37:882','answers':[{'type':'m','id':'1','answer':''}]},{'fun':'update_answer_list','card_id':'3429470117','rssi':'-80','update_time':'2018-07-13 16:10:37:909','answers':[{'type':'m','id':'1','answer':''}]}],";
					RedisMapAttendance.addAttendance(jsonData); //签到
//					RedisMapSingleAnswer.addAnswer(jsonData);
					break;
				case "数字题":
					RedisMapClassTestAnswer.addRedisMapClassTestAnswer2(jsonData);
					break;
				default:
					break;
				}
			}

		}
	}
}
