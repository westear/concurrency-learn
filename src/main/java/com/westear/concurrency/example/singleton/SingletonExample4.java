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
@NotRecommend
public class SingletonExample4 {

	private SingletonExample4(){
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
	
	//使用volatile限制指令重排
	private volatile static SingletonExample4 instance = null;
	
	//静态工厂方法
	public static SingletonExample4 getInstance(){
		if(instance == null){	//双重检测机制
			synchronized (SingletonExample4.class) {	//同步锁
				if(instance == null){	//volatile确保instance获取时不被另外的线程读
					instance = new SingletonExample4();
				}
			}
		}
		return instance;
	}
}
