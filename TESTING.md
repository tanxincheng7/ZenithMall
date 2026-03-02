# ZenithMall 测试指南

## 测试架构

本项目采用**分层测试架构**，符合企业级开发规范和测试金字塔原则：

```
┌─────────────────────────────────────────────────────┐
│                    测试金字塔                          │
├─────────────────────────────────────────────────────┤
│  少量 E2E 测试     ← 验证关键业务流程                   │
├─────────────────────────────────────────────────────┤
│  适量集成测试     ← 验证模块协作（zenith-start）      │
├─────────────────────────────────────────────────────┤
│  大量单元测试     ← 快速反馈（各业务模块）            │
└─────────────────────────────────────────────────────┘
```

## 测试层次

### 1. 单元测试层（业务模块内）

**位置**：`zenith-modules/*/src/test/java/`

**职责**：
- 测试单个组件的功能正确性
- 使用 Mock 隔离外部依赖
- 快速反馈，适合 TDD

**示例**：`zenith-modules/zenith-member/src/test/java/com/tanxincheng/zenith/member/service/UserServiceTest.java`

**特点**：
- ✅ 使用 H2 内存数据库，无需真实数据库
- ✅ 使用 Mockito Mock 外部依赖
- ✅ 执行速度快（秒级）
- ✅ 易于定位问题

### 2. 集成测试层（启动模块）

**位置**：`zenith-start/src/test/java/com/tanxincheng/zenith/integration/`

**职责**：
- 验证模块间的协作
- 验证与真实数据库的交互
- 验证完整的业务流程

**示例**：`zenith-start/src/test/java/com/tanxincheng/zenith/integration/user/UserIntegrationTest.java`

**特点**：
- ✅ 启动完整的 Spring Boot 应用
- ✅ 连接真实测试数据库
- ✅ 验证 API 接口协作
- ✅ 自动回滚事务，保持数据清洁

## 运行测试

### 方式一：运行单元测试（快速反馈）

```bash
# 运行特定模块的单元测试
cd zenith-modules/zenith-member
mvn test

# 运行特定测试类
mvn test -Dtest=UserServiceTest

# 运行特定测试方法
mvn test -Dtest=UserServiceTest#testRegister_Success
```

**执行时间**：< 10 秒

### 方式二：运行集成测试（验证完整流程）

```bash
# 运行集成测试
cd zenith-start
mvn test

# 运行特定模块的集成测试
mvn test -Dtest=UserIntegrationTest

# 运行完整流程测试
mvn test -Dtest=UserIntegrationTest#testCompleteUserFlow
```

**执行时间**：10-30 秒

### 方式三：运行所有测试

```bash
# 在项目根目录执行
mvn test

# 或使用 verify 阶段
mvn verify
```

## 环境准备

### 1. 单元测试环境

单元测试使用 H2 内存数据库，**无需额外配置**，开箱即用。

### 2. 集成测试环境

#### 创建测试数据库

```bash
# 连接到 MySQL
mysql -u zenith -p

# 执行测试数据库脚本
source G:/Projects/ZenithMall/sql/test/01-test-database.sql
```

或在命令行中：
```bash
mysql -u zenith -p000000 < G:/Projects/ZenithMall/sql/test/01-test-database.sql
```

#### 验证数据库连接

确保 `zenith-start/src/test/resources/application-test.yml` 中的配置正确：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/zenith_mall_test?...
    username: zenith
    password: "000000"
```

## 测试用例说明

### 单元测试用例（UserServiceTest）

| 测试用例 | 描述 | 类型 |
|---------|------|-----|
| `testRegister_Success` | 成功注册新用户 | 正常流程 |
| `testRegister_UsernameExists` | 用户名已存在 | 异常流程 |
| `testRegister_PhoneExists` | 手机号已存在 | 异常流程 |
| `testLogin_WithUsername_Success` | 用户名登录成功 | 正常流程 |
| `testLogin_UserNotFound` | 用户不存在 | 异常流程 |
| `testLogin_WrongPassword` | 密码错误 | 异常流程 |
| `testLogin_UserDisabled` | 用户被禁用 | 异常流程 |
| `testGetUserInfo_Success` | 获取用户信息成功 | 正常流程 |
| `testGetUserInfo_UserNotFound` | 用户不存在 | 异常流程 |
| `testGetUserByUsername_Success` | 根据用户名查询 | 正常流程 |
| `testGetUserByPhone_Success` | 根据手机号查询 | 正常流程 |

### 集成测试用例（UserIntegrationTest）

| 测试用例 | 描述 | 验证内容 |
|---------|------|---------|
| `testRegister_Success` | 用户注册 | API + 数据库插入 |
| `testRegister_DuplicateUsername` | 用户名重复 | API + 唯一约束 |
| `testLogin_WithUsername` | 用户名登录 | API + JWT Token |
| `testLogin_WithPhone` | 手机号登录 | API + JWT Token |
| `testLogin_WrongPassword` | 密码错误 | API + 异常处理 |
| `testGetUserInfo_ValidToken` | 获取用户信息 | API + 认证 |
| `testGetUserInfo_NoToken` | 无 Token | API + 认证失败 |
| `testGetUserInfo_InvalidToken` | 无效 Token | API + Token 验证 |
| `testCompleteUserFlow` | 完整流程 | 端到端验证 |

## 测试输出示例

### 单元测试输出

```
[INFO] Running com.tanxincheng.zenith.member.service.UserServiceTest
✅ 用户注册测试通过
✅ 用户名已存在测试通过
✅ 手机号已存在测试通过
✅ 用户名登录测试通过
...
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
```

### 集成测试输出

```
========================================
用户模块集成测试
测试用户名: testuser_123456
测试手机号: 13812345678
测试邮箱: test_123456@example.com
========================================

✅ 用户注册测试通过 - 数据库联通验证成功
注册用户ID: 1
✅ 用户名登录测试通过 - Token获取成功
Token: eyJhbGciOiJIUzI1NiJ9...
✅ 获取用户信息测试通过 - 数据库查询验证成功
========================================
完整流程测试全部通过！
✅ 数据库联通性验证成功
✅ JWT Token 认证验证成功
✅ API 接口协作验证成功
========================================
```

## 测试最佳实践

### 开发阶段

```bash
# 快速反馈：只运行当前模块的单元测试
cd zenith-modules/zenith-member
mvn test
```

### 提交前

```bash
# 运行所有单元测试
mvn test
```

### CI/CD 流水线

```bash
# 1. 运行所有单元测试
mvn test

# 2. 运行集成测试
cd zenith-start
mvn verify

# 3. 生成测试报告
mvn jacoco:report
```

## 故障排除

### 问题 1：单元测试失败 - Mock 对象未正确配置

**解决方案**：
- 检查 `@ExtendWith(MockitoExtension.class)` 注解
- 检查 `@Mock` 和 `@InjectMocks` 注解
- 验证 Mock 对象的 stub 配置

### 问题 2：集成测试失败 - 数据库连接失败

**错误信息**：
```
Communications link failure
```

**解决方案**：
1. 确认 MySQL 服务已启动
2. 检查 `application-test.yml` 中的连接配置
3. 确认数据库 `zenith_mall_test` 已创建

### 问题 3：集成测试失败 - 表不存在

**错误信息**：
```
Table 'zenith_mall_test.user' doesn't exist
```

**解决方案**：
执行测试数据库初始化脚本：
```bash
mysql -u zenith -p000000 < G:/Projects/ZenithMall/sql/test/01-test-database.sql
```

## 添加新测试

### 添加单元测试

在对应模块的 `src/test/java` 目录下创建测试类：

```java
@ExtendWith(MockitoExtension.class)
class YourServiceTest {
    @Mock
    private YourMapper yourMapper;

    @InjectMocks
    private YourServiceImpl yourService;

    @Test
    void testYourMethod() {
        // 测试代码
    }
}
```

### 添加集成测试

在 `zenith-start/src/test/java/com/tanxincheng/zenith/integration/` 下创建测试类：

```java
public class YourIntegrationTest extends BaseIntegrationTest {
    @Test
    void testCompleteFlow() {
        // 测试代码
    }
}
```

## 企业级开发规范

### ✅ 推荐做法

1. **分层测试**：单元测试和集成测试分离
2. **测试隔离**：单元测试使用 Mock，集成测试使用真实数据库
3. **快速反馈**：单元测试应该快速执行（秒级）
4. **事务回滚**：集成测试使用 `@Transactional` 自动清理数据
5. **测试覆盖率**：核心业务逻辑应该有完整的测试覆盖

### ❌ 避免做法

1. ❌ 在单元测试中使用真实数据库
2. ❌ 在集成测试中过度使用 Mock
3. ❌ 测试之间有依赖关系
4. ❌ 测试方法名不清晰
5. ❌ 忽略异常情况测试

## 相关文档

- [Spring Boot Test 文档](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [Mockito 文档](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [JUnit 5 文档](https://junit.org/junit5/docs/current/user-guide/)
