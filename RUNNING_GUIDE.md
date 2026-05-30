# 德莫无人车队管理系统 - 多项目协同运行指南

## 📋 目录

1. [项目架构概览](#项目架构概览)
2. [环境要求](#环境要求)
3. [端口配置](#端口配置)
4. [启动顺序](#启动顺序)
5. [快速启动](#快速启动)
6. [跨项目通信机制](#跨项目通信机制)
7. [依赖管理策略](#依赖管理策略)
8. [环境变量设置](#环境变量设置)
9. [运行状态监控](#运行状态监控)
10. [故障排查指南](#故障排查指南)

---

## 项目架构概览

```
XClaw20260429/
├── JavaEEWeb03/          # Java后端 (Spring MVC + MySQL)
│   ├── src/main/java/    # Java源码
│   ├── pom.xml           # Maven配置
│   └── target/           # 编译输出
│
├── PC20260426/           # PC端前端 (Vue 3 + Vite)
│   ├── src/              # Vue源码
│   ├── package.json      # npm配置
│   └── vite.config.js    # Vite配置
│
├── Phone20260423/        # 手机端前端 (HTML + Node.js)
│   ├── demo.html         # 单页应用
│   ├── server.js         # Node.js服务器
│   └── package.json      # npm配置
│
├── start-all.bat         # 统一启动脚本
├── stop-all.bat          # 统一停止脚本
├── check-status.bat      # 状态检查脚本
└── .env.example          # 环境变量模板
```

---

## 环境要求

### 必需软件

| 软件 | 版本要求 | 用途 |
|------|---------|------|
| JDK | 8+ | Java后端运行 |
| Maven | 3.6+ | Java项目构建 |
| Node.js | 18+ | 前端项目运行 |
| MySQL | 5.7+ / 8.0+ | 数据库 |
| Tomcat | 9.0+ | Java Web容器 |

### 可选软件

| 软件 | 用途 |
|------|------|
| IDEA | Java开发调试 |
| VS Code | 前端开发调试 |

### 验证环境

```bash
# 检查Java
java -version

# 检查Maven
mvn -version

# 检查Node.js
node -v
npm -v

# 检查MySQL
mysql --version
```

---

## 端口配置

### 端口分配表

| 服务 | 端口 | 说明 |
|------|------|------|
| MySQL | 3306 | 数据库服务 |
| Java后端 (Tomcat) | 8080 | Spring MVC后端 |
| PC端前端 (Vite) | 5173 | Vue开发服务器 |
| 手机端前端 (Node.js) | 3000 | Express服务器 |

### 端口冲突解决

如果端口被占用，可以：

1. **查找占用进程**:
   ```bash
   netstat -ano | findstr ":8080"
   ```

2. **终止进程**:
   ```bash
   taskkill /PID <进程ID> /F
   ```

3. **修改端口**:
   - PC端: 修改 `PC20260426/vite.config.js` 中的 `server.port`
   - 手机端: 修改 `Phone20260423/server.js` 中的 `PORT`
   - Java后端: 修改Tomcat配置 `server.xml`

---

## 启动顺序

### 推荐启动顺序

```
1. MySQL数据库     → 2. Java后端     → 3. 手机端前端     → 4. PC端前端
```

### 详细步骤

#### 第一步：启动MySQL数据库

```bash
# Windows服务方式
net start mysql

# 或通过MySQL安装目录
mysqld --console
```

#### 第二步：启动Java后端

**方式A：使用IDEA**
1. 打开IDEA，导入 `JavaEEWeb03` 项目
2. 配置Tomcat服务器
3. 部署war包并启动

**方式B：使用命令行**
```bash
cd JavaEEWeb03
mvn clean package -DskipTests

# 将target/JavaEEWeb03.war复制到Tomcat的webapps目录
# 启动Tomcat
catalina.bat run
```

#### 第三步：启动手机端前端

```bash
cd Phone20260423
npm install          # 首次运行需要
node server.js       # 或使用 node server-unified.js
```

#### 第四步：启动PC端前端

```bash
cd PC20260426
npm install          # 首次运行需要
npm run dev
```

---

## 快速启动

### 使用统一启动脚本

双击运行 `start-all.bat`，脚本会自动：
1. 检查运行环境
2. 检查端口占用
3. 安装前端依赖
4. 编译Java后端
5. 启动所有前端服务

### 单独启动各项目

```bash
# 手机端前端
cd Phone20260423 && node server.js

# PC端前端
cd PC20260426 && npm run dev

# Java后端 (需要手动启动Tomcat)
```

---

## 跨项目通信机制

### API路由设计

```
┌─────────────────────────────────────────────────────────────────┐
│                        通信架构图                                │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  PC端前端 (5173)              手机端前端 (3000)                 │
│  ┌──────────────┐             ┌──────────────┐                 │
│  │  Vue 3 App   │             │  HTML App    │                 │
│  └──────┬───────┘             └──────┬───────┘                 │
│         │                            │                          │
│         │ /api/*                     │ /api/*                   │
│         ▼                            ▼                          │
│  ┌──────────────┐             ┌──────────────┐                 │
│  │ Vite Proxy   │             │ Node Proxy   │                 │
│  │ (开发代理)   │             │ (服务器代理) │                 │
│  └──────┬───────┘             └──────┬───────┘                 │
│         │                            │                          │
│         └────────────┬───────────────┘                          │
│                      │                                          │
│                      ▼                                          │
│              ┌──────────────┐                                   │
│              │ Java后端     │                                   │
│              │ :8080        │                                   │
│              │ /JavaEEWeb03 │                                   │
│              └──────────────┘                                   │
└─────────────────────────────────────────────────────────────────┘
```

### API端点列表

| 端点 | 方法 | 说明 |
|------|------|------|
| `/api/login` | GET | 用户登录 |
| `/api/register` | GET | 用户注册 |
| `/api/vehicles` | GET | 获取车辆列表 |
| `/api/vehicles/:id` | GET | 获取车辆详情 |
| `/api/vehicles/batch` | POST | 批量操作车辆 |
| `/api/analytics/daily` | GET | 获取运营数据 |
| `/api/energy/stats` | GET | 获取能耗数据 |
| `/api/dispatch` | GET/POST | 任务调度 |
| `/api/chat` | POST | GLM AI对话 |

---

## 依赖管理策略

### Java后端依赖

```xml
<!-- pom.xml 主要依赖 -->
- Spring MVC 5.3.20
- Spring JDBC 5.3.20
- MySQL Connector 8.0.33
- Druid 连接池 1.1.9
- Jackson 2.13.3
```

安装命令：
```bash
cd JavaEEWeb03
mvn dependency:resolve
```

### PC端前端依赖

```json
// package.json 主要依赖
- vue: ^3.5.32
- vite: ^8.0.10
- element-plus: ^2.13.7
- pinia: ^3.0.4
- axios: ^1.15.2
- echarts: ^6.0.0
```

安装命令：
```bash
cd PC20260426
npm install
```

### 手机端前端依赖

```json
// package.json 主要依赖
- express: ^5.2.1
```

安装命令：
```bash
cd Phone20260423
npm install
```

---

## 环境变量设置

### 配置文件

复制 `.env.example` 为 `.env` 并修改：

```env
# 数据库配置
DB_HOST=localhost
DB_PORT=3306
DB_NAME=javaee
DB_USER=root
DB_PASSWORD=123456

# 服务端口
JAVA_BACKEND_PORT=8080
PC_FRONTEND_PORT=5173
PHONE_FRONTEND_PORT=3000

# API配置
JAVA_BACKEND_URL=http://localhost:8080/JavaEEWeb03
GLM_API_KEY=your_api_key_here
```

### Java后端配置

修改 `JavaEEWeb03/src/main/resources/applicationContext.xml`:

```xml
<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
    <property name="url" value="jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?..."/>
    <property name="username" value="${DB_USER}"/>
    <property name="password" value="${DB_PASSWORD}"/>
</bean>
```

---

## 运行状态监控

### 使用监控脚本

```bash
# 运行状态检查
check-status.bat
```

输出示例：
```
══════════════════════════════════════════════════════════════
端口状态检查
══════════════════════════════════════════════════════════════

[Java 后端 - 端口 8080]
    状态: ✓ 运行中

[PC端前端 - 端口 5173]
    状态: ✓ 运行中

[手机端前端 - 端口 3000]
    状态: ✓ 运行中
```

### 手动检查

```bash
# 检查端口
netstat -ano | findstr "LISTENING" | findstr "8080 5173 3000"

# 检查进程
tasklist | findstr "java node"

# 检查API健康
curl http://localhost:8080/JavaEEWeb03/api/login?username=test&password=test
curl http://localhost:3000/api/health
```

---

## 故障排查指南

### 常见问题

#### 1. Java后端无法启动

**症状**: Tomcat启动失败或404错误

**排查步骤**:
```bash
# 检查MySQL是否运行
netstat -ano | findstr ":3306"

# 检查数据库是否存在
mysql -u root -p -e "SHOW DATABASES LIKE 'javaee';"

# 检查war包是否正确部署
ls Tomcat/webapps/JavaEEWeb03/

# 查看Tomcat日志
cat Tomcat/logs/catalina.out
```

**解决方案**:
- 确保MySQL服务已启动
- 创建数据库: `CREATE DATABASE javaee;`
- 检查数据库连接配置是否正确

#### 2. 前端无法连接后端

**症状**: API请求返回CORS错误或404

**排查步骤**:
```bash
# 测试后端API是否可访问
curl http://localhost:8080/JavaEEWeb03/api/login?username=admin&password=123456

# 检查代理配置
cat PC20260426/vite.config.js
```

**解决方案**:
- 确认Java后端已启动
- 检查代理配置中的target地址
- 确认API路径正确

#### 3. 端口被占用

**症状**: 启动时提示端口已被使用

**排查步骤**:
```bash
# 查找占用端口的进程
netstat -ano | findstr ":8080"

# 查看进程详情
tasklist /FI "PID eq <进程ID>"
```

**解决方案**:
```bash
# 终止占用进程
taskkill /PID <进程ID> /F

# 或修改项目端口配置
```

#### 4. 数据库连接失败

**症状**: Java后端报数据库连接错误

**排查步骤**:
```bash
# 测试数据库连接
mysql -u root -p -h localhost javaee

# 检查用户权限
mysql -u root -p -e "SHOW GRANTS FOR 'root'@'localhost';"
```

**解决方案**:
- 确认MySQL服务运行中
- 检查用户名密码是否正确
- 检查数据库是否存在

#### 5. 依赖安装失败

**症状**: npm install报错

**排查步骤**:
```bash
# 清除npm缓存
npm cache clean --force

# 检查Node.js版本
node -v  # 需要18+
```

**解决方案**:
```bash
# 删除node_modules重新安装
rm -rf node_modules package-lock.json
npm install

# 使用国内镜像
npm install --registry=https://registry.npmmirror.com
```

### 日志位置

| 服务 | 日志位置 |
|------|---------|
| Java后端 | `Tomcat/logs/catalina.out` |
| PC端前端 | 浏览器控制台 (F12) |
| 手机端前端 | 命令行窗口 |

### 调试模式

**PC端前端**:
```bash
# 启动开发模式（带热更新）
npm run dev
```

**手机端前端**:
```bash
# 启动带调试日志的服务器
DEBUG=* node server.js
```

**Java后端**:
- 在IDEA中使用Debug模式启动Tomcat
- 设置断点进行调试

---

## 访问地址汇总

启动成功后，可通过以下地址访问：

| 服务 | 地址 | 说明 |
|------|------|------|
| 手机端前端 | http://localhost:3000 | 手机端界面 |
| PC端前端 | http://localhost:5173 | PC端管理后台 |
| Java后端 | http://localhost:8080/JavaEEWeb03 | 后端API |
| 登录页面 | http://localhost:8080/JavaEEWeb03/pages/login.jsp | JSP登录页 |

---

## 默认账号

| 系统 | 用户名 | 密码 |
|------|--------|------|
| 数据库 | root | 123456 |
| 管理后台 | admin | 123456 |

---

*文档版本: v1.0*
*最后更新: 2026-04-29*
