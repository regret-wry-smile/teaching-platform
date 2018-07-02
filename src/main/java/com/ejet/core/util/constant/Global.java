package com.ejet.core.util.constant;

import java.util.List;

import com.zkxltech.domain.ClassHour;
import com.zkxltech.domain.ClassInfo;
import com.zkxltech.domain.StudentInfo;

public class Global {
	/**
	 * 当前班级id
	 */
	public  static String classId;
	/**
	 * 当前班级信息
	 */
	public static ClassInfo classInfo;
	
	/**
	 * 当前班级学生信息
	 */
	public static List<StudentInfo> studentInfos;
	
	public static ClassHour classHour;
	/**
	 * 当前设备绑定的所有答题器编号<进入设置页面后同步设备和数据库绑定状态时保存到该处,以便后期修改学生的答题器编号时进行查询更新>
	 */ 
	
	/**
	 * 是否开始作答中
	 */
	public  static boolean isAnswerStart = false;
	
	private static List<String> iclickerIds;
	
	public static List<String> getIclickerIds() {
        return iclickerIds;
    }

    public static void setIclickerIds(List<String> iclickerIds) {
        Global.iclickerIds = iclickerIds;
    }

    public static ClassInfo getClassInfo() {
		return classInfo;
	}

	public static void setClassInfo(ClassInfo classInfo) {
		Global.classInfo = classInfo;
	}

	public static List<StudentInfo> getStudentInfos() {
		return studentInfos;
	}

	public static void setStudentInfos(List<StudentInfo> studentInfos) {
		Global.studentInfos = studentInfos;
	}

	public static ClassHour getClassHour() {
		return classHour;
	}

	public static void setClassHour(ClassHour classHour) {
		Global.classHour = classHour;
	}

	public static String getClassId() {
		return classId;
	}

	public static void setClassId(String classId) {
		Global.classId = classId;
	}


	
	
	
}
