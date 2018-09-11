package com.zkxltech.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.ejet.core.util.EquipmentUtils;
import com.ejet.core.util.SerialListener;
import com.ejet.core.util.SerialPortManager;
import com.ejet.core.util.comm.ListUtils;
import com.ejet.core.util.comm.StringUtils;
import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.constant.EquipmentConstant;
import com.ejet.core.util.constant.Global;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.domain.Answer;
import com.zkxltech.domain.Result;
import com.zkxltech.sql.StudentInfoSql;
import com.zkxltech.thread.BaseThread;
import com.zkxltech.thread.MsgThread;
import com.zkxltech.thread.MultipleAnswerThread;
import com.zkxltech.thread.ThreadManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author: luozheng
 * @date:2018年7月17日 下午2:54:45
 */
public interface EquipmentService2 {

	public Result get_device_info();
	
	public Result bind_stop();
	
	public Result clear_wl() ;
    
    public Result set_channel(int tx_ch , int rx_ch) ;
    
    public Result set_tx_power(int tx_power) ;
    /**设备和数据库绑定的状态同步*/
    public Result equipmentDatabaseSynchronization();
    
    /** 开始答题 */
    public Result answerStart2(String answerType,Object param);
    Result answer_stop();
}
