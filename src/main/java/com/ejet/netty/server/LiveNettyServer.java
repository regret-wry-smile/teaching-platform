package com.ejet.netty.server;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

/**
 * netty 服务端（基于socket）
 * 
 * @author ShenYijie
 *
 */
public class LiveNettyServer {
	private static final Logger logger = LoggerFactory.getLogger(LiveNettyServer.class);
	private int port = 8800;
	private String host = null;
	private DefaultEventExecutorGroup bizGroup = null;
	private int bizThreadNum = 10;
	
	public LiveNettyServer(String host, int port) {
		this.port = port;
		this.host = host;
	}
	
	public LiveNettyServer(int port) throws InterruptedException {
		this(null, port);
	}
	
	public void bind() throws Exception {
		//接收客户端的 TCP 连接
		EventLoopGroup boss = new NioEventLoopGroup(1);
		// 用于处理 I/O、执行系统 Task 和定时任务
		EventLoopGroup worker = new NioEventLoopGroup(4);
		try {
			bizGroup = new DefaultEventExecutorGroup(bizThreadNum);
			ServerBootstrap sbs = new ServerBootstrap();
			sbs.group(boss, worker);
			sbs.channel(NioServerSocketChannel.class);
			sbs.option(ChannelOption.SO_BACKLOG, 128);
			//Boss线程采用内存池
			sbs.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
			sbs.option(ChannelOption.SO_KEEPALIVE, true);
			sbs.option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(128, 512, 65535));
			//worker线程采用内存池
			sbs.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT); //使用内存池
			// 通过NoDelay禁用Nagle,使消息立即发出去，不用等待到一定的数据量才发出去
			sbs.option(ChannelOption.TCP_NODELAY, true);
			
			if(host!=null) {
				sbs.localAddress(host, port);
			} else {
				sbs.localAddress(port);
			}
			// 保持长连接状态
			sbs.childHandler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					// TODO Auto-generated method stub
					//获取管道
                    ChannelPipeline pipe = ch.pipeline();
                    pipe.addLast(new LoggingHandler(LogLevel.DEBUG));
					//1.读操作空闲时间，2.写操作空闲时间 3.读写操作空闲时间，
                    pipe.addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));
                    //添加对象解码器 负责对序列化POJO对象进行解码 设置对象序列化最大长度为1M 防止内存溢出
                    // 设置线程安全的WeakReferenceMap对类加载器进行缓存 支持多线程并发访问 防止内存溢出 
                    //pipe.addLast("encoder", new LiveEncoder());
                    pipe.addLast(new ObjectDecoder(1024 * 1024, ClassResolvers  
                            .weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                    //添加对象编码器 在服务器对外发送消息的时候自动将实现序列化的POJO对象编码
                    pipe.addLast(new ObjectEncoder());
                    pipe.addLast(bizGroup, new LiveServerBusinessHandler());
				}
			});
			ChannelFuture cf = sbs.bind(port).sync();  
		    if(cf.isSuccess()) {
		    	logger.info("teacher server start........");
		    }
		    // Wait until the server socket is closed.
            cf.channel().closeFuture().sync();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
		
	}

}
