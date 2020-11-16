package jvm_test;

import org.openjdk.jol.info.ClassLayout;

/**
 * java对象的大小 测试类
 * Java对象字节大小默认都是8的倍数，不够8的倍数则用0进行补位填充
 * 使用了 jol-core 进行对象大小查看
 * JDK6 之后， 默认开启(-XX:+UseCompressedOops)了指针压缩，所以 对象指针（Klass pointer）=4B， 想要关闭指针压缩则使用 -XX:-UseCompressedOops
 */
public class JavaObjectSizeTest {

    public static void main(String[] args) {
        Object emptyInstance = new Object();
        //MarkWord：8B + 对象指针：4B + 数组长度：0B + 实例数据：0B + 补位填充：4B = 16B
        System.out.println("空实例：" + ClassLayout.parseInstance(emptyInstance).toPrintable());

        //MarkWord：8B + 对象指针：4B + 数组长度：0B + 实例数据：4B + 补位填充：0B = 16B; int = 4B
        Integer integer = new Integer(130);
        System.out.println(ClassLayout.parseInstance(integer).toPrintable());

        //MarkWord：8B + 对象指针：4B + 数组长度：0B + 实例数据：4B + 补位填充：0B = 16B; float = 4B
        Float floatNumber = new Float(22.2222);
        System.out.println(ClassLayout.parseInstance(floatNumber).toPrintable());

        //MarkWord：8B + 对象指针：4B + 数组长度：0B + 实例数据：8B + 补位填充：4B = 24B; long = 8B
        Long longNumber = new Long(10000L);
        System.out.println(ClassLayout.parseInstance(longNumber).toPrintable());

        //MarkWord：8B + 对象指针：4B + 数组长度：0B + 实例数据：8B + 补位填充：4B = 24B; double = 8B
        Double doubleNumber = new Double(88.888);
        System.out.println(ClassLayout.parseInstance(doubleNumber).toPrintable());

        //MarkWord：8B + 对象指针：4B + 数组长度：0B + 实例数据：2B + 补位填充：2B = 16B; char = 2B
        Character character = new Character((char) 48);
        System.out.println(ClassLayout.parseInstance(character).toPrintable());

        //MarkWord：8B + 对象指针：4B + 数组长度：0B + 实例数据：2B + 补位填充：2B = 16B; short = 2B
        Short shortNumber = new Short((short) 1);
        System.out.println(ClassLayout.parseInstance(shortNumber).toPrintable());

        //MarkWord：8B + 对象指针：4B + 数组长度：0B + 实例数据：1B + 补位填充：3B = 16B; double = 1B
        Boolean flag = new Boolean(String.valueOf(0));
        System.out.println(ClassLayout.parseInstance(flag).toPrintable());

        //MarkWord：8B + 对象指针：4B + 数组长度=0：4B(int型) + 实例数据：0B + 补位填充：0B = 16B; String[] = new String[]{} = 16B
        String[] emptyArr = new String[]{};
        System.out.println(ClassLayout.parseInstance(emptyArr).toPrintable());

        //MarkWord：8B + 对象指针：4B + 数组长度：4B(int型) + 实例数据：3*4B + 补位填充：4B = 32B;
        String[] strArr = new String[]{"A","B","C"};
        System.out.println(ClassLayout.parseInstance(strArr).toPrintable());

        //MarkWord：8B + 对象指针：4B + 数组长度：4B(int型) + 实例数据：4*2B(char) + 补位填充：0B = 24B;
        char[] charArr = new char[]{'a','b','c','d'};
        System.out.println(ClassLayout.parseInstance(charArr).toPrintable());

        //MarkWord：8B + 对象指针：4B + 数组长度：4B(int型) + 实例数据：3*4B(int) + 补位填充：4B = 32B;
        int[] intArr = new int[]{1,2,3};
        System.out.println(ClassLayout.parseInstance(intArr).toPrintable());

        //MarkWord：8B + 对象指针：4B + 数组长度：4B(int型) + 实例数据：3*8B(long) + 补位填充：0B = 40B;
        long[] longArr = new long[]{1L,2L,3L};
        System.out.println(ClassLayout.parseInstance(longArr).toPrintable());
    }
}
