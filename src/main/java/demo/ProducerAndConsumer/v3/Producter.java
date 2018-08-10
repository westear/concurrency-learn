package demo.ProducerAndConsumer.v3;

import java.util.List;
import java.util.Random;

/**
 * @author Qinyunchan
 * @date Created in 下午5:53 2018/8/10
 * @Modified By
 */

public class Producter implements Runnable{
    private List<PCData> queue;
    private int len;
    public Producter(List<PCData> queue,int len){
        this.queue = queue;
        this.len = len;
    }
    @Override
    public void run() {
        try{
            while(true){
                if(Thread.currentThread().isInterrupted()) {
                    break;
                }
                Random r = new Random();
                PCData data = new PCData();
                data.setData(r.nextInt(500));
                Main.lock.lock();
                if(queue.size() >= len)
                {
                    //唤醒所有等待线程
                    Main.empty.signalAll();
                    //造成当前线程在接到信号或被中断之前一直处于等待状态
                    Main.full.await();
                }
                Thread.sleep(1000);
                queue.add(data);
                Main.lock.unlock();
                System.out.println("生产者ID:"+Thread.currentThread().getId()+" 生产了:"+data.getData());
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
