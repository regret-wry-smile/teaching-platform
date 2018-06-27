package com.zkxltech.service.impl;

import com.ejet.cache.RedisMapScore;
import com.ejet.cache.RedisMapVote;
import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.Vote;
import com.zkxltech.service.VoteService;
import com.zkxltech.ui.util.StringUtils;

public class VoteServiceImpl implements VoteService{
	private Result result = new Result();


	@Override
	public Result startVote(Object object) {
		result = new Result();
		try {
		    Vote vote =  (Vote) StringUtils.parseJSON(object, Vote.class);
		    Result reStart = EquipmentServiceImpl.getInstance().startVote(vote.getPrograms().size());
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
