package com.westear.concurrency.example.collection;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import com.westear.concurrency.annoations.ThreadSafe;

import lombok.extern.slf4j.Slf4j;

/**
 * 同步且有序的 put
 * 生成跳跃链表形式的数据结构（有序，多层索引）
 *
 *  *  Head nodes          Index nodes
 *  *  +-+    right        +-+                      +-+
 *  *  |2|---------------->| |--------------------->| |->null
 *  *  +-+                 +-+                      +-+
 *  *   | down              |                        |
 *  *   v                   v                        v
 *  *  +-+            +-+  +-+       +-+            +-+       +-+
 *  *  |1|----------->| |->| |------>| |----------->| |------>| |->null
 *  *  +-+            +-+  +-+       +-+            +-+       +-+
 *  *   v              |    |         |              |         |
 *  *  Nodes  next     v    v         v              v         v
 *  *  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+
 *  *  | |->|A|->|B|->|C|->|D|->|E|->|F|->|G|->|H|->|I|->|J|->|K|->null
 *  *  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+
 *  *
 */
@Slf4j
@ThreadSafe
public class ConcurrentSkipListMapExample {

	// 请求总数
    public static int clientTotal = 5000;

    // 同时并发执行的线程数
    public static int threadTotal = 200;

    private static Map<Integer, Integer> map = new ConcurrentSkipListMap<>();

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            final int count = i;
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    update(count);
                    semaphore.release();
                } catch (Exception e) {
                    log.error("exception", e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        log.info("size:{}", map.size());
    }

    private static void update(int i) {
        map.put(i, i);
    }
}
