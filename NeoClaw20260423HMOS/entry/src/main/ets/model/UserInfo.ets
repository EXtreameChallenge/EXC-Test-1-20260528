/**
 * 用户信息数据模型
 */

export interface UserInfo {
  username: string;      // 用户名/工号
  password?: string;     // 密码（仅在注册/登录时使用）
  name: string;          // 姓名
  role: string;          // 角色："admin"/"operator"/"driver"/"viewer"
  level: string;         // 等级："A级"/"B级"/"C级"/"D级"
  phone: string;         // 手机号
  avatar: string | null; // 头像DataURL
  permissions?: string[]; // 权限列表
}

// 用户角色类型
export enum UserRole {
  ADMIN = 'admin',
  OPERATOR = 'operator',
  DRIVER = 'driver',
  VIEWER = 'viewer'
}

// 用户等级类型
export enum UserLevel {
  A = 'A级',
  B = 'B级',
  C = 'C级',
  D = 'D级'
}