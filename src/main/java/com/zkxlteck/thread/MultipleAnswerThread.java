package com.zkxlteck.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.cache.RedisMapClassTestAnswer;
import com.ejet.cache.RedisMapMultipleAnswer;
import com.ejet.core.util.comm.StringUtils;
import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;
import com.zkxlteck.scdll.ScDll;

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
        while(FLAG){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                logger.error(IOUtils.getError(e));
            }
            String jsonData = ScDll.intance.get_answer_list();
            if (!StringUtils.isBlank(jsonData)) {
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
    }
}
