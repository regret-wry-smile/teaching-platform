package com.ejet.core.util;

import java.text.DecimalFormat;

/**
* 浮点数格式化类。<br>
* 1、可格式化为任意位数的小数位（包括整数）；<br>
* 2、提供简单单位转换功能，例：元转换为分，分钟转换为小时……<br>
*/
public class NumberFormatUtils {
	
	
	/**
	 * 格式化
	 * 
	 * @param radixPoint
	 */
	public static DecimalFormat format(double factor, int radixPoint) {
		DecimalFormat df = null;
		//系数
		//double factor = factor;
		//格式化参数的整数部分
		String integerFormatParam = "#########################0";
		//格式化参数的小数部分（包含小数点）
		String decimalFormatParam = getDecimalFormatParam(radixPoint);
		df = new DecimalFormat(integerFormatParam + decimalFormatParam);
		
		return df;
	}
	
	/**
	 * 获得格式化参数的小数部分（包含小数点）
	 * 
	 * @return 格式化参数的小数部分
	 */
	private static String getDecimalFormatParam(int radixPoint) {
		StringBuffer s = new StringBuffer();
		if (radixPoint > 0) {
			s.append(".");
			for (int i = 0; i < radixPoint; i++) {
				s.append("0");
			}
		}
		return s.toString();
	}
	

}
