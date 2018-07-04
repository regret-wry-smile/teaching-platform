package com.zkxltech.scdll;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface ScDll extends Library{
    
		ScDll intance = (ScDll)Native.loadLibrary("SC", ScDll.class) ;
		/**
		 * 获取设备信息
		 * @return
		 */
		public String get_device_info();
		
		/**
		 * 清除白名单
		 * @return
		 */
		public int clear_wl() ;
		/**
		 * 开始绑定
		 * @return
		 */
		public int bind_start() ;
		/**
		 * 获取绑定的卡片信息
		 * @return
		 */
		public String get_card_info() ;
		/**
		 * 停止绑定
		 * @return
		 */
		public int bind_stop() ;
		
		
		/**
		 * 开始答题
		 */
		public int  answer_start( int is_quick_response, String answer_str );
		
		/**
		 * 开始答题
		 */
		public int answer_start_with_raise_hand( int is_quick_response, int raise_hand, String answer_str ); 
		/**
		 * 设置举手功能
		 * @param raise_hand
		 * @return
		 */
		public int set_raise_hand(int raise_hand) ;
		
		/**
		 * 设置签到功能
		 * @param attendance
		 * @return
		 */
		public int set_sign_in(int attendance) ;
		
		/**
		 * 获取答案
		 * @return
		 */
		public String get_answer_list() ; 
		
		/**
		 * 停止作答
		 * @return
		 */
		public int answer_stop() ;
		
		
		public int set_student_id(String student_id_str) ;
		
		public String get_student_id_info() ;
		
		public int set_channel(int tx_ch , int rx_ch) ;
		
		public int set_tx_power(int tx_power) ;
		
		public int attendance_24g(int is_open , int pro_index) ;
		
		
		//////////////////////////////////////////////////
		/**
		 * 以下为M1版本函数
		 */
		//////////////////////////////////////////////////
		/**
		 * 读取答题器UID:刷卡
		 * @return
		 */
		public int read_card_uid_start() ;
		
		/**
		 * 获取绑定的卡片信息
		 * @return
		 */
		public String get_card_uid_Info() ;
		
		/**
		 * 停止读取答题器UID
		 * @return
		 */
		public int read_card_uid_stop() ;
		
		//绑定相关函数(M1版本）
		/**
		 * 开始绑定:无线
		 * @param mode 绑定方式   1.无线手动绑定    2:无线自动绑定
		 * @param uid_str  需要绑定的答题器的UI D名单，只有当mode为2（无线自动）时才有意义，
		 * 此list 是一个JSON格式的数组，格式如下
		 * [
         *  { 'uid': '0983584512' },
         *  { 'uid': '1180852736' }
         *  ]
		 * @return
		 */
		public int wireless_bind_start(int mode , String uid_str ) ;
		
		/**
		 * 获取绑定的卡片信息
		 * @return
		 */
		public String get_wireless_bind_info() ;
		
		/**
		 * 停止绑定：无线
		 * @param mode
		 * @return
		 */
		public int wireless_bind_stop() ;
		
		/**
		 * 开始举手,监听开始
		 * @return
		 */
		public int raise_hand_start() ;
		
		/**
		 * 停止举手,监听结束
		 * @return
		 */
		public int raise_hand_stop() ;
		
		/**
		 * 获取学生提交的举手信息.
		 * @return
		 */
		public String get_raise_hand_list() ;
		
		/////////////
		//签到相关函数
		/////////////
		
		/**
		 * 开始签到
		 * @return
		 */
		public int sign_in_start() ;
		
		/**
		 * 停止签到
		 * @return
		 */
		public int sign_in_stop() ;
		
		/**
		 * 获取学生提交的签到信息
		 * @return
		 */
		public String get_sign_in_list() ;
		
		/**
		 * 设置2.4G考勤功能(绑定前设置有效)
		 * @param is_open
		 * @param pro_index
		 * @return
		 */
		public int set_attendance_24g(int is_open , int pro_index) ;
		
		/**
		 * 无线设置学号
		 * @param uid_str
		 * @param student_id_str
		 * @return
		 */
		public int set_wireless_student_id(String uid_str , String student_id_str) ; 
		
		/**
		 * 无线获取设置学号卡片信息
		 * @return
		 */
		public String get_wireless_student_id_info() ;
}
