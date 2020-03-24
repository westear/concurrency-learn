package com.westear.concurrency.blockingQueueOrDeque;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * PriorityBlockingQueue : 一个支持优先级排序的无界阻塞队列
 * 对结果分析，每次添加一个元素，PriorityBlockingQueue中的person都会执行compareTo方法进行排序，
 * 但是只是把第一个元素排在首位，其他元素按照队列的一系列复杂算法排序。这就保障了每次获取到的元素都是经过排序的第一个元素。
 */
public class PriorityBlockingQueueDemo {

    class Person implements Comparable<Person> {

        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Person() {

        }

        public Person(int id, String name) {
            super();
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }

        @Override
        public int compareTo(@NotNull PriorityBlockingQueueDemo.Person o) {
            return Integer.compare(this.id, o.getId());
        }
    }

    public static void main(String[] args) {
        PriorityBlockingQueue<Person> pbq = new PriorityBlockingQueue<>();
        pbq.add(new PriorityBlockingQueueDemo().new Person(3,"person3"));
        System.err.println("容器为：" + pbq);
        pbq.add(new PriorityBlockingQueueDemo().new Person(2,"person2"));
        System.err.println("容器为：" + pbq);
        pbq.add(new PriorityBlockingQueueDemo().new Person(1,"person1"));
        System.err.println("容器为：" + pbq);
        pbq.add(new PriorityBlockingQueueDemo().new Person(4,"person4"));
        System.err.println("容器为：" + pbq);
    }
}
