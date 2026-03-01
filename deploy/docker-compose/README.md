# ZenithMall 基础设施部署

本目录包含 ZenithMall 电商项目所需的 Docker Compose 配置文件。

## 📦 包含的服务

### 核心服务

| 服务 | 端口 | 说明 |
|------|------|------|
| **MySQL** | 3306 | 数据库服务，自动初始化表结构和测试数据 |
| **Redis** | 6379 | 缓存和会话存储 |
| **Nginx** | 80, 443 | 反向代理和静态资源服务 |

### 可选服务

| 服务 | 端口 | 说明 |
|------|------|------|
| **RabbitMQ** | 5672, 15672 | 消息队列，用于异步处理和订单延迟取消 |
| **Elasticsearch** | 9200, 9300 | 搜索引擎，用于商品搜索和日志分析 |

## 🚀 快速开始

### 前置要求

- Docker Desktop for Windows 已安装并运行
- 确保 Docker 有足够的资源（建议至少 4GB 内存）

### 启动所有服务

```bash
# 进入配置目录
cd deploy/docker-compose

# 启动服务（包含可选服务）
docker-compose -f infrastructure.yml up -d

# 仅启动核心服务
docker-compose -f infrastructure.yml up -d mysql redis nginx
```

### 查看服务状态

```bash
docker-compose -f infrastructure.yml ps
```

### 查看日志

```bash
# 查看所有服务日志
docker-compose -f infrastructure.yml logs -f

# 查看特定服务日志
docker-compose -f infrastructure.yml logs -f mysql
```

### 停止服务

```bash
# 停止所有服务
docker-compose -f infrastructure.yml down

# 停止服务并删除数据卷（⚠️ 会删除所有数据）
docker-compose -f infrastructure.yml down -v
```

## 🔧 服务配置

### MySQL

**连接信息：**
- Host: `localhost` 或 `127.0.0.1`
- Port: `3306`
- Database: `zenith_mall`
- Username: `root` / `zenith`
- Password: `root123456` / `zenith123456`

**初始化：**
- 首次启动时自动执行 `sql/init/` 目录下的初始化脚本
- 数据持久化到 Docker volume: `mysql-data`

**管理工具连接：**
```bash
# 命令行连接
docker exec -it zenith-mall-mysql mysql -uzenith -pzenith123456 zenith_mall

# 或从本地连接
mysql -h localhost -P 3306 -u zenith -pzenith123456 zenith_mall
```

### Redis

**连接信息：**
- Host: `localhost`
- Port: `6379`
- Password: 无（默认）

**管理工具连接：**
```bash
# 命令行连接
docker exec -it zenith-mall-redis redis-cli

# 测试连接
docker exec -it zenith-mall-redis redis-cli ping
```

### Nginx

**访问地址：**
- HTTP: `http://localhost`
- HTTPS: `https://localhost`（需要配置证书）

**配置文件：**
- 主配置: `./nginx/nginx.conf`
- 站点配置: `./nginx/conf.d/`
- 静态资源: `./nginx/html/`

### RabbitMQ

**访问地址：**
- AMQP: `localhost:5672`
- 管理界面: `http://localhost:15672`
- Username: `zenith`
- Password: `zenith123456`

### Elasticsearch

**访问地址：**
- HTTP: `http://localhost:9200`
- Java 客户端: `localhost:9300`

## 📁 目录结构

```
deploy/docker-compose/
├── infrastructure.yml       # 基础设施服务定义
├── mysql/
│   └── my.cnf              # MySQL 配置文件
├── redis/
│   └── redis.conf          # Redis 配置文件
├── nginx/
│   ├── nginx.conf          # Nginx 主配置
│   ├── conf.d/
│   │   └── default.conf    # 站点配置
│   ├── html/
│   │   └── index.html      # 首页
│   └── ssl/                # SSL 证书目录（需自行添加）
└── README.md               # 本文档
```

## 🔍 健康检查

所有服务都配置了健康检查，可以通过以下命令查看：

```bash
docker inspect --format='{{.State.Health.Status}}' zenith-mall-mysql
docker inspect --format='{{.State.Health.Status}}' zenith-mall-redis
docker inspect --format='{{.State.Health.Status}}' zenith-mall-nginx
```

## ⚙️ 自定义配置

### 修改端口

如果默认端口已被占用，可以在 `infrastructure.yml` 中修改端口映射：

```yaml
services:
  mysql:
    ports:
      - "13306:3306"  # 使用 13306 端口
```

### 调整资源限制

对于 Elasticsearch 等资源密集型服务，可以调整内存限制：

```yaml
services:
  elasticsearch:
    environment:
      ES_JAVA_OPTS: -Xms1g -Xmx1g  # 增加到 1GB
```

### 启用 Redis 密码

编辑 `redis/redis.conf`，取消注释并修改密码行：

```conf
requirepass your_strong_password
```

## 🐛 故障排查

### MySQL 启动失败

```bash
# 查看详细日志
docker-compose logs mysql

# 重新初始化数据库
docker-compose down -v
docker-compose up -d mysql
```

### 端口冲突

如果端口被占用，可以使用以下命令查看：

```bash
# Windows (PowerShell)
netstat -ano | findstr :3306

# 或使用 Docker
docker ps
```

### 性能优化

- 建议在 Docker Desktop 中分配至少 4GB 内存
- 对于生产环境，建议调整 MySQL 的 `innodb_buffer_pool_size` 参数

## 📊 监控和管理

### 使用 Docker Desktop

- 打开 Docker Desktop
- 查看 "Containers" 标签页
- 可以查看日志、资源使用情况等

### 使用命令行

```bash
# 查看资源使用情况
docker stats

# 查看容器详细信息
docker inspect zenith-mall-mysql
```

## 🔐 安全建议

**生产环境部署前请务必：**

1. ✅ 修改所有默认密码
2. ✅ 启用 Redis 密码认证
3. ✅ 配置 SSL/TLS 证书
4. ✅ 限制数据库访问权限
5. ✅ 启用防火墙规则
6. ✅ 定期备份数据库
7. ✅ 监控系统资源使用

## 📝 注意事项

- Windows 上 Docker 的性能可能略低于 Linux
- 首次启动 Elasticsearch 可能较慢
- 数据卷会持久化数据，即使容器删除
- 修改配置文件后需要重启服务：`docker-compose restart <service>`

## 🆘 获取帮助

如遇问题，请检查：
1. Docker 是否正常运行
2. 端口是否被占用
3. 防火墙设置
4. Docker 日志

---

**文档版本:** 1.0  
**最后更新:** 2024-03-01
