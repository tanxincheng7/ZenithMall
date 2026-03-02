package com.tanxincheng.zenith.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CORS 跨域配置
 * <p>
 * 解决当 allowCredentials 为 true 时，allowedOrigins 不能使用 "*" 的问题
 * 使用 allowedOriginPatterns 代替 allowedOrigins
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 允许携带凭证（Cookie、Authorization 等）
        config.setAllowCredentials(true);

        // 使用 allowedOriginPatterns 代替 allowedOrigins
        // 这样可以匹配动态端口号，同时支持 allowCredentials
        config.addAllowedOriginPattern("*");

        // 允许所有请求头
        config.addAllowedHeader("*");

        // 允许所有请求方法（GET、POST、PUT、DELETE 等）
        config.addAllowedMethod("*");

        // 暴露的响应头
        config.addExposedHeader("Content-Disposition");
        config.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
