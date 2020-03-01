package com.westear.concurrency.javaThread.application.server;

public class SimpleHttpServerTest {

    public static void main(String[] args) throws Exception {
        SimpleHttpServer.setBasePath("D:\\IdeaProjects\\concurrency-learn\\src\\main\\java\\com\\westear\\concurrency\\javaThread\\application\\server");
        SimpleHttpServer.setPort(8080);
        SimpleHttpServer.start();
    }
}
