package com.zkxltech.service.impl;

import com.ejet.cache.RedisMapScore;
import com.ejet.cache.RedisMapVote;
import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.Vote;
import com.zkxltech.service.VoteService;
import com.zkxltech.ui.util.StringUtils;
import com.zkxlteck.scdll.ScDll;
import com.zkxlteck.thread.VoteThread;

public class VoteServiceImpl implements VoteService{
	private Result result = new Result();
	private static Thread thread ;
	public static Thread getThread() {
        return thread;
    }

    public static void setThread(Thread thread) {
        VoteServiceImpl.thread = thread;
    }

    @Override
	public Result startVote(Object object) {
		result = new Result();
		try {
		    Vote vote =  (Vote) StringUtils.parseJSON(object, Vote.class);
		    Result reStart = startVote(vote.getPrograms().size());
		    if (reStart.getRet().equals(Constant.ERROR)) {
               result.setMessage(reStart.getMessage());
               return result;
            }
			RedisMapVote.clearMap();//清除投票缓存
				
			RedisMapVote.addVoteInfo(vote); //投票评分主题信息
			
			//将评分主题相关信息保存到缓存
			result.setMessage("开始投票！");
			result.setRet(Constant.SUCCESS);
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("开始投票失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result getVote() {
		result = new Result();
		try {
			result.setItem(RedisMapVote.getVoteInfoBar());
			result.setRet(Constant.SUCCESS);
			result.setMessage("获取投票数据成功！");
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("获取投票数据失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}

	@Override
	public Result stopVote() {
        Result r = new Result();
        if (thread != null && thread instanceof VoteThread) {
            VoteThread c =  (VoteThread)thread;
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
	public Result startVote(int questionNum) {
        Result r = new Result();
        r.setRet(Constant.ERROR);
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("[");
        for (int i = 0; i < questionNum; i++) {
          strBuilder.append("{");
          String id = String.valueOf(i+1);
          String type = "s";
          String range = "";
          try {
            range = "A-C";
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
        thread = new VoteThread();
        thread.start();
        r.setRet(Constant.SUCCESS);
        r.setMessage("发送成功");
        return r;
    }
	
	@Override
	public Result getVoteTitleInfo() {
		result = new Result();
		try {
			result.setItem(RedisMapVote.getVoteInfo()); //保存评分主题信息
			result.setRet(Constant.SUCCESS);
			result.setMessage("获取投票主题成功！");
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setMessage("获取投票主题失败！");
			result.setDetail(IOUtils.getError(e));
			return result;
		}
	}
	
}
