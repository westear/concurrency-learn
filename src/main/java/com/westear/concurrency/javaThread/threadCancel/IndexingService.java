package com.westear.concurrency.javaThread.threadCancel;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *  通过“毒丸”对象来关闭服务
 *
 *  毒丸”是指一个放在队列上的对象，其含义是：“当得到这个对象时，立即停止。”
 *  在FIFO 队列中，“毒丸”对象将确保消费者在关闭之前首先完成队列中的所有工作，在提交“毒丸”对象之前提交的所有工作都会被处理，
 *  而生产者在提交了“毒丸”对象后，将不会在提交任何工作。
 *  在下面的程序清单中给出了一个单生产者——单消费者的桌面搜索示例，使用了“毒丸”对象来关闭服务。
 */
public class IndexingService {
    private static final int CAPACITY = 1000;
    //毒丸
    private static final File POISON = new File("");

    private final IndexerThread consumer = new IndexerThread();
    private final CrawlerThread producer = new CrawlerThread();

    private final BlockingQueue<File> queue;
    //private final FileFilter fileFilter;
    private final File root;

    public IndexingService(File root) {
        this.root = root;
        this.queue = new LinkedBlockingQueue<File>(CAPACITY);

    }

    private boolean alreadyIndexed(File f) {
        return false;
    }

    // IndexingService的生产者线程
    class CrawlerThread extends Thread {
        public void run() {
            try {
                crawl(root);
            } catch (InterruptedException e) { /* 发生异常 */
            } finally {
                while (true) {
                    try {
                        System.out.println("放入“毒丸”");
                        queue.put(POISON);//放入毒丸
                        break;
                    } catch (InterruptedException e1) { /* 重试 */
                    }
                }
            }
        }

        private void crawl(File root) throws InterruptedException {
            File[] entries = root.listFiles();
            if (entries != null) {
                for (File entry : entries) {
                    if (entry.isDirectory())
                        crawl(entry);
                    else if (!alreadyIndexed(entry)){
                        System.out.println("放入生产者队列文件："+entry.getName()+" 来自线程："+Thread.currentThread().getName());
                        queue.put(entry);
                    }
                }
            }
        }
    }

    // IndexingService的消费者线程
    class IndexerThread extends Thread {
        public void run() {
            try {
                while (true) {
                    File file = queue.take();
                    // 遇到毒丸，终止
                    if (file == POISON){
                        System.out.println("遇到“毒丸”，终止");
                        break;
                    }
                    else
                        indexFile(file);
                }
            } catch (InterruptedException consumed) {
            }
        }

        public void indexFile(File file) {
            System.out.println("消费者取出文件："+file.getName()+" 来自线程："+Thread.currentThread().getName());
            /* ... */
        };
    }

    public void start() {
        producer.start();
        consumer.start();
    }

    public void stop() {
        producer.interrupt();
    }

    public void awaitTermination() throws InterruptedException {
        consumer.join();
    }
}
