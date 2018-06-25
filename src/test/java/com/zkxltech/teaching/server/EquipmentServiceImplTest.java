package com.zkxltech.teaching.server;
/**
 * @author: ZhouWei
 * @date:2018年6月25日 上午11:10:36
 */

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.zkxltech.domain.Result;
import com.zkxltech.service.impl.EquipmentServiceImpl;

import net.sf.json.JSONObject;

public class EquipmentServiceImplTest {
    
    /** 开启绑定   */
    @Test
    public void bindStart(){
        EquipmentServiceImpl es = EquipmentServiceImpl.getInstance();
        JSONObject jo = new JSONObject();
        jo.put("model", 1);
        jo.put("uidStr", "");
        Result rs = es.bind_start(jo);
        System.out.println(rs.toString());
    }
    
    /** 开启绑定   */
    @Test
    public void bindStop(){
        EquipmentServiceImpl es = EquipmentServiceImpl.getInstance();
        Result rs = es.bind_stop();
        System.out.println(rs.toString());
    }
    
    /** 设置信道   */
    @Test
    public void setChannel(){
        EquipmentServiceImpl es = EquipmentServiceImpl.getInstance();
        Result rs = es.set_channel(2, 52);
        System.out.println(rs.toString());
    }
    /**设置答题器功率*/
    @Test
    public void setPower(){
        EquipmentServiceImpl es = EquipmentServiceImpl.getInstance();
        Result rs = es.set_tx_power(5);
        System.out.println(rs.toString());
    }
    /**获取设备信息*/
    @Test
    public void getDeviceInfo(){
        EquipmentServiceImpl es = EquipmentServiceImpl.getInstance();
        Result rs = es.get_device_info();
        System.out.println(rs.toString());
    }
    //FIXME error
    /**远距离考勤 */ 
    @Test
    public void setAttendance24g(){
        EquipmentServiceImpl es = EquipmentServiceImpl.getInstance();
        Result rs = es.set_attendance_24g(0, 0);
        System.out.println(rs.toString());
    }
    @Test
    public void setClearWl(){
        EquipmentServiceImpl es = EquipmentServiceImpl.getInstance();
        Result rs = es.clear_wl();
        System.out.println(rs.toString());
    }
}
