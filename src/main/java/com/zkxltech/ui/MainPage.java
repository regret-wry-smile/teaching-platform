package com.zkxltech.ui;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.ejet.cache.BrowserManager;
import com.zkxltech.config.ConfigConstant;
import com.zkxltech.ui.functions.AnswerFunctionManage;
import com.zkxltech.ui.functions.AttendanceFunctionManage;
import com.zkxltech.ui.functions.PreemptiveFunctionManage;
import com.zkxltech.ui.functions.RecordFunctionManage;
import com.zkxltech.ui.functions.ScoreFunctionManage;
import com.zkxltech.ui.functions.SetFunctionManage;
import com.zkxltech.ui.functions.StudentFunctionManage;
import com.zkxltech.ui.functions.TestPaperFunctionManage;
import com.zkxltech.ui.functions.VoteFunctionManage;
import com.zkxltech.ui.util.PageConstant;
/*
 * 	页面回调指令
 * 	start_answer 开始作答
 */
public class MainPage {

	protected Object result;
	protected Shell shell;
	private MainStart mainStart;
	private int shellMaxWidth;/* 窗口最大宽度 */
	private int shellMaxHeight;/* 窗口最大高度 */
	private int shellX;/* 窗口x坐标 */
	private int shellY;/* 窗口y坐标 */
	
	private boolean isTest;

	public MainPage(Shell parent) {
		shell = parent;
	}

	public void setMainStart(MainStart mainStart) {
		this.mainStart = mainStart;
	}
	
	/* 初始化配置 */
	private void init() {
		shellMaxWidth = (int) (Toolkit.getDefaultToolkit().getScreenSize().width / 2.4);
		shellMaxHeight = shellMaxWidth * 560 / 800;
		shellX = 0;
		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle maximumWindowBounds = graphicsEnvironment.getMaximumWindowBounds();
		shellY = maximumWindowBounds.height - shellMaxHeight - 6;
		
		isTest = Boolean.parseBoolean(ConfigConstant.projectConf.getApp_test());
	}

	public void closeShell() {
		shell.setVisible(false);
		mainStart.changeImage();
		mainStart.frame.repaint();
	}

	public void showShell() {
		shell.setVisible(true);
	}

	public Shell open() {
		try {
			init();
			createContents();
			shell.layout();
			Display display = shell.getDisplay();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
		}
		return shell;
	}

	private void createContents() {
		shell = new Shell(shell, SWT.NO_TRIM | SWT.ON_TOP);
		shell.setVisible(false);
		shell.setSize(shellMaxWidth, shellMaxHeight);
		shell.setLocation(shellX, shellY);
		shell.setLayout(new FormLayout());
		shell.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				// 屏蔽按下Esc按键
				if (e.detail == SWT.TRAVERSE_ESCAPE) {
					e.doit = false;
				}
			}
		});

		 //FIXME /*测试部分*/
		 CLabel refresh = new CLabel(shell, SWT.NONE);
		 FormData fd_refresh = new FormData();
		 fd_refresh.left = new FormAttachment(50);
		 fd_refresh.top = new FormAttachment(50);
		 refresh.setLayoutData(fd_refresh);
		 refresh.setText("刷新");

		PageConstant.browser = new Browser(shell, SWT.NONE);
		FormData fd_browser = new FormData();
		fd_browser.left = new FormAttachment(0);
		fd_browser.top = new FormAttachment(0);
		fd_browser.width = shellMaxWidth;
		fd_browser.height = shellMaxHeight;
		PageConstant.browser.setLayoutData(fd_browser);
		if (isTest) {
			PageConstant.browser.setUrl(PageConstant.TEST_PAGE_URL);
		}else {
			PageConstant.browser.setUrl(PageConstant.MAIN_PAGE_URL);
		}
		
		
		/* 创建java调用页面的方法 */
		BrowserManager.setBrower(PageConstant.browser);
		BrowserManager.setShell(shell);
		
		/*【注册页面调用java的方法 】*/
		new AnswerFunctionManage(PageConstant.browser, "execute_answer");//答题模块
		new AttendanceFunctionManage(PageConstant.browser, "execute_attendance");//出勤模块
		new PreemptiveFunctionManage(PageConstant.browser, "execute_preemptive");//抢答模块
		new ScoreFunctionManage(PageConstant.browser, "execute_score");//评分模块
		new SetFunctionManage(PageConstant.browser, "execute_set");//设置模块
		new StudentFunctionManage(PageConstant.browser, "execute_student");//学生模块
		new TestPaperFunctionManage(PageConstant.browser, "execute_testPaper");//试卷模块
		new RecordFunctionManage(PageConstant.browser, "execute_record");//记录模块
		new VoteFunctionManage(PageConstant.browser, "execute_vote");//投票模块
		
		 //FIXME /*测试部分*/
		 refresh.addMouseListener(new MouseAdapter() {
		 @Override
		 public void mouseDown(MouseEvent e) {
			 PageConstant.browser.refresh();
		 }
		 });
	}
}
