package com.zkxltech.ui.functions;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.ejet.cache.RedisMapMultipleAnswer;
import com.ejet.core.util.constant.Constant;
import com.zkxltech.domain.Result;
import com.zkxltech.service.AnswerInfoService;
import com.zkxltech.service.StudentInfoService;
import com.zkxltech.service.impl.AnswerInfoServiceImpl;
import com.zkxltech.service.impl.EquipmentServiceImpl;
import com.zkxltech.service.impl.StudentInfoServiceImpl;

import net.sf.json.JSONObject;

/**
 * 【答题模块页面调用方法】
 *
 */
public class AnswerFunctionManage extends BrowserFunction{
	private AnswerInfoService answerInfoService = new AnswerInfoServiceImpl();
	public AnswerFunctionManage(Browser browser, String name) {
		super(browser, name);
	}
	@Override
	public Object function(Object[] params) {
		Result result = new Result();
		if (params.length>0) {
			String method = (String) params[0]; //页面要调用的方法
			Object param = params.length == 2 ? params[1] : new Object(); //页面要调用该方法的参数
			StudentInfoService service = new StudentInfoServiceImpl();
			switch (method) {
			case "import_paper":
				result = service.selectStudentInfo(param);
				break;
			case "single_answer":
			    result = service.singleAnswer(param);
                break;
			case "get_single_answer":
                //result = EquipmentServiceImpl.getInstance().singleAnswer(param);
                break;
			case "start_multiple_answer": //开始多选答题
                result = answerInfoService.startMultipleAnswer(params[1]);
                break;
			case "get_multiple_range":
				//获取多选范围
               return RedisMapMultipleAnswer.getRange();
			case "get_multiple_answer_num":
				//获取多选作答人数
               return RedisMapMultipleAnswer.getAnswerNum();
			case "get_multiple_answer_detail":
				//获取多选作答详情
               return RedisMapMultipleAnswer.getEveryAnswerInfoBar();
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
