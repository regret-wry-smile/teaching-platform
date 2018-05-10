package com.ejet.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.internal.StringUtil;
//
//public class LiveEncoder extends MessageToByteEncoder<LiveBaseMsg>{
//
//	
//	@Override
//    protected void encode(ChannelHandlerContext channelHandlerContext, LiveBaseMsg liveMessage, ByteBuf byteBuf) throws Exception {
//        byteBuf.writeByte(liveMessage.getType());
//        byteBuf.writeInt(liveMessage.getLength());
//        if (!StringUtil.isNullOrEmpty(liveMessage.getContent())) {
//            byteBuf.writeBytes(liveMessage.getContent().getBytes());
//        }
//    }
//	
////	@Override
////	protected void encode(ChannelHandlerContext ctx, LiveMessage msg, ByteBuf out) throws Exception {
////		// TODO Auto-generated method stub
////		int length = 10;
////		ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(length);
////		try {
////			byte[] context = new byte[length];
////			buffer.writeBytes(context);
////			out.writeBytes(context);
////			
////		} finally {
////			//自己申请的内存池缓冲区，必须释放，否则内存泄漏，out为发送的缓冲区，会自动释放
////			buffer.release();
////		}
////		
////	}
//
//}
