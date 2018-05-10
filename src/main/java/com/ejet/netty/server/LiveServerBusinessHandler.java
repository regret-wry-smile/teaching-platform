package com.ejet.netty.server;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.core.util.io.IOUtils;
import com.ejet.netty.message.LiveAnswerMsg;
import com.ejet.netty.message.LiveBaseMsg;
import com.ejet.netty.message.LiveMsgType;
import com.zkxltech.teaching.AnswerVO;
import com.zkxltech.teaching.CommunicationResponse;
import com.zkxltech.teaching.msg.DeviceBindResponse;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 业务处理入口Handler，由业务线程池执行
 * 
 * @author ShenYijie
 *
 */
@Sharable//注解@Sharable可以让它在channels间共享  
public class LiveServerBusinessHandler extends ChannelInboundHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(LiveServerBusinessHandler.class);
	/**
	 * 连接异常计数
	 */
	private int lossConnectedCount = 0;
	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object evt) {
		lossConnectedCount = 0;
		LiveBaseMsg message = (LiveBaseMsg) evt; // (1)
		try {
			if (message.getType() == LiveMsgType.BEAT) {
				logger.info("{} 发送beat消息：" , ctx.channel().remoteAddress());
				LiveServerHelper.addClient(ctx.channel(), message);
	            sendBeatMsg(ctx);
	        } else {
	        	logger.info("服务端接收{}消息 {} " , ctx.channel().remoteAddress(), message.getType());
	        	if(message.getType()==LiveMsgType.ANSWER_REP) { //答题响应消息
        			LiveAnswerMsg msg = (LiveAnswerMsg)message;
        			CommunicationResponse.answerUpload((AnswerVO)msg.getContent());
	        	}
	        	if(message.getType()==LiveMsgType.BIND_CARD_REP) { //帮卡消息
        			LiveAnswerMsg msg = (LiveAnswerMsg)message;
        			CommunicationResponse.bindCardRep((DeviceBindResponse)msg.getContent());
	        	}
	       }
		} finally {
			//message.release();
		}
	}
  
    private void sendBeatMsg(ChannelHandlerContext ctx) {  
    	LiveBaseMsg message = new LiveBaseMsg();
		message.setType(LiveMsgType.BEAT);
		ctx.writeAndFlush(message);
        logger.info("{} [sent beat msg to]", getIP(ctx));
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
	
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("{} [channelActive]客户端连接成功!", getIP(ctx));
		LiveChannelGroups.add(ctx.channel());
    }
	 @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        ctx.fireChannelInactive();
        LiveChannelGroups.remove(ctx.channel());
        LiveServerHelper.removeClient(ctx.channel());
        logger.info("{} [channelInactive]客户端关闭!", getIP(ctx));
    }
	 
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) {
		logger.info("{} [exceptionCaught]客户端关闭! {}", getIP(ctx), IOUtils.getError(cause));
		//LiveChannelGroups.remove(ctx.channel());
		LiveServerHelper.removeClient(ctx.channel());
		close(ctx);//出现异常时关闭channel
    }
	
	protected void handleReaderIdle(ChannelHandlerContext ctx) {
		logger.info("{}---READER_IDLE---", getIP(ctx));
        lossConnectedCount++;
        if (lossConnectedCount>2) {
        	logger.info("{}---关闭这个不活跃通道---", getIP(ctx));
            ctx.channel().close();
        }
    }
  
    protected void handleWriterIdle(ChannelHandlerContext ctx) {
        logger.info("{}---WRITER_IDLE---", getIP(ctx));
    } 
  
    protected void handleAllIdle(ChannelHandlerContext ctx) {
    	logger.info("{}---ALL_IDLE---", getIP(ctx));
    }
   
	private void close(ChannelHandlerContext ctx) {
		if (ctx != null) {
			ChannelFuture cf = ctx.close();
			close(cf.channel());
		}
	}

	private void close(Channel cl) {
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
	

	
	
}
