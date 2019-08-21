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
				result.setMessage("Failed to send instructions to start answering questions！");
				return result;
			}
			result.setRet(Constant.SUCCESS);
			Global.setModeMsg(Constant.BUSINESS_ANSWER);
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("Failed to send instructions to start answering questions！");
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
				result.setMessage("Failed to check the questions in the paper!");
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
				result.setMessage("The paper has no objective questions！");
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
				result.setMessage("Hardware instruction sending failed！");
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
						BrowserManager.showMessage(false, "Hardware instruction sending failed！");
						return ;
					}
					//删除原来的记录
					List<Record> records = RedisMapClassTestAnswer.getObjectiveRecordList();
					Record recordParam = new Record();
					recordParam.setClassId(Global.getClassId());
					recordParam.setSubject(Global.getClassHour().getSubjectName());
					recordParam.setTestId((String)testId);
					recordParam.setClassHourId(Global.getClassHour().getClassHourId());
					recordSql.deleteRecord(recordParam);
					
					result =recordSql.insertRecords(records); //将缓存中数据保存到数据库
					if (Constant.ERROR.equals(result.getRet())) {
						BrowserManager.showMessage(false, "Failed to save response record！");
					}else {
						BrowserManager.showMessage(true, "Save response record successfully！");
					}
//					result = EquipmentServiceImpl2.getInstance().answerStart2(requestVos); //发送硬件指令
				} catch (Exception e) {
					BrowserManager.showMessage(false, "Failed to save response record！");
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
						BrowserManager.showMessage(false, "Hardware instruction sending failed！");
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
						BrowserManager.showMessage(false, "Failed to save response record！");
						return ;
					}else {
						BrowserManager.showMessage(true, "Save response record successfully！");
						return ;
					}
				} catch (Exception e) {
					BrowserManager.showMessage(false, "Failed to save response record！");
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
                r.setMessage("Missing parameter,The topic type cannot be empty");
                return r;
            }
            //传入类型 ,清空数据
            RedisMapSingleAnswer.setAnswer(answer);
            
            //总的答题人数
            List<StudentInfo> studentInfos = Global.getStudentInfos();
            if (ListUtils.isEmpty(studentInfos)) {
                r.setMessage("Student information for current shift is not available");
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
                    r.setMessage("Parameter error");
                    return r;
            }
            if (r.getRet() == Constant.ERROR) {
	            r.setMessage("Instruction sending failure");
	            return r;
	        }
	        BaseThread thread = new SingleAnswerThread();
	        thread.start();
	        ThreadManager.getInstance().addThread(thread);
            r.setRet(Constant.SUCCESS);
            Global.setModeMsg(Constant.BUSINESS_ANSWER);
        }catch (Exception e) {
            log.error(IOUtils.getError(e));
            r.setMessage("System Exception");
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
            r.setMessage("Stop Success");
        }catch (Exception e) {
        	 log.error(IOUtils.getError(e));
            r.setMessage("System Exception");
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
			result.setMessage("Success in obtaining response data！");
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("Failed to obtain response data！");
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
        r.setMessage("Stop Success");
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
				result.setMessage("Failed to check the questions in the paper!");
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
				result.setMessage("The paper has no subjective questions！");
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
				result.setMessage("Hardware instruction sending failed！");
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
        r.setMessage("Startup check successful");
        return r;
    }
    @Override
    public Result checkEquipmentStatusStop(){
        Result r = new Result();
        r.setRet(Constant.SUCCESS);
        if (equipmentStatusThread != null) {
            equipmentStatusThread.stopThread();
            r.setMessage("Stop Success");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("Stop Failure");
        return r;
    }
	
}
