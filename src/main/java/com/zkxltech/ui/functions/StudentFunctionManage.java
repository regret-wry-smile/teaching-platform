package com.zkxltech.ui.functions;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.cache.BrowserManager;
import com.ejet.cache.RedisMapBind;
import com.ejet.core.util.ICallBack;
import com.ejet.core.util.StringUtils;
import com.ejet.core.util.constant.Constant;
import com.zkxltech.domain.Result;
import com.zkxltech.service.ClassInfoService;
import com.zkxltech.service.StudentInfoService;
import com.zkxltech.service.impl.ClassInfoServiceImpl;
import com.zkxltech.service.impl.StudentInfoServiceImpl;

import net.sf.json.JSONObject;

/**
 * 【学生模块页面调用方法】
 *  select_student 查询学生信息
 *
 */
public class StudentFunctionManage extends BrowserFunction{
	private static final Logger logger = LoggerFactory.getLogger(StudentFunctionManage.class);
	
	private ClassInfoService classInfoService = new ClassInfoServiceImpl();
	private StudentInfoService studentInfoservice = new StudentInfoServiceImpl();
	public StudentFunctionManage(Browser browser, String name) {
		super(browser, name);
	}
	
	@Override
	public Object function(Object[] params) {
		Result result = new Result();
		if (params.length>0) {
			String method = (String) params[0]; //页面要调用的方法
			switch (method) {
			case "insert_student":
				result = studentInfoservice.insertStudentInfo(params[1]);
				break;
			case "import_student2":
				new Thread(new Runnable() {
					@Override
					public void run() {
						studentInfoservice.importStudentInfo2(params[1],params[2],new ICallBack() {
							@Override
							public void onResult(int code, String message, Object ext) {
								if (StringUtils.intToBoolen(code)) {
									BrowserManager.showMessage(true,"导入成功!");
									BrowserManager.refreshClass();
									BrowserManager.refreshStudent((String)ext);
								} else {
									BrowserManager.showMessage(false,"导入失败!");
//									BrowserManager.refreshClass();
								}
							}
						});
					}
				}).start();
				break;
			case "import_student":
				result =  studentInfoservice.importStudentInfo(params[1],params[2]);
				break;
			case "import_server":
				result = studentInfoservice.importStudentInfoByServer(params[1]);
				break;
			case "delete_student":
				result = studentInfoservice.deleteStudentById(params[1]);
				break;
			case "update_student":
				result = studentInfoservice.updateStudentById(params[1]);
				break;
			case "clear_student":
				/*解绑*/
				result = studentInfoservice.clearStudentByIds(params[1]);
				break;
			case "select_student":
				result = studentInfoservice.selectStudentInfo(params[1]);
				break;
			case "select_class":
				result = classInfoService.selectClassInfo(params[1]);
				break;
			case "insert_class":
				result = classInfoService.insertClassInfo(params[1]);
				break;
			case "delete_class":
				result = classInfoService.deleteClassInfo(params[1]);
				break;
			case "update_class":
				result = classInfoService.updateClassInfo(params[1]);
				break;
			case "get_bind_info":
				return RedisMapBind.getBindMapValue();
				/**清除白名单位(解绑)*/
			case "clear_bind":
			    result = classInfoService.clearWl(params[1]);
			    break;
			    /**开始绑定(一键配对)*/
			case "bind_start":
			    result = classInfoService.bindStart(params[1]);
                break;
			case "bind_stop":
			    result = classInfoService.bindStop();
                break;
			default:
				result.setRet(Constant.ERROR);
				result.setMessage("【"+method+"】未找到该指令！");
			}
		}else {
			logger.error("页面传递的参数为空");
			result.setRet(Constant.ERROR);
			result.setMessage("参数不能为空！");
		}
		return JSONObject.fromObject(result).toString();
	}
}
