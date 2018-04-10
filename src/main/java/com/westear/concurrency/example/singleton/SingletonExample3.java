package com.westear.concurrency.example.singleton;

import com.westear.concurrency.annoations.NotRecommend;
import com.westear.concurrency.annoations.NotThreadSafe;

/**
 * 懒汉模式
 * 第一次调用实例时创建实例
 * @author SAGE
 *
 */
@NotThreadSafe
@NotRecommend
public class SingletonExample3 {

	private SingletonExample3(){
		//进行相关操作
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 对象的创建过程：
	 * 1.分配内存空间
	 * 2.初始化对象
	 * 3.设置对象的引用
	 * 步骤2,3可能发生指令重排,造成对未初始化的对象进行引用
	 */
	
	private static SingletonExample3 instance = null;
	
	//静态工厂方法
	public static SingletonExample3 getInstance(){
		if(instance == null){	//双重检测机制
			synchronized (SingletonExample3.class) {	//同步锁
				if(instance == null){ //线程不安全
					instance = new SingletonExample3();
				}
			}
		}
		return instance;
	}
}
