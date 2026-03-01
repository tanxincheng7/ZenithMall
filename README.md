# Zenith Mall

一个基于 Spring Boot 的单体多模块电商平台项目。

## 项目简介

Zenith Mall 是一套功能完善的电商系统，采用单体多模块架构设计，便于快速开发和部署。项目采用模块化设计，各业务模块职责清晰，便于维护和扩展。

## 技术栈

- **Java**: JDK 1.8
- **Spring Boot**: 2.7.18
- **数据库**: MySQL 8.0.29
- **ORM框架**: MyBatis Plus 3.5.3
- **JWT**: jjwt 0.11.5
- **工具库**: Hutool 5.8.25
- **构建工具**: Maven
- **容器化**: Docker & Docker Compose

## 项目结构

```
ZenithMall
├── zenith-common              # 公共模块（统一响应、JWT工具、异常处理等）
├── zenith-infrastructure      # 基础设施模块
├── zenith-start               # 启动模块（主启动类、全局配置）✨
├── zenith-api                 # API 接口模块
│   ├── zenith-admin-api       # 管理后台 API
│   └── zenith-portal-api      # 门户前台 API
├── zenith-modules             # 业务模块
│   ├── zenith-cart            # 购物车模块
│   ├── zenith-member          # 会员模块 ✅
│   ├── zenith-order           # 订单模块
│   ├── zenith-payment         # 支付模块
│   ├── zenith-product         # 商品模块
│   ├── zenith-promotion       # 促销模块
│   └── zenith-search          # 搜索模块
├── deploy                     # 部署配置
│   └── docker-compose         # Docker Compose 配置
├── sql                        # 数据库脚本
│   └── init                   # 初始化脚本（表结构+测试数据）
└── .github                    # GitHub Actions 配置
```

## 模块说明

### 核心模块

- **zenith-common**: 公共组件，包含通用工具类、常量定义、统一响应、JWT工具、异常处理等
- **zenith-infrastructure**: 基础设施，包含数据库配置、缓存配置等
- **zenith-start**: 📌 **应用启动模块**，包含主启动类和全局配置（单体应用入口）

### 业务模块

- **zenith-cart**: 购物车管理，支持添加、删除、修改商品等操作
- **zenith-member**: ✅ 会员管理，包含用户注册、登录、个人信息管理
- **zenith-order**: 订单管理，处理订单创建、支付、发货、退款等流程
- **zenith-payment**: 支付服务，对接第三方支付平台
- **zenith-product**: 商品管理，包含商品分类、SKU、库存管理
- **zenith-promotion**: 促销活动，包含优惠券、满减、秒杀等
- **zenith-search**: 搜索服务，提供商品搜索功能

### API 模块

- **zenith-admin-api**: 管理后台接口，供后台管理系统调用
- **zenith-portal-api**: 门户前台接口，供前端用户使用

## 架构说明

本项目采用 **单体多模块架构**（非微服务）：

- 所有业务模块共享同一个 JVM 进程
- 统一由 `zenith-start` 模块启动
- 共享同一个数据库连接池
- 模块间通过直接方法调用通信

**优势**：
- 开发简单，部署方便
- 适合中小型电商项目
- 避免微服务架构的复杂性

## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.6+
- Docker & Docker Compose（用于基础设施）

### 1. 启动基础设施

```bash
cd deploy/docker-compose
docker-compose -f infrastructure.yml up -d mysql redis
```

详细说明请查看：[基础设施部署文档](deploy/docker-compose/README.md)

### 2. 编译项目

```bash
# 在项目根目录执行
mvn clean install -DskipTests
```

### 3. 启动应用

```bash
cd zenith-start
mvn spring-boot:run
```

或在 IDE 中运行主启动类：
```
com.tanxincheng.zenith.ZenithMallApplication
```

### 4. 访问应用

- 应用地址: http://localhost:8080
- 会员模块 API: http://localhost:8080/v1/users/*

### 5. 测试接口

查看各模块的 `tests.http` 文件或 API 文档。

## 当前进度

### ✅ 已完成

- [x] 数据库设计（7张核心表）
- [x] 公共模块（统一响应、JWT、异常处理）
- [x] 会员模块（注册、登录、获取用户信息）
- [x] Docker Compose 配置（MySQL、Redis、Nginx等）

### 🚧 进行中

- [ ] 商品模块
- [ ] 购物车模块
- [ ] 订单模块
- [ ] 支付模块

### 📋 计划中

- [ ] 促销模块
- [ ] 搜索模块
- [ ] 管理后台

## API 文档

| 模块 | 文档位置 |
|------|----------|
| 会员模块 | [zenith-modules/zenith-member/API.md](zenith-modules/zenith-member/API.md) |

## 开发指南

### 添加新模块

1. 在 `zenith-modules` 下创建新模块
2. 在 `zenith-start/pom.xml` 中添加依赖
3. 创建 Controller、Service、Mapper 等组件
4. 主启动类会自动扫描新模块的组件

### 代码规范

项目遵循阿里巴巴 Java 开发规范，建议使用 IntelliJ IDEA 进行开发。

### 分支管理

- **main**: 生产环境分支
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
feat(member): 添加用户注册登录功能
fix(payment): 修复支付回调处理bug
docs(readme): 更新项目说明文档
```

## 数据库

### 初始化

项目使用 Docker Compose 自动初始化数据库：

```bash
docker-compose -f deploy/docker-compose/infrastructure.yml up -d mysql
```

初始化脚本位置：`sql/init/`
- `01-schema.sql` - 表结构定义
- `02-test-data.sql` - 测试数据

### 测试账号

| 用户名 | 密码 | 说明 |
|--------|------|------|
| admin | 123456 | 管理员 |
| user01 | 123456 | 测试用户1 |
| user02 | 123456 | 测试用户2 |

## CI/CD

项目使用 GitHub Actions 进行持续集成，配置文件位于 `.github/workflows/`。

每次提交代码到 develop 分支会自动触发构建和测试。

## 常见问题

### 1. 端口冲突

修改 `zenith-start/src/main/resources/application.yml` 中的 `server.port`。

### 2. 数据库连接失败

检查 MySQL 容器是否正常运行：
```bash
docker ps | grep mysql
docker logs zenith-mall-mysql
```

### 3. 找不到主启动类

主启动类位于 `zenith-start` 模块：
```
com.tanxincheng.zenith.ZenithMallApplication
```

### 4. 模块依赖问题

确保先执行 `mvn clean install` 安装所有模块到本地仓库。

## 许可证


## 联系方式

- 作者: tanxincheng

## 致谢

感谢所有贡献者对本项目的支持。
