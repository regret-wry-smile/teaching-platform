package com.zkxltech.ui.functions;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.ejet.core.util.constant.Constant;
import com.zkxltech.domain.Result;
import com.zkxltech.service.ClassHourService;
import com.zkxltech.service.StudentInfoService;
import com.zkxltech.service.impl.ClassHourServiceImpl;
import com.zkxltech.service.impl.StudentInfoServiceImpl;

import net.sf.json.JSONObject;

/**
 * 【答题记录模块页面调用方法】
 *
 */
public class RecordFunctionManage extends BrowserFunction{
	
	private ClassHourService classHourService = new ClassHourServiceImpl();
	public RecordFunctionManage(Browser browser, String name) {
		super(browser, name);
	}
	@Override
	public Object function(Object[] params) {
		Result result = new Result();
		if (params.length>0) {
			String method = (String) params[0]; //页面要调用的方法
			switch (method) {
			case "start_class":
				if (params.length != 3) {
					result.setRet(Constant.ERROR);
					result.setMessage("参数个数有误！");
					break;
				}
				result = classHourService.startClass(params[1], params[2]);
				break;
			case "select_class_hour":
				/*查询课程列表列表*/
				if (params.length != 3) {
					result.setRet(Constant.ERROR);
					result.setMessage("参数个数有误！");
					break;
				}
				result = classHourService.selectClassInfo(params[1], params[2]);
				break;
			case "insert_class_hour":
				/*查询课程列表列表*/
				if (params.length != 2) {
					result.setRet(Constant.ERROR);
					result.setMessage("参数个数有误！");
					break;
				}
				result = classHourService.insertClassInfo(params[1]);
				break;
			case "delete_class_hour":
				/*查询课程列表列表*/
				if (params.length != 2) {
					result.setRet(Constant.ERROR);
					result.setMessage("参数个数有误！");
					break;
				}
				result = classHourService.deleteClassInfo(params[1]);
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
