package com.github;

import java.util.function.Supplier;

public class LazyLoader {
    public static void load(Supplier<HeavyObject> supplier, boolean needed) {
        if (needed) {
            HeavyObject obj = supplier.get();
            System.out.println("对象已创建");
        } else {
            System.out.println("未使用对象，无需创建");
        }
    }

    public static void main(String[] args) {
        load(HeavyObject::new, false); // HeavyObject 不会被创建
        load(HeavyObject::new, true);  // 会创建
    }
}
