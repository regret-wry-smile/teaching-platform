package com.zkxltech.sql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.zkxltech.domain.AnswerInfo;
import com.zkxltech.domain.Record;
import com.zkxltech.domain.Result;
import com.zkxltech.jdbc.DBHelper;
import com.zkxltech.ui.util.StringUtils;



public class RecordSql {
	private DBHelper<Record> dbHelper = new DBHelper<Record>();
	
	/*批量插入答案*/
	public Result insertRecords(List<Record> records) throws IllegalArgumentException, IllegalAccessException{
		List<String> sqls = new ArrayList<String>();
		Record record = null;
		for (int i = 0; i < records.size(); i++) {
			record = records.get(i);
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("insert into record (");
			Field[] files = dbHelper.getFields(record);
			for (int j = 0; j < files.length; j++) {
				Object obj = dbHelper.getFiledValues(files[j], record);
				if (!StringUtils.isEmpty(obj)) {
					sqlBuilder.append(dbHelper.HumpToUnderline(files[j].getName())+",");
				}
			}
			sqlBuilder = new StringBuilder(sqlBuilder.substring(0, sqlBuilder.lastIndexOf(",")));
			sqlBuilder.append(") values (");
			for (int  j= 0; j < files.length; j++) {
				Object obj = dbHelper.getFiledValues(files[j], record);
				if (!StringUtils.isEmpty(obj)) {
					sqlBuilder.append("'"+dbHelper.getFiledValues(dbHelper.getFields(record)[j], record)+"'");
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
	public Result selectRecord(Record record) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select * from record");
		Field[] files = dbHelper.getFields(record);
		int index = 0;
		for (int i = 0; i < files.length; i++) {
			Object obj = dbHelper.getFiledValues(files[i], record);
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
		return dbHelper.onQuery(sqlBuilder.toString(), record);
	}
//	
//	
//	/*删除答题信息*/
//	public Result deleteAnswerInfo(AnswerInfo answerInfo) throws IllegalArgumentException, IllegalAccessException{
//		StringBuilder sqlBuilder = new StringBuilder();
//		sqlBuilder.append("delete from answer_info");
//		Field[] files = dbHelper.getFields(answerInfo);
//		int index = 0;
//		for (int i = 0; i < files.length; i++) {
//			Object obj = dbHelper.getFiledValues(files[i], answerInfo);
//			if (!StringUtils.isEmpty(obj)) {
//				if (index == 0) {
//					sqlBuilder.append(" where ");
//				}else {
//					sqlBuilder.append(" and ");
//				}
//				sqlBuilder.append(dbHelper.HumpToUnderline(files[i].getName())+" = ?");
//				index++;
//			}
//		}
//		return dbHelper.onUpdate(sqlBuilder.toString(), answerInfo);
//	}
}
