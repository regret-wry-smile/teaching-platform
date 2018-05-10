package com.ejet.netty.message;

/**
 * 答题响应消息
 * 
 * @author ShenYijie
 *
 */
public class LiveAnswerMsg extends LiveBaseMsg {
	
	private Object content;

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
	
	

}
