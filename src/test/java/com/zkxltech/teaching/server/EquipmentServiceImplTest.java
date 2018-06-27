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
        //Result rs = es.bindStart(jo);
        //System.out.println(rs.toString());
    }
    
    /** 开启绑定   */
    @Test
    public void bindStop(){
        EquipmentServiceImpl es = EquipmentServiceImpl.getInstance();
        //Result rs = es.bindStop();
        //System.out.println(rs.toString());
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
    /**清除*/
    @Test
    public void setClearWl(){
        EquipmentServiceImpl es = EquipmentServiceImpl.getInstance();
        JSONObject jo = new JSONObject();
        jo.put("classId", "BJ1001");
        //Result rs = es.clearWl(jo);
        //System.out.println(rs.toString());
    }
    /**开始签到   
     * @throws InterruptedException */
    //FIXME error
    @Test
    public void setSignIn() throws InterruptedException{
        EquipmentServiceImpl es = EquipmentServiceImpl.getInstance();
        Result set_sign_in = es.set_sign_in(1);
        System.out.println(set_sign_in.toString());
        Thread.sleep(10000);
        Result set_sign_in2 = es.set_sign_in(0);
        System.out.println(set_sign_in2.toString());
    }
    @Test
    public void answer_start(){
        
        EquipmentServiceImpl es = EquipmentServiceImpl.getInstance();
        JSONObject jo = new JSONObject();
        jo.put("isQuickResponse", 1);
        jo.put("answerStr", "");
        //Result set_sign_in = es.answerStart(jo);
    }
}
