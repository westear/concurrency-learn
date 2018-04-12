package com.westear.concurrency.example.concurrencyHashMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * 两个线程操作ConcurrentHashMap，意图将value变为10。但是，因为多个线程用相同的key调用时，很可能会覆盖相互的结果，造成记录的次数比实际出现的次数少
 * @author SAGE
 *
 */
public class ConcurrencyHashMapExample1 {

	public static void main(String[] args){
		final Map<String, Integer> count = new ConcurrentHashMap<>();  
	    final CountDownLatch endLatch = new CountDownLatch(2);  
	    Runnable task = new Runnable() {  
	        @Override  
	        public void run() {  
	            for (int i = 0; i < 5; i++) {  
	                Integer value = count.get("a");  
	                if (null == value) {  
	                    count.put("a", 1);  
	                } else {  
	                    count.put("a", value + 1);  
	                }  
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
