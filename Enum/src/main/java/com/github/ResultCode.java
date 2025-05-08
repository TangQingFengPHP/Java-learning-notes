package com.github;

/**
 * 枚举定义字段和方法
 */
public enum ResultCode {
    SUCCESS(200, "Success"),
    ERROR(500, "Error");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 根据code值反查枚举
     */
    public static ResultCode fromCode(int code) {
        for (ResultCode result : ResultCode.values()) {
            if (result.getCode() == code) {
                return result;
            }
        }
        throw new IllegalArgumentException("No such ResultCode");
    }
}
