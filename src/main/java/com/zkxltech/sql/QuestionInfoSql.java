package com.zkxltech.sql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.zkxltech.domain.QuestionInfo;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.jdbc.DBHelper;
import com.zkxltech.ui.util.StringUtils;



public class QuestionInfoSql {
	private DBHelper<QuestionInfo> dbHelper = new DBHelper<QuestionInfo>();
	
	/*批量插入题目*/
	public Result importQuestion(List<List<Object>> rowList){

		List<String> sqls = new ArrayList<String>();
		String sql = "";
		String testId = "";
		String subject = "";
		for (int i = 0; i < rowList.size(); i++) {
			if(i == 0){
				subject = (String) rowList.get(i).get(0);
				testId = (String) rowList.get(i).get(1);
				sqls.add("delete from test_paper where test_id = '"+ rowList.get(i).get(1)+"'"); //删除原来的试卷
				sqls.add("delete from question_info where test_id = '"+ rowList.get(i).get(1)+"'"); //删除原来的题目
				sqls.add("insert into test_paper (subject,test_id,test_name,describe,atype) values('"+subject+"','"+
						testId+"','"+rowList.get(i).get(2)+"','"+rowList.get(i).get(3)+"','0')"); //新增试卷信息
			}else{
				String range = "";
				if(rowList.get(i).size() == 5){
					range = (String) rowList.get(i).get(4);
				}
				//插入题目信息
				sql = "insert into question_info (test_id,question_id,question,question_type,true_answer,range) values('"+testId+"','"+
						rowList.get(i).get(0)+"','"+rowList.get(i).get(1)+"','"+rowList.get(i).get(2)+"','"+rowList.get(i).get(3)+"','"+range+"')";	

				sqls.add(sql);
			}
		}
		return DBHelper.onUpdateByGroup(sqls);
	}
	
	/*查询试卷*/
	public Result selectStudentInfo(QuestionInfo questionInfo) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select * from question_info");
		Field[] files = dbHelper.getFields(questionInfo);
		int index = 0;
		for (int i = 0; i < files.length; i++) {
			Object obj = dbHelper.getFiledValues(files[i], questionInfo);
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
		sqlBuilder.append(" order by status desc");
		return dbHelper.onQuery(sqlBuilder.toString(), questionInfo);
	}
	
	/**
	 * 新增题目(单题添加)
	 * @param questionInfo
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public Result insertQuestionInfo(QuestionInfo questionInfo) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder sqlBuilder = new StringBuilder(0);
		sqlBuilder.append("insert into question_info ");
		Field[] files = dbHelper.getFields(questionInfo);
		sqlBuilder.append("(");
		for (int i = 0; i < files.length; i++) {
			Object obj = dbHelper.getFiledValues(files[i], questionInfo);
			if (!StringUtils.isEmpty(obj)) {
				sqlBuilder.append(dbHelper.HumpToUnderline(files[i].getName()));
				sqlBuilder.append(",");
			}
		}
		sqlBuilder = new StringBuilder(sqlBuilder.substring(0, sqlBuilder.lastIndexOf(",")));
		sqlBuilder.append(") values (");
		for (int i = 0; i < files.length; i++) {
			Object obj = dbHelper.getFiledValues(files[i], questionInfo);
			if (!StringUtils.isEmpty(obj)) {
				sqlBuilder.append("?");
				sqlBuilder.append(",");
			}
		}
		sqlBuilder = new StringBuilder(sqlBuilder.substring(0, sqlBuilder.lastIndexOf(",")));
		sqlBuilder.append(")");
		return dbHelper.onUpdate(sqlBuilder.toString(), questionInfo);
	}
	
	
	/**
	 * 新增题目(多题添加)
	 * @param questionInfos
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public Result insertQuestionInfo(List<QuestionInfo> questionInfos) throws IllegalArgumentException, IllegalAccessException{
		List<String> sqls = new ArrayList<String>();
		for (int i = 0; i < questionInfos.size(); i++) {
			QuestionInfo questionInfo = questionInfos.get(i);
			//插入题目信息
			String sql = "insert into question_info (test_id,question_id,question,question_type,true_answer,range) values('"+questionInfo.getTestId()+"','"+
					questionInfo.getQuestionId()+"','"+questionInfo.getQuestion()+"','"+questionInfo.getQuestionType()+"','"+questionInfo.getTrueAnswer()+"','"+questionInfo.getRange()+"')";	
			sqls.add(sql);
		}
		return dbHelper.onUpdateByGroup(sqls);
	}
	
	
	/**
	 * 主键批量删除题目
	 * @param studentInfo
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public Result deleteQuestionInfoById(List<Integer> ids) throws IllegalArgumentException, IllegalAccessException{
		List<String> sqls = new ArrayList<String>();
		for (int i = 0; i < ids.size(); i++) {
			sqls.add("delete from question_info where id = "+ids.get(i));
		}
		return DBHelper.onUpdateByGroup(sqls);
	}
	
	/**
	 * 删除题目
	 * 
	 * */
	public Result deleteQuestionInfo(QuestionInfo questionInfo) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("delete from question_info");
		Field[] files = dbHelper.getFields(questionInfo);
		int index = 0;
		for (int i = 0; i < files.length; i++) {
			Object obj = dbHelper.getFiledValues(files[i], questionInfo);
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
		return dbHelper.onUpdate(sqlBuilder.toString(), questionInfo);
	}
	
	/**
	 * 根据主键更新学生
	 * 
	 * */
	public Result updateStudentById(QuestionInfo questionInfo) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("update question_info");
		Field[] files = dbHelper.getFields(questionInfo);
		int index = 0;
		for (int i = 1; i < files.length; i++) {
			Object obj = dbHelper.getFiledValues(files[i], questionInfo);
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
		sqlBuilder.append(" where id = '"+questionInfo.getId()+"'");
		return dbHelper.onUpdate(sqlBuilder.toString(), null);
	}
	
	/**
	 * 根据主键批量更新学生
	 * 
	 * */
	public Result updateStudentsById(List<QuestionInfo> questionInfos) throws IllegalArgumentException, IllegalAccessException{
		//testId,questionId,question,questionType,trueAnswer,range,score,partScore,highScore,downScore
		List<String> sqls = new ArrayList<String>();
		for (int i = 0; i < questionInfos.size(); i++) {
			QuestionInfo questionInfo = questionInfos.get(i);
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("update question_info set question = '"+questionInfo.getQuestion()+"',questionType = '"+questionInfo.getQuestionType()+"',trueAnswer ='"+
					questionInfo.getTrueAnswer()+"',range = '"+questionInfo.getRange()+"' where id = '" +questionInfo.getId()+"'");
			sqls.add(sqlBuilder.toString());
		}
		return DBHelper.onUpdateByGroup(sqls);
	}
	
//	public static void main(String[] args) {
//		StudentInfo studentInfo = new StudentInfo();
//		studentInfo.setId(104320);
//		studentInfo.setStatus("1");
//		studentInfo.setIclickerId("9999999");
//		try {
//			new StudentInfoSql().updateStudent(studentInfo);
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
