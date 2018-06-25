package com.zkxltech.service.impl;

import com.ejet.core.util.comm.StringUtils;
import com.ejet.core.util.constant.Constant;
import com.zkxltech.domain.Result;
import com.zkxltech.service.EquipmentService;
import com.zkxlteck.scdll.MachineThread;
import com.zkxlteck.scdll.ScDll;

/**
 * @author: ZhouWei
 * @date:2018年6月21日 下午2:55:17
 */
public class EquipmentServiceImpl implements EquipmentService{
    //private ExecutorService threadPool = Executors.newSingleThreadExecutor(); //单线程池
    public static final int SUCCESS = 0 ;//
    public static final int ERROR = -1 ; //
    private Thread t ;
    private static final EquipmentServiceImpl SINGLE = new EquipmentServiceImpl();  
    
    private EquipmentServiceImpl() {
    }
    public static EquipmentServiceImpl getInstance() {  
        return SINGLE;  
    }
    @Override
    public Result get_device_info() {
        Result r = new Result();
        String get_device_info = ScDll.intance.get_device_info();
        if(!StringUtils.isBlank(get_device_info)) {
            r.setRet(Constant.SUCCESS);
            r.setItem(get_device_info);
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("查询失败");
        return r;
    }
    @Override
    public Result clear_wl() {
        Result r = new Result();
        int clear_wl = ScDll.intance.clear_wl();
        if (clear_wl == SUCCESS) {
            r.setRet(Constant.SUCCESS);
            r.setMessage("清除成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("清除失败");
        return r;
    }
    @Override
    public Result bind_start(Integer model,String data) {
        Result r = new Result();
        int bind_start = ScDll.intance.wireless_bind_start(model, data) ;
        if (bind_start != -1) {
            //FIXME
//            Thread t = new MachineThread(true, MachineThread.GET_CARD_INFO);
//            threadPool.submit(t);
            r.setRet(Constant.SUCCESS);
            r.setMessage("操作成功");
        }else{
            r.setRet(Constant.ERROR);
            r.setMessage("操作失败");
        }
        return r;
    }
    @Override
    public Result bind_stop() {
        Result r = new Result();
        int bind_stop = ScDll.intance.wireless_bind_stop();
        if (bind_stop == SUCCESS) {
            r.setRet(Constant.SUCCESS);
            r.setMessage("停止成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("停止失败");
        return r;
    }
    @Override
    public Result answer_start(int is_quick_response, String answer_str) {
        Result r = new Result();
        int answer_start = ScDll.intance.answer_start(is_quick_response, answer_str);
        if (answer_start == SUCCESS) {
            t = new MachineThread(MachineThread.GET_ANSWER);
            t.start();
            r.setRet(Constant.SUCCESS);
            r.setMessage("发送成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("发送失败");
        return r;
    }
    @Override
    public Result answer_start_with_raise_hand(int is_quick_response, int raise_hand, String answer_str) {
        Result r = new Result();
        int answer_start_with_raise_hand = ScDll.intance.answer_start_with_raise_hand(is_quick_response, raise_hand, answer_str);
        if (answer_start_with_raise_hand == SUCCESS) {
            t = new MachineThread(MachineThread.GET_ANSWER);
            t.start();
            r.setRet(Constant.SUCCESS);
            r.setMessage("发送成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("发送失败");
        return r;
    }
    @Override
    public Result set_raise_hand(int raise_hand) {
        Result r = new Result();
        int set_raise_hand = ScDll.intance.set_raise_hand(raise_hand);
        if (set_raise_hand == SUCCESS) {
            r.setRet(Constant.SUCCESS);
            r.setMessage("设置成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("设置失败");
        return r;
    }
    @Override
    public Result set_sign_in(int attendance) {
        Result r = new Result();
        int set_sign_in = ScDll.intance.set_sign_in(attendance);
        if (set_sign_in == SUCCESS) {
            r.setRet(Constant.SUCCESS);
            r.setMessage("设置成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("设置失败");
        return r;
    }

//    public Result get_answer_list() {
//        Result r = new Result();
//        MachineThread t = new MachineThread(true, MachineThread.GET_ANSWER);
//        t.start();
//        map.put("thread", t);
//        return r;
//    }
    @Override
    public Result answer_stop() {
        Result r = new Result();
        int answer_stop = ScDll.intance.answer_stop();
        if (answer_stop == SUCCESS) {
            //FIXME
            if (t != null) {
                MachineThread m = (MachineThread)t;
                m.setFLAG(false);
            }
            r.setRet(Constant.SUCCESS);
            r.setMessage("停止成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("停止失败");
        return r;
    }
    @Override
    public Result set_student_id(String student_id_str) {
        Result r = new Result();
        int set_student_id = ScDll.intance.set_student_id(student_id_str);
        if (set_student_id == SUCCESS) {
            r.setRet(Constant.SUCCESS);
            r.setMessage("操作成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("操作失败");
        return r;
    }
    @Override
    public Result get_student_id_info() {
        Result r = new Result();
        String get_student_id_info = ScDll.intance.get_student_id_info();
        if (!StringUtils.isBlank(get_student_id_info)) {
            r.setItem(get_student_id_info);
            r.setRet(Constant.SUCCESS);
            r.setMessage("操作成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("操作失败");
        return r;
    }
    @Override
    public Result set_channel(int tx_ch, int rx_ch) {
        Result r = new Result();
        int set_channel = ScDll.intance.set_channel(tx_ch,rx_ch);
        if (set_channel == SUCCESS) {
            r.setRet(Constant.SUCCESS);
            r.setMessage("操作成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("操作失败");
        return r;
    }
    @Override
    public Result set_tx_power(int tx_power) {
        Result r = new Result();
        int set_tx_power = ScDll.intance.set_tx_power(tx_power);
        if (set_tx_power == SUCCESS) {
            r.setRet(Constant.SUCCESS);
            r.setMessage("操作成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("操作失败");
        return r;
    }

//    public Result attendance_24g(int is_open, int pro_index) {
//        Result r = new Result();
//        int attendance_24g = ScDll.intance.attendance_24g(is_open,pro_index);
//        if (attendance_24g == SUCCESS) {
//            r.setRet(Constant.SUCCESS);
//            r.setMessage("操作成功");
//            return r;
//        }
//        r.setRet(Constant.ERROR);
//        r.setMessage("操作失败");
//        return r;
//    }
    @Override
    public Result read_card_uid_start() {
        Result r = new Result();
        int read_card_uid_start = ScDll.intance.read_card_uid_start();
        if (read_card_uid_start == SUCCESS) {
            r.setItem(read_card_uid_start);
            r.setRet(Constant.SUCCESS);
            r.setMessage("操作成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("操作失败");
        return r;
    }
    @Override
    public Result get_card_uid_Info() {
        Result r = new Result();
        String get_card_uid_Info = ScDll.intance.get_card_uid_Info();
        //FIXME
        if (!StringUtils.isBlank(get_card_uid_Info)) {
            r.setItem(get_card_uid_Info);
            r.setRet(Constant.SUCCESS);
            r.setMessage("读取成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("读取失败");
        return r;
    }
    @Override
    public Result read_card_uid_stop() {
        Result r = new Result();
        int read_card_uid_stop = ScDll.intance.read_card_uid_stop();
        if (read_card_uid_stop == SUCCESS) {
            r.setRet(Constant.SUCCESS);
            r.setMessage("停止成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("停止失败");
        return r;
    }
    @Override
    public Result get_wireless_bind_info() {
        Result r = new Result();
        String get_wireless_bind_info = ScDll.intance.get_wireless_bind_info();
        if (!StringUtils.isBlank(get_wireless_bind_info)) {
            r.setItem(get_wireless_bind_info);
            r.setRet(Constant.SUCCESS);
            r.setMessage("获取成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("获取失败");
        return r;
    }

    @Override  
    public Result raise_hand_start() {
        Result r = new Result();
        int raise_hand_start = ScDll.intance.raise_hand_start();
        //FIXME
        if (raise_hand_start == SUCCESS) {
            r.setRet(Constant.SUCCESS);
            r.setMessage("操作成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("操作失败");
        return r;
    }

    @Override    
    public Result raise_hand_stop() {
        Result r = new Result();
        int raise_hand_stop = ScDll.intance.raise_hand_stop();
        if (raise_hand_stop == SUCCESS) {
            r.setRet(Constant.SUCCESS);
            r.setMessage("停止成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("停止失败");
        return r;
    }

    @Override
    public Result get_raise_hand_list() {
        Result r = new Result();
        String get_raise_hand_list = ScDll.intance.get_raise_hand_list();
        if (!StringUtils.isBlank(get_raise_hand_list)) {
            r.setItem(get_raise_hand_list);
            r.setRet(Constant.SUCCESS);
            r.setMessage("获取成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("获取失败");
        return r;
    }

    @Override
    public Result sign_in_start() {
        Result r = new Result();
        int sign_in_start = ScDll.intance.sign_in_start();
        if (sign_in_start == SUCCESS) {
            r.setRet(Constant.SUCCESS);
            r.setMessage("操作成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("操作失败");
        return r;
    }

    @Override
    public Result sign_in_stop() {
        Result r = new Result();
        int sign_in_stop = ScDll.intance.sign_in_stop();
        if (sign_in_stop == SUCCESS) {
            r.setRet(Constant.SUCCESS);
            r.setMessage("停止成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("停止失败");
        return r;
    }

    @Override
    public Result get_sign_in_list() {
        Result r = new Result();
        String get_sign_in_list = ScDll.intance.get_sign_in_list();
        if (!StringUtils.isBlank(get_sign_in_list)) {
            r.setItem(get_sign_in_list);
            r.setRet(Constant.SUCCESS);
            r.setMessage("获取成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("获取失败");
        return r;
    }

    @Override
    public Result set_attendance_24g(int is_open, int pro_index) {
      Result r = new Result();
      int set_attendance_24g = ScDll.intance.set_attendance_24g(is_open,pro_index);
      if (set_attendance_24g == SUCCESS) {
          r.setRet(Constant.SUCCESS);
          r.setMessage("设置成功");
          return r;
      }
      r.setRet(Constant.ERROR);
      r.setMessage("设置失败");
      return r;
    }

    @Override
    public Result set_wireless_student_id(String uid_str, String student_id_str) {
        Result r = new Result();
        int set_wireless_student_id = ScDll.intance.set_wireless_student_id(uid_str,student_id_str);
        if (set_wireless_student_id == SUCCESS) {
            r.setRet(Constant.SUCCESS);
            r.setMessage("设置成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("设置失败");
        return r;
    }

    @Override
    public Result get_wireless_student_id_info() {
        Result r = new Result();
        String get_wireless_student_id_info = ScDll.intance.get_wireless_student_id_info();
        if (!StringUtils.isBlank(get_wireless_student_id_info)) {
            r.setItem(get_wireless_student_id_info);
            r.setRet(Constant.SUCCESS);
            r.setMessage("获取成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("获取失败");
        return r;
    }
}