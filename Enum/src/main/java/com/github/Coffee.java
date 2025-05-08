package com.github;

/**
 * 枚举实现接口
 */
public enum Coffee implements PriceCalculator {
    ESPRESSO(2.5){
        @Override
        public double calculatePrice() {
            return basePrice * 1.2;
        }
    },
    LATTE(3.5){
        @Override
        public double calculatePrice() {
            return basePrice * 1.5;
        }
    };

    // 这里的字段必须为protected，否则在子类中无法访问
    protected final double basePrice;
    Coffee(double basePrice) {
        this.basePrice = basePrice;
    }
}
