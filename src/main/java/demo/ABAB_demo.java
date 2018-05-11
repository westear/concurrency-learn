package demo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ABAB_demo {

	public static void main(String[] args) throws InterruptedException {
		Lock lock = new ReentrantLock();
		
		Condition c1 = lock.newCondition();
		Condition c2 = lock.newCondition();
		
		ThreadA a = new ThreadA(lock, c1, c2);
		ThreadB b = new ThreadB(lock, c1, c2);
		
		a.start();	//A线程先启动
		Thread.sleep(50);
		b.start();	//B线程后启动
	}
}

class ThreadA extends Thread {
	// 一个锁和两个Condition

	private Lock lock;
	private Condition c1;
	private Condition c2;

	// 构造方法注入这些引用
	public ThreadA(Lock lock, Condition c1, Condition c2) {
		this.lock = lock;
		this.c1 = c1;
		this.c2 = c2;
	}

	// 线程开始
	public void run() {
		try {
			lock.lock();// 加锁

			// 循环52次
			for (int i = 1; i <= 52; i++) {
				// i为奇数时可以打印，打印两次

				if (i % 2 != 0) {
					System.out.print(i + "" + (i + 1));
					c2.signal();// 通知c2这个condition开始运行
				} else {
					c1.await();// i为偶数时c1挂起
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}

class ThreadB extends Thread {
	private Lock lock;
	private Condition c1;
	private Condition c2;

	public ThreadB(Lock lock, Condition c1, Condition c2) {
		this.lock = lock;
		this.c1 = c1;
		this.c2 = c2;
	}

	public void run() {
		try {
			lock.lock();
			char c = 'A';// 定义char变量作为打印变量
			// 循环51次，因为遍历26个字母并且每个字母之间插入两个数字的话，需要循环51次，我是调试出来的，最初我也不知道需要51次。。。
			// 这里并不把char作为循环变量因为涉及到的是奇数次循环时不打印，所以char如果作为循环变量会跳过奇数次循环，会丢失打印。
			for (int i = 0; i < 51; i++) {
				if (i % 2 == 0) {// 偶数次循环时打印c并自增，通知c1这个condition可以运行。
					System.out.print(c++);
					c1.signal();
				} else {// 循环奇数次时，c2这个condition挂起。
					c2.await();
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}
