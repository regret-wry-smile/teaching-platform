package com.zkxltech.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.cache.RedisMapBind;
import com.ejet.core.util.comm.StringUtils;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.device.DeviceComm;

public class CardInfoThread extends BaseThread {
    private static final Logger logger = LoggerFactory.getLogger(CardInfoThread.class);
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
                String jsonData = DeviceComm.getWirelessBindInfo();
                if (!StringUtils.isBlank(jsonData) && !"[]".equals(jsonData)) {
                	logger.info("获取到绑定数据:===>>"+jsonData);
                	RedisMapBind.addBindMap(jsonData);
				}
                /*List<String> data = SerialListener.getDataMap();
                if (!StringUtils.isBlankList(data)) {
                	String jsonData = JSONArray.fromObject(data).toString();
                    StringBuilder stringBuilder = new StringBuilder(jsonData);
                    if (jsonData.startsWith("{")) {
                        stringBuilder.insert(0, "[").append("]");
                    }
                    jsonData = stringBuilder.toString();
                    logger.info("获取到答题数据:===>>"+jsonData);
                    RedisMapBind.addBindMap(jsonData);
                    SerialListener.removeList(data);
                }*/
            }
        } catch (InterruptedException e) {
            logger.error(IOUtils.getError(e));
        } catch (Throwable e){
            logger.error("线程获取硬件数据异常",IOUtils.getError(e));
        }
    }
}
