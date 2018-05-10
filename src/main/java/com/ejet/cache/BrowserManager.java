package com.ejet.cache;

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


public class BrowserManager {
	
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
	public static void bindCard(EchoRequest echoRequest) {
		Browser b  = browerManager.get();
		if (b!=null) {
			StudentInfoService studentInfoService = new StudentInfoServiceImpl();
			
			StudentInfo studentInfo = new StudentInfo();
			studentInfo.setId(Integer.parseInt(echoRequest.getId()));
			studentInfo.setIclickerId(echoRequest.getIclickerId());
			studentInfo.setStatus(Constant.BING_YES);
			studentInfoService.updateStudentInfo(studentInfo);
			new Thread(new Runnable() {
				@Override
				public void run() {
					shell.getDisplay().syncExec(new Runnable() {
						@Override
						public void run() {
							b.execute("document.getElementById('refresh').click();");
						}
					});
				}
			}).start();
			
			
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
	//刷新页面(如果是学生端重新查询学生，如果是老师端重新查询连接信息)
	public static void refreshStudent() {
		Browser b  = browerManager.get();
		if (b!=null) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					shell.getDisplay().syncExec(new Runnable() {
						@Override
						public void run() {
							b.execute("document.getElementById('refresh').click();");
						}
					});
				}
			}).start();
		}
	}
	/*答题类型页面切换*/
	public static void selectAnswerType(String answerType) {
		Browser b  = browerManager.get();
		if (b!=null) {
			shell.getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					switch (answerType) {
					case "1":
						b.execute("document.getElementById('answerType01').click();");
						break;
					case "2":
						b.execute("document.getElementById('answerType02').click();");
						break;
					case "3":
						b.execute("document.getElementById('answerType03').click();");
						break;
					}
					b.execute("document.getElementById('startAnswer').click();");
				}
			});
				
		}
	}
	/*停止答题*/
	public static void stopAnswer() {
		Browser b  = browerManager.get();
		if (b!=null) {
			shell.getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					b.execute("document.getElementById('stopAnswer').click();");
				}
			});
				
		}
	}
	
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
	
	/*选择答案同步*/
	public static void chartClick(String answer){
		Browser b  = browerManager.get();
		if (b!=null) {
			shell.getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					b.execute("var selectAnswer = '"+answer+"';document.getElementById('chart').click();");
				}
			});
				
		}
	}

}
