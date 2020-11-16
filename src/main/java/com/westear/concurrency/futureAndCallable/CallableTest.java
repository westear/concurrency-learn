package com.westear.concurrency.futureAndCallable;

import java.util.concurrent.*;

/**
 * 最大的好处就是能够返回结果，加入有这样的场景，
 * 我们现在需要计算一个数据，而这个数据的计算比较耗时，而我们后面的程序也要用到这个数据结果，那么这个时Callable岂不是最好的选择？
 * 我们可以开设一个线程去执行计算，而主线程继续做其他事，而后面需要使用到这个数据时，我们再使用Future获取
 */
public class CallableTest {

    public static void main(String[] args) {
        //创建线程池
        ExecutorService executor = Executors.newCachedThreadPool();
        //创建Callable对象任务
        Task task = new Task();
        //提交任务并获取执行结果
        Future<Integer> result = executor.submit(task);
        //关闭线程池
        executor.shutdown();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        System.out.println("主线程在执行任务");

        try {
            if(result.get()!=null){
                System.out.println("task运行结果"+result.get());
            }else{
                System.out.println("未获取到结果");
            }
        } catch (InterruptedException e) {
            System.err.println("响应中断");
        } catch (ExecutionException e) {
            System.err.println("执行出错");
        }

        System.out.println("所有任务执行完毕");
    }

    private static class Task implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            System.out.println("子线程在进行计算");
            Thread.sleep(3000);
            int sum = 0;
            for(int i=0;i<100;i++)
                sum += i;
            return sum;
        }
    }
}
