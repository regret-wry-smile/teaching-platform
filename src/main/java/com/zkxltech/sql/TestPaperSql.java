package com.zkxltech.sql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.zkxltech.domain.ClassInfo;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.domain.TestPaper;
import com.zkxltech.jdbc.DBHelper;
import com.zkxltech.service.impl.TestPaperServiceImpl;
import com.zkxltech.ui.util.StringConstant;
import com.zkxltech.ui.util.StringUtils;

import net.sf.json.JSONObject;



public class TestPaperSql {
	private DBHelper<TestPaper> dbHelper = new DBHelper<TestPaper>();
	private String tableName = "test_paper";
	
	/*查询试卷*/
	public Result selectTestPaper(TestPaper testPaper) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select * from " + tableName);
		Field[] files = dbHelper.getFields(testPaper);
		int index = 0;
		for (int i = 0; i < files.length; i++) {
			Object obj = dbHelper.getFiledValues(files[i], testPaper);
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
		sqlBuilder.append(" order by atype desc");
		return dbHelper.onQuery(sqlBuilder.toString(), testPaper);
	}
	
	/*新增试卷*/
	public Result insertTestPaper(TestPaper testPaper) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder sqlBuilder = new StringBuilder(0);
		sqlBuilder.append("insert into " + tableName);
		Field[] files = dbHelper.getFields(testPaper);
		sqlBuilder.append("(");
		for (int i = 0; i < files.length; i++) {
			Object obj = dbHelper.getFiledValues(files[i], testPaper);
			if (!StringUtils.isEmpty(obj)) {
				sqlBuilder.append(dbHelper.HumpToUnderline(files[i].getName()));
				sqlBuilder.append(",");
			}
		}
		sqlBuilder = new StringBuilder(sqlBuilder.substring(0, sqlBuilder.lastIndexOf(",")));
		sqlBuilder.append(") values (");
		for (int i = 0; i < files.length; i++) {
			Object obj = dbHelper.getFiledValues(files[i], testPaper);
			if (!StringUtils.isEmpty(obj)) {
				sqlBuilder.append("?");
				sqlBuilder.append(",");
			}
		}
		sqlBuilder = new StringBuilder(sqlBuilder.substring(0, sqlBuilder.lastIndexOf(",")));
		sqlBuilder.append(")");
		return dbHelper.onUpdate(sqlBuilder.toString(), testPaper);
	}
	
	/*删除试卷*/
	public Result deleteTestPaper(TestPaper testPaper) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("delete from " + tableName);
		Field[] files = dbHelper.getFields(testPaper);
		int index = 0;
		for (int i = 0; i < files.length; i++) {
			Object obj = dbHelper.getFiledValues(files[i], testPaper);
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
		return dbHelper.onUpdate(sqlBuilder.toString(), testPaper);
	}
	
	/**
	 * 删除最新添加的试卷
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public Result deleteTestPaper() throws IllegalArgumentException, IllegalAccessException{
		String sql = "delete from " + tableName + "where id = (select max(id) from student_info)";
		return dbHelper.onUpdate(sql);
	}
	
	/*根据主键更新试卷*/
	public Result updateTestPaper(TestPaper testPaper) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("update " + tableName);
		Field[] files = dbHelper.getFields(testPaper);
		int index = 0;
		for (int i = 1; i < files.length; i++) {
			Object obj = dbHelper.getFiledValues(files[i], testPaper);
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
		sqlBuilder.append(" where id = '"+testPaper.getId()+"'");
		return dbHelper.onUpdate(sqlBuilder.toString(), null);
	}
	
	
}
