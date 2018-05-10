package com.zkxltech.teaching.sql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.zkxltech.domain.AnswerInfo;
import com.zkxltech.domain.Result;
import com.zkxltech.jdbc.DBHelper;



public class AnswerInfoSql {
	private DBHelper<AnswerInfo> dbHelper = new DBHelper<AnswerInfo>();
	
	/*批量插入答案*/
	public Result insertAnswerInfos(List<AnswerInfo> answerInfos) throws IllegalArgumentException, IllegalAccessException{
		List<String> sqls = new ArrayList<String>();
		AnswerInfo answerInfo = null;
		for (int i = 0; i < answerInfos.size(); i++) {
			answerInfo = answerInfos.get(i);
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("insert into answer_info (");
			Field[] files = dbHelper.getFields(answerInfo);
			for (int j = 0; j < files.length; j++) {
				Object obj = dbHelper.getFiledValues(files[j], answerInfo);
				if (obj != null) {
					sqlBuilder.append(dbHelper.HumpToUnderline(files[j].getName())+",");
				}
			}
			sqlBuilder = new StringBuilder(sqlBuilder.substring(0, sqlBuilder.lastIndexOf(",")));
			sqlBuilder.append(") values (");
			for (int  j= 0; j < files.length; j++) {
				Object obj = dbHelper.getFiledValues(files[j], answerInfo);
				if (obj != null) {
					sqlBuilder.append("'"+dbHelper.getFiledValues(dbHelper.getFields(answerInfo)[j], answerInfo)+"'");
					sqlBuilder.append(",");
				}
			}
			sqlBuilder = new StringBuilder(sqlBuilder.substring(0, sqlBuilder.lastIndexOf(",")));
			sqlBuilder.append(")");
			sqls.add(sqlBuilder.toString());
		}
		return dbHelper.onUpdateByGroup(sqls);
	}
	
	/*查询答题信息*/
	public Result selectAnswerInfoInfo(AnswerInfo answerInfo) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select * from answer_info");
		Field[] files = dbHelper.getFields(answerInfo);
		int index = 0;
		for (int i = 0; i < files.length; i++) {
			Object obj = dbHelper.getFiledValues(files[i], answerInfo);
			if (obj != null) {
				if (index == 0) {
					sqlBuilder.append(" where ");
				}else {
					sqlBuilder.append(" and ");
				}
				sqlBuilder.append(dbHelper.HumpToUnderline(files[i].getName())+" = ?");
				index++;
			}
		}
		return dbHelper.onQuery(sqlBuilder.toString(), answerInfo);
	}
	
	
	/*删除答题信息*/
	public Result deleteAnswerInfo(AnswerInfo answerInfo) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("delete from answer_info");
		Field[] files = dbHelper.getFields(answerInfo);
		int index = 0;
		for (int i = 0; i < files.length; i++) {
			Object obj = dbHelper.getFiledValues(files[i], answerInfo);
			if (obj != null) {
				if (index == 0) {
					sqlBuilder.append(" where ");
				}else {
					sqlBuilder.append(" and ");
				}
				sqlBuilder.append(dbHelper.HumpToUnderline(files[i].getName())+" = ?");
				index++;
			}
		}
		return dbHelper.onUpdate(sqlBuilder.toString(), answerInfo);
	}
}
