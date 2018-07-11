package com.zkxltech.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.cache.RedisMapClassTestAnswer;
import com.ejet.cache.RedisMapMultipleAnswer;
import com.ejet.core.util.comm.StringUtils;
import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.scdll.ScDll;

public class MultipleAnswerThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(MultipleAnswerThread.class);
    private boolean FLAG = true;
    private String answerType;
    public boolean isFLAG() {
        return FLAG;
    }
    public void setFLAG(boolean fLAG) {
        FLAG = fLAG;
    }
    
    
  
	public MultipleAnswerThread(String answerType) {
		super();
		this.answerType = answerType;
	}
	@Override
    public void run() {
	    try {
            while(FLAG){
                Thread.sleep(50);
                String jsonData = ScDll.intance.get_answer_list();
                if (!StringUtils.isBlank(jsonData)) {
                    StringBuilder stringBuilder = new StringBuilder(jsonData);
                    if (jsonData.startsWith("{")) {
                        stringBuilder.insert(0, "[").append("]");
                    }
                    jsonData = stringBuilder.toString();
                    logger.info("获取到答题数据:===>>"+jsonData);
                    switch (answerType) {
    				case Constant.ANSWER_MULTIPLE_TYPE:
    					RedisMapMultipleAnswer.addEveryAnswerInfo(jsonData);
    					break;
    				case Constant.ANSWER_CLASS_TEST_OBJECTIVE:
    					RedisMapClassTestAnswer.addRedisMapClassTestAnswer1(jsonData);
    					break;
    				case Constant.ANSWER_CLASS_TEST_SUBJECTIVE :
    					RedisMapClassTestAnswer.addRedisMapClassTestAnswer2(jsonData);
    					break;
    				default:
    					break;
    				}
                	
                }
            }
	    } catch (InterruptedException e) {
	        logger.error(IOUtils.getError(e));
	    } catch (Throwable e){
	        logger.error("线程获取硬件数据异常",IOUtils.getError(e));
	    }
    }
}
