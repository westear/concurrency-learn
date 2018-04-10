package com.westear.concurrency.example.syncContainer;

import java.util.Vector;

import com.westear.concurrency.annoations.NotThreadSafe;

/**
 * Vector是同步容器，但不确保线程绝对安全
 * @author SAGE
 *
 */
@NotThreadSafe
public class VectorExample2 {
	//请求总数
	public static int clientTotal = 5000;
	//并发线程数
	public static int threadTotal = 200;
	
	public static Vector<Integer> vector = new Vector<>();
	
	public static void main(String[] args) throws InterruptedException{
		while(true){
			for(int i = 0; i < 10; i++){
				vector.add(i);
			}
		
			Thread thread1 = new Thread(){
				public void run(){
//					System.out.println(Thread.currentThread().getId());
					for(int i = 0; i < vector.size(); i++){
						vector.remove(i);
					}
				}
			};
			
			Thread thread2 = new Thread(){
				public void run(){
//					System.out.println(Thread.currentThread().getId());
					for(int i = 0; i < vector.size(); i++){
						vector.get(i);
					}
				}
			};
			thread1.start();
			thread2.start();
		}
	}
	
}
