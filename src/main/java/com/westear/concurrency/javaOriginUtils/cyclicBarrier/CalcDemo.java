package com.westear.concurrency.javaOriginUtils.cyclicBarrier;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 模拟多线程分组计算
 * 有一个大小为50000的随机数组，用5个线程分别计算10000个元素的和
 * 然后在将计算结果进行合并，得出最后的结果。
 * 重点分析：
 *
 * 用5个线程分别计算：定义一个大小为5的线程池。
 * 计算结果进行合并：定义一个屏障线程，将上面5个线程计算的子结果信息合并。
 */
public class CalcDemo {

    public static void main(String[] args) {
        //数组大小
        int size = 50000;
        //定义数组
        int[] numbers = new int[size];
        //随机初始化数组
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            numbers[i] = random.nextInt(100);
        }

        //单线程计算结果
        long sum = 0L;
        for (int i = 0; i < size; i++) {
            sum += numbers[i];
        }
        System.out.println("单线程计算结果：" + sum);


        //多线程计算结果
        //定义线程池
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        //定义五个Future去保存子数组计算结果
        final int[] results = new int[5];

        //定义一个循环屏障，在屏障线程中进行计算结果合并
        CyclicBarrier barrier = new CyclicBarrier(5, () -> {
            int sums = 0;
            for (int i = 0; i < 5; i++) {
                sums += results[i];
            }
            System.out.println("多线程计算结果：" + sums);
        });

        //子数组长度
        int length = 10000;
        //定义五个线程去计算
        for (int i = 0; i < 5; i++) {
            //定义子数组
            int[] subNumbers = Arrays.copyOfRange(numbers, (i * length), ((i + 1) * length));
            //盛放计算结果
            int finalI = i;
            executorService.submit(() -> {
                for (int subNumber : subNumbers) {
                    results[finalI] += subNumber;
                }
                //等待其他线程进行计算
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }

        //关闭线程池
        executorService.shutdown();
    }
}
