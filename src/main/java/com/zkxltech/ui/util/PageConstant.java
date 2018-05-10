package com.zkxltech.ui.util;

import org.eclipse.swt.browser.Browser;

public class PageConstant {
	/**
	 * 浏览器对象
	 */
	public static Browser browser;
	
	private final static String COMMON_URL = System.getProperty("user.dir").replaceAll("\\\\", "/") + "/" + "htmls";
	/**
	 * 主页面路径
	 */
	public final static String MAIN_PAGE_URL = COMMON_URL+"/showAnswer.html";
	
	/**
	 * 调试页面路径
	 */
	public final static String TEST_PAGE_URL = COMMON_URL+"/myTest.html";
}
