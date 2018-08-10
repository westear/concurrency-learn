package demo.ProducerAndConsumer.v3;

import java.util.List;

/**
 * @author Qinyunchan
 * @date Created in 下午5:52 2018/8/10
 * @Modified By
 */

public class Consumer implements Runnable{
        private List<PCData> queue;
    public Consumer(List<PCData> queue){
            this.queue = queue;
        }
        @Override
        public void run() {
            try {
                while (true) {
                    if (Thread.currentThread().isInterrupted()) {
                        break;
                    }
                    PCData data = null;
                    Main.lock.lock();
                    if (queue.size() == 0){
                        //唤醒所有等待线程
                        Main.full.signalAll();
                        //造成当前线程在接到信号或被中断之前一直处于等待状态
                        Main.empty.await();
                    }
                    Thread.sleep(1000);
                    data = queue.remove(0);
                    Main.lock.unlock();
                    System.out.println("消费者ID:"+Thread.currentThread().getId()+" 消费了:"+data.getData()+" result:"+(data.getData()*data.getData()));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
