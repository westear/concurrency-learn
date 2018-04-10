package com.westear.concurrency.example.publish;

import java.util.Arrays;

import com.westear.concurrency.annoations.NotThreadSafe;

import lombok.extern.slf4j.Slf4j;

/**
 * 发布不安全的类
 * @author SAGE
 *
 */

@NotThreadSafe
@Slf4j
public class UnsafePublish {

	private String[] states = {"a","b","c","d"};

	public String[] getStates() {
		return states;
	}

	public static void main(String[] args){
		UnsafePublish unsafePublish = new UnsafePublish();
		log.info("{}",Arrays.toString(unsafePublish.getStates()));
		
		//修改数组中的元素
		unsafePublish.getStates()[0] = "d";
		log.info("{}",Arrays.toString(unsafePublish.getStates()));
	}
	
	
}
