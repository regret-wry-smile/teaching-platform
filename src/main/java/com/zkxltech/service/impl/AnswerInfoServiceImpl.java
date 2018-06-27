package com.zkxltech.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ejet.cache.RedisMapMultipleAnswer;
import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.domain.AnswerInfo;
import com.zkxltech.domain.RequestVo;
import com.zkxltech.domain.Result;
import com.zkxltech.service.AnswerInfoService;
import com.zkxltech.sql.AnswerInfoSql;
import com.zkxltech.ui.util.StringUtils;

public class AnswerInfoServiceImpl implements AnswerInfoService{
	private Result result;
	private AnswerInfoSql answerInfoSql = new AnswerInfoSql();
	@Override
	public Result insertAnswerInfos(List<AnswerInfo> answerInfos) {
		result = new Result();
		try {
			result = answerInfoSql.insertAnswerInfos(answerInfos);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("批量插入答题信息成功!");
			}else {
				result.setMessage("批量插入答题信息失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("批量插入答题信息失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}
	@Override
	public Result deleteAnswerInfo(AnswerInfo answerInfo) {
		result = new Result();
		try {
			result = answerInfoSql.deleteAnswerInfo(answerInfo);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("删除答题信息成功!");
			}else {
				result.setMessage("删除答题信息失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("删除答题信息失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}
	@Override
	public Result selectAnswerInfo(AnswerInfo answerInfo) {
		result = new Result();
		try {
			result = answerInfoSql.selectAnswerInfoInfo(answerInfo);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("查询答题信息成功!");
			}else {
				result.setMessage("查询答题信息失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("查询答题信息失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}
	@Override
	public Result startMultipleAnswer(Object object) {
		result = new Result();
		try {
			RequestVo requestVo = StringUtils.parseJSON(object, RequestVo.class);
			List<RequestVo> list = new ArrayList<RequestVo>();
			list.add(requestVo);
			result = EquipmentServiceImpl.getInstance().answerStart2(list);
			if (Constant.ERROR.equals(result.getRet())) {
				result.setRet(Constant.ERROR);
				result.setMessage("开始答题指令发送失败！");
				return result;
			}
			RedisMapMultipleAnswer.clearMap();
			RedisMapMultipleAnswer.startAnswer((String)object);
			result.setRet(Constant.SUCCESS);
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("开始答题指令发送失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}
}
