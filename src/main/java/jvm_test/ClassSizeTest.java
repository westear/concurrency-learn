package jvm_test;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * java对象的大小 测试类
 * Java对象大小默认都是8的倍数，不够8的倍数则用0进行补位填充
 *
 * @projectName: concurrency
 * @author: Qinyunchan
 * @date: 2020/2/15  3:06 下午
 * @version: 1.0
 */
public class ClassSizeTest {

    public static void main(String[] args) {
        Object object = new Object();
        //对象头：8B + 对象指针：4B + 数组长度：0B + 实例数据：0B + 补位填充：4B = 16B
        System.out.println("Object class size: " + ObjectSizeCalculator.getObjectSize(object));

        System.out.println("Integer: " + ObjectSizeCalculator.getObjectSize(Integer.valueOf(122)));
        System.out.println("Long: " + ObjectSizeCalculator.getObjectSize(Long.valueOf(122L)));
        System.out.println("Double: " + ObjectSizeCalculator.getObjectSize(Double.valueOf(122.22)));
        System.out.println("Float: " + ObjectSizeCalculator.getObjectSize(Float.valueOf(122.22f)));
        System.out.println("Boolean: " + ObjectSizeCalculator.getObjectSize(Boolean.valueOf(false)));
        System.out.println("Character: " + ObjectSizeCalculator.getObjectSize(Character.valueOf('a')));
        System.out.println("Short: " + ObjectSizeCalculator.getObjectSize(Short.valueOf("1")));
        System.out.println("Byte: " + ObjectSizeCalculator.getObjectSize(Byte.valueOf("1")));
        System.out.println("Date: " + ObjectSizeCalculator.getObjectSize(new Date()));
        System.out.println("Timestamp: " + ObjectSizeCalculator.getObjectSize(new Timestamp(System.currentTimeMillis())));
        System.out.println("String=123: " + ObjectSizeCalculator.getObjectSize(new String("123")));

        Integer[] integerArr = new Integer[2];
        System.out.println("Integer[]:" + ObjectSizeCalculator.getObjectSize(integerArr));

        Long[] longArr = new Long[2];
        System.out.println("Long[]:" + ObjectSizeCalculator.getObjectSize(longArr));

        Map<String, Object> map = new HashMap<>();
        map.put("11", 11);
        map.put("22", "22");
        map.put("33", 33L);
        map.put("44", 44.44);
        System.out.println("Map: " + ObjectSizeCalculator.getObjectSize(map));

    }
}
