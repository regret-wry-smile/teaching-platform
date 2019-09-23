package com.zkxltech.sql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
		String testId = com.ejet.core.util.StringUtils.getUUID();
		String subject = "";
		for (int i = 2; i < rowList.size(); i++) {
			if(i < 3){
				subject = (String) rowList.get(0).get(1);
				String testName = (String)rowList.get(1).get(1);
				String describes = (String)rowList.get(2).get(1);
				sqls.add("delete from test_paper where test_id = '"+ rowList.get(i).get(1)+"'"); //删除原来的试卷
				sqls.add("delete from question_info where test_id = '"+ rowList.get(i).get(1)+"'"); //删除原来的题目
				sqls.add("insert into test_paper (subject,test_id,test_name,describe,atype) values('"+subject+"','"+
						testId+"','"+testName+"','"+describes+"','0')"); //新增试卷信息
			}else{
				String range = " ";
				if(rowList.get(i).size() == 5){
					range = (String) rowList.get(i).get(3);
				}
				//0单选；1多选；2判断；3数字；4主观题
				String type = (String) rowList.get(i).get(1);
				String  trueAnswer = "";
				String score = "";
				trueAnswer = (String) rowList.get(i).get(2);
				switch (type) {
				case "single":
					type = "0";
					score = (String) rowList.get(i).get(4);
					break;
				case "multiple":
					type = "1";
					score = (String) rowList.get(i).get(4);
					break;
				case "Y/N":
					type = "2";
					if ("Y".equals(trueAnswer)) {
						trueAnswer = "YES";
					}else if("N".equals(trueAnswer)){
						trueAnswer = "NO";
					}
					score = (String) rowList.get(i).get(3);
					break;
				case "digital":
					type = "3";
					score = (String) rowList.get(i).get(4);
					break;
				default:
					result.setRet(Constant.ERROR);
					result.setMessage("Line"+(i+1)+"topic type error!");
					return result;
				}
				try {
					Integer.valueOf(score);
				}catch (Exception e){
					result.setRet(Constant.ERROR);
					result.setMessage("Line"+(i+1)+"score error!");
					return result;
				}
				//插入题目信息
				sql = "insert into question_info (test_id,question_id,question_type,true_answer,range,score,status) values('"+testId+"','"+
						rowList.get(i).get(0)+"','"+type+"','"+trueAnswer+"','"+range+"','"+score+"','1')";
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
			result.setMessage("topic information empty！");
			return result;
		}
		List<String> questionIds = new ArrayList<String>();
		
		for (int i = 0; i < rowList.size(); i++) {
			if(i ==0 ){
				if(rowList.get(i).size() != 3){
					result.setMessage("Line"+(1)+"paper information error！");
					return result;
				}
			}else if (i <3 && i>0) {
				if (rowList.get(i).size() != 2) {
					result.setMessage("Line" + (1) + "paper information error！");
					return result;
				}
			} else {
				String range = "";
				//0单选；1多选；2判断；3数字；4主观题
				List<Object> list = rowList.get(i);
				if (list.size() < 4) {
                    result.setMessage("Line"+(i+3)+"topic error,please check the question type, the correct answer, and the wrong scope！");
                    return result;
                }
				String questionId = (String) list.get(0);
				if (!verifyCheckQuetionId(questionId)) {
                     result.setMessage("Line"+(i+3)+"topic ID error！");
                     return result;
                }
				questionIds.add(questionId);
				String type = (String) list.get(1);
				String trueAnswer = (String) list.get(2);
				switch (type) {
				case "single":
				    if (StringUtils.isEmpty(trueAnswer)) {
                        result.setMessage("Line"+(i+3)+"correct answer cannot be empty");
                        return result;
                    }
				    if (trueAnswer.length() >1) {
                        result.setMessage("Line"+(i+3)+"single cannot be a multi-choice answer！");
                        return result;
                    }
                    if(StringUtils.isEmpty(rowList.get(i).get(3))){
                    	result.setMessage("Line"+(i+3)+"lacks the range of questions！");
					}
					if(rowList.get(i).size() != 5){
						result.setMessage("Line"+(i+3)+"missing the score！");
						return result;
					}
					range = (String) rowList.get(i).get(3);
					if (!verifySingleRange(range)) {
						result.setMessage("Line"+(i+3)+"answer range format error！");
						return result;
					}
					if (!verifySingleAnswer(trueAnswer)) {
						result.setMessage("Line"+(i+3)+"correct answer format error！");
						return result;
					}
					break;
				case "multiple":
					if(rowList.get(i).size() != 5){
						result.setMessage("Line"+(i+3)+"Column number format error！");
						return result;
					}
					range = (String) rowList.get(i).get(3);
					if (!verifyMultipleRange(range)) {
						result.setMessage("Line"+(i+3)+"answer range format error！");
						return result;
					}
					if (!verifyMultipleAnswer(trueAnswer,range)) {
						result.setMessage("Line"+(i+3)+"correct answer format error！");
						return result;
					}
					break;
				case "Y/N":
					if(rowList.get(i).size() != 4){
						result.setMessage("Line"+(i+3)+"Column number format error！");
						return result;
					}
					if (!verifyCheckAnswer(trueAnswer)) {
						result.setMessage("Line"+(i+3)+"correct answer format error！");
						return result;
					}
					break;
				case "digital":
					range = (String) rowList.get(i).get(3);
					if (!verifyNumRange(range)) {
						result.setMessage("Line"+(i+3)+"answer range format error！");
						return result;
					}
					if (!verifyNumAnswer(trueAnswer,range)) {
						result.setMessage("Line"+(i+3)+"correct answer format error！");
						return result;
					}
					break;
				default:
					result.setMessage((i+3)+"topic type error!");
					return result;
				}
			}
		}	
		
		if (questionIds.stream().distinct().collect(Collectors.toList()).size() != questionIds.size()) {
			result.setMessage("Topic ID repeated！");
			return result;
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
	 * 校验题号
	 * @param answer
	 * @return
	 */
	private boolean verifyCheckQuetionId(String questionId){
		if (StringUtils.isEmpty(questionId)) {
			return false;
		}
		Pattern p = Pattern.compile("^[1-9][0-9]?+$");
		Matcher m = p.matcher(questionId);
		return m.matches();
	}
	
	
	/**
	 * 校验判断答案
	 * @param answer
	 * @return
	 */
	private boolean verifyCheckAnswer(String answer){
		if (StringUtils.isEmpty(answer)) {
			return false;
		}
		return "Y".equals(answer) || "N".equals(answer);
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
		sqlBuilder.append(" order by CAST(question_id AS DECIMAL),status asc");
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
			String sql = "insert into question_info (test_id,question_id,question,question_type,true_answer,range,score) values('"+questionInfo.getTestId()+"','"+
					questionInfo.getQuestionId()+"','"+questionInfo.getQuestion()+"','"+questionInfo.getQuestionType()+"','"+questionInfo.getTrueAnswer()+"','"+questionInfo.getRange()+"','"+questionInfo.getScore()+"')";
			sqls.add(sql);
		}
		return dbHelper.onUpdateByGroup(sqls);
	}
	
	
	/**
	 * 主键批量删除题目
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
	 * 主键批量修改题目分数
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public Result updateQuestionInfoById(List<Integer> ids) throws IllegalArgumentException, IllegalAccessException{
		List<String> sqls = new ArrayList<String>();
		String score = String.valueOf(ids.get(0));
		for (int i = 1; i < ids.size(); i++) {
			sqls.add("update question_info set score = "+ score +" where id = "+ids.get(i));
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

    /**
     * 批量更新或新增生
     *
     * */
    public Result updateOrSaveStudents(List<QuestionInfo> questionInfos) throws IllegalArgumentException, IllegalAccessException{
        List<String> sqls = new ArrayList<String>();
        for(int i = 0; i < questionInfos.size(); i++) {
            QuestionInfo questionInfo = questionInfos.get(i);
            questionInfo.setQuestionId((i+1)+"");
            StringBuilder sqlBuilder = new StringBuilder();
            if (StringUtils.isEmpty(questionInfo.getId()) || "null".equals(questionInfo.getId())){
                //插入题目信息
                sqlBuilder.append("insert into question_info (test_id,question_id,question,question_type,true_answer,range,score) values('"+questionInfo.getTestId()+"','"+
                        questionInfo.getQuestionId()+"','"+questionInfo.getQuestion()+"','"+questionInfo.getQuestionType()+"','"+questionInfo.getTrueAnswer()+"','"+questionInfo.getRange()+"','"+questionInfo.getScore()+"')");
            }else {
                //更新题目信息
                sqlBuilder.append("update question_info set question_id = '"+questionInfo.getQuestionId()+"',question = '"+questionInfo.getQuestion()+"',question_type = '"+questionInfo.getQuestionType()+"',true_answer ='"+
                        questionInfo.getTrueAnswer()+"',range = '"+questionInfo.getRange()+"',score = '"+questionInfo.getScore()+"' where id = '" +questionInfo.getId()+"'");
            }
            sqls.add(sqlBuilder.toString());
        }
        return DBHelper.onUpdateByGroup(sqls);
    }
}
