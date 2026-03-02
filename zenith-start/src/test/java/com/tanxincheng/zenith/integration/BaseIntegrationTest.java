package com.tanxincheng.zenith.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * 集成测试基类
 * <p>
 * 提供通用的测试配置和工具方法，所有集成测试应该继承此类
 * <p>
 * 特点：
 * - 启动完整的 Spring Boot 应用上下文
 * - 使用真实数据库连接（测试数据库）
 * - 自动回滚事务，保持数据库清洁
 * - 提供 MockMvc 进行 Web 层测试
 */
@SpringBootTest(
        classes = com.tanxincheng.zenith.ZenithMallApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // 每个测试方法执行前的初始化
        // 子类可以覆盖此方法添加额外的初始化逻辑
    }

    /**
     * 生成唯一的测试数据
     *
     * @param prefix 前缀
     * @return 唯一的字符串
     */
    protected String generateUniqueValue(String prefix) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        // 用户名最多20字符，前缀控制在13个字符以内
        return prefix + timestamp.substring(timestamp.length() - 9);
    }

    /**
     * 生成唯一的手机号
     *
     * @return 唯一的手机号
     */
    protected String generateUniquePhone() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return "138" + timestamp.substring(timestamp.length() - 8);
    }

    /**
     * 生成唯一的邮箱
     *
     * @param prefix 邮箱前缀
     * @return 唯一的邮箱
     */
    protected String generateUniqueEmail(String prefix) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + timestamp.substring(timestamp.length() - 6) + "@test.com";
    }
}
