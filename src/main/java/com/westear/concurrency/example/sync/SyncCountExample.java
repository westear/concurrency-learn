package com.westear.concurrency.example.sync;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import com.westear.concurrency.annoations.ThreadSafe;

import lombok.extern.slf4j.Slf4j;

/**
 * 什么时候使用CountDownLatch : http://www.importnew.com/15731.html
 * @author SAGE
 *
 */

@ThreadSafe
@Slf4j
public class SyncCountExample {

	//请求总数
	public static int clientTotal = 5000;
	//并发线程数
	public static int threadTotal = 200;
	
	public static int count = 0;
	
	public static void main(String[] args) throws InterruptedException{
		ExecutorService exec = Executors.newCachedThreadPool();
		//创建允许同时运行的线程数目的信号量。允许同时运行的线程数目=200
		//200个线程同时运行的时候，第201个线程必须等待前面有一个要完成，才能执行第201个线程启动。
		final Semaphore semaphore = new Semaphore(threadTotal);
		//闭锁需要等待的线程数量=5000
		final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
		
		for(int i = 0; i < clientTotal; i++){
			exec.execute(new Runnable() {
				@Override
				public void run() {
					try{
						//从信号量中获取一个允许机会
						semaphore.acquire();
						add();
						//释放允许，将占有的信号量归还
						semaphore.release();
					}catch (Exception e) {
						log.error(e.getMessage(),e);
					}
					//每个线程执行完后计数减一，直到线程计数为0
					countDownLatch.countDown();
				}
			});
		}
		//200个线程一共执行5000次，所有的线程已经完成了任务，闭锁上等待的线程就可以恢复执行任务
		countDownLatch.await();
		exec.shutdown();
		log.info("count: {}",count);
	}
	
	//修饰一个静态方法，作用对象是所有对象，能够保证其原子性
	public synchronized static void add(){
		count++;
	}
}
