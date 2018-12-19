package demo.falseMemoryShare;

/**
 * 一个伪共享示例，拓展了解，多线程间消息队列框架disruptor
 * @author Qinyunchan
 * @date Created in 5:19 PM 2018/12/19
 */


/**
 * Java的对象都有一个2个word的头部，一个word就是32位。
 * 第一个word存储对象的hashcode和一些特殊的位标志，如GC的分代年龄、偏向锁标记等，
 * 第二个word存储对象的指针地址，
 * 两个word是8*8=64位,所以是8个字节
 * 然后加上v和6个p变量，总共就是8个long的长度，也就是64字节,达到最小缓存行。
 */
class SharingLong{
    /**
     * v的值是volatile类型的，意味着CPU要保证v变量在不同线程之间的读写可见行。
     * 当CPU对v变量进行修改的时候会将数据立即回写至主存并将相应的缓存行置为失效。
     * 这样后续对v变量进行的读写操作都需要重新从内存中加载缓存行，这样就保证了其它线程读到的数据是最新的
     */
    volatile long v;
    /**
    下行代码：在性能上注释前后时间耗时差别高达5比1,使得v1,v2,v3,v4分别占用自己的缓存行，不发生写竞争
     */
    long p1,p2,p3,p4,p5,p6;
}

class LightThread extends Thread {
    SharingLong[] shares;
    int index;

    LightThread(SharingLong[] shares, int index){
        this.shares = shares;
        this.index = index;
    }

    @Override
    public void run(){
        for (int i = 0; i < 100000000L; i++){
            shares[index].v++;
        }
    }
}

public class FalseSharingTest {

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 1000000000L; i++) {
            Benchmark();
        }
    }

    public static void Benchmark() throws InterruptedException {
        //线程数=4
        int size = Runtime.getRuntime().availableProcessors();
        SharingLong[] shares = new SharingLong[size];
        for (int i = 0; i < size; i++){
            shares[i] = new SharingLong();
        }
        Thread[] threads = new Thread[size];
        for (int i = 0; i < size; i++){
            threads[i] = new LightThread(shares,i);
        }
        for (Thread t : threads){
            t.start();
        }
        long start = System.currentTimeMillis();
        for (Thread t : threads){
            t.join();
        }
        long end = System.currentTimeMillis();
        System.out.printf("total costs %dms\n", end - start);
    }
}
