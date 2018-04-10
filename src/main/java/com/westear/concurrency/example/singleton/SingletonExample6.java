package com.westear.concurrency.example.singleton;

import com.westear.concurrency.annoations.Recommend;
import com.westear.concurrency.annoations.ThreadSafe;

/**
 * 使用枚举类保证线程安全
 * @author SAGE
 *
 */
@ThreadSafe
@Recommend /*最安全的单例获取模式*/
public class SingletonExample6 {

	private SingletonExample6(){
		//进行相关操作
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//调用枚举
	public static SingletonExample6 getInstance(){
		return Singleton.INSTANCE.getInstance();
	}
	
	/**
	 * 枚举类
	 * @author SAGE
	 *
	 */
	private enum Singleton{
		INSTANCE;
		
		private SingletonExample6 singleton;
		
		//JVM保证这个方法绝对只调用一次
		Singleton(){
			singleton = new SingletonExample6();
		}
		
		public SingletonExample6 getInstance(){
			return singleton;
		}
	}
}
