package com.zkxltech.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.domain.Result;
import com.zkxltech.service.RecordService;
import com.zkxltech.ui.util.ExportExcel;

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
    public Result selectRecord(Object object) {
        
        return null;
    }

}
