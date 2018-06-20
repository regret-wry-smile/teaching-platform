package com.zkxltech.teaching.service.impl;

import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.domain.ClassInfo;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.TestPaper;
import com.zkxltech.teaching.service.ClassInfoService;
import com.zkxltech.teaching.service.TestPaperService;
import com.zkxltech.teaching.sql.ClassInfoSql;
import com.zkxltech.teaching.sql.TestPaperSql;
import com.zkxltech.ui.util.StringUtils;

import net.sf.json.JSONObject;

public class TestPaperServiceImpl implements TestPaperService{
	private Result result;
	private TestPaperSql testPaperSql = new TestPaperSql();;
	
	@Override
	public Result insertTestPaper(Object object) {
		result = new Result();
		try {
			TestPaper testPaper =  (TestPaper) StringUtils.parseJSON(object, TestPaper.class);
			//插入试卷
			result = testPaperSql.insertTestPaper(testPaper);
			if (Constant.ERROR.equals(result.getRet())) {
				result.setMessage("添加试卷信息失败！");
				return result;
			}
			result.setMessage("添加试卷成功！");
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("添加试卷失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result selectTestPaper(Object object) {
		result = new Result();
		try {
			TestPaper testPaper =  (TestPaper) StringUtils.parseJSON(object, TestPaper.class);
			result = testPaperSql.selectTestPaper(testPaper);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("查询班级成功!");
			}else {
				result.setMessage("查询班级失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("查询班级失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result deleteTestPaper(Object object) {
		result = new Result();
		try {
			TestPaper testPaper =  (TestPaper) StringUtils.parseJSON(object, TestPaper.class);
			result = testPaperSql.deleteTestPaper(testPaper);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("删除班级成功!");
			}else {
				result.setMessage("删除班级失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("删除班级失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result updateTestPaper(Object object) {
		result = new Result();
		try {
			TestPaper testPaper =  (TestPaper) StringUtils.parseJSON(object, TestPaper.class);
			result = testPaperSql.updateTestPaper(testPaper);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("修改班级成功!");
			}else {
				result.setMessage("修改班级失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("修改班级失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

}
