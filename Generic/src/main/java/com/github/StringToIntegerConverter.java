package com.github;

/**
 * 字符串转整型转换器
 */
public class StringToIntegerConverter implements Converter<String, Integer> {
    @Override
    public Integer convert(String from) {
        return Integer.parseInt(from);
    }
}
