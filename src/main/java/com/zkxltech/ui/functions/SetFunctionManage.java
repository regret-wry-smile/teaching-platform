package com.zkxltech.ui.functions;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.ejet.core.util.constant.Constant;
import com.zkxltech.domain.Result;
import com.zkxltech.teaching.service.StudentInfoService;
import com.zkxltech.teaching.service.impl.StudentInfoServiceImpl;

import net.sf.json.JSONObject;

/**
 * 【设置模块页面调用方法】
 *
 */
public class SetFunctionManage extends BrowserFunction{
private Result result = new Result();
	
	public SetFunctionManage(Browser browser, String name) {
		super(browser, name);
	}
	@Override
	public Object function(Object[] params) {
		if (params.length>0) {
			String method = (String) params[0]; //页面要调用的方法
			Object param = params.length == 2 ? params[1] : new Object(); //页面要调用该方法的参数
			switch (method) {
			case "import_paper":
				StudentInfoService service = new StudentInfoServiceImpl();
				result = service.selectStudentInfo(param);
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
