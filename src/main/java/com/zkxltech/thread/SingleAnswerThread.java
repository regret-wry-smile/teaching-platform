package com.zkxltech.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.cache.RedisMapSingleAnswer;
import com.ejet.core.util.SerialListener;
import com.ejet.core.util.comm.StringUtils;
import com.ejet.core.util.constant.EquipmentConstant;
import com.ejet.core.util.io.IOUtils;

public class SingleAnswerThread extends BaseThread {
    private static final Logger logger = LoggerFactory.getLogger(SingleAnswerThread.class);
    private boolean FLAG = true;
    public boolean isFLAG() {
        return FLAG;
    }
    public void setFLAG(boolean fLAG) {
        FLAG = fLAG;
    }
    
    @Override
    public void stopThread() {
        FLAG = false;
    }
    
    @Override
    public void run() {
        try {
            while(FLAG){
                    Thread.sleep(100);
                String jsonData = SerialListener.getDataMap(EquipmentConstant.UPDATE_ANSWER_LIST);
                if (!StringUtils.isBlank(jsonData)) {
                    StringBuilder stringBuilder = new StringBuilder(jsonData);
                    if (jsonData.startsWith("{")) {
                        stringBuilder.insert(0, "[").append("]");
                    }
                    jsonData = stringBuilder.toString();
                    logger.info("获取到答题数据:===>>"+jsonData);
                    RedisMapSingleAnswer.addAnswer(jsonData);
                    SerialListener.clearMap();
                }
            }
        } catch (InterruptedException e) {
            logger.error(IOUtils.getError(e));
        } catch (Throwable e){
            logger.error("线程获取硬件数据异常",IOUtils.getError(e));
        }
    }
}
