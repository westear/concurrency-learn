package demo.ProducerAndConsumer.v1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Qinyunchan
 * @date Created in 下午5:19 2018/8/10
 * @Modified By
 */

public class Main {
    public static void main(String[] args) throws InterruptedException {
        //一个基于已链接节点的、任选范围的阻塞双端队列
        BlockingQueue<PCdata> queue = new LinkedBlockingDeque<>(10);
        //生产者
        Producer p1 = new Producer(queue);
        Producer p2 = new Producer(queue);
        Producer p3 = new Producer(queue);
        //消费者
        Consumer c1 = new Consumer(queue);
        Consumer c2 = new Consumer(queue);
        Consumer c3 = new Consumer(queue);

        ExecutorService service = Executors.newCachedThreadPool();
        //生产者线程
        service.execute(p1);
        service.execute(p2);
        service.execute(p3);
        //消费者线程
        service.execute(c1);
        service.execute(c2);
        service.execute(c3);
        //休眠10秒
        Thread.sleep(10*1000);
        //生产者线程停止
        p1.stop();
        p2.stop();
        p3.stop();
        //休眠3秒
        Thread.sleep(3000);
        //线程池关闭
        service.shutdown();
    }
}
