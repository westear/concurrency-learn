package com.westear.concurrency.javaThread.threadCancel;

import javax.annotation.concurrent.GuardedBy;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

/**
 * 实现了CancellableTask,并定义了Future.cancel来关闭socket和调用super.cancel。
 * 如果SocketUsingTask通过其自己的Future来取消，那么底层的socket将被关闭时确保响应取消操作，而且还能调用可阻塞的socket I/O方法
 */
public class SocketUsingTask<T> implements CancellableTask<T> {

    @GuardedBy("this") private Socket socket;

    protected synchronized void setSocket(Socket s) {
        socket = s;
    }

    public synchronized void cancel() {
        try {
            if (socket != null)
                socket.close();
        } catch (IOException ignored) {
        }
    }

    public RunnableFuture<T> newTask() {
        return new FutureTask<T>(this) {
            public boolean cancel(boolean mayInterruptIfRunning) {
                try {
                    SocketUsingTask.this.cancel();
                } finally {
                    return super.cancel(mayInterruptIfRunning);
                }
            }
        };
    }

    @Override
    public T call() throws Exception {
        return null;
    }
}
