package com.zkxltech.sql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.sun.org.apache.regexp.internal.recompile;
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
	 * 
	 * @param testId 试卷id
	 * @param subjectNam 科目名称
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public Result deleteTestPaper(String testId,String subjectName) throws IllegalArgumentException, IllegalAccessException{
		String sql = "delete from " + tableName + " where test_id = '"+testId+"' and subject = '"+subjectName+"'";
		return dbHelper.onUpdate(sql);
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
	
	// 批量保存题目(服务器标准答案)
		public Result saveTitlebyBatch(String testId, String answers) {
			List<String> sqls = new ArrayList<String>();
			// testId = 9999;
			// answers
			// ="[{\"tno\":1,\"tanswer\":\"A\",\"tscore\":5.0,\"type\":0,\"atype\":0,\"partScore\":0.0,\"highScore\":0.0,\"downScore\":0.0},{\"tno\":2,\"tanswer\":\"A\",\"tscore\":5.0,\"type\":0,\"atype\":0,\"partScore\":0.0,\"highScore\":0.0,\"downScore\":0.0},{\"tno\":3,\"tanswer\":\"A\",\"tscore\":5.0,\"type\":0,\"atype\":0,\"partScore\":0.0,\"highScore\":0.0,\"downScore\":0.0},{\"tno\":4,\"tanswer\":\"A\",\"tscore\":5.0,\"type\":0,\"atype\":0,\"partScore\":0.0,\"highScore\":0.0,\"downScore\":0.0},{\"tno\":5,\"tanswer\":\"A\",\"tscore\":5.0,\"type\":0,\"atype\":0,\"partScore\":0.0,\"highScore\":0.0,\"downScore\":0.0},{\"tno\":6,\"tanswer\":\"\",\"tscore\":5.0,\"type\":0,\"atype\":1,\"partScore\":0.0,\"highScore\":5.0,\"downScore\":0.0},{\"tno\":7,\"tanswer\":\"\",\"tscore\":5.0,\"type\":0,\"atype\":1,\"partScore\":0.0,\"highScore\":5.0,\"downScore\":0.0},{\"tno\":8,\"tanswer\":\"\",\"tscore\":5.0,\"type\":0,\"atype\":1,\"partScore\":0.0,\"highScore\":5.0,\"downScore\":0.0},{\"tno\":9,\"tanswer\":\"\",\"tscore\":5.0,\"type\":0,\"atype\":1,\"partScore\":0.0,\"highScore\":5.0,\"downScore\":0.0},{\"tno\":10,\"tanswer\":\"\",\"tscore\":5.0,\"type\":0,\"atype\":1,\"partScore\":0.0,\"highScore\":5.0,\"downScore\":0.0}]";
			JSONArray jsonArray = JSONArray.parseArray(answers);
			// insert into title_manage values
			// (1,1,"1","1","1",1,"1"),(1,1,"1","1","1",1,"1")
			for (int i = 0; i < jsonArray.size(); i++) {
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(
						"insert into question_info (question_id,question_type,true_answer,test_id,range,score,part_score,high_score,down_score,atype) values");
				com.alibaba.fastjson.JSONObject jsonObject = jsonArray.getJSONObject(i);
//				System.out.println(jsonObject);
				int titleId = jsonObject.getInteger("tno"); // 题号
				String titleAnswer = jsonObject.getString("tanswer"); // 答案
				String score = jsonObject.getString("tscore"); // 分值
				int type = jsonObject.getInteger("type"); // 0单选1多选
				String typeString = "";
				if (type == 0) {
					typeString = "单选";
				} else {
					typeString = "多选";
				}
				int atype = jsonObject.getInteger("atype"); // 0客观1主观
				String partScore = "", highScore = "", downScore = "";
				String range = "A-F";
				if (atype == 1) {
					partScore = jsonObject.getString("partScore");
					highScore = jsonObject.getString("highScore");
					downScore = jsonObject.getString("downScore");
					titleAnswer = highScore;
					range = "";

				}
				stringBuilder.append("('" + titleId + "','" + typeString + "','" + titleAnswer + "','" + testId
						+ "','" + range + "','" + score + "','" + partScore + "','" + highScore + "','" + downScore + "',"
						+ atype + ")");
//				System.out.println(stringBuilder.toString());
				sqls.add(stringBuilder.toString());
			}
			return DBHelper.onUpdateByGroup(sqls);
		}
}
