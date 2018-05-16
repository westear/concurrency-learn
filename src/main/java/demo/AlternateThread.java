package demo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A,B两个线程交替执行
 * @author SAGE
 *
 */
public class AlternateThread {
	
	public static int count = 20;
	
	public static void main(String[] args) throws InterruptedException{
		Lock lock = new ReentrantLock();
		Condition conditionA = lock.newCondition();
		Condition conditionB = lock.newCondition();
		
		Thread_A thread_A = new Thread_A(lock, conditionA, conditionB, count);
		Thread_B thread_B = new Thread_B(lock, conditionA, conditionB, count);
		
		Thread threadA = new Thread(thread_A);
		Thread threadB = new Thread(thread_B);
		
		threadA.start();
		Thread.sleep(50);
		threadB.start();
		
	}
}

class Thread_A implements Runnable{

	private Lock lock;
	private Condition conditionA;
	private Condition conditionB;
	private int count;
	
	Thread_A(Lock lock, Condition conditionA, Condition conditionB, int count){
		this.lock = lock;
		this.conditionA = conditionA;
		this.conditionB = conditionB;
		this.count = count;
	}
	
	@Override
	public void run() {
		try{
			lock.lock();
			while(count > 0){
				if(count % 2 != 0){
					System.out.println("ThreadA:"+count);
					conditionB.signal();
				}else{
					conditionA.await();
				}
				count--;
			}
		}catch (InterruptedException e) {
			System.out.println("interruptedException");
		}finally{
			lock.unlock();
		}
	}
	
}

class Thread_B implements Runnable{
	
	private Lock lock;
	private Condition conditionA;
	private Condition conditionB;
	private int count;
	
	Thread_B(Lock lock, Condition conditionA, Condition conditionB, int count){
		this.lock = lock;
		this.conditionA = conditionA;
		this.conditionB = conditionB;
		this.count = count;
	}
	
	@Override
	public void run() {
		try{
			lock.lock();
			while(count > 0){
				if(count % 2 == 0){
					System.out.println("ThreadB:"+count);
					conditionA.signal();
				}else{
					conditionB.await();
				}
				count--;
			}
		}catch (InterruptedException e) {
			System.out.println("interruptedException");
		}finally{
			lock.unlock();
		}
	}
	
}