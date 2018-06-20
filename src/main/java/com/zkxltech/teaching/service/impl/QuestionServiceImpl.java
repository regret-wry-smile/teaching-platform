package com.zkxltech.teaching.service.impl;

import java.io.File;
import java.io.FileInputStream;

import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;
import com.ejet.core.util.io.ImportExcelUtils;
import com.zkxltech.domain.Result;
import com.zkxltech.teaching.service.QuestionService;
import com.zkxltech.teaching.sql.QuestionInfoSql;

public class QuestionServiceImpl implements QuestionService{
	private Result result;
	private QuestionInfoSql studentInfoSql = new QuestionInfoSql();
	@Override
	public Result importQuestion(Object object) {
		result = new Result();
		try {
			String fileName = String.valueOf(object);
			result = studentInfoSql.importQuestion(ImportExcelUtils.getBankListByExcel2(new FileInputStream(new File(fileName)), fileName));
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
		// TODO Auto-generated method stub
		return null;
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
