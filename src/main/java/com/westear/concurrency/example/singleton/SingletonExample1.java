package com.westear.concurrency.example.singleton;

import com.westear.concurrency.annoations.NotRecommend;
import com.westear.concurrency.annoations.ThreadSafe;

/**
 * 懒汉模式
 * 第一次调用实例时创建实例
 * @author SAGE
 *
 */
@ThreadSafe
@NotRecommend /*影响性能*/
public class SingletonExample1 {

	private SingletonExample1(){
		//进行相关操作
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static SingletonExample1 instance = null;
	
	//静态工厂方法
	public static synchronized SingletonExample1 getInstance(){
		if(instance == null){
			instance = new SingletonExample1();
		}
		return instance;
	}
}
