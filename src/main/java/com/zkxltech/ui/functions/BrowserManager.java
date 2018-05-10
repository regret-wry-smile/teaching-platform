package com.zkxltech.ui.functions;

import java.lang.ref.WeakReference;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.ejet.core.util.constant.Constant;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.teaching.msg.EchoRequest;
import com.zkxltech.teaching.service.StudentInfoService;
import com.zkxltech.teaching.service.impl.StudentInfoServiceImpl;

/**
 * 
 * 【回调页面方法】
 *
 */
public class BrowserManager {
	
	private static WeakReference<Browser> browerManager = null; 
	private static Shell shell = null;
	
	public static void setShell(Shell shell) {
		BrowserManager.shell = shell;
	}
	public static void setBrower(Browser brower) {
		browerManager = new WeakReference<Browser>(brower);
	}
	
	//刷新页面(如果是学生端重新查询学生，如果是老师端重新查询连接信息)
	public static void refreshStudent() {
		Browser b  = browerManager.get();
		if (b!=null) {
			shell.getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					b.execute("document.getElementById('refresh').click();");
				}
			});
		}
	}

}
