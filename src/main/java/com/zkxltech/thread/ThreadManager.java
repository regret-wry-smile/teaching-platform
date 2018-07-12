package com.zkxltech.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ShenYijie
 * @Description: TODO( 线程管理 )
 * @date 2018年7月11日
 * 
 */
public class ThreadManager {
	private static final Logger logger = LoggerFactory.getLogger(ThreadManager.class);
	private static ThreadManager instance = null;
	private static List<BaseThread> threads = Collections.synchronizedList(new ArrayList<>());
	private ThreadManager() {
		
	}
	public static ThreadManager getInstance() {
		if(instance==null) {
			instance = new ThreadManager();
		}
		return instance;
	}
	
	public synchronized void addThread(BaseThread thread) {
		threads.add(thread);
	}
	
	public synchronized void stopAllThread() {
		//threads.remove(thread);
	    printThead();
	    for (BaseThread baseThread : threads) {
            baseThread.stopThread();
        }
	    threads.clear();
	    logger.info("停止后线程个数 ==>:" +threads.size());
	}
	
	public synchronized void printThead() {
		logger.info("当前线程个数 ==>:" + threads.size());
	}
	
	
}
