package com.zkxltech.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ejet.core.util.OkHttpUtils;
import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.config.ConfigConstant;
import com.zkxltech.domain.QuestionInfo;
import com.zkxltech.domain.ResponseTestPaper;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.TestPaper;
import com.zkxltech.service.ServerService;
import com.zkxltech.sql.QuestionInfoSql;
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

	
	public static void main(String[] args) {
		Result result = new ServerServiceImpl().getTestInfoFromServer("705","语文");
		System.out.println(JSONObject.toJSONString(result));
		//{"item":[{"xm":"中天测试","xmid":"4Y0001","id":6843},{"xm":"景县助手器测试","xmid":"T11","id":6842}],"message":"从服务器中获取试卷！","ret":"success"}
		ResponseTestPaper responseTestPaper = new ResponseTestPaper();
		responseTestPaper.setClassId("705");
		responseTestPaper.setId(6843);
		responseTestPaper.setSubjectName("语文");
		responseTestPaper.setXm("中天测试");
		responseTestPaper.setXmid("4Y0001");
		Result result2 = new ServerServiceImpl().getQuestionInfoFromServer(responseTestPaper);
		System.out.println(JSONObject.toJSONString(result2));
		
	}

}
