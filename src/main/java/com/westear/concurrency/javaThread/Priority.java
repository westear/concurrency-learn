package com.westear.concurrency.javaThread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 线程优先级的测试
 * 线程构建的时候可以通过setPriority(int args); 方法修改优先级，默认优先级是5, 优先级高的线程分配时间片的数量要多于优先级低的线程。
 * 从输出可以看出，优先级10的cpu时间片时间还是大于优先级1的线程。
 * 但并非所有的操作系统都会认可线程优先级，其他操作系统和cpu也可能会忽略优先级的设置，所以程序的正确性不能依赖线程的优先级高低。
 */
public class Priority {
    private static volatile boolean notStart = true;
    private static volatile boolean notEnd = true;

    public static void main(String[] args) throws Exception {
        List<Job> jobs = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            int priority = i < 5 ? Thread.MIN_PRIORITY : Thread.MAX_PRIORITY;
            Job job = new Job(priority);
            jobs.add(job);
            Thread thread = new Thread(job, "Thread:" + i);
            thread.setPriority(priority);
            thread.start();
        }

        notStart = false;
        TimeUnit.SECONDS.sleep(5);
        notEnd = false;

        for (Job job : jobs) {
            System.out.println("Job Priority : " + job.priority +", Count : " + job.jobCount);
        }
    }

    static class Job implements Runnable {
        private int priority;
        private long jobCount;

        public Job(int priority) {
            this.priority = priority;
        }

        @Override
        public void run() {
            while (notStart){
                //让掉当前线程 CPU 的时间片,正在运行中的线程重新变成就绪状态,并重新竞争 CPU 的调度权。它可能会获取到，也有可能被其他线程获取到
                Thread.yield();
            }
            while (notEnd){
                //让掉当前线程 CPU 的时间片,正在运行中的线程重新变成就绪状态,并重新竞争 CPU 的调度权。它可能会获取到，也有可能被其他线程获取到
                Thread.yield();
                //notEnd != false ，在让掉当前线程 CPU 的时间片之后，jobCount + 1
                jobCount++;
            }
        }

    }
}
