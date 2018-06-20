package com.ejet.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeachingCache {
	private static final Logger logger = LoggerFactory.getLogger(TeachingCache.class);
	
	/**
	 * 答案对象（各个答题答案进行区分）
	 */
	private  static Map<String, Object> answerMap = Collections.synchronizedMap(new HashMap<String, Object>());
	
}
