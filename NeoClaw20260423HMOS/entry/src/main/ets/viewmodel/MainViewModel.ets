/**
 * 主ViewModel - 管理应用全局状态
 */

import { Vehicle, VehicleStatusType } from '../model/Vehicle';
import { ChatMessage, MessageFrom } from '../model/ChatMessage';
import { UserInfo } from '../model/UserInfo';
import { LogEntry } from '../model/LogEntry';
import { InitialData, Scenes, Colors } from '../common/Constants';

export class MainViewModel {
  // 应用状态
  @State showLogin: boolean = true;
  @State currentTab: string = 'chat';
  @State isChatExpanded: boolean = false;
  
  // 数据状态
  @State vehicles: Vehicle[] = [];
  @State chatMessages: ChatMessage[] = [];
  @State logs: LogEntry[] = [];
  @State currentUser: UserInfo | null = null;
  
  // UI状态
  @State showTyping: boolean = false;
  @State showSecurityModal: boolean = false;
  @State showConfirmCard: boolean = false;
  @State showVehicleModal: boolean = false;
  @State showCompanyModal: boolean = false;
  @State showProfileModal: boolean = false;
  @State showPermissionsModal: boolean = false;
  @State showApiInfoModal: boolean = false;
  @State showAboutModal: boolean = false;
  @State showSecurityLogModal: boolean = false;
  
  // 临时数据
  @State confirmCardData: any = null;
  @State selectedVehicle: Vehicle | null = null;
  @State sceneChips: Array<{text: string, cmd: string}> = [];
  
  constructor() {
    this.loadInitialData();
  }

  // 加载初始数据
  private loadInitialData(): void {
    this.vehicles = InitialData.VEHICLES;
    this.logs = InitialData.LOGS;
    this.sceneChips = Scenes.getRecommendations();
  }

  // 标签页切换
  goTab(tab: string): void {
    this.currentTab = tab;
    
    // 如果是聊天页，展开导航岛
    if (tab === 'chat') {
      this.isChatExpanded = true;
    }
  }

  // 添加聊天消息
  addMessage(html: string, from: MessageFrom): void {
    const time = new Date().toLocaleTimeString('zh-CN', { 
      hour: '2-digit', 
      minute: '2-digit' 
    });
    
    this.chatMessages.push({
      html,
      from,
      time,
      id: `msg-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`
    });
    
    // 限制消息数量，防止内存溢出
    if (this.chatMessages.length > 100) {
      this.chatMessages = this.chatMessages.slice(-50);
    }
  }

  // 显示打字指示器
  showTypingIndicator(): void {
    this.showTyping = true;
  }

  // 隐藏打字指示器
  hideTypingIndicator(): void {
    this.showTyping = false;
  }

  // 显示安全验证弹窗
  showSecurityVerification(): void {
    this.showSecurityModal = true;
  }

  // 隐藏安全验证弹窗
  hideSecurityVerification(): void {
    this.showSecurityModal = false;
  }

  // 显示确认卡片
  showConfirmCard(data: any): void {
    this.confirmCardData = data;
    this.showConfirmCard = true;
  }

  // 隐藏确认卡片
  hideConfirmCard(): void {
    this.showConfirmCard = false;
    this.confirmCardData = null;
  }

  // 显示车辆管理弹窗
  showVehicleManagement(vehicle?: Vehicle): void {
    this.selectedVehicle = vehicle || null;
    this.showVehicleModal = true;
  }

  // 隐藏车辆管理弹窗
  hideVehicleManagement(): void {
    this.showVehicleModal = false;
    this.selectedVehicle = null;
  }

  // 显示公司信息弹窗
  showCompanyInformation(): void {
    this.showCompanyModal = true;
  }

  // 隐藏公司信息弹窗
  hideCompanyInformation(): void {
    this.showCompanyModal = false;
  }

  // 显示个人资料弹窗
  showProfileEdit(): void {
    this.showProfileModal = true;
  }

  // 隐藏个人资料弹窗
  hideProfileEdit(): void {
    this.showProfileModal = false;
  }

  // 显示权限管理弹窗
  showPermissionsManagement(): void {
    this.showPermissionsModal = true;
  }

  // 隐藏权限管理弹窗
  hidePermissionsManagement(): void {
    this.showPermissionsModal = false;
  }

  // 显示API信息弹窗
  showApiInformation(): void {
    this.showApiInfoModal = true;
  }

  // 隐藏API信息弹窗
  hideApiInformation(): void {
    this.showApiInfoModal = false;
  }

  // 显示关于弹窗
  showAboutInformation(): void {
    this.showAboutModal = true;
  }

  // 隐藏关于弹窗
  hideAboutInformation(): void {
    this.showAboutModal = false;
  }

  // 显示安全日志弹窗
  showSecurityLog(): void {
    this.showSecurityLogModal = true;
  }

  // 隐藏安全日志弹窗
  hideSecurityLog(): void {
    this.showSecurityLogModal = false;
  }

  // 添加日志
  addLog(text: string, type: string = 'info'): void {
    const time = new Date().toLocaleTimeString('zh-CN', { 
      hour: '2-digit', 
      minute: '2-digit' 
    });
    
    this.logs.unshift({
      time,
      text,
      type
    });
    
    // 限制日志数量
    if (this.logs.length > 50) {
      this.logs = this.logs.slice(0, 50);
    }
  }

  // 更新车辆状态
  updateVehicleStatus(vehicleId: string, status: VehicleStatusType, task?: string): void {
    const vehicleIndex = this.vehicles.findIndex(v => v.id === vehicleId);
    if (vehicleIndex !== -1) {
      this.vehicles[vehicleIndex].status = status;
      if (task) {
        this.vehicles[vehicleIndex].task = task;
      }
      
      // 触发UI更新
      this.vehicles = [...this.vehicles];
    }
  }

  // 批量更新车辆状态
  updateVehiclesStatus(vehicleIds: string[], status: VehicleStatusType, task?: string): void {
    vehicleIds.forEach(vehicleId => {
      this.updateVehicleStatus(vehicleId, status, task);
    });
  }

  // 获取车辆统计
  getVehicleStats(): { total: number, idle: number, busy: number, charge: number, fault: number } {
    return {
      total: this.vehicles.length,
      idle: this.vehicles.filter(v => v.status === VehicleStatusType.IDLE).length,
      busy: this.vehicles.filter(v => v.status === VehicleStatusType.BUSY).length,
      charge: this.vehicles.filter(v => v.status === VehicleStatusType.CHARGE).length,
      fault: this.vehicles.filter(v => v.status === VehicleStatusType.FAULT).length
    };
  }

  // 获取低电量车辆
  getLowBatteryVehicles(): Vehicle[] {
    return this.vehicles.filter(v => v.battery < 30);
  }

  // 获取可调度车辆
  getAvailableVehicles(): Vehicle[] {
    return this.vehicles.filter(v => v.status === VehicleStatusType.IDLE);
  }

  // 获取故障车辆
  getFaultVehicles(): Vehicle[] {
    return this.vehicles.filter(v => v.status === VehicleStatusType.FAULT);
  }

  // 用户登录
  login(user: UserInfo): void {
    this.currentUser = user;
    this.showLogin = false;
    
    // 发送欢迎消息
    setTimeout(() => {
      this.addMessage(`
        <div class="welcome-message">
          <div class="welcome-title">🚀 智能体就绪</div>
          <div class="welcome-content">
            <p>您好，${user.name}！我是轻行Claw AI智能体，随时为您服务。</p>
            <p>当前车队状态：${this.getVehicleStats().total}辆车，${this.getVehicleStats().idle}辆待命。</p>
            <p>您可以通过语音或文字与我交互，也可以使用下方的场景推荐快速操作。</p>
          </div>
        </div>
      `, MessageFrom.AI);
    }, 1000);
  }

  // 用户登出
  logout(): void {
    this.currentUser = null;
    this.showLogin = true;
    this.chatMessages = [];
    this.isChatExpanded = false;
    this.currentTab = 'chat';
  }

  // 切换导航岛展开状态
  toggleChatExpansion(): void {
    this.isChatExpanded = !this.isChatExpanded;
  }

  // 更新场景推荐
  updateSceneRecommendations(): void {
    this.sceneChips = Scenes.getRecommendations();
  }
}

export default MainViewModel;