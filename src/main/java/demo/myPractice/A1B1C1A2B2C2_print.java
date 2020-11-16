package demo.myPractice;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class A1B1C1A2B2C2_print {

    private volatile static int index = 1;
    private volatile static int THREAD_TYPE = 1;

    private final static int TYPE_A = 1;
    private final static int TYPE_B = 2;
    private final static int TYPE_C = 3;

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition conditionA = lock.newCondition();
        Condition conditionB = lock.newCondition();
        Condition conditionC = lock.newCondition();

        Runnable runnableA = () -> {
            lock.lock();
            String name = Thread.currentThread().getName();
            System.out.println(name + " 获得锁 ");
            String type = name.substring(0, 1);
            int order = Integer.parseInt(name.substring(name.length()-1));
            while (!name.contains(type) || THREAD_TYPE != TYPE_A || index != order) {
                try {
                    System.out.println(name + " 阻塞");
                    conditionA.await();
                }catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            try {
                THREAD_TYPE = TYPE_B;
                System.out.println(name);
                System.out.println("当前线程：" + name + " THREAD_TYPE 改为 " + THREAD_TYPE);
                TimeUnit.SECONDS.sleep(1);
                conditionB.signalAll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }finally {
                System.out.println(name + " 释放锁 ");
                lock.unlock();
            }
        };

        Runnable runnableB = () -> {
            lock.lock();
            String name = Thread.currentThread().getName();
            System.out.println(name + " 获得锁 ");
            String type = name.substring(0, 1);
            int order = Integer.parseInt(name.substring(name.length()-1));
            while (!name.contains(type) || THREAD_TYPE != TYPE_B || index != order) {
                try {
                    System.out.println(name + " 阻塞");
                    conditionB.await();
                }catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            try {
                THREAD_TYPE = TYPE_C;
                System.out.println(name);
                System.out.println("当前线程：" + name + " THREAD_TYPE 改为 " + THREAD_TYPE);
                TimeUnit.SECONDS.sleep(1);
                conditionC.signalAll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }finally {
                System.out.println(name + " 释放锁 ");
                lock.unlock();
            }
        };

        Runnable runnableC = () -> {
            lock.lock();
            String name = Thread.currentThread().getName();
            System.out.println(name + " 获得锁 ");
            String type = name.substring(0, 1);
            int order = Integer.parseInt(name.substring(name.length()-1));
            while (!name.contains(type) || THREAD_TYPE != TYPE_C || index != order) {
                try {
                    System.out.println(name + " 阻塞");
                    conditionC.await();
                }catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            try {
                THREAD_TYPE = TYPE_A;
                index++;
                System.out.println(name);
                System.out.println("当前线程：" + name + " THREAD_TYPE 改为 " + THREAD_TYPE);
                TimeUnit.SECONDS.sleep(1);
                conditionA.signalAll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }finally {
                System.out.println(name + " 释放锁 ");
                lock.unlock();
            }
        };

        for (int i = 1; i <= 5; i++) {
            new Thread(runnableA,"A"+i).start();
            new Thread(runnableB,"B"+i).start();
            new Thread(runnableC,"C"+i).start();
        }
    }

}
