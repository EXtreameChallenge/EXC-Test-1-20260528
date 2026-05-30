// 主题常量定义 - 标准化UI设计规范

// 颜色系统接口
interface ColorSystem {
  primary: string;
  primaryLight: string;
  primaryDark: string;
  success: string;
  warning: string;
  error: string;
  info: string;
  white: string;
  black: string;
  background: string;
  backgroundLight: string;
  backgroundDark: string;
  textPrimary: string;
  textSecondary: string;
  textTertiary: string;
  textDisabled: string;
  border: string;
  borderLight: string;
  borderDark: string;
}

// 字体系统接口
interface FontSizeSystem {
  xs: number;
  sm: number;
  base: number;
  lg: number;
  xl: number;
  xxl: number;
  xxxl: number;
  xxxxl: number;
}

// 间距系统接口
interface SpacingSystem {
  xs: number;
  sm: number;
  base: number;
  lg: number;
  xl: number;
  xxl: number;
  xxxl: number;
}

// 圆角系统接口
interface RadiusSystem {
  xs: number;
  sm: number;
  base: number;
  lg: number;
  xl: number;
  xxl: number;
  full: number;
}

// 阴影系统接口
interface ShadowConfig {
  radius: number;
  color: string;
  offsetY: number;
}

interface ShadowSystem {
  sm: ShadowConfig;
  base: ShadowConfig;
  lg: ShadowConfig;
  xl: ShadowConfig;
}

// 动画时长接口
interface AnimationSystem {
  fast: number;
  normal: number;
  slow: number;
  page: number;
}

// 动画曲线接口
interface CurveSystem {
  sharp: Curve;
  easeInOut: Curve;
  easeOut: Curve;
  easeIn: Curve;
  friction: Curve;
  rhythm: Curve;
}

// 响应式断点接口
interface BreakpointSystem {
  mobile: number;
  tablet: number;
  desktop: number;
}

// 安全区域接口
interface SafeAreaSystem {
  top: number;
  bottom: number;
}

export class AppTheme {
  // 颜色系统
  static readonly Colors: ColorSystem = {
    // 主色调
    primary: '#4a9eff',
    primaryLight: '#6366f1',
    primaryDark: '#2563eb',
    
    // 功能色
    success: '#4ade80',
    warning: '#fbbf24',
    error: '#f87171',
    info: '#60a5fa',
    
    // 中性色
    white: '#ffffff',
    black: '#000000',
    background: '#0d1b3e',
    backgroundLight: '#060614',
    backgroundDark: '#0a0520',
    
    // 文字色
    textPrimary: '#f0f0f8',
    textSecondary: '#94a3b8',
    textTertiary: '#64748b',
    textDisabled: '#475569',
    
    // 边框色
    border: '#1affffff',
    borderLight: '#ffffff20',
    borderDark: '#00000020'
  }

  // 字体系统
  static readonly FontSize: FontSizeSystem = {
    xs: 10,      // 极小文字
    sm: 12,      // 小文字
    base: 14,    // 基础文字
    lg: 16,      // 大文字
    xl: 18,      // 特大文字
    xxl: 20,     // 标题
    xxxl: 24,    // 大标题
    xxxxl: 28    // 超大标题
  }

  // 间距系统 (基于8px基数)
  static readonly Spacing: SpacingSystem = {
    xs: 4,       // 4px
    sm: 8,       // 8px
    base: 16,    // 16px
    lg: 24,      // 24px
    xl: 32,      // 32px
    xxl: 48,     // 48px
    xxxl: 64     // 64px
  }

  // 圆角系统
  static readonly Radius: RadiusSystem = {
    xs: 4,       // 极小圆角
    sm: 8,       // 小圆角
    base: 12,    // 基础圆角
    lg: 16,      // 大圆角
    xl: 20,      // 特大圆角
    xxl: 24,     // 超大圆角
    full: 9999   // 完全圆角
  }

  // 阴影系统
  static readonly Shadows: ShadowSystem = {
    sm: { radius: 4, color: '#00000010', offsetY: 2 },
    base: { radius: 8, color: '#00000020', offsetY: 4 },
    lg: { radius: 16, color: '#00000030', offsetY: 8 },
    xl: { radius: 24, color: '#00000040', offsetY: 12 }
  }

  // 动画时长
  static readonly Animation: AnimationSystem = {
    fast: 150,   // 快速动画
    normal: 250, // 标准动画
    slow: 400,   // 慢速动画
    page: 300    // 页面切换动画
  }
  
  // 动画曲线
  static readonly Curves: CurveSystem = {
    sharp: Curve.Sharp,
    easeInOut: Curve.EaseInOut,
    easeOut: Curve.EaseOut,
    easeIn: Curve.EaseIn,
    friction: Curve.Friction,
    rhythm: Curve.Rhythm
  }

  // 响应式断点
  static readonly Breakpoints: BreakpointSystem = {
    mobile: 360,   // 小屏手机
    tablet: 768,   // 平板
    desktop: 1024  // 桌面
  }
  
  // 安全区域
  static readonly SafeArea: SafeAreaSystem = {
    top: 0,
    bottom: 0
  }
}

// 导出主题常量
export default AppTheme