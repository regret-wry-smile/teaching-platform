package com.zkxltech.sql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.zkxltech.domain.ClassInfo;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.jdbc.DBHelper;
import com.zkxltech.ui.util.StringUtils;



public class ClassInfoSql {
	private DBHelper<ClassInfo> dbHelper = new DBHelper<ClassInfo>();
	private String tableName = "class_info";
	
	/*查询班级*/
	public Result selectClassInfo(ClassInfo classInfo) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select * from " + tableName);
		Field[] files = dbHelper.getFields(classInfo);
		int index = 0;
		for (int i = 0; i < files.length; i++) {
			Object obj = dbHelper.getFiledValues(files[i], classInfo);
			if (!StringUtils.isEmpty(obj)) {
				if (index == 0) {
					sqlBuilder.append(" where ");
				}else {
					sqlBuilder.append(" and ");
				}
				sqlBuilder.append(dbHelper.HumpToUnderline(files[i].getName())+" = ?");
				index++;
			}
		}
		sqlBuilder.append(" order by id asc");
		return dbHelper.onQuery(sqlBuilder.toString(), classInfo);
	}
	
	/*新增班级*/
	public Result insertClassInfo(ClassInfo classInfo) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder sqlBuilder = new StringBuilder(0);
		sqlBuilder.append("insert into " + tableName);
		Field[] files = dbHelper.getFields(classInfo);
		sqlBuilder.append("(");
		for (int i = 0; i < files.length; i++) {
			Object obj = dbHelper.getFiledValues(files[i], classInfo);
			if (!StringUtils.isEmpty(obj)) {
				sqlBuilder.append(dbHelper.HumpToUnderline(files[i].getName()));
				sqlBuilder.append(",");
			}
		}
		sqlBuilder = new StringBuilder(sqlBuilder.substring(0, sqlBuilder.lastIndexOf(",")));
		sqlBuilder.append(") values (");
		for (int i = 0; i < files.length; i++) {
			Object obj = dbHelper.getFiledValues(files[i], classInfo);
			if (!StringUtils.isEmpty(obj)) {
				sqlBuilder.append("?");
				sqlBuilder.append(",");
			}
		}
		sqlBuilder = new StringBuilder(sqlBuilder.substring(0, sqlBuilder.lastIndexOf(",")));
		sqlBuilder.append(")");
		return dbHelper.onUpdate(sqlBuilder.toString(), classInfo);
	}
	
	/*删除班级*/
	public Result deleteClassInfo(ClassInfo classInfo) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("delete from " + tableName);
		Field[] files = dbHelper.getFields(classInfo);
		int index = 0;
		for (int i = 0; i < files.length; i++) {
			Object obj = dbHelper.getFiledValues(files[i], classInfo);
			if (!StringUtils.isEmpty(obj)) {
				if (index == 0) {
					sqlBuilder.append(" where ");
				}else {
					sqlBuilder.append(" and ");
				}
				sqlBuilder.append(dbHelper.HumpToUnderline(files[i].getName())+" = ?");
				index++;
			}
		}
		return dbHelper.onUpdate(sqlBuilder.toString(), classInfo);
	}
	
	/*根据主键更新班级*/
	public Result updateClassInfo(ClassInfo classInfo) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("update " + tableName);
		Field[] files = dbHelper.getFields(classInfo);
		int index = 0;
		for (int i = 1; i < files.length; i++) {
			Object obj = dbHelper.getFiledValues(files[i], classInfo);
			if (!StringUtils.isEmpty(obj)) {
				if (index == 0) {
					sqlBuilder.append(" set ");
				}else {
					sqlBuilder.append(" , ");
				}
				sqlBuilder.append(dbHelper.HumpToUnderline(files[i].getName())+" =  '"+obj+"'");
				index++;
			}
		}
		sqlBuilder.append(" where id = '"+classInfo.getId()+"'");
		return dbHelper.onUpdate(sqlBuilder.toString(), null);
	}
	
	/*public static void main(String[] args) {
		ClassInfo classInfo = new ClassInfo();
		classInfo.setClassId("999999");
		classInfo.setClassName("三年级二班");
		try {
			new ClassInfoSql().insertClassInfo(classInfo);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
