package com.github;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // 泛型类的基本使用
        Box<String> stringBox = new Box<>();
        stringBox.set("Hello World!");
        System.out.println(stringBox.get());

        // 泛型方法的基本使用
        String[] arr = {"A", "B", "C"};
        Util.printArray(arr);

        // 泛型接口基本使用
        StringToIntegerConverter converter = new StringToIntegerConverter();
        System.out.println(converter.convert("123"));

        // ?通配符基本使用
        List<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("World");
        Util.printList(list);

        // ? extends T 上界通配符基本使用
        // 协变
        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        Util.printNumbers(list2);

        // ? super T 下界通配符基本使用
        // 逆变
        List<Number> list3 = new ArrayList<>();
        list3.add(1);
        list3.add(2.0);
        List<? super Integer> list4 = Util.addNumbers(list3);
        Object obj = list4.get(0);
        System.out.println("obj: " + obj);
        System.out.println(list4.getClass().getTypeName());
        System.out.println(Util.addNumbers(list3));

        // 类型擦除
        List<String> list5 = new ArrayList<>();
        List<Integer> list6 = new ArrayList<>();
        System.out.println(list5.getClass());
        System.out.println(list6.getClass());
        System.out.println(list5.getClass() == list6.getClass());

        // 泛型数组不支持
        //List<String>[] lists = new List<String>[10];
    }
}
