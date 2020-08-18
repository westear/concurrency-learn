package java_container.map;

import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class WeakHashMapTest {

    public static void main(String[] args) {
        WeakHashMap<String, String> weakHashMap = new WeakHashMap<>(10);

        String key0 = new String("kuang");
        String key1 = new String("zhong");
        String key2 = new String("wen");

        // 存放元素
        weakHashMap.put(key0, "q1");
        weakHashMap.put(key1, "q2");
        weakHashMap.put(key2, "q3");
        System.out.printf("weakHashMap: %s\n", weakHashMap);

        // 是否包含某key
        System.out.printf("contains key kuang : %s\n", weakHashMap.containsKey(key0));
        System.out.printf("contains key zhong : %s\n", weakHashMap.containsKey(key1));

        // 是否包含某value
        System.out.printf("contains value 0 : %s\n", weakHashMap.containsValue(0));

        // 移除key
        weakHashMap.remove(key2);
        System.out.printf("weakHashMap after remove: %s", weakHashMap);

        // 这意味着"弱键"key0再没有被其它对象引用，调用gc时会回收WeakHashMap中与key0对应的键值对
        key0 = null;
        // 内存回收，这里会回收WeakHashMap中与"key0"对应的键值对
        System.gc();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 遍历WeakHashMap
        Iterator iter = weakHashMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry en = (Map.Entry) iter.next();
            System.out.printf("next : %s - %s\n", en.getKey(), en.getValue());
        }
        // 打印WeakHashMap的实际大小
        System.out.printf("after gc WeakHashMap size: %s\n", weakHashMap.size());
    }

}
