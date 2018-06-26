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
import com.zkxltech.service.EquipmentService;
import com.zkxltech.service.StudentInfoService;
import com.zkxltech.service.impl.ClassInfoServiceImpl;
import com.zkxltech.service.impl.EquipmentServiceImpl;
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
	private EquipmentService equipmentService = EquipmentServiceImpl.getInstance();
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
				result = studentInfoservice.insertStudentInfo(param);
				break;
			case "import_student2":
				new Thread(new Runnable() {
					@Override
					public void run() {
						studentInfoservice.importStudentInfo2(param,new ICallBack() {
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
				result =  studentInfoservice.importStudentInfo(param);
				break;
			case "import_server":
				result = studentInfoservice.importStudentInfoByServer(param);
				break;
			case "delete_student":
				result = studentInfoservice.deleteStudentById(param);
				break;
			case "update_student":
				result = studentInfoservice.updateStudentById(param);
				break;
			case "clear_student":
				/*解绑*/
				result = studentInfoservice.clearStudentByIds(param);
				break;
			case "select_student":
				result = studentInfoservice.selectStudentInfo(param);
				break;
			case "select_class":
				result = classInfoService.selectClassInfo(param);
				break;
			case "insert_class":
				result = classInfoService.insertClassInfo(param);
				break;
			case "delete_class":
				result = classInfoService.deleteClassInfo(param);
				break;
			case "update_class":
				result = classInfoService.updateClassInfo(param);
				break;
			case "get_bind_info":
				return RedisMapBind.getBindMap();
				/**清除白名单位(解绑)*/
			case "clear_bind":
			    result = equipmentService.clear_wl();
			    break;
			    /**开始绑定(一键配对)*/
			case "bind_start":
                result = equipmentService.bind_start(param);
                break;
			case "bind_stop":
                result = equipmentService.bind_stop();
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
