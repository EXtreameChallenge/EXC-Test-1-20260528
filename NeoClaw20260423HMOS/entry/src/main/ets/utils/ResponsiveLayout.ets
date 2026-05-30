// 响应式布局工具类 - 管理不同屏幕尺寸的适配
import AppTheme from '../constants/Theme'

export class ResponsiveLayout {
  // 屏幕尺寸信息
  private static screenInfo = {
    width: 0,
    height: 0,
    density: 1,
    orientation: 'portrait' as 'portrait' | 'landscape'
  }

  // 初始化屏幕信息
  static initialize(): void {
    // 这里可以获取实际的屏幕信息
    // 暂时使用默认值
    this.screenInfo = {
      width: 360, // 默认手机宽度
      height: 640, // 默认手机高度
      density: 2, // 默认密度
      orientation: 'portrait'
    }
  }

  // 获取屏幕宽度
  static getScreenWidth(): number {
    return this.screenInfo.width
  }

  // 获取屏幕高度
  static getScreenHeight(): number {
    return this.screenInfo.height
  }

  // 判断屏幕尺寸类型
  static getScreenType(): 'mobile' | 'tablet' | 'desktop' {
    const width = this.getScreenWidth()
    
    if (width < AppTheme.Breakpoints.mobile) {
      return 'mobile'
    } else if (width < AppTheme.Breakpoints.tablet) {
      return 'mobile'
    } else if (width < AppTheme.Breakpoints.desktop) {
      return 'tablet'
    } else {
      return 'desktop'
    }
  }

  // 响应式宽度计算
  static responsiveWidth(baseWidth: number): string {
    const screenType = this.getScreenType()
    
    switch (screenType) {
      case 'mobile':
        return `${baseWidth}px`
      case 'tablet':
        return `${baseWidth * 1.5}px`
      case 'desktop':
        return `${baseWidth * 2}px`
      default:
        return `${baseWidth}px`
    }
  }

  // 响应式高度计算
  static responsiveHeight(baseHeight: number): string {
    const screenType = this.getScreenType()
    
    switch (screenType) {
      case 'mobile':
        return `${baseHeight}px`
      case 'tablet':
        return `${baseHeight * 1.2}px`
      case 'desktop':
        return `${baseHeight * 1.5}px`
      default:
        return `${baseHeight}px`
    }
  }

  // 百分比宽度
  static percentWidth(percent: number): string {
    return `${percent}%`
  }

  // 百分比高度
  static percentHeight(percent: number): string {
    return `${percent}%`
  }

  // 根据屏幕类型获取字体大小
  static responsiveFontSize(baseSize: number): number {
    const screenType = this.getScreenType()
    
    switch (screenType) {
      case 'mobile':
        return baseSize
      case 'tablet':
        return Math.round(baseSize * 1.2)
      case 'desktop':
        return Math.round(baseSize * 1.5)
      default:
        return baseSize
    }
  }

  // 根据屏幕类型获取间距
  static responsiveSpacing(baseSpacing: number): number {
    const screenType = this.getScreenType()
    
    switch (screenType) {
      case 'mobile':
        return baseSpacing
      case 'tablet':
        return Math.round(baseSpacing * 1.5)
      case 'desktop':
        return Math.round(baseSpacing * 2)
      default:
        return baseSpacing
    }
  }

  // 网格布局列数
  static getGridColumns(): number {
    const screenType = this.getScreenType()
    
    switch (screenType) {
      case 'mobile':
        return 2
      case 'tablet':
        return 3
      case 'desktop':
        return 4
      default:
        return 2
    }
  }

  // 安全区域适配
  static getSafeAreaPadding(): { top: number, bottom: number, left: number, right: number } {
    const screenType = this.getScreenType()
    
    switch (screenType) {
      case 'mobile':
        return {
          top: AppTheme.SafeArea.top,
          bottom: AppTheme.SafeArea.bottom,
          left: AppTheme.Spacing.base,
          right: AppTheme.Spacing.base
        }
      case 'tablet':
        return {
          top: AppTheme.SafeArea.top,
          bottom: AppTheme.SafeArea.bottom,
          left: AppTheme.Spacing.lg,
          right: AppTheme.Spacing.lg
        }
      case 'desktop':
        return {
          top: AppTheme.SafeArea.top,
          bottom: AppTheme.SafeArea.bottom,
          left: AppTheme.Spacing.xl,
          right: AppTheme.Spacing.xl
        }
      default:
        return {
          top: AppTheme.SafeArea.top,
          bottom: AppTheme.SafeArea.bottom,
          left: AppTheme.Spacing.base,
          right: AppTheme.Spacing.base
        }
    }
  }

  // 最大宽度限制
  static getMaxWidth(): string {
    const screenType = this.getScreenType()
    
    switch (screenType) {
      case 'mobile':
        return '100%'
      case 'tablet':
        return '768px'
      case 'desktop':
        return '1024px'
      default:
        return '100%'
    }
  }

  // 图片尺寸适配
  static getImageSize(baseSize: number): { width: number, height: number } {
    const screenType = this.getScreenType()
    
    switch (screenType) {
      case 'mobile':
        return { width: baseSize, height: baseSize }
      case 'tablet':
        return { width: baseSize * 1.5, height: baseSize * 1.5 }
      case 'desktop':
        return { width: baseSize * 2, height: baseSize * 2 }
      default:
        return { width: baseSize, height: baseSize }
    }
  }

  // 按钮尺寸适配
  static getButtonSize(): { width: string, height: number } {
    const screenType = this.getScreenType()
    
    switch (screenType) {
      case 'mobile':
        return { width: '100%', height: AppTheme.Spacing['3xl'] }
      case 'tablet':
        return { width: '80%', height: AppTheme.Spacing['3xl'] * 1.2 }
      case 'desktop':
        return { width: '60%', height: AppTheme.Spacing['3xl'] * 1.5 }
      default:
        return { width: '100%', height: AppTheme.Spacing['3xl'] }
    }
  }

  // 卡片尺寸适配
  static getCardSize(): { width: string, height: string } {
    const screenType = this.getScreenType()
    
    switch (screenType) {
      case 'mobile':
        return { width: '100%', height: 'auto' }
      case 'tablet':
        return { width: '48%', height: 'auto' }
      case 'desktop':
        return { width: '32%', height: 'auto' }
      default:
        return { width: '100%', height: 'auto' }
    }
  }

  // 检查是否为横屏
  static isLandscape(): boolean {
    return this.screenInfo.orientation === 'landscape'
  }

  // 横屏适配
  static landscapeAdapter(baseValue: number): number {
    return this.isLandscape() ? baseValue * 0.8 : baseValue
  }

  // 密度适配
  static densityAdapter(baseValue: number): number {
    return baseValue * this.screenInfo.density
  }
}

// 响应式布局辅助函数
export const Responsive = {
  // 快速创建响应式样式
  createStyle(baseStyle: Record<string, any>): Record<string, any> {
    const screenType = ResponsiveLayout.getScreenType()
    
    return {
      ...baseStyle,
      fontSize: ResponsiveLayout.responsiveFontSize(baseStyle.fontSize || AppTheme.FontSize.base),
      padding: ResponsiveLayout.responsiveSpacing(baseStyle.padding || AppTheme.Spacing.base),
      margin: ResponsiveLayout.responsiveSpacing(baseStyle.margin || 0)
    }
  },

  // 创建网格布局
  createGrid(columns: number = ResponsiveLayout.getGridColumns()): string {
    return `repeat(${columns}, 1fr)`
  },

  // 创建弹性布局
  createFlex(direction: 'row' | 'column' = 'row', justify: FlexAlign = FlexAlign.Start, align: VerticalAlign | HorizontalAlign = VerticalAlign.Top): Record<string, any> {
    return {
      display: 'flex',
      flexDirection: direction,
      justifyContent: justify,
      alignItems: align
    }
  }
}

// 默认导出
export default ResponsiveLayout