# AIIDE 提示词 - 德莫 PC 端后台管理系统

## 角色设定

你是一位专业的前端开发工程师，负责为"德莫"无人车队 AI 智能体管理系统开发 PC 端后台管理界面。

---

## 项目背景

**产品名称**：德莫（DEMO）
**产品类型**：无人车队智能调度运营平台
**目标用户**：运营管理人员、调度员、监控员
**核心功能**：车队监控、智能调度、数据分析、AI 智能体配置

**现有产品**：移动端单页应用（demo.html），使用纯 HTML/CSS/JavaScript 开发
**开发产品**：PC 端后台管理系统，需保持与移动端一致的视觉风格

---

## 设计目标

### 核心目标

使用 React 18 + TailwindCSS 开发一套功能完整的 PC 端后台管理系统，要求：

1. **功能完整性**：覆盖移动端所有核心功能，并针对 PC 端场景合理扩展
2. **视觉一致性**：与现有移动端（demo.html）保持相同的设计语言
3. **高效交互**：符合传统后台管理系统的操作习惯
4. **代码质量**：组件化、模块化、易维护

### 目标平台

- 主要平台：PC 桌面端（1920×1080 及以上分辨率）
- 最小支持宽度：1024px
- 不需要响应式移动端适配

---

## 技术栈约束

```
前端框架：React 18 (使用 Vite 构建)
样式方案：TailwindCSS (必须)
UI 组件：HeadlessUI (用于无样式组件)
图表库：ECharts (数据可视化)
状态管理：Zustand (全局状态)
HTTP 客户端：Axios
路由：React Router v6
构建工具：Vite
代码规范：ESLint + Prettier
```

---

## 视觉设计规范（严格遵循）

### 色彩系统（必须与移动端一致）

```css
/* 背景色 */
--bg-primary: #0a0a1a      /* 主背景 - 深紫黑色 */
--bg-secondary: #12122a    /* 卡片/面板背景 */
--bg-tertiary: #1a1a3a     /* 表格背景 */

/* 玻璃效果 */
--glass-bg: rgba(255,255,255,0.08)
--glass-border: rgba(255,255,255,0.15)

/* 文字色 */
--text-primary: #f0f0f8    /* 主要文字 - 亮白 */
--text-secondary: #a0a0b8  /* 次要文字 - 灰紫 */
--text-muted: #606078      /* 辅助文字 */

/* 强调色 */
--accent-blue: #4a9eff     /* 主操作 - 亮蓝 */
--accent-purple: #c084fc   /* 次要强调 - 紫色 */
--accent-cyan: #22d3ee    /* 信息提示 - 青色 */

/* 状态色 */
--status-green: #4ade80    /* 成功/在线/待命 - 亮绿 */
--status-blue: #4a9eff    /* 进行中/配送 - 蓝色 */
--status-orange: #fbbf24   /* 警告/充电 - 橙色 */
--status-red: #f87171      /* 错误/故障 - 红色 */
```

### 字体规范

```css
font-family: -apple-system, BlinkMacSystemFont, "SF Pro Display", "Noto Sans SC", "Microsoft YaHei", sans-serif
font-size: 14px (基础), 12px (辅助), 18px (标题), 28px+ (数据大字)
```

### 组件样式

**按钮**：
- 主按钮：渐变 `bg-gradient-to-r from-[#4a9eff] to-[#6366f1]`，白色文字，圆角 `rounded-xl`，内阴影
- 次按钮：`bg-white/8`，边框 `border border-white/10`
- 悬停态：亮度提升 `brightness-110`，轻微上移 `translate-y-[-1px]`

**卡片**：
- 背景：`bg-white/8`，圆角 `rounded-2xl`
- 边框：`border border-white/15`
- 阴影：`shadow-[inset_0_1px_0_rgba(255,255,255,0.08),_0_2px_8px_rgba(0,0,0,0.2)]`

**表格**：
- 表头：`bg-[#1a1a3a]`，文字 `--text-secondary`
- 行悬停：`hover:bg-white/4`
- 边框：`border-b border-white/6`

**输入框**：
- 背景：`bg-white/6`，边框 `border border-white/10`
- 聚焦态：`focus:border-[#4a9eff]`，`focus:ring-2 focus:ring-[#4a9eff]/20`

### 动画效果

- 页面切换：淡入淡出 `transition-opacity duration-200`
- 按钮悬停：`transition-all duration-200`
- 卡片悬停：`hover:scale-[1.01] transition-transform duration-200`
- Toast 弹出：`translateY` 配合 `cubic-bezier(0.34, 1.56, 0.64, 1)`

---

## 页面结构要求

### 整体布局（必须实现）

```
┌────────────────────────────────────────────────────────────────┐
│ Header (h-16 = 64px)                                          │
│ Logo | 面包屑/标题 | 搜索框 | 通知图标 | 用户头像下拉菜单      │
├──────────┬─────────────────────────────────────────────────────┤
│ Sidebar  │ Content Area                                       │
│ (w-60)   │                                                     │
│          │ ┌─────────────────────────────────────────────────┐ │
│ 导航菜单 │ │ Page Header                                     │ │
│          │ │ 页面标题 + 描述 + 操作按钮                       │ │
│ - 数据大屏│ ├─────────────────────────────────────────────────┤ │
│ - 车队管理│ │                                                 │ │
│ - 任务调度│ │ Content (可滚动)                               │ │
│ - 能源管理│ │                                                 │ │
│ - 运营分析│ │                                                 │ │
│ - AI智能体│ │                                                 │ │
│ - 故障管理│ │                                                 │ │
│ - 系统设置│ │                                                 │ │
│          │ └─────────────────────────────────────────────────┘ │
└──────────┴─────────────────────────────────────────────────────┘
```

### 导航菜单结构

```
1. 数据大屏 (/dashboard)
2. 车队管理 (/fleet)
   - 车辆列表 (/fleet/vehicles)
   - 车辆详情 (/fleet/vehicles/:id)
3. 任务调度 (/dispatch)
   - 调度中心 (/dispatch/new)
   - 任务列表 (/dispatch/tasks)
4. 能源管理 (/energy)
   - 充电站 (/energy/stations)
   - 能耗分析 (/energy/analysis)
5. 运营分析 (/analytics)
   - 运营大屏 (/analytics/overview)
   - 数据报表 (/analytics/reports)
6. AI 智能体 (/ai)
   - 对话管理 (/ai/conversation)
   - 技能配置 (/ai/skills)
7. 故障管理 (/faults)
   - 故障告警 (/faults/alerts)
   - 维修工单 (/faults/orders)
8. 系统设置 (/settings)
   - 基本设置 (/settings/general)
   - 用户管理 (/settings/users)
```

---

## 功能模块详细需求

### 1. 数据大屏 (Dashboard)

**路由**：`/dashboard`

**功能要求**：
- 顶部欢迎信息 + 日期
- 5个核心指标卡片：总车辆数、今日单量、完成率、平均时效、耗电量
- 环形图：车辆状态分布（待命/配送/充电/故障）
- 折线图：今日单量24小时趋势
- 柱状图：区域配送分布（A/B/C/D/E区）
- 任务动态列表：实时显示最新10条操作日志
- 待处理任务栏：显示待确认的调度任务，可快速操作

**布局**：Grid 布局，充分利用大屏优势，左右/上下分区

---

### 2. 车队管理

#### 2.1 车辆列表页

**路由**：`/fleet/vehicles`

**功能要求**：
- 顶部操作栏：搜索框 + 状态筛选下拉 + 类型筛选下拉 + 导出按钮
- 数据表格：
  - 列：勾选框、车辆ID、类型、状态（带颜色标识）、电量（进度条）、位置、今日里程、操作按钮
  - 支持排序：按电量、里程、状态
  - 分页：每页20条，显示总条数
- 批量操作：选中后批量调度/充电/维修
- 快速操作：查看详情、编辑
- 状态颜色：待命(绿)、配送(蓝)、充电(橙)、故障(红)

**表格行高**：48px
**悬停效果**：整行背景变亮

#### 2.2 车辆详情页

**路由**：`/fleet/vehicles/:id`

**功能要求**：
- 左侧：车辆基础信息卡片（ID、类型、载重、累计里程）
- 中间：实时状态卡片（状态、电量进度条、位置、当前任务）
- 右侧：快捷操作按钮（调度任务、调度充电、远程开门、故障申报）
- 下方Tab区域：
  - 运行数据：今日里程、今日单量、在线时长、能耗评级
  - 能耗数据：百公里能耗、今日耗电、累计耗电
  - 历史记录：时间线展示车辆操作历史

---

### 3. 任务调度

#### 3.1 新建调度页

**路由**：`/dispatch/new`

**功能要求**：
- 左侧表单区：
  - 任务名称输入框
  - 执行时间选择：单选（立即执行/定时执行）+ 时间选择器
  - 目的地选择：A/B/C/D/E区 下拉选择
  - 货物类型：红酒/生鲜/电子产品/药品 下拉选择
  - 货物数量：数字输入 + 单位（箱/件）
  - 特殊要求：多选框（温控运输、防震处理、高价值货物）
- 右侧智能推荐区：
  - AI 根据目的地+货物类型自动推荐最优车辆组合
  - 显示推荐理由：距离近、电量足、车型合适
  - 一键应用推荐方案按钮
- 车辆选择区：
  - 左右双栏：左侧可选车辆列表，右侧已选车辆（可拖拽或点击添加）
  - 车辆卡片显示：ID、状态、电量、位置
- 底部：取消按钮 + 创建任务按钮

**验证规则**：
- 任务名称：必填，最多50字符
- 车辆数量：至少选择1辆
- 电量不足车辆不可选（<20%）

#### 3.2 任务列表页

**路由**：`/dispatch/tasks`

**功能要求**：
- 顶部筛选：任务状态（全部/待确认/执行中/已完成） + 日期范围
- 数据表格：
  - 列：任务ID、任务名称、涉及车辆、目的地、状态、执行时间、创建时间、操作
  - 状态标签：待确认(橙)、已确认(蓝)、执行中(紫)、已完成(绿)、已取消(灰)
- 操作：查看详情、确认执行、取消任务
- 支持按状态筛选和日期筛选

---

### 4. 运营分析

#### 4.1 运营大屏

**路由**：`/analytics/overview`

**功能要求**：
- 时间范围选择器：今日、近7天、近30天、自定义
- 核心指标行：总单量、总里程、平均时效、完成率、耗电量
- 折线图：单量趋势（支持多日对比）
- 环形图：车型分布
- 柱状图：区域单量分布
- 明细数据表：支持排序和导出

#### 4.2 数据报表

**路由**：`/analytics/reports`

**功能要求**：
- 时间范围筛选
- 报表类型选择：日报、周报、月报
- 数据表格：日期、总单量、里程、能耗、完成率、评价
- 导出功能：支持 Excel/CSV 导出

---

### 5. AI 智能体配置

**路由**：`/ai/conversation`

**功能要求**：
- 左侧配置区：
  - 模型选择下拉：GLM-4-Flash
  - 温度滑块：0.0-1.0，默认0.7
  - 最大Token输入：默认1024
  - 流式输出开关
  - 上下文记忆开关
- 右侧预览区：
  - 系统提示词预览（只读）
  - 编辑提示词按钮（打开Modal）
- 技能开关：
  - 自然语言理解、任务拆解、智能推荐、安全确认流程、异常告警、多轮对话
- 降级策略：
  - 当API不可用时的行为：本地规则引擎/友好错误/自动重试

---

### 6. 故障管理

#### 6.1 故障告警页

**路由**：`/faults/alerts`

**功能要求**：
- 实时告警列表
- 告警等级：严重(红)、警告(橙)、提示(蓝)
- 告警内容：车辆ID、故障类型、告警时间、持续时长
- 操作：确认告警、提交维修工单、忽略
- 筛选：按等级、按车辆、按时间

#### 6.2 维修工单页

**路由**：`/faults/orders`

**功能要求**：
- 工单列表表格
- 状态：待处理(橙)、处理中(蓝)、已完成(绿)
- 信息：工单ID、车辆ID、故障描述、提交时间、处理人、状态
- 操作：查看详情、更新状态

---

### 7. 系统设置

#### 7.1 用户管理

**路由**：`/settings/users`

**功能要求**：
- 用户列表表格
- 列：勾选框、用户名、角色、手机号、邮箱、状态、最后登录、操作
- 操作：编辑用户、禁用/启用、删除
- 添加用户 Modal：
  - 用户名、手机号、邮箱、角色下拉选择、初始密码

**角色选项**：超级管理员、运营管理员、调度员、监控员、维修员

#### 7.2 角色权限

**路由**：`/settings/roles`

**功能要求**：
- 角色列表
- 每个角色的权限矩阵（勾选框）
- 权限分类：车队管理、任务调度、能源管理、运营分析、AI智能体、故障管理、系统设置
- 每个分类下的具体权限项

---

## 组件清单（必须实现）

### 基础组件

1. **Button**
   - 变体：primary（渐变蓝）、secondary（玻璃）、danger（渐变红）
   - 尺寸：sm、md、lg
   - 状态：default、hover、active、disabled、loading

2. **Input**
   - 类型：text、password、number
   - 状态：default、focus、error、disabled
   - 支持前缀图标、后缀图标

3. **Select**
   - 下拉选择
   - 支持搜索
   - 单选/多选模式

4. **Table**
   - 支持排序
   - 支持分页
   - 支持行选择
   - 支持自定义列

5. **Card**
   - 玻璃态卡片
   - 支持标题、描述、操作区

6. **Modal**
   - 居中弹窗
   - 背景遮罩
   - 关闭按钮
   - 标题、内容、操作区

7. **Toast**
   - 位置：顶部居中
   - 类型：success、error、warning、info
   - 自动消失：3秒

8. **Badge**
   - 状态标签
   - 颜色：green、blue、orange、red、gray

9. **Progress**
   - 线性进度条
   - 颜色根据百分比变化

10. **Tabs**
    - 水平标签页
    - 支持下划线指示器

---

## 数据模拟要求

由于是前端开发阶段，使用 Mock 数据：

```javascript
// 车辆数据 - 12辆车
const vehicles = [
  { id: 'DM-01', type: '小型', status: 'idle', battery: 87, location: 'A区-3号点', ... },
  // ... 完整数据见 demo.html
];

// 任务数据
const tasks = [
  { id: 'TASK-001', name: 'B区红酒运输', vehicles: ['DM-01', 'DM-02'], status: 'pending', ... },
  // ...
];

// 运营数据
const stats = {
  todayOrders: 1847,
  todayKm: 1246,
  completionRate: 98.2,
  avgTime: 23,
  energyConsumed: 47.6
};
```

---

## 项目结构要求

```
src/
├── components/
│   ├── ui/              # 基础 UI 组件
│   │   ├── Button.tsx
│   │   ├── Input.tsx
│   │   ├── Select.tsx
│   │   ├── Table.tsx
│   │   ├── Card.tsx
│   │   ├── Modal.tsx
│   │   ├── Toast.tsx
│   │   ├── Badge.tsx
│   │   ├── Progress.tsx
│   │   └── Tabs.tsx
│   ├── layout/          # 布局组件
│   │   ├── DashboardLayout.tsx
│   │   ├── Sidebar.tsx
│   │   ├── Header.tsx
│   │   └── PageHeader.tsx
│   └── features/        # 业务组件
│       ├── VehicleCard.tsx
│       ├── TaskCard.tsx
│       ├── StatusBadge.tsx
│       └── ...
├── pages/
│   ├── Dashboard.tsx
│   ├── fleet/
│   │   ├── VehicleList.tsx
│   │   └── VehicleDetail.tsx
│   ├── dispatch/
│   │   ├── NewTask.tsx
│   │   └── TaskList.tsx
│   ├── analytics/
│   │   ├── Overview.tsx
│   │   └── Reports.tsx
│   ├── ai/
│   │   └── Conversation.tsx
│   ├── faults/
│   │   ├── Alerts.tsx
│   │   └── Orders.tsx
│   └── settings/
│       ├── Users.tsx
│       └── Roles.tsx
├── stores/              # Zustand stores
│   ├── vehicleStore.ts
│   ├── taskStore.ts
│   └── uiStore.ts
├── data/                # Mock 数据
│   └── mock.ts
├── styles/
│   └── globals.css      # Tailwind + 自定义CSS变量
├── App.tsx
└── main.tsx
```

---

## 代码规范要求

1. **组件规范**：
   - 使用函数组件 + Hooks
   - Props 类型使用 TypeScript interface
   - 组件文件首字母大写

2. **命名规范**：
   - 组件名：PascalCase（如 VehicleList）
   - 函数名：camelCase（如 handleSubmit）
   - CSS 类：遵循 Tailwind 命名

3. **样式规范**：
   - 优先使用 Tailwind 类
   - 复杂样式使用 CSS 变量引用设计规范颜色
   - 避免内联 style（除动态值）

4. **文件规范**：
   - 一个组件一个文件
   - 组件放在 features/ 对应模块目录
   - 共用组件放在 ui/ 目录

---

## 性能要求

- 首屏加载 < 2s
- 页面切换 < 300ms
- 表格渲染 100 行 < 100ms
- 无内存泄漏
- 使用 React.memo 优化不必要渲染

---

## 验收检查清单

### 功能完整性
- [ ] 所有页面路由可访问
- [ ] 数据表格支持排序和分页
- [ ] 表单验证正确执行
- [ ] 弹窗/Toast 反馈正常
- [ ] 状态筛选和搜索功能正常

### 视觉一致性
- [ ] 色彩使用设计规范中的颜色
- [ ] 圆角、阴影符合规范
- [ ] 字体大小层级正确
- [ ] 组件状态（hover/active/disabled）样式正确

### 代码质量
- [ ] 无 TypeScript 编译错误
- [ ] 无 ESLint 错误
- [ ] 组件拆分合理
- [ ] Props 传递正确

---

## 交付物要求

1. **完整可运行的项目代码**
2. **README.md**：项目启动说明
3. **所有页面和组件的完整实现**
4. **Mock 数据和服务层（即使是假数据）**

---

## 重要约束

1. **不要修改移动端 demo.html** - 保持现有移动端不变
2. **不要使用其他 UI 框架** - 必须使用 TailwindCSS
3. **不要引入不必要的依赖** - 只用清单中列出的技术栈
4. **不要忽略 TypeScript 类型** - 所有 Props 必须有类型定义
5. **不要硬编码颜色值** - 使用 Tailwind 变量或 CSS 自定义属性

---

*提示词版本：v1.0*
*生成日期：2026-04-24*
