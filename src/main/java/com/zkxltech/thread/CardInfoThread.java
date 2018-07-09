package com.zkxltech.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.cache.RedisMapBind;
import com.ejet.core.util.comm.StringUtils;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.scdll.ScDll;

public class CardInfoThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(CardInfoThread.class);
    private boolean FLAG = true;
    public boolean isFLAG() {
        return FLAG;
    }
    public void setFLAG(boolean fLAG) {
        FLAG = fLAG;
    }
    @Override
    public void run() {
        try {
            while(FLAG){
                Thread.sleep(50);
                String jsonData = ScDll.intance.get_wireless_bind_info() ;
                if (!StringUtils.isBlank(jsonData)) {
                    logger.info("获取到答题数据:===>>"+jsonData);
                    RedisMapBind.addBindMap(jsonData);
                }
            }
        } catch (InterruptedException e) {
            logger.error(IOUtils.getError(e));
        } catch (Throwable e){
            logger.error("线程获取硬件数据异常",IOUtils.getError(e));
        }
    }
}
