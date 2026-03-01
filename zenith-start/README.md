# ZenithMall 启动模块

## 模块说明

`zenith-start` 是 ZenithMall 项目的启动模块，包含主启动类和全局配置文件。

## 技术栈

- Spring Boot 2.7.18
- MyBatis Plus 3.5.3
- MySQL 8.0
- JDK 8

## 模块依赖

本模块依赖以下业务模块：

- zenith-common (公共模块)
- zenith-member (会员模块)
- zenith-product (商品模块)
- zenith-cart (购物车模块)
- zenith-order (订单模块)
- zenith-payment (支付模块)
- zenith-promotion (促销模块)
- zenith-search (搜索模块)

## 快速开始

### 1. 启动基础设施

```bash
cd deploy/docker-compose
docker-compose -f infrastructure.yml up -d
```

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

或者在 IDE 中直接运行：
```
com.tanxincheng.zenith.ZenithMallApplication
```

### 4. 访问应用

- 应用地址: http://localhost:8080
- 会员模块 API: http://localhost:8080/v1/users/*

## 配置说明

配置文件位置: `src/main/resources/application.yml`

### 关键配置项

```yaml
server:
  port: 8080                    # 服务端口

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/zenith_mall
    username: zenith
    password: zenith123456

mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true  # 驼峰命名转换
    log-impl: ...StdOutImpl            # SQL 日志
```

## API 路由

所有业务模块的 API 都通过统一的端口 8080 访问：

| 模块 | 路由前缀 | 说明 |
|------|----------|------|
| 会员模块 | /v1/users/* | 用户注册、登录、信息查询 |
| 商品模块 | /v1/products/* | 商品管理 |
| 购物车模块 | /v1/cart/* | 购物车操作 |
| 订单模块 | /v1/orders/* | 订单管理 |
| 支付模块 | /v1/payments/* | 支付接口 |

详细的 API 文档请查看各模块的 README.md。

## 项目结构

```
zenith-start/
├── src/main/java/com/tanxincheng/zenith/
│   └── ZenithMallApplication.java      # 主启动类
└── src/main/resources/
    └── application.yml                 # 全局配置文件
```

## 组件扫描

主启动类配置了组件扫描，会自动扫描所有模块的：

- Controller: `com.tanxincheng.zenith.*.controller`
- Service: `com.tanxincheng.zenith.*.service`
- Mapper: `com.tanxincheng.zenith.*.mapper`

## 常见问题

### 1. 端口冲突

如果 8080 端口被占用，修改 `application.yml` 中的 `server.port`。

### 2. 数据库连接失败

检查：
- MySQL 容器是否启动: `docker ps | grep mysql`
- 数据库配置是否正确
- 数据库是否已初始化

### 3. 模块依赖问题

确保先执行 `mvn clean install` 安装所有模块到本地仓库。

## 开发建议

1. 使用热部署：添加 spring-boot-devtools 依赖
2. 使用 Lombok：简化代码，提高开发效率
3. 查看日志：控制台输出 SQL 日志，方便调试
4. API 测试：使用各模块的 tests.http 文件

## 打包部署

```bash
# 打包
mvn clean package -DskipTests

# 运行
java -jar zenith-start/target/zenith-start-1.0-SNAPSHOT.jar
```

## 注意事项

- 本模块是单体应用的启动入口，不是微服务
- 所有业务模块共享同一个数据库和 JVM
- 生产环境建议修改默认配置（端口、密码等）
