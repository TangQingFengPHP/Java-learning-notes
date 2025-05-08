package com.github;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.EnumSet;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // 枚举基本使用
        Status status = Status.SUCCESS;
        System.out.println(status);
        System.out.println(status.getClass());
        System.out.println(status.name());
        System.out.println(status.ordinal());

        // 使用枚举中的方法
        System.out.println(ResultCode.SUCCESS.getCode());
        System.out.println(ResultCode.ERROR.getMessage());

        // 使用枚举中的策略模式的方法
        System.out.println(Operation.PLUS.apply(1, 2));
        System.out.println(Operation.MINUS.apply(1, 2));

        // 根据code值反查枚举
        System.out.println(ResultCode.fromCode(200));
        //System.out.println(ResultCode.fromCode(300));

        // 遍历枚举
        for (Status s : Status.values()) {
            System.out.println(s);
        }

        // 枚举实现接口
        System.out.println(Coffee.ESPRESSO.calculatePrice());

        // 枚举实现单例模式
        Singleton.INSTANCE.increment();
        System.out.println(Singleton.INSTANCE.getCount());

        // EnumSet的使用，高效储存枚举集合
        EnumSet<Status> enumSet = EnumSet.of(Status.SUCCESS, Status.FAILURE);
        System.out.println(enumSet);
        if (enumSet.contains(Status.SUCCESS)) {
            System.out.println("包含成功");
        }

        // EnumMap的使用，高效储存枚举和值之间的映射关系
        EnumMap<Status, String> map = new EnumMap<>(Status.class);
        map.put(Status.SUCCESS, "success");
        map.put(Status.FAILURE, "failure");
        System.out.println(map);
        System.out.println(map.get(Status.SUCCESS));

        // 枚举valueOf的使用，根据名称或值获取枚举实例
        System.out.println(Status.valueOf("SUCCESS"));
        //System.out.println(Status.valueOf("success"));

        // 枚举在switch语句中的使用
        switch (status) {
            case SUCCESS:
                System.out.println("成功");
                break;
            case FAILURE:
                System.out.println("失败");
                break;
            default:
                System.out.println("未知");
                break;
        }

        // 枚举实现状态机
        OrderState state = OrderState.CREATED.nextState();
        System.out.println(state);

        // 枚举反射操作
        Class<Status> statusClass = Status.class;
        if (statusClass.isEnum()) {
            for (Field field : statusClass.getDeclaredFields()) {
                if (field.isEnumConstant()) {
                    System.out.println("枚举常量：" + field.getName());
                }
            }
        }
    }

}
