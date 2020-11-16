package com.westear.concurrency.javaOriginUtils.countDownLatch;

/**
 * 需求描述：
 * 解析一个Excel ，里面多个Sheet， 每个线程解析一个Sheet, 所有Sheet解析完成后执行主线程的代码,
 * 最简单的做法是使用Thread.join()
 * 也可以使用CountDownLatch类
 *
 * join 用于让当前执行线程等待join线程执行结束。其实原理是不停检测join线程是否存活，如果join线程存活则让当前线程永远等待
 */
public class JoinCountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        Thread parser1 = new Thread(() -> {

        });

        Thread parser2 = new Thread(()->{
            System.out.println("parser2 finish");
        });

        parser1.start();
        parser2.start();
        parser1.join();
        parser2.join();
        System.out.println("all parser finish");
    }
}
