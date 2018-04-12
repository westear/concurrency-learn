package com.westear.concurrency.example.concurrencyHashMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 由于ConcurrentMap中不能保存value为null的值，所以需要处理不存在和已存在两种情况，不过可以使用AtomicInteger来替代
 * @author SAGE
 *
 */
public class ConcurrencyHashMapExample3 {

	public static void main(String[] args){
		final Map<String, AtomicInteger> count = new ConcurrentHashMap<>();  
	    final CountDownLatch endLatch = new CountDownLatch(2);  
	    Runnable task = new Runnable() {
	        @Override  
	        public void run() {  
	        	AtomicInteger oldValue;
	        	for (int i = 0; i < 5; i++) {
	                oldValue = count.get("a");
	                if (null == oldValue) {
	                    AtomicInteger zeroValue = new AtomicInteger(0);
	                    oldValue = count.putIfAbsent("a", zeroValue);
	                    if (null == oldValue) {
	                        oldValue = zeroValue;  
	                    }
	                }
	                oldValue.incrementAndGet();
	            }
	            endLatch.countDown(); 
	        }
	    };  
	    new Thread(task).start();  
	    new Thread(task).start();  
	  
	    try {  
	        endLatch.await();  
	        System.out.println(count);  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	}  
}
