package com.ejet.netty.client;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejet.netty.NettyConstant;
import com.ejet.netty.message.LiveAnswerMsg;
import com.ejet.netty.message.LiveMsgType;
import com.ejet.netty.server.LiveServerBusinessHandler;
import com.zkxltech.config.ConfigConstant;
import com.zkxltech.config.Global;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Netty客户端连接
 * 
 * @author ShenYijie	
 *
 */
public class LiveNettyClient {
	private static final Logger logger = LoggerFactory.getLogger(LiveServerBusinessHandler.class);
    private int port;  
    private String host;  
    public SocketChannel channel;  
    private Bootstrap bootstrap = null;
    private NioEventLoopGroup workGroup = null;
    private int reconnectedTimeout = ConfigConstant.clientLocalConf.getReconnect_interval_time();
    		
    public LiveNettyClient(String host, int port) {  
        this.port = port;  
        this.host = host;  
    }
    
    public void start(){  
        //ChannelFuture future = null;  
        try {
        	
        	workGroup = new NioEventLoopGroup(4);  
            bootstrap = new Bootstrap();  
            bootstrap.channel(NioSocketChannel.class);  
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.option(ChannelOption.TCP_NODELAY, true); 
            //连接超时时间
            reconnectedTimeout = reconnectedTimeout<1 ? 1 :reconnectedTimeout;
            int connectTimeout = ConfigConstant.clientLocalConf.getConnect_timeout();
            connectTimeout = connectTimeout<1 ? 1 : connectTimeout;
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout);
            bootstrap.group(workGroup);  
            bootstrap.remoteAddress(host, port);
            bootstrap.handler(new LoggingHandler(LogLevel.INFO));
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {  
                @Override  
                protected void initChannel(SocketChannel ch) throws Exception {
                	//获取管道
                    ChannelPipeline pipe = ch.pipeline();
                    pipe.addLast(new IdleStateHandler(0, 10, 0));
                    pipe.addLast(new ObjectDecoder(1024 * 1024, ClassResolvers  
                            .cacheDisabled(this.getClass().getClassLoader())));
                    ch.pipeline().addLast(new ObjectEncoder());
                    pipe.addLast(new LiveClientBusinessHandler("【客户端连接服务端】"));  
                }  
            });
            
            //开启连接
            doConnect();
//            future = bootstrap.connect(host,port).sync();  
//            if (future.isSuccess()) {  
//            	channel = (SocketChannel)future.channel();  
//            	logger.info("client connect server  成功---------");
//            } else {
//            	logger.info("连接失败！");
//            	logger.info("准备重连！");
//                start();
//            } 
        } catch (Exception e) {  
        	throw new RuntimeException(e);
        }finally{  
        }
    }
    
    /**
     * 重连机制
     */
    protected void doConnect() {
    	if (channel != null && channel.isActive()) {
            return;
        }
    	ChannelFuture future = bootstrap.connect(host, port);
    	future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture futureListener) throws Exception {
                if (futureListener.isSuccess()) {
                    channel = (SocketChannel) futureListener.channel();
                    //设置通道标识
                    sendBeat();
                    //重发失败的数据
                    LiveNettyClientHelper.reSendCache();
                    logger.info("Connect to server successfully!");
                } else {
                	logger.info("Failed to connect to server, try connect after {} s", reconnectedTimeout);
                    futureListener.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            doConnect();
                        }
                    }, reconnectedTimeout, TimeUnit.SECONDS);
                }
            }
        });
    }
    
    
    /**
     * 
     * 发送数据 
     * 
     * @throws Exception
     */
    public int sendData(Object msg) {
    	int length = -1;
    	try {
	        if (channel != null && channel.isActive()) {
	        	ChannelFuture cf = channel.writeAndFlush(msg);
	        } else  {
	        	doConnect();
	        }
        } catch (Exception e) {  
        	logger.error("客户端发送消息错误", e);
        	start();
        }
        return length;
    }
    
    
    /**
     * 发送心跳消息
     */
    private void sendBeat() {
    	LiveAnswerMsg msg = new LiveAnswerMsg();
     	msg.setType(LiveMsgType.BEAT);
     	msg.setClientName(ConfigConstant.clientLocalConf.getClass_name());
     	msg.setClientId(Global.getClientUUID());
     	sendData(msg);
    }
    
}
