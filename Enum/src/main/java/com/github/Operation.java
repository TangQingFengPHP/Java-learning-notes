package com.github;

/**
 * 枚举中定义抽象方法（策略模式）
 */
public enum Operation {
    PLUS {
        public int apply(int x, int y) {
            return x + y;
        }
    },
    MINUS {
        public int apply(int x, int y) {
            return x - y;
        }
    };

    public abstract int apply(int x, int y);
}
