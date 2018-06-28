package com.zkxltech.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ejet.cache.RedisMapClassTestAnswer;
import com.ejet.cache.RedisMapMultipleAnswer;
import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.domain.QuestionInfo;
import com.zkxltech.domain.Record;
import com.zkxltech.domain.RequestVo;
import com.zkxltech.domain.Result;
import com.zkxltech.service.AnswerInfoService;
import com.zkxltech.sql.RecordSql;
import com.zkxltech.ui.util.StringUtils;

public class AnswerInfoServiceImpl implements AnswerInfoService{
	private Result result;
	private RecordSql recordSql = new RecordSql();

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
	public Result getEverybodyAnswerInfo() {
		result = new Result();
		try {
			RedisMapClassTestAnswer.getEverybodyAnswerInfo();
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
}
