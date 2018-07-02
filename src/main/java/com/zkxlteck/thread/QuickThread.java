package com.zkxlteck.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.cache.RedisMapQuick;
import com.ejet.core.util.comm.StringUtils;
import com.ejet.core.util.io.IOUtils;
import com.zkxlteck.scdll.ScDll;

public class QuickThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(QuickThread.class);
    private boolean FLAG = true;
    public boolean isFLAG() {
        return FLAG;
    }
    public void setFLAG(boolean fLAG) {
        FLAG = fLAG;
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
                RedisMapQuick.addQuickAnswer(jsonData);
            }
        }
    }
}
