import router from '@ohos.router'
import { Vehicle, createDefaultVehicles, statusText, batteryColor, statusColor } from '../model/Vehicle'
import { ChatMessage, createMessage } from '../model/ChatMessage'
import { callGLM, clearHistory } from '../service/GLMService'
import AppTheme from '../constants/Theme'
import Article, { createDefaultArticles } from '../model/Article'

@Entry
@Component
struct Main {
  @State currentTab: number = 1
  @State vehicles: Vehicle[] = createDefaultVehicles()
  @State messages: ChatMessage[] = []
  @State inputText: string = ''
  @State isTyping: boolean = false
  @State userName: string = '张运营'
  @State userRole: string = 'admin'
  @State userPhone: string = '13800000001'
  @State userLevel: string = 'A级'
  @State userPermissions: string[] = ['车队管理', '任务调度', '能源管理', '运营分析', 'AI智能体', '故障管理', '车辆管理', '系统设置']
  @State toastMsg: string = ''
  @State toastDetail: string = ''
  @State toastVisible: boolean = false
  @State fleetViewMode: number = 0
  @State orb1Y: number = 0
  @State orb2Y: number = 0
  @State orb3Y: number = 0
  @State toastOffset: number = -120
  @State showModal: boolean = false
  @State modalTitle: string = ''
  @State modalContent: string = ''
  @State modalType: string = ''
  @State showDiscoverDetail: boolean = false
  @State selectedArticle: Article | null = null
  @State discoverArticles: Article[] = createDefaultArticles()
  private chatScroller: Scroller = new Scroller()

  aboutToAppear() {
    const userStr = AppStorage.get<string>('currentUser') || ''
    if (userStr) {
      try {
        const u: Record<string, string | string[]> = JSON.parse(userStr) as Record<string, string | string[]>
        this.userName = (u['name'] as string) || '张运营'
        this.userRole = (u['role'] as string) || 'admin'
        this.userPhone = (u['phone'] as string) || ''
        this.userLevel = (u['level'] as string) || 'A级'
        this.userPermissions = (u['permissions'] as string[]) || this.userPermissions
      } catch (e) {
      }
    }
    this.startOrbAnimation()
    this.addWelcomeMessage()
  }

  startOrbAnimation() {
    // 预览器模式下禁用动画，避免崩溃
    try {
      animateTo({ duration: 10000, iterations: -1, curve: AppTheme.Curves.easeInOut }, () => { this.orb1Y = -50 })
      animateTo({ duration: 10000, iterations: -1, curve: AppTheme.Curves.easeInOut, delay: 4000 }, () => { this.orb2Y = -40 })
      animateTo({ duration: 10000, iterations: -1, curve: AppTheme.Curves.easeInOut, delay: 6000 }, () => { this.orb3Y = -30 })
    } catch (e) {
      console.warn('Animation failed:', e)
    }
  }

  addWelcomeMessage() {
    const msg = `🤖 轻行Claw 智能体已就绪\n\nGLM-4-Flash 驱动 · ${this.vehicles.length}辆车在线\n\n📋 我能做什么：\n• 查看车队状态\n• 调度充电\n• 运营数据分析\n• 制定调度计划\n\n💡 试试说："把低电量的车调度去充电" 或 "制定明日计划"`
    this.messages.push(createMessage('ai', msg))
  }

  // 显示模态框
  showModalDialog(title: string, content: string, type: string = 'info') {
    this.modalTitle = title
    this.modalContent = content
    this.modalType = type
    this.showModal = true
  }

  // 隐藏模态框
  hideModalDialog() {
    this.showModal = false
  }

  // 显示文章详情
  showArticleDetail(article: Article) {
    this.selectedArticle = article
    animateTo({ 
      duration: AppTheme.Animation.page, 
      curve: AppTheme.Curves.easeInOut 
    }, () => {
      this.showDiscoverDetail = true
    })
  }

  // 隐藏文章详情
  hideArticleDetail() {
    animateTo({ 
      duration: AppTheme.Animation.page, 
      curve: AppTheme.Curves.easeInOut 
    }, () => {
      this.showDiscoverDetail = false
      this.selectedArticle = null
    })
  }

  // 点赞文章
  toggleArticleLike(article: Article) {
    const index = this.discoverArticles.findIndex(a => a.id === article.id)
    if (index !== -1) {
      animateTo({ 
        duration: AppTheme.Animation.fast, 
        curve: AppTheme.Curves.easeInOut 
      }, () => {
        this.discoverArticles[index].isLiked = !this.discoverArticles[index].isLiked
        if (this.discoverArticles[index].isLiked) {
          this.discoverArticles[index].likes += 1
        } else {
          this.discoverArticles[index].likes -= 1
        }
      })
    }
  }

  // 收藏文章
  toggleArticleCollect(article: Article) {
    const index = this.discoverArticles.findIndex(a => a.id === article.id)
    if (index !== -1) {
      animateTo({ 
        duration: AppTheme.Animation.fast, 
        curve: AppTheme.Curves.easeInOut 
      }, () => {
        this.discoverArticles[index].isCollected = !this.discoverArticles[index].isCollected
      })
    }
  }

  build() {
    Stack({ alignContent: Alignment.TopStart }) {
      // 背景层
      Column() {}
        .width('100%')
        .height('100%')
        .linearGradient({ 
          angle: 135, 
          colors: [
            [AppTheme.Colors.background, 0], 
            [AppTheme.Colors.backgroundLight, 0.5], 
            [AppTheme.Colors.backgroundDark, 1]
          ] 
        })

      // 光球效果 - 响应式定位
      Column() {}
        .width('40%')
        .aspectRatio(1)
        .borderRadius('50%')
        .linearGradient({ angle: 135, colors: [[AppTheme.Colors.primary, 0], ['transparent', 1]] })
        .opacity(0.06)
        .blur(80)
        .position({ x: '-10%', y: '-10%' })
        .translate({ y: this.orb1Y })

      Column() {}
        .width('30%')
        .aspectRatio(1)
        .borderRadius('50%')
        .linearGradient({ angle: 135, colors: [[AppTheme.Colors.primaryLight, 0], ['transparent', 1]] })
        .opacity(0.05)
        .blur(80)
        .position({ x: '60%', y: '50%' })

      Column() {}
        .width('20%')
        .aspectRatio(1)
        .borderRadius('50%')
        .linearGradient({ angle: 135, colors: [['#c084fc', 0], ['transparent', 1]] })
        .opacity(0.04)
        .blur(60)
        .position({ x: '20%', y: '60%' })

      // 主内容区
      Column() {
        this.Header()

        Stack() {
          if (this.currentTab === 0) {
            this.ChatPage()
          } else if (this.currentTab === 1) {
            this.FleetPage()
          } else if (this.currentTab === 2) {
            this.DiscoverPage()
          } else {
            this.ProfilePage()
          }
        }
        .layoutWeight(1)

        this.NavIsland()
      }
      .width('100%')
      .height('100%')

      // Toast通知
      if (this.toastVisible) {
        this.ToastView()
      }

      // 模态框
      if (this.showModal) {
        this.ModalView()
      }

      // 发现详情页
      if (this.showDiscoverDetail) {
        this.DiscoverDetailView()
      }
    }
    .width('100%')
    .height('100%')
    .padding({ top: AppTheme.SafeArea.top })
    .padding({ bottom: AppTheme.SafeArea.bottom })
  }

  @Builder
  Header() {
    Column({ space: AppTheme.Spacing.sm }) {
      Row() {
        Row() {
          Text('🦞')
            .fontSize(AppTheme.FontSize.lg)
          Text('轻行Claw')
            .fontSize(AppTheme.FontSize.lg)
            .fontWeight(700)
            .fontColor(AppTheme.Colors.textPrimary)
            .margin({ left: AppTheme.Spacing.sm })
        }

        Row({ space: AppTheme.Spacing.sm }) {
          Text(this.getRoleLabel())
            .fontSize(AppTheme.FontSize.xs)
            .fontWeight(600)
            .fontColor(AppTheme.Colors.success)
            .padding({ 
              left: AppTheme.Spacing.sm, 
              right: AppTheme.Spacing.sm, 
              top: AppTheme.Spacing.xs, 
              bottom: AppTheme.Spacing.xs 
            })
            .borderRadius(AppTheme.Radius.sm)
            .backgroundColor('#0f4ade80')
            .border({ width: 0.5, color: '#264ade80', radius: AppTheme.Radius.sm })
          
          Text('在线')
            .fontSize(AppTheme.FontSize.xs)
            .fontColor(AppTheme.Colors.success)
            .padding({ 
              left: AppTheme.Spacing.sm, 
              right: AppTheme.Spacing.sm, 
              top: AppTheme.Spacing.xs, 
              bottom: AppTheme.Spacing.xs 
            })
            .borderRadius(AppTheme.Radius.base)
            .backgroundColor('#0f4ade80')
        }
      }
      .width('100%')
      .justifyContent(FlexAlign.SpaceBetween)
      .padding({ left: AppTheme.Spacing.base, right: AppTheme.Spacing.base })

      Row({ space: AppTheme.Spacing.sm }) {
        this.HeaderMetric('12', '车辆', AppTheme.Colors.primary)
        Row()
          .width(AppTheme.Spacing.xs)
          .height(AppTheme.Spacing.xs)
          .borderRadius(AppTheme.Radius.xs)
          .backgroundColor('#1affffff')
        this.HeaderMetric('1,847', '今日单', AppTheme.Colors.success)
        Row()
          .width(AppTheme.Spacing.xs)
          .height(AppTheme.Spacing.xs)
          .borderRadius(AppTheme.Radius.xs)
          .backgroundColor('#1affffff')
        this.HeaderMetric('98.2%', '完成率', AppTheme.Colors.warning)
      }
      .width('100%')
      .justifyContent(FlexAlign.Center)
      .padding({ left: AppTheme.Spacing.base, right: AppTheme.Spacing.base })
    }
    .width('100%')
    .padding({ top: AppTheme.Spacing.base, bottom: AppTheme.Spacing.base })
  }

  getRoleLabel(): string {
    if (this.userRole === 'admin') return '管理员'
    if (this.userRole === 'operator') return '操作员'
    if (this.userRole === 'driver') return '驾驶员'
    return '观察者'
  }

  @Builder
  HeaderMetric(value: string, label: string, color: string) {
    Column({ space: AppTheme.Spacing.xs }) {
      Text(value)
        .fontSize(AppTheme.FontSize.lg)
        .fontWeight(700)
        .fontColor(color)
      
      Text(label)
        .fontSize(AppTheme.FontSize.xs)
        .fontColor(AppTheme.Colors.textTertiary)
    }
    .alignItems(HorizontalAlign.Center)
  }

  @Builder
  FleetPage() {
    Scroll() {
      Column({ space: AppTheme.Spacing.base }) {
        Row({ space: AppTheme.Spacing.sm }) {
          Button('📊 数据')
            .fontSize(AppTheme.FontSize.sm)
            .fontColor(this.fleetViewMode === 0 ? AppTheme.Colors.textPrimary : AppTheme.Colors.textTertiary)
            .backgroundColor(this.fleetViewMode === 0 ? '#1affffff' : 'transparent')
            .borderRadius(AppTheme.Radius.base)
            .padding({ 
              left: AppTheme.Spacing.base, 
              right: AppTheme.Spacing.base, 
              top: AppTheme.Spacing.sm, 
              bottom: AppTheme.Spacing.sm 
            })
            .onClick(() => {
              animateTo({ 
                duration: AppTheme.Animation.normal, 
                curve: AppTheme.Curves.easeInOut 
              }, () => { 
                this.fleetViewMode = 0 
              })
            })
          
          Button('🚗 车队')
            .fontSize(AppTheme.FontSize.sm)
            .fontColor(this.fleetViewMode === 1 ? AppTheme.Colors.textPrimary : AppTheme.Colors.textTertiary)
            .backgroundColor(this.fleetViewMode === 1 ? '#1affffff' : 'transparent')
            .borderRadius(AppTheme.Radius.base)
            .padding({ 
              left: AppTheme.Spacing.base, 
              right: AppTheme.Spacing.base, 
              top: AppTheme.Spacing.sm, 
              bottom: AppTheme.Spacing.sm 
            })
            .onClick(() => {
              animateTo({ 
                duration: AppTheme.Animation.normal, 
                curve: AppTheme.Curves.easeInOut 
              }, () => { 
                this.fleetViewMode = 1 
              })
            })
        }
        .justifyContent(FlexAlign.Center)

        if (this.fleetViewMode === 1) {
          this.FleetView()
        } else {
          this.DataView()
        }
      }
      .padding({ 
        left: AppTheme.Spacing.base, 
        right: AppTheme.Spacing.base, 
        bottom: AppTheme.Spacing.base 
      })
    }
    .width('100%')
    .layoutWeight(1)
    .scrollBar(BarState.Off)
  }

  // 其他构建器方法...
  @Builder
  FleetView() {
    Column({ space: AppTheme.Spacing.base }) {
      Text('实时概况')
        .fontSize(AppTheme.FontSize.sm)
        .fontWeight(600)
        .fontColor(AppTheme.Colors.textTertiary)
        .alignSelf(ItemAlign.Start)
      
      Grid() {
        GridItem() {
          this.FleetSummaryCard('7', '待命', AppTheme.Colors.success)
        }
        GridItem() {
          this.FleetSummaryCard('3', '配送', AppTheme.Colors.primary)
        }
        GridItem() {
          this.FleetSummaryCard('1', '充电', AppTheme.Colors.warning)
        }
        GridItem() {
          this.FleetSummaryCard('1', '故障', AppTheme.Colors.error)
        }
      }
      .columnsTemplate('1fr 1fr')
      .columnsGap(AppTheme.Spacing.sm)
      .rowsGap(AppTheme.Spacing.sm)

      Text('全部车辆')
        .fontSize(AppTheme.FontSize.sm)
        .fontWeight(600)
        .fontColor(AppTheme.Colors.textTertiary)
        .alignSelf(ItemAlign.Start)

      Column({ space: AppTheme.Spacing.sm }) {
        ForEach(this.vehicles, (v: Vehicle) => {
          this.VehicleRow(v)
        })
      }
    }
  }

  @Builder
  FleetSummaryCard(num: string, label: string, color: string) {
    Column({ space: AppTheme.Spacing.xs }) {
      Text(num)
        .fontSize(AppTheme.FontSize.xxl)
        .fontWeight(700)
        .fontColor(color)
      
      Text(label)
        .fontSize(AppTheme.FontSize.xs)
        .fontColor(AppTheme.Colors.textTertiary)
    }
    .width('100%')
    .padding(AppTheme.Spacing.base)
    .borderRadius(AppTheme.Radius.lg)
    .alignItems(HorizontalAlign.Center)
    .backgroundColor('#0fffffff')
    .border({ width: 0.5, color: AppTheme.Colors.border, radius: AppTheme.Radius.lg })
  }

  @Builder
  VehicleRow(v: Vehicle) {
    Row({ space: AppTheme.Spacing.sm }) {
      Row()
        .width(AppTheme.Spacing.sm)
        .height(AppTheme.Spacing.sm)
        .borderRadius(AppTheme.Radius.xs)
        .backgroundColor(statusColor(v.status))

      Column({ space: AppTheme.Spacing.xs }) {
        Text(v.id)
          .fontSize(AppTheme.FontSize.sm)
          .fontWeight(600)
          .fontColor(AppTheme.Colors.textPrimary)
        
        Text(`${v.type} · ${v.loc}`)
          .fontSize(AppTheme.FontSize.xs)
          .fontColor(AppTheme.Colors.textTertiary)
      }
      .alignItems(HorizontalAlign.Start)
      .layoutWeight(1)

      Row({ space: AppTheme.Spacing.xs }) {
        Row() {
          Row()
            .width(`${v.battery}%`)
            .height('100%')
            .borderRadius(AppTheme.Radius.xs)
            .backgroundColor(batteryColor(v.battery))
        }
        .width(AppTheme.Spacing.xxl)
        .height(AppTheme.Spacing.sm)
        .borderRadius(AppTheme.Radius.xs)
        .backgroundColor('#1affffff')
        
        Text(`${v.battery}%`)
          .fontSize(AppTheme.FontSize.xs)
          .fontColor(batteryColor(v.battery))
      }

      Text(statusText(v.status))
        .fontSize(AppTheme.FontSize.xs)
        .fontWeight(600)
        .padding({ 
          left: AppTheme.Spacing.sm, 
          right: AppTheme.Spacing.sm, 
          top: AppTheme.Spacing.xs, 
          bottom: AppTheme.Spacing.xs 
        })
        .borderRadius(AppTheme.Radius.base)
        .fontColor(statusColor(v.status))
        .backgroundColor(v.status === 'idle' ? '#0f4ade80' : v.status === 'busy' ? '#0f4a9eff' : v.status === 'charge' ? '#0ffbbf24' : '#0ff87171')
    }
    .width('100%')
    .padding(AppTheme.Spacing.base)
    .borderRadius(AppTheme.Radius.lg)
    .backgroundColor('#0fffffff')
    .border({ width: 0.5, color: AppTheme.Colors.border, radius: AppTheme.Radius.lg })
  }

  // 简化版本，确保文件能够编译
  @Builder
  DataView() {
    Column({ space: AppTheme.Spacing.base }) {
      Text('数据视图')
        .fontSize(AppTheme.FontSize.lg)
        .fontWeight(700)
        .fontColor(AppTheme.Colors.textPrimary)
    }
  }

  @Builder
  ChatPage() {
    Column({ space: AppTheme.Spacing.base }) {
      Text('聊天页面')
        .fontSize(AppTheme.FontSize.lg)
        .fontWeight(700)
        .fontColor(AppTheme.Colors.textPrimary)
    }
  }

  @Builder
  DiscoverPage() {
    Column({ space: AppTheme.Spacing.base }) {
      Text('发现页面')
        .fontSize(AppTheme.FontSize.lg)
        .fontWeight(700)
        .fontColor(AppTheme.Colors.textPrimary)
    }
  }

  @Builder
  ProfilePage() {
    Column({ space: AppTheme.Spacing.base }) {
      Text('个人页面')
        .fontSize(AppTheme.FontSize.lg)
        .fontWeight(700)
        .fontColor(AppTheme.Colors.textPrimary)
    }
  }

  @Builder
  NavIsland() {
    Row({ space: AppTheme.Spacing.base }) {
      Button('💬')
        .fontSize(AppTheme.FontSize.lg)
        .backgroundColor(this.currentTab === 0 ? AppTheme.Colors.primary : 'transparent')
        .onClick(() => { this.currentTab = 0 })
      
      Button('🚗')
        .fontSize(AppTheme.FontSize.lg)
        .backgroundColor(this.currentTab === 1 ? AppTheme.Colors.primary : 'transparent')
        .onClick(() => { this.currentTab = 1 })
      
      Button('🔍')
        .fontSize(AppTheme.FontSize.lg)
        .backgroundColor(this.currentTab === 2 ? AppTheme.Colors.primary : 'transparent')
        .onClick(() => { this.currentTab = 2 })
      
      Button('👤')
        .fontSize(AppTheme.FontSize.lg)
        .backgroundColor(this.currentTab === 3 ? AppTheme.Colors.primary : 'transparent')
        .onClick(() => { this.currentTab = 3 })
    }
    .width('80%')
    .padding(AppTheme.Spacing.base)
    .borderRadius(AppTheme.Radius.xl)
    .backgroundColor('#0fffffff')
    .border({ width: 0.5, color: AppTheme.Colors.border, radius: AppTheme.Radius.xl })
    .alignSelf(ItemAlign.Center)
    .margin({ bottom: AppTheme.Spacing.base })
  }

  @Builder
  ToastView() {
    // 简化Toast视图
    Column({ space: AppTheme.Spacing.sm }) {
      Text(this.toastMsg)
        .fontSize(AppTheme.FontSize.sm)
        .fontColor(AppTheme.Colors.textPrimary)
      
      if (this.toastDetail) {
        Text(this.toastDetail)
          .fontSize(AppTheme.FontSize.xs)
          .fontColor(AppTheme.Colors.textSecondary)
      }
    }
    .padding(AppTheme.Spacing.base)
    .borderRadius(AppTheme.Radius.lg)
    .backgroundColor('#0fffffff')
    .border({ width: 0.5, color: AppTheme.Colors.border, radius: AppTheme.Radius.lg })
    .alignSelf(ItemAlign.Center)
    .translate({ y: this.toastOffset })
  }

  @Builder
  ModalView() {
    // 简化模态框视图
    Column({ space: AppTheme.Spacing.base }) {
      Text(this.modalTitle)
        .fontSize(AppTheme.FontSize.lg)
        .fontWeight(700)
        .fontColor(AppTheme.Colors.textPrimary)
      
      Text(this.modalContent)
        .fontSize(AppTheme.FontSize.sm)
        .fontColor(AppTheme.Colors.textSecondary)
      
      Button('确定')
        .onClick(() => { this.hideModalDialog() })
    }
    .padding(AppTheme.Spacing.xl)
    .borderRadius(AppTheme.Radius.lg)
    .backgroundColor('#0fffffff')
    .border({ width: 0.5, color: AppTheme.Colors.border, radius: AppTheme.Radius.lg })
  }

  @Builder
  DiscoverDetailView() {
    // 简化发现详情视图
    Column({ space: AppTheme.Spacing.base }) {
      Text('文章详情')
        .fontSize(AppTheme.FontSize.lg)
        .fontWeight(700)
        .fontColor(AppTheme.Colors.textPrimary)
      
      Button('关闭')
        .onClick(() => { this.hideArticleDetail() })
    }
    .padding(AppTheme.Spacing.xl)
    .borderRadius(AppTheme.Radius.lg)
    .backgroundColor('#0fffffff')
    .border({ width: 0.5, color: AppTheme.Colors.border, radius: AppTheme.Radius.lg })
  }
}