package com.westear.concurrency.javaOriginUtils.Exchanger;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Exchanger 是一个用于线程间协作的工具类。用于进行线程间的数据交换。
 * 它提供一个同步点，在这个同步点，两个线程可以交换彼此的数据。这两个线程通过exchange() 方法交换数据，如果第一个线程先执行 exchange方法，
 * 它会一直等待第二个线程也执行exchange方法，当两个线程都达到同步点时，这两个线程就可以交换数据，将本线程生产的数据传递给对方。
 * 如果两个线程中有一个没有执行exchange() 方法，则会一直等待，如果担心有特殊情况发生，避免一直等待，可以使用带超时时间的exchange方法
 *
 * 应用场景：
 *      遗传算法：选出两个元素作为交配对象，交换两方数据，并使用交叉规则得出交配结果
 *      用于校对工作，比如需要将纸质银行流水通过人工方式录入成电子银行流水，为避免错误，使用A/B两个人进行录入，对两个excel进行校对，看是否录入一致
 *
 */
public class ExchangerTest {

    private static final Exchanger<String> exchanger = new Exchanger<>();

    private static ExecutorService threadPool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        threadPool.execute(() ->{
            try {
                //A录入银行流水数据
                String A = "银行流水A";
                exchanger.exchange(A);
                System.out.println("当前线程:" + Thread.currentThread().getName() + "；放入: " + A);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        threadPool.execute(() -> {
            try {
                //B录入银行流水数据
                String B = "银行流水B";
                //返回另一个线程提供的值, 证明已经完成交换
                String A = exchanger.exchange(B);
                System.out.println("当前线程:" + Thread.currentThread().getName() + "；放入: " + B);
                System.out.println("A和B数据是否一致: " + A.equals(B) + ",A录入的是: " + A + ",B录入是：" + B);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        threadPool.shutdown();
    }
}
