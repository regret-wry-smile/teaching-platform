package com.zkxltech.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.cache.BrowserManager;
import com.ejet.cache.RedisMapClassTestAnswer;
import com.ejet.cache.RedisMapMultipleAnswer;
import com.ejet.cache.RedisMapSingleAnswer;
import com.ejet.core.util.comm.ListUtils;
import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.constant.Global;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.domain.Answer;
import com.zkxltech.domain.QuestionInfo;
import com.zkxltech.domain.Record;
import com.zkxltech.domain.RequestVo;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.service.AnswerInfoService;
import com.zkxltech.sql.RecordSql;
import com.zkxltech.thread.BaseThread;
import com.zkxltech.thread.EquipmentStatusThread;
import com.zkxltech.thread.SingleAnswerThread;
import com.zkxltech.thread.ThreadManager;
import com.zkxltech.ui.util.StringUtils;

public class AnswerInfoServiceImpl implements AnswerInfoService{
    private static final Logger log = LoggerFactory.getLogger(AnswerInfoServiceImpl.class);
	private Result result;
	private RecordSql recordSql = new RecordSql();
	private static BaseThread equipmentStatusThread;
    
	@Override
	public Result startMultipleAnswer(Object object) {
		result = new Result();
		RedisMapMultipleAnswer.clearMap();
		try {
			RequestVo requestVo = StringUtils.parseJSON(object, RequestVo.class);
			List<RequestVo> list = new ArrayList<RequestVo>();
			list.add(requestVo);
			RedisMapMultipleAnswer.startAnswer(requestVo.getRange());
			result = EquipmentServiceImpl.getInstance().answerStart2(Constant.ANSWER_MULTIPLE_TYPE,list);
			if (Constant.ERROR.equals(result.getRet())) {
				result.setRet(Constant.ERROR);
				result.setMessage("开始答题指令发送失败！");
				return result;
			}
			result.setRet(Constant.SUCCESS);
			Global.setModeMsg(Constant.BUSINESS_ANSWER);
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("开始答题指令发送失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}
	@Override
	public Result startObjectiveAnswer(Object testId) {
		result = new Result();
		try {
			//获取试卷
			QuestionInfo questionInfoParm = new QuestionInfo();
			questionInfoParm.setTestId((String)testId);
			Result result = new QuestionServiceImpl().selectQuestion(questionInfoParm);	
			if (Constant.ERROR.equals(result.getRet())) {
				result.setRet(Constant.ERROR);
				result.setMessage("查询试卷题目失败!");
				return result;
			}
			
			//筛选主观题
			List<QuestionInfo> questionInfos = (List<QuestionInfo>)result.getItem();
			List<QuestionInfo> questionInfos2 = new ArrayList<QuestionInfo>();
			for (int i = 0; i < questionInfos.size(); i++) {
				if (!Constant.ZHUGUANTI_NUM.equals(questionInfos.get(i).getQuestionType())) {
					questionInfos2.add(questionInfos.get(i));
				}
			}
			
			if (StringUtils.isEmptyList(questionInfos2)) {
				result.setMessage("该试卷没有客观题目！");
				result.setRet(Constant.ERROR);
				return result;
			}


			RedisMapClassTestAnswer.startClassTest(questionInfos2); //缓存初始化
			
			List<RequestVo> requestVos = new ArrayList<RequestVo>();
			for (int i = 0; i < questionInfos2.size(); i++) {
				RequestVo requestVo = new RequestVo();
				requestVo.setId(questionInfos2.get(i).getQuestionId());
				if (Constant.DANXUANTI_NUM.equals(questionInfos2.get(i).getQuestionType())) {
					requestVo.setType("s");
				}else if (Constant.DUOXUANTI_NUM.equals(questionInfos2.get(i).getQuestionType())) {
					requestVo.setType("m");
				}else if (Constant.PANDUANTI_NUM.equals(questionInfos2.get(i).getQuestionType())) {
					requestVo.setType("j");
				}else if (Constant.SHUZITI_NUM.equals(questionInfos2.get(i).getQuestionType())) {
					requestVo.setType("d");
				}else {
					requestVo.setType("g");
				}
				
				requestVo.setRange(questionInfos2.get(i).getRange());
				requestVos.add(requestVo);
			}
			
			result = EquipmentServiceImpl.getInstance().answerStart2(Constant.ANSWER_CLASS_TEST_OBJECTIVE,requestVos); //发送硬件指令
			if (Constant.ERROR.equals(result.getRet())) {
				result.setMessage("硬件指令发送失败！");
				return result;
			}
		
			result.setRet(Constant.SUCCESS);
			Global.setModeMsg(Constant.BUSINESS_CLASSTEST);
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage(e.getMessage());
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result stopObjectiveAnswer(Object testId) {

		result = new Result();
		/*停止所有线程*/
		ThreadManager.getInstance().stopAllThread();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//调用硬件停止指令
					result = EquipmentServiceImpl.getInstance().answer_stop(); //发送硬件指令
					if (Constant.ERROR.equals(result.getRet())) {
						BrowserManager.showMessage(false, "硬件指令发送失败！");
						return ;
					}
					//删除原来的记录
					List<Record> records = RedisMapClassTestAnswer.getObjectiveRecordList();
					Record recordParam = new Record();
					recordParam.setClassId(Global.getClassId());
					recordParam.setSubject(Global.getClassHour().getSubjectName());
					recordParam.setTestId((String)testId);
					recordSql.deleteRecord(recordParam);
					
					result =recordSql.insertRecords(records); //将缓存中数据保存到数据库
					if (Constant.ERROR.equals(result.getRet())) {
						BrowserManager.showMessage(false, "保存作答记录失败！");
					}else {
						BrowserManager.showMessage(true, "保存作答记录成功！");
					}
//					result = EquipmentServiceImpl2.getInstance().answerStart2(requestVos); //发送硬件指令
				} catch (Exception e) {
					BrowserManager.showMessage(false, "保存作答记录失败！");
				}finally {
					BrowserManager.removeLoading();
					Global.setModeMsg(Constant.BUSINESS_NORMAL);
				}
			}
		}).start();
		return result;
	}
	
	@Override
	public Result stopSubjectiveAnswer(Object testId) {
		result = new Result();
		/*停止所有线程*/
		ThreadManager.getInstance().stopAllThread();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					//调用硬件停止指令
					result = EquipmentServiceImpl.getInstance().answer_stop(); //发送硬件指令
					if (Constant.ERROR.equals(result.getRet())) {
						BrowserManager.showMessage(false, "硬件指令发送失败！");
						return ;
					}
					//删除原来的记录
					List<Record> records = RedisMapClassTestAnswer.getSubjectiveRecordList();
					Record recordParam = new Record();
					recordParam.setClassId(Global.getClassId());
					recordParam.setSubject(Global.getClassHour().getSubjectName());
					recordParam.setTestId((String)testId);
					recordSql.deleteRecord(recordParam);
					
					result =recordSql.insertRecords(records); //将缓存中数据保存到数据库
					if (Constant.ERROR.equals(result.getRet())) {
						BrowserManager.showMessage(false, "保存作答记录失败！");
						return ;
					}else {
						BrowserManager.showMessage(true, "保存作答记录成功！");
						return ;
					}
				} catch (Exception e) {
					BrowserManager.showMessage(false, "保存作答记录失败！");
					return ;
				}finally {
					BrowserManager.removeLoading();
					Global.setModeMsg(Constant.BUSINESS_NORMAL);
				}
			}
		}).start();
		
		return result;
	}
	
	@Override
    public Result singleAnswer(Object param) {
        Result r = new Result();
        r.setRet(Constant.ERROR);
        /*停止所有线程*/
        ThreadManager.getInstance().stopAllThread();
        RedisMapSingleAnswer.clearSingleAnswerNumMap();
        RedisMapSingleAnswer.clearStudentInfoMap();
        RedisMapSingleAnswer.clearSingleAnswerStudentNameMap();
        RedisMapSingleAnswer.clearIclickerAnswerMap();
        try{
            Answer answer = com.zkxltech.ui.util.StringUtils.parseJSON(param, Answer.class);
            if (answer == null || StringUtils.isEmpty(answer.getType())) {
                r.setMessage("缺少参数,题目类型不能为空");
                return r;
            }
            //传入类型 ,清空数据
            RedisMapSingleAnswer.setAnswer(answer);
            
            //总的答题人数
            List<StudentInfo> studentInfos = Global.getStudentInfos();
            if (ListUtils.isEmpty(studentInfos)) {
                r.setMessage("未获取到当前班次学生信息");
                return r;
            }
            Map<String,StudentInfo> map = new HashMap<>();
            RedisMapSingleAnswer.setStudentInfoMap(map);
			for (int i = 0;i< studentInfos.size();i++) {
				StudentInfo studentInfo = studentInfos.get(i);
				if ("************".equals(studentInfo.getIclickerId()) || com.zkxltech.ui.util.StringUtils.isEmpty(studentInfo.getIclickerId())){
					continue;
				}
				map.put(studentInfo.getIclickerId(), studentInfo);
            }
            String type = answer.getType();
            switch (type) {
                case Constant.ANSWER_CHAR_TYPE:
                    r = EquipmentServiceImpl.getInstance().answer_start(0, Constant.SINGLE_ANSWER_CHAR);
                    RedisMapSingleAnswer.getSingleAnswerNumMap().put(RedisMapSingleAnswer.CHAR_A, 0);
                    RedisMapSingleAnswer.getSingleAnswerNumMap().put(RedisMapSingleAnswer.CHAR_B, 0);
                    RedisMapSingleAnswer.getSingleAnswerNumMap().put(RedisMapSingleAnswer.CHAR_C, 0);
                    RedisMapSingleAnswer.getSingleAnswerNumMap().put(RedisMapSingleAnswer.CHAR_D, 0);
                    break;
                case Constant.ANSWER_NUMBER_TYPE:
                    r = EquipmentServiceImpl.getInstance().answer_start(0, Constant.SINGLE_ANSWER_NUMBER);
                    RedisMapSingleAnswer.getSingleAnswerNumMap().put(RedisMapSingleAnswer.NUMBER_1, 0);
                    RedisMapSingleAnswer.getSingleAnswerNumMap().put(RedisMapSingleAnswer.NUMBER_2, 0);
                    RedisMapSingleAnswer.getSingleAnswerNumMap().put(RedisMapSingleAnswer.NUMBER_3, 0);
                    RedisMapSingleAnswer.getSingleAnswerNumMap().put(RedisMapSingleAnswer.NUMBER_4, 0);
                    RedisMapSingleAnswer.getSingleAnswerNumMap().put(RedisMapSingleAnswer.NUMBER_5, 0);
                    RedisMapSingleAnswer.getSingleAnswerNumMap().put(RedisMapSingleAnswer.NUMBER_6, 0);
                    RedisMapSingleAnswer.getSingleAnswerNumMap().put(RedisMapSingleAnswer.NUMBER_7, 0);
                    RedisMapSingleAnswer.getSingleAnswerNumMap().put(RedisMapSingleAnswer.NUMBER_8, 0);
                    RedisMapSingleAnswer.getSingleAnswerNumMap().put(RedisMapSingleAnswer.NUMBER_9, 0);
                    break;
                case Constant.ANSWER_JUDGE_TYPE:
                    r = EquipmentServiceImpl.getInstance().answer_start(0, Constant.SINGLE_ANSWER_JUDGE);
                    RedisMapSingleAnswer.getSingleAnswerNumMap().put(RedisMapSingleAnswer.JUDGE_TRUE, 0);
                    RedisMapSingleAnswer.getSingleAnswerNumMap().put(RedisMapSingleAnswer.JUDGE_FALSE, 0);
                    break;
                default:
                    r.setMessage("参数错误");
                    return r;
            }
            if (r.getRet() == Constant.ERROR) {
	            r.setMessage("指令发送失败");
	            return r;
	        }
	        BaseThread thread = new SingleAnswerThread();
	        thread.start();
	        ThreadManager.getInstance().addThread(thread);
            r.setRet(Constant.SUCCESS);
            Global.setModeMsg(Constant.BUSINESS_ANSWER);
        }catch (Exception e) {
            log.error(IOUtils.getError(e));
            r.setMessage("系统异常");
            r.setDetail(IOUtils.getError(e));
        }
        return r;
    }

    @Override
    public Result stopSingleAnswer() {
        Result r = new Result();
        r.setRet(Constant.ERROR);
        try{
            ThreadManager.getInstance().stopAllThread();
            Global.setModeMsg(Constant.BUSINESS_NORMAL);
            ThreadManager.getInstance().stopAllThread();
            r = EquipmentServiceImpl.getInstance().answer_stop();
            if (r.getRet().equals(Constant.ERROR)) {
                return r;
            }
            r.setRet(Constant.SUCCESS);
            r.setMessage("停止成功");
        }catch (Exception e) {
        	 log.error(IOUtils.getError(e));
            r.setMessage("系统异常");
            r.setDetail(IOUtils.getError(e));
        }
        return r;
    }
    
    @Override
	public Result getEverybodyAnswerInfo() {
		result = new Result();
		try {
			result.setItem(RedisMapClassTestAnswer.getEverybodyAnswerInfo());;
			result.setRet(Constant.SUCCESS);
			result.setMessage("获取作答数据成功！");
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("获取作答数据失败！");
			result.setDetail(IOUtils.getError(e));
		}
		return result;
	}

    @Override
    public Result stopMultipleAnswer() {
        Result r = new Result();
        Global.setModeMsg(Constant.BUSINESS_NORMAL);
        /*停止所有线程*/
	    ThreadManager.getInstance().stopAllThread();
        r = EquipmentServiceImpl.getInstance().answer_stop();
        if (r.getRet().equals(Constant.ERROR)) {
            return r;
        }
       
        r.setRet(Constant.SUCCESS);
        r.setMessage("停止成功");
        return r;
    }

	@Override
	public Result startSubjectiveAnswer(Object testId) {
		result = new Result();
		try {
			//获取试卷
			QuestionInfo questionInfoParm = new QuestionInfo();
			questionInfoParm.setTestId((String)testId);
			Result result = new QuestionServiceImpl().selectQuestion(questionInfoParm);	
			if (Constant.ERROR.equals(result.getRet())) {
				result.setRet(Constant.ERROR);
				result.setMessage("查询试卷题目失败!");
				return result;
			}
			
			//筛选客观题
			List<QuestionInfo> questionInfos = (List<QuestionInfo>)result.getItem();
			List<QuestionInfo> questionInfos2 = new ArrayList<QuestionInfo>();
			for (int i = 0; i < questionInfos.size(); i++) {
				if (Constant.ZHUGUANTI_NUM.equals(questionInfos.get(i).getQuestionType())) {
					questionInfos2.add(questionInfos.get(i));
				}
			}
			
			if (StringUtils.isEmptyList(questionInfos2)) {
				result.setMessage("该试卷没有主观题目！");
				result.setRet(Constant.ERROR);
				return result;
			}
			
			RedisMapClassTestAnswer.startClassTest(questionInfos2); //缓存初始化
			
			List<RequestVo> requestVos = new ArrayList<RequestVo>();
			for (int i = 0; i < questionInfos2.size(); i++) {
				RequestVo requestVo = new RequestVo();
				requestVo.setId(questionInfos2.get(i).getQuestionId());
				requestVo.setType("d");
				requestVo.setRange("0-"+(int)Double.parseDouble(questionInfos2.get(i).getScore()));
				requestVos.add(requestVo);
			}
			
			result = EquipmentServiceImpl.getInstance().answerStart2(Constant.ANSWER_CLASS_TEST_SUBJECTIVE,requestVos); //发送硬件指令
			if (Constant.ERROR.equals(result.getRet())) {
				result.setMessage("硬件指令发送失败！");
				return result;
			}
			
			result.setRet(Constant.SUCCESS);
			Global.setModeMsg(Constant.BUSINESS_ANSWER);
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage(e.getMessage());
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

    @Override
    public Result checkEquipmentStatusStart() {
        Result r = new Result();
        r.setRet(Constant.SUCCESS);
        equipmentStatusThread = new EquipmentStatusThread();
        equipmentStatusThread.start();
        r.setItem(Global.isEquipmentStatus);
        r.setMessage("启动检查成功");
        return r;
    }
    @Override
    public Result checkEquipmentStatusStop(){
        Result r = new Result();
        r.setRet(Constant.SUCCESS);
        if (equipmentStatusThread != null) {
            equipmentStatusThread.stopThread();
            r.setMessage("停止成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("停止失败");
        return r;
    }
	
}
