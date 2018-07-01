package com.zkxltech.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.cache.BrowserManager;
import com.ejet.cache.RedisMapBind;
import com.ejet.core.util.comm.ListUtils;
import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;
import com.mysql.jdbc.log.Log;
import com.zkxltech.domain.ClassInfo;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.service.ClassInfoService;
import com.zkxltech.sql.ClassInfoSql;
import com.zkxltech.sql.StudentInfoSql;
import com.zkxltech.ui.util.StringUtils;
import com.zkxlteck.scdll.ScDll;
import com.zkxlteck.thread.CardInfoThread;

import net.sf.json.JSONObject;

public class ClassInfoServiceImpl implements ClassInfoService{
    private static final Logger log = LoggerFactory.getLogger(ClassInfoServiceImpl.class);
	private Result result;
	private ClassInfoSql classInfoSql = new ClassInfoSql();
	private StudentInfoSql studentInfoSql = new StudentInfoSql();
	private static Thread thread;
	
	public static Thread getThread() {
        return thread;
    }

    public static void setT(Thread thread) {
        ClassInfoServiceImpl.thread = thread;
    }

    @Override
	public Result insertClassInfo(Object object) {
		result = new Result();
		try {
			ClassInfo classInfo =  (ClassInfo) StringUtils.parseJSON(object, ClassInfo.class);
			result = classInfoSql.insertClassInfo(classInfo);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("新增班级成功!");
			}else {
				result.setMessage("新增班级失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("新增班级失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result selectClassInfo(Object object) {
		result = new Result();
		try {
			ClassInfo classInfo =  (ClassInfo) StringUtils.parseJSON(object, ClassInfo.class);
			result = classInfoSql.selectClassInfo(classInfo);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("查询班级成功!");
			}else {
				result.setMessage("查询班级失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("查询班级失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result deleteClassInfo(Object object) {
		result = new Result();
		try {
			ClassInfo classInfo =  (ClassInfo) StringUtils.parseJSON(object, ClassInfo.class);
			result = classInfoSql.deleteClassInfo(classInfo); //删除班级
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("删除班级成功!");
			}else {
				result.setMessage("删除班级失败！");
				return result;
			}
			StudentInfo studentInfo = new StudentInfo();
			studentInfo.setClassId(classInfo.getClassId());  //删除学生
			studentInfoSql.deleteStudent(studentInfo);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("删除班级成功!");
			}else {
				result.setMessage("删除班级失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("删除班级失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result updateClassInfo(Object object) {
		result = new Result();
		try {
			ClassInfo classInfo =  (ClassInfo) StringUtils.parseJSON(object, ClassInfo.class);
			result = classInfoSql.updateClassInfo(classInfo);
			if (Constant.SUCCESS.equals(result.getRet())) {
				result.setMessage("修改班级成功!");
			}else {
				result.setMessage("修改班级失败！");
			}
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("修改班级失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	/**清除白名单*/
    @Override
    public Result clearWl(Object param) {
        Result r = new Result();
        r.setRet(Constant.ERROR);
        
        String get_device_info = ScDll.intance.get_device_info();
        if (StringUtils.isEmpty(get_device_info)) {
            r.setMessage("设备故障,请重启设备");
            return r;
        }
        try {
            StudentInfoSql studentInfoSql = new StudentInfoSql();
            r = studentInfoSql.updateStatus(Constant.BING_NO);
            if (r.getRet().equals(Constant.ERROR)) {
                return r;
            }
            int clear_wl = ScDll.intance.clear_wl();
            if (clear_wl == Constant.SEND_SUCCESS) {
                JSONObject jsono = JSONObject.fromObject(param);
                if (jsono.containsKey("classId")) {
                    BrowserManager.refreshStudent(jsono.getString("classId"));
                }
                r.setRet(Constant.SUCCESS);
                r.setMessage("清除成功");
                return r;
            }
        } catch (Exception e) {
            r.setDetail(IOUtils.getError(e));
        }
        r.setMessage("清除失败");
        return r;
    }
    @Override
    public Result bindStart(Object param) {
        Result r = new Result();
        r.setRet(Constant.ERROR);
      //每次调用绑定方法先清空,再存
        RedisMapBind.clearCardIdMap();
        RedisMapBind.clearBindMap();
        try {
            int bind_start = ScDll.intance.wireless_bind_start(1,"") ;
            if (bind_start < 1) {
                int bind_start2 = ScDll.intance.wireless_bind_start(1,"") ;
                if (bind_start2 < 1) {
                    log.error("\"开始绑定\"指令发送失败");
                    r.setMessage("指令发送失败");
                    return r;
                }
            }
            log.info("\"开始绑定\"指令发送成功");
            /**根据班级id查询学生信息*/
            StudentInfoServiceImpl sis= new StudentInfoServiceImpl();
            Result result = sis.selectStudentInfo(param);
            List<StudentInfo> studentInfos = (List)result.getItem();
            if (result== null || ListUtils.isEmpty(studentInfos)) {
                r.setMessage("您还未上传学生信息");
                return r;
            }
            /**将查出来的学生信息按学生编号进行分类,并存入静态map中*/
            Map<String, StudentInfo> studentInfoMap = new HashMap<>();
            /**按绑定状态进行分类*/
            int bind = 0,notBind = 0 ;
            for (StudentInfo studentInfo : studentInfos) {
                if (studentInfo.getStatus().equals(Constant.BING_YES)) {
                    ++bind;
                }else{
                    ++notBind;
                }
                if (!StringUtils.isEmpty(studentInfo.getIclickerId())) {
                    studentInfoMap.put(studentInfo.getIclickerId(), studentInfo);
                }
            }
            /**存入静态map*/
            RedisMapBind.getBindMap().put("studentName", null);
            RedisMapBind.getBindMap().put("code", bind_start);
            RedisMapBind.getBindMap().put("accomplish", bind);
            RedisMapBind.getBindMap().put("notAccomplish",notBind);
            RedisMapBind.setStudentInfoMap(studentInfoMap);
            thread = new CardInfoThread();
            thread.start();
            r.setItem(bind_start);
            r.setRet(Constant.SUCCESS);
            r.setMessage("操作成功");
        } catch (Exception e) {
            r.setMessage("操作失败");
            r.setDetail(e.getMessage());
        } 
        return r;
    }
    
    @Override
    public Result bindStop() {
        Result r = new Result();
        if (thread != null && thread instanceof CardInfoThread) {
            CardInfoThread c =  (CardInfoThread)thread;
            c.setFLAG(false);
            log.info("绑定线程停止成功");
        }else{
            log.error("绑定线程停止失败");
        }
        int bind_stop = ScDll.intance.wireless_bind_stop();
        if (bind_stop == Constant.SEND_ERROR) {
            int bind_stop2 = ScDll.intance.wireless_bind_stop();
            if (bind_stop2 == Constant.SEND_ERROR) {
                r.setRet(Constant.ERROR);
                r.setMessage("停止指令发送失败");
                log.info("\"停止绑定\"失败");
                return r;
            }
           
        }
        log.info("\"停止绑定\"成功");
        r.setRet(Constant.SUCCESS);
        r.setMessage("停止成功");
        return r;
    }
}
