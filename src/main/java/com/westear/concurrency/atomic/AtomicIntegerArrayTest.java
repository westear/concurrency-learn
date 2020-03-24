package com.westear.concurrency.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * AtomicIntegerArray 会将 value 数组复制一份，
 * 当 AtomicIntegerArray 对内部元素进行修改时，不会影响传入的 value 数组的值
 */
public class AtomicIntegerArrayTest {

    static int[] value = new int[] {1,2};
    static AtomicIntegerArray array = new AtomicIntegerArray(value);

    public static void main(String[] args) {
        array.getAndSet(0,3);
        System.out.println(array.get(0));
        System.out.println(value[0]);
    }
}
