package com.zkxltech.service.impl;

import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;
import com.ejet.core.util.io.ImportExcelUtils;
import com.zkxltech.domain.QuestionInfo;
import com.zkxltech.domain.Result;
import com.zkxltech.service.QuestionService;
import com.zkxltech.sql.QuestionInfoSql;
import com.zkxltech.ui.util.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

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
				result.setMessage("Import success!");
			}else {
				result.setMessage("Failed to import students！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("Failed to import students！");
			result.setDetail(IOUtils.getError(e));
			log.error(IOUtils.getError(e));
			return result;
		}
	}
	
	@Override
	public Result insertQuestion(Object object) {
		result = new Result();
		try {
			QuestionInfo questionInfo =  (QuestionInfo) StringUtils.parseJSON(object, QuestionInfo.class);
			result = questionInfoSql.insertQuestionInfo(questionInfo);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("Add topic successfully!");
			}else {
				result.setMessage("Failed to add topic！");
			}
			result.setRet(Constant.SUCCESS);
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("Failed to add topic！");
			result.setDetail(IOUtils.getError(e));
			log.error(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result insertQuestions(Object object) {
		result = new Result();
		try {
			List<QuestionInfo> questionInfos = (List<QuestionInfo>) JSONArray.toCollection(JSONArray.fromObject(object),QuestionInfo.class);
			result = questionInfoSql.updateOrSaveStudents(questionInfos);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("Add topic successfully!");
			}else {
				result.setMessage("Failed to add topic！");
			}
			result.setRet(Constant.SUCCESS);
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("Failed to add topic！");
			result.setDetail(IOUtils.getError(e));
			log.error(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result selectQuestion(Object object) {
		result = new Result();
		try {
			QuestionInfo questionInfo =  (QuestionInfo) StringUtils.parseJSON(object, QuestionInfo.class);
			result = questionInfoSql.selectQuestionInfo(questionInfo);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("Select topic successfully!");
			}else {
				result.setMessage("Failed to select topic！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("Failed to select topic！");
			result.setDetail(IOUtils.getError(e));
			log.error(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result deleteQuestionByIds(Object object) {
		try {
			JSONArray jsonArray = JSONArray.fromObject(object);
			List<Integer> ids = new ArrayList<Integer>();
			for (int i = 0; i < jsonArray.size(); i++) {
				ids.add(jsonArray.getInt(i));
			}
			result = questionInfoSql.deleteQuestionInfoById(ids);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("Delete topic successfully!");
			}else {
				result.setMessage("Failed to delete topic！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("Failed to delete topic！");
			result.setDetail(IOUtils.getError(e));
			log.error(IOUtils.getError(e));
			return result;
		}
	}
	
//	@Override
//	public Result deleteQuestionRedis(Object object) {
//		try {
//			QuestionInfo questionInfo =  (QuestionInfo) StringUtils.parseJSON(object, QuestionInfo.class);
//			RedisMapPaper.deleteQuestion(questionInfo);
//			result.setRet(Constant.SUCCESS);
//			result.setMessage("删除题目成功!");
//			return result;
//		} catch (Exception e) {
//			result.setRet(Constant.ERROR);
//			result.setMessage("删除题目失败！");
//			result.setDetail(IOUtils.getError(e));
//			return result;
//		}
//	}

	@Override
	public Result updateQuestion(Object object) {
		try {
			QuestionInfo questionInfo =  (QuestionInfo)  StringUtils.parseJSON(JSONObject.fromObject(object), QuestionInfo.class);
			result = questionInfoSql.updateStudentById(questionInfo);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("Modified topic successfully!");
			}else {
				result.setMessage("Topic modified failed！");
			}
			result.setRet(Constant.SUCCESS);
			result.setMessage("Modified topic successfully!");
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("Topic modified failed！");
			result.setDetail(IOUtils.getError(e));
			log.error(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result updateQuestionScore(Object object) {
		try {
			JSONArray jsonArray = JSONArray.fromObject(object);
			List<Integer> ids = new ArrayList<Integer>();
//			Integer score = jsonArray.getInt(1);
			for (int i = 0; i < jsonArray.size(); i++) {
				ids.add(jsonArray.getInt(i));
			}
			result = questionInfoSql.updateQuestionInfoById(ids);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("Modified score successfully!");
			}else {
				result.setMessage("Failure to modify score！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("Failure to modify score");
			result.setDetail(IOUtils.getError(e));
			log.error(IOUtils.getError(e));
			return result;
		}
	}

//	@Override
//	public Result selectQuestionedis(Object object) {
//		result = new Result();
//		try {
//			QuestionInfo questionInfo =  (QuestionInfo)  StringUtils.parseJSON(JSONObject.fromObject(object), QuestionInfo.class);
//			result.setItem(RedisMapPaper.getQuestions(questionInfo));
//			result.setMessage("查询题目成功!");
//			result.setRet(Constant.SUCCESS);
//			return result;
//		} catch (Exception e) {
//			result.setRet(Constant.ERROR);
//			result.setMessage("查询题目失败！");
//			result.setDetail(IOUtils.getError(e));
//			log.error(IOUtils.getError(e));
//			return result;
//		}
//	}

}
