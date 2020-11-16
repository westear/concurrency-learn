package java_container.fail_fast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Fail_FastDemo {

    /**
     * 多线程操作容器结构改变的情况下，抛出 java.util.ConcurrentModificationException
     * @param arrayList
     */
    private static void pintByIterator(ArrayList<Integer> arrayList) {
        Iterator<Integer> iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            System.out.println(next);
        }
    }

    public static void main(String[] args) throws InterruptedException {

//        ArrayList<Integer> arrayList = new ArrayList<>();
//        for (int i = 1; i <= 3 ; i++) {
//            arrayList.add(i);
//        }

        /**
         * 使用removeIf 判断后删除，不会 抛出 java.util.ConcurrentModificationException
         */
//        arrayList.removeIf(integer -> integer.equals(1));

        /**
         * foreach 形式遍历集合
         * 1
         * 2
         * remove 3
         * 3
         * 抛出 java.util.ConcurrentModificationException
         */
//        for (Integer i : arrayList) {
//            if(i.equals(3)) {
//                System.out.println("remove " + i);
//                arrayList.remove(i);
//            }
//            System.out.println(i);
//        }

        ExecutorService executor = Executors.newFixedThreadPool(3);
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            executor.execute(() -> {
                arrayList.add((int) (Math.random() * 10));
                pintByIterator(arrayList);
            });
        }
    }
}
