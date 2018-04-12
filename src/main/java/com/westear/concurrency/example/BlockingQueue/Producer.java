package com.westear.concurrency.example.BlockingQueue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Producer implements Runnable {

	private BlockingQueue<String> queue;
	private static AtomicInteger count = new AtomicInteger();
	private volatile boolean isRunning = true;
    private static final int DEFAULT_RANGE_FOR_SLEEP = 1000;
	
	public Producer(BlockingQueue<String> queue){
		this.queue = queue;
	}
	
	@Override
	public void run() {
		String data = null;
        Random r = new Random();
 
        log.info("启动生产者线程！");
        
        while(isRunning){
        	log.info("正在生产数据...");
            try {
				Thread.sleep(r.nextInt(DEFAULT_RANGE_FOR_SLEEP));
				data = "data:" + count.incrementAndGet();
				log.info("将数据：{} 放入队列...",data);
                if (!queue.offer(data, 2, TimeUnit.SECONDS)) {
                	log.info("放入数据失败：{} ",data);
                }
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}finally {
				log.info("退出生产者线程！");
	        }
        }
        
	}

	public void stop() {
        isRunning = false;
    }
}
