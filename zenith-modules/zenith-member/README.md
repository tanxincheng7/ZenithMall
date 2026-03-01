# 会员模块

## 模块说明

会员模块（zenith-member）提供用户注册、登录、信息查询等功能。

## 技术栈

- Spring Boot 2.7.18
- MyBatis Plus 3.5.3
- JWT (jjwt 0.11.5)
- Hutool 5.8.25

## 功能特性

- ✅ 用户注册（用户名、密码、手机号、邮箱）
- ✅ 用户登录（支持用户名/手机号登录）
- ✅ JWT Token 认证
- ✅ 密码 BCrypt 加密
- ✅ 参数校验
- ✅ 全局异常处理
- ✅ 统一响应格式

## 快速开始

### 1. 启动基础设施

```bash
cd deploy/docker-compose
docker-compose -f infrastructure.yml up -d mysql
```

### 2. 编译项目

```bash
# 在项目根目录执行
mvn clean install -DskipTests
```

### 3. 启动应用

**注意**：本项目采用单体多模块架构，统一由 `zenith-start` 模块启动。

```bash
cd zenith-start
mvn spring-boot:run
```

或在 IDE 中运行主启动类：
```
com.tanxincheng.zenith.ZenithMallApplication
```

### 4. 访问服务

- 应用地址: http://localhost:8080
- 会员模块 API: http://localhost:8080/v1/users/*

## 配置说明

会员模块的配置统一由 `zenith-start/src/main/resources/application.yml` 管理。

关键配置项：
- 服务端口：8080（在 zenith-start 中配置）
- 数据库：zenith_mall（已自动初始化）
- JWT 过期时间：7天

## API 接口

详细 API 文档请查看：[API.md](./API.md)

### 接口列表

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 用户注册 | POST | /v1/users/register | 注册新用户 |
| 用户登录 | POST | /v1/users/login | 用户登录 |
| 获取用户信息 | GET | /v1/users/info | 获取当前用户信息（需认证）|

## 项目结构

```
zenith-member/
├── src/main/java/com/tanxincheng/zenith/member/
│   ├── config/
│   │   └── MyBatisPlusConfig.java      # MyBatis Plus 配置
│   ├── controller/
│   │   └── UserController.java         # 用户控制器
│   ├── dto/
│   │   ├── UserRegisterRequest.java    # 注册请求 DTO
│   │   ├── UserLoginRequest.java       # 登录请求 DTO
│   │   ├── UserLoginResponse.java      # 登录响应 DTO
│   │   └── UserInfoResponse.java       # 用户信息响应 DTO
│   ├── entity/
│   │   └── User.java                   # 用户实体
│   ├── mapper/
│   │   └── UserMapper.java             # 用户 Mapper
│   └── service/
│       ├── UserService.java            # 用户服务接口
│       └── impl/
│           └── UserServiceImpl.java    # 用户服务实现
├── API.md                              # API 文档
├── tests.http                          # HTTP 测试文件
└── README.md                           # 本文档
```

## 测试方法

### 方法一：使用 tests.http 文件

1. 使用 IntelliJ IDEA 的 REST Client 插件
2. 打开 `tests.http` 文件
3. 点击请求旁边的绿色执行按钮

### 方法二：使用 curl

```bash
# 注册用户
curl -X POST http://localhost:8080/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456","phone":"13800138000","email":"test@example.com"}'

# 登录
curl -X POST http://localhost:8080/v1/users/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456"}'

# 获取用户信息（替换 TOKEN）
curl -X GET http://localhost:8080/v1/users/info \
  -H "Authorization: Bearer TOKEN"
```

### 方法三：使用 Postman

手动创建请求，参考 API.md 文档。

## 依赖模块

- zenith-common: 公共模块（统一响应、JWT工具、异常处理等）

## 启动架构

```
┌─────────────────────────────────────┐
│     zenith-start (启动模块)          │
│  - ZenithMallApplication (主启动类)  │
│  - application.yml (全局配置)        │
└─────────────────────────────────────┘
              │
              ├──────► zenith-member (会员模块)
              ├──────► zenith-product (商品模块)
              ├──────► zenith-cart (购物车模块)
              ├──────► zenith-order (订单模块)
              ├──────► zenith-payment (支付模块)
              ├──────► zenith-promotion (促销模块)
              └──────► zenith-search (搜索模块)
```

## 注意事项

1. 首次启动前请确保数据库已初始化
2. JWT Token 有效期为 7 天
3. 密码长度至少 6 位，用户名长度至少 4 位
4. 生产环境请修改 JWT 密钥和数据库密码
5. 统一从 zenith-start 模块启动应用

## 常见问题

### 1. 数据库连接失败

检查数据库是否启动：
```bash
docker ps | grep mysql
docker logs zenith-mall-mysql
```

### 2. 找不到主启动类

会员模块没有独立的启动类，请运行 `zenith-start` 模块的主启动类：
```
com.tanxincheng.zenith.ZenithMallApplication
```

### 3. 端口被占用

修改 `zenith-start/src/main/resources/application.yml` 中的 `server.port`。

### 4. Token 验证失败

确保：
- Token 格式正确：`Authorization: Bearer {token}`
- Token 未过期
- JWT 密钥配置一致

## 下一步计划

- [ ] 添加手机号验证码登录
- [ ] 添加邮箱验证功能
- [ ] 添加忘记密码功能
- [ ] 添加用户信息修改接口
- [ ] 添加用户头像上传
- [ ] 添加收货地址管理
