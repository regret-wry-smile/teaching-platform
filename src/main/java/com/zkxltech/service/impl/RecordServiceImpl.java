package com.zkxltech.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.record.SelectionRecord;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.ejet.core.util.comm.ListUtils;
import com.ejet.core.util.comm.StringUtils;
import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.domain.ClassHour;
import com.zkxltech.domain.QuestionInfo;
import com.zkxltech.domain.Record;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.TestPaper;
import com.zkxltech.service.RecordService;
import com.zkxltech.sql.ClassHourSql;
import com.zkxltech.sql.QuestionInfoSql;
import com.zkxltech.sql.RecordSql;
import com.zkxltech.sql.TestPaperSql;
import com.zkxltech.ui.util.ExportExcel;

import net.sf.json.JSONObject;

public class RecordServiceImpl implements RecordService{
	private Result result ;
	//FIXME 导入答题记录
	@Override
	public Result exportRecord(Object object) {
		result = new Result();
		try {
			result.setMessage("导出成功!");
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("导入学生失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}
	
	@Deprecated
	public void testExport(Object object) throws Exception{
		String fileName = String.valueOf(object);
		String titleName = "课程名称为['课程名']的作答详情";
		String testName = "试卷名称："; //FIXME 试卷名称
		String className = "班级名称:"; //FIXME 班级名称
		String studentSum = "学生人数:";//FIXME 学生人数
		String dates = "";
		//FIXEME 列数 = 6 + 该试卷的所有题目个数
		int columnNumber = 0;
        int[] columnWidth = new int[columnNumber];// 行宽
        for (int i = 0; i < columnWidth.length; i++) {
        	if (i==0) {
        		columnWidth[i] = 12;	
			}else {
				columnWidth[i] = 10;	
			}
		}
        String[] columnName = new String[columnNumber];// 标题
        columnName[0] = "键盘";columnName[1] = "学号";columnName[2] = "姓名";
        columnName[3] = "得分";columnName[4] = "正确率";columnName[5] = "排名";
        for (int i = 6; i < columnName.length; i++) {
        	columnName[i] = "题目"+(i-5);
		}
		List<List<Object>> lists = new ArrayList<List<Object>>();
		//FIXME 获取所有学生信息
		List<Map<String, Object>> studentInfos = new ArrayList<Map<String,Object>>();
		int questionSum = columnNumber - 6;//题数
		studentSum = String.valueOf(studentInfos.size()); //学生人数
		for (int i = 0; i < studentInfos.size(); i++) {
			List<Object> listMaps = new ArrayList<Object>();
			while (columnNumber > listMaps.size()) {
				listMaps.add(null);
			}
			listMaps.set(0,(String) studentInfos.get(i).get("iclicker_id")); //答题器编号
			String studentId = (String)studentInfos.get(i).get("student_id");//学号
			listMaps.set(1, studentId); 
			listMaps.set(2,(String) studentInfos.get(i).get("student_name"));//姓名
			//FIXME 每个学生的所有答题详情
			List<Map<String, Object>> answerMapList = new ArrayList<Map<String,Object>>();
			double scoreSum = 0;
			double trueSum = 0; //正确题数
			for (int j = 0; j < answerMapList.size(); j++) {
				if (j == 0) {
					String answerDate = (String) answerMapList.get(j).get("answer_date");
		            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					dates = "创建时间:"+simpleDateFormat.format(new Date())+"  作答时间:"+answerDate;
				}
				Map<String, Object> map = new HashMap<String, Object>();
				String answer = (String) answerMapList.get(j).get("answer");
				String score = (String) answerMapList.get(j).get("score");
				String type = (String) answerMapList.get(j).get("type");
				String result = (String) answerMapList.get(j).get("result");
				int questionId = Integer.parseInt((String) answerMapList.get(j).get("question_id"));
				if (score != null && !"".equals(score) && !"null".equals(score)) {
					if ("1".equals(type)) { //客观题得分需要正确
						if ("正确".equals(result)) {
							scoreSum += Double.parseDouble(score);
						}
					}else if ("2".equals(type)) { //主观题得分
						scoreSum += Double.parseDouble(score);
					}
				}
				if ("正确".equals(result)) {
					trueSum ++;
				}
				map.put("type", type);
				if ( answer == null ||answer.equals("null") || answer.equals("")) {
					answer = "";
				}
				map.put("answer", answer);
				map.put("result", result);
				listMaps.set(5+questionId,map);
			}
			listMaps.set(3,String.valueOf(scoreSum));//总分
			listMaps.set(4,String.format("%.2f",trueSum * 100/questionSum)+"%");//正确率
			lists.add(listMaps);
		}
		/*按分数降序排序*/
		Collections.sort(lists, new Comparator<Object>(){  
	        @Override  
	        public int compare(Object o1, Object o2) { 
	        	@SuppressWarnings("unchecked")
				double score1=Double.parseDouble(((List<String>)o1).get(3));  
	        	@SuppressWarnings("unchecked")
	        	double score2=Double.parseDouble(((List<String>)o2).get(3)); 
	        	  if(score1<score2){
	                    return 1;
	                }else if(score1==score2){
	                    return 0;
	                }else{
	                    return -1;
	                }
	        }             
	    });
		/*计算名次*/
		int rank = 1;
		for (int i = 0; i < lists.size(); i++) {
			if (i != lists.size()-1) {
				double score1 = Double.parseDouble((String) lists.get(i).get(3));
				double score2 = Double.parseDouble((String) lists.get(i+1).get(3));
				lists.get(i).set(5,String.valueOf(rank));
				if (score1 != score2) {
					rank = i+2;
				}
			}else {
				lists.get(i).set(5,String.valueOf(rank));
			}
		}
		String flieUrl = System.getProperty("user.dir").replaceAll("\\\\", "/") + "/"+"excels/";
		SXSSFWorkbook wb = ExportExcel.ExportWithResponse("成绩明细表", titleName,testName ,dates,className, studentSum, testName, columnNumber, columnWidth, columnName , lists);	
		FileOutputStream out = new FileOutputStream(new File(flieUrl+fileName));
        wb.write(out);// 将数据写出去  
        out.flush();// 将数据写出去
        out.close(); 
	}
	/**
	 * 查询答题记录
	 * @param object
	 * @return
	 */
    public static Result selectRecord(Object object) {
        Result r = new Result();
        r.setRet(Constant.ERROR);
        Record record = com.zkxltech.ui.util.StringUtils.parseJSON(object, Record.class);
        if (StringUtils.isBlank(record.getClassHourId())||StringUtils.isBlank(record.getTestId())) {
            r.setMessage("缺少参数:课程id和试卷id不能为空");
            return r;
        }
        try {
            //查询课程的开始时间
            ClassHourSql classHourSql = new ClassHourSql();
            ClassHour classHour = new ClassHour();
            classHour.setClassHourId(record.getClassHourId());
            r = classHourSql.selectClassHour(classHour);
            if (r.getRet().equals(Constant.ERROR)) {
                return r;
            }
            List<ClassHour>  classHours = (List<ClassHour>) r.getItem();
            classHour = classHours.get(0);
            if (classHour == null) {
                r.setMessage("未查询到该课时信息");
                return r;
            }
            //查试卷的详情
            TestPaperSql testPaperSql = new TestPaperSql();
            TestPaper testPaper = new TestPaper();
            testPaper.setTestId(record.getTestId());
            r = testPaperSql.selectTestPaper(testPaper);
            if (r.getRet().equals(Constant.ERROR)) {
                return r;
            }
            List<TestPaper> testPapers = (List<TestPaper>) r.getItem();
            testPaper = testPapers.get(0);
            if (testPaper == null) {
                r.setMessage("未查询到该试卷信息");
                return r;
            }
            //查试卷的题目总数
            QuestionInfoSql questionInfoSql = new QuestionInfoSql();
            QuestionInfo questionInfo = new QuestionInfo();
            questionInfo.setTestId(record.getTestId());
            questionInfo.setStatus(Constant.STATUS_ENABLED);
            r = questionInfoSql.selectQuestionInfo(questionInfo);
            if (r.getRet().equals(Constant.ERROR)) {
                return r;
            }
            List<QuestionInfo> questInfos = (List<QuestionInfo>) r.getItem();
            if (ListUtils.isEmpty(questInfos)) {
                r.setMessage("该试卷没有对应题目");
                return r;
            }
            //查试卷的所有答题记录
            RecordSql recordSql = new RecordSql();
            r = recordSql.selectRecord(record);
            if (r.getRet().equals(Constant.ERROR)) {
                return r;
            }
            //查询到所有学生的所有答题数据
            List<Record> records = (List<Record>) r.getItem();
            if (ListUtils.isEmpty(records)) {
                r.setMessage("未查询到该试卷任意答题记录");
                return r;
            }
            //根据学生id进行分组,分别计算每个学生的正确率
            Map<Object, List<Record>> studentRecordMap = ListUtils.getClassificationMap(records, "studentId");
            //用来存返回数据
            List<Record> result = new ArrayList<>();
            for (Object key : studentRecordMap.keySet()) {
                List<Record> list = studentRecordMap.get(key);//得到每个学生的所有答题记录
                //按正确和错误进行分类
                Map<Object, List<Record>> resultMap = ListUtils.getClassificationMap(list, "result");
                List<Record> corrects = resultMap.get("2");//得到所有正确的答案总数
                float b = (float)corrects.size() / questInfos.size();
                Record resultRocord = new Record();
                resultRocord.setStudentName(list.get(0).getStudentName());
                resultRocord.setResult(formattedDecimalToPercentage(b));
                resultRocord.setTestName(testPaper.getTestName());
                resultRocord.setTime(classHour.getStartTime());
                result.add(resultRocord);
            }
            r.setItem(result);
            r.setRet(Constant.SUCCESS);
            r.setMessage("查询成功");
            return r;
        } catch (Exception e) {
            r.setMessage("查询数据库失败");
            r.setDetail(IOUtils.getError(e));
        }
        return r;
    }
    private static String formattedDecimalToPercentage(double decimal) {
        //获取格式化对象
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(2);
        return nt.format(decimal);
    }
}
