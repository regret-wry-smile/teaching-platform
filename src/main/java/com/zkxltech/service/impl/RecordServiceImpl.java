package com.zkxltech.service.impl;

import com.ejet.cache.BrowserManager;
import com.ejet.core.util.comm.ListUtils;
import com.ejet.core.util.comm.StringUtils;
import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.constant.Global;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.domain.*;
import com.zkxltech.service.RecordService;
import com.zkxltech.sql.*;
import com.zkxltech.ui.util.ExportExcel;
import net.sf.json.JSONObject;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class RecordServiceImpl implements RecordService{
    private static final Logger log = LoggerFactory.getLogger(RecordServiceImpl.class);
    private Result result ;
    
    private RecordSql recordSql = new RecordSql();
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
//  @Test
//  public void aa(){
//      try {
//          JSONObject jo = new JSONObject();
//          jo.put("classId", "BJ1001");
//          jo.put("subject", "语文");
//          jo.put("classHourId", "7b44b6206d934057ac437f978c1e9c2b");
//          jo.put("testId", "4Y0001");
//          System.out.println(jo.toString());
//            testExport(jo);
//            
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//  }
    @Override
    public Result testExport(Object object) {
        Result r = new Result();
        r.setMessage("正在导出,请稍后......");
        r.setRet(Constant.SUCCESS);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //查询
                String fileName = "";
                String titleName = "课程名称为[";
                String testName = "试卷名称："; 
                String className = "班级名称:"; 
                String studentSum = "学生人数:";
                String dates = "";
                Result r = new Result();
                r.setRet(Constant.ERROR);
                FileOutputStream out = null ;
                try{
                    Record record = com.zkxltech.ui.util.StringUtils.parseJSON(object, Record.class);
                    if (StringUtils.isBlank(record.getClassId())||StringUtils.isBlank(record.getSubject())
                            ||StringUtils.isBlank(record.getClassHourId())||StringUtils.isBlank(record.getTestId())) {
                        BrowserManager.showMessage(false,"缺少参数,请检查班次id,科目,课程id,试卷id四个参数");
                        return;
                    }
                    //查询课程名称
                    ClassHourSql classHourSql = new ClassHourSql();
                    ClassHour classHour = new ClassHour();
                    classHour.setClassHourId(record.getClassHourId());
                    r = classHourSql.selectClassHour(classHour);
                    List<ClassHour> classHours = (List<ClassHour>) r.getItem();
                    if (ListUtils.isEmpty(classHours)) {
                        BrowserManager.showMessage(false,"未查询到该课程");
                        return ;
                    }
                    classHour = classHours.get(0);
                    titleName+=classHour.getClassHourName()+"]的作答详情";
                    //查询试卷名称
                    TestPaperSql testPaperSql = new TestPaperSql();
                    TestPaper testPaper = new TestPaper();
                    testPaper.setTestId(record.getTestId());
                    List<TestPaper> testPapers = (List<TestPaper>) testPaperSql.selectTestPaper(testPaper).getItem();
                    testPaper = testPapers.get(0);
                    testName+=testPaper.getTestName();
                    //查询班级名称
                    ClassInfoSql classInfoSql = new ClassInfoSql();
                    ClassInfo classInfo = new ClassInfo();
                    classInfo.setClassId(record.getClassId());
                     List<ClassInfo> classInfos= (List<ClassInfo>) classInfoSql.selectClassInfo(classInfo).getItem();
                     classInfo = classInfos.get(0);
                     className+=classInfo.getClassName();
                     
                     SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                     String date = format.format(new Date());
                     fileName += classInfo.getClassName()+classHour.getSubjectName()+classHour.getClassHourName()+date+".xls";
                     SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                     dates = "创建时间:"+date+"  作答时间:"+classHours.get(0).getStartTime();
                    //FIXEME 列数 = 6 + 该试卷的所有题目个数
                    QuestionInfoSql questionInfoSql = new QuestionInfoSql();
                    QuestionInfo questionInfo = new QuestionInfo();
                    questionInfo.setTestId(record.getTestId());
                    questionInfo.setStatus(Constant.STATUS_ENABLED);
                    r = questionInfoSql.selectQuestionInfo(questionInfo);
                    List<QuestionInfo> questionInfos = (List<QuestionInfo>) r.getItem();
                    if (ListUtils.isEmpty(questionInfos)) {
                        BrowserManager.showMessage(false,"该试卷下没有任何题目信息");
                        return ;
                    }
                    int columnNumber = 6 + questionInfos.size();
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
                    //List<Map<String, Object>> studentInfos = new ArrayList<Map<String,Object>>();
                    List<StudentInfo> studentInfos = Global.getStudentInfos();
                    if (ListUtils.isEmpty(studentInfos)) {
                        StudentInfoSql studentInfoSql = new StudentInfoSql();
                        StudentInfo studentInfo = new StudentInfo();
                        studentInfo.setClassId(record.getClassId());
                        r = studentInfoSql.selectStudentInfo(studentInfo);
                        studentInfos = (List<StudentInfo>) r.getItem();
                        if (ListUtils.isEmpty(studentInfos)) {
                            BrowserManager.showMessage(false,"未查到该班级下的学生信息");
                           return;
                        }
                    }
                    int questionSum = columnNumber - 6;//题数
                    studentSum = String.valueOf(studentInfos.size()); //学生人数
                    for (int i = 0; i < studentInfos.size(); i++) {
                        List<Object> listMaps = new ArrayList<Object>();
                        while (columnNumber > listMaps.size()) {
                            listMaps.add(null);
                        }
                        listMaps.set(0,(String) studentInfos.get(i).getIclickerId()); //答题器编号
                        String studentId = (String)studentInfos.get(i).getStudentId();//学号
                        listMaps.set(1, studentId); 
                        listMaps.set(2,(String) studentInfos.get(i).getStudentName());//姓名
                        //FIXME 每个学生的所有答题详情
                        //List<Map<String, Object>> answerMapList = new ArrayList<Map<String,Object>>();
                        RecordSql recordSql = new RecordSql();
                        record.setStudentId(studentInfos.get(i).getStudentId());
                        Result re = recordSql.selectRecord(record);
                        List<Record> answerMapList = (List<Record>) re.getItem();
                        double scoreSum = 0;
                        double trueSum = 0; //正确题数
                        for (int j = 0; j < answerMapList.size(); j++) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            String answer = (String) answerMapList.get(j).getAnswer();
                            String score = (String) answerMapList.get(j).getScore();
                            String type = (String) answerMapList.get(j).getQuestionType();
                            String result = (String) answerMapList.get(j).getResult();
                            int questionId = Integer.parseInt((String) answerMapList.get(j).getQuestionId());
                            if (score != null && !"".equals(score) && !"null".equals(score)) {
                                if (!Constant.ZHUGUANTI_NUM.equals(type)) { //客观题得分需要正确
                                    if (Constant.RESULT_TRUE.equals(result)) {
                                        scoreSum += Double.parseDouble(score);
                                    }
                                }else { //主观题得分
                                    if (!StringUtils.isBlank(answer)) {
                                        scoreSum += Double.parseDouble(answer);
                                    }
                                }
                            }
                            if (Constant.RESULT_TRUE.equals(result)) {
                                trueSum ++;
                            }
                            map.put("type", type);
                            if ( answer == null ||answer.equals("null") || answer.equals("")) {
                                answer = "";
                            }else if("true".equals(answer)){
                            	answer = "√";
                            }else if("false".equals(answer)){
                            	answer = "×";
                            }
                            map.put("answer", answer);
                            map.put("result", result);
                            listMaps.set(6+j,map);
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
                    File file = new File(flieUrl);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    out = new FileOutputStream(new File(flieUrl,fileName));
                    wb.write(out);// 将数据写出去  
                    out.flush();// 将数据写出去
                    BrowserManager.showMessage(true,"导出成功");
                    openFile();
                }catch (Exception e) {
                    log.error("", e);
                    r.setMessage("导出失败");
                    r.setDetail(IOUtils.getError(e));
                    BrowserManager.showMessage(false,"导出失败");
                }finally {
                    BrowserManager.removeLoading();
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            log.error("输出流关闭失败", e);
                        } 
                    }
                }
            }
        }).start();
        return r;
    }
    /**
     * 查询答题记录
     * @param object
     * @return
     */
    @Override
    public Result selectRecord(Object object) {
        Result result2 = new Result();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Result r = new Result();
                r.setRet(Constant.ERROR);
                try {
                    Record record = com.zkxltech.ui.util.StringUtils.parseJSON(object, Record.class);
                    if (StringUtils.isBlank(record.getClassHourId())||StringUtils.isBlank(record.getTestId())) {
                        r.setMessage("缺少参数:课程id和试卷id不能为空");
                        BrowserManager.refreSelectRecord(JSONObject.fromObject(r).toString());
                        return ;
                    }
                    //查询课程的开始时间
                    ClassHourSql classHourSql = new ClassHourSql();
                    ClassHour classHour = new ClassHour();
                    classHour.setClassHourId(record.getClassHourId());
                    r = classHourSql.selectClassHour(classHour);
                    if (r.getRet().equals(Constant.ERROR)) {
                        BrowserManager.refreSelectRecord(JSONObject.fromObject(r).toString());
                        return;
                    }
                    List<ClassHour>  classHours = (List<ClassHour>) r.getItem();
                    classHour = classHours.get(0);
                    if (classHour == null) {
                        r.setMessage("未查询到该课时信息");
                        BrowserManager.refreSelectRecord(JSONObject.fromObject(r).toString());
                        return;
                    }
                    //查试卷的详情
                    TestPaperSql testPaperSql = new TestPaperSql();
                    TestPaper testPaper = new TestPaper();
                    testPaper.setTestId(record.getTestId());
                    r = testPaperSql.selectTestPaper(testPaper);
                    if (r.getRet().equals(Constant.ERROR)) {
                        BrowserManager.refreSelectRecord(JSONObject.fromObject(r).toString());
                        return ;
                    }
                    List<TestPaper> testPapers = (List<TestPaper>) r.getItem();
                    testPaper = testPapers.get(0);
                    if (testPaper == null) {
                        r.setMessage("未查询到该试卷信息");
                        BrowserManager.refreSelectRecord(JSONObject.fromObject(r).toString());
                        return ;
                    }
                    //查试卷的题目总数
                    QuestionInfoSql questionInfoSql = new QuestionInfoSql();
                    QuestionInfo questionInfo = new QuestionInfo();
                    questionInfo.setTestId(record.getTestId());
                    questionInfo.setStatus(Constant.STATUS_ENABLED);
                    r = questionInfoSql.selectQuestionInfo(questionInfo);
                    if (r.getRet().equals(Constant.ERROR)) {
                        BrowserManager.refreSelectRecord(JSONObject.fromObject(r).toString());
                        return ;
                    }
                    List<QuestionInfo> questInfos = (List<QuestionInfo>) r.getItem();
                    if (ListUtils.isEmpty(questInfos)) {
                        r.setMessage("该试卷没有对应题目");
                        BrowserManager.refreSelectRecord(JSONObject.fromObject(r).toString());
                        return ;
                    }
                    //查试卷的所有答题记录
                    RecordSql recordSql = new RecordSql();
                    r = recordSql.selectRecord(record);
                    if (r.getRet().equals(Constant.ERROR)) {
                        BrowserManager.refreSelectRecord(JSONObject.fromObject(r).toString());
                        return ;
                    }
                    //查询到所有学生的所有答题数据
                    List<Record> records = (List<Record>) r.getItem();
                    if (ListUtils.isEmpty(records)) {
                        r.setMessage("未查询到该试卷任意答题记录");
                        BrowserManager.refreSelectRecord(JSONObject.fromObject(r).toString());
                        return ;
                    }
                    //根据学生id进行分组,分别计算每个学生的正确率
                    Map<Object, List<Record>> studentRecordMap = ListUtils.getClassificationMap(records, "studentId");
                    //用来存返回数据
                    List<Record> result = new ArrayList<>();
                    for (Object key : studentRecordMap.keySet()) {
                        List<Record> list = studentRecordMap.get(key);//得到每个学生的所有答题记录
                        //按正确和错误进行分类
                        Map<Object, List<Record>> resultMap = ListUtils.getClassificationMap(list, "result");
                        float b = 0;
                        float score = 0;
                        if (resultMap != null && resultMap.size() > 0) {
                            List<Record> corrects = resultMap.get(Constant.RESULT_TRUE);//得到所有正确的答案总数
                            if (!com.zkxltech.ui.util.StringUtils.isEmptyList(corrects)) {
                            	  b = (float)corrects.size() / questInfos.size();
                                for (Record correct:corrects) {
                                    score = score + Float.parseFloat(correct.getScore());
                                }
							}
                        }
                        Record resultRocord = new Record();
                        resultRocord.setStudentId((String)key);
                        resultRocord.setStudentName(list.get(0).getStudentName());
                        resultRocord.setScore(String.valueOf(score));
                        resultRocord.setPercentage(b);
                        resultRocord.setTestName(testPaper.getTestName());
                        resultRocord.setTime(classHour.getStartTime());
                        result.add(resultRocord);
                    }
                    if (!ListUtils.isEmpty(result)) {
                        result = result.stream().sorted(Comparator.comparing(Record::getPercentage).reversed())
                                .collect(Collectors.toList());
                    }
                    for (Record record2 : result) {//格式化成百分比
                        record2.setResult(StringUtils.formattedDecimalToPercentage(record2.getPercentage()));
                    }
                    r.setItem(result);
                    r.setRet(Constant.SUCCESS);
                    BrowserManager.refreSelectRecord(JSONObject.fromObject(r).toString());
                } catch (Exception e) {
                    r.setMessage("查询数据库失败");
                    r.setDetail(IOUtils.getError(e));
                    log.error(IOUtils.getError(e));
                }finally {
					BrowserManager.removeLoading();
				}
            }
        }).start();
        return result2;
        
    }
    @Override
    public Result selectSubjectiveRecord(Object object) {
        result = new Result();
        try {
            Record record = com.zkxltech.ui.util.StringUtils.parseJSON(object, Record.class);
            record.setClassId(Global.getClassId());
            record.setSubject(Global.getClassHour().getSubjectName());
            record.setQuestionType("4");
            record.setClassHourId(Global.getClassHour().getClassHourId());
            result = recordSql.selectRecord(record);
            if (Constant.ERROR.equals(result.getRet())) {
                result.setMessage("查询记录失败!");
                return result;
            }
            result.setMessage("查询记录成功!");
            return result;
        } catch (Exception e) {
            result.setRet(Constant.ERROR);
            result.setMessage("查询记录失败！");
            result.setDetail(IOUtils.getError(e));
            log.error(IOUtils.getError(e));
            return result;
        }
    }
    
    @Override
    public Result selectObjectiveRecord(Object object) {
        result = new Result();
        try {
            Record record = com.zkxltech.ui.util.StringUtils.parseJSON(object, Record.class);
            record.setClassId(Global.getClassId());
            record.setSubject(Global.getClassHour().getSubjectName());
            record.setClassHourId(Global.getClassHour().getClassHourId());
            result = recordSql.selectRecord(record);
            if (Constant.ERROR.equals(result.getRet())) {
                result.setMessage("查询记录失败!");
                return result;
            }
            List<Record> records = (List<Record>) result.getItem();
            List<Record> retList = new ArrayList<Record>();
            for (int i = 0; i < records.size(); i++) {
                if (!Constant.ZHUGUANTI_NUM.equals(records.get(i).getQuestionType())) {
                    retList.add(records.get(i));
                }
            }
            result.setItem(retList);
            result.setMessage("查询记录成功!");
            return result;
        } catch (Exception e) {
            result.setRet(Constant.ERROR);
            result.setMessage("查询记录失败！");
            result.setDetail(IOUtils.getError(e));
            log.error(IOUtils.getError(e));
            return result;
        }
    }

    @Override
    public Result deleteRecord(Object object) {
        Result r = new Result();
        r.setRet(Constant.ERROR);
        try {
            Record record = com.zkxltech.ui.util.StringUtils.parseJSON(object, Record.class);
            if (StringUtils.isBlank(record.getTestId())||record.getStudentIds()== null || record.getStudentIds().size() < 1) {
                r.setMessage("试卷id和学生id参数不能为空");
                return r;
            }
            RecordSql sql = new RecordSql();
            r = sql.deleteRecordByStudentId(record);
            if (r.getRet().equals(Constant.ERROR)) {
                return r;
            }
        } catch (Exception e) {
            r.setMessage("删除失败");
            r.setDetail(IOUtils.getError(e));
            log.error(IOUtils.getError(e));
        }
        r.setRet(Constant.SUCCESS);
        r.setMessage("删除成功");
        return r;
    }
    public void openFile() throws IOException{
        String flieUrl = System.getProperty("user.dir").replaceAll("\\\\", "/") + "/"+"excels/";
        Desktop.getDesktop().open(new File(flieUrl));
    }
    
    @Override
    public Result selectStudentRecordDetail(Object object) {
        Result r = new Result();
        r.setRet(Constant.ERROR);
        try {
            Record record = com.zkxltech.ui.util.StringUtils.parseJSON(object, Record.class);
            if (StringUtils.isBlank(record.getClassHourId())||StringUtils.isBlank(record.getClassId())||StringUtils.isBlank(record.getStudentId())
                    ||StringUtils.isBlank(record.getTestId())||StringUtils.isEmpty(record.getSubject())) {
                r.setMessage("缺少参数,班级id,课程,课时id,试卷id,学生id均不能为空");
                return r;
            }
            RecordSql recordSql = new RecordSql();
            r = recordSql.selectRecord(record);
        } catch (Exception e) {
            log.error(IOUtils.getError(e));
            r.setMessage("查询失败");
        }
        return r;
    }
}
