package demo.ProducerAndConsumer.v1;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Qinyunchan
 * @date Created in 下午4:53 2018/8/10
 * @Modified By
 */

public class Producer implements Runnable {
    private volatile boolean isRunning = true;
    /**
     * 内存缓冲区
     */
    private BlockingQueue<PCdata> queue;
    /**
     * 总数 原子操作
     */
    private static AtomicInteger count = new AtomicInteger();
    private static final int SLEEPTIME = 1000;

    public Producer(BlockingQueue<PCdata> queue){
        this.queue = queue;
    }

    @Override
    public void run() {
        PCdata data = null;
        Random r = new Random();
        System.out.println("start producting id:" + Thread.currentThread().getId());
        try {
            while (isRunning){
                Thread.sleep(r.nextInt(SLEEPTIME));
                data = new PCdata(count.incrementAndGet());
                System.out.println(data + "加入队列");
                //等待2秒后，超时
                if(!queue.offer(data, 2, TimeUnit.SECONDS)){
                    System.err.println("加入队列失败");
                }
            }
        }catch (InterruptedException e){
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public void stop(){
        isRunning = false;
    }
}
