package com.westear.concurrency.example.atomic;

import java.util.concurrent.atomic.AtomicStampedReference;

import com.westear.concurrency.annoations.ThreadSafe;

import lombok.extern.slf4j.Slf4j;

/**
 * 什么时候使用CountDownLatch : http://www.importnew.com/15731.html
 * Atomic 保证线程安全 :AtomicStampedReference 解决ABA 问题的类
 * @author SAGE
 *
 */

@ThreadSafe
@Slf4j
public class AtomicCountExample6 {

	private static AtomicStampedReference<Integer> money = new AtomicStampedReference<Integer>(9, 0);
	
	public static void main(String[] args){
		//充值
		for(int i = 0; i < 3; i++){
			final int timestamp = money.getStamp();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true){
						Integer m = money.getReference();
						if(m < 20){
							if(money.compareAndSet(m, m+20, timestamp, timestamp+1)){
								try {
									System.out.println("余额小于20元，充值成功，余额:"+money.getReference()+"元");
									Thread.sleep(500);
									break;
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}else{
							break;
						}
					}
					
				}
			}).start();
		}
		
		//消费
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					int timestamp = money.getStamp();
					Integer m = money.getReference();
					if(m > 10){
						System.out.println("大于10元");
						if(money.compareAndSet(m, m-10, timestamp, timestamp)){
							System.out.println("成功消费10元，余额:"+money.getReference());
//							break;
						}else{
							System.out.println("没有足够的金额");  
							break; 
						}
					}
				}
				try{
					Thread.sleep(100);
				}catch (InterruptedException e) {
					log.error(e.getMessage(),e);
				}
			}
		}).start();
	}
}
