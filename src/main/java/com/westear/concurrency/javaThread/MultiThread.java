package com.westear.concurrency.javaThread;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * 一个Java程序从main()方法开始执行，然后按照既定的代码逻辑执行，看似没有其他线程参与，但实际上Java程序天数就是多线程程序，
 * 因为执行main()方法是一个名为main的线程。下面使用JMX来查看一个普通的Java程序包含哪些线程。
 * 运行结果：
 *      [1]main                     //main线程，用户程序入口
 *      [2]Reference Handler        //清除Reference的线程
 *      [3]Finalizer                //调用对象finalize方法的线程
 *      [4]Signal Dispatcher        //分发处理发送给JVM信号的线程
 *      [5]Attach Listener
 *      [6]Monitor Ctrl-Break
 *
 */
public class MultiThread {
    public static void main(String[] args) {
        //获取Java线程管理MXBean
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        //不需要获取同步的 monitor 和 synchronized 信息，仅获取线程和线程堆栈信息
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        //遍历线程信息，仅打印线程ID和线程名称信息
        for (int i = threadInfos.length - 1; i >= 0; i--){
            ThreadInfo threadInfo = threadInfos[i];
            System.out.println("[" + threadInfo.getThreadId() + "]" + threadInfo.getThreadName());
        }
    }
}
