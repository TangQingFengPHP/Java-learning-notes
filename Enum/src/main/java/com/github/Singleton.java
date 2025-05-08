package com.github;

/**
 * 枚举实现单例模式
 */
public enum Singleton {
    INSTANCE;

    private int counter = 0;

    public void increment() {
        counter++;
    }

    public int getCount() {
        return counter;
    }
}
