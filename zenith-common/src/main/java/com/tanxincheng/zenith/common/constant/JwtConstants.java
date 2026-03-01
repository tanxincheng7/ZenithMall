package com.tanxincheng.zenith.common.constant;

/**
 * JWT 常量
 */
public class JwtConstants {

    /**
     * Token 请求头名称
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * Token 前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * Token 在请求头中的名称
     */
    public static final String TOKEN_HEADER_WITH_PREFIX = "Authorization";

    /**
     * JWT 密钥（生产环境应从配置文件读取）
     */
    public static final String SECRET = "zenith-mall-secret-key-2024-spring-boot-jwt";

    /**
     * Token 过期时间（7天）
     */
    public static final long EXPIRATION = 7 * 24 * 60 * 60 * 1000;

    /**
     * 用户ID在JWT中的key
     */
    public static final String CLAIM_USER_ID = "userId";

    /**
     * 用户名在JWT中的key
     */
    public static final String CLAIM_USERNAME = "username";
}
