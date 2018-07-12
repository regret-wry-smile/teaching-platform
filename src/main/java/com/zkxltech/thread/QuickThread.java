package com.zkxltech.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.cache.RedisMapQuick;
import com.ejet.core.util.comm.StringUtils;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.scdll.ScDll;

public class QuickThread extends BaseThread {
    private static final Logger logger = LoggerFactory.getLogger(QuickThread.class);
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
                Thread.sleep(50);
                String jsonData = ScDll.intance.get_answer_list();
                if (!StringUtils.isBlank(jsonData)) {
                    StringBuilder stringBuilder = new StringBuilder(jsonData);
                    if (jsonData.startsWith("{")) {
                        stringBuilder.insert(0, "[").append("]");
                    }
                    jsonData = stringBuilder.toString();
                    logger.info("获取到答题数据:===>>"+jsonData);
                    RedisMapQuick.addQuickAnswer(jsonData);
                }
            }
        } catch (InterruptedException e) {
            logger.error(IOUtils.getError(e));
        } catch (Throwable e){
            logger.error("线程获取硬件数据异常",IOUtils.getError(e));
        }
    }
}
