# Zenith Mall

一个基于 Spring Boot + Spring Cloud 的微服务电商平台项目。

## 项目简介

Zenith Mall 是一套功能完善的电商系统，采用微服务架构设计，支持高并发、高可用的电商业务场景。项目采用模块化设计，各业务模块独立部署，便于扩展和维护。

## 技术栈

- **Java**: JDK 1.8
- **Spring Boot**: 2.7.18
- **Spring Cloud**: 2021.0.9
- **数据库**: MySQL 8.0.29
- **ORM框架**: MyBatis 3.5.10
- **工具库**: Hutool 5.8.25
- **构建工具**: Maven

## 项目结构

```
ZenithMall
├── zenith-common              # 公共模块
├── zenith-infrastructure      # 基础设施模块
├── zenith-start              # 启动模块
├── zenith-api                # API 接口模块
│   ├── zenith-admin-api      # 管理后台 API
│   └── zenith-portal-api     # 门户前台 API
├── zenith-modules            # 业务模块
│   ├── zenith-cart           # 购物车服务
│   ├── zenith-member         # 会员服务
│   ├── zenith-order          # 订单服务
│   ├── zenith-payment        # 支付服务
│   ├── zenith-product        # 商品服务
│   ├── zenith-promotion      # 促销服务
│   └── zenith-search         # 搜索服务
├── document                  # 项目文档
│   ├── api                   # API 文档
│   ├── architecture          # 架构设计文档
│   └── sql                   # 数据库文档
├── sql                       # 数据库脚本
│   ├── init                  # 初始化脚本
│   └── update                # 更新脚本
└── .github                   # GitHub Actions 配置
```

## 模块说明

### 核心模块

- **zenith-common**: 公共组件，包含通用工具类、常量定义等
- **zenith-infrastructure**: 基础设施，包含数据库配置、缓存配置等
- **zenith-start**: 应用启动模块，包含主启动类

### 业务模块

- **zenith-cart**: 购物车管理，支持添加、删除、修改商品等操作
- **zenith-member**: 会员管理，包含用户注册、登录、个人信息管理
- **zenith-order**: 订单管理，处理订单创建、支付、发货、退款等流程
- **zenith-payment**: 支付服务，对接第三方支付平台
- **zenith-product**: 商品管理，包含商品分类、SKU、库存管理
- **zenith-promotion**: 促销活动，包含优惠券、满减、秒杀等
- **zenith-search**: 搜索服务，提供商品搜索功能

### API 模块

- **zenith-admin-api**: 管理后台接口，供后台管理系统调用
- **zenith-portal-api**: 门户前台接口，供前端用户使用

## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis (可选，用于缓存)

### 数据库初始化

```bash
# 执行初始化脚本
mysql -u root -p < sql/init/init.sql
```

### 编译项目

```bash
# 克隆项目
git clone https://github.com/your-username/ZenithMall.git
cd ZenithMall

# 编译打包
mvn clean package -DskipTests
```

### 运行项目

```bash
# 启动应用
java -jar zenith-start/target/zenith-start-1.0-SNAPSHOT.jar
```

## 开发指南

### 代码规范

项目遵循阿里巴巴 Java 开发规范，建议使用 IDEA 进行开发。

### 分支管理

- **master**: 生产环境分支
- **develop**: 开发分支
- **feature/***: 功能分支
- **fix/***: 修复分支

### 提交规范

提交信息格式：`type(scope): subject`

- type: feat, fix, docs, style, refactor, test, chore
- scope: 影响的模块
- subject: 简短描述

示例：
```
feat(order): 添加订单取消功能
fix(payment): 修复支付回调处理bug
docs(readme): 更新项目说明文档
```

## CI/CD

项目使用 GitHub Actions 进行持续集成，配置文件位于 `.github/workflows/maven-ci.yml`。

每次提交代码到 develop 分支会自动触发构建和测试。

## 文档

详细文档请查看 `document` 目录：
- API 文档: `document/api/`
- 架构设计: `document/architecture/`
- 数据库设计: `document/sql/`

## 许可证



## 联系方式

- 作者: tanxincheng
- 邮箱: your-email@example.com

## 致谢

感谢所有贡献者对本项目的支持。
