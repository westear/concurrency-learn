package java_container.iterator_demo;

import java.util.*;

/**
 * ListIterator 只遍历 List
 * ListIterator 可以双向遍历
 * 添加了一些额外的功能：添加一个元素、替换一个元素、获取前面或后面元素的索引位置
 */
public class ListIteratorDemo {

    public static void main(String[] args) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            arrayList.add(i);
        }

        ListIterator<Integer> listIterator = arrayList.listIterator();
        Spliterator<Integer>  ArrayListSpliterator= arrayList.spliterator();   //Spliterator是一个可分割迭代器(splitable iterator)
        while (listIterator.hasPrevious()) {    //向前遍历
            Integer previous = listIterator.previous();
            System.out.println(previous);
        }

        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            set.add(i);
        }

        Iterator<Integer> setIterator = set.iterator();
        Spliterator<Integer> spliterator = set.spliterator();   //Spliterator是一个可分割迭代器(splitable iterator)
    }
}
