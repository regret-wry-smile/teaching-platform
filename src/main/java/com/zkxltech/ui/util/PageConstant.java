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
	
	public static String imagePack = "/com/zkxltech/image";
	
	/**
	 * 头部左上角图片
	 */
	public static String titleImg = imagePack + "/title.png";
	
	/**
	 * 悬浮框默认图片
	 */
	public static String image01 = imagePack + "/desktop_preview_03.png";
	
	/**
	 * 背景颜色图片
	 */
	public static String image02 = imagePack + "/desktop_preview_03_bg.png";
	
	/**
	 * 白色框关闭图片
	 */
	public static String close_white = imagePack + "/close_white.png";
	
	/**
	 * 黑色关闭图片
	 */
	public static String close_black = imagePack + "/guanbi.png";
	
	/**
	 * 最大化
	 */
	public static String max_white = imagePack + "/max_white.png";
	/**
	 * 最小化——黑色
	 */
	public static String min_black = imagePack + "/min_black.png";
}
