package com.zkxltech.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.zkxltech.service.EquipmentService2;
import com.zkxltech.sql.StudentInfoSql;
import com.zkxltech.thread.BaseThread;
import com.zkxltech.thread.MsgThread;
import com.zkxltech.thread.MsgThread2;
import com.zkxltech.thread.MultipleAnswerThread;
import com.zkxltech.thread.ThreadManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author: luozheng
 * @date:2018年7月17日 下午2:54:45
 */
public class EquipmentServiceImpl2 implements EquipmentService2 {
	private static final Logger log = LoggerFactory.getLogger(EquipmentServiceImpl2.class);
	private static final EquipmentServiceImpl2 SINGLE = new EquipmentServiceImpl2();

	private EquipmentServiceImpl2() {
	}

	public static EquipmentServiceImpl2 getInstance() {
		return SINGLE;
	}

	@Override
	public Result get_device_info() {
		Result r = new Result();
		try {
			if (SerialPortManager.sendToPort(EquipmentConstant.GET_DEVICE_INFO_CODE)) {
				Vector<Thread> threads = new Vector<Thread>();
				Thread iThread = new MsgThread(EquipmentConstant.GET_DEVICE_INFO);
				threads.add(iThread);
				iThread.start();
				// 等待所有线程执行完毕
				iThread.join();
				
				String str = SerialListener.getDataMap().get(0);
				if (com.zkxltech.ui.util.StringUtils.isEmpty(str)) {
					r.setRet(Constant.ERROR);
					r.setMessage("指令发送失败");
					return r;
				}
				r.setItem(str);
				SerialListener.clearMap();
				r.setRet(Constant.SUCCESS);
			} else {
				r.setRet(Constant.ERROR);
				r.setMessage("指令发送失败");
			}
		} catch (Exception e) {
			log.error(IOUtils.getError(e));
			r.setRet(Constant.ERROR);
			r.setMessage("指令发送失败");
		}

		return r;
	}


	/** 设备和数据库绑定的状态同步 */
	@Override
	public Result equipmentDatabaseSynchronization() {
		Result r = new Result();
		try {
			if (SerialPortManager.sendToPort(EquipmentConstant.GET_DEVICE_INFO_CODE)) {
				Vector<Thread> threads = new Vector<Thread>();
				Thread iThread = new MsgThread(EquipmentConstant.GET_DEVICE_INFO_CODE);
				threads.add(iThread);
				iThread.start();
				// 等待所有线程执行完毕
				iThread.join();
				
				String str = SerialListener.getDataMap().get(0);
				if (com.zkxltech.ui.util.StringUtils.isEmpty(str)) {
					r.setRet(Constant.ERROR);
					r.setMessage("指令发送失败");
					return r;
				}
				r.setItem(str);
				SerialListener.clearMap();
				r.setRet(Constant.SUCCESS);
				
				StudentInfoSql studentInfoSql = new StudentInfoSql();
	            List<String> iclickerIds = getEquipmentAllUid(str);
	            //将结果保存到项目全局变量中
	            
	            Global.setIclickerIds(iclickerIds);
	            
	            /**如果设备没有值,直接将库全改为未绑定*/
	            if (ListUtils.isEmpty(iclickerIds)) {
	                r = studentInfoSql.updateStatus(Constant.BING_NO);
	           }else{
	               /**有值的将库里对应的学生改为绑定,没值的全部是未绑定*/
	               r = studentInfoSql.updateStatusByIclickerIds(iclickerIds,Constant.BING_YES);
	               if (r.getRet().equals(Constant.ERROR)) {
	                   return r;
	               }
	               r = studentInfoSql.updateStatusByIclickerIds(iclickerIds, Constant.BING_NO," not in");
	               if (r.getRet().equals(Constant.ERROR)) {
	                   return r;
	               }
	               r.setRet(Constant.SUCCESS);
	           }
			} else {
				r.setRet(Constant.ERROR);
				r.setMessage("指令发送失败");
			}
		} catch (Exception e) {
			log.error(IOUtils.getError(e));
			r.setRet(Constant.ERROR);
			r.setMessage("指令发送失败");
		}

		return r;
	}

	private List<String> getEquipmentAllUid(String get_device_info) {
		List<String> uids = new ArrayList<>();
		Object list = JSONObject.fromObject(get_device_info).get("list");
		JSONArray jsonArray = JSONArray.fromObject(list);
		for (Object object : jsonArray) {
			JSONObject jo = JSONObject.fromObject(object);
			String uid = jo.getString("uid");
			if (!com.zkxltech.ui.util.StringUtils.isEmpty(uid)) {
				uids.add(uid);
			}
		}
		return uids;
	}

	@Override
	public Result answerStart2(String answerType, Object param) {
		Result r = new Result();
		try {
			/* 停止所有线程 */
			ThreadManager.getInstance().stopAllThread();
			List<Answer> answers = (List<Answer>) JSONArray.toCollection(JSONArray.fromObject(param),
					Answer.class);
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append("[");

			for (int i = 0; i < answers.size(); i++) {
				Answer requestVo = answers.get(i);
				String id = requestVo.getId();
				String type = requestVo.getType();
				strBuilder.append("{");
				if ("单选".equals(type)) {
					type = "s";
				} else if ("多选".equals(type)) {
					type = "m";
				} else if ("判断".equals(type)) {
					type = "j";
				} else if ("数字".equals(type)) {
					type = "d";
				}
				String range = requestVo.getRange();
				strBuilder.append("'id':'" + id + "',");
				strBuilder.append("'type':'" + type + "',");
				strBuilder.append("'range':'" + range + "'");
				strBuilder.append("}");

				strBuilder.append(",");

			}
			strBuilder = new StringBuilder(strBuilder.substring(0, strBuilder.lastIndexOf(",")));
			
			if (SerialPortManager.sendToPort(EquipmentConstant.ANSWER_START_CODE(answers))) {
				Vector<Thread> threads = new Vector<Thread>();
				Thread iThread = new MsgThread2(EquipmentConstant.ANSWER_START);
				threads.add(iThread);
				iThread.start();
				// 等待所有线程执行完毕
				iThread.join();
				
				String str = SerialListener.getRetCode();
				SerialListener.clearRetCode();
				r = EquipmentUtils.parseResult(str);
				if (Constant.ERROR.equals(r.getRet())) {
					r.setRet(Constant.ERROR);
					r.setMessage("指令发送失败");
					return r;
				}
				r.setItem(str);
				SerialListener.clearMap();
				r.setRet(Constant.SUCCESS);
				
				if (r.getRet() == Constant.SUCCESS) {
					BaseThread thread = new MultipleAnswerThread(answerType);
					thread.start();
					// 添加到线程管理
					ThreadManager.getInstance().addThread(thread);
				}
				return r;
			} else {
				r.setRet(Constant.ERROR);
				r.setMessage("指令发送失败");
			}
		} catch (Exception e) {
			log.error(IOUtils.getError(e));
			r.setRet(Constant.ERROR);
			r.setMessage("指令发送失败");
		}

		return r;
	}

	@Override
	public Result answer_stop() {
		Result r = new Result();
		try {
			if (SerialPortManager.sendToPort(EquipmentConstant.ANSWER_STOP_CODE)) {
				Vector<Thread> threads = new Vector<Thread>();
				Thread iThread = new MsgThread2(EquipmentConstant.ANSWER_STOP);
				threads.add(iThread);
				iThread.start();
				// 等待所有线程执行完毕
				iThread.join();
				
				String str = SerialListener.getRetCode();
				SerialListener.clearRetCode();
				SerialListener.clearStringBuffer();
				r = EquipmentUtils.parseResult(str);
				if (Constant.ERROR.equals(r.getRet())) {
					r.setRet(Constant.ERROR);
					r.setMessage("指令发送失败");
					return r;
				}else {
					r = EquipmentUtils.parseResult(str);
				}
				SerialListener.clearMap();
				r.setRet(Constant.SUCCESS);
			} else {
				r.setRet(Constant.ERROR);
				r.setMessage("指令发送失败");
			}
		} catch (Exception e) {
			log.error(IOUtils.getError(e));
			r.setRet(Constant.ERROR);
			r.setMessage("指令发送失败");
		}
		return r;
	}

	public Result answer_start(int is_quick_response, String answer_str) {
		Result r = new Result();
		try {
			if (SerialPortManager.sendToPort(EquipmentConstant.ANSWER_START_CODE(answer_str))) {
				Vector<Thread> threads = new Vector<Thread>();
				Thread iThread = new MsgThread2(EquipmentConstant.ANSWER_START);
				threads.add(iThread);
				iThread.start();
				// 等待所有线程执行完毕
				iThread.join();
				
				String str = SerialListener.getRetCode();
				SerialListener.clearRetCode();
				r = EquipmentUtils.parseResult(str);
				if (Constant.ERROR.equals(r.getRet())) {
					r.setRet(Constant.ERROR);
					r.setMessage("指令发送失败");
					return r;
				}else {
					r = EquipmentUtils.parseResult(str);
				}
				SerialListener.clearMap();
			} else {
				r.setRet(Constant.ERROR);
				r.setMessage("指令发送失败");
			}
		} catch (Exception e) {
			log.error(IOUtils.getError(e));
			r.setRet(Constant.ERROR);
			r.setMessage("指令发送失败");
		}

		return r;
	}
	
	public Result bind_stop() {
		Result r = new Result();
		try {
			if (SerialPortManager.sendToPort(EquipmentConstant.WIRELESS_BIND_STOP_CODE)) {
				Vector<Thread> threads = new Vector<Thread>();
				Thread iThread = new MsgThread2(EquipmentConstant.WIRELESS_BIND_STOP);
				threads.add(iThread);
				iThread.start();
				// 等待所有线程执行完毕
				iThread.join();
				
				String str = SerialListener.getRetCode();
				SerialListener.clearRetCode();
				SerialListener.clearStringBuffer();
				r = EquipmentUtils.parseResult(str);
				if (Constant.ERROR.equals(r.getRet())) {
					r.setRet(Constant.ERROR);
					r.setMessage("指令发送失败");
					return r;
				}
				r.setItem(str);
				SerialListener.clearMap();
				r.setRet(Constant.SUCCESS);
			} else {
				r.setRet(Constant.ERROR);
				r.setMessage("指令发送失败");
			}
		} catch (Exception e) {
			log.error(IOUtils.getError(e));
			r.setRet(Constant.ERROR);
			r.setMessage("指令发送失败");
		}

		return r;
	}
	
	public Result clear_wl() {
		Result r = new Result();
		try {
			if (SerialPortManager.sendToPort(EquipmentConstant.CLEAR_WL_CODE)) {
				Vector<Thread> threads = new Vector<Thread>();
				Thread iThread = new MsgThread2(EquipmentConstant.CLEAR_WL);
				threads.add(iThread);
				iThread.start();
				// 等待所有线程执行完毕
				iThread.join();
				
				String str = SerialListener.getRetCode();
				SerialListener.clearRetCode();
				r = EquipmentUtils.parseResult(str);
				if (Constant.ERROR.equals(r.getRet())) {
					r.setRet(Constant.ERROR);
					r.setMessage("指令发送失败");
					return r;
				}
				r.setItem(str);
				SerialListener.clearMap();
				r.setRet(Constant.SUCCESS);
			} else {
				r.setRet(Constant.ERROR);
				r.setMessage("指令发送失败");
			}
		} catch (Exception e) {
			log.error(IOUtils.getError(e));
			r.setRet(Constant.ERROR);
			r.setMessage("指令发送失败");
		}

		return r;
	}


	@Override
	public Result set_channel(int tx_ch, int rx_ch) {
		Result r = new Result();
		try {
			if (SerialPortManager.sendToPort(EquipmentConstant.SET_CHANNEL_CODE(tx_ch, rx_ch))) {
				Vector<Thread> threads = new Vector<Thread>();
				Thread iThread = new MsgThread2(EquipmentConstant.SET_CHANNEL);
				threads.add(iThread);
				iThread.start();
				// 等待所有线程执行完毕
				iThread.join();
				
				String str = SerialListener.getRetCode();
				SerialListener.clearRetCode();
				if (com.zkxltech.ui.util.StringUtils.isEmpty(str)) {
					r.setRet(Constant.ERROR);
					r.setMessage("指令发送失败");
					return r;
				}else {
					r = EquipmentUtils.parseResult(str);
				}
				SerialListener.clearMap();
			} else {
				r.setRet(Constant.ERROR);
				r.setMessage("指令发送失败");
			}
		} catch (Exception e) {
			log.error(IOUtils.getError(e));
			r.setRet(Constant.ERROR);
			r.setMessage("指令发送失败");
		}

		return r;
	}

	@Override
	public Result set_tx_power(int tx_power) {
		Result r = new Result();
		try {
			if (SerialPortManager.sendToPort(EquipmentConstant.SET_TX_POWER_CODE(tx_power))) {
				Vector<Thread> threads = new Vector<Thread>();
				Thread iThread = new MsgThread2(EquipmentConstant.SET_TX_POWER);
				threads.add(iThread);
				iThread.start();
				// 等待所有线程执行完毕
				iThread.join();
				
				String str = SerialListener.getRetCode();
				SerialListener.clearRetCode();
				if (com.zkxltech.ui.util.StringUtils.isEmpty(str)) {
					r.setRet(Constant.ERROR);
					r.setMessage("指令发送失败");
					return r;
				}else {
					r = EquipmentUtils.parseResult(str);
				}
				SerialListener.clearMap();
			} else {
				r.setRet(Constant.ERROR);
				r.setMessage("指令发送失败");
			}
		} catch (Exception e) {
			log.error(IOUtils.getError(e));
			r.setRet(Constant.ERROR);
			r.setMessage("指令发送失败");
		}

		return r;
	};
}
