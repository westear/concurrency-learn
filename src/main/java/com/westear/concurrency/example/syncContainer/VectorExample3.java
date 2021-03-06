package com.westear.concurrency.example.syncContainer;

import java.util.Iterator;
import java.util.Vector;

import com.westear.concurrency.annoations.NotThreadSafe;

/**
 * 同步容器在遍历操作过程中进行数据更新删除带来的问题
 * Vector是同步容器，但不确保线程绝对安全
 * @author SAGE
 * 
 */
@NotThreadSafe
public class VectorExample3 {
	// java.util.ConcurrentModificationException
    private static void test1(Vector<Integer> v1) { // foreach
//        v1.removeIf(i -> i.equals(3));  //不会抛出异常

        for (Integer i : v1) { // 抛出 java.util.ConcurrentModificationException
            if(i.equals(3)) {
                v1.remove(i);
            }
        }
    }

    // java.util.ConcurrentModificationException
    private static void test2(Vector<Integer> v1) { // iterator
        Iterator<Integer> iterator = v1.iterator();
        while (iterator.hasNext()) {
            Integer i = iterator.next();
            if (i.equals(3)) {
                v1.remove(i);
            }
        }
    }

    // success
    private static void test3(Vector<Integer> v1) { // for
        for (int i = 0; i < v1.size(); i++) {
            if (v1.get(i).equals(3)) {
                v1.remove(i);
            }
        }
    }

    public static void main(String[] args) {

        Vector<Integer> vector = new Vector<>();
        vector.add(1);
        vector.add(2);
        vector.add(3);
//        test1(vector);
        test2(vector);
//        test3(vector);

        for (int i = 0; i < vector.size(); i++) {
            System.out.println(vector.get(i));
        }
    }
	
}
