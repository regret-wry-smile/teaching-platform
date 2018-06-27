package com.zkxltech.service;

import com.zkxltech.domain.Result;

/**
 * @author: ZhouWei
 * @date:2018年6月21日 下午2:54:45
 */
public interface EquipmentService {
    /** 获取设备信息 */
    public Result get_device_info();
    /** 清除白名单 */
    public Result clearWl(Object param) ;
    /** 开始绑定  */
    public Result bindStart(Object param) ;
    /** 停止绑定 */
    public Result bindStop() ;
    /** 开始答题 */
    public Result answerStart(Object param);
    /**  开始答题 */
    public Result answer_start_with_raise_hand( int is_quick_response, int raise_hand, String answer_str ); 
    /** 设置举手功能 */
    public Result set_raise_hand(int raise_hand) ;
    /** 设置签到功能 */
    public Result set_sign_in(int attendance) ;
    /** 停止作答 */
    public Result answerStop() ;
    
    public Result set_student_id(String student_id_str) ;
    
    public Result get_student_id_info() ;
    
    public Result set_channel(int tx_ch , int rx_ch) ;
    
    public Result set_tx_power(int tx_power) ;
    
    //public Result attendance_24g(int is_open , int pro_index) ;
    /** 读取答题器UID:刷卡 */
    public Result read_card_uid_start() ;
    /** 获取绑定的卡片信息 */
    public Result get_card_uid_Info() ;
    /** 停止读取答题器UID */
    public Result read_card_uid_stop() ;
    /** 获取绑定的卡片信息 */
    public Result get_wireless_bind_info() ;
    /** 开始举手,监听开始 */
    public Result raise_hand_start() ;
    /**  停止举手,监听结束 */
    public Result raise_hand_stop() ;
    /** 获取学生提交的举手信息. */
    public Result get_raise_hand_list() ;
    /** 开始签到 */
    public Result signInStart(Object param) ;
    /**  停止签到 */
    public Result signInStop() ;
    /** 获取学生提交的签到信息 */
    public Result get_sign_in_list() ;
    /** 设置2.4G考勤功能(绑定前设置有效) */
    public Result set_attendance_24g(int is_open , int pro_index) ;
    /** 无线设置学号  */
    public Result set_wireless_student_id(String uid_str , String student_id_str) ; 
    /** 无线获取设置学号卡片信息 */
    public Result get_wireless_student_id_info() ;
    /**快速抢答*/
    public Result quickAnswer(Object param);
}
