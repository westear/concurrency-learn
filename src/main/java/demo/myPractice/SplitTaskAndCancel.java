package demo.myPractice;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 有一个总任务，分解为子任务 A1,A1,A3 ....,任何一个子任务失败后要快速取消所有任务
 */
public class SplitTaskAndCancel {

    private static volatile boolean cancelled = false;
    public static List<FutureTask<Integer>> futureTaskList = null;

    public static void main(String[] args) throws InterruptedException {
        doTask(5);
//        TimeUnit.SECONDS.sleep(1);
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            executor.execute(() -> {
                String name = Thread.currentThread().getName();
                int index = Integer.parseInt(name.substring(name.length()-1));
                FutureTask<Integer> futureTask = futureTaskList.get(index-1);
                try {
                    System.out.println("线程["+futureTask.get()+"]执行完毕");
                } catch (InterruptedException e) {
                    System.err.println(Thread.currentThread().getName()+" 响应中断");
                } catch (CancellationException e) {
                    System.err.println(Thread.currentThread().getName()+" 被取消");
                } catch (ExecutionException e) {
                    System.err.println(Thread.currentThread().getName()+" 执行出错");
                }finally {
                    cancel(futureTaskList);
                    executor.shutdown();
                }
            });
        }
//        if(futureTaskList != null && futureTaskList.size() > 0)
//            cancel(futureTaskList);
    }

    public static void doTask(int taskCount) {
        ExecutorService executor = Executors.newFixedThreadPool(taskCount);
        futureTaskList = new ArrayList<>(taskCount);
        for (int i = 0; i < taskCount; i++) {
            FutureTask<Integer> futureTask = new FutureTask<>(new Task(i));
            futureTaskList.add(futureTask);
            executor.submit(futureTask);
        }
        executor.shutdown();
    }

    public static void cancel(List<FutureTask<Integer>> futureTaskList) {
        for (FutureTask<Integer> futureTask : futureTaskList) {
            futureTask.cancel(true);
        }
    }


    private static class Task implements Callable<Integer> {

        int index;

        Task(int index) {
            this.index = index;
        }

        @Override
        public Integer call() throws Exception {
            if (index != 2) {
                TimeUnit.SECONDS.sleep(2);
            }
            if(index == 2) {
                //模拟线程因故发生中断
//                Thread.currentThread().interrupt(); //不会立即中断线程
                throw new RuntimeException("线程执行发生异常");
            }
            if(!cancelled) {
                String name = Thread.currentThread().getName();
                String index = name.substring(name.length()-1);
                return Integer.valueOf(index);
            }
            return null;
        }
    }

}
