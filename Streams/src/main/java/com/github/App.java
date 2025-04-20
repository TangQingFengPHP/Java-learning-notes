package com.github;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // 从集合中创建流
        List<String> list = Arrays.asList("A", "B", "C");
        Stream<String> stream1 = list.stream();
        //System.out.println(stream1.count());

        // 从数组中创建流
        Stream<Integer> stream2 = Arrays.stream(new Integer[]{1, 2, 3});
        System.out.println(stream2.count());

        // 使用Stream.of()创建流
        Stream<String> stream3 = Stream.of("D", "E", "F");
        //System.out.println(stream3.count());

        // 生成无限流
        Stream<Double> randoms = Stream.generate(Math::random).limit(5);
        System.out.println("无限流" + randoms.count());

        // 迭代生成
        Stream<Integer> stream4 = Stream.iterate(0, n -> n + 2).limit(10);
        System.out.println(stream4.count());

        // 合并流
        Stream<String> stream5 = Stream.concat(stream1, stream3);
        // 此处注意：以上的流打印后，这时不能再次调用使用了，否则会报错
        System.out.println(stream5.count());

        // 流操作
        List<String> result = list.stream()
                .filter(s -> s.startsWith("A"))
                .map(String::toLowerCase)
                .distinct()
                .collect(Collectors.toList());
        System.out.println(result);

        // 流统计
        long count = list.stream().filter(s -> s.length() > 3).count();
        System.out.println(count);

        // 字符串拼接
        String joined = list.stream().collect(Collectors.joining(", "));
        System.out.println(joined);

        // 自然排序
        list.stream().sorted().forEach(System.out::println);

        // 自定义排序
        list.stream()
                .sorted((a, b) -> a.length() - b.length())
                .forEach(System.out::println);

        // flatMap 扁平化
        List<String> lines = Arrays.asList("A B", "C D");
        List<String> words = lines.stream()
                .flatMap(line -> Arrays.stream(line.split(" ")))
                .collect(Collectors.toList());
        System.out.println(words);

        // 并行流
        list.parallelStream().forEach(System.out::println);

        // reduce 聚合
        int sum = Arrays.asList(1, 2, 3, 4).stream()
                .reduce(0, Integer::sum);
        // 或
        //int sum2 = Stream.of(1, 2, 3, 4).reduce(0, Integer::sum);
        System.out.println(sum);

        DoubleSummaryStatistics stats = Stream.of(1.0, 2.0, 3.0, 4.0, 5.0)
                .mapToDouble(Double::doubleValue)
                .summaryStatistics();
        System.out.println(stats.getAverage());
        System.out.println(stats.getMax());
        System.out.println(stats.getMin());
    }
}
