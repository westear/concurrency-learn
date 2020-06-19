package com.luban.container.collections;

import java.util.*;

/**
 * Collections
 *
 * collections是java里面一个集合处理类，里面有给容器加锁的方法，通过调用api可以返回一个加了锁的容器。
 */
public class CollectionsSyncDemo {

	public static void main(String[] args) {
		ArrayList<String> arrayList = new ArrayList<>();
		List<String> synchronizedList = Collections.synchronizedList(arrayList);

		Map<String,Object> map = new HashMap<>();
		Map<String,Object> synchronizedMap = Collections.synchronizedMap(map);

		Set<String> set = new HashSet<>();
		Set<String> synchronizedSet = Collections.synchronizedSet(set);

	}
}
