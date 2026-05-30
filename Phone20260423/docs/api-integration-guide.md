# API 对接配置指南

## 一、架构概览

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│  移动端 Demo │     │  Node.js    │     │ Spring Boot │
│  (demo.html)│────>│  代理服务器 │────>│  后端服务    │
└─────────────┘     │  (server.js)│     │  :8080      │
                    └─────────────┘     └─────────────┘
                          │
                          │
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│  Vue3 管理端 │     │  Nginx      │     │   数据库     │
│  :5173      │────>│  反向代理   │     │   MySQL     │
└─────────────┘     └─────────────┘     └─────────────┘
```

---

## 二、移动端 Demo 对接 Spring Boot

### 2.1 修改 server.js 代理配置

当前 `server.js` 已实现 GLM API 代理，需要扩展为转发到 Spring Boot 后端：

```javascript
const express = require('express');
const http = require('http');
const path = require('path');
const { createProxyMiddleware } = require('http-proxy-middleware');

const app = express();
const PORT = process.env.PORT || 3000;
const SPRING_BOOT_URL = 'http://localhost:8080';

// 静态文件服务
app.use(express.static(path.join(__dirname)));

// 代理 GLM API
app.use('/api/chat', createProxyMiddleware({
  target: 'https://open.bigmodel.cn',
  changeOrigin: true,
  pathRewrite: { '^/api/chat': '/api/paas/v4/chat/completions' },
  on: {
    proxyReq: (proxyReq) => {
      proxyReq.setHeader('Authorization', `Bearer ${process.env.GLM_API_KEY || '70888881134e4137aa2956b6534d6ac6.X83pGlMih9mmmlxq'}`);
    }
  }
}));

// 代理 Spring Boot API
app.use('/api/v1', createProxyMiddleware({
  target: SPRING_BOOT_URL,
  changeOrigin: true,
  pathRewrite: { '^/api/v1': '/api/v1' },
  on: {
    error: (err, req, res) => {
      console.log('Spring Boot 连接失败:', err.message);
      res.status(502).json({ code: 502, message: '后端服务不可用' });
    }
  }
}));

// 健康检查
app.get('/api/health', (req, res) => {
  res.json({ status: 'ok', springBoot: 'pending' });
});

// SPA 回退
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'demo.html'));
});

const server = http.createServer(app);
server.listen(PORT, () => {
  console.log(`德莫代理服务器运行在 http://localhost:${PORT}`);
  console.log(`Spring Boot 后端: ${SPRING_BOOT_URL}`);
});
```

### 2.2 移动端 Demo API 调用封装

在 `demo.html` 中添加 API 调用层：

```javascript
/* ================================================================
   Spring Boot API Client
   ================================================================ */
const API_BASE = window.location.origin + '/api/v1';

async function apiRequest(endpoint, options = {}) {
  const token = localStorage.getItem('auth_token');
  
  const response = await fetch(`${API_BASE}${endpoint}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...(token && { 'Authorization': `Bearer ${token}` }),
      ...options.headers
    }
  });
  
  const result = await response.json();
  
  if (!response.ok) {
    throw new Error(result.message || 'API 请求失败');
  }
  
  return result.data;
}

// 使用示例：
// const vehicles = await apiRequest('/vehicles');
// const stats = await apiRequest('/analytics/daily');
```

---

## 三、Vue3 管理端对接 Spring Boot

### 3.1 Vite 代理配置

`vite.config.ts`:

```typescript
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      '/api/v1': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/ws': {
        target: 'http://localhost:8080',
        ws: true
      }
    }
  }
})
```

### 3.2 Axios 配置

`src/api/index.ts`:

```typescript
import axios from 'axios'

const api = axios.create({
  baseURL: '/api/v1',
  timeout: 10000
})

// 请求拦截器
api.interceptors.request.use(config => {
  const token = localStorage.getItem('auth_token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截器
api.interceptors.response.use(
  response => response.data,
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('auth_token')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default api
```

### 3.3 车辆 API 封装

`src/api/vehicle.ts`:

```typescript
import api from './index'

export interface Vehicle {
  id: string
  type: '小型' | '中型' | '大型'
  status: 'idle' | 'busy' | 'charge' | 'fault'
  battery: number
  location: { zone: string; point: string }
  task: string
  todayKm: number
  todayOrders: number
  energyPer100km: number
  totalKm: number
  online: boolean
  lastUpdate: string
}

export interface VehicleListResponse {
  content: Vehicle[]
  totalElements: number
  totalPages: number
  currentPage: number
}

// 获取车辆列表
export async function getVehicles(params?: {
  page?: number
  size?: number
  status?: string
  type?: string
  zone?: string
  sort?: string
}): Promise<VehicleListResponse> {
  return api.get('/vehicles', { params })
}

// 获取车辆详情
export async function getVehicleDetail(id: string): Promise<Vehicle> {
  return api.get(`/vehicles/${id}`)
}

// 批量操作
export async function batchOperateVehicles(vehicleIds: string[], action: string): Promise<any> {
  return api.post('/vehicles/batch', { vehicleIds, action, operatorId: 1 })
}

// 远程车门控制
export async function controlDoor(vehicleId: string, action: 'open' | 'close'): Promise<any> {
  return api.post(`/vehicles/${vehicleId}/door`, { action, operatorId: 1 })
}
```

### 3.4 认证 API 封装

`src/api/auth.ts`:

```typescript
import api from './index'

export interface LoginRequest {
  username: string
  password: string
  deviceType: 'mobile' | 'pc'
}

export interface User {
  id: number
  username: string
  role: string
  permissions: string[]
  level: string
}

export interface LoginResponse {
  token: string
  user: User
}

// 用户登录
export async function login(data: LoginRequest): Promise<LoginResponse> {
  const response = await api.post('/auth/login', data)
  if (response.token) {
    localStorage.setItem('auth_token', response.token)
    localStorage.setItem('user', JSON.stringify(response.user))
  }
  return response
}

// 权限验证
export async function verifyAuth(): Promise<{ valid: boolean; user: User }> {
  return api.get('/auth/verify')
}

// 登出
export function logout() {
  localStorage.removeItem('auth_token')
  localStorage.removeItem('user')
}

// 获取当前用户
export function getCurrentUser(): User | null {
  const userStr = localStorage.getItem('user')
  return userStr ? JSON.parse(userStr) : null
}

// 检查权限
export function hasPermission(permission: string): boolean {
  const user = getCurrentUser()
  return user?.permissions.includes(permission) ?? false
}
```

---

## 四、跨域配置 (Spring Boot)

`CorsConfig.java`:

```java
package com.demo.fleet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                    .allowedOrigins(
                        "http://localhost:3000",  // 移动端 Demo
                        "http://localhost:5173"   // Vue3 管理端
                    )
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true)
                    .maxAge(3600);
            }
        };
    }
}
```

---

## 五、WebSocket 实时通信

### 5.1 Spring Boot WebSocket 配置

`WebSocketConfig.java`:

```java
package com.demo.fleet.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
            .setAllowedOrigins("http://localhost:3000", "http://localhost:5173")
            .withSockJS();
    }
}
```

### 5.2 Vue3 WebSocket 客户端

`src/composables/useWebSocket.ts`:

```typescript
import { ref, onMounted, onUnmounted } from 'vue'
import SockJS from 'sockjs-client'
import { Client } from '@stomp/stompjs'

export function useWebSocket() {
  const stompClient = ref<Client | null>(null)
  const connected = ref(false)

  const connect = () => {
    stompClient.value = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      onConnect: () => {
        connected.value = true
        console.log('WebSocket 已连接')
      },
      onDisconnect: () => {
        connected.value = false
      }
    })
    stompClient.value.activate()
  }

  const subscribe = (topic: string, callback: (message: any) => void) => {
    if (stompClient.value?.connected) {
      return stompClient.value.subscribe(topic, (message) => {
        callback(JSON.parse(message.body))
      })
    }
  }

  onMounted(connect)
  onUnmounted(() => stompClient.value?.deactivate())

  return { connected, subscribe }
}
```

---

## 六、安全验证流程

### 6.1 双重验证流程

```
用户发起操作
    │
    ▼
┌─────────────────┐
│ 1. 身份验证     │ ← JWT Token 验证
└────────┬────────┘
         ▼
┌─────────────────┐
│ 2. 权限校验     │ ← 检查用户是否有操作权限
└────────┬────────┘
         ▼
┌─────────────────┐
│ 3. 车辆唯一性   │ ← 确认目标车辆 ID 正确
└────────┬────────┘
         ▼
┌─────────────────┐
│ 4. 安全确认     │ ← 用户最终确认
└────────┬────────┘
         ▼
    执行操作
```

### 6.2 Spring Boot 安全拦截器

`SecurityInterceptor.java`:

```java
package com.demo.fleet.security;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SecurityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, 
                           HttpServletResponse response, 
                           Object handler) {
        // 1. 验证 JWT Token
        String token = request.getHeader("Authorization");
        if (!isValidToken(token)) {
            response.setStatus(401);
            return false;
        }

        // 2. 验证权限
        String permission = getRequiredPermission(request);
        if (!hasPermission(getCurrentUser(token), permission)) {
            response.setStatus(403);
            return false;
        }

        return true;
    }
}
```

---

## 七、部署配置

### 7.1 环境变量

`.env`:

```
# Spring Boot
SPRING_BOOT_PORT=8080
DB_URL=jdbc:mysql://localhost:3306/demo_fleet
DB_USERNAME=root
DB_PASSWORD=your_password
GLM_API_KEY=your_glm_api_key

# Node.js Proxy
PROXY_PORT=3000

# Vue3
VITE_APP_TITLE=德莫后台管理
```

### 7.2 Docker Compose

`docker-compose.yml`:

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: your_password
      MYSQL_DATABASE: demo_fleet
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  spring-boot:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/demo_fleet
      - SPRING_DATASOURCE_USERNAME=root
      - SPRING_DATASOURCE_PASSWORD=your_password
    depends_on:
      - mysql

  node-proxy:
    build: ./proxy
    ports:
      - "3000:3000"
    environment:
      - SPRING_BOOT_URL=http://spring-boot:8080
    depends_on:
      - spring-boot

  vue-admin:
    build: ./frontend
    ports:
      - "5173:80"
    depends_on:
      - spring-boot

volumes:
  mysql_data:
```

---

*配置文档版本: v1.0*
*最后更新: 2026-04-24*
