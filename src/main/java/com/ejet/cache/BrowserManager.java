package com.ejet.cache;

import java.lang.ref.WeakReference;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Shell;

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
	
	/*******************/
	/****** 设置模块   ******/
	/*******************/
	/**
	 * 数据绑定调用页面刷新
	 * @param echoRequest
	 */
	public static void refreshBindCard() {
		Browser b  = browerManager.get();
		if (b!=null) {
			shell.getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					b.execute("document.getElementById('refreshBindCard').click();");
				}
			});
		}
	}
	/**
	 * 选中当前班级
	 */
	public static void selectClass(String classId) {
		Browser b  = browerManager.get();
		if (b!=null) {
			shell.getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					b.execute("document.getElementById('refreshBindCard').click();");
				}
			});
		}
	}
	/**
	 * 调用页面弹出相关提示
	 * @param message 提示信息
	 */
	public static void retResult(String message) {
		Browser b  = browerManager.get();
		if (b!=null) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					shell.getDisplay().syncExec(new Runnable() {
						@Override
						public void run() {
							b.execute("var message ='"+message+"';document.getElementById('getTip').click();");
						}
					});
				}
			}).start();
		}
	}
	
	@Deprecated
	/*弹出加载图片*/
	public static void showLoading(){
		Browser b  = browerManager.get();
		if (b!=null) {
			shell.getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					b.execute("var message = '正在导入';document.getElementById('showLoading').click();");
				}
			});
				
		}
	}
	
	/*移除加载图片*/
	public static void removeLoading(){
		Browser b  = browerManager.get();
		if (b!=null) {
			shell.getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					b.execute("document.getElementById('removeLoading').click();");
				}
			});
				
		}
	}

}
