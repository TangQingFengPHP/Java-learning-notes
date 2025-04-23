package com.github;

import java.util.List;

/**
 * 泛型方法
 */
public class Util {

    /**
     * 静态泛型方法，传入数组，泛型参数为T
     * 泛型静态方法必须声明 <T>
     * @param array
     * @param <T>
     */
    public static <T> void printArray(T[] array) {
        for (T element : array) {
            System.out.println(element);
        }
    }

    /**
     * 泛型方法，传入List<?>，泛型参数不确定，可以是任何类型
     * @param list
     */
    public static void printList(List<?> list) {
        for (Object element : list) {
            System.out.println(element);
        }
    }

    /**
     * 泛型方法，传入List<? extends Number>，泛型参数为Number的子类，可以是Integer、Double等等
     * @param list
     */
    public static void printNumbers(List<? extends Number> list) {
        // list.add(1.2); // 编译报错，只能读不能写
        for (Number element : list) {
            System.out.println(element);
        }
    }

    /**
     * 泛型方法，传入List<? super Integer>，泛型参数为Integer的父类，可以是Object、Number等等
     * @param list
     * @return
     */
    public static List<? super Integer> addNumbers(List<? super Integer> list) {
        list.add(3); // 编译通过
        return list;
    }

}
