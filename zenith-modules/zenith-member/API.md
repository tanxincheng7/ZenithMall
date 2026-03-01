# 会员模块 API 文档

## 基础信息

- **服务名称**: zenith-member
- **服务端口**: 8081
- **Base URL**: `http://localhost:8080`
- **API 前缀**: `/v1/users`

## API 接口

### 1. 用户注册

**接口地址**: `POST /v1/users/register`

**请求参数**:
```json
{
  "username": "test",
  "password": "123456",
  "phone": "13800138000",
  "email": "test@example.com"
}
```

**参数说明**:

| 参数名 | 类型 | 必填 | 说明 | 验证规则 |
|--------|------|------|------|----------|
| username | String | 是 | 用户名 | 长度4-20个字符 |
| password | String | 是 | 密码 | 长度6-20个字符 |
| phone | String | 是 | 手机号 | 11位手机号，1开头 |
| email | String | 是 | 邮箱 | 有效的邮箱格式 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "userId": 1
  }
}
```

**错误响应**:
```json
{
  "code": 1002,
  "message": "用户已存在",
  "data": null
}
```

---

### 2. 用户登录

**接口地址**: `POST /v1/users/login`

**请求参数**:
```json
{
  "username": "test",
  "password": "123456"
}
```

**参数说明**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | String | 是 | 用户名或手机号 |
| password | String | 是 | 密码 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "userInfo": {
      "id": 1,
      "username": "test",
      "phone": "13800138000",
      "email": "test@example.com"
    }
  }
}
```

**错误响应**:
```json
{
  "code": 1003,
  "message": "用户名或密码错误",
  "data": null
}
```

---

### 3. 获取用户信息

**接口地址**: `GET /v1/users/info`

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "test",
    "phone": "13800138000",
    "email": "test@example.com"
  }
}
```

**错误响应**:
```json
{
  "code": 1006,
  "message": "Token 无效或已过期",
  "data": null
}
```

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 500 | 失败 |
| 1001 | 用户不存在 |
| 1002 | 用户已存在 |
| 1003 | 用户名或密码错误 |
| 1004 | 手机号已被注册 |
| 1005 | 邮箱已被注册 |
| 1006 | Token 无效或已过期 |
| 1007 | Token 已过期 |
| 2001 | 参数校验失败 |
| 2002 | 用户名长度至少4位 |
| 2003 | 密码长度至少6位 |
| 2004 | 手机号格式不正确 |
| 2005 | 邮箱格式不正确 |
| 5000 | 系统错误 |

---

## 测试示例

### 使用 curl 测试

#### 1. 注册用户
```bash
curl -X POST http://localhost:8080/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test",
    "password": "123456",
    "phone": "13800138000",
    "email": "test@example.com"
  }'
```

#### 2. 用户登录
```bash
curl -X POST http://localhost:8080/v1/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test",
    "password": "123456"
  }'
```

#### 3. 获取用户信息
```bash
# 替换 YOUR_TOKEN 为登录返回的 token
curl -X GET http://localhost:8080/v1/users/info \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## JWT Token 说明

- **签名算法**: HS512
- **Token 前缀**: `Bearer `
- **过期时间**: 7天
- **Token 包含信息**: userId, username

---

## 注意事项

1. 所有请求和响应均使用 JSON 格式
2. 需要认证的接口必须在请求头中携带 `Authorization: Bearer {token}`
3. Token 有效期为 7 天，过期后需要重新登录
4. 密码使用 BCrypt 加密存储
5. 支持用户名或手机号登录

---

## 数据库配置

默认配置:
- **数据库**: zenith_mall
- **地址**: localhost:3306
- **用户名**: zenith
- **密码**: zenith123456

如需修改，请编辑 `src/main/resources/application.yml`
