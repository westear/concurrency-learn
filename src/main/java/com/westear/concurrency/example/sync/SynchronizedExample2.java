package com.westear.concurrency.example.sync;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.westear.concurrency.annoations.ThreadSafe;

import lombok.extern.slf4j.Slf4j;

@ThreadSafe
@Slf4j
public class SynchronizedExample2 {
	
	// 修饰一个类,作用于所有对象
    public static void test1(int j) {
        synchronized (SynchronizedExample2.class) {
            for (int i = 0; i < 10; i++) {
                log.info("test1 {} - {}", j, i);
            }
        }
    }

    // 修饰一个静态方法，作用于所有对象
    public static synchronized void test2(int j) {
        for (int i = 0; i < 10; i++) {
            log.info("test2 {} - {}", j, i);
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {
        	SynchronizedExample2.test1(1);
        });
        executorService.execute(() -> {
        	SynchronizedExample2.test1(2);
        });
    }
}
