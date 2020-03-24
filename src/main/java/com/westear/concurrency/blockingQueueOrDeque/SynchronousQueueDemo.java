package com.westear.concurrency.blockingQueueOrDeque;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * SynchronousQueue 不存储元素的阻塞队列, 每个put操作必须等待一个take操作，否则不能继续添加元素。支持公平访问队列
 * 该队列吞吐量高于LinkedBlockingQueue和ArrayBlockingQueue
 *
 * 同步队列是容量为0，也就是来的东西必须给消费掉.
 * 首先启动一个消费者，调用add方法，他报错了
 * 只能调用put，意思就是阻塞等待消费者消费。put里面其实用的是transfer，任何东西必须消费，不能往容器里面扔。
 */
public class SynchronousQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> str = new SynchronousQueue<>();

        new Thread(()->{
            try {
                System.out.println(str.take());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
//		str.add("aaa"); //add 会报错，因为SynchronousQueue size=0
        str.put("aaa"); //put 会线程阻塞
        str.put("aaa");
        str.put("aaa");
        str.put("aaa");
        str.put("aaa");
        System.out.println(str.size());
    }
}
