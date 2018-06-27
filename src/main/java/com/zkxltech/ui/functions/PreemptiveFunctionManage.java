package com.zkxltech.ui.functions;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.ejet.cache.RedisMapQuick;
import com.ejet.core.util.constant.Constant;
import com.zkxltech.domain.Result;
import com.zkxltech.service.StudentInfoService;
import com.zkxltech.service.impl.EquipmentServiceImpl;
import com.zkxltech.service.impl.StudentInfoServiceImpl;

import net.sf.json.JSONObject;

/**
 * 【抢答模块页面调用方法】
 *
 */
public class PreemptiveFunctionManage extends BrowserFunction{
	
	public PreemptiveFunctionManage(Browser browser, String name) {
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
			case "quick_answer":
			    result = service.quickAnswer(param);
                break;
			case "get_quick_answer_studentName":
			    return RedisMapQuick.getQuickAnswer();
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
