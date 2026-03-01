package com.tanxincheng.zenith.common.enums;

import lombok.Getter;

/**
 * 响应码枚举
 */
@Getter
public enum ResultCode {

    SUCCESS(200, "success"),
    FAIL(500, "fail"),

    // 用户相关错误码 1000-1999
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    USERNAME_OR_PASSWORD_ERROR(1003, "用户名或密码错误"),
    PHONE_ALREADY_EXISTS(1004, "手机号已被注册"),
    EMAIL_ALREADY_EXISTS(1005, "邮箱已被注册"),
    INVALID_TOKEN(1006, "Token 无效或已过期"),
    TOKEN_EXPIRED(1007, "Token 已过期"),

    // 参数校验错误 2000-2999
    INVALID_PARAM(2001, "参数校验失败"),
    USERNAME_TOO_SHORT(2002, "用户名长度至少4位"),
    PASSWORD_TOO_SHORT(2003, "密码长度至少6位"),
    INVALID_PHONE_FORMAT(2004, "手机号格式不正确"),
    INVALID_EMAIL_FORMAT(2005, "邮箱格式不正确"),

    // 系统错误 5000-5999
    SYSTEM_ERROR(5000, "系统错误"),
    DATABASE_ERROR(5001, "数据库错误"),
    NETWORK_ERROR(5002, "网络错误");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
