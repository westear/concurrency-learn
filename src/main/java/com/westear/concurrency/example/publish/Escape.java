package com.westear.concurrency.example.publish;

import com.westear.concurrency.annoations.NotRecommend;
import com.westear.concurrency.annoations.NotThreadSafe;

import lombok.extern.slf4j.Slf4j;

/**
 * 类的逸出
 * @author SAGE
 *
 */
@NotThreadSafe
@NotRecommend
@Slf4j
public class Escape {

	private int thisCanBeEscape = 0;
	
	private class InnerClass{
		public InnerClass(){
			//此时的 this 已经被InnerClass引用，但Escape可能还未初始化完成
			log.info("{}",Escape.this.thisCanBeEscape);
		}
	}
	
	public Escape(){
		new InnerClass();
	}
	
	public static void main(String[] args){
		new Escape();
	}
}
