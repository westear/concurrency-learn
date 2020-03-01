package com.westear.concurrency.javaThread.application.connectionPool;

/**
 * 模拟数据库连接接口
 */
public interface Connection {

    void commit();

    void createStatement();
}
