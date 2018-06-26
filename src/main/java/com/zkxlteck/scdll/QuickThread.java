package com.zkxlteck.scdll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.cache.RedisMapQuick;
import com.ejet.core.util.comm.StringUtils;

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
                logger.error(" Thread sleep failure ");
            }
            String jsonData = ScDll.intance.get_answer_list();
            if (!StringUtils.isBlank(jsonData)) {
                RedisMapQuick.addQuickAnswer(jsonData);
            }
        }
    }
}
