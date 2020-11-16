package com.westear.concurrency.javaOriginUtils.cyclicBarrier;

import java.util.Map;
import java.util.concurrent.*;

/**
 * CyclicBarrier 应用场景：
 * 用于多线程计算数据，最后合并计算结果的场景。如：用一个Excel保存用户所有银行流水，每个Sheet保存一个账户近一年的每笔银行流水，
 * 现在需要统计用户的日均银行流水，先用多线程处理每个sheet里得银行流水，都执行完之后，得到每个sheet的日均银行流水.
 * 最后，再用barrierAction计算这些线程的计算结果，整个excel的日均银行流水
 *
 * 该程序为银行流水处理服务类
 */
public class BankWaterService implements Runnable {

    //创建4个屏障，处理完之后执行当前类的run方法
    private CyclicBarrier cyclicBarrier = new CyclicBarrier(4, this);

    //假设只有4个sheet,所以只启动4个线程
    private Executor executor = Executors.newFixedThreadPool(4);

    //假设每个Sheet计算出的银流结果
    private ConcurrentHashMap<String, Integer> sheetBankWaterCount = new ConcurrentHashMap<>();

    private void count() {
        for (int i = 0; i < 4; i++) {
            //每个线程计算一个sheet的结果
            executor.execute(()->{
                //计算当前sheet的银流数据，计算代码省略
                sheetBankWaterCount.put(Thread.currentThread().getName(), 1);
                //银流计算完成，插入一个屏障,等待其他计算线程完成
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * 所以计算sheet 的线程达到屏障后， 在屏障放开前的运行线程
     */
    @Override
    public void run() {
        int result = 0;
        //汇总每个sheet的计算结果
        for (Map.Entry<String, Integer> sheet : sheetBankWaterCount.entrySet()) {
            result += sheet.getValue();
        }
        //将结果输出
        sheetBankWaterCount.put("result", result);
        System.out.println(result);
    }

    public static void main(String[] args) {
        BankWaterService bankWaterService = new BankWaterService();
        bankWaterService.count();
    }
}
