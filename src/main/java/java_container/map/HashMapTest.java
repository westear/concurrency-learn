package java_container.map;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class HashMapTest {

    public static void main(String[] args) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            map.put(i,i);
        }

        System.out.println(map.getOrDefault(11, 11));


        Map<Integer, Integer> linkedMap = new LinkedHashMap<>();
        for (int i = 0; i < 10; i++) {
            linkedMap.put(i,i);
        }

        Set<Map.Entry<Integer, Integer>> entrySet = linkedMap.entrySet();
        for (Map.Entry<Integer, Integer> entry : entrySet) {
            System.out.println(entry.getValue());
        }
    }
}
