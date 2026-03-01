# ZenithMall 数据库初始化脚本

## 文件说明

### 01-schema.sql
数据库表结构定义文件，包含以下核心表：

- **user** - 用户表：存储用户基本信息
- **category** - 商品分类表：支持多级分类
- **product** - 商品表：商品基本信息和库存
- **cart** - 购物车表：用户购物车数据
- **order** - 订单表：订单主信息
- **order_item** - 订单项表：订单明细
- **payment** - 支付记录表：支付流水记录

### 02-test-data.sql
测试数据文件，包含：

- 3个测试用户（密码均为 `123456`）
- 12个商品分类（包含二级分类）
- 15个测试商品
- 购物车数据
- 订单和订单项数据
- 支付记录数据

## 使用方法

### 方式一：MySQL 命令行

```bash
# 1. 创建数据库
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS zenith_mall DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 2. 导入表结构
mysql -u root -p zenith_mall < sql/init/01-schema.sql

# 3. 导入测试数据（可选）
mysql -u root -p zenith_mall < sql/init/02-test-data.sql
```

### 方式二：Docker Compose

使用项目中的 Docker Compose 配置：

```bash
# 启动数据库并自动初始化
docker-compose -f deploy/docker-compose/infrastructure.yml up -d
```

### 方式三：数据库管理工具

使用 Navicat、DBeaver、MySQL Workbench 等工具：
1. 连接到 MySQL 服务器
2. 创建数据库 `zenith_mall`
3. 分别执行 01-schema.sql 和 02-test-data.sql

## 测试账号

初始化后的测试账号：

| 用户名 | 密码 | 手机号 | 邮箱 |
|--------|------|--------|------|
| admin | 123456 | 13800138001 | admin@zenith.com |
| user01 | 123456 | 13800138002 | user01@example.com |
| user02 | 123456 | 13800138003 | user02@example.com |

## 注意事项

1. 生产环境部署时请修改测试用户的密码
2. 建议根据实际业务需求调整字段长度和索引
3. 密码使用 BCrypt 加密，cost factor 为 10
4. 所有表使用 InnoDB 引擎，支持事务
5. 字符集使用 utf8mb4，支持 emoji 等特殊字符

## 表关系说明

```
user (用户)
  ├── cart (购物车) [1:N]
  └── order (订单) [1:N]

category (分类)
  └── product (商品) [1:N]

product (商品)
  ├── cart (购物车) [1:N]
  └── order_item (订单项) [1:N]

order (订单)
  ├── order_item (订单项) [1:N]
  └── payment (支付记录) [1:1]
```

## 扩展建议

根据业务发展，可以考虑添加以下表：

- 用户收货地址表 (user_address)
- 商品规格表 (product_sku)
- 商品图片表 (product_image)
- 优惠券表 (coupon)
- 订单优惠券关联表 (order_coupon)
- 退款记录表 (refund)
- 商品评论表 (product_review)
- 积分记录表 (user_points)
