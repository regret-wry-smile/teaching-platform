package com.zkxltech.sql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.jdbc.DBHelper;
import com.zkxltech.ui.util.StringUtils;



public class StudentInfoSql {
	private DBHelper<StudentInfo> dbHelper = new DBHelper<StudentInfo>();
	
	/*批量插入学生*/
	public Result importStudent(List<List<Object>> rowList){
		List<String> sqls = new ArrayList<String>();
		String sql = "";
		for (int i = 0; i < rowList.size(); i++) {
			if(i == 0){
				sqls.add("delete from student_info where class_id = '" + rowList.get(i).get(0)+"'"); //删除原来的班级学生
			}
			sql = "insert into student_info (class_id,class_name,student_id,student_name,iclicker_id,status) values('"+rowList.get(i).get(0)+"','"+
					rowList.get(i).get(1)+"','"+rowList.get(i).get(2)+"','"+rowList.get(i).get(3)+"','"+rowList.get(i).get(4)+"','0')";
			sqls.add(sql);
		}
		return dbHelper.onUpdateByGroup(sqls);
	}
	
	/*查询学生*/
	public Result selectStudentInfo(StudentInfo studentInfo) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select * from student_info");
		Field[] files = dbHelper.getFields(studentInfo);
		int index = 0;
		for (int i = 0; i < files.length; i++) {
			Object obj = dbHelper.getFiledValues(files[i], studentInfo);
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
		return dbHelper.onQuery(sqlBuilder.toString(), studentInfo);
	}
	
	/*新增学生*/
	public Result insertStudentInfo(StudentInfo studentInfo) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder sqlBuilder = new StringBuilder(0);
		sqlBuilder.append("insert into student_info ");
		Field[] files = dbHelper.getFields(studentInfo);
		sqlBuilder.append("(");
		for (int i = 0; i < files.length; i++) {
			Object obj = dbHelper.getFiledValues(files[i], studentInfo);
			if (!StringUtils.isEmpty(obj)) {
				sqlBuilder.append(dbHelper.HumpToUnderline(files[i].getName()));
				sqlBuilder.append(",");
			}
		}
		sqlBuilder = new StringBuilder(sqlBuilder.substring(0, sqlBuilder.lastIndexOf(",")));
		sqlBuilder.append(") values (");
		for (int i = 0; i < files.length; i++) {
			Object obj = dbHelper.getFiledValues(files[i], studentInfo);
			if (!StringUtils.isEmpty(obj)) {
				sqlBuilder.append("?");
				sqlBuilder.append(",");
			}
		}
		sqlBuilder = new StringBuilder(sqlBuilder.substring(0, sqlBuilder.lastIndexOf(",")));
		sqlBuilder.append(")");
		return dbHelper.onUpdate(sqlBuilder.toString(), studentInfo);
	}
	
	/**
	 * 主键删除学生
	 * @param studentInfo
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public Result deleteStudentById(List<Integer> ids) throws IllegalArgumentException, IllegalAccessException{
		List<String> sqls = new ArrayList<String>();
		for (int i = 0; i < ids.size(); i++) {
			sqls.add("delete from student_info where id = "+ids.get(i));
		}
		return DBHelper.onUpdateByGroup(sqls);
	}
	
	/*删除学生*/
	public Result deleteStudent(StudentInfo studentInfo) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("delete from student_info");
		Field[] files = dbHelper.getFields(studentInfo);
		int index = 0;
		for (int i = 0; i < files.length; i++) {
			Object obj = dbHelper.getFiledValues(files[i], studentInfo);
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
		return dbHelper.onUpdate(sqlBuilder.toString(), studentInfo);
	}
	
	/*根据主键更新学生*/
	public Result updateStudentById(StudentInfo studentInfo) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("update student_info");
		Field[] files = dbHelper.getFields(studentInfo);
		int index = 0;
		for (int i = 1; i < files.length; i++) {
			Object obj = dbHelper.getFiledValues(files[i], studentInfo);
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
		sqlBuilder.append(" where id = '"+studentInfo.getId()+"'");
		return dbHelper.onUpdate(sqlBuilder.toString(), null);
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
