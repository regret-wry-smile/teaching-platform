package com.zkxltech.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.zkxltech.ui.util.StringUtils;
import com.zkxlteck.scdll.ScDll;
import com.zkxlteck.thread.AttendanceThread;
import com.zkxlteck.thread.MultipleAnswerThread;
import com.zkxlteck.thread.SingleAnswerThread;

public class AnswerInfoServiceImpl implements AnswerInfoService{
    private static final Logger log = LoggerFactory.getLogger(AnswerInfoServiceImpl.class);
	private Result result;
	private RecordSql recordSql = new RecordSql();
	private static Thread thread;
    
    public static Thread getThread() {
        return thread;
    }

    public static void setThread(Thread thread) {
        AnswerInfoServiceImpl.thread = thread;
    }
	@Override
	public Result startMultipleAnswer(Object object) {
		result = new Result();
		try {
			RequestVo requestVo = StringUtils.parseJSON(object, RequestVo.class);
			List<RequestVo> list = new ArrayList<RequestVo>();
			list.add(requestVo);
			result = EquipmentServiceImpl.getInstance().answerStart2(Constant.ANSWER_MULTIPLE_TYPE,list);
			if (Constant.ERROR.equals(result.getRet())) {
				result.setRet(Constant.ERROR);
				result.setMessage("开始答题指令发送失败！");
				return result;
			}
			RedisMapMultipleAnswer.clearMap();
			RedisMapMultipleAnswer.startAnswer(requestVo.getRange());
			result.setRet(Constant.SUCCESS);
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
				requestVo.setType(questionInfos2.get(i).getQuestionType());
				requestVo.setRange(questionInfos2.get(i).getRange());
				requestVos.add(requestVo);
			}
			
			result = EquipmentServiceImpl.getInstance().answerStart2(Constant.ANSWER_CLASS_TEST_OBJECTIVE,requestVos); //发送硬件指令
			if (Constant.ERROR.equals(result.getRet())) {
				result.setMessage("硬件指令发送失败！");
				return result;
			}
			result.setRet(Constant.SUCCESS);
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage(e.getMessage());
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result stopObjectiveAnswer() {
		result = new Result();
		try {
			List<Record> records = RedisMapClassTestAnswer.getRecordList();
			result =recordSql.insertRecords(records); //将缓存中数据保存到数据库
			if (Constant.ERROR.equals(result.getRet())) {
				result.setMessage("保存作答记录失败！");
				return result;
			}
//			result = EquipmentServiceImpl.getInstance().answerStart2(requestVos); //发送硬件指令
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("保存作答记录失败！");
			return result;
		}
		return result;
	}
	@Override
    public Result singleAnswer(Object param) {
        Result r = new Result();
        r.setRet(Constant.ERROR);
        Answer answer = com.zkxltech.ui.util.StringUtils.parseJSON(param, Answer.class);
        if (answer == null || StringUtils.isEmpty(answer.getType())) {
            r.setMessage("缺少参数,题目类型不能为空");
            return r;
        }
        //传入类型 ,清空数据
        RedisMapSingleAnswer.setAnswer(answer);
        RedisMapSingleAnswer.clearSingleAnswerNumMap();
        RedisMapSingleAnswer.clearStudentInfoMap();
        RedisMapSingleAnswer.clearSingleAnswerStudentNameMap();
        RedisMapSingleAnswer.cleariclickerIdsSet();
        List<StudentInfo> studentInfos = Global.getStudentInfos();
        if (ListUtils.isEmpty(studentInfos)) {
            r.setMessage("未获取到当前班次学生信息");
            return r;
        }
        Map<String,StudentInfo> map = new HashMap<>();
        RedisMapSingleAnswer.setStudentInfoMap(map);
        for (StudentInfo studentInfo : studentInfos) {
            map.put(studentInfo.getIclickerId(), studentInfo);
        }
        String type = answer.getType();
        int status = -1;
        switch (type) {
            case Constant.ANSWER_CHAR_TYPE:
                status = ScDll.intance.answer_start(0, Constant.SINGLE_ANSWER_CHAR);
                if (status == Constant.SEND_ERROR) {
                    status = ScDll.intance.answer_start(0, Constant.SINGLE_ANSWER_CHAR);
                }
                RedisMapSingleAnswer.getSingleAnswerNumMap().put(RedisMapSingleAnswer.CHAR_A, 0);
                RedisMapSingleAnswer.getSingleAnswerNumMap().put(RedisMapSingleAnswer.CHAR_B, 0);
                RedisMapSingleAnswer.getSingleAnswerNumMap().put(RedisMapSingleAnswer.CHAR_C, 0);
                RedisMapSingleAnswer.getSingleAnswerNumMap().put(RedisMapSingleAnswer.CHAR_D, 0);
                break;
            case Constant.ANSWER_NUMBER_TYPE:
                status = ScDll.intance.answer_start(0, Constant.SINGLE_ANSWER_NUMBER);
                if (status == Constant.SEND_ERROR) {
                    status = ScDll.intance.answer_start(0, Constant.SINGLE_ANSWER_NUMBER);
                }
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
                status = ScDll.intance.answer_start(0, Constant.SINGLE_ANSWER_JUDGE);
                if (status == Constant.SEND_ERROR) {
                    status = ScDll.intance.answer_start(0, Constant.SINGLE_ANSWER_JUDGE);
                }
                RedisMapSingleAnswer.getSingleAnswerNumMap().put(RedisMapSingleAnswer.JUDGE_TRUE, 0);
                RedisMapSingleAnswer.getSingleAnswerNumMap().put(RedisMapSingleAnswer.JUDGE_FALSE, 0);
                break;
            default:
                r.setMessage("参数错误");
                return r;
        }
        if (status == Constant.SEND_ERROR) {
            r.setMessage("指令发送失败");
            return r;
        }
        
        thread = new SingleAnswerThread();
        thread.start();
        r.setRet(Constant.SUCCESS);
        return r;
    }

    @Override
    public Result stopSingleAnswer() {
        Result r = new Result();
        if (thread != null && thread instanceof SingleAnswerThread ) {
            AttendanceThread a = (AttendanceThread)thread;
            a.setFLAG(false);
            log.info("单选线程停止成功");
        }else{
            log.error("单选线程停止失败");;
        }
        r = EquipmentServiceImpl.getInstance().answer_stop();
        if (r.getRet().equals(Constant.ERROR)) {
            return r;
        }
        r.setRet(Constant.SUCCESS);
        r.setMessage("停止成功");
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
        if (EquipmentServiceImpl.getThread() != null && EquipmentServiceImpl.getThread() instanceof MultipleAnswerThread ) {
            AttendanceThread a = (AttendanceThread)thread;
            a.setFLAG(false);
            log.info("多选线程停止成功");
        }else{
            log.error("多选线程停止失败");;
        }
        r = EquipmentServiceImpl.getInstance().answer_stop();
        if (r.getRet().equals(Constant.ERROR)) {
            return r;
        }
        r.setRet(Constant.SUCCESS);
        r.setMessage("停止成功");
        return r;
    }
}
