package com.ejet.cache;

import java.lang.ref.WeakReference;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 【回调页面方法】
 *
 */
public class BrowserManager {
	private static final Logger log = LoggerFactory.getLogger(BrowserManager.class);
	private static boolean doRet;
	private static WeakReference<Browser> browerManager = null; 
	private static Shell shell = null;
	
	public static void setShell(Shell shell) {
		BrowserManager.shell = shell;
	}
	public static void setBrower(Browser brower) {
		browerManager = new WeakReference<Browser>(brower);
	}
	/**
	 * 数据绑定调用页面刷新
	 * @param echoRequest
	 */
	public static void refreshBindCard() {
		Browser b  = browerManager.get();
		if (b!=null) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					shell.getDisplay().syncExec(new Runnable() {
						@Override
						public void run() {
							boolean  doRet = b.execute("document.getElementById('refresh').click();");
							log.info("绑定刷新："+doRet);
						}
					});
				}
			}).start();
			
			
		}
	}
	/**
	 * 调用页面弹出相关提示(不需要回调)
	 * @param message 提示信息
	 */
	public static void showMessage(boolean ret,String message) {
		Browser b  = browerManager.get();
		if (b!=null) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					shell.getDisplay().syncExec(new Runnable() {
						@Override
						public void run() {
							boolean  doRet = b.execute("var ret = '"+ret+"';var message ='"+message+"';document.getElementById('getTip').click();");
							log.info("调用页面提示框："+doRet);
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
					boolean  doRet = b.execute("var message = '正在导入';document.getElementById('showLoading').click();");
					log.info("弹窗加载图片："+doRet);
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
