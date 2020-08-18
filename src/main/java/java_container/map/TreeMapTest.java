package java_container.map;

import java_container.set.Person1;

import java.util.*;

public class TreeMapTest {

    public static void main(String[] args) {
        Map<Integer, Integer> map = new TreeMap<>();
        NavigableMap<Integer, Integer> navigableMap = new TreeMap<>();
        SortedMap<Integer, Integer> sortedMap = new TreeMap<>();
        TreeMap<Integer, Person1> treeMap = new TreeMap<>();

        // 测试常用的API
        testTreeMapOrdinaryAPIs();

        // 测试TreeMap导航函数
        testNavigableMapAPIs();

        // 测试TreeMap的子Map函数
        testSubMapAPIs();
    }

    /**
     * 测试常用的API
     */
    private static void testTreeMapOrdinaryAPIs() {
        // 生成随机数
        Random random = new Random();

        // 创建TreeMap实例
        TreeMap treeMap = new TreeMap();
        treeMap.put("one", random.nextInt(10));
        treeMap.put("two", random.nextInt(10));
        treeMap.put("three", random.nextInt(10));

        System.out.println("TreeMapDemo.testTreeMapOrdinaryAPIs-Begin");

        // 打印TreeMap
        System.out.printf("打印treeMap:\n%s\n", treeMap);;

        // 通过Iterator遍历key-value
        Iterator iterator = treeMap.entrySet().iterator();
        System.out.println("通过Iterator遍历key-value:");
        while (iterator.hasNext()) {
            Map.Entry entity = (Map.Entry) iterator.next();
            System.out.printf("%s-%s\n", entity.getKey(), entity.getValue());
        }

        // TreeMap的键值对个数
        System.out.printf("TreeMap的键值对个数:%s\n", treeMap.size());

        // 是否包含key
        System.out.println("是否包含key:");
        System.out.printf("是否包含key:one-%s\n", treeMap.containsKey("one"));
        System.out.printf("是否包含key:four-%s\n", treeMap.containsKey("four"));

        // 删除key对应的键值对
        System.out.println("删除key对应的键值对:");
        treeMap.remove("one");
        System.out.printf("删除key为one的键值对后，treeMap为:\n%s\n", treeMap);

        // 清空TreeMap的节点
        System.out.println("清空treeMap的节点:");
        treeMap.clear();
        System.out.printf("%s\n", treeMap.isEmpty() ? "treeMap is empty!" : "treeMap is not empty!");
        System.out.printf("%s\n", treeMap == null ? "treeMap is null!" : "treeMap is not null!");

        System.out.println("TreeMapDemo.testTreeMapOrdinaryAPIs-End");
    }

    /**
     * 测试TreeMap导航函数
     */
    private static void testNavigableMapAPIs() {
        // 创建TreeMap实例，TreeMap是 NavigableMap接口的实现类
        NavigableMap navigableMap = new TreeMap();
        navigableMap.put("aaa",1);
        navigableMap.put("bbb",2);
        navigableMap.put("ccc",3);
        navigableMap.put("ddd",4);

        System.out.println("TreeMapDemo.testNavigableMapAPIs-Begin");

        // 打印TreeMap
        System.out.printf("打印navigableMap:\n%s\n", navigableMap);

        // 获取第一个key和节点
        System.out.printf("First key:%s\tFirst entry:%s\n", navigableMap.firstKey(), navigableMap.firstEntry());

        // 获取最后一个key和节点
        System.out.printf("Last key:%s\tLast entry:%s\n", navigableMap.lastKey(), navigableMap.lastEntry());

        // 获取小于/等于 key为bbb 最大的key和节点
        System.out.printf("Key floor before bbb:%s\t%s\n", navigableMap.floorKey("bbb"), navigableMap.floorEntry("bbb"));

        // 获取小于 key为bbb 最大的key和节点
        System.out.printf("Key lower before bbb:%s\t%s\n", navigableMap.lowerKey("bbb"), navigableMap.lowerEntry("bbb"));

        // 获取大于/等于 key为bbb 最大的key和节点
        System.out.printf("Key ceiling after bbb:%s\t%s\n", navigableMap.ceilingKey("bbb"), navigableMap.ceilingEntry("bbb"));

        // 获取大于 key为bbb 最大的key和节点
        System.out.printf("Key higher after bbb:%s\t%s\n", navigableMap.higherKey("bbb"), navigableMap.higherEntry("bbb"));

        System.out.println("TreeMapDemo.testNavigableMapAPIs-End");
    }

    /**
     * 测试TreeMap的子Map函数
     */
    private static void testSubMapAPIs() {
        // 实例化TreeMap对象
        TreeMap treeMap = new TreeMap();
        treeMap.put("a",1);
        treeMap.put("b",2);
        treeMap.put("c",3);
        treeMap.put("d",4);

        System.out.println("TreeMapDemo.testSubMapAPIs-Begin");

        // 打印TreeMap
        System.out.printf("打印TreeMap:\n%s\n", treeMap);

        // 打印 key为c节点 前的节点（默认不包含c节点）
        System.out.printf("打印 key为c节点 前的节点（默认不包含c节点）:%s", treeMap.headMap("c"));

        System.out.printf("打印 key为c节点 前的节点（包含c节点）:%s\n", treeMap.headMap("c", true));
        System.out.printf("打印 key为c节点 前的节点（不包含c节点）:%s\n", treeMap.headMap("c", false));

        // 打印 key为c节点 后的节点（默认包含c节点）
        System.out.printf("打印 key为c节点 后的节点（默认包含c节点）:%s\n", treeMap.tailMap("c"));

        System.out.printf("打印 key为c节点 后的节点（包含c节点）%s\n", treeMap.tailMap("c", true));
        System.out.printf("打印 key为c节点 后的节点（不包含c节点）%s\n", treeMap.tailMap("c", false));

        // 打印 key为a与c节点 之间的节点（默认不包含c节点）
        System.out.printf("打印 key为a与c节点 之间的节点（默认包含c节点）:\n%s\n", treeMap.subMap("a", "c"));

        System.out.printf("打印 key为a与c节点 之间的节点（包含a、c节点）:\n%s\n", treeMap.subMap("a", true, "c", true));
        System.out.printf("打印 key为a与c节点 之间的节点（包含a节点）:\n%s\n", treeMap.subMap("a", true, "c", false));
        System.out.printf("打印 key为a与c节点 之间的节点（包含c节点）:\n%s\n", treeMap.subMap("a", false, "c", true));
        System.out.printf("打印 key为a与c节点 之间的节点（不包含a、c节点）:\n%s\n", treeMap.subMap("a", false, "c", false));

        // 正序打印TreeMap的key
        System.out.printf("正序打印TreeMap的key:\n%s\n", treeMap.navigableKeySet());
        // 倒序打印TreeMap的key
        System.out.printf("倒序打印TreeMap的key:\n%s\n", treeMap.descendingKeySet());

        System.out.println("TreeMapDemo.testSubMapAPIs-End");
    }
}
