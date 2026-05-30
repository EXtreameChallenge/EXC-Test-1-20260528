/**
 * 认证ViewModel - 管理用户登录注册
 */

import { UserInfo } from '../model/UserInfo';
import HttpUtil from '../common/HttpUtil';
import PrefsUtil from '../common/PrefsUtil';
import { InitialData, Roles } from '../common/Constants';

export class AuthViewModel {
  // 登录状态
  @State loginAccount: string = '';
  @State loginPassword: string = '';
  @State loginError: string = '';
  
  // 注册状态
  @State registerName: string = '';
  @State registerPhone: string = '';
  @State registerPassword: string = '';
  @State registerRole: string = Roles.OPERATOR;
  @State registerError: string = '';
  @State regPhoneErr: string = '';
  
  // UI状态
  @State isLoginMode: boolean = true;
  @State isLoading: boolean = false;
  
  // 本地用户存储
  private localUsers: UserInfo[] = [];

  constructor() {
    this.loadLocalUsers();
  }

  // 加载本地用户
  private async loadLocalUsers(): Promise<void> {
    try {
      this.localUsers = await PrefsUtil.getLocalUsers() || InitialData.USERS;
    } catch (error) {
      console.error('加载本地用户失败:', error);
      this.localUsers = InitialData.USERS;
    }
  }

  // 切换登录/注册模式
  toggleMode(): void {
    this.isLoginMode = !this.isLoginMode;
    this.clearErrors();
    this.clearForm();
  }

  // 处理登录
  async handleLogin(): Promise<UserInfo | null> {
    // 验证输入
    if (!this.loginAccount.trim()) {
      this.loginError = '请输入手机号或工号';
      return null;
    }
    
    if (!this.loginPassword.trim()) {
      this.loginError = '请输入密码';
      return null;
    }

    this.isLoading = true;
    this.loginError = '';

    try {
      // 首先尝试API登录
      const httpUtil = HttpUtil.getInstance();
      const response = await httpUtil.login(this.loginAccount, this.loginPassword);
      
      if (response && response.token) {
        // API登录成功
        await PrefsUtil.saveToken(response.token);
        await PrefsUtil.saveRefreshToken(response.refreshToken || '');
        
        const userInfo: UserInfo = {
          username: this.loginAccount,
          name: response.name || this.loginAccount,
          role: response.role || Roles.OPERATOR,
          level: response.level || 'B级',
          phone: response.phone || this.loginAccount,
          avatar: response.avatar || null,
          permissions: response.permissions || []
        };
        
        await PrefsUtil.saveCurrentUser(userInfo);
        this.clearForm();
        this.isLoading = false;
        return userInfo;
      }
      
    } catch (apiError) {
      console.log('API登录失败，尝试本地验证:', apiError);
    }

    // API失败，尝试本地验证
    try {
      const user = this.localUsers.find(u => 
        (u.username === this.loginAccount || u.phone === this.loginAccount) && 
        u.password === this.loginPassword
      );

      if (user) {
        // 本地验证成功
        const userInfo: UserInfo = {
          username: user.username,
          name: user.name,
          role: user.role,
          level: user.level,
          phone: user.phone,
          avatar: user.avatar,
          permissions: Roles.PERMISSIONS[user.role] || []
        };
        
        await PrefsUtil.saveCurrentUser(userInfo);
        this.clearForm();
        this.isLoading = false;
        return userInfo;
      } else {
        this.loginError = '账号或密码错误';
      }
    } catch (localError) {
      console.error('本地验证失败:', localError);
      this.loginError = '登录失败，请检查网络连接';
    }

    this.isLoading = false;
    return null;
  }

  // 处理注册
  async handleRegister(): Promise<UserInfo | null> {
    // 验证输入
    if (!this.registerName.trim()) {
      this.registerError = '请输入姓名';
      return null;
    }
    
    if (!this.validatePhone(this.registerPhone)) {
      this.regPhoneErr = '请输入正确的手机号';
      return null;
    }
    
    if (this.registerPassword.length < 6) {
      this.registerError = '密码至少6位';
      return null;
    }

    this.isLoading = true;
    this.registerError = '';
    this.regPhoneErr = '';

    try {
      // 首先尝试API注册
      const httpUtil = HttpUtil.getInstance();
      const userData = {
        username: this.registerPhone,
        password: this.registerPassword,
        name: this.registerName,
        phone: this.registerPhone,
        role: this.registerRole
      };
      
      const response = await httpUtil.register(userData);
      
      if (response && response.token) {
        // API注册成功
        await PrefsUtil.saveToken(response.token);
        await PrefsUtil.saveRefreshToken(response.refreshToken || '');
        
        const userInfo: UserInfo = {
          username: this.registerPhone,
          name: this.registerName,
          role: this.registerRole,
          level: this.getLevelByRole(this.registerRole),
          phone: this.registerPhone,
          avatar: null,
          permissions: Roles.PERMISSIONS[this.registerRole] || []
        };
        
        await PrefsUtil.saveCurrentUser(userInfo);
        this.clearForm();
        this.isLoading = false;
        return userInfo;
      }
      
    } catch (apiError) {
      console.log('API注册失败，尝试本地注册:', apiError);
      
      // 检查是否用户已存在
      if (apiError.message && apiError.message.includes('已存在')) {
        this.regPhoneErr = '该手机号已注册';
        this.isLoading = false;
        return null;
      }
    }

    // API失败，进行本地注册
    try {
      // 检查本地是否已存在
      const existingUser = this.localUsers.find(u => u.phone === this.registerPhone);
      if (existingUser) {
        this.regPhoneErr = '该手机号已注册';
        this.isLoading = false;
        return null;
      }

      // 创建新用户
      const newUser: UserInfo = {
        username: this.registerPhone,
        password: this.registerPassword,
        name: this.registerName,
        role: this.registerRole,
        level: this.getLevelByRole(this.registerRole),
        phone: this.registerPhone,
        avatar: null,
        permissions: Roles.PERMISSIONS[this.registerRole] || []
      };

      // 添加到本地用户列表
      this.localUsers.push(newUser);
      await PrefsUtil.saveLocalUsers(this.localUsers);
      
      // 保存当前用户信息（不保存密码）
      const userInfo: UserInfo = {
        username: newUser.username,
        name: newUser.name,
        role: newUser.role,
        level: newUser.level,
        phone: newUser.phone,
        avatar: newUser.avatar,
        permissions: newUser.permissions
      };
      
      await PrefsUtil.saveCurrentUser(userInfo);
      this.clearForm();
      this.isLoading = false;
      return userInfo;
      
    } catch (localError) {
      console.error('本地注册失败:', localError);
      this.registerError = '注册失败，请重试';
    }

    this.isLoading = false;
    return null;
  }

  // 验证手机号
  private validatePhone(phone: string): boolean {
    const phoneRegex = /^1\d{10}$/;
    return phoneRegex.test(phone);
  }

  // 根据角色获取等级
  private getLevelByRole(role: string): string {
    const levelMap = {
      [Roles.ADMIN]: 'A级',
      [Roles.OPERATOR]: 'B级', 
      [Roles.DRIVER]: 'C级',
      [Roles.VIEWER]: 'D级'
    };
    return levelMap[role] || 'B级';
  }

  // 清空错误信息
  private clearErrors(): void {
    this.loginError = '';
    this.registerError = '';
    this.regPhoneErr = '';
  }

  // 清空表单
  private clearForm(): void {
    this.loginAccount = '';
    this.loginPassword = '';
    this.registerName = '';
    this.registerPhone = '';
    this.registerPassword = '';
    this.registerRole = Roles.OPERATOR;
  }

  // 设置登录账号
  setLoginAccount(account: string): void {
    this.loginAccount = account;
  }

  // 设置登录密码
  setLoginPassword(password: string): void {
    this.loginPassword = password;
  }

  // 设置注册姓名
  setRegisterName(name: string): void {
    this.registerName = name;
  }

  // 设置注册手机号
  setRegisterPhone(phone: string): void {
    this.registerPhone = phone;
  }

  // 设置注册密码
  setRegisterPassword(password: string): void {
    this.registerPassword = password;
  }

  // 设置注册角色
  setRegisterRole(role: string): void {
    this.registerRole = role;
  }
}

export default AuthViewModel;