package com.zkxlteck.scdll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.cache.RedisMapClassTest;
import com.ejet.core.util.comm.StringUtils;

public class MachineThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(MachineThread.class);
    public static final String GET_ANSWER = "get_answer_list";
    public static final String GET_CARD_INFO = "get_card_info";
    private String method;
    private boolean FLAG = true;
    public MachineThread(String method) {
        this.method = method;
    }
    public boolean isFLAG() {
        return FLAG;
    }

    public void setFLAG(boolean fLAG) {
        FLAG = fLAG;
    }

    @Override
    public void run() {
        while(FLAG){
            if (GET_ANSWER.equals(method)) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        logger.error(" Thread sleep failure ");
                    }
                    String jsonData = ScDll.intance.get_answer_list();
                    if (!StringUtils.isBlank(jsonData)) {
                        RedisMapClassTest.addAnswer(jsonData);
                    }
                    
            }
            if (GET_CARD_INFO.equals(method)) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        logger.error(" Thread sleep failure ");
                    }
                    String jsonData = ScDll.intance.get_wireless_bind_info() ;
                    System.out.println("获取卡信息: "+jsonData);
            }
        }
    }
}
