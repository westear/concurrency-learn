package com.westear.concurrency.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * AtomicIntegerFieldUpdater : 原子更新整型的字段的更新器
 * AtomicLongFieldUpdater : 原子更新长整型字段的更新器
 * AtomicStampedReference : 原子更新带有版本号的引用类型。该类将整数值与引用关联起来，可用于原子的更新数据和数据的版本号，
 *                          可以解决使用CAS进行原子更新时可能出现的ABA问题
 */
public class AtomicIntegerFieldUpdaterTest {

    //创建原子更新器，并设置需要更新的对象和对象的属性
    private static AtomicIntegerFieldUpdater<User> a = AtomicIntegerFieldUpdater.newUpdater(User.class, "old");

    public static void main(String[] args) {
        //设置柯南年龄为10岁
        User conan = new User("conan", 10);
        //柯南长了一岁，但是仍然会输出旧的年龄
        System.out.println(a.getAndIncrement(conan));
        //输出新的值
        System.out.println(a.get(conan));
    }

    public static class User {
        private String name;
        public volatile int old;
        public User(String name, int old) {
            this.name = name;
            this.old = old;
        }

        public String getName() {
            return name;
        }

        public int getOld() {
            return old;
        }
    }
}
