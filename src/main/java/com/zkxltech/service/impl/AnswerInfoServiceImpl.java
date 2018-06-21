package com.zkxltech.service.impl;

import java.util.List;

import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.domain.AnswerInfo;
import com.zkxltech.domain.Result;
import com.zkxltech.service.AnswerInfoService;
import com.zkxltech.sql.AnswerInfoSql;

public class AnswerInfoServiceImpl implements AnswerInfoService{
	private Result result = new Result();
	private AnswerInfoSql answerInfoSql = new AnswerInfoSql();
	@Override
	public Result insertAnswerInfos(List<AnswerInfo> answerInfos) {
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
}
