package com.zkxltech.ui;

import java.awt.Toolkit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
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
import com.zkxltech.ui.util.PageConstant;
import com.zkxltech.ui.util.SwtTools;
import org.eclipse.swt.browser.Browser;

public class MainPage extends Dialog {

	protected Object result;
	protected Display display;
	protected Shell shell;
	private MainStart mainStart;
	
	private int Window_Width; /*屏幕宽度*/ 
	private int Window_Height; /*屏幕高度*/ 
	private int shellWidth;/*窗口宽度*/ 
	private int shellHeight;/*窗口高度*/ 
    
    private boolean isTest; /*是否为调试模式*/
	private boolean isMax = false;/*是否最大化*/
	private String pageType; /*页面类型1答题2设置3记录*/
    
	public MainPage(Shell parent,MainStart mainStart,String pageType) {
		super(parent);
		this.mainStart = mainStart;
		this.pageType = pageType;
	}

	public Object open() {
		initData();
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	private void initData(){
		/*屏幕宽高*/
		Window_Width = Toolkit.getDefaultToolkit().getScreenSize().width;  
	    Window_Height = Toolkit.getDefaultToolkit().getScreenSize().height;
	    shellWidth = (int) (Window_Width * 0.48);
	    shellHeight = shellWidth * 600 / 920;

	    System.out.println(shellWidth+"======"+shellHeight);
	    /*窗口宽高*/
	    if(shellWidth <= 920 || shellHeight <= 600){
	    	shellWidth = 920;
			shellHeight = 600;
	    }
	    System.out.println(shellWidth+"======"+shellHeight);
		isTest = Boolean.parseBoolean(ConfigConstant.projectConf.getApp_test());
	}

	private void createContents() {
		shell = new Shell(getParent(), SWT.PRIMARY_MODAL);
		shell.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				// 屏蔽按下Esc按键
				if (e.detail == SWT.TRAVERSE_ESCAPE) {
					e.doit = false;
				}
			}
		});
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shell.setText("电子答题器");
		shell.setSize(shellWidth, shellHeight);
		// shell.setLocation(400, 150);
		shell.setLocation(Display.getCurrent().getClientArea().width / 2 - shell.getShell().getSize().x / 2,
				Display.getCurrent().getClientArea().height / 2 - shell.getSize().y / 2);
		shell.setLayout(new FormLayout());

		// 头部
		Composite composite_top = new Composite(shell, SWT.NONE);
		composite_top.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite_top.setLayout(new FormLayout());
		FormData fd_composite_1 = new FormData();
		fd_composite_1.bottom = new FormAttachment(0, 57);
		fd_composite_1.right =new FormAttachment(100);
		fd_composite_1.top = new FormAttachment(0);
		fd_composite_1.left = new FormAttachment(0);
		composite_top.setLayoutData(fd_composite_1);

		// 移动窗口事件
		Listener listener = SwtTools.MoveWindow(shell);
		composite_top.addListener(SWT.MouseDown, listener);
		composite_top.addListener(SWT.MouseMove, listener);

		CLabel titleImg = new CLabel(composite_top, SWT.NONE);
		FormData fd_titleImg = new FormData();
		fd_titleImg.bottom = new FormAttachment(0, 41);
		fd_titleImg.right = new FormAttachment(0, 41);
		fd_titleImg.top = new FormAttachment(0, 15);
		fd_titleImg.left = new FormAttachment(0, 15);
		titleImg.setLayoutData(fd_titleImg);
		titleImg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		titleImg.setBackground(SWTResourceManager.getImage(MainPage.class,  PageConstant.titleImg));

		CLabel titleText = new CLabel(composite_top, SWT.NONE);
		FormData fd_titleText = new FormData();
		fd_titleText.bottom = new FormAttachment(0, 41);
		fd_titleText.top = new FormAttachment(0, 15);
		fd_titleText.left = new FormAttachment(0, 47);
		titleText.setLayoutData(fd_titleText);
		titleText.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		titleText.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		titleText.setText("电子答题器");

		CLabel closeLabel = new CLabel(composite_top, SWT.NONE);
		FormData fd_closeLabel = new FormData();
		fd_closeLabel.bottom = new FormAttachment(0, 34);
		fd_closeLabel.right = new FormAttachment(100, -23);
		fd_closeLabel.width = 11;
		fd_closeLabel.top = new FormAttachment(0, 23);
		closeLabel.setLayoutData(fd_closeLabel);
		closeLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		closeLabel.setBackground(SWTResourceManager.getImage(MainPage.class, PageConstant.close_black));
		closeLabel.setAlignment(SWT.CENTER);

		closeLabel.addMouseTrackListener(SwtTools.showHand(closeLabel));
		// 关闭窗口
		closeLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				shell.dispose();
				mainStart.floatingWindow();
			}
		});
		
		//最大化
		CLabel maxShow = new CLabel(composite_top, SWT.NONE);
		FormData fd_maxShow = new FormData();
		fd_maxShow.bottom = new FormAttachment(0, 34);
		fd_maxShow.top = new FormAttachment(0, 23);
		fd_maxShow.right = new FormAttachment(100, -59);
		fd_maxShow.width = 11;
		fd_maxShow.width = 11;
		maxShow.setLayoutData(fd_maxShow);
		maxShow.setBackground(SWTResourceManager.getImage(MainPage.class, PageConstant.max_white));
		maxShow.addMouseTrackListener(SwtTools.showHand(maxShow));
		maxShow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				if(!isMax){
					shell.setSize(Window_Width, Window_Height);
					shell.setLocation(0, 0);
				}else {
					shell.setSize(shellWidth, shellHeight);
					shell.setLocation(Display.getCurrent().getClientArea().width / 2 - shell.getShell().getSize().x / 2,
							Display.getCurrent().getClientArea().height / 2 - shell.getSize().y / 2);
				}
				isMax = !isMax;
			}
			
		});
		
		// 最小化
		CLabel changeMin = new CLabel(composite_top, SWT.CENTER);
		FormData fd_changeMin = new FormData();
		fd_changeMin.bottom = new FormAttachment(0, 34);
		fd_changeMin.right = new FormAttachment(100, -41);
		fd_changeMin.top = new FormAttachment(0, 23);
		fd_changeMin.width = 11;
		changeMin.setLayoutData(fd_changeMin);

		changeMin.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		changeMin.setBackground(SWTResourceManager.getImage(MainPage.class, PageConstant.min_black));
		// 最小化
		changeMin.addListener(SWT.MouseDown, new Listener() { // 监听点击事件

			@Override
			public void handleEvent(Event event) {
				shell.setMinimized(true);
			}
		});
		changeMin.addMouseTrackListener(SwtTools.showHand(changeMin));
		
		if(isTest){
			//FIXME /*测试部分*/
			CLabel refresh = new CLabel(shell, SWT.NONE);
			FormData fd_refresh = new FormData();
			fd_refresh.left = new FormAttachment(50);
			fd_refresh.top = new FormAttachment(50);
			refresh.setLayoutData(fd_refresh);
			refresh.setText("刷新");
			//FIXME /*测试部分*/
			refresh.addMouseListener(new MouseAdapter() {
				 @Override
				 public void mouseDown(MouseEvent e) {
					 PageConstant.browser.refresh();
				 }
			});
		}
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FormLayout());
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100);
		fd_composite.top = new FormAttachment(composite_top, 0);
		fd_composite.right = new FormAttachment(100);
		fd_composite.left = new FormAttachment(0);
		composite.setLayoutData(fd_composite);
		
		PageConstant.browser = new Browser(composite, SWT.NONE);
		FormData fd_browser = new FormData();
		fd_browser.bottom = new FormAttachment(100);
		fd_browser.top = new FormAttachment(0);
		fd_browser.right = new FormAttachment(100);
		fd_browser.left = new FormAttachment(0);
		PageConstant.browser.setLayoutData(fd_browser);
		
		if (isTest) {
			PageConstant.browser.setUrl(PageConstant.TEST_PAGE_URL);
		}else {
			switch (pageType) {
			case "1":
				PageConstant.browser.setUrl(PageConstant.PAGE_ANSWER_URL);
				break;
			case "2":
				PageConstant.browser.setUrl(PageConstant.PAGE_SET_URL);
				break;
			case "3":
				PageConstant.browser.setUrl(PageConstant.PAGE_RECORD_URL);
				break;
			}
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
	
		 
	}
}
