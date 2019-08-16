package com.zkxltech.service.impl;

import java.io.File;
import java.io.FileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.constant.Global;
import com.ejet.core.util.io.IOUtils;
import com.ejet.core.util.io.ImportExcelUtils;
import com.zkxltech.domain.QuestionInfo;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.TestPaper;
import com.zkxltech.service.TestPaperService;
import com.zkxltech.sql.QuestionInfoSql;
import com.zkxltech.sql.TestPaperSql;
import com.zkxltech.ui.util.StringUtils;

import net.sf.json.JSONObject;

public class TestPaperServiceImpl implements TestPaperService{
	private static final Logger log = LoggerFactory.getLogger(TestPaperServiceImpl.class);
	private Result result;
	private TestPaperSql testPaperSql = new TestPaperSql();
	private QuestionInfoSql questionInfoSql = new QuestionInfoSql();
	@Override
	public Result insertTestPaper(Object testInfo) {
		result = new Result();
		try {
			TestPaper testPaper =  (TestPaper) StringUtils.parseJSON(testInfo, TestPaper.class);
			//插入试卷
			result = testPaperSql.insertTestPaper(testPaper);
			if (Constant.ERROR.equals(result.getRet())) {
				result.setMessage("Failed to insert problem information！");
				return result;
			}
			//将该试卷的状态改为启用状态
			QuestionInfo questionInfo = new QuestionInfo();
			questionInfo.setTestId(testPaper.getTestId());
			questionInfo.setStatus("1");
			result = questionInfoSql.updateStudent(questionInfo);
			if (Constant.ERROR.equals(result.getRet())) {
				result.setMessage("Failed to insert problem information！");
				return result;
			}
			result.setMessage("add paper successful！");
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("add paper failed！");
			result.setDetail(IOUtils.getError(e));
			log.error(IOUtils.getError(e));
			return result;
		}
	}
	@Override
	public Result importTestPaper(Object object) {
		result = new Result();
		try {
			String fileName = String.valueOf(object);
			result = questionInfoSql.importQuestion(ImportExcelUtils.getBankListByExcel2(new FileInputStream(new File(fileName)), fileName));
			if (Constant.ERROR.equals(result.getRet())) {
				return result;
			}
			result.setMessage("add paper successful！");
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("add paper failed！");
			result.setDetail(IOUtils.getError(e));
			log.error(IOUtils.getError(e));
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
				result.setMessage("select paper successful!");
			}else {
				result.setMessage("select paper failed！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("select paper failed！");
			result.setDetail(IOUtils.getError(e));
			log.error(IOUtils.getError(e));
			return result;
		}
	}
	
	@Override
    public Result selectTestPaperByClassHourId(Object object) {
        result = new Result();
        try {
            TestPaper testPaper =  (TestPaper) StringUtils.parseJSON(object, TestPaper.class);
            result = testPaperSql.selectTestPaperByClassHourId(testPaper);
            if (Constant.SUCCESS.equals(result.getRet())) {
                result.setMessage("select paper successful!");
            }else {
                result.setMessage("select paper failed！");
            }
            return result;
        } catch (Exception e) {
            result.setRet(Constant.ERROR);
            result.setMessage("select paper failed！");
            result.setDetail(IOUtils.getError(e));
            log.error(IOUtils.getError(e));
            return result;
        }
    }
	public Result selectNowTestPaper(Object object) {
		result = new Result();
		try {
			TestPaper testPaper =  (TestPaper) StringUtils.parseJSON(object, TestPaper.class);
			testPaper.setSubject(Global.getClassHour().getSubjectName());
			result = testPaperSql.selectTestPaper(testPaper);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("select paper successful!");
			}else {
				result.setMessage("select paper failed！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("select paper failed！");
			result.setDetail(IOUtils.getError(e));
			log.error(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result deleteTestPaper(Object object) {
		result = new Result();
		try {
			TestPaper testPaper =  (TestPaper) StringUtils.parseJSON(object, TestPaper.class);
			/*根据试卷id删除题目*/
			QuestionInfo questionInfo = new QuestionInfo();
			questionInfo.setTestId(testPaper.getTestId());
			result = questionInfoSql.deleteQuestionInfo(questionInfo);
			if(Constant.ERROR.equals(result.getRet())){
				result.setMessage("Failed to delete question!");
				return result;
			}
			result = testPaperSql.deleteTestPaper(testPaper);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("Delete paper successful!");
			}else {
				result.setMessage("Failed to delete paper！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("Failed to delete class！");
			result.setDetail(IOUtils.getError(e));
			log.error(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result updateTestPaper(Object object) {
		result = new Result();
		try {
			TestPaper testPaper =  (TestPaper) StringUtils.parseJSON(object, TestPaper.class);
			
			QuestionInfo questionInfo = new QuestionInfo();
			
			questionInfo.setTestId(testPaper.getTestId());
			questionInfo.setStatus("1");
			Result result = questionInfoSql.updateStudent(questionInfo);
			if (Constant.ERROR.equals(result.getRet())) {
				result.setMessage("Modified the information successfully!");
				return result;
			}
			result = testPaperSql.updateTestPaper(testPaper);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("Modify success!");
			}else {
				result.setMessage("Modify failed！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("Modify failed！");
			result.setDetail(IOUtils.getError(e));
			log.error(IOUtils.getError(e));
			return result;
		}
	}

}
