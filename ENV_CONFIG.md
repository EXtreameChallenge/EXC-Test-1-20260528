# 环境变量配置说明

## 快速配置

1. 打开项目根目录下的 `.env` 文件
2. 将 `请替换为你的智谱GLM_API_Key` 替换为你真实的智谱 GLM API Key
3. 其他配置项保持默认即可（本地开发用）

## 配置文件位置

```
XClaw20260429/
├── .env                    # 后端环境变量
├── .env.example            # 配置模板
└── Phone20260423/
    └── .env                # Phone端环境变量
```

## 配置项说明

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| JWT_SECRET | JWT令牌密钥 | ClawFleetManagement2026SecretKeyForJWT |
| GLM_API_KEY | 智谱GLM API密钥 | 需要填写 |
| MYSQL_PASSWORD | MySQL数据库密码 | 123456 |
| DRUID_PASSWORD | Druid监控台密码 | admin123 |
| VITE_GLM_API_KEY | Phone端GLM密钥 | 需要填写 |

## 获取智谱 GLM API Key

1. 访问 https://open.bigmodel.cn/
2. 注册/登录账号
3. 进入控制台 -> API密钥管理
4. 创建新的API密钥

## 启动顺序

1. 确保 MySQL 已启动，数据库 `claw_db` 已创建
2. 启动后端: `cd claw-server && mvn spring-boot:run`
3. 启动PC前端: `cd PC20260426 && npm run dev`
4. 启动Phone端: `cd Phone20260423 && npm run dev`
