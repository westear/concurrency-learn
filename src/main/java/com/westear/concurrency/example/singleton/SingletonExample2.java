package com.westear.concurrency.example.singleton;

import com.westear.concurrency.annoations.ThreadSafe;

/**
 * 饿汉模式:适用于构造函数中没有太多的处理逻辑
 * 类装载时创建实例
 * @author SAGE
 *
 */
@ThreadSafe
public class SingletonExample2 {

	private SingletonExample2(){
		//进行相关操作
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static SingletonExample2 instance = new SingletonExample2();
	
	//静态工厂方法
	public static SingletonExample2 getInstance(){
		
		return instance;
	}
}
