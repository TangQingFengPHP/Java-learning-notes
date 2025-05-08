package com.github;

/**
 * 枚举实现状态机
 */
public enum OrderState {
    CREATED{
        @Override
        public OrderState nextState() {
            return PAID;
        }
    },
    PAID {
        @Override
        public OrderState nextState() {
            return SHIPPED;
        }
    },
    SHIPPED {
        @Override
        public OrderState nextState() {
            throw new IllegalStateException("Order has already been shipped.");
        }
    };

    public abstract OrderState nextState();
}
