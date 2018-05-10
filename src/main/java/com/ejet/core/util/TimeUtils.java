package com.ejet.core.util;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * TimeUtils
 * 
 */
public class TimeUtils {

	public static final String TAG = TimeUtils.class.getSimpleName();
	/**
	 * 日期格式 ：yyyyMMddHHmmsss
	 */
	public static final SimpleDateFormat DEFAULT_TIME = new SimpleDateFormat("yyyyMMddHHmmsss");
	/**
	 * 日期格式 ：yyyyMMdd
	 */
	public static final SimpleDateFormat DEFAULT_DAY = new SimpleDateFormat("yyyyMMdd");
	/**
	 * 日期格式 ： yyyyMMdd HH:mm:sss
	 */
    public static final SimpleDateFormat S_FORMAT_TIME = new SimpleDateFormat("yyyyMMdd HH:mm:sss");
    /**
     * 日期格式 ：yyyy-MM-dd HH:mm:sss
     */
    public static final SimpleDateFormat F_FORMAT_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
    /**
     * 日期格式 ：yyyy-MM-dd
     */
    public static final SimpleDateFormat F_FORMAT_DAY  = new SimpleDateFormat("yyyy-MM-dd");

    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * 返回格式化日期字符串
     * 比如：dateFormat为 yyyyMMddHHmmsss  则输出：201602121501001
     * 
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }
    /**
     * long time to string, format is {@link #DEFAULT_TIME}
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_TIME);
    }
    /**
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis, String format) {
    	
        return getTime(timeInMillis, new SimpleDateFormat(format));
    }
    
    /**
     * get current time in milliseconds
     * 
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }
    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     * 
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     * 
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }
    
    /**
     * 获得当前时间
     * @param dateFormat
     * @return
     */
    public static String getCurrentTimeInString(String dateFormat) {
    	SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    	return getCurrentTimeInString(sdf);
    }
    /**
     * 获取网络时间,根据dateFormat格式进行输出
     * 比如：dateFormat为 yyyyMMddHHmmsss  则输出：201602121501001
     * 
     * @param dateFormat
     * @return
     */
    public static String getTimeFromNet(String dateFormat) {
    	String bjTime = null;
    	Locale locale = Locale.CHINA; //这是获得本地中国时区
		SimpleDateFormat df = new SimpleDateFormat(dateFormat, locale);//设定日期格式
		try{
			URL url = new URL("http://open.baidu.com/special/time");//取得资源对象
			java.net.URLConnection uc = url.openConnection();//生成连接对象 
			uc.setConnectTimeout(1000);//毫秒
			uc.setReadTimeout(1000);//
			uc.connect(); //发出连接
			long ld = uc.getDate(); //取得网站日期时间
			bjTime = getTime(ld, df);
		}catch(Exception e){
			//Log.e(TAG, "getLocaltime", e);
			bjTime = getCurrentTimeInString(df);
		}finally {
			if(bjTime==null) 
				bjTime = getCurrentTimeInString(df);
		}
		return bjTime;
    }
    /**
     * yyyyMMdd HH:mm:sss
     * 
     * @return
     */
    public static String getCurrentShortTime() {
    	return getCurrentTimeInString(S_FORMAT_TIME);
    }
    
    /**
     * yyyyMMdd
     * 
     * @return
     */
    public static String getCurrentDay() {
    	return getCurrentTimeInString(DEFAULT_DAY);
    }
    
    /**
     * yyyy-MM-dd
     * @return
     */
    public static String getCurrentFullDay() {
    	return getCurrentTimeInString(F_FORMAT_DAY);
    }
    
    /**
     * yyyy-MM-dd HH:mm:sss
     * 
     * @return
     */
    public static String getCurrentFullTime() {
    	return getCurrentTimeInString(F_FORMAT_TIME);
    }
    
    /**
	 * 获取 当前时间，beforeDays前的日期字符串
	 * dateFormat ： 日期格式
	 * beforeDays： 天数
	 * 
	 * @param dateFormat
	 * @param beforeDays
	 */
	public static String getCurrentDayBefore(String dateFormat, int beforeDays) {
		String rs = dateFormat;
		try{
			SimpleDateFormat FORMAT_DAY  = new SimpleDateFormat(dateFormat);
		    Date date = new Date(System.currentTimeMillis());
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(date);
		    calendar.add(Calendar.DATE, -beforeDays);
			date = calendar.getTime();
			rs = FORMAT_DAY.format(date);
		}catch(Exception e) {
			//Log.e(TAG, "getLocaltime", e);
		}
		return rs;
	}

	/**
	 * 获取指定日期格式，当前时间 beforeMonths 以前的时间
	 * 
	 * @param dateFormat
	 * @param beforeMonths
	 * @return
	 */
	public static String getCurrentMonthBefore(String dateFormat, int beforeMonths) {
		String rs = dateFormat;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		    Date date = new Date(System.currentTimeMillis());
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(date);
		    calendar.add(Calendar.MONTH, -beforeMonths);
			date = calendar.getTime();
			rs = sdf.format(date);
		}catch(Exception e) {
			//Log.e(TAG, "getCurrentMonthBefore", e);
		}
		return rs;
	}
    
	
	/**
	 * 获取指定格式当前年， 指定beforeYears前的年份
	 * 
	 * @param dateFormat
	 * @param beforeYear
	 * @return
	 * 
	 */
	public static String getCurrentYearBefore(String dateFormat, int beforeYears) {
		String rs = dateFormat;
		try{
			SimpleDateFormat FORMAT_DAY  = new SimpleDateFormat(dateFormat);
		    Date date = new Date(System.currentTimeMillis());
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(date);
		    calendar.add(Calendar.YEAR, -beforeYears);
			date = calendar.getTime();
			rs = FORMAT_DAY.format(date);
		}catch(Exception e) {
			//Log.e(TAG, "getCurrentYearBefore", e);
		}
		return rs;
	}
	
	/**
	 * 当前
	 * @param dateFormat
	 * @param beforeMinute
	 * 
	 * @return
	 */
	public static String getCurrentMinuteBefore(String dateFormat, int beforeMinute) {
		String rs = dateFormat;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		    Date date = new Date(System.currentTimeMillis());
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(date);
		    calendar.add(Calendar.MINUTE, -beforeMinute);
			date = calendar.getTime();
			rs = sdf.format(date);
		}catch(Exception e) {
			//Log.e(TAG, "getCurrentMinuteBefore", e);
		}
		return rs;
	}
    
	/**
	 * 获取当前时间，几分钟以前的时间
	 * 
	 * @param beforeMinute
	 * @return
	 */
	public static Date getCurrentMinuteBefore(int beforeMinute) {
		 Date date = new Date(System.currentTimeMillis());
		try{
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(date);
		    calendar.add(Calendar.MINUTE, -beforeMinute);
			date = calendar.getTime();
		}catch(Exception e) {
			//Log.e(TAG, "getCurrentMinuteBefore", e);
		}
		return date;
	}
	
	/**
	 * 秒钟以前
	 * @param date
	 * @param minutes
	 * @return
	 */
	public static boolean isMinuteAgo(Date date, int minutes) {
		Date currentDate = new Date();
		long time = currentDate.getTime() - date.getTime();
		long b =1000;
        Long disSec = Long.valueOf(time / b);
        return disSec>=minutes;
	}
	
	/**
	 * 
	 * @param date
	 * @param seconds
	 * @return
	 */
	public static boolean isSecondAgo(Date date, int seconds) {
		Date currentDate = new Date();
		long time = currentDate.getTime() - date.getTime();
        Long disSec = Long.valueOf(time/1000L);
        return disSec>=seconds;
	}
	
	/**
	 * 功能描述：以指定的格式来格式化日期
	 * 
	 * @param date
	 *            Date 日期
	 * @param format
	 *            String 格式
	 * @return String 日期字符串
	 */
	public static String formatDateByFormat(Date date, String format) {
		String result = "";
		if (date != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				result = sdf.format(date);
			} catch (Exception ex) {
				//ex.printStackTrace();
			}
		}
		return result;
	}
	
    public static void main(String[] args)  {
    	System.out.println(getTimeFromNet("yyyyMMdd HH:mm:sss"));
    	System.out.println(getCurrentShortTime());
    	System.out.println(getCurrentDay());
    	System.out.println(getCurrentFullDay());
    	System.out.println(getCurrentDayBefore("yyyyMMdd HH:mm:sss", 1));
    	System.out.println(getCurrentMonthBefore("yyyyMMdd HH:mm:sss", 1));
    	System.out.println(getCurrentYearBefore("yyyyMMdd HH:mm:sss", 1));
    }
    
    
    
    
}
