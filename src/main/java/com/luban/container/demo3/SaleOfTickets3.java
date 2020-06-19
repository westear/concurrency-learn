package com.luban.container.demo3;

import com.westear.concurrency.annoations.NotThreadSafe;
import com.westear.concurrency.annoations.ThreadSafe;

import java.util.LinkedList;
import java.util.List;

/**
 * 有10000张火车票,同时有10个窗口对外售票
 * 请写一个模拟程序
 * 虽然使用的容器不是同步的，但是程序判断过程中使用同步锁 synchronized 对整个容器进行了锁定，保证操作是原子性的
 */
@ThreadSafe
public class SaleOfTickets3 {

	private static List<Integer> tickets = new LinkedList<>();
	
	static {
		for (int i = 0; i < 10000; i++) {
			tickets.add(i);
		}
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			new Thread(()->{
				while(true){
					//这里使用synchronized，使两个操作具备了原子性，不会出问题
					synchronized(tickets){
						if(tickets.size() <= 0){
							break;
						}
						System.out.println("销售票编号:" + tickets.remove(0));
					}
				}
			}).start();
		}
	}

}
