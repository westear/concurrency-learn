package com.westear.concurrency.lockAndAQS;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * @see StampedLock
 * StampedLock是为了优化可重入读写锁性能的一个锁实现工具，jdk8开始引入
 * 相比于普通的ReentranReadWriteLock主要多了一种乐观读的功能
 * 在API上增加了stamp的入参和返回值
 * 不支持重入
 * 支持锁的降级和升级： 乐观读锁 -> 悲观读锁; 悲观读锁 -> 写锁
 *
 * StampedLock的特点
 * 所有获取锁的方法，都返回一个邮戳（Stamp），Stamp为0表示获取失败，其余都表示成功；
 * 所有释放锁的方法，都需要一个邮戳（Stamp），这个Stamp必须是和成功获取锁时得到的Stamp一致；
 * StampedLock是不可重入的；（如果一个线程已经持有了写锁，再去获取写锁的话就会造成死锁）
 * 支持锁升级跟锁降级
 * 可以乐观读也可以悲观读
 * 使用有限次自旋，增加锁获得的几率，避免上下文切换带来的开销
 * 乐观读不阻塞写操作，悲观读，阻塞写得操作
 *
 * StampedLock的优点
 * 相比于ReentrantReadWriteLock，吞吐量大幅提升
 *
 * StampedLock的缺点
 * api相对复杂，容易用错
 * 内部实现相比于ReentrantReadWriteLock复杂得多
 *
 * StampedLock的原理
 * 每次获取锁的时候，都会返回一个邮戳（stamp），相当于mysql里的version字段
 * 释放锁的时候，再根据之前的获得的邮戳，去进行锁释放
 *
 * 使用stampedLock注意点
 * 如果使用乐观读，一定要判断返回的邮戳是否是一开始获得到的，如果不是，要去获取悲观读锁，再次去读取
 *
 *
 * 1、进入悲观读锁前先看下有没有进入写模式（说白了就是有没有已经获取了悲观写锁）
 *
 * 2、如果其他线程已经获取了悲观写锁，那么就只能老老实实的获取悲观读锁（这种情况相当于退化成了读写锁）
 *
 * 3、如果其他线程没有获取悲观写锁，那么就不用获取悲观读锁了，
 * 减少了一次获取悲观读锁的消耗和避免了因为读锁导致写锁阻塞的问题，直接返回读的数据即可
 * （必须再tryOptimisticRead和validate之间获取好数据，否则数据可能会不一致了，试想如果过了validate再获取数据，这时数据可能被修改并且读操作也没有任何保护措施）
 */
public class StampedLockDemo {
    // 锁实例
    private static final StampedLock stampedLock = new StampedLock();

    private static volatile int count = 0;

    private static final Random r = new Random();

    /**
     * 乐观读锁读取计数, 减少了一次获取悲观读锁的消耗和避免了因为读锁导致写锁阻塞的问题
     * @return int
     */
    private static int read() {
        int r;
        // 尝试获取乐观读锁（1）
        long stamp = stampedLock.tryOptimisticRead();
        // 将全部变量拷贝到方法体栈内（2）
        r = count;
        // 检查在（1）获取到读锁票据后，锁有没有被其他写线程排它性抢占对应写锁（3）
        if (!stampedLock.validate(stamp)) {
            // 如果对应写锁被抢占则获取一个共享读锁（悲观获取）（4）
            stamp = stampedLock.readLock();
            try {
                // 将全部变量拷贝到方法体栈内（5）
                r = count;
            } finally {
                // 释放共享读锁（6）
                stampedLock.unlockRead(stamp);
            }
        }
        //没有被其他写线程排它性抢占对应写锁, 直接返回乐观读锁取到的值
        return r;
    }

    /**
     * 计数加 1
     */
    private static int add() {
        long stamp = stampedLock.writeLock();
        try {
            count++;
        } finally {
            stampedLock.unlockWrite(stamp);
        }
        return count;
    }

    public static void main(String[] args) {
        //启动 5个线程写计数,95 个线程读计数,
        for (int i = 0; i < 100; i++) {
            if (i % 20 == 0) {  //写线程
                new Thread(() -> {
                    try {
                        Thread.sleep(r.nextInt(10));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " 计数新增 1 :" + add());
                },("writeThread"+i/20)).start();
            } else {        //读线程
                new Thread(() -> {
                    try {
                        Thread.sleep(r.nextInt(10));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " 读计数:" + read());
                },("readThread"+i%20)).start();
            }
        }
    }

}

