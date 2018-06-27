package com.zkxltech.service.impl;

import com.ejet.cache.RedisMapScore;
import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.Score;
import com.zkxltech.service.ScoreService;
import com.zkxltech.ui.util.StringUtils;
import com.zkxlteck.scdll.ScDll;
import com.zkxlteck.scdll.ScoreThread;

public class ScoreServiceImpl implements ScoreService{
	private Result result = new Result();
	private static Thread thread ;
	
	public static Thread getThread() {
        return thread;
    }

    public static void setThread(Thread thread) {
        ScoreServiceImpl.thread = thread;
    }

    @Override
	public Result startScore(Object object) {
		result = new Result();
		try {
		    Score score =  (Score) StringUtils.parseJSON(object, Score.class);
		    Result reStart = scoreStart(score.getPrograms().size());
		    if (reStart.getRet().equals(Constant.ERROR)) {
               result.setMessage(reStart.getMessage());
               return result;
            }
			RedisMapScore.clearMap();//清除评分缓存
				
			RedisMapScore.addScoreInfo(score); //保存评分主题信息
			
			//将评分主题相关信息保存到缓存
			result.setMessage("开始评分！");
			result.setRet(Constant.SUCCESS);
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("开始评分失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result getScore() {
		result = new Result();
		try {
			result.setItem(RedisMapScore.getScoreInfoBar());
			result.setRet(Constant.SUCCESS);
			result.setMessage("获取评分数据成功！");
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("获取评分数据失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}
	
	@Override
	public Result getScoreTitleInfo() {
		result = new Result();
		try {
			result.setItem(RedisMapScore.getScoreInfo()); //保存评分主题信息
			result.setRet(Constant.SUCCESS);
			result.setMessage("获取评分主题成功！");
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("获取评分主题失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

    @Override
    public Result stopScore() {
        Result r = new Result();
        if (thread != null && thread instanceof ScoreThread) {
            ScoreThread c =  (ScoreThread)thread;
            c.setFLAG(false);
        }
        int answer_stop = ScDll.intance.answer_stop();
        if (answer_stop == Constant.SEND_SUCCESS) {
            r.setRet(Constant.SUCCESS);
            r.setMessage("停止成功");
            return r;
        }
        r.setRet(Constant.ERROR);
        r.setMessage("停止失败");
        return r;
    }
    @Override
    public Result scoreStart(int questionNum) {
        Result r = new Result();
        r.setRet(Constant.ERROR);
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("[");
        for (int i = 0; i < questionNum; i++) {
          strBuilder.append("{");
          String id = String.valueOf(i+1);
          String type = "d";
          String range = "";
          try {
            range = "0-9";
          } catch (Exception e2) {
            range = "";
          }
          strBuilder.append("'id':'"+id+"',");
          strBuilder.append("'type':'"+type+"',");
          strBuilder.append("'range':'"+range+"'");
          strBuilder.append("}");
          
          if (questionNum-1 != i) {
            strBuilder.append(",");
          }
        }
        strBuilder.append("]");
        int answer_start = ScDll.intance.answer_start(0, strBuilder.toString());
        if (answer_start == Constant.SEND_ERROR) {
            r.setMessage("指令发送失败");
            return r;
        }
        thread = new ScoreThread();
        thread.start();
        r.setRet(Constant.SUCCESS);
        r.setMessage("发送成功");
        return r;
    }
}
