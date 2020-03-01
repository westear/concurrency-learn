package com.westear.concurrency.javaThread.application.connectionPool;

public class ConnectionImpl implements Connection {
    @Override
    public void commit() {
        System.out.println("invoke Connection's commit method");
    }

    @Override
    public void createStatement() {
        System.out.println(" createStatement ......");
    }
}
