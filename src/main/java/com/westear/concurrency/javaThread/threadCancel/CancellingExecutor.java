package com.westear.concurrency.javaThread.threadCancel;

import java.util.concurrent.*;

/**
 * 扩展了ThreadPoolExecutor，并通过改写newTaskFor使得CancellableTask可以创建自己的Future
 */
public class CancellingExecutor extends ThreadPoolExecutor {


    public CancellingExecutor(int corePoolSize, int maximumPoolSize,
                              long keepAliveTime, TimeUnit unit,
                              BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        if (callable instanceof CancellableTask)
            return ((CancellableTask<T>) callable).newTask();
        else
            return super.newTaskFor(callable);
    }
}
