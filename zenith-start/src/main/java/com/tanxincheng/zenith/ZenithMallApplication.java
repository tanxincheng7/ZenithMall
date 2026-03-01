package com.tanxincheng.zenith;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ZenithMall 应用启动类
 * 
 * 单体多模块项目主入口
 */
@SpringBootApplication(scanBasePackages = "com.tanxincheng.zenith")
@MapperScan("com.tanxincheng.zenith.*.mapper")
public class ZenithMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZenithMallApplication.class, args);
        System.out.println("========================================");
        System.out.println("  ZenithMall 启动成功！");
        System.out.println("  访问地址: http://localhost:8080");
        System.out.println("  API 文档: 请查看各模块 README");
        System.out.println("========================================");
    }
}
