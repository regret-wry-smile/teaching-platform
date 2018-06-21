package com.zkxltech.ui.functions;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.ejet.core.util.constant.Constant;
import com.zkxltech.domain.Result;
import com.zkxltech.service.StudentInfoService;
import com.zkxltech.service.impl.StudentInfoServiceImpl;

import net.sf.json.JSONObject;

/**
 * 【学生模块页面调用方法】
 *  select_student 查询学生信息
 *
 */
public class StudentFunctionManage extends BrowserFunction{
	private StudentInfoService service = new StudentInfoServiceImpl();
	
	public StudentFunctionManage(Browser browser, String name) {
		super(browser, name);
	}
	
	@Override
	public Object function(Object[] params) {
		Result result = new Result();
		if (params.length>0) {
			String method = (String) params[0]; //页面要调用的方法
			Object param = params.length == 2 ? params[1] : new Object(); //页面要调用该方法的参数
			switch (method) {
			case "insert_student":
				result = service.insertStudentInfo(param);
				break;
			case "import_student":
				result = service.importStudentInfo(param);
				break;
			case "delete_student":
				result = service.deleteStudentById(param);
				break;
			case "update_student":
				result = service.updateStudentById(param);
				break;
			case "select_student":
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
