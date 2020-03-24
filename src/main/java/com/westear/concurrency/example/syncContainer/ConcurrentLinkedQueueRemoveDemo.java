package com.westear.concurrency.example.syncContainer;

import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ConcurrentLinkedQueue 遍历过程中发生修改操作并不会抛出 java.util.ConcurrentModificationException 的异常:
 * They do <em>not</em> throw {@link java.util.ConcurrentModificationException}, and may proceed concurrently with other operations.
 */
public class ConcurrentLinkedQueueRemoveDemo {

    public static void remove1(Integer number, ConcurrentLinkedQueue<Integer> queue) {
        if(queue.isEmpty()) {
            return;
        }
        queue.removeIf(i -> Objects.equals(number, i));
    }

    public static void remove2(Integer number, ConcurrentLinkedQueue<Integer> queue) {
        if(queue.isEmpty()) {
            return;
        }
        Iterator<Integer> iterator = queue.iterator();
        while (iterator.hasNext()) {
            Integer num = iterator.next();
            if(Objects.equals(number, num)) {
                queue.remove(num);
            }
        }
    }

    public static void print(ConcurrentLinkedQueue<Integer> queue) {
        for (Integer i : queue) {
            System.out.println(i);
        }
    }

    public static void main(String[] args) {
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
        queue.add(3);
        queue.add(5);
        queue.add(1);
//        remove1(5, queue);
        remove2(5,queue);
        print(queue);

    }
}
