package com.luban.producerAndConsumer;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * 使用Lock和Condition来实现
 * condition就是在什么条件下怎么做
 * 对比上一个例子，Condition的方式可以更加精确的指定哪些线程被唤醒
 *
 */
public class ConditionDemo<T> {

	private final LinkedList<T> lists = new LinkedList<>();
	private final int MAX = 10;
	private int count = 0;
	
	private Lock lock = new ReentrantLock();
	private Condition producer = lock.newCondition();
	private Condition consumer = lock.newCondition();
	
	public void put(T t){
		/*
		 * 1.在try-finally外加锁，如果因为异常导致加锁失败，try-finally块中的代码不会执行。
		 * 	 相反，如果在try{}代码块中加锁失败，finally中的代码无论如何都会执行，但是由于当前线程加锁失败并没有持有lock对象锁 ，
		 *   所以程序会抛出IllegalMonitorStateException异常。
		 * 2.加锁语句和try代码块之间不能有其他会抛出异常的代码，
		 *   因为如果代码抛出异常，却无法执行 lock.unlock() ，会造成锁无法被释放
		 */
		lock.lock();
		try {
			while (lists.size() == MAX) {
				producer.await();
			}
			lists.add(t);
			++count;
			consumer.signalAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public T get(){
		T t = null;
		lock.lock();
		try {
			while (lists.size() == 0) {
				consumer.await();
			}
			
			t = lists.removeFirst();
			count --;
			producer.signalAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		
		return t;
	}
	
	public static void main(String[] args) {
		ConditionDemo<String> c = new ConditionDemo<>();
		for (int i = 0; i < 100; i++) {
			new Thread(()->{
				for (int j = 0; j < 5; j++) {
					System.out.println(c.get());
				}
			}, "c" + i).start();
		}
		
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < 2; i++) {
			new Thread(()->{
				for (int j = 0; j < 25; j++) {
					c.put(Thread.currentThread().getName() + " " + j);
				}
			}, "p" + i).start();
		}
	}
}
