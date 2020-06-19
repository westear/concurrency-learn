package com.westear.concurrency.blockingQueueOrDeque;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

/**
 * LinkedTransferQueue : 一个由链表结构组成的无界队列
 * 和普通的queue的方法差不多，多了一个transfer方法和tryTransfer方法。
 * 如果你用这种队列的话，往往是消费者先启动，生产者生产一个东西的时候，他先是去找消费者，
 * 如果有消费者就直接丢给消费者。
 */
public class LinkedTransferQueueDemo {

    public static void main(String[] args) throws InterruptedException {

        LinkedTransferQueue<String> strings = new LinkedTransferQueue<>();

        new Thread(()->{
            try {
                System.out.println("t1 take "+strings.take());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(()->{
            try {
                System.out.println("t2 take"+strings.take());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        TimeUnit.SECONDS.sleep(2);

        strings.transfer("transfer aaa");
//		strings.put("aaa");
        System.out.println(strings.size());
//		new Thread(()->{
//			try {
//				System.out.println(strings.take());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}).start();
    }
}
