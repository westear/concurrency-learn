package demo.myPractice;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一个固定长度的生产者消费者容器
 */
public class FixContainer<T> {

    private Object[] array;
    private int count;
    private int maxSize;
    private ReentrantLock lock;
    private Condition producerCondition;
    private Condition customerCondition;

    public FixContainer(int maxSize) {
        this.maxSize = maxSize;
        this.count = 0;
        array = new Object[maxSize];
        lock = new ReentrantLock();
        producerCondition = lock.newCondition();
        customerCondition = lock.newCondition();
    }

    public void put(T element) {
        lock.lock();
        try {
            //消费者队列已满
            while (count == maxSize-1) {
                //消费者线程阻塞挂起
                producerCondition.await();
            }
            //添加元素
            array[count] = element;
            count++;
            //唤醒消费者线程
            customerCondition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public Object pop() {
        Object element = null;
        lock.lock();
        try {
            while (count == 0) {
                customerCondition.await();
            }
            element = array[count];
            count--;
            producerCondition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return element;
    }



    public static void main(String[] args) {
        FixContainer<String> fixContainer = new FixContainer<>(10);

        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    System.out.println(Thread.currentThread().getName() + " : " + fixContainer.pop());
                }
            }, "thread-customer-"+i).start();
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                for (int j = 0; j < 25; j++) {
                    fixContainer.put(Thread.currentThread().getName());
                }
            }, "thread-producer-"+i).start();
        }
    }
}
