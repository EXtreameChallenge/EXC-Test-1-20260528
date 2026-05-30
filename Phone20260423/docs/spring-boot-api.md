# 德莫 API 接口文档 (Spring Boot)

> **版本**: v1.0
> **日期**: 2026-04-24
> **基础路径**: `/api/v1`

---

## 一、通用说明

### 1.1 认证方式

所有 API 请求需要在 Header 中携带 JWT Token：

```
Authorization: Bearer <token>
```

### 1.2 响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 1.3 分页参数

```
GET /api/v1/vehicles?page=0&size=20&sort=battery,desc
```

---

## 二、认证接口 `/auth`

### 2.1 用户登录

```
POST /auth/login
```

**请求体**：
```json
{
  "username": "zhangyunying",
  "password": "******",
  "deviceType": "mobile"
}
```

**响应**：
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "user": {
      "id": 1,
      "username": "张运营",
      "role": "运营管理员",
      "permissions": ["车队管理", "任务调度", "能源管理", "运营分析", "AI智能体", "故障管理"],
      "level": "A级"
    }
  }
}
```

### 2.2 权限验证

```
GET /auth/verify
```

**Headers**: `Authorization: Bearer <token>`

**响应**：
```json
{
  "code": 200,
  "data": {
    "valid": true,
    "user": {
      "name": "张运营",
      "role": "运营管理员",
      "permissions": [...],
      "level": "A级"
    }
  }
}
```

---

## 三、车辆接口 `/vehicles`

### 3.1 获取车辆列表

```
GET /vehicles
```

**查询参数**：
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | int | 否 | 页码，默认 0 |
| size | int | 否 | 每页条数，默认 20 |
| status | string | 否 | 状态筛选: idle/busy/charge/fault |
| type | string | 否 | 车型筛选: 小型/中型/大型 |
| zone | string | 否 | 区域筛选: A区/B区/C区/D区/E区 |
| sort | string | 否 | 排序字段: battery/todayKm/status |

**响应**：
```json
{
  "code": 200,
  "data": {
    "content": [
      {
        "id": "DM-01",
        "type": "小型",
        "status": "idle",
        "battery": 87,
        "location": { "zone": "A区", "point": "3号点" },
        "task": "待命",
        "todayKm": 42,
        "todayOrders": 8,
        "energyPer100km": 6.1,
        "totalKm": 12847,
        "online": true,
        "lastUpdate": "2026-04-24T12:28:00"
      }
    ],
    "totalElements": 12,
    "totalPages": 1,
    "currentPage": 0
  }
}
```

### 3.2 获取车辆详情

```
GET /vehicles/{id}
```

**响应**：
```json
{
  "code": 200,
  "data": {
    "id": "DM-01",
    "type": "小型",
    "status": "idle",
    "battery": 87,
    "location": { "zone": "A区", "point": "3号点" },
    "task": "待命",
    "todayKm": 42,
    "todayOrders": 8,
    "energyPer100km": 6.1,
    "totalKm": 12847,
    "capacity": 500,
    "plateNumber": "--",
    "online": true,
    "lastUpdate": "2026-04-24T12:28:00",
    "history": [
      { "time": "12:28", "action": "开始配送 #1842", "type": "info" },
      { "time": "12:25", "action": "进入充电站-2", "type": "ok" }
    ]
  }
}
```

### 3.3 批量操作车辆

```
POST /vehicles/batch
```

**权限要求**: `任务调度`

**请求体**：
```json
{
  "vehicleIds": ["DM-01", "DM-02", "DM-03"],
  "action": "charge",
  "operatorId": 1
}
```

**响应**：
```json
{
  "code": 200,
  "message": "批量操作已提交",
  "data": {
    "batchId": "BATCH-001",
    "affectedVehicles": ["DM-01", "DM-02", "DM-03"],
    "action": "charge",
    "status": "executing",
    "submittedAt": "2026-04-24T14:00:00"
  }
}
```

### 3.4 远程车门控制

```
POST /vehicles/{id}/door
```

**权限要求**: `任务调度` + `安全确认`

**请求体**：
```json
{
  "action": "open",
  "operatorId": 1,
  "securityToken": "eyJhbGciOiJIUzI1NiIs..."
}
```

**安全验证流程**：
1. 身份验证（JWT Token）
2. 车辆唯一性判定（防止误操作其他车辆）
3. 权限校验（操作员是否有车门控制权限）
4. 安全确认（最终人工确认）

**响应**：
```json
{
  "code": 200,
  "message": "车门操作已确认",
  "data": {
    "vehicleId": "DM-08",
    "action": "open",
    "status": "success",
    "confirmedAt": "2026-04-24T14:05:00",
    "confirmedBy": "张运营"
  }
}
```

---

## 四、任务调度接口 `/dispatch`

### 4.1 创建调度任务

```
POST /dispatch
```

**权限要求**: `任务调度`

**请求体**：
```json
{
  "name": "B区红酒运输",
  "type": "dispatch",
  "vehicles": ["DM-01", "DM-02", "DM-07"],
  "destination": "B区",
  "cargo": {
    "type": "红酒",
    "quantity": 300,
    "unit": "箱",
    "requirements": ["温控", "防震"]
  },
  "scheduleTime": "2026-04-25T14:00:00",
  "operatorId": 1
}
```

**响应**：
```json
{
  "code": 200,
  "message": "调度任务已创建",
  "data": {
    "id": "TASK-001",
    "name": "B区红酒运输",
    "status": "pending",
    "vehicles": ["DM-01", "DM-02", "DM-07"],
    "createdAt": "2026-04-24T10:00:00"
  }
}
```

### 4.2 获取任务列表

```
GET /dispatch
```

**查询参数**：
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| status | string | 否 | pending/confirmed/executing/completed/cancelled |
| startDate | string | 否 | 起始日期 |
| endDate | string | 否 | 结束日期 |
| page | int | 否 | 页码 |
| size | int | 否 | 每页条数 |

### 4.3 确认执行任务

```
POST /dispatch/{id}/confirm
```

**权限要求**: `运营管理员` 或 `超级管理员`

**响应**：
```json
{
  "code": 200,
  "message": "任务已确认执行",
  "data": {
    "id": "TASK-001",
    "status": "confirmed",
    "confirmedAt": "2026-04-24T10:05:00",
    "confirmedBy": "张运营"
  }
}
```

---

## 五、能源管理接口 `/energy`

### 5.1 获取能耗统计

```
GET /energy/stats
```

**查询参数**：
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| period | string | 否 | today/week/month/custom |
| startDate | string | 否 | 自定义起始日期 |
| endDate | string | 否 | 自定义结束日期 |

**响应**：
```json
{
  "code": 200,
  "data": {
    "todayOrders": 1847,
    "todayKm": 1246,
    "completionRate": 98.2,
    "avgTime": 23,
    "energyConsumed": 47.6,
    "avgEnergyPer100km": 7.3,
    "energyRating": "良好",
    "comparedToLastMonth": "-3.2%",
    "byVehicleType": {
      "小型": { "count": 7, "avgEnergy": 6.2, "totalEnergy": 28.4 },
      "中型": { "count": 4, "avgEnergy": 8.5, "totalEnergy": 15.8 },
      "大型": { "count": 1, "avgEnergy": 12.1, "totalEnergy": 3.4 }
    }
  }
}
```

---

## 六、运营分析接口 `/analytics`

### 6.1 获取运营数据

```
GET /analytics/daily
```

**查询参数**：
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| date | string | 否 | 日期，默认今天 |
| days | int | 否 | 近N天数据，默认 7 |

**响应**：
```json
{
  "code": 200,
  "data": {
    "date": "2026-04-24",
    "totalOrders": 1847,
    "totalKm": 1246,
    "completionRate": 98.2,
    "avgTime": 23,
    "comparedToYesterday": "+12.3%",
    "byZone": {
      "A区": { "orders": 412, "percentage": 22.3, "avgTime": 19 },
      "B区": { "orders": 378, "percentage": 20.5, "avgTime": 21 },
      "C区": { "orders": 356, "percentage": 19.3, "avgTime": 25 },
      "D区": { "orders": 341, "percentage": 18.5, "avgTime": 22 },
      "E区": { "orders": 360, "percentage": 19.5, "avgTime": 28 }
    },
    "weeklyTrend": [
      { "date": "2026-04-21", "orders": 1523 },
      { "date": "2026-04-22", "orders": 1689 },
      { "date": "2026-04-23", "orders": 1756 },
      { "date": "2026-04-24", "orders": 1847 }
    ]
  }
}
```

---

## 七、AI 对话接口 `/ai`

### 7.1 发送消息

```
POST /ai/chat
```

**权限要求**: `AI智能体`

**请求体**：
```json
{
  "message": "调配3辆车去B区",
  "sessionId": "session_001",
  "operatorId": 1,
  "context": {
    "currentTime": "2026-04-24T14:00:00",
    "currentScene": "afternoon"
  }
}
```

**响应**：
```json
{
  "code": 200,
  "data": {
    "reply": "收到！我来给你安排去B区的调度任务...",
    "needConfirm": true,
    "actionSummary": "调配3辆车去B区运输",
    "affectedVehicles": ["DM-02", "DM-07", "DM-01"],
    "actionType": "调度",
    "sessionId": "session_001"
  }
}
```

### 7.2 语音输入识别

```
POST /ai/voice
Content-Type: multipart/form-data
```

**请求体**：
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| audio | file | 是 | 音频文件 |
| dialect | string | 否 | 方言类型: 普通话/四川话/东北话/粤语 |

**响应**：
```json
{
  "code": 200,
  "data": {
    "originalText": "调三辆车去B区",
    "dialect": "四川话",
    "translatedText": "调配3辆车去B区",
    "confidence": 0.95
  }
}
```

---

## 八、故障管理接口 `/faults`

### 8.1 获取故障列表

```
GET /faults
```

**查询参数**：
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| severity | string | 否 | critical/warning/info |
| status | string | 否 | open/acknowledged/resolved |

### 8.2 提交维修工单

```
POST /faults/orders
```

**权限要求**: `故障管理`

**请求体**：
```json
{
  "vehicleId": "DM-11",
  "faultType": "传感器异常",
  "severity": "warning",
  "description": "右侧传感器数据异常",
  "submittedBy": "张运营"
}
```

---

## 九、系统设置接口 `/settings`

### 9.1 获取用户列表

```
GET /settings/users
```

**权限要求**: `超级管理员`

### 9.2 创建用户

```
POST /settings/users
```

**权限要求**: `超级管理员`

### 9.3 角色权限矩阵

```
GET /settings/roles
```

**权限要求**: `超级管理员`

**响应**：
```json
{
  "code": 200,
  "data": {
    "roles": [
      {
        "name": "超级管理员",
        "permissions": ["全部权限"]
      },
      {
        "name": "运营管理员",
        "permissions": ["车队管理", "任务调度", "能源管理", "运营分析", "AI智能体", "故障管理"]
      },
      {
        "name": "调度员",
        "permissions": ["车队管理", "任务调度"]
      },
      {
        "name": "监控员",
        "permissions": ["车队管理(只读)", "运营分析(只读)", "故障管理(只读)"]
      },
      {
        "name": "维修员",
        "permissions": ["故障管理", "维修工单"]
      }
    ]
  }
}
```

---

## 十、WebSocket 实时推送

### 10.1 连接

```
ws://localhost:8080/ws
```

### 10.2 订阅主题

| 主题 | 说明 |
|------|------|
| `/topic/vehicles` | 车辆状态变更 |
| `/topic/tasks` | 任务状态变更 |
| `/topic/faults` | 故障告警 |
| `/topic/notifications` | 系统通知 |

### 10.3 消息格式

```json
{
  "type": "vehicle_update",
  "timestamp": "2026-04-24T12:28:00",
  "data": {
    "vehicleId": "DM-03",
    "status": "busy",
    "battery": 64,
    "location": "C区-途中"
  }
}
```

---

## 十一、错误码

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未认证或 Token 过期 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 409 | 冲突（如车辆不可用） |
| 500 | 服务器内部错误 |

---

## 十二、部署配置

### 12.1 application.yml

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/demo_fleet?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update

jwt:
  secret: your-secret-key
  expiration: 86400000

demo:
  api:
    glm-endpoint: https://open.bigmodel.cn/api/paas/v4/chat/completions
    glm-api-key: ${GLM_API_KEY}
```

### 12.2 跨域配置

```java
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                    .allowedOrigins("http://localhost:3000", "http://localhost:5173")
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowedHeaders("*")
                    .allowCredentials(true);
            }
        };
    }
}
```

---

*API 文档版本: v1.0*
*最后更新: 2026-04-24*
