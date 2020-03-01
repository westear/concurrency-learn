package com.westear.concurrency.javaThread.application.server;

import com.westear.concurrency.javaThread.application.threadPool.DefaultThreadPool;
import com.westear.concurrency.javaThread.application.threadPool.ThreadPool;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 在 SimpleHttpServer 与客户端建立了连接之后，并不会处理客户端的请求，而是将其包装成自定义的 HttpRequestHandler ，
 * 并交由线程池处理。在线程池中的 Worker 处理客户端请求的同时，SimpleHttpServer 能够继续完成后续客户端的连接建立，
 * 不会阻塞后续客户端的请求。
 */
public class SimpleHttpServer {
    //处理HttpRequest的线程池
    static ThreadPool<HttpRequestHandler> threadPool = new DefaultThreadPool<>(1);
    //SimpleHttpServer的根路径
    static String basePath;
    static ServerSocket serverSocket;
    //服务监听接口
    static int port = 8080;

    public static void setPort(int port) {
        SimpleHttpServer.port = port;
    }

    public static void setBasePath(String basePath) {
        if(basePath != null && new File(basePath).exists() && new File(basePath).isDirectory()) {
            SimpleHttpServer.basePath = basePath;
        }
    }

    //启动SimpleHttpServer
    public static void start() throws Exception {
        serverSocket = new ServerSocket(port);
        Socket socket = null;
        while ((socket = serverSocket.accept()) != null) {
            //接收一个客户端Socket, 生成一个HttpRequestHandler,放入线程池执行
            threadPool.execute(new HttpRequestHandler(socket));
        }
        serverSocket.close();
    }

    static class HttpRequestHandler implements Runnable {
        private Socket socket;

        public HttpRequestHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String line = null;
            BufferedReader br = null;
            BufferedReader reader = null;
            PrintWriter out = null;
            InputStream in = null;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String header = reader.readLine();
                //由相对路径计算出绝对路径
                String filePath = basePath + header.split(" ")[1];
                out = new PrintWriter(socket.getOutputStream());
                //如果请求资源的后缀为ijpg或者ico,则读取资源并输出
                if(filePath.endsWith("jpg") || filePath.endsWith("ico")) {
                    in = new FileInputStream(filePath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int i = 0;
                    while ((i = in.read()) != -1) {
                        baos.write(i);
                    }
                    byte[] array = baos.toByteArray();
                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: Molly");
                    out.println("Content-Type: image/jpeg");
                    out.println("Content-Length: " + array.length);
                    out.println(" ");
                    socket.getOutputStream().write(array,0,array.length);
                } else {
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
                    out = new PrintWriter(socket.getOutputStream());
                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: Molly");
                    out.println("Content-Type: text/html;charset=UTF-8");
                    out.println("");
                    while ((line = br.readLine()) != null) {
                        out.println(line);
                    }
                }
                out.flush();
            } catch (Exception ex) {
                if(out != null) {
                    out.println("HTTP/1.1 500");
                    out.println("");
                    out.flush();
                }
            } finally {
                close(br, in, reader, out, socket);
            }
        }
    }

    /**
     * 关闭流或者Socket等资源
     * @param closeables Closeable
     */
    private static void close(Closeable... closeables) {
        if(closeables != null) {
            for (Closeable closeable : closeables) {
                try {
                    closeable.close();
                } catch (Exception ex) {

                }
            }
        }
    }
}
