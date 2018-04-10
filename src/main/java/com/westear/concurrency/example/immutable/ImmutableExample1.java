package com.westear.concurrency.example.immutable;

import java.util.Map;

import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * final定义不可变成员变量和引用变量的引用
 * @author SAGE
 *
 */

@Slf4j
public class ImmutableExample1 {

	private final static Integer a = 1;
	
	private final static String b = "2";
	
	private final static Map<Integer,Integer> map = Maps.newHashMap();
	
	static {
		map.put(1, 2);
		map.put(3, 4);
		map.put(5, 6);
	}
	
	public static void main(String[] args){
//		a = 2;	//不允许修改基本类型的值
//		map = n;	//对于引用型变量，不允许指向另外的引用
		map.put(1, 8);	//但是可以修改引用里面的值，容易引发线程安全问题
		log.info("map {} ",map.get(1));
		
	}
}
