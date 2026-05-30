import router from '@ohos.router'
import AppTheme from '../constants/Theme'

@Entry
@Component
struct Index {
  @State isRegisterMode: boolean = false
  @State account: string = ''
  @State password: string = ''
  @State regName: string = ''
  @State regPhone: string = ''
  @State regPassword: string = ''
  @State selectedRole: string = 'admin'
  @State accountError: string = ''
  @State passwordError: string = ''
  @State regNameError: string = ''
  @State regPhoneError: string = ''
  @State regPasswordError: string = ''
  @State isLoading: boolean = false
  @State orb1Y: number = 0
  @State orb2Y: number = 0
  @State btnScale: number = 1

  aboutToAppear() {
    // 预览器模式下禁用动画，避免崩溃
    try {
      this.startOrbAnimation()
    } catch (e) {
      console.warn('Animation initialization failed:', e)
    }
  }

  startOrbAnimation() {
    // 预览器模式下禁用动画，避免崩溃
    try {
      animateTo({ duration: 10000, iterations: -1, curve: AppTheme.Curves.easeInOut }, () => {
        this.orb1Y = -50
      })
      animateTo({ duration: 10000, iterations: -1, curve: AppTheme.Curves.easeInOut, delay: 4000 }, () => {
        this.orb2Y = -40
      })
    } catch (e) {
      console.warn('Animation failed:', e)
    }
  }

  build() {
    Stack({ alignContent: Alignment.Center }) {
      // 背景层 - 响应式设计
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
        .opacity(0.08)
        .blur(80)
        .position({ x: '-10%', y: '-10%' })
        .translate({ y: this.orb1Y })

      Column() {}
        .width('30%')
        .aspectRatio(1)
        .borderRadius('50%')
        .linearGradient({ angle: 135, colors: [[AppTheme.Colors.primaryLight, 0], ['transparent', 1]] })
        .opacity(0.06)
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

      // 主内容区 - 响应式布局
      Scroll() {
        Column({ space: AppTheme.Spacing.base }) {
          // Logo区域
          Row() {
            Text('🦞')
              .fontSize(AppTheme.FontSize.xxxl)
          }
          .width(AppTheme.Spacing.xxxl)
          .height(AppTheme.Spacing.xxxl)
          .borderRadius(AppTheme.Radius.base)
          .justifyContent(FlexAlign.Center)
          .alignItems(VerticalAlign.Center)
          .linearGradient({ angle: 135, colors: [['#ef4444', 0], ['#f97316', 1]] })
          .shadow(AppTheme.Shadows.lg)

          // 标题区域
          Column({ space: AppTheme.Spacing.xs }) {
            Text('轻行Claw')
              .fontSize(AppTheme.FontSize.xxl)
              .fontWeight(800)
              .fontColor(AppTheme.Colors.textPrimary)

            Text('AI车队智能体管理平台')
              .fontSize(AppTheme.FontSize.sm)
              .fontColor(AppTheme.Colors.textTertiary)
          }
          .alignItems(HorizontalAlign.Center)

          // 表单区域
          Column() {
            if (!this.isRegisterMode) {
              this.LoginForm()
            } else {
              this.RegisterForm()
            }
          }
          .width('100%')
          .constraintSize({ maxWidth: AppTheme.Breakpoints.mobile })

          // 切换模式
          Row() {
            Text(this.isRegisterMode ? '已有账号？' : '还没有账号？')
              .fontSize(AppTheme.FontSize.sm)
              .fontColor(AppTheme.Colors.textTertiary)
            
            Text(this.isRegisterMode ? '返回登录' : '立即注册')
              .fontSize(AppTheme.FontSize.sm)
              .fontColor(AppTheme.Colors.primary)
              .fontWeight(600)
              .margin({ left: AppTheme.Spacing.xs })
              .onClick(() => {
                animateTo({ 
                  duration: AppTheme.Animation.normal, 
                  curve: AppTheme.Curves.easeInOut 
                }, () => {
                  this.isRegisterMode = !this.isRegisterMode
                  this.clearErrors()
                })
              })
          }
        }
        .width('100%')
        .padding({ 
          left: AppTheme.Spacing.base, 
          right: AppTheme.Spacing.base,
          top: AppTheme.Spacing.xxl,
          bottom: AppTheme.Spacing.xxl
        })
        .alignItems(HorizontalAlign.Center)
      }
      .width('100%')
      .height('100%')
      .scrollBar(BarState.Off)
    }
    .width('100%')
    .height('100%')
  }

  @Builder
  LoginForm() {
    Column({ space: AppTheme.Spacing.base }) {
      this.FormInput(
        '手机号 / 工号',
        this.account,
        '请输入手机号或工号',
        (v: string) => { this.account = v; this.accountError = '' },
        this.accountError,
        false
      )

      this.FormInput(
        '密码',
        this.password,
        '请输入密码',
        (v: string) => { this.password = v; this.passwordError = '' },
        this.passwordError,
        true
      )

      this.AuthButton('登 录', this.handleLogin)
    }
  }

  @Builder
  RegisterForm() {
    Column({ space: AppTheme.Spacing.base }) {
      this.FormInput(
        '姓名',
        this.regName,
        '请输入真实姓名',
        (v: string) => { this.regName = v; this.regNameError = '' },
        this.regNameError,
        false
      )

      this.FormInput(
        '手机号',
        this.regPhone,
        '请输入手机号',
        (v: string) => { this.regPhone = v; this.regPhoneError = '' },
        this.regPhoneError,
        false
      )

      this.FormInput(
        '密码',
        this.regPassword,
        '至少6位密码',
        (v: string) => { this.regPassword = v; this.regPasswordError = '' },
        this.regPasswordError,
        true
      )

      this.RoleSelection()

      this.AuthButton('注 册', this.handleRegister)
    }
  }

  @Builder
  FormInput(label: string, value: string, placeholder: string, onChange: (v: string) => void, error: string, isPassword: boolean = false) {
    Column({ space: AppTheme.Spacing.xs }) {
      Text(label)
        .fontSize(AppTheme.FontSize.sm)
        .fontWeight(600)
        .fontColor(AppTheme.Colors.textSecondary)
        .alignSelf(ItemAlign.Start)

      TextInput({ text: value, placeholder: placeholder })
        .type(isPassword ? InputType.Password : InputType.Normal)
        .width('100%')
        .height(AppTheme.Spacing.xxxl)
        .borderRadius(AppTheme.Radius.base)
        .fontSize(AppTheme.FontSize.base)
        .fontColor(AppTheme.Colors.textPrimary)
        .placeholderColor(AppTheme.Colors.textTertiary)
        .backgroundColor('#0fffffff')
        .border({ 
          width: error ? 1 : 0.5, 
          color: error ? AppTheme.Colors.error : AppTheme.Colors.border, 
          radius: AppTheme.Radius.base 
        })
        .padding({ left: AppTheme.Spacing.base, right: AppTheme.Spacing.base })
        .onChange(onChange)

      if (error) {
        Text(error)
          .fontSize(AppTheme.FontSize.xs)
          .fontColor(AppTheme.Colors.error)
          .alignSelf(ItemAlign.Start)
      }
    }
    .width('100%')
  }

  @Builder
  RoleSelection() {
    Column({ space: AppTheme.Spacing.sm }) {
      Text('选择身份')
        .fontSize(AppTheme.FontSize.sm)
        .fontWeight(600)
        .fontColor(AppTheme.Colors.textSecondary)
        .alignSelf(ItemAlign.Start)

      Grid() {
        GridItem() {
          this.RoleOption('👔', '管理员', '全权限管理', 'admin')
        }
        GridItem() {
          this.RoleOption('👷', '操作员', '调度与查看', 'operator')
        }
        GridItem() {
          this.RoleOption('🚗', '驾驶员', '车辆操作', 'driver')
        }
        GridItem() {
          this.RoleOption('👀', '观察者', '仅查看数据', 'viewer')
        }
      }
      .columnsTemplate('1fr 1fr')
      .columnsGap(AppTheme.Spacing.sm)
      .rowsGap(AppTheme.Spacing.sm)
    }
  }

  @Builder
  RoleOption(icon: string, name: string, desc: string, role: string) {
    Column({ space: AppTheme.Spacing.xs }) {
      Text(icon)
        .fontSize(AppTheme.FontSize.xl)
      
      Text(name)
        .fontSize(AppTheme.FontSize.sm)
        .fontWeight(600)
        .fontColor(AppTheme.Colors.textPrimary)
      
      Text(desc)
        .fontSize(AppTheme.FontSize.xs)
        .fontColor(AppTheme.Colors.textTertiary)
    }
    .width('100%')
    .padding(AppTheme.Spacing.sm)
    .borderRadius(AppTheme.Radius.base)
    .alignItems(HorizontalAlign.Center)
    .backgroundColor(this.selectedRole === role ? '#1a4a9eff' : '#0fffffff')
    .border({
      width: this.selectedRole === role ? 1 : 0.5,
      color: this.selectedRole === role ? AppTheme.Colors.primary : AppTheme.Colors.border,
      radius: AppTheme.Radius.base
    })
    .shadow(this.selectedRole === role ? AppTheme.Shadows.base : { radius: 0, color: '#00000000' })
    .onClick(() => {
      animateTo({ 
        duration: AppTheme.Animation.normal, 
        curve: AppTheme.Curves.friction 
      }, () => { 
        this.selectedRole = role 
      })
    })
  }

  @Builder
  AuthButton(text: string, handler: () => void) {
    Button(text)
      .width('100%')
      .height(AppTheme.Spacing.xxxl)
      .borderRadius(AppTheme.Radius.lg)
      .fontSize(AppTheme.FontSize.lg)
      .fontWeight(700)
      .fontColor(Color.White)
      .linearGradient({ 
        angle: 135, 
        colors: [[AppTheme.Colors.primary, 0], [AppTheme.Colors.primaryLight, 1]] 
      })
      .shadow(AppTheme.Shadows.lg)
      .enabled(!this.isLoading)
      .scale({ x: this.btnScale, y: this.btnScale })
      .onClick(() => {
        // 按钮点击反馈
        animateTo({ 
          duration: AppTheme.Animation.fast, 
          curve: AppTheme.Curves.sharp 
        }, () => { 
          this.btnScale = 0.95 
        })
        
        setTimeout(() => {
          animateTo({ 
            duration: AppTheme.Animation.fast, 
            curve: AppTheme.Curves.sharp 
          }, () => { 
            this.btnScale = 1 
          })
        }, AppTheme.Animation.fast)
        
        handler()
      })
  }

  clearErrors() {
    this.accountError = ''
    this.passwordError = ''
    this.regNameError = ''
    this.regPhoneError = ''
    this.regPasswordError = ''
  }

  handleAuth() {
    this.clearErrors()
    if (this.isRegisterMode) {
      this.handleRegister()
    } else {
      this.handleLogin()
    }
  }

  handleLogin() {
    let valid = true
    if (!this.account) { this.accountError = '请输入账号'; valid = false }
    if (!this.password) { this.passwordError = '请输入密码'; valid = false }
    if (!valid) return

    const user = REGISTERED_USERS.find((u: UserRecord) => u.account === this.account && u.password === this.password)
    if (!user) {
      this.accountError = '账号或密码错误'
      return
    }

    const config = ROLE_CONFIG[user.role]
    AppStorage.setOrCreate<string>('currentUser', JSON.stringify({
      name: user.name, role: user.role, phone: user.phone,
      level: config.level, permissions: config.permissions
    }))

    this.navigateToMain()
  }

  handleRegister() {
    let valid = true
    if (!this.regName) { this.regNameError = '请输入姓名'; valid = false }
    if (this.regPhone.length < 11) { this.regPhoneError = '请输入有效手机号'; valid = false }
    if (this.regPassword.length < 6) { this.regPasswordError = '密码至少6位'; valid = false }
    if (!valid) return

    const config = ROLE_CONFIG[this.selectedRole]
    AppStorage.setOrCreate<string>('currentUser', JSON.stringify({
      name: this.regName, role: this.selectedRole, phone: this.regPhone,
      level: config.level, permissions: config.permissions
    }))

    this.navigateToMain()
  }

  navigateToMain() {
    this.isLoading = true
    setTimeout(() => {
      this.isLoading = false
      router.replaceUrl({ url: 'pages/Main' })
    }, AppTheme.Animation.normal)
  }
}

interface RoleConfigItem {
  label: string
  level: string
  permissions: string[]
}

const ROLE_CONFIG: Record<string, RoleConfigItem> = {
  'admin': { label: '管理员', level: 'A级', permissions: ['车队管理', '任务调度', '能源管理', '运营分析', 'AI智能体', '故障管理', '车辆管理', '系统设置'] },
  'operator': { label: '操作员', level: 'B级', permissions: ['车队管理', '任务调度', '能源管理', '运营分析', 'AI智能体'] },
  'driver': { label: '驾驶员', level: 'C级', permissions: ['车辆操作', '查看状态', 'AI智能体'] },
  'viewer': { label: '观察者', level: 'D级', permissions: ['查看状态', '运营分析'] }
}

interface UserRecord {
  account: string
  password: string
  name: string
  role: string
  phone: string
}

const REGISTERED_USERS: Array<UserRecord> = [
  { account: 'admin', password: '123456', name: '张运营', role: 'admin', phone: '13800000001' },
  { account: 'operator1', password: '123456', name: '李调度', role: 'operator', phone: '13800000002' },
  { account: 'driver1', password: '123456', name: '王师傅', role: 'driver', phone: '13800000003' }
]