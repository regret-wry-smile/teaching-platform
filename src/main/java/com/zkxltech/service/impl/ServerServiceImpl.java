package com.zkxltech.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ejet.core.util.BusinessException;
import com.ejet.core.util.OkHttpUtils;
import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.constant.Global;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.config.ConfigConstant;
import com.zkxltech.domain.QuestionInfo;
import com.zkxltech.domain.Record;
import com.zkxltech.domain.ResponseTestPaper;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.domain.TestPaper;
import com.zkxltech.service.ServerService;
import com.zkxltech.sql.QuestionInfoSql;
import com.zkxltech.sql.RecordSql;
import com.zkxltech.sql.TestPaperSql;
import com.zkxltech.ui.util.StringUtils;

public class ServerServiceImpl implements ServerService{
	
	private String urlString  = ConfigConstant.serverDbConfig.getServer_url();
	private QuestionInfoSql questionInfoSql = new QuestionInfoSql();
	private TestPaperSql testPaperSql = new TestPaperSql();
	
	@Override
	public Result getTestInfoFromServer(String classId,String subjectName) {
		Result result = new Result();
		try {
			List<JSONObject> testList = new ArrayList<JSONObject>();//保存所有的试卷信息
			//获取服务器上的试卷信息
			StringBuilder params1 = new StringBuilder();
			params1.append("Code=1006&V={\"bjID\":\""+classId+"\",\"SubName\":\""+subjectName+"\"}");
			//[{"id":6845,"xmid":"2Y0002","xm":"test"}]
			String testInfo =  OkHttpUtils.postData(urlString, params1.toString());
			if ("0".equals(testInfo)) {
				result.setRet(Constant.ERROR);
				result.setMessage("从服务器中获取试卷失败！");
				return result;
			}else {
				JSONArray jsonArray = JSONArray.parseArray(testInfo);
				String[] items = new String[jsonArray.size()];
				for (int j = 0; j < jsonArray.size(); j++) {
					JSONObject jsonObject = (JSONObject) jsonArray.get(j);
					items[j] = jsonObject.getString("xmid");
					testList.add(jsonObject);
				}
			}
			result.setRet(Constant.SUCCESS);
			result.setMessage("从服务器中获取试卷！");
			result.setItem(testList);
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("从服务器中获取试卷失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}
	@Override
	public Result getQuestionInfoFromServer(Object object) {
		Result result = new Result();
		try {
			ResponseTestPaper responseTestPaper =  (ResponseTestPaper) StringUtils.parseJSON(object, ResponseTestPaper.class);

			String classId = responseTestPaper.getClassId();
			String codeId = String.valueOf(responseTestPaper.getId());
			String subjectName = responseTestPaper.getSubjectName();
			String testId  = responseTestPaper.getXmid();
			String testName = responseTestPaper.getXm();
			List<JSONObject> testList = new ArrayList<JSONObject>();//保存所有的试卷信息
			//根据codeId获取标准答案
			StringBuilder params = new StringBuilder();
			params.append("Code=1002&V={\"bjID\":\""+classId+"\",\"CodeID\":"+codeId+",\"SubName\":\""+subjectName+"\"}");
			//[{"tno":1,"tanswer":"A","tscore":5.0,"type":0,"atype":0,"partScore":0.0,"highScore":0.0,"downScore":0.0}]
			String answersInfo =  OkHttpUtils.postData(urlString, params.toString());
			if ("0".equals(answersInfo)) {
				result.setRet(Constant.ERROR);
				result.setMessage("从服务器中获取标准答案失败！");
				return result;
			}else {
				testPaperSql.deleteTestPaper(testId,subjectName);//根据试卷id和科目删除原来的试卷

				TestPaper testPaper = new TestPaper();
				testPaper.setTestId(testId);
				testPaper.setAtype("1");
				testPaper.setSubject(subjectName);
				testPaper.setTestName(responseTestPaper.getXm());
				testPaperSql.insertTestPaper(testPaper);
				
				QuestionInfo questionInfo = new QuestionInfo();
				questionInfo.setTestId(testId);
				questionInfoSql.deleteQuestionInfo(questionInfo); //删除原来的题目
				
				result = testPaperSql.saveTitlebyBatch(responseTestPaper.getXmid(), answersInfo);
				if (Constant.ERROR.equals(result.getRet())) {
					result.setMessage("保存服务器中的题目信息失败！");
					return result;
				}
			}
			result.setRet(Constant.SUCCESS);
			result.setMessage("保存服务器中的题目信息成功！");
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("保存服务器中的题目信息失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	
//	public static void main(String[] args) {
//		Result result = new ServerServiceImpl().getTestInfoFromServer("705","语文");
//		System.out.println(JSONObject.toJSONString(result));
//		//{"item":[{"xm":"中天测试","xmid":"4Y0001","id":6843},{"xm":"景县助手器测试","xmid":"T11","id":6842}],"message":"从服务器中获取试卷！","ret":"success"}
//		ResponseTestPaper responseTestPaper = new ResponseTestPaper();
//		responseTestPaper.setClassId("705");
//		responseTestPaper.setId(6843);
//		responseTestPaper.setSubjectName("语文");
//		responseTestPaper.setXm("中天测试");
//		responseTestPaper.setXmid("4Y0001");
//		Result result2 = new ServerServiceImpl().getQuestionInfoFromServer(responseTestPaper);
//		System.out.println(JSONObject.toJSONString(result2));
//		
//	}
	
//	public static void main(String[] args) {
//		/*模拟当前班级的学生信息*/
//		List<StudentInfo> studentInfos = new ArrayList<StudentInfo>();
//		StudentInfo studentInfo = new StudentInfo();
//		studentInfo.setIclickerId("0000001");
//		studentInfo.setStudentId("10001");
//		studentInfo.setStudentName("学号01");
//		studentInfo.setClassId("9999");
//		studentInfos.add(studentInfo);
//		StudentInfo studentInfo2 = new StudentInfo();
//		studentInfo2.setIclickerId("0000002");
//		studentInfo2.setStudentId("10002");
//		studentInfo2.setStudentName("学号02");
//		studentInfo2.setClassId("9999");
//		studentInfos.add(studentInfo2);
//		StudentInfo studentInfo3 = new StudentInfo();
//		studentInfo3.setIclickerId("0000003");
//		studentInfo3.setStudentId("10003");
//		studentInfo3.setStudentName("学号03");
//		studentInfo3.setClassId("9999");
//		studentInfos.add(studentInfo3);
//		Global.setStudentInfos(studentInfos);
//		
//		Result result = new ServerServiceImpl().uploadServer( "4Y0001","语文","9999");
//		System.out.println(net.sf.json.JSONObject.fromObject(result));
//	}
	
	@Override
	public Result uploadServer(Object testId) {
		Result result = new Result();
		try {
//			String testId = "4Y0001";
			String subject = Global.getClassHour().getSubjectName();
			String classId = Global.getClassHour().getClassId();
			// 客观题参数
			List<String> paramList1 = concatParamData1((String)testId, (String)subject, (String)classId);
			System.out.println(JSONArray.toJSONString(paramList1));
			// 主观题参数
			List<String> paramList2 = concatParamData2((String)testId, (String)subject,(String)classId);
			// 发送http请求
			for (int j = 0; j < paramList1.size(); j++) {
				if (!"0".equals(OkHttpUtils.postData(urlString, paramList1.get(j)))) {
					result.setRet(Constant.ERROR);
					result.setMessage("上传失败！");
					return result;
				}
			}
			for (int j = 0; j < paramList2.size(); j++) {
				if (!"0".equals(OkHttpUtils.postData(urlString, paramList2.get(j)))) {
					result.setRet(Constant.ERROR);
					result.setMessage("上传失败！");
					return result;
				}
			}
//			System.out.println(JSONArray.toJSONString(paramList2));
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("上传失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
		return result;
	}
	
	/**
	 * 拼接上传客观题数据
	 */
	/**
	 * 
	 * @param CodeID
	 *            试卷id
	 * @param SubName
	 *            科目
	 * @param BjID
	 *            班级id
	 * @return
	 * @throws BusinessException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private List<String> concatParamData1(String CodeID, String SubName, String BjID) throws BusinessException, IllegalArgumentException, IllegalAccessException {
		List<String> paramsList = new ArrayList<String>();
		List<StudentInfo> studentList = Global.getStudentInfos();
		
		Record recordParam = new Record();
		recordParam.setTestId(CodeID);
		recordParam.setSubject(SubName);
		recordParam.setClassId(BjID);
		Result result = new RecordSql().selectRecord(recordParam);
		if (Constant.ERROR.equals(result.getRet())) {
			throw new BusinessException(Constant.ERROR, "查询作答记录失败!");
		}
		List<Record> list =(List<Record>) result.getItem();
		for (int i = 0; i < studentList.size(); i++) {
			StringBuilder parmas = new StringBuilder();
			parmas.append("Code=1004&&V=");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("SubName", SubName);
			jsonObject.put("CodeID", CodeID);
			jsonObject.put("BjID", BjID);
			String XH = (String) studentList.get(i).getStudentId();// 考号 XH
			String XM = (String) studentList.get(i).getStudentName();// 姓名 XM
			jsonObject.put("XH", XH);
			jsonObject.put("XM", XM);
			// 学生填写的答案 Daxl
			StringBuilder Daxl = new StringBuilder();
			// 学生填写的答案 Daxl2 答案不转换，分隔符用|
			StringBuilder Daxl2 = new StringBuilder();
			// Kgf:客观分
			BigDecimal Kgf = new BigDecimal("0");
			// Zgf:主观分
			BigDecimal Zgf = new BigDecimal("0");
			for (int j = 0; j < list.size(); j++) {
				Record record = list.get(j);
				if (XH.equals(record.getStudentId())) { // 判断是否为该学生的答题信息
					String answer = record.getAnswer();
					String answerType = ""; // A客观题，O主观题
					if (!Constant.ZHUGUANTI_NUM.equals(record.getQuestionType())) { // 客观题的得分就是每题的分值(答对才得分)
						Daxl2.append(answer + "|");
						answerType = "A";
						if ("1".equals(record.getResult())) {
							Kgf = Kgf.add(new BigDecimal(record.getScore()));
						}
						// 答案转换
						if (Constant.DUOXUANTI_NUM.equals(record.getQuestionType())) { // 多选
							Daxl.append(convertAnswer(answer));
						} else {
							Daxl.append(answer);
						}
					} else if (Constant.ZHUGUANTI_NUM.equals(record.getQuestionType())) { // 主观题的得分就是答题器的答案
						if (answer != null && !"".equals(answer)&& !"null".equals(answer)) {
							Zgf = Zgf.add(new BigDecimal(answer));
						}
						answerType = "O";
					}
					// 每题得分 A1,A2,A3 ....
					jsonObject.put(answerType + (j + 1), record.getAnswer());
				}
			}
			jsonObject.put("Daxl", Daxl);
			jsonObject.put("Daxl2", Daxl2.length() > 0 ? Daxl2.substring(0, Daxl2.lastIndexOf("|")) : Daxl);
			jsonObject.put("Kgf", Kgf);
			jsonObject.put("Zgf", Zgf);
			parmas.append(jsonObject.toString());
			paramsList.add(parmas.toString());
		}
		return paramsList;
	}

	/**
	 * 拼接上传主观题数据
	 */
	/**
	 * 
	 * @param CodeID
	 *            试卷id
	 * @param SubName
	 *            科目
	 * @param BjID
	 *            班级id
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws BusinessException 
	 */
	private List<String> concatParamData2(String CodeID, String SubName, String BjID) throws IllegalArgumentException, IllegalAccessException, BusinessException {
		List<String> paramsList = new ArrayList<String>();
		List<StudentInfo> studentList = Global.getStudentInfos();
		List<JSONObject> answerInfoJsonObjects = new ArrayList<JSONObject>();
		Record recordParam = new Record();
		recordParam.setTestId(CodeID);
		recordParam.setSubject(SubName);
		recordParam.setClassId(BjID);
		Result result = new RecordSql().selectRecord(recordParam);
		if (Constant.ERROR.equals(result.getRet())) {
			throw new BusinessException(Constant.ERROR, "查询作答记录失败!");
		}
		List<Record> list =(List<Record>) result.getItem();
		for (int i = 0; i < studentList.size(); i++) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("SubName", SubName);
			jsonObject.put("CodeID", CodeID);
			jsonObject.put("BjID", BjID);
			String XH = (String) studentList.get(i).getStudentId();// 考号 XH
			String XM = (String) studentList.get(i).getStudentName();// 姓名 XM
			jsonObject.put("XH", XH);
			jsonObject.put("XM", XM);

			// Kgf:客观分
			BigDecimal Kgf = new BigDecimal("0");
			// Zgf:主观分
			BigDecimal Zgf = new BigDecimal("0");
			for (int j = 0; j < list.size(); j++) {
				Record record = list.get(j);
				if (XH.equals(record.getStudentId())) { // 判断是否为该学生的答题信息
					String answer = record.getAnswer();
					if (!Constant.ZHUGUANTI_NUM.equals(record.getQuestionType())) { // 客观题的得分就是每题的分值(答对才得分)
						if ("1".equals(record.getResult())) {
							Kgf = Kgf.add(new BigDecimal(record.getScore()));
						}
					} else if (Constant.ZHUGUANTI_NUM.equals(record.getQuestionType())) { // 主观题的得分就是答题器的答案
						if (answer != null && !"".equals(answer) && !"null".equals(answer)) {
							Zgf = Zgf.add(new BigDecimal(answer));
						}
						// 每道主观题得分 O1,O2,O3 ....
						jsonObject.put("O" + (j + 1), record.getAnswer());
					}

				}
			}
			jsonObject.put("Zgf", Zgf);
			jsonObject.put("Zf", Kgf.add(Zgf));
			answerInfoJsonObjects.add(jsonObject);
		}
		/* 按分数降序排序 */
		Collections.sort(answerInfoJsonObjects, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				@SuppressWarnings("unchecked")
				int score1 = ((JSONObject) o1).getInteger("Zf");
				@SuppressWarnings("unchecked")
				int score2 = ((JSONObject) o2).getInteger("Zf");
				if (score1 < score2) {
					return 1;
				} else if (score1 == score2) {
					return 0;
				} else {
					return -1;
				}
			}
		});
		/* 计算名次 */
		int rank = 1;
		for (int i = 0; i < answerInfoJsonObjects.size(); i++) {
			JSONObject jsonObject = answerInfoJsonObjects.get(i);
			if (i != answerInfoJsonObjects.size() - 1) {
				int score1 = jsonObject.getInteger("Zf");
				int score2 = answerInfoJsonObjects.get(i + 1).getInteger("Zf");
				jsonObject.put("Mc", rank);
				if (score1 != score2) {
					rank = i + 2;
				}
			} else {
				jsonObject.put("Mc", rank);
			}
			StringBuilder parmas = new StringBuilder();
			parmas.append("Code=1009&&V=");
			parmas.append(jsonObject);
			paramsList.add(parmas.toString());
		}
		return paramsList;
	}
	
	/* 答案转换 */
	private String convertAnswer(String answer) {
		String answer2 = "";
		switch (answer) {
		case "AB":
			answer2 = "E";
			break;
		case "AC":
			answer2 = "F";
			break;
		case "AD":
			answer2 = "G";
			break;
		case "BC":
			answer2 = "H";
			break;
		case "BD":
			answer2 = "I";
			break;
		case "CD":
			answer2 = "J";
			break;
		case "ABC":
			answer2 = "K";
			break;
		case "ABD":
			answer2 = "L";
			break;
		case "ACD":
			answer2 = "M";
			break;
		case "BCD":
			answer2 = "N";
			break;
		case "ABCD":
			answer2 = "O";
			break;
		default:
			answer2 = "P";
			break;
		}
		return answer2;
	}
}
