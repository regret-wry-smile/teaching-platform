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
			switch (method) {
			case "single_answer":
                result = EquipmentServiceImpl.getInstance().singleAnswer(params[1]);
                break;
			case "start_multiple_answer":
                result = answerInfoService.startMultipleAnswer(params[1]);
                break;
			case "get_multiple_answer_num":
				//获取多选作答人数
               return RedisMapMultipleAnswer.getAnswerNum();
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
