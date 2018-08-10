package demo.ProducerAndConsumer.v1;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * @author Qinyunchan
 * @date Created in 下午5:10 2018/8/10
 * @Modified By
 */

public class Consumer implements Runnable{
    private BlockingQueue<PCdata> queue;

    private static final int SLEEPTIME = 1000;

    public Consumer(BlockingQueue<PCdata> queue){
        this.queue = queue;
    }

    @Override
    public void run() {
        System.out.println("start Consumer id :" + Thread.currentThread().getId());
        Random r = new Random();
        try{
            while (true){
                //获取并移除此队列的头部，在元素变得可用之前一直等待（如果有必要）
                PCdata data = queue.take();
                if(data != null){
                    int re = data.getData() * data.getData();
                    System.out.println(MessageFormat.format("消费: {0}*{1}={2}",data.getData(), data.getData(), re));
                    Thread.sleep(r.nextInt(SLEEPTIME));
                }
            }
        }catch (InterruptedException e){
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
