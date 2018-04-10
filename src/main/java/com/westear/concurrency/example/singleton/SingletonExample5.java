package com.westear.concurrency.example.singleton;

import com.westear.concurrency.annoations.ThreadSafe;

/**
 * 饿汉模式:适用于构造函数中没有太多的处理逻辑
 * 类装载时创建实例
 * @author SAGE
 *
 */
@ThreadSafe
public class SingletonExample5 {

	private SingletonExample5(){
		//进行相关操作
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//得到空值
//	static{
//		instance = new SingletonExample5();
//	}
	
	private static SingletonExample5 instance = new SingletonExample5();
	
	static{
		instance = new SingletonExample5();
	}
	
	//静态工厂方法
	public static SingletonExample5 getInstance(){
		
		return instance;
	}
	
	public static void main(String[] args){
		System.out.println(SingletonExample5.getInstance().hashCode());
		System.out.println(SingletonExample5.getInstance().hashCode());
	}
}
