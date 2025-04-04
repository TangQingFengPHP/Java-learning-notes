package com.github;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class App
{
    public static void main( String[] args ) throws ExecutionException, InterruptedException {
        // Runnable 无参数、无返回值
        // 传统写法
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello Runnable!");
            }
        };
        new Thread(r1).start();

        // new Runnable() 不是直接实例化接口，而是创建了Runnable接口的匿名实现类，等价于
        // class MyRunnable implements Runnable {
        //    @Override
        //    public void run() {
        //        System.out.println("Running...");
        //    }
        //  }
        //
        // Runnable task = new MyRunnable();
        // task.run();

        // Lambda 表达式写法
        Runnable r2 = () -> System.out.println("Hello Lambda!");
        new Thread(r2).start();

        // 进一步简化
        new Thread(() -> System.out.println("Hello Lambda!")).start();

        // Comparator<T>（多个参数，有返回值）
        // 传统写法
        Comparator<Integer> c1 = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        };
        System.out.println(c1.compare(1, 2));

        // Lambda 表达式写法
        Comparator<Integer> c2 = (o1, o2) -> o1 - o2;
        System.out.println(c2.compare(1, 2));

        // 进一步简化成方法引用
        Comparator<Integer> c3 = Integer::compare;
        System.out.println(c3.compare(1, 2));

        // Consumer<T>（消费型接口）
        // 传统写法
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };
        consumer.accept("Hello Consumer!");

        // Lambda 表达式写法
        Consumer<String> consumer2 = s -> System.out.println(s);
        consumer2.accept("Hello Consumer!");

        // 进一步简化成方法引用
        Consumer<String> consumer3 = System.out::println;
        consumer3.accept("Hello Consumer!");

        // Function<T, R>（转换型接口）
        // 传统写法
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return s.length();
            }
        };
        System.out.println(function.apply("Hello Function!"));

        // Lambda 表达式写法
        Function<String, Integer> function2 = s -> s.length();
        System.out.println(function2.apply("Hello Function!"));

        // 进一步简化成方法引用
        Function<String, Integer> function3 = String::length;
        System.out.println(function3.apply("Hello Function!"));

        // Predicate<T>（断言型接口）
        // 传统写法
        Predicate<Integer> predicate = new Predicate<Integer>() {
            @Override
            public boolean test(Integer num) {
                return num > 10;
            }
        };
        System.out.println(predicate.test(15));

        // Lambda 表达式写法
        Predicate<Integer> predicate2 = num -> num > 10;
        System.out.println(predicate2.test(15));

        // 方法引用示例
        List<String> list = Arrays.asList("a", "b", "c");
        list.forEach(System.out::println);

        // Stream API 与 Lambda
        // Lambda 常配合 Stream API 处理集合：
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        List<String> filteredNames = names.stream()
                .filter(name -> name.length() > 3)
                .map(String::toUpperCase) // 这里的map需要的是Function接口，String::toUpperCase是未绑定的实例方法引用，需要一个String参数，等同于s -> s.toUpperCase()
                .collect(Collectors.toList());
        System.out.println(filteredNames);

        // 使用自定义的Lambda
        MyFunction add = (a, b) -> a + b;
        System.out.println(add.apply(1, 2));

        // 进一步简化为方法引用
        MyFunction add2 = Integer::sum;
        System.out.println(add2.apply(1, 2));

        // 为什么能使用方法引用呢，原因是方法引用是一种语法糖，它允许我们使用已有的方法或构造函数的名称来表示一个函数。
        // 这使得代码更简洁，更易于阅读和理解。
        // 上面的(a, b) -> a + b，函数签名和函数体的逻辑与Integer::sum完全一致，所以就可以使用Integer::sum来表示。

        // 静态方法引用示例
        Function<String, Integer> parseInt = Integer::parseInt;
        // 等同于Function<String, Integer> parseInt = (s) -> Integer.parseInt(s);
        System.out.println(parseInt.apply("123"));

        // 实例方法引用
        String str = "hello";
        Supplier<String> toUpper = str::toUpperCase;
        // 等同于Supplier<String> toUpper = () -> str.toUpperCase();
        System.out.println(toUpper.get());

        // 构造方法引用
        Supplier<ArrayList<String>> listSupplier = ArrayList::new;
        // 等同于Supplier<ArrayList<String>> listSupplier = () -> new ArrayList<>();
        ArrayList<String> list2 = listSupplier.get();
        System.out.println(list2);

        // 应用场景
        Function<Integer, int[]> arrayCreator = int[]::new;
        // 等同于Function<Integer, int[]> arrayCreator = (size) -> new int[size];
        int[] arr = arrayCreator.apply(5);
        System.out.println(Arrays.toString(arr));

        // 方法引用用作方法参数
        printSomething(() -> "Hello World");
        printSomething("Hello"::toUpperCase); // 这里的toUpperCase是已绑定的实例方法引用，不需要参数，等同于() -> "Hello".toUpperCase()

        // Callable<T> 示例
        ExecutorService executor = Executors.newFixedThreadPool(2);
        // 提交 Callable 任何
        Callable<Integer> task = () -> {
            Thread.sleep(2000);
            return 42;
        };

        Future<Integer> future = executor.submit(task);

        // 执行其他任务
        System.out.println("正在执行其他任务...");

        // 获取返回结果
        Integer result = future.get();
        System.out.println("任务结果：" + result);

        // 关闭线程池
        executor.shutdown();

    }

    public static void printSomething(Supplier<String> supplier) {
        System.out.println(supplier.get());
    }
}
