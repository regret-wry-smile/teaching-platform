package com.zkxltech.service;

import java.util.List;

import com.zkxltech.domain.AnswerInfo;
import com.zkxltech.domain.Result;

/**
 * 【作答记录】
 *
 */
public interface RecordService {
	/**
	 * 导出文件
	 * @param object 生成的文件名
	 */
	Result exportRecord(Object object);
	/**
	 * 查询主观题作答记录（随堂检测专用）
	 * @param object
	 * @return
	 */
	Result selectSubjectiveRecord(Object object);
	
	/**
	 * 查询客观题作答记录（随堂检测专用）
	 * @param object
	 * @return
	 */
	Result selectObjectiveRecord(Object object);
}
