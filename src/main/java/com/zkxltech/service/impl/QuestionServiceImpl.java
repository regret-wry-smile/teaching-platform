package com.zkxltech.service.impl;

import java.io.File;
import java.io.FileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;
import com.ejet.core.util.io.ImportExcelUtils;
import com.zkxltech.domain.QuestionInfo;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.TestPaper;
import com.zkxltech.service.QuestionService;
import com.zkxltech.sql.QuestionInfoSql;
import com.zkxltech.ui.util.StringUtils;

public class QuestionServiceImpl implements QuestionService{
	private static final Logger log = LoggerFactory.getLogger(QuestionServiceImpl.class);
	private Result result;
	private QuestionInfoSql questionInfoSql = new QuestionInfoSql();
	@Override
	public Result importQuestion(Object object) {
		result = new Result();
		try {
			String fileName = String.valueOf(object);
			result = questionInfoSql.importQuestion(ImportExcelUtils.getBankListByExcel2(new FileInputStream(new File(fileName)), fileName));
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("导入成功!");
			}else {
				Thread.sleep(1000);
				result.setMessage("导入学生失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("导入学生失败！");
			result.setDetail(IOUtils.getError(e));
			log.error(IOUtils.getError(e));
			return result;
		}
	}
	
	@Override
	public Result insertQuestion(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result selectQuestion(Object object) {
		result = new Result();
		try {
			QuestionInfo questionInfo =  (QuestionInfo) StringUtils.parseJSON(object, QuestionInfo.class);
			result = questionInfoSql.selectStudentInfo(questionInfo);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("查询题目成功!");
			}else {
				result.setMessage("查询题目失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("查询题目失败！");
			result.setDetail(IOUtils.getError(e));
			log.error(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result deleteQuestion(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result updateQuestion(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

}
