package com.westear.concurrency.javaThread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 该程序有 begin() 和 end() 两个方法，而 end() 方法返回从 begin() 方法调用开始到 end() 方法被调用时的时间差，单位是毫秒
 *
 * ThreadLocal实现原理 :
 *      首先 ThreadLocal 是一个泛型类，保证可以接受任何类型的对象。
 *      因为一个线程内可以存在多个 ThreadLocal 对象，所以其实是 ThreadLocal 内部维护了一个 Map ，
 *      这个 Map 不是直接使用的 HashMap ，而是 ThreadLocal 实现的一个叫做 ThreadLocalMap 的静态内部类。
 *      而我们使用的 get()、set() 方法其实都是调用了这个ThreadLocalMap类对应的 get()、set() 方法
 *
 * 内存泄漏问题:
 *      实际上 ThreadLocalMap 中使用的 key 为 ThreadLocal 的弱引用，弱引用的特点是，如果这个对象只存在弱引用，那么在下一次垃圾回收的时候必然会被清理掉。
 *      所以如果 ThreadLocal 没有被外部强引用的情况下，在垃圾回收的时候会被清理掉的，这样一来 ThreadLocalMap中使用这个 ThreadLocal 的 key 也会被清理掉。
 *      但是，value 是强引用，不会被清理，这样一来就会出现 key 为 null 的 value。
 *      ThreadLocalMap实现中已经考虑了这种情况，在调用 set()、get()、remove() 方法的时候，会清理掉 key 为 null 的记录。
 *      如果说会出现内存泄漏，那只有在出现了 key 为 null 的记录后，没有手动调用 remove() 方法，并且之后也不再调用 get()、set()、remove() 方法的情况下。
 */
public class ThreadLocalDemo {
    //第一次 get() 方法调用时会进行初始化（如果set方法没有调用）,每个线程会调用一次
    private static final ThreadLocal<Long> TIME_THREADLOCAL = ThreadLocal.withInitial(System::currentTimeMillis);

    //线程变量里的 SimpleDateFormat
//    private static final ThreadLocal<SimpleDateFormat> dateFormat_thread = ThreadLocal.withInitial(new Supplier<SimpleDateFormat>() {
//        @Override
//        public SimpleDateFormat get() {
//            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        }
//    });
     private static final ThreadLocal<SimpleDateFormat> dateFormat_thread
            = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    public static final void begin() {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    public static final long end() {
        return System.currentTimeMillis() - TIME_THREADLOCAL.get();
    }

    public static void main(String[] args) throws Exception {
        ThreadLocalDemo.begin();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Cost: " + ThreadLocalDemo.end() + "mills");
    }

    public static String formatDate(Date date) {
        return dateFormat_thread.get().format(date);
    }
}
