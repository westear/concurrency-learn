package com.westear.concurrency.javaOriginUtils.Exchanger;

import java.util.concurrent.Exchanger;

/**
 * 此程序创建了两个线程，线程在执行过程中，调用同一个exchanger对象的exchange方法，进行信息通信，
 * 当两个线程均已将信息放入到exchanger对象中时，exchanger对象会将两个线程放入的信息交换，然后返回
 *
 * 另外需要注意的是，Exchanger类仅可用作两个线程的信息交换，当超过两个线程调用同一个exchanger对象时，得到的结果是随机的，
 * exchanger对象仅关心其包含的两个“格子”是否已被填充数据，当两个格子都填充数据完成时，该对象就认为线程之间已经配对成功，然后开始执行数据交换操作。
 */
public class ExchangerTest1 extends Thread {

    private Exchanger<String> exchanger;
    private String string;
    private String threadName;

    public ExchangerTest1(Exchanger<String> exchanger, String string,
                         String threadName) {
        this.exchanger = exchanger;
        this.string = string;
        this.threadName = threadName;
    }

    public void run() {
        try {
            System.out.println(threadName + ": " + exchanger.exchange(string));
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();
        ExchangerTest1 test1 = new ExchangerTest1(exchanger, "string1", "thread-1");
        ExchangerTest1 test2 = new ExchangerTest1(exchanger, "string2", "thread-2");

        test1.start();
        test2.start();

    }

}
