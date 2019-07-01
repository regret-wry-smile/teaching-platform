package com.zkxltech.ui.functions;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.cache.RedisMapMultipleAnswer;
import com.ejet.cache.RedisMapPaper;
import com.ejet.core.util.StringUtils;
import com.ejet.core.util.constant.Constant;
import com.zkxltech.domain.Result;
import com.zkxltech.service.QuestionService;
import com.zkxltech.service.ServerService;
import com.zkxltech.service.StudentInfoService;
import com.zkxltech.service.TestPaperService;
import com.zkxltech.service.impl.QuestionServiceImpl;
import com.zkxltech.service.impl.ServerServiceImpl;
import com.zkxltech.service.impl.StudentInfoServiceImpl;
import com.zkxltech.service.impl.TestPaperServiceImpl;
import com.zkxltech.ui.util.StringConstant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 【试卷模块页面调用方法】
 *	import_paper 导入试卷
 */
public class TestPaperFunctionManage extends BrowserFunction{
	private TestPaperService testPaperService = new TestPaperServiceImpl();
	private QuestionService questionService = new QuestionServiceImpl();
	private ServerService serverService = new ServerServiceImpl();
	public TestPaperFunctionManage(Browser browser, String name) {
		super(browser, name);
	}
	@Override
	public Object function(Object[] params) {
		Result result = new Result();
		if (params.length>0) {
			String method = (String) params[0]; //页面要调用的方法
			switch (method) {
			case "create_test_id":
				return StringUtils.getUUID();
			case "get_subject":
				String[] subjects = StringConstant.SUBJECTS;
				return JSONArray.fromObject(subjects).toString();
			case "select_paper":
				result = testPaperService.selectTestPaper(params[1]);
				break;
			case "select_paper_by_classHourId":
			    result = testPaperService.selectTestPaperByClassHourId(params[1]);
			    break;
			case "select_now_paper":
				result = testPaperService.selectNowTestPaper(params[1]);
				break;
			case "select_paper_server":
				/*服务器查询试卷列表*/
				if (params.length != 3) {
					result.setRet(Constant.ERROR);
					result.setMessage("参数个数有误！");
					break;
				}
				result = serverService.getTestInfoFromServer((String)params[1], (String)params[2]);
				break;
			case "import_paper_server":
				/*服务器导入试卷*/
				if (params.length != 2) {
					result.setRet(Constant.ERROR);
					result.setMessage("参数个数有误！");
					break;
				}
				result = serverService.getQuestionInfoFromServer((String)params[1]);
				break;
			case "import_paper":
				if (params.length != 2) {
					result.setRet(Constant.ERROR);
					result.setMessage("参数个数有误！");
					break;
				}
				result = testPaperService.importTestPaper(params[1]);
				break;
			case "insert_paper":
				if (params.length != 2) {
					result.setRet(Constant.ERROR);
					result.setMessage("参数个数有误！");
					break;
				}
				result = testPaperService.insertTestPaper(params[1]);
				break;
			case "delete_paper":
				if (params.length != 2) {
					result.setRet(Constant.ERROR);
					result.setMessage("参数个数有误！");
					break;
				}
				result = testPaperService.deleteTestPaper(params[1]);
				break;
			case "update_paper":
				if (params.length != 2) {
					result.setRet(Constant.ERROR);
					result.setMessage("参数个数有误！");
					break;
				}
				result = testPaperService.updateTestPaper(params[1]);
				break;
			case "select_question":
				/*从数据库中查询*/
				if (params.length != 2) {
					result.setRet(Constant.ERROR);
					result.setMessage("参数个数有误！");
					break;
				}
				result = questionService.selectQuestion(params[1]);
				break;
			case "insert_question":
				result = questionService.insertQuestion(params[1]);
				break;
			case "insert_questions":
				result = questionService.insertQuestions(params[1]);
				break;
			case "update_question":
				result = questionService.updateQuestion(params[1]);
				break;
//			case "clear_question_redis":
//				/*清空题目缓存*/
//				RedisMapPaper.clearRedis();
//				break;
//			case "select_question_redis":
//				/*从缓存中查询*/
//				result = questionService.selectQuestionedis(params[1]);
//				break;
//			case "add_question_redis":
//				/*添加题目到缓存*/
//				result = questionService.insertQuestion(params[1]);
//				break;
//			case "update_question_redis":
//				/*修改缓存中题目*/
//				result = questionService.updateQuestion(params[1]);
//				break;
//			case "delete_question_redis":
//				/*修改缓存中题目*/
//				result = questionService.deleteQuestionRedis(params[1]);
//				break;
			case "delete_question":
				if (params.length != 2) {
					result.setRet(Constant.ERROR);
					result.setMessage("参数个数有误！");
					break;
				}
				result = questionService.deleteQuestionByIds(params[1]);
				break;
			default:
				result.setRet(Constant.ERROR);
				result.setMessage("【"+method+"】未找到该指令！");
			}
		}else {
			result.setRet(Constant.ERROR);
			result.setMessage("参数不能为空！");
		}
		return JSONObject.fromObject(result).toString();
	}
}
