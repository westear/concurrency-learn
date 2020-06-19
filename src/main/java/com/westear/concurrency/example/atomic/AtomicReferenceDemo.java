package com.westear.concurrency.example.atomic;

import java.util.concurrent.atomic.AtomicReference;

import com.westear.concurrency.annoations.ThreadSafe;

import lombok.extern.slf4j.Slf4j;

/**
 * 什么时候使用CountDownLatch : http://www.importnew.com/15731.html
 * Atomic 保证线程安全
 * @author SAGE
 *
 */

@ThreadSafe
@Slf4j
public class AtomicReferenceDemo {

	private static AtomicReference<Integer> count = new AtomicReference<Integer>(0);
	
	public static void main(String[] args){
		count.compareAndSet(0, 2);	//if==0; value==2
		count.compareAndSet(0, 1);	//不执行
		count.compareAndSet(1, 3);	//不执行
		count.compareAndSet(2, 4);	//if==2; value==4
		count.compareAndSet(3, 5);	//不执行
		log.info("count: {}",count.get());
	}
}
