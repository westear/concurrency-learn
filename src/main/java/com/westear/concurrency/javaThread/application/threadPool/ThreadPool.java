package com.westear.concurrency.javaThread.application.threadPool;

public interface ThreadPool<Job extends Runnable> {
    /**
     * 执行一个Job, 这个Job需要实现Runnable
     * @param job extends Runnable
     */
    void execute(Job job);

    /**
     * 关闭线程池
     */
    void shutdown();

    /**
     * 增加工作者线程
     * @param num 工作者线程数
     */
    void addWorkers(int num);

    /**
     * 减少工作者线程
     * @param num 工作者线程数
     */
    void removeWorker(int num);

    /**
     * 得到正在等待执行的任务数量
     * @return int
     */
    int getWaitingJobSize();
}
