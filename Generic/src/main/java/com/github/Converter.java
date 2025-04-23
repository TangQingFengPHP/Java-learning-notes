package com.github;

/**
 * 泛型接口
 */
public interface Converter<F, T> {
    T convert(F from);
}
