package com.ejet.netty.client;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.io.IOUtils;
import com.ejet.netty.message.LiveAnswerMsg;
import com.ejet.netty.message.LiveBaseMsg;
import com.ejet.netty.message.LiveMsgType;
import com.ejet.netty.server.LiveChannelGroups;
import com.ejet.netty.server.LiveServerHelper;
import com.zkxltech.config.ConfigConstant;
import com.zkxltech.config.Global;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 
 * netty socket客户端实现类
 * 
 * @author ShenYijie
 *
 */
public abstract class LiveClientBaseHandler extends ChannelInboundHandlerAdapter {
	
	final Logger logger = LoggerFactory.getLogger(LiveClientBaseHandler.class);
	
	protected String name = "ChannelInboundHandlerAdapter";
	/**
	 * 连接异常计数
	 */
	private int lossConnectedCount = 0;
	
	public LiveClientBaseHandler(String name) {
		this.name = name;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		lossConnectedCount = 0;
		LiveBaseMsg message = (LiveBaseMsg) msg; // (1)
		try {
			if (message.getType() == LiveMsgType.BEAT) {
				logger.info("{}, {} 发送心跳消息：" , name, ctx.channel().remoteAddress());
	        } else {
	        	handleData(ctx, message);
	        	logger.info("{}, {} 处理消息{}" , name, getIP(ctx));
	       }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
	}

	@Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {  
            IdleStateEvent e = (IdleStateEvent) evt;  
            switch (e.state()) {  
                case READER_IDLE:  
                    handleReaderIdle(ctx);  
                    break;  
                case WRITER_IDLE:  
                    handleWriterIdle(ctx);  
                    break;  
                case ALL_IDLE:  
                    handleAllIdle(ctx);  
                    break;  
                default:  
                    break;  
            }  
        } else {
            super.userEventTriggered(ctx,evt);
        }
    }
	
	protected abstract void handleData(ChannelHandlerContext ctx, Object evt) throws Exception;
	
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("[channelActive] {}, 连接成功!", getInfo(ctx));
		LiveChannelGroups.add(ctx.channel());
    }
	 @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        ctx.fireChannelInactive();
        LiveChannelGroups.remove(ctx.channel());
        logger.info("[channelInactive]{} 关闭!", getInfo(ctx));
        closeAndReConnect(ctx);
    }
	 
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) {
		logger.info("[exceptionCaught]{}, 关闭! {}", getInfo(ctx), IOUtils.getError(cause));
		//LiveChannelGroups.remove(ctx.channel());
//		close(ctx);//出现异常时关闭channel
		closeAndReConnect(ctx);
		
    }
	
	protected void handleReaderIdle(ChannelHandlerContext ctx) {
		logger.info("{}---READER_IDLE---", getInfo(ctx));
        lossConnectedCount++;
        if (lossConnectedCount>2) {
        	logger.info("{}---关闭这个不活跃通道---", getInfo(ctx));
            ctx.channel().close();
        }
    }
  
    protected void handleWriterIdle(ChannelHandlerContext ctx) {
        logger.info("{}{}---WRITER_IDLE---", name, getIP(ctx));
        lossConnectedCount++;
        LiveAnswerMsg msg = new LiveAnswerMsg();
    	msg.setType(LiveMsgType.BEAT);
    	msg.setClientName(ConfigConstant.clientLocalConf.getClass_name());
    	msg.setClientId(Global.getClientUUID());
    	LiveNettyClientHelper.send(msg);
        if (lossConnectedCount>2) {
        	logger.info("{}---关闭这个不活跃通道---", getInfo(ctx));
            ctx.channel().close();
        }
    } 
  
    protected void handleAllIdle(ChannelHandlerContext ctx) {
    	logger.info("{}---ALL_IDLE---", getInfo(ctx));
    }
   
    /**
     * 关闭，并重新连接
     * @param ctx
     */
    private void closeAndReConnect(ChannelHandlerContext ctx) {
    	close(ctx);
    	LiveNettyClientHelper.reConnected();
    }
    
    protected void close(ChannelHandlerContext ctx) {
		if (ctx != null) {
			ChannelFuture cf = ctx.close();
			close(cf.channel());
		}
	}

    protected void close(Channel cl) {
		if (cl != null) {
			cl.flush();
			cl.close();
		}
	}
	
    /**
     * 获取IP信息
     * 
     * @param ctx
     * @return
     */
    protected String getIP(ChannelHandlerContext ctx) {
		Channel ch = ctx.channel();
		InetSocketAddress insocket = (InetSocketAddress)ch.remoteAddress();
		String ip = insocket.getAddress().getHostAddress();
		return ip;
	}
	
    /**
     * 客户端连接信息
     * 
     * @param ctx
     * @return
     */
    protected String getInfo(ChannelHandlerContext ctx) {
		return name + getIP(ctx);
	}
	
	
}
