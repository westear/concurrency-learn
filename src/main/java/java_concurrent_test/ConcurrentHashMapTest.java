package java_concurrent_test;

import org.junit.Assert;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ConcurrentHashMapTest {

    public static void main(String[] args) {
        ConcurrentHashMap<String, Integer> hashMap = new ConcurrentHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hashMap.put("str_"+i, i);
        }

        System.out.println(hashMap.replace("str_" + 1, 1, 0));
        System.out.println(hashMap.replace("str_" + 11, 22, 11));

        /*
         * 不包含key就返回null, 但是已经把新建的这个键值对加入map中了
         */
        System.out.println(hashMap.putIfAbsent("str_11", 22));

        /*
         * 不包含key就将计算的value和key放入map返回apply计算的值
         */
        System.out.println(hashMap.computeIfAbsent("str_22", key -> {
            String[] s = key.split("_");
            if (s.length > 1) {
                return Integer.parseInt(s[1]) * 2;
            }
            return null;
        }));
        /*
         * 包含key就返回当前key对应的value，但不会将计算的新值覆盖旧值
         */
        System.out.println(hashMap.computeIfAbsent("str_10", key -> {
            String[] s = key.split("_");
            if (s.length > 1) {
                return Integer.parseInt(s[1]) * 2;
            }
            return null;
        }));


        /*
         * 包含key， 计算的值不为空，返回计算的值， 并覆盖旧值
         */
        System.out.println(hashMap.computeIfPresent("str_10", (key, integer) -> {
            String[] s = key.split("_");
            if (s.length > 1) {
                return Integer.parseInt(s[1]) * integer;
            }
            return null;
        }));
        /*
         * 包含key， 计算的值为空，返回空，删除该键值对，
         */
        System.out.println(hashMap.computeIfPresent("str_5", (key, integer) -> null));
        /*
         * 不包含key， 返回null
         */
        System.out.println(hashMap.computeIfPresent("str_33", (key, integer) -> {
            String[] s = key.split("_");
            if (s.length > 1) {
                return Integer.parseInt(s[1]) * integer;
            }
            return null;
        }));

        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }


        ConcurrentHashMap<String,String> map =  new  ConcurrentHashMap<>(3);  //初始化 ConcurrentHashMap

        //新增个人信息
        map.put(    "id"    ,    "1"    );
        map.put(    "name"    ,    "andy"    );
        map.put(    "sex"    ,    "男"    );

        //获取姓名
        String name = map.get(    "name"    );
        Assert.assertEquals(name,    "andy"    );

        //计算大小
        int  size = map.size();
        Assert.assertEquals(size,    3    );
    }
}
