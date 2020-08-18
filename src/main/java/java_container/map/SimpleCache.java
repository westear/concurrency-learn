package java_container.map;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 通过继承 LinkedHashMap 实现一个简单的 LRU 算法的缓存
 * @param <K> key
 * @param <V> value
 */
public class SimpleCache<K, V> extends LinkedHashMap<K, V> {

    private static final int MAX_COUNT = 100;

    private int limit;

    public SimpleCache() {
        this(MAX_COUNT);
    }

    public SimpleCache(int limit) {
        super(limit, 0.75f, true);
        this.limit = limit;
    }

    /**
     * 判断节点数是否超限
     * @param eldest 最早的节点
     * @return 超限返回 true，否则返回 false
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > limit;
    }

    public static void main(String[] args) {
        SimpleCache<Integer, Integer> cache = new SimpleCache<>(3);

        for (int i = 0; i < 10; i++) {
            cache.put(i, i * i);
        }

        System.out.println("插入10个键值对后，缓存内容：");
        System.out.println(cache + "\n");

        System.out.println("访问键值为7的节点后，缓存内容：");
        cache.get(7);
        System.out.println(cache + "\n");

        System.out.println("插入键值为1的键值对后，缓存内容：");
        cache.put(1, 1);
        System.out.println(cache);
    }
}
