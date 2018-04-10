package com.westear.concurrency.example.immutable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import lombok.extern.slf4j.Slf4j;

/**
 * 使用guava中的不可变集合对象:ImmutableXXX
 * @author SAGE
 *
 */

@Slf4j
public class ImmutableExample3 {

	private final static ImmutableList<Integer> list = ImmutableList.of(1,2,3);
	
	private final static ImmutableSet<Integer> set = ImmutableSet.copyOf(list);
	
	private final static ImmutableMap<Integer, Integer> map = ImmutableMap.of(1,2,3,4,5,6);
	
	private final static ImmutableMap<Integer, Integer> map2 = ImmutableMap.<Integer,Integer>builder()
			.put(1,2).put(3,4).put(5,6).build();
	
	public static void main(String[] args){
//		list.add(4);	//不可变
//		set.add(4);		//不可变
//		map.put(1, 8);	//不可变
//		map2.put(2, 9);	//不可变
		log.info("{}",list.get(1));
		log.info("{}",set.size());
		log.info("{}",map.get(1));
		log.info("{}",map2.get(1));
	}
}
