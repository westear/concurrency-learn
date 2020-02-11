package demo;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 固定容量队列生产消费者
 * @author Qinyunchan
 * @date Created in 11:21 AM 2019/3/6
 * @Modified By
 */

public class MyFixedContainer<T> {

    private LinkedList<T> lists = new LinkedList<>();
    private int MAX = 10;
    private int count = 0;

    public synchronized void put(T t){
        while (lists.size() == MAX){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lists.addLast(t);
        ++count;
        this.notifyAll();
    }

    public synchronized T get(){
        while (lists.size() == 0){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        T t = lists.removeFirst();
        --count;
        this.notifyAll();
        return t;
    }

    public static void main(String[] args) {
        MyFixedContainer<String> container = new MyFixedContainer<>();
        //启动消费者线程
        for(int i = 0; i < 10; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 5; j++){
                        System.out.println(container.get());
                    }
                }
            },"c-"+i).start();
        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //启动生产者线程
        for(int i=0; i<2; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int j = 0; j < 25; j++){
                        container.put(Thread.currentThread().getName()+" ");
                    }
                }
            },"p-"+i).start();
        }
    }
}
