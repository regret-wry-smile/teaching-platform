package com.config.ui;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
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
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.config.util.Constant;
import com.ejet.core.util.PropertyUtils;
import com.zkxltech.config.ConfigConstant;
import com.zkxltech.domain.ConfigParams;
import com.zkxltech.ui.util.SwtTools;

import net.sf.json.JSONObject;

/**
 * update_config_file 修改文件配置
 * 
 *
 */
public class ConfigPage extends Dialog {
	protected Object result;
	protected Shell shell;
	
	private int shellMaxWidth;/* 窗口最大宽度 */
	private int shellMaxHeight;/* 窗口最大高度 */
	private int shellX;/* 窗口x坐标 */
	private int shellY;/* 窗口y坐标 */
	private Browser browser;

	public static void main(String[] args) {
		Shell shell = new Shell();
		ConfigPage configPage = new ConfigPage(shell, shell.getStyle());
		configPage.open();
	}	
	
	public ConfigPage(Shell parent, int style) {
		super(parent, style);
		setText("设置");
	}

	public Object open() {
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

	/* 初始化配置 */
	private void init() {
		shellMaxWidth = (int) (Toolkit.getDefaultToolkit().getScreenSize().width / 3);
		shellMaxHeight = shellMaxWidth * 330 / 405;
		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle maximumWindowBounds = graphicsEnvironment.getMaximumWindowBounds();
		shellX = (Toolkit.getDefaultToolkit().getScreenSize().width-shellMaxWidth)/2;
		shellY = (Toolkit.getDefaultToolkit().getScreenSize().height-shellMaxHeight)/2;
	}
	
	private void createContents() {
		init();
		shell = new Shell(SWT.NO_TRIM | SWT.ON_TOP);
		shell.setSize(shellMaxWidth, shellMaxHeight);
		shell.setLocation(shellX, shellY);
		shell.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				// 屏蔽按下Esc按键
				if (e.detail == SWT.TRAVERSE_ESCAPE) {
					e.doit = false;
				}
			}
		});
		
		
		shell.setLayout(new FormLayout());
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new FormData());
		composite.setLayout(new FormLayout());
		FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		fd_composite.bottom = new FormAttachment(0, 65);
		fd_composite.right = new FormAttachment(100);
		composite.setLayoutData(fd_composite);
		
		//头部拖拽事件
		Listener listener = SwtTools.MoveWindow(shell);
		composite.addListener(SWT.MouseDown, listener);
		composite.addListener(SWT.MouseMove, listener);
		
//		 //FIXME /*测试部分*/
//		 CLabel refresh = new CLabel(shell, SWT.NONE);
//		 FormData fd_refresh = new FormData();
//		 fd_refresh.left = new FormAttachment(50);
//		 fd_refresh.top = new FormAttachment(50);
//		 refresh.setLayoutData(fd_refresh);
//		 refresh.setText("刷新");
//		 refresh.addMouseListener(new MouseAdapter() {
//		 @Override
//		 public void mouseDown(MouseEvent e) {
//			 browser.refresh();
//		 }
//		 });
		
		browser = new Browser(shell, SWT.NONE);
		FormData fd_browser_1 = new FormData();
		fd_browser_1.top = new FormAttachment(composite);
		fd_browser_1.bottom = new FormAttachment(100);
		fd_browser_1.right = new FormAttachment(100, 0);
		fd_browser_1.left = new FormAttachment(0);
		browser.setLayoutData(fd_browser_1);
		
		String flieUrl = System.getProperty("user.dir").replaceAll("\\\\", "/") + "/" + "htmls/config.html";
		browser.setUrl(flieUrl);
		setFuncs();
		
		CLabel setText = new CLabel(composite, SWT.NONE);
		setText.setAlignment(SWT.CENTER);
		setText.setLayoutData(new FormData());
		setText.setText("配置");
		FormData fd_setText = new FormData();
		fd_setText.top = new FormAttachment(composite);
		fd_setText.bottom = new FormAttachment(100);
		fd_setText.right = new FormAttachment(55);
		fd_setText.left = new FormAttachment(45);
		setText.setLayoutData(fd_setText);
		
		CLabel close = new CLabel(composite, SWT.NONE);
		FormData fd_close = new FormData();
		fd_close.top = new FormAttachment(0, 24);
		fd_close.right = new FormAttachment(100, -25);
		fd_close.bottom = new FormAttachment(60, -4);
		fd_close.width = 11;
		fd_close.height = 11;
		close.setLayoutData(fd_close);
		close.addMouseTrackListener(SwtTools.showHand(close));
		close.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					shell.dispose();
				}
			}
		});
		close.setAlignment(SWT.CENTER);
		close.setBackground(SWTResourceManager.getImage(ConfigPage.class, "/com/zkxltech/image/guanbi.png"));
	}
	
	/**
	 * 绑定浏览器调用事件
	 */
	public void setFuncs(){
		// 修改文件配置
		new BrowserFunction(browser, "update_config_file") {
			@Override
			public Object function(Object[] params) {
				try {
					if (params.length>0) {
					
					}else {
						browser.execute("alert('参数错误！')");
					}
				} catch (Exception e) {
					browser.execute("alert('设置失败！')");
				}
				return null;
			}
		};
	}
	
}
