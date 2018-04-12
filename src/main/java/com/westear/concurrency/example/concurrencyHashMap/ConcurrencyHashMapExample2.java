package com.westear.concurrency.example.concurrencyHashMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * 两个线程操作ConcurrentHashMap，意图将value变为10。但是，因为多个线程用相同的key调用时，很可能会覆盖相互的结果，造成记录的次数比实际出现的次数少
 * 使用putIfAbsent(),replace()改进
 * @author SAGE
 *
 */
public class ConcurrencyHashMapExample2 {

	public static void main(String[] args){
		final Map<String, Integer> count = new ConcurrentHashMap<>();  
	    final CountDownLatch endLatch = new CountDownLatch(2);  
	    Runnable task = new Runnable() {  
	        @Override  
	        public void run() {  
	            Integer oldValue, newValue;  
	            for (int i = 0; i < 5; i++) {  
	                while (true) {  
	                    oldValue = count.get("a");  
	                    if (null == oldValue) {  
	                        newValue = 1;  
	                        if (count.putIfAbsent("a", newValue) == null) {  //如果key对应的value不存在，则put进去，返回null。否则不put，返回已存在的value。
	                            break;  
	                        }  
	                    } else {  
	                        newValue = oldValue + 1;  
	                        if (count.replace("a", oldValue, newValue)) { //果key对应的当前值是oldValue，则替换为newValue，返回true。否则不替换，返回false。 
	                            break;  
	                        }  
	                    }  
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
