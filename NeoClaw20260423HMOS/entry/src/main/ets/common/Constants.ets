/**
 * 轻行Claw AI车队智能体管理平台 - 常量定义
 * 颜色、角色、状态映射等全局常量
 */
import { Vehicle } from '../model/Vehicle';
import { LogEntry } from '../model/LogEntry';
import { UserInfo } from '../model/UserInfo';

// 颜色系统
export class Colors {
  // 主色板
  static readonly BACKGROUND = '#0a0a1a';
  static readonly PRIMARY = '#4a9eff';
  static readonly SUCCESS = '#4ade80';
  static readonly WARNING = '#fbbf24';
  static readonly ERROR = '#f87171';
  static readonly SPECIAL = '#c084fc';
  static readonly AUXILIARY = '#22d3ee';

  // 文本色
  static readonly TEXT_PRIMARY = '#f0f0f8';
  static readonly TEXT_SECONDARY = '#a0a0b8';
  static readonly TEXT_AUXILIARY = '#606078';

  // 玻璃态色
  static readonly GLASS_LIGHT = '#14FFFFFF'; // rgba(255,255,255,0.08)
  static readonly GLASS_HEAVY = '#1FFFFFFF'; // rgba(255,255,255,0.12)
  static readonly GLASS_BORDER = '#26FFFFFF'; // rgba(255,255,255,0.15)
  static readonly DIVIDER = '#0FFFFFFF'; // rgba(255,255,255,0.06)

  // 渐变色
  static readonly GRADIENT_BRAND = 'linear-gradient(135deg, #4a9eff, #6366f1)';
  static readonly GRADIENT_AVATAR = 'linear-gradient(135deg, #4a9eff, #c084fc)';
  static readonly GRADIENT_SUCCESS = 'linear-gradient(135deg, #4ade80, #22c55e)';
  static readonly GRADIENT_TITLE = 'linear-gradient(135deg, #fff, #a0a0b8)';
  static readonly GRADIENT_LOGO = 'linear-gradient(135deg, #ef4444, #f97316)';

  // 光球渐变
  static readonly ORB_1 = 'radial-gradient(ellipse 600px 600px at 20% 30%, rgba(74,158,255,0.15), transparent 70%)';
  static readonly ORB_2 = 'radial-gradient(ellipse 500px 500px at 80% 20%, rgba(192,132,252,0.12), transparent 70%)';
  static readonly ORB_3 = 'radial-gradient(ellipse 400px 400px at 60% 80%, rgba(34,211,238,0.10), transparent 70%)';
  static readonly ORB_4 = 'radial-gradient(ellipse 500px 500px at 10% 70%, rgba(74,222,128,0.08), transparent 70%)';

  // 根据电量获取颜色
  static getBatteryColor(battery: number): string {
    if (battery > 50) return Colors.SUCCESS;
    if (battery > 20) return Colors.WARNING;
    return Colors.ERROR;
  }
}

// 角色权限项接口
interface RolePermissionItem {
  permissions: string[];
}

// 角色系统
export class Roles {
  static readonly ADMIN = 'admin';
  static readonly OPERATOR = 'operator';
  static readonly DRIVER = 'driver';
  static readonly VIEWER = 'viewer';

  // 角色权限映射
  static readonly PERMISSIONS: Record<string, RolePermissionItem> = {
    'admin': { permissions: ['车队管理', '任务调度', '能源管理', '运营分析', 'AI智能体', '故障管理', '车辆管理', '系统设置'] },
    'operator': { permissions: ['车队管理', '任务调度', '能源管理', '运营分析', 'AI智能体'] },
    'driver': { permissions: ['车辆操作', '查看状态', 'AI智能体'] },
    'viewer': { permissions: ['查看状态', '运营分析'] }
  };

  // 角色显示名称
  static getDisplayName(role: string): string {
    if (role === Roles.ADMIN) return '管理员';
    if (role === Roles.OPERATOR) return '操作员';
    if (role === Roles.DRIVER) return '驾驶员';
    if (role === Roles.VIEWER) return '观察者';
    return '未知角色';
  }

  // 角色图标
  static getIcon(role: string): string {
    if (role === Roles.ADMIN) return '👔';
    if (role === Roles.OPERATOR) return '👷';
    if (role === Roles.DRIVER) return '🚗';
    if (role === Roles.VIEWER) return '👀';
    return '❓';
  }
}

// 车辆状态信息接口
interface StatusInfo {
  backend: string;
  chinese: string;
  color: string;
}

// 车辆状态系统
export class VehicleStatus {
  static readonly IDLE = 'idle';
  static readonly BUSY = 'busy';
  static readonly CHARGE = 'charge';
  static readonly FAULT = 'fault';

  // 状态映射：前端→后端→中文
  static readonly STATUS_MAP: Record<string, StatusInfo> = {
    'idle': { backend: 'standby', chinese: '待命', color: Colors.SUCCESS },
    'busy': { backend: 'delivering', chinese: '配送中', color: Colors.PRIMARY },
    'charge': { backend: 'charging', chinese: '充电中', color: Colors.WARNING },
    'fault': { backend: 'fault', chinese: '故障', color: Colors.ERROR }
  };

  // 获取状态信息
  static getStatusInfo(status: string): StatusInfo {
    const info = VehicleStatus.STATUS_MAP[status];
    return info !== undefined ? info : VehicleStatus.STATUS_MAP[VehicleStatus.IDLE];
  }

  // 获取状态颜色
  static getStatusColor(status: string): string {
    return VehicleStatus.getStatusInfo(status).color;
  }

  // 获取状态中文
  static getStatusChinese(status: string): string {
    return VehicleStatus.getStatusInfo(status).chinese;
  }

  // 获取后端状态
  static getBackendStatus(status: string): string {
    return VehicleStatus.getStatusInfo(status).backend;
  }
}

// 车辆类型系统
export class VehicleTypes {
  static readonly SMALL = '小型';
  static readonly MEDIUM = '中型';
  static readonly LARGE = '大型';

  // 类型颜色映射
  static readonly TYPE_COLORS: Record<string, string> = {
    '小型': Colors.PRIMARY,
    '中型': Colors.SPECIAL,
    '大型': Colors.WARNING
  };

  // 获取类型颜色
  static getTypeColor(type: string): string {
    const color = VehicleTypes.TYPE_COLORS[type];
    return color !== undefined ? color : Colors.TEXT_SECONDARY;
  }
}

// 日志类型系统
export class LogTypes {
  static readonly INFO = 'info';
  static readonly OK = 'ok';
  static readonly WARN = 'warn';

  // 日志类型颜色
  static readonly TYPE_COLORS: Record<string, string> = {
    'info': Colors.PRIMARY,
    'ok': Colors.SUCCESS,
    'warn': Colors.WARNING
  };

  // 获取日志颜色
  static getLogColor(type: string): string {
    const color = LogTypes.TYPE_COLORS[type];
    return color !== undefined ? color : Colors.TEXT_SECONDARY;
  }
}

// 场景推荐项接口
interface RecommendationItem {
  text: string;
  cmd: string;
}

// 场景推荐系统
export class Scenes {
  static readonly MORNING = 'morning'; // 6-11点
  static readonly NOON = 'noon';       // 11-14点
  static readonly AFTERNOON = 'afternoon'; // 14-18点
  static readonly EVENING = 'evening'; // 18-6点

  // 场景推荐内容
  static readonly RECOMMENDATIONS: Record<string, RecommendationItem[]> = {
    'morning': [
      { text: '🔋检查低电量车辆/查看哪些车需要充电', cmd: '检查低电量车辆' },
      { text: '📊今日首单统计/统计今日运营数据', cmd: '今日运营数据' },
      { text: '🚗早高峰调度/调配5辆车去A区', cmd: '调度派车' },
      { text: '⚠️晨检报告/查看所有车辆状态', cmd: '车队状态' }
    ],
    'noon': [
      { text: '🔋午间充电调度/把电量低于30%的车调度去充电', cmd: '充电调度' },
      { text: '📦午高峰准备/调配3辆车去B区', cmd: '调度派车' },
      { text: '📊半日数据/统计今日运营数据', cmd: '今日运营数据' },
      { text: '🚪车厢检查/打开DM-08的车门', cmd: '开门' }
    ],
    'afternoon': [
      { text: '🚗下午配送调度/调配3辆车去C区', cmd: '调度派车' },
      { text: '⚠️故障巡检/查看故障车辆', cmd: '故障处理' },
      { text: '📊运营进度/统计今日运营数据', cmd: '今日运营数据' },
      { text: '🔋充电提醒/检查车辆电量', cmd: '能耗分析' }
    ],
    'evening': [
      { text: '📊今日总结/统计今日运营数据', cmd: '今日运营数据' },
      { text: '🔋夜间充电/安排低电量车辆充电', cmd: '充电调度' },
      { text: '🚗末班调度/查看待命车辆', cmd: '车队状态' },
      { text: '📋明日计划/制定明日调度计划', cmd: '明日计划' }
    ]
  };

  // 获取当前场景
  static getCurrentScene(): string {
    const hour = new Date().getHours();
    if (hour >= 6 && hour < 11) return Scenes.MORNING;
    if (hour >= 11 && hour < 14) return Scenes.NOON;
    if (hour >= 14 && hour < 18) return Scenes.AFTERNOON;
    return Scenes.EVENING;
  }

  // 获取场景推荐
  static getRecommendations(): RecommendationItem[] {
    const items = Scenes.RECOMMENDATIONS[Scenes.getCurrentScene()];
    return items !== undefined ? items : [];
  }
}

// API配置
export class ApiConfig {
  static readonly BASE_URL = 'http://localhost:8080/api/v1';
  static readonly TIMEOUT = 15000;
  static readonly GLM_MODEL = 'glm-4-flash';
  static readonly GLM_MAX_TOKENS = 1024;
  static readonly GLM_TEMPERATURE = 0.7;
  static readonly GLM_TOP_P = 0.9;
}

// 动画配置
export class Animations {
  // 动画时长
  static readonly DURATION_SHORT = 200;
  static readonly DURATION_NORMAL = 350;
  static readonly DURATION_LONG = 500;

  // 光球动画时长
  static readonly ORB_DURATION_1 = 20000;
  static readonly ORB_DURATION_2 = 18000;
  static readonly ORB_DURATION_3 = 22000;
  static readonly ORB_DURATION_4 = 16000;

  // 背景渐变漂移动画时长
  static readonly BG_DRIFT_DURATION_1 = 25000;
  static readonly BG_DRIFT_DURATION_2 = 30000;
}

// 圆角配置
export class BorderRadius {
  static readonly XS = 2;  // 进度条
  static readonly SM = 4;  // 消息气泡顶角
  static readonly MD = 8;  // 状态标签
  static readonly LG = 12; // 按钮和卡片内元素
  static readonly XL = 16; // 弹窗和消息气泡
  static readonly XXL = 24; // 模态框顶部
  static readonly PILL = 26; // 导航岛
  static readonly CIRCLE = 50; // 头像和指示灯
}

// 间距配置
export class Spacing {
  static readonly PAGE_HORIZONTAL = 20; // 页面水平内边距
  static readonly CARD_PADDING = 12;    // 卡片内边距
  static readonly LIST_GAP = 8;        // 列表项间距
  static readonly MESSAGE_GAP = 12;    // 消息间距
  static readonly HEADER_PADDING = 10; // 头部内边距
  static readonly MODAL_PADDING = 16;  // 弹窗内边距
  static readonly CHIP_GAP = 8;        // 芯片间距
  static readonly FORM_GAP = 16;       // 表单组间距
}

// 字体配置
export class Typography {
  static readonly FONT_FAMILY = 'HarmonyOS Sans, Noto Sans SC, sans-serif';
  
  // 字号层级
  static readonly SIZE_XXS = 11;  // 极小
  static readonly SIZE_XS = 12;   // 小字
  static readonly SIZE_SM = 14;   // 正文
  static readonly SIZE_MD = 16;   // 卡片标题
  static readonly SIZE_LG = 20;   // 页面标题
  static readonly SIZE_XL = 32;   // 登录标题

  // 字重
  static readonly WEIGHT_NORMAL = 400;
  static readonly WEIGHT_MEDIUM = 500;
  static readonly WEIGHT_SEMIBOLD = 600;
  static readonly WEIGHT_BOLD = 700;

  // 字间距
  static readonly TRACKING_BRAND = -0.4;
  static readonly TRACKING_TITLE = -0.5;
  static readonly TRACKING_CARD = 0.3;
}

// 存储键名
export class StorageKeys {
  static readonly TOKEN = 'claw_token';
  static readonly REFRESH_TOKEN = 'claw_refresh_token';
  static readonly CURRENT_USER = 'claw_current_user';
  static readonly VEHICLES = 'claw_vehicles';
  static readonly LOGS = 'claw_logs';
  static readonly CHAT_HISTORY = 'claw_chat_history';
  static readonly LOCAL_USERS = 'claw_local_users';
}

// WebSocket主题接口
interface WebSocketTopics {
  VEHICLE: string;
  ALERT: string;
  TASK: string;
  USER_VEHICLE: string;
  USER_ALERT: string;
  USER_COLLAB: string;
}

// WebSocket配置
export class WebSocketConfig {
  static readonly WS_URL = 'ws://localhost:8080/ws';
  static readonly RECONNECT_DELAY = 5000; // 5秒重连
  static readonly HEARTBEAT_INTERVAL = 30000; // 30秒心跳

  // 订阅主题
  static readonly TOPICS: WebSocketTopics = {
    VEHICLE: '/topic/vehicle',
    ALERT: '/topic/alert',
    TASK: '/topic/task',
    USER_VEHICLE: '/user/topic/vehicle',
    USER_ALERT: '/user/topic/alert',
    USER_COLLAB: '/user/topic/collab'
  };
}

// 公司信息接口
interface CompanyInfo {
  name: string;
  address: string;
  phone: string;
  fleetSize: number;
}

// 初始数据
export class InitialData {
  // 初始车辆数据
  static readonly VEHICLES: Vehicle[] = [
    { id: 'DM-01', type: '小型', status: 'idle', battery: 87, loc: 'A区-3号点', task: '待命', todayKm: 42, todayOrders: 8, energyPer100km: 6.1, totalKm: 12847 },
    { id: 'DM-02', type: '小型', status: 'idle', battery: 92, loc: 'B区-1号点', task: '待命', todayKm: 38, todayOrders: 7, energyPer100km: 5.9, totalKm: 11234 },
    { id: 'DM-03', type: '中型', status: 'busy', battery: 64, loc: 'C区-途中配送#1842', task: '配送中', todayKm: 56, todayOrders: 6, energyPer100km: 8.7, totalKm: 15678 },
    { id: 'DM-04', type: '小型', status: 'idle', battery: 45, loc: 'A区-5号点', task: '待命', todayKm: 31, todayOrders: 5, energyPer100km: 6.4, totalKm: 9876 },
    { id: 'DM-05', type: '小型', status: 'busy', battery: 71, loc: 'D区-途中配送#1845', task: '配送中', todayKm: 47, todayOrders: 9, energyPer100km: 6.0, totalKm: 13456 },
    { id: 'DM-06', type: '中型', status: 'charge', battery: 12, loc: '充电站-2', task: '充电中', todayKm: 28, todayOrders: 4, energyPer100km: 8.9, totalKm: 16789 },
    { id: 'DM-07', type: '小型', status: 'idle', battery: 55, loc: 'B区-2号点', task: '待命', todayKm: 35, todayOrders: 6, energyPer100km: 6.3, totalKm: 10543 },
    { id: 'DM-08', type: '小型', status: 'idle', battery: 38, loc: 'A区-1号点', task: '待命', todayKm: 44, todayOrders: 8, energyPer100km: 5.8, totalKm: 8921 },
    { id: 'DM-09', type: '中型', status: 'busy', battery: 53, loc: 'E区-途中配送#1846', task: '配送中', todayKm: 51, todayOrders: 5, energyPer100km: 8.3, totalKm: 14567 },
    { id: 'DM-10', type: '中型', status: 'idle', battery: 29, loc: 'C区-4号点', task: '待命', todayKm: 22, todayOrders: 3, energyPer100km: 8.6, totalKm: 7890 },
    { id: 'DM-11', type: '大型', status: 'fault', battery: 41, loc: 'B区-3号点', task: '传感器异常', todayKm: 15, todayOrders: 2, energyPer100km: 12.1, totalKm: 18234 },
    { id: 'DM-12', type: '小型', status: 'idle', battery: 76, loc: 'D区-2号点', task: '待命', todayKm: 39, todayOrders: 7, energyPer100km: 6.0, totalKm: 12112 }
  ];

  // 初始日志数据
  static readonly LOGS: LogEntry[] = [
    { time: '12:28', text: 'DM-03开始配送#1842', type: 'info' },
    { time: '12:25', text: 'DM-06进入充电站-2', type: 'ok' },
    { time: '12:18', text: 'DM-11上报传感器异常', type: 'warn' },
    { time: '12:10', text: 'DM-05开始配送#1845', type: 'info' },
    { time: '11:58', text: 'DM-09开始配送#1846', type: 'info' },
    { time: '11:42', text: 'DM-02完成配送#1841', type: 'ok' }
  ];

  // 初始用户数据（密码应由后端验证，这里仅用于演示）
  static readonly USERS: UserInfo[] = [
    { username: 'admin', name: '张运营', role: 'admin', level: 'A级', phone: '13800000001', avatar: null },
    { username: 'operator1', name: '李调度', role: 'operator', level: 'B级', phone: '13800000002', avatar: null },
    { username: 'driver1', name: '王师傅', role: 'driver', level: 'C级', phone: '13800000003', avatar: null }
  ];

  // 公司信息
  static readonly COMPANY: CompanyInfo = {
    name: '轻行Claw智行科技有限公司',
    address: '北京市海淀区中关村科技园',
    phone: '010-88888888',
    fleetSize: 12
  };
}