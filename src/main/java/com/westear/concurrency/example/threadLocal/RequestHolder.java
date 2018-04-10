package com.westear.concurrency.example.threadLocal;

public class RequestHolder {

	private final static ThreadLocal<Long> requestHolder = new ThreadLocal<>();
	
	public static void add(Long id){
		requestHolder.set(id);
	}
	
	public static Long getId(){
		return requestHolder.get();
	}
	
	//不移除的话容易造成内存泄漏
	public static void remove(){
		requestHolder.remove();
	}
}
