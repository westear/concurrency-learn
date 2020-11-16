package com.westear.concurrency.javaThread.threadCancel;

import com.westear.concurrency.annoations.ThreadSafe;

import java.util.concurrent.*;

/**
 * 扩展了Callable,并增加了一个cancel 方法和一个newTask工厂方法来构造RunnableFuture
 * 通过newTaskFor将非标准的取消操作封装在一个任务中
 */
public interface CancellableTask<T> extends Callable<T> {

    void cancel();

    /**
     * 通过newTaskFor将非标准的取消操作封装在一个任务中
     * @return RunnableFuture
     */
    RunnableFuture<T> newTask();

}
