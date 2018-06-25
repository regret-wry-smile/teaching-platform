package com.zkxltech.ui.functions;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.ejet.core.util.constant.Constant;
import com.zkxltech.domain.Result;
import com.zkxltech.service.ServerService;
import com.zkxltech.service.StudentInfoService;
import com.zkxltech.service.TestPaperService;
import com.zkxltech.service.impl.ServerServiceImpl;
import com.zkxltech.service.impl.StudentInfoServiceImpl;
import com.zkxltech.service.impl.TestPaperServiceImpl;

import net.sf.json.JSONObject;

/**
 * 【试卷模块页面调用方法】
 *	import_paper 导入试卷
 */
public class TestPaperFunctionManage extends BrowserFunction{
	
	private TestPaperService testPaperService = new TestPaperServiceImpl();
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
			case "select_paper":
				result = testPaperService.selectTestPaper(params[1]);
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
			case "import_paper":
				if (params.length != 2) {
					result.setRet(Constant.ERROR);
					result.setMessage("参数个数有误！");
					break;
				}
				result = testPaperService.importTestPaper(params[1]);
				break;
			case "insert_paper":
				if (params.length != 3) {
					result.setRet(Constant.ERROR);
					result.setMessage("参数个数有误！");
					break;
				}
				result = testPaperService.insertTestPaper(params[1],params[2]);
				break;
			case "update_paper":
				if (params.length != 3) {
					result.setRet(Constant.ERROR);
					result.setMessage("参数个数有误！");
					break;
				}
				result = testPaperService.updateTestPaper(params[1],params[2]);
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
