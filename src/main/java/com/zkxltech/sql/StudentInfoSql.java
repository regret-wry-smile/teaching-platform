package com.zkxltech.sql;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.jdbc.DBHelper;
import com.zkxltech.jdbc.DBHelper2;
import com.zkxltech.ui.util.StringUtils;



public class StudentInfoSql {
	private DBHelper<StudentInfo> dbHelper = new DBHelper<StudentInfo>();
	
	/*批量插入学生*/
	@SuppressWarnings("static-access")
	public Result importStudent(List<List<Object>> rowList){
		List<String> sqls = new ArrayList<String>();
		String sql = "";
		String classId = "";
		for (int i = 0; i < rowList.size(); i++) {
			if(i == 0){
				classId = (String) rowList.get(i).get(0);
				sqls.add("insert into class_info (class_id,class_name,atype) values('"+classId+"','"+
						rowList.get(i).get(1)+"','0')"); //添加班级信息
				sqls.add("delete from student_info where class_id = '" + rowList.get(i).get(0)+"'"); //删除原来的班级学生
			}
			sql = "insert into student_info (class_id,class_name,student_id,student_name,iclicker_id,status) values('"+classId+"','"+
					rowList.get(i).get(1)+"','"+rowList.get(i).get(2)+"','"+rowList.get(i).get(3)+"','"+rowList.get(i).get(4)+"','0')";
			sqls.add(sql);
		}
		Result result = dbHelper.onUpdateByGroup(sqls);
		result.setRemak(classId);
		return result;
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
	/**
	 * 批量解绑学生
	 * @param studentInfo
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public Result clearStudentByIds(List<Integer> ids) throws IllegalArgumentException, IllegalAccessException{
		List<String> sqls = new ArrayList<String>();
		for (int i = 0; i < ids.size(); i++) {
			sqls.add("update student_info set iclicker_id = '************'  where id = "+ids.get(i));
		}
		return DBHelper.onUpdateByGroup(sqls);
	}
	/**
	 * 根据班级名称获取服务器上的学生
	 * @param className 班级id
	 * @return
	 */
	public static Result getServerStudent(String classId){
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from hz_student t1 LEFT JOIN  hz_student_keybroad t2 ON t1.sid = t2.sid where t1.bjid = ?";
		String[] key = {"xm","sid","kh","sex","bjid","id","KID"};
		params.add(classId);
		return DBHelper2.onQuery(sql, key, params);
	}
	
	/**
	 * 批量保存学生信息
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public Result saveStudentByGroup(List<Map<String, Object>> listMaps) throws ClassNotFoundException, SQLException{
		List<String> sqls = new ArrayList<String>();
		for (int i = 0; i < listMaps.size(); i++) {
			int id = Integer.parseInt((String) listMaps.get(i).get("id"));
			String classId = String.valueOf((int) listMaps.get(i).get("bjid"));
			String studentId = (String) listMaps.get(i).get("kh");
			String studentName = (String) listMaps.get(i).get("xm");
			String kid = (String) listMaps.get(i).get("KID");
			String string = "insert into student_info (id,class_id,class_name,student_id,student_name,iclicker_id) values('"+id+"','"+classId+"','"
					+classId+"','"+studentId+"','"+studentName+"','"+kid+"');";
			sqls.add(string);
		}
		return DBHelper.onUpdateByGroup(sqls);
	}

    public Result updateByIclickerIds(List<String> uidList) {
        //update student_info set status = '0' where iclicker_id in('3429469477','6666660002','************')
        StringBuilder sb = new StringBuilder("update student_info set status = '0' where iclicker_id in(");
        for (int i = 0; i< uidList.size();i++) {
            sb.append(uidList.get(i));
            if (i != uidList.size()-1) {
                sb.append(",");
            }
        }
        sb.append(")");
        return dbHelper.onUpdate(sb.toString(), null);
    }
}
