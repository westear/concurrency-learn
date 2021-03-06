package com.westear.concurrency.example.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

import com.westear.concurrency.annoations.ThreadSafe;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 什么时候使用CountDownLatch : http://www.importnew.com/15731.html
 * Atomic 保证线程安全 :AtomicIntegerFieldUpdater
 * @author SAGE
 *
 */

@ThreadSafe
@Slf4j
public class AtomicIntegerFieldUpdaterDemo {

	private static AtomicIntegerFieldUpdater<AtomicIntegerFieldUpdaterDemo> updater =  AtomicIntegerFieldUpdater.newUpdater(AtomicIntegerFieldUpdaterDemo.class,"count");
	
	@Getter
	public volatile int count = 100;
	
	public static void main(String[] args){
		AtomicIntegerFieldUpdaterDemo example5 = new AtomicIntegerFieldUpdaterDemo();
		if(updater.compareAndSet(example5, 100, 120)){
			log.info("update success 1: {}", example5.getCount());
		}
		
		if(updater.compareAndSet(example5, 100, 120)){
			log.info("update success 2: {}", example5.getCount());
		}else{
			log.info("update fail: {}", example5.getCount());
		}
	}
}
