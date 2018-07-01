package com.zkxltech.sql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ejet.core.util.constant.Constant;
import com.zkxltech.domain.QuestionInfo;
import com.zkxltech.domain.Result;
import com.zkxltech.jdbc.DBHelper;
import com.zkxltech.ui.util.StringUtils;



public class QuestionInfoSql {
	private DBHelper<QuestionInfo> dbHelper = new DBHelper<QuestionInfo>();
	
	/*批量插入题目*/
	public Result importQuestion(List<List<Object>> rowList){
		Result result = new Result();
		result = verifyQuetion(rowList); //校验模板格式
		if (Constant.ERROR.equals(result.getRet())) {
			return result;
		}
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
				//0单选；1多选；2判断；3数字；4主观题
				String type = (String) rowList.get(i).get(2);
				String  trueAnswer = "";
				trueAnswer = (String) rowList.get(i).get(3);
				switch (type) {
				case "单选":
					type = "0";
					break;
				case "多选":
					type = "1";
					break;
				case "判断":
					type = "2";
					if ("对".equals(trueAnswer)) {
						trueAnswer = "T";
					}else if("错".equals(trueAnswer)){
						trueAnswer = "F";
					}
					break;
				case "数字":
					type = "3";
					break;
				case "主观题":
					type = "4";
					break;
				default:
					result.setRet(Constant.ERROR);
					result.setMessage((i+1)+"行题目类型有误!");
					return result;
				}
				//插入题目信息
				sql = "insert into question_info (test_id,question_id,question,question_type,true_answer,range,status) values('"+testId+"','"+
						rowList.get(i).get(0)+"','"+rowList.get(i).get(1)+"','"+type+"','"+trueAnswer+"','"+range+"','1')";	
				sqls.add(sql);
			}
		}
		return DBHelper.onUpdateByGroup(sqls);
	}
	
	
	public Result verifyQuetion(List<List<Object>> rowList){
		Result result = new Result();
		result.setRet(Constant.ERROR);
		int rows = rowList.size();
		if (rows < 2) {
			result.setMessage("题目信息为空！");
			return result;
		}
		for (int i = 0; i < rowList.size(); i++) {
			if(i == 0){
				if (rowList.get(i).size() != 4) {
					result.setMessage("第"+(i+1)+"行试卷信息格式错误！");
					return result;
				}
			}else{
				String range = "";
				//0单选；1多选；2判断；3数字；4主观题
				List<Object> list = rowList.get(i);
                    
				if (list.size() < 4) {
                    result.setMessage("第"+(i+1)+"行题目错误,请检查题目类型,正确答案,作答范围是否有误！");
                    return result;
                }
				String type = (String) list.get(2);
				String trueAnswer = (String) list.get(3);
				switch (type) {
				case "单选":
				    if (StringUtils.isEmpty(trueAnswer)) {
                        result.setMessage("第"+(i+1)+"行正确答案不能是空的！");
                        return result;
                    }
				    if (trueAnswer.length() >1) {
                        result.setMessage("第"+(i+1)+"行单选不能是多选答案！");
                        return result;
                    }
					if(rowList.get(i).size() != 5){
						result.setMessage("第"+(i+1)+"行缺少作答范围！");
						return result;
					}
					range = (String) rowList.get(i).get(4);
					if (!verifySingleRange(range)) {
						result.setMessage("第"+(i+1)+"行答案范围格式错误！");
						return result;
					}
					if (!verifySingleAnswer(trueAnswer)) {
						result.setMessage("第"+(i+1)+"行正确答案格式错误！");
						return result;
					}
					break;
				case "多选":
					if(rowList.get(i).size() != 5){
						result.setMessage("第"+(i+1)+"行列数格式错误！");
						return result;
					}
					range = (String) rowList.get(i).get(4);
					if (!verifyMultipleRange(range)) {
						result.setMessage("第"+(i+1)+"行答案范围格式错误！");
						return result;
					}
					if (!verifyMultipleAnswer(trueAnswer,range)) {
						result.setMessage("第"+(i+1)+"行正确答案格式错误！");
						return result;
					}
					break;
				case "判断":
					if(rowList.get(i).size() != 4){
						result.setMessage("第"+(i+1)+"行列数格式错误！");
						return result;
					}
					if (!verifyCheckAnswer(trueAnswer)) {
						result.setMessage("第"+(i+1)+"行正确答案格式错误！");
						return result;
					}
					break;
				case "数字":
					if (!verifyNumRange(range)) {
						result.setMessage("第"+(i+1)+"行答案范围格式错误！");
						return result;
					}
					if (!verifyNumAnswer(trueAnswer,range)) {
						result.setMessage("第"+(i+1)+"行正确答案格式错误！");
						return result;
					}
					break;
				default:
					result.setMessage((i+1)+"行题目类型有误!");
					return result;
				}
			}
		}	
		result.setRet(Constant.SUCCESS);
		return result;
	}
	
	/**
	 * 校验单选答案
	 * @param answer
	 * @return
	 */
	private boolean verifySingleAnswer(String answer){
		if (StringUtils.isEmpty(answer)) {
			return false;
		}
		Pattern p = Pattern.compile("^[A-D]+$");
		Matcher m = p.matcher(answer);
		return m.matches();
	}
	
	/**
	 * 校验单选范围
	 * @param answer
	 * @return
	 */
	private boolean verifySingleRange(String answer){
		Pattern p = Pattern.compile("^[A][-][D]+$");
		Matcher m = p.matcher(answer);
		return m.matches();
	}
	
	/**
	 * 校验多选范围
	 * @param answer
	 * @return
	 */
	private boolean verifyMultipleRange(String range){
		if (StringUtils.isEmpty(range)) {
			return false;
		}
		Pattern p = Pattern.compile("^[A][-][C-F]+$");
		Matcher m = p.matcher(range);
		return m.matches();
	}
	/**
	 * 校验多选答案
	 * @param answer
	 * @return
	 */
	private boolean verifyMultipleAnswer(String answer,String range){
		if (StringUtils.isEmpty(answer)) {
			return false;
		}
		Pattern p = Pattern.compile("^["+range+"]*+$");
		Matcher m = p.matcher(answer);
		return m.matches();
	}
	
//	/**
//	 * 校验判断范围
//	 * @param answer
//	 * @return
//	 */
//	private boolean verifCheckRange(String range){
//		if (StringUtils.isEmpty(range)) {
//			return false;
//		}
//		Pattern p = Pattern.compile("^[A][-][C-F]+$");
//		Matcher m = p.matcher(range);
//		return m.matches();
//	}
	/**
	 * 校验判断答案
	 * @param answer
	 * @return
	 */
	private boolean verifyCheckAnswer(String answer){
		if (StringUtils.isEmpty(answer)) {
			return false;
		}
		return "对".equals(answer) || "错".equals(answer);
	}
	
	/**
	 * 校验数字范围
	 * @param answer
	 * @return
	 */
	private boolean verifyNumRange(String range){
		if (StringUtils.isEmpty(range)) {
			return false;
		}
		Pattern p = Pattern.compile("^[0][-][1-9]+$");
		Matcher m = p.matcher(range);
		return m.matches();
	}
	
	/**
	 * 校验数字答案
	 * @param answer
	 * @return
	 */
	private  boolean verifyNumAnswer(String answer,String range){
		if (StringUtils.isEmpty(answer)) {
			return false;
		}
		Pattern p = Pattern.compile("^["+range+"]*+$");
		Matcher m = p.matcher(answer);
		return m.matches();
	}
	
	
	/*查询题目*/
	public Result selectQuestionInfo(QuestionInfo questionInfo) throws IllegalArgumentException, IllegalAccessException{
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
	 * 根据试卷id更新题目
	 * 
	 * */
	public Result updateStudent(QuestionInfo questionInfo) throws IllegalArgumentException, IllegalAccessException{
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
		sqlBuilder.append(" where test_id = '"+questionInfo.getTestId()+"'");
		return dbHelper.onUpdate(sqlBuilder.toString(), null);
	}
	
	/**
	 * 根据主键更新题目
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
