package com.westear.concurrency.javaThread.application.connectionPool;

import java.util.LinkedList;

/**
 * 使用超时等待模式来构造一个简单的数据库连接池
 */
public class ConnectionPool {
    private final LinkedList<Connection> pool = new LinkedList<>();
    public ConnectionPool(int initialSize) {
        if(initialSize > 0) {
            for (int i = 0; i < initialSize; i++) {
                pool.addLast(ConnectionDriver.createConnection());
            }
        }
    }

    public void releaseConnection(Connection connection) {
        if(connection != null) {
            synchronized (pool) {
                //连接释放后需要进行通知，这样其他消费者能够感知到连接池中已经归还了一个连接
                pool.addLast(connection);
                pool.notifyAll();
            }
        }
    }

    //在mills内无法获取到连接，将会返回null
    public Connection fetchConnection(long mills) throws InterruptedException {
        synchronized (pool) {
            //完全超时
            if(mills <= 0) {
                while (pool.isEmpty()) {
                    pool.wait();
                }
                return pool.removeFirst();
            }else {
                long future = System.currentTimeMillis() +mills;
                long remaining = mills;
                //线程池等于空，且剩余等待时间大于0，则进入循环
                while (pool.isEmpty() && remaining > 0) {
                    //该线程在此行代码阻塞，阻塞时长=剩余等待时间，让出锁资源，让出cpu
                    pool.wait(remaining);
                    //假设该线程在此获得cpu执行时间片，则从此行代码往下执行，计算并更新剩余等待时间
                    remaining = future - System.currentTimeMillis();
                }
                Connection result = null;
                if(!pool.isEmpty()) {
                    result = pool.removeFirst();
                }
                return result;
            }
        }
    }
}
