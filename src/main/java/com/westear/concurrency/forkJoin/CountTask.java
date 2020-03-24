package com.westear.concurrency.forkJoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * 使用Fork/Join 框架执行计算任务
 * RecursiveTask： 用于有返回结果的任务
 * RecursiveAction: 用于没有返回结果的任务
 * ForkJoinPool: ForkJoinTask 需要通过 ForkJoinPool 来执行
 *
 * 任务分割出的子任务会添加到当前工作线程所维护的双端队列中，进入队列的头部。
 * 当一个工作线程的队列里暂时没有任务时，它会随机从其他工作线程的队列的尾部获取一个任务
 */
public class CountTask extends RecursiveTask<Integer> {

    private static final int THRESHOLD = 2;
    private int start;
    private int end;

    public CountTask(int start, int end){
        this.start = start;
        this.end = end;
    }

    /**
     * 每个子任务在调用fork方法时，又会进入computer方法
     * 使用join方法会等待子任务执行完并得到其结果
     */
    @Override
    protected Integer compute() {
        int sum = 0;
        //如果任务足够小就直接计算任务
        boolean canCompute = (end - start) <= THRESHOLD;
        if(canCompute) {
            for (int i = start; i <= end; i++) {
                sum += i;
            }
        }else {
            //如果任务大于阈值，就分裂成两个子任务计算
            int middle = (start + end) / 2;
            CountTask leftTask = new CountTask(start, middle);
            CountTask rightTask = new CountTask(middle+1, end);
            //执行子任务
            leftTask.fork();
            rightTask.fork();
            //等待子任务执行完，并得到结果
            int leftResult = leftTask.join();
            int rightResult = rightTask.join();
            //合并子任务
            sum = leftResult + rightResult;
        }
        return sum;
    }

    public static void main(String[] args) {
        //创建一个 ForkJoinPool
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        //生成一个计算任务，负责计算1+2+3+4
        CountTask task = new CountTask(1,4);
        //执行一个任务
        Future<Integer> result = forkJoinPool.submit(task);
        try {
            System.out.println(result.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
