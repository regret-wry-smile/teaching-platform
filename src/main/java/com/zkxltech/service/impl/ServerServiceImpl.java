package com.zkxltech.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ejet.cache.BrowserManager;
import com.ejet.core.util.BusinessException;
import com.ejet.core.util.OkHttpUtils;
import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.constant.Global;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.config.ConfigConstant;
import com.zkxltech.domain.ClassHour;
import com.zkxltech.domain.QuestionInfo;
import com.zkxltech.domain.Record;
import com.zkxltech.domain.ResponseTestPaper;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.domain.TestPaper;
import com.zkxltech.service.ServerService;
import com.zkxltech.sql.QuestionInfoSql;
import com.zkxltech.sql.RecordSql;
import com.zkxltech.sql.StudentInfoSql;
import com.zkxltech.sql.TestPaperSql;
import com.zkxltech.ui.functions.TestPaperFunctionManage;
import com.zkxltech.ui.util.StringUtils;

public class ServerServiceImpl implements ServerService{
	private static final Logger logger = LoggerFactory.getLogger(ServerServiceImpl.class);
	private String urlString  = ConfigConstant.serverDbConfig.getServer_url();
	private QuestionInfoSql questionInfoSql = new QuestionInfoSql();
	private TestPaperSql testPaperSql = new TestPaperSql();
	private RecordSql recordSql = new RecordSql();
	
	@Override
	public Result getTestInfoFromServer(String classId,String subjectName) {
		Result result = new Result();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Result result = new Result();
					List<JSONObject> testList = new ArrayList<JSONObject>();//保存所有的试卷信息
					//获取服务器上的试卷信息
					StringBuilder params1 = new StringBuilder();
					params1.append("Code=1006&V={\"bjID\":\""+classId+"\",\"SubName\":\""+subjectName+"\"}");
					//[{"id":6845,"xmid":"2Y0002","xm":"test"}]
					result = OkHttpUtils.postData(urlString, params1.toString());
					if (Constant.ERROR.equals(result.getRet())) {
						BrowserManager.showMessage(false, result.getMessage());
						return;
					}
					String testInfo =  (String) result.getItem();
					if ("0".equals(testInfo)) {
//						result.setRet(Constant.ERROR);
//						result.setMessage("从服务器中获取试卷失败！");
						BrowserManager.showMessage(false, "从服务器中获取试卷失败！");
						return;
					}else {
						JSONArray jsonArray = JSONArray.parseArray(testInfo);
						String[] items = new String[jsonArray.size()];
						for (int j = 0; j < jsonArray.size(); j++) {
							JSONObject jsonObject = (JSONObject) jsonArray.get(j);
							items[j] = jsonObject.getString("xmid");
							testList.add(jsonObject);
						}
					}
//					result.setRet(Constant.SUCCESS);
//					result.setMessage("从服务器中获取试卷！");
//					result.setItem(testList);
//					return result;
					Map<String, Object> map = new HashMap<>();
					map.put("testInfo", testList);
					BrowserManager.refreTestPaper(net.sf.json.JSONObject.fromObject(map).toString());
					return;
				} catch (Exception e) {
					BrowserManager.showMessage(false, "从服务器中获取试卷失败！");
					logger.error(IOUtils.getError(e));
					return;
//					result.setRet(Constant.ERROR);
//					result.setMessage("从服务器中获取试卷失败！");
//					result.setDetail(IOUtils.getError(e));
//					return result;
				}finally {
					BrowserManager.removeLoading();
				}
			}
		}).start();
		return result;
		
	}
	@Override
	public Result getQuestionInfoFromServer(Object object) {
		Result result = new Result();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Result result = new Result();
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
					result = OkHttpUtils.postData(urlString, params.toString());
					if (Constant.ERROR.equals(result.getRet())) {
						BrowserManager.showMessage(false, result.getMessage());
						return;
					}
					String answersInfo =  (String) result.getItem();
					if ("0".equals(answersInfo)) {
						BrowserManager.showMessage(false, "从服务器中获取标准答案失败！");
						return;
					}else {
						result = testPaperSql.deleteTestPaper(testId,subjectName);//根据试卷id和科目删除原来的试卷
						if (Constant.ERROR.equals(result.getRet())) {
							BrowserManager.showMessage(false, "删除原来的试卷失败!");
							return;
						}
						TestPaper testPaper = new TestPaper();
						testPaper.setTestId(testId);
						testPaper.setAtype("1");
						testPaper.setSubject(subjectName);
						testPaper.setTestName(responseTestPaper.getXm());
						result = testPaperSql.insertTestPaper(testPaper);
						if (Constant.ERROR.equals(result.getRet())) {
							BrowserManager.showMessage(false, "插入试卷信息失败!");
							return;
						}
						QuestionInfo questionInfo = new QuestionInfo();
						questionInfo.setTestId(testId);
						result = questionInfoSql.deleteQuestionInfo(questionInfo); //删除原来的题目
						if (Constant.ERROR.equals(result.getRet())) {
							BrowserManager.showMessage(false, "删除原来的题目失败!");
							return;
						}
						result = testPaperSql.saveTitlebyBatch(responseTestPaper.getXmid(), answersInfo);
						if (Constant.ERROR.equals(result.getRet())) {
							BrowserManager.showMessage(false, "保存服务器中的题目信息失败！");
							return;
						}
					}
					result.setRemak(testId);
					BrowserManager.showMessage(true, "导入成功！");
					BrowserManager.refreTestPaper2();
					return;
				} catch (Exception e) {
					BrowserManager.showMessage(false, "保存服务器中的题目信息失败！");
					logger.error(IOUtils.getError(e));
					return;
				}finally {
					BrowserManager.removeLoading();
				}
			}
		}).start();
		return result;
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
//		StudentInfo studentInfo = new StudentInfo();
//		studentInfo.setClassId("9999");
//		ClassHour classHour = new ClassHour();
//		classHour.setClassHourId("3d123895edc04d748cdc9875bebbba6d");
//		classHour.setSubjectName("语文");
//		classHour.setClassId("9999");
//		Global.setClassId("9999");
//		Global.setClassHour(classHour);
//		
//		try {
//			Global.setStudentInfos((List<StudentInfo>)new StudentInfoSql().selectStudentInfo(studentInfo).getItem());
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		Result result = new ServerServiceImpl().uploadServer( "T11");
//		System.out.println(net.sf.json.JSONObject.fromObject(result));
//	}
	
	@Override
	public Result uploadServer(Object testId) {
		Result result = new Result();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Result result = new Result();
					String subject = Global.getClassHour().getSubjectName();
					String classId = Global.getClassHour().getClassId();
					// 客观题参数
					Map<String, String> paramList1 = concatParamData1((String)testId, (String)subject, (String)classId);
					// 主观题参数
					Map<String, String> paramList2 = concatParamData2((String)testId, (String)subject, (String)classId);
					// 发送http请求
					int sucessSum = 0; //发送成功个数
					int totalSum = paramList1.keySet().size(); //总条数
					String message01 ="";
					List<Record> records1 = new ArrayList<Record>(); //已经上传成功的学生id集合
					for (String studentId : paramList1.keySet()) {
						result = OkHttpUtils.postData(urlString, paramList1.get(studentId));
						if (Constant.ERROR.equals(result.getRet())) {
							//修改记录上传状态
							if (Constant.ERROR.equals(recordSql.updateObjectiveRecord(records1).getRet())) {
								BrowserManager.showMessage(false, "修改记录上传状态失败");
								return;
							};
							BrowserManager.showMessage(false, "上传客观题成功学生人数:"+sucessSum+";失败人数:"+(totalSum-sucessSum));
							return;
						};
						sucessSum++;
						
						Record record = new Record();
						record.setClassId(Global.getClassId());
						record.setSubject(Global.getClassHour().getSubjectName());
						record.setClassHourId(Global.getClassHour().getClassHourId());
						record.setStudentId(studentId);
						record.setTestId((String)testId);
						record.setIsObjectiveUpload(Constant.IS_LOAD_YES);
						records1.add(record);
					}
					//修改记录上传状态
					if (Constant.ERROR.equals(recordSql.updateObjectiveRecord(records1).getRet())) {
						BrowserManager.showMessage(false, "修改记录上传状态失败");
						return;
					};
					message01 = "上传客观题成功学生人数:"+sucessSum+";失败人数:"+(totalSum-sucessSum);
					
					sucessSum = 0; //发送成功个数
					totalSum = paramList2.keySet().size(); //总条数
					String message02 = "";
					List<Record> records2 = new ArrayList<Record>(); //已经上传成功的学生id集合
					for (String studentId : paramList2.keySet()) {
						result = OkHttpUtils.postData(urlString, paramList2.get(studentId));
						if (Constant.ERROR.equals(result.getRet())) {
							//修改记录上传状态
							if (Constant.ERROR.equals(recordSql.updateSubjectiveRecord(records2).getRet())) {
								BrowserManager.showMessage(false, "修改记录上传状态失败");
								return;
							};
							BrowserManager.showMessage(true, message01+"上传主观题成功学生人数:"+sucessSum+";失败人数:"+(totalSum-sucessSum));
							return;
						};
						sucessSum++;
						
						Record record = new Record();
						record.setClassId(Global.getClassId());
						record.setSubject(Global.getClassHour().getSubjectName());
						record.setClassHourId(Global.getClassHour().getClassHourId());
						record.setStudentId(studentId);
						record.setTestId((String)testId);
						record.setIsSubjectiveUpload(Constant.IS_LOAD_YES);
						records2.add(record);
					}
					
					//修改记录上传状态
					if (Constant.ERROR.equals(recordSql.updateSubjectiveRecord(records2).getRet())) {
						BrowserManager.showMessage(false, "修改记录上传状态失败");
						return;
					};
					message02 = "上传主观题成功学生人数:"+sucessSum+";失败人数:"+(totalSum-sucessSum);
					BrowserManager.showMessage(true, message01+message02);
					return;
				} catch (Exception e) {
					BrowserManager.showMessage(false, "上传失败！");
					logger.error(IOUtils.getError(e));
					return;
				}finally {
					BrowserManager.removeLoading();
				}
			}
		}).start();
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
	private Map<String,String> concatParamData1(String CodeID, String SubName, String BjID) throws BusinessException, IllegalArgumentException, IllegalAccessException {
		Map<String,String> retMap = new HashMap<String, String>();
		List<String> paramsList = new ArrayList<String>();
		List<StudentInfo> studentList = Global.getStudentInfos();
		
		Record recordParam = new Record();
		recordParam.setTestId(CodeID);
		recordParam.setSubject(SubName);
		recordParam.setClassId(BjID);
		Result result = new RecordSql().selectRecord(recordParam);
		if (Constant.ERROR.equals(result.getRet())) {
			throw new BusinessException(Constant.ERROR, "查找做到记录失败！");
		}
		List<Record> list =(List<Record>) result.getItem();

		
		String studentId ;
		for (int i = 0; i < studentList.size(); i++) {
			studentId = studentList.get(i).getStudentId();
			StringBuilder parmas = new StringBuilder();
			parmas.append("Code=1004&&V=");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("SubName", SubName);
			jsonObject.put("CodeID", CodeID);
			jsonObject.put("BjID", BjID);
			String KH = (String) studentList.get(i).getStudentId();// 考号 XH
			String XM = (String) studentList.get(i).getStudentName();// 姓名 XM
			jsonObject.put("KH", KH);
			jsonObject.put("XM", XM);
			// 学生填写的答案 Daxl
			StringBuilder Daxl = new StringBuilder();
			// 学生填写的答案 Daxl2 答案不转换，分隔符用|
			StringBuilder Daxl2 = new StringBuilder();
			// Kgf:客观分
			BigDecimal Kgf = new BigDecimal("0");
			// Zgf:主观分
			BigDecimal Zgf = new BigDecimal("0");
			
			String isLoad = Constant.IS_LOAD_NO;
			
			for (int j = 0; j < list.size(); j++) {
				Record record = list.get(j);
				if (Constant.IS_LOAD_YES.equals(record.getIsObjectiveUpload())) {
					isLoad = Constant.IS_LOAD_YES;
					break;
				}
				if (KH.equals(record.getStudentId())) { // 判断是否为该学生的答题信息
					String answer = record.getAnswer();
					if (StringUtils.isEmpty(answer)) {
						answer = "";
					}
					String answerType = ""; // A客观题，O主观题
					if (!Constant.ZHUGUANTI_NUM.equals(record.getQuestionType())) { // 客观题的得分就是每题的分值(答对才得分)
						Daxl2.append(answer + "|");
						answerType = "A";
						if ("1".equals(record.getResult())) {
						    if (!StringUtils.isEmpty(record.getScore())) {
						        Kgf = Kgf.add(new BigDecimal(record.getScore()));
                            }
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
			if (Constant.IS_LOAD_NO.equals(isLoad)) { //如果没有上传则加入待上传数据中
				jsonObject.put("Daxl", Daxl);
				jsonObject.put("Daxl2", Daxl2.length() > 0 ? Daxl2.substring(0, Daxl2.lastIndexOf("|")) : Daxl);
				jsonObject.put("Kgf", Kgf);
				jsonObject.put("Zgf", Zgf);
				parmas.append(jsonObject.toString());
				paramsList.add(parmas.toString());
				retMap.put(studentId, parmas.toString());
			}
		}
//		return paramsList;
		return retMap;
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
	private Map<String,String> concatParamData2(String CodeID, String SubName, String BjID) throws IllegalArgumentException, IllegalAccessException, BusinessException {
		Map<String,String> retMap = new HashMap<String, String>();
//		List<String> paramsList = new ArrayList<String>();
		List<StudentInfo> studentList = Global.getStudentInfos();
		List<JSONObject> answerInfoJsonObjects = new ArrayList<JSONObject>();
		
		for (int i = 0; i < studentList.size(); i++) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("SubName", SubName);
			jsonObject.put("CodeID", CodeID);
			jsonObject.put("BjID", BjID);
			String KH = (String) studentList.get(i).getStudentId();// 考号 XH
			String XM = (String) studentList.get(i).getStudentName();// 姓名 XM
			jsonObject.put("KH", KH);
			jsonObject.put("XM", XM);

			// Kgf:客观分
			BigDecimal Kgf = new BigDecimal("0");
			// Zgf:主观分
			BigDecimal Zgf = new BigDecimal("0");
			
			
			Record recordParam = new Record();
			recordParam.setTestId(CodeID);
			recordParam.setSubject(SubName);
			recordParam.setClassId(BjID);
			recordParam.setStudentId(KH);
			recordParam.setIsSubjectiveUpload(Constant.IS_LOAD_NO);
			Result result = new RecordSql().selectRecord(recordParam);
			if (Constant.ERROR.equals(result.getRet())) {
				throw new BusinessException(Constant.ERROR, "查询作答记录失败!");
			}

			List<Record> list =(List<Record>) result.getItem();
			String isLoad ;
			if (StringUtils.isEmptyList(list)) {
				isLoad = Constant.IS_LOAD_YES;
			}else {
				isLoad = Constant.IS_LOAD_NO;
			}
			for (int j = 0; j < list.size(); j++) {
				Record record = list.get(j);
				if (Constant.IS_LOAD_YES.equals(record.getIsSubjectiveUpload())) {
					isLoad = Constant.IS_LOAD_YES;
					break;
				}
				
				if (KH.equals(record.getStudentId())) { // 判断是否为该学生的答题信息
					String answer = record.getAnswer();
					if (!Constant.ZHUGUANTI_NUM.equals(record.getQuestionType())) { // 客观题的得分就是每题的分值(答对才得分)
						if (Constant.RESULT_TRUE.equals(record.getResult())) {
							if (!StringUtils.isEmpty(record.getScore())) {
								Kgf = Kgf.add(new BigDecimal(record.getScore()));
                            }
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
			if (Constant.IS_LOAD_NO.equals(isLoad)) {
				answerInfoJsonObjects.add(jsonObject);
			}
			
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
//			paramsList.add(parmas.toString());
			retMap.put(jsonObject.getString("KH"), parmas.toString());
		}
//		return paramsList;
		return retMap;
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
