package com.zkxltech.ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JPanel;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

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
import com.zkxltech.ui.util.Colors;
import com.zkxltech.ui.util.PageConstant;
import com.zkxltech.ui.util.SwtTools;
import org.eclipse.swt.events.MouseTrackAdapter;
/*
 * 	页面回调指令
 * 	start_answer 开始作答
 */
public class MainPage2 {

	protected Object result;
	protected Shell shell;
	private int shellMaxWidth;/* 窗口最大宽度 */
	private int shellMaxHeight;/* 窗口最大高度 */
	private int shellMinWidth = 800;/*窗口最小宽度标准*/
	private int shellMinHeight =450;/*窗口最小高度标准*/
	private int shellX;/* 窗口x坐标 */
	private int shellY;/* 窗口y坐标 */
	
	private boolean isTest;

	public MainPage2(Shell parent) {
		shell = parent;
	}


	
	public static void main(String[] args) {
		new MainPage2(new Shell()).open();
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
		shell = new Shell(SWT.PRIMARY_MODAL);
		shell.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				// 屏蔽按下Esc按键  
		        if (e.detail == SWT.TRAVERSE_ESCAPE) {  
		            e.doit = false;  
		        }  
			}
		});
		int shellWidth = Display.getCurrent().getClientArea().width / 2;
		int shellHeight =shellWidth * 900 / 1600;
		if (shellWidth<shellMinWidth || shellHeight<shellMinHeight) {
			shellWidth = shellMinWidth;
			shellHeight = shellMinHeight;
		}
		shell.setVisible(true);
		shell.setSize(shellWidth, shellHeight);
		shell.setLocation(Display.getCurrent().getClientArea().width / 2 - shell.getShell().getSize().x/2, Display.getCurrent()  
                .getClientArea().height / 2 - shell.getSize().y/2);  
		shell.setLayout(new FormLayout());
		shell.setBackground(Colors.SLIGHT_WHITE);
		
		//中间内容
		Composite compositeAll = new Composite(shell, SWT.NONE);
		compositeAll.setLayout(new FormLayout());
		FormData fd_compositeAll = new FormData(shellWidth-6,shellHeight-6);
		fd_compositeAll.left = new FormAttachment(0,2);
		fd_compositeAll.top = new FormAttachment(0,2);
		compositeAll.setLayoutData(fd_compositeAll);
		compositeAll.setBackground(Colors.SLIGHT_BLUE);
		
//		//上面部分
//		Composite composite = new Composite(shell, SWT.NONE);
//		composite.setLayout(new FormLayout());
//		FormData fd_composite = new FormData(800,60);
//		fd_composite.left = new FormAttachment(0);
//		fd_composite.right = new FormAttachment(100);
//		composite.setLayoutData(fd_composite);
//		composite.setBackground(Colors.WHITE_BG);
		
//		//头部图标
//		CLabel titleImg = new CLabel(composite, SWT.NONE);
//		FormData fd_titleImg = new FormData(26,26);
//		fd_titleImg.top = new FormAttachment(0, 17);
//		fd_titleImg.left = new FormAttachment(0, 15);
//		titleImg.setLayoutData(fd_titleImg);
//		titleImg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
//		titleImg.setBackground(SWTResourceManager.getImage(MainPage2.class, PageConstant.titleImg));
		
//		//头部文字
//		CLabel titleText = new CLabel(composite, SWT.NONE);
//		FormData fd_titleText = new FormData(86,26);
//		fd_titleText.top = new FormAttachment(0, 17);
//		fd_titleText.left = new FormAttachment(0, 47);
//		titleText.setLayoutData(fd_titleText);
//		titleText.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
//		titleText.setForeground(Colors.TEXTCOLOR1);
//		titleText.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
//		titleText.setText("电子答题器");
		
		//自由移动
//		Listener listener = SwtTools.MoveWindow(shell);
//		composite.addListener(SWT.MouseDown, listener);
//		composite.addListener(SWT.MouseMove, listener);
		
		//改变窗口大小
		Listener listener2 = SwtTools.ChangeShellSize(shell);
		shell.addListener(SWT.MouseDown, listener2);
		shell.addListener(SWT.MouseMove, listener2);
		Listener listener3 = SwtTools.resetData(shell);
		compositeAll.addListener(SWT.MouseDown, listener3);
		compositeAll.addListener(SWT.MouseMove, listener3);
		
		
//		//关闭
//		CLabel close = new CLabel(composite, SWT.CENTER);
//		FormData formData3 = new FormData(25,25);
//		formData3.top = new FormAttachment(0, 18);
//		formData3.right = new FormAttachment(100, -18);
//		close.setLayoutData(formData3);
//		close.setBackground(Colors.WHITE_BG);
//		close.setImage(SWTResourceManager.getImage(MainPage2.class,PageConstant.closeGrey));
//		close.addMouseTrackListener(SwtTools.showHand(close));
//		
//		//关闭按钮  鼠标点击事件
//			close.addListener(SWT.MouseDown, new Listener() {
//				public void handleEvent(Event event) {
//					shell.dispose();
//				}
//			});
			
			
//		 //FIXME /*测试部分*/
//		 CLabel refresh = new CLabel(shell, SWT.NONE);
//		 FormData fd_refresh = new FormData();
//		 fd_refresh.left = new FormAttachment(50);
//		 fd_refresh.top = new FormAttachment(50);
//		 refresh.setLayoutData(fd_refresh);
//		 refresh.setText("刷新");
//
//		Browser browser = new Browser(shell, SWT.NONE);
//		browser.setLayoutData(new FormData());
//		PageConstant.browser = browser;
//		FormData fd_browser = new FormData();
//		fd_browser.top = new FormAttachment(0, 60);
//		fd_browser.bottom = new FormAttachment(0,shellHeight-10);
//		fd_browser.right = new FormAttachment(0, shellWidth-2);
//		fd_browser.left = new FormAttachment(0);
//		PageConstant.browser.setLayoutData(fd_browser);
//		if (isTest) {
//			PageConstant.browser.setUrl(PageConstant.TEST_PAGE_URL);
//		}else {
//			PageConstant.browser.setUrl(PageConstant.MAIN_PAGE_URL);
//		}
//		
//		
//		/* 创建java调用页面的方法 */
//		BrowserManager.setBrower(PageConstant.browser);
//		BrowserManager.setShell(shell);
//		
//		/*【注册页面调用java的方法 】*/
//		new AnswerFunctionManage(PageConstant.browser, "execute_answer");//答题模块
//		new AttendanceFunctionManage(PageConstant.browser, "execute_attendance");//出勤模块
//		new PreemptiveFunctionManage(PageConstant.browser, "execute_preemptive");//抢答模块
//		new ScoreFunctionManage(PageConstant.browser, "execute_score");//评分模块
//		new SetFunctionManage(PageConstant.browser, "execute_set");//设置模块
//		new StudentFunctionManage(PageConstant.browser, "execute_student");//学生模块
//		new TestPaperFunctionManage(PageConstant.browser, "execute_testPaper");//试卷模块
//		new RecordFunctionManage(PageConstant.browser, "execute_record");//记录模块
//		new VoteFunctionManage(PageConstant.browser, "execute_vote");//投票模块
//		
//		 //FIXME /*测试部分*/
//		 refresh.addMouseListener(new MouseAdapter() {
//		 @Override
//		 public void mouseDown(MouseEvent e) {
//			 PageConstant.browser.refresh();
//		 }
//		 });
	
	}
}
