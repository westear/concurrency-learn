package java_container.set;

import java.util.TreeSet;

/**
 * TreeSet的元素必须实现Comparable接口，实现排序规则。否则代码会抛出java.lang.ClassCastException异常。
 */
public class TreeSetDemo {

    public static void main(String[] args) {
        TreeSet<Person> treeSet = new TreeSet<>(new MyComparator());
        treeSet.add(new Person("Bob", 18));
        treeSet.add(new Person("apple", 19));
        treeSet.add(new Person("jack", 20));

        for (Person person : treeSet) {
            System.out.println(person);
        }

        System.out.println("=================");

        TreeSet<Person1> treeSet1 = new TreeSet<>();
        treeSet1.add(new Person1("Bob", 18));
        treeSet1.add(new Person1("apple", 19));
        treeSet1.add(new Person1("jack", 20));

        for (Person1 person1 : treeSet1) {
            System.out.println(person1);
        }
    }
}
