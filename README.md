# 🚗 QingClaw无人车队集群调度管理系统

<div align="center">

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![Java](https://img.shields.io/badge/Java-8%2B-orange.svg)
![Vue](https://img.shields.io/badge/Vue-3.x-brightgreen.svg)
![License](https://img.shields.io/badge/license-MIT-green.svg)
![Platform](https://img.shields.io/badge/platform-PC%20%7C%20Mobile%20%7C%20HarmonyOS-lightgrey.svg)
![平台](https://img.shields.io/badge/platform-PC%20%7C%20Mobile%20%7C%20HarmonyOS-lightgrey.svg)

**一套面向无人车队的多端协同调度与运营管理平台**

[快速开始](#-快速开始) · [功能特性](#-功能特性) · [项目架构](#-项目架构) · [技术栈](#-技术栈) · [运行指南](#-运行指南) · [贡献指南](#-贡献指南)

</div>

---

## 📖 项目简介

**QingClaw无人车队集群调度管理系统** 是一套面向无人驾驶车队运营场景的全栈管理平台，支持 PC 管理端、移动端、华为鸿蒙端三端协同，提供车辆实时监控、智能调度、能耗分析、运营数据可视化以及 AI 智能对话等核心能力。

> 本项目采用前后端分离架构，后端基于 Java Spring Boot，PC 前端基于 Vue 3，移动端基于 Node.js + HTML，同时适配华为鸿蒙（HarmonyOS）平台。

---

## ✨ 功能特性

| 功能模块 | 描述 |
|---|---|
| 🚘 **车辆管理** | 车辆列表查询、详情查看、批量操作与状态监控 |
| 📋 **任务调度** | 智能派单、任务分配、调度状态跟踪 |
| ⚡ **能耗分析** | 实时能耗数据采集与图表展示 |
| 📊 **运营数据** | 日常运营报表、数据可视化（ECharts） |
| 🤖 **AI 智能对话** | 集成智谱 GLM 大模型，支持自然语言运营问答 |
| 👤 **用户管理** | 注册、登录、JWT 鉴权、权限控制 |
| 📱 **多端适配** | PC 端 / 手机端 / 鸿蒙端 三端同步支持 |

---

## 🏗 项目架构

```
EXC-Test-1-20260528/
├── claw-server/              # ☕ Java 后端服务 (Spring Boot + MySQL)
├── PC20260426/               # 🖥️ PC 管理端前端 (Vue 3 + Vite)
├── Phone20260423/            # 📱 手机端前端 (HTML + Node.js)
├── NeoClaw20260423HMOS/      # 🌐 华为鸿蒙端
├── .arts/                    # 🎨 设计资源
├── .codeartsdoer/            # 🛠️ 华为 CodeArts 配置
├── start-all.bat             # ▶️ 一键启动脚本
├── stop-all.bat              # ⏹️ 一键停止脚本
├── check-status.bat          # 🔍 运行状态检查脚本
├── .env.example              # 📄 环境变量配置模板
├── ENV_CONFIG.md             # 📘 环境配置说明
└── RUNNING_GUIDE.md          # 📗 详细运行指南
```

### 通信架构

```
PC 端 (5173)          手机端 (3000)
     │                      │
  Vite Proxy            Node Proxy
     └──────────┬───────────┘
                ▼
         Java 后端 (:8080)
         /JavaEEWeb03
                │
           MySQL (:3306)
```

---

## 🔧 技术栈

### 后端 (`claw-server`)
| 技术 | 版本 | 用途 |
|---|---|---|
| Java | 8+ | 主语言 |
| Spring Boot / Spring MVC | — | Web 框架 |
| MySQL | 5.7+ / 8.0+ | 关系型数据库 |
| Druid | — | 数据库连接池 & 监控 |
| Maven | 3.6+ | 构建工具 |
| JWT | — | 身份鉴权 |
| 智谱 GLM API | — | AI 对话能力 |

### PC 前端 (`PC20260426`)
| 技术 | 版本 | 用途 |
|---|---|---|
| Vue | 3.5.x | UI 框架 |
| Vite | 8.x | 构建 & 开发服务器 |
| Element Plus | 2.x | 组件库 |
| ECharts | 6.x | 数据可视化 |
| Pinia | 3.x | 状态管理 |
| Axios | 1.x | HTTP 请求 |

### 手机端 (`Phone20260423`)
| 技术 | 版本 | 用途 |
|---|---|---|
| Node.js | 18+ | 服务器运行时 |
| Express | 5.x | HTTP 服务框架 |
| HTML / CSS / JS | — | 单页应用 |

### 鸿蒙端 (`NeoClaw20260423HMOS`)
- 华为 HarmonyOS 原生开发
- 华为 CodeArts 配套工具链

---

## 🚀 快速开始

### 环境要求

| 软件 | 版本要求 |
|---|---|
| JDK | 8+ |
| Maven | 3.6+ |
| Node.js | 18+ |
| MySQL | 5.7+ / 8.0+ |
| Tomcat | 9.0+ |

### 1. 克隆仓库

```bash
git clone https://github.com/EXtreameChallenge/EXC-Test-1-20260528.git
cd EXC-Test-1-20260528
```

### 2. 配置环境变量

```bash
# 复制配置模板
cp .env.example .env
```

编辑 `.env` 文件，填写以下关键配置：

```env
# 数据库配置
DB_HOST=localhost
DB_PORT=3306
DB_NAME=claw_db
DB_USER=root
DB_PASSWORD=your_password

# 智谱 GLM API Key（必填）
# 获取地址：https://open.bigmodel.cn/
GLM_API_KEY=your_glm_api_key_here

# JWT 密钥
JWT_SECRET=your_jwt_secret_here
```

> 详细配置说明请参阅 [ENV_CONFIG.md](./ENV_CONFIG.md)

### 3. 初始化数据库

```sql
CREATE DATABASE claw_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 4. 一键启动（Windows）

```bash
# 启动所有服务
start-all.bat

# 查看运行状态
check-status.bat

# 停止所有服务
stop-all.bat
```

### 5. 手动启动

```bash
# 第一步：启动 MySQL 数据库

# 第二步：启动 Java 后端
cd claw-server
mvn spring-boot:run

# 第三步：启动手机端前端
cd Phone20260423
npm install
npm start

# 第四步：启动 PC 端前端
cd PC20260426
npm install
npm run dev
```

---

## 🌐 服务地址

| 服务 | 地址 | 说明 |
|---|---|---|
| PC 端管理后台 | http://localhost:5173 | Vue 3 管理界面 |
| 手机端 | http://localhost:3000 | 移动端界面 |
| Java 后端 API | http://localhost:8080/JavaEEWeb03 | RESTful API |
| Druid 监控台 | http://localhost:8080/JavaEEWeb03/druid | 数据库监控 |

## 🔌 主要 API 端点

| 端点 | 方法 | 说明 |
|---|---|---|
| `/api/login` | GET | 用户登录 |
| `/api/register` | GET | 用户注册 |
| `/api/vehicles` | GET | 获取车辆列表 |
| `/api/vehicles/:id` | GET | 获取车辆详情 |
| `/api/vehicles/batch` | POST | 批量操作车辆 |
| `/api/analytics/daily` | GET | 获取运营数据 |
| `/api/energy/stats` | GET | 获取能耗数据 |
| `/api/dispatch` | GET/POST | 任务调度 |
| `/api/chat` | POST | GLM AI 对话 |

---

## 📁 端口配置

| 服务 | 端口 |
|---|---|
| MySQL | 3306 |
| Java 后端（Tomcat） | 8080 |
| PC 端前端（Vite） | 5173 |
| 手机端前端（Node.js） | 3000 |

---

## 📚 文档

- 📗 [RUNNING_GUIDE.md](./RUNNING_GUIDE.md) — 完整运行指南（含故障排查）
- 📘 [ENV_CONFIG.md](./ENV_CONFIG.md) — 环境变量配置说明

---

## 🛠️ 开发调试

```bash
# PC 端热更新开发
cd PC20260426 && npm run dev

# 手机端调试模式
cd Phone20260423 && node server.js

# Java 后端（推荐使用 IDEA Debug 模式启动 Tomcat）
```

---

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/your-feature`)
3. 提交更改 (`git commit -m 'feat: add your feature'`)
4. 推送分支 (`git push origin feature/your-feature`)
5. 创建 Pull Request

---

## 📄 License

本项目采用 [MIT License](./LICENSE) 开源协议。

---

<div align="center">

Made with ❤️ by [EXtreameChallenge](https://github.com/EXtreameChallenge)

</div>
