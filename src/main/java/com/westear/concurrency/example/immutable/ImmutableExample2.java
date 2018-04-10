package com.westear.concurrency.example.immutable;

import java.util.Collections;
import java.util.Map;

import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * final定义不可变成员变量和引用变量的引用
 * @author SAGE
 *
 */

@Slf4j
public class ImmutableExample2 {

	private static Map<Integer,Integer> map = Maps.newHashMap();
	
	static {
		map.put(1, 2);
		map.put(3, 4);
		map.put(5, 6);
		map = Collections.unmodifiableMap(map);
	}
	
	public static void main(String[] args){
		map.put(1, 8);	//调用Collections.unmodifiableXXX时，不允许修改引用变量中的值，运行出错
		log.info("map {} ",map.get(1));
		
	}
}
