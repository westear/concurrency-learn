package java_container.list;

import java.util.ArrayList;
import java.util.List;

public class ArrayListTest {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(3);
        list.add(2);
        list.add(5);
        list.add(0);
        list.add(-3);
        list.sort(Integer::compareTo);

        for (Integer i : list) {
            System.out.println(i);
        }
    }
}
