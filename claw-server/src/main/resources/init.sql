-- ============================================
-- claw_db 数据库初始化脚本
-- 用于 claw-server (Spring Boot) 项目
-- ============================================

CREATE DATABASE IF NOT EXISTS claw_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE claw_db;

-- ============================================
-- 1. 用户认证与权限表
-- ============================================

CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    avatar VARCHAR(500),
    status INT DEFAULT 1 COMMENT '0=禁用 1=启用',
    last_login_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '0=正常 1=已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_key VARCHAR(50) NOT NULL UNIQUE,
    role_name VARCHAR(100) NOT NULL,
    description VARCHAR(200),
    status INT DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    permission_key VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(200)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_role_permission (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 2. 操作日志表
-- ============================================

CREATE TABLE IF NOT EXISTS sys_operation_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    username VARCHAR(50),
    operation VARCHAR(200),
    method VARCHAR(200),
    params TEXT,
    ip VARCHAR(50),
    duration INT,
    status TINYINT DEFAULT 1,
    error_msg TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 3. 车辆管理表
-- ============================================

CREATE TABLE IF NOT EXISTS vehicle (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100),
    model VARCHAR(100),
    type VARCHAR(50),
    status VARCHAR(50) DEFAULT 'standby',
    battery DECIMAL(5,2),
    mileage DECIMAL(10,2),
    location VARCHAR(200),
    latitude DECIMAL(10,6),
    longitude DECIMAL(10,6),
    last_update DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 4. 调度任务表
-- ============================================

CREATE TABLE IF NOT EXISTS dispatch_task (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(200),
    status VARCHAR(50) DEFAULT 'pending',
    destination VARCHAR(200),
    cargo_type VARCHAR(100),
    vehicle_id VARCHAR(50),
    priority VARCHAR(20) DEFAULT 'normal',
    execute_time DATETIME,
    completed_time DATETIME,
    creator_id BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 5. 故障告警表
-- ============================================

CREATE TABLE IF NOT EXISTS fault_alert (
    id VARCHAR(50) PRIMARY KEY,
    level VARCHAR(20),
    message TEXT,
    vehicle_id VARCHAR(50),
    status VARCHAR(50) DEFAULT 'active',
    confirmed_by BIGINT,
    confirmed_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 6. 工单表
-- ============================================

CREATE TABLE IF NOT EXISTS work_order (
    id VARCHAR(50) PRIMARY KEY,
    title VARCHAR(200),
    status VARCHAR(50) DEFAULT 'open',
    priority VARCHAR(20) DEFAULT 'normal',
    vehicle_id VARCHAR(50),
    alert_id VARCHAR(50),
    assignee_id BIGINT,
    description TEXT,
    completed_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 7. 车辆轨迹表
-- ============================================

CREATE TABLE IF NOT EXISTS vehicle_track (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id VARCHAR(50),
    battery DECIMAL(5,2),
    speed DECIMAL(6,2),
    latitude DECIMAL(10,6),
    longitude DECIMAL(10,6),
    heading DECIMAL(5,2),
    recorded_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 8. 电子围栏表
-- ============================================

CREATE TABLE IF NOT EXISTS geofence (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    type VARCHAR(50),
    boundary TEXT,
    rules TEXT,
    status INT DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 9. 充电站表
-- ============================================

CREATE TABLE IF NOT EXISTS charging_station (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    latitude DECIMAL(10,6),
    longitude DECIMAL(10,6),
    total_slots INT,
    available_slots INT,
    power_kw DECIMAL(6,2),
    queue_count INT DEFAULT 0,
    status INT DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 10. 维护预测表
-- ============================================

CREATE TABLE IF NOT EXISTS maintenance_prediction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id VARCHAR(50),
    component VARCHAR(100),
    health_score DECIMAL(5,2),
    predicted_failure_date DATE,
    recommendation TEXT,
    status VARCHAR(50) DEFAULT 'normal',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 11. 协作事件表
-- ============================================

CREATE TABLE IF NOT EXISTS collaboration_event (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_type VARCHAR(50),
    source_user_id BIGINT,
    target_user_id BIGINT,
    source_device VARCHAR(50),
    payload TEXT,
    read_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 12. AI 对话表
-- ============================================

CREATE TABLE IF NOT EXISTS ai_conversation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    title VARCHAR(200),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS ai_message (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    conversation_id BIGINT,
    role VARCHAR(20),
    content TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 初始数据
-- ============================================

-- 管理员角色
INSERT INTO sys_role (id, role_key, role_name, description, status)
VALUES (1, 'admin', '系统管理员', '拥有全部权限', 1)
ON DUPLICATE KEY UPDATE role_name='系统管理员';

-- 普通操作员角色
INSERT INTO sys_role (id, role_key, role_name, description, status)
VALUES (2, 'operator', '操作员', '普通操作员', 1)
ON DUPLICATE KEY UPDATE role_name='操作员';

-- 权限定义
INSERT INTO sys_permission (id, permission_key, description) VALUES
(1, 'PERM_vehicle:manage', '车辆增删改查'),
(2, 'PERM_task:manage', '调度任务管理'),
(3, 'PERM_alert:manage', '告警管理'),
(4, 'PERM_workorder:manage', '工单管理'),
(5, 'PERM_user:manage', '用户管理'),
(6, 'PERM_system:config', '系统配置')
ON DUPLICATE KEY UPDATE description=VALUES(description);

-- 管理员拥有全部权限
INSERT INTO sys_role_permission (role_id, permission_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6)
ON DUPLICATE KEY UPDATE role_id=VALUES(role_id);

-- 操作员拥有车辆和任务权限
INSERT INTO sys_role_permission (role_id, permission_id) VALUES
(2, 1), (2, 2)
ON DUPLICATE KEY UPDATE role_id=VALUES(role_id);

-- 管理员用户 (密码: admin123, BCrypt 加密)
INSERT INTO sys_user (id, username, password, name, phone, status)
VALUES (1, 'admin', '$2b$10$B1Iy0WZa5oeHl1Qxf74Wq.bggfk24V1u7a30zgMJMfmucTbUAoJ.6', '管理员', '13800000000', 1)
ON DUPLICATE KEY UPDATE password=VALUES(password);

-- 管理员角色绑定
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1)
ON DUPLICATE KEY UPDATE user_id=VALUES(user_id);

-- 示例车辆数据
INSERT INTO vehicle (id, name, model, type, status, battery, mileage, location, latitude, longitude) VALUES
('V-001', '配送车-01', 'DM-A1', '小型', 'standby', 87.00, 12847.50, 'A区-3号点', 39.120000, 117.230000),
('V-002', '配送车-02', 'DM-A1', '小型', 'standby', 92.00, 11234.00, 'B区-1号点', 39.125000, 117.235000),
('V-003', '配送车-03', 'DM-B2', '中型', 'delivering', 64.00, 15678.30, 'C区-途中', 39.118000, 117.240000),
('V-004', '配送车-04', 'DM-A1', '小型', 'charging', 12.00, 9876.00, '充电站-2', 39.130000, 117.225000),
('V-005', '配送车-05', 'DM-A1', '小型', 'standby', 71.00, 13456.80, 'D区-2号点', 39.115000, 117.245000),
('V-006', '配送车-06', 'DM-B2', '中型', 'standby', 55.00, 16789.00, 'B区-3号点', 39.128000, 117.232000),
('V-007', '配送车-07', 'DM-A1', '小型', 'standby', 76.00, 10543.20, 'A区-1号点', 39.122000, 117.228000),
('V-008', '配送车-08', 'DM-A1', '小型', 'fault', 41.00, 18234.00, 'B区-3号点', 39.126000, 117.238000)
ON DUPLICATE KEY UPDATE name=VALUES(name);
