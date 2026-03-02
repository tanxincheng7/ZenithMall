-- =====================================================
-- 测试数据库初始化脚本
-- 用于运行用户模块的集成测试
-- =====================================================

-- 创建测试数据库
CREATE DATABASE IF NOT EXISTS `zenith_mall_test`
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_unicode_ci;

USE `zenith_mall_test`;

-- =====================================================
-- 用户表
-- =====================================================
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(20) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码（加密）',
    `phone` VARCHAR(11) NOT NULL COMMENT '手机号',
    `email` VARCHAR(50) NOT NULL COMMENT '邮箱',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`),
    KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- =====================================================
-- 测试数据（可选）
-- 注意：集成测试会自己创建和清理数据，这里的测试数据主要用于手动测试
-- =====================================================

-- 插入一个测试用户（密码：test123456）
INSERT INTO `user` (`username`, `password`, `phone`, `email`, `status`)
VALUES ('manual_test', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '13800000000', 'manual@test.com', 1);

-- =====================================================
-- 验证脚本
-- =====================================================

-- 查看表结构
DESC `user`;

-- 查看测试数据
SELECT * FROM `user`;

-- =====================================================
-- 清理脚本（测试完成后可使用）
-- =====================================================

-- DROP DATABASE IF EXISTS `zenith_mall_test`;
