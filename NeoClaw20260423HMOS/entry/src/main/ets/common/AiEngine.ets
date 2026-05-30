/**
 * AI引擎类 - 处理本地AI逻辑和GLM API调用
 */

import HttpUtil from './HttpUtil';
import HtmlBuilder from './HtmlBuilder';
import { ApiConfig, InitialData } from './Constants';

class AiEngine {
  private conversationHistory: Array<{role: string, content: string}> = [];
  private maxHistoryLength: number = 10;

  // 判断是否使用本地处理
  shouldUseLocalProcessing(text: string): boolean {
    const lowerText = text.toLowerCase();
    
    const keywords = [
      // 明日计划相关
      '明日', '计划', '调度', '制定',
      // 车辆状态相关
      '车辆状态', '车队状态', '所有车辆', '全部车辆',
      // 运营数据相关
      '今日数据', '运营数据', '运营', '数据',
      // 能耗相关
      '能耗', '耗电', '电耗',
      // 充电相关
      '充电',
      // 调度派车相关
      '调配', '调度', '派车',
      // 开门相关
      '开门', '打开', '门',
      // 故障相关
      '故障', '维修',
      // 批量操作相关
      '批量',
      // 帮助相关
      '帮助', '能做什么',
      // 状态查询相关
      '状态', '情况', '怎么样', '多少', '统计', '低电量', '电量'
    ];

    return keywords.some(keyword => lowerText.includes(keyword));
  }

  // 处理本地命令
  processLocalCmd(text: string, vehicles: any[]): { reply: string, need_confirm?: boolean, affected_vehicles?: string[], action_summary?: string, action_type?: string } {
    const lowerText = text.toLowerCase();
    
    // 1. 运营数据查询
    if (this.containsAny(lowerText, ['运营数据', '今日数据', '单量', '配送', '运营', '数据'])) {
      return this.handleOperationData();
    }
    
    // 2. 能耗分析
    if (this.containsAny(lowerText, ['能耗', '耗电', '电耗']) && !lowerText.includes('低电量') && !lowerText.includes('充电')) {
      return this.handleEnergyAnalysis();
    }
    
    // 3. 全部车辆状态
    if (this.containsAny(lowerText, ['车辆状态', '车队状态', '所有车辆', '全部车辆', '车队', '车辆', '状态'])) {
      return this.handleAllVehicles(vehicles);
    }
    
    // 4. 充电调度
    if (lowerText.includes('充电')) {
      return this.handleChargeScheduling(vehicles);
    }
    
    // 5. 调度派车
    if (this.containsAny(lowerText, ['调配', '调度', '派车', '需要', '运输', '运货'])) {
      return this.handleDispatch(vehicles);
    }
    
    // 6. 明日计划
    if (this.containsAny(lowerText, ['明日', '计划', '调度', '制定'])) {
      return this.handleTomorrowPlan();
    }
    
    // 7. 开门操作
    if (this.containsAny(lowerText, ['开门', '打开', '门'])) {
      return this.handleOpenDoor(vehicles);
    }
    
    // 8. 故障维修
    if (this.containsAny(lowerText, ['故障', '维修'])) {
      return this.handleFaultRepair(vehicles);
    }
    
    // 9. 批量充电
    if (lowerText.includes('批量') && lowerText.includes('充电')) {
      return this.handleBatchCharge(vehicles);
    }
    
    // 10. 批量调度
    if (lowerText.includes('批量') && lowerText.includes('调度')) {
      return this.handleBatchDispatch(vehicles);
    }
    
    // 11. 帮助
    if (this.containsAny(lowerText, ['帮助', '能做什么'])) {
      return this.handleHelp();
    }
    
    // 默认回复
    return this.handleDefault();
  }

  // 调用GLM API
  async callGLM(userMessage: string, vehicles: any[]): Promise<{ reply: string, need_confirm?: boolean, affected_vehicles?: string[], action_summary?: string, action_type?: string, error?: string }> {
    // 添加用户消息到历史
    this.addToHistory('user', userMessage);

    // 构建系统提示词
    const systemPrompt = this.buildSystemPrompt(vehicles);
    
    // 构建消息数组
    const messages = [
      { role: 'system', content: systemPrompt },
      ...this.getRecentHistory()
    ];

    let retryCount = 0;
    const maxRetries = 2;

    while (retryCount <= maxRetries) {
      try {
        const httpUtil = HttpUtil.getInstance();
        const response = await httpUtil.chatWithGLM(messages);
        
        if (response && response.choices && response.choices.length > 0) {
          const content = response.choices[0].message.content;
          
          // 添加到历史
          this.addToHistory('assistant', content);
          
          // 尝试解析JSON回复
          try {
            // 去除可能的代码块标记
            const cleanContent = content.replace(/```json\n?/g, '').replace(/\n?```/g, '');
            
            // 尝试提取JSON对象
            const jsonMatch = cleanContent.match(/\{.*\}/s);
            if (jsonMatch) {
              const parsed = JSON.parse(jsonMatch[0]);
              return {
                reply: parsed.reply || content,
                need_confirm: parsed.need_confirm || false,
                affected_vehicles: parsed.affected_vehicles || [],
                action_summary: parsed.action_summary || '',
                action_type: parsed.action_type || '其他'
              };
            }
          } catch (jsonError) {
            console.warn('JSON解析失败，返回原始内容:', jsonError);
          }
          
          return {
            reply: content,
            need_confirm: false
          };
        }
        
        throw new Error('API响应格式错误');
        
      } catch (error) {
        retryCount++;
        
        if (retryCount > maxRetries) {
          console.error('GLM API调用失败:', error);
          return {
            reply: null,
            error: `API连接异常: ${error.message}`
          };
        }
        
        // 重试前等待
        await this.delay(1000 * retryCount);
      }
    }

    return {
      reply: null,
      error: 'GLM API调用失败'
    };
  }

  // 构建系统提示词
  private buildSystemPrompt(vehicles: any[]): string {
    const idleCount = vehicles.filter(v => v.status === 'idle').length;
    const busyCount = vehicles.filter(v => v.status === 'busy').length;
    const chargeCount = vehicles.filter(v => v.status === 'charge').length;
    const faultCount = vehicles.filter(v => v.status === 'fault').length;
    const lowBatteryCount = vehicles.filter(v => v.battery < 30).length;
    const avgBattery = vehicles.reduce((sum, v) => sum + v.battery, 0) / vehicles.length;

    return `你是"轻行Claw"无人车队AI智能体，核心职责是主动帮运营人员完成工作。

行为准则：先干活查数据分析，用对话告诉结果，最后才问确认，数据现成直接用，给完整方案。

当前车队状态：
- 总车辆：${vehicles.length}辆
- 待命：${idleCount}辆
- 配送中：${busyCount}辆  
- 充电中：${chargeCount}辆
- 故障：${faultCount}辆
- 低电量(<30%)：${lowBatteryCount}辆
- 平均电量：${avgBattery.toFixed(1)}%

运营数据：
- 今日配送：1847单
- 总里程：12,847km
- 完成率：98.2%
- 平均时效：23分钟
- 较昨日：+12.3%
- 平均能耗：7.2kWh/100km

关键规则：
1. 基于现有数据回答，不说"没有数据"
2. 调度时给出完整方案
3. 特别关注低电量车辆
4. 故障和配送中车辆不能调度
5. 需要操作时先说明方案，最后问确认

回复格式严格使用JSON，不要加markdown标记：
- 需要操作：{"reply": "回复内容", "need_confirm": true, "action_summary": "操作摘要", "affected_vehicles": ["DM-01", "DM-02"], "action_type": "调度|充电|开门|维修|其他"}
- 纯查询：{"reply": "回复内容", "need_confirm": false}

车辆数据：${JSON.stringify(vehicles, null, 2)}`;
  }

  // 本地处理函数
  private handleOperationData() {
    const html = HtmlBuilder.buildStatCardForOperation();
    return {
      reply: html,
      need_confirm: false
    };
  }

  private handleEnergyAnalysis() {
    const html = HtmlBuilder.buildStatCardForEnergy();
    return {
      reply: html,
      need_confirm: false
    };
  }

  private handleAllVehicles(vehicles: any[]) {
    const html = HtmlBuilder.buildVehicleStatusGroups(vehicles);
    return {
      reply: html,
      need_confirm: false
    };
  }

  private handleChargeScheduling(vehicles: any[]) {
    const lowBatteryVehicles = vehicles.filter(v => v.battery < 30 && v.status === 'idle');
    
    if (lowBatteryVehicles.length === 0) {
      return {
        reply: HtmlBuilder.buildNotice('目前没有需要充电的低电量车辆。', 'tip'),
        need_confirm: false
      };
    }

    const html = HtmlBuilder.buildInfoCard('低电量车辆', lowBatteryVehicles.map(v => ({
      label: v.id,
      value: `${v.battery}% - ${v.loc}`
    })), 'warn');

    return {
      reply: html,
      need_confirm: true,
      affected_vehicles: lowBatteryVehicles.map(v => v.id),
      action_summary: `安排${lowBatteryVehicles.length}辆低电量车辆充电`,
      action_type: '充电'
    };
  }

  private handleDispatch(vehicles: any[]) {
    const availableVehicles = vehicles.filter(v => v.status === 'idle');
    
    if (availableVehicles.length === 0) {
      return {
        reply: HtmlBuilder.buildNotice('目前没有可用的待命车辆。', 'warn'),
        need_confirm: false
      };
    }

    const html = HtmlBuilder.buildInfoCard('可用车辆', availableVehicles.map(v => ({
      label: v.id,
      value: `${v.battery}% - ${v.loc}`
    })), 'info');

    return {
      reply: html,
      need_confirm: true,
      affected_vehicles: availableVehicles.slice(0, 3).map(v => v.id), // 默认调度3辆
      action_summary: `调度${Math.min(3, availableVehicles.length)}辆待命车辆执行配送任务`,
      action_type: '调度'
    };
  }

  private handleTomorrowPlan() {
    const timeSlots = [
      { time: '06:00-08:00', plan: '早高峰准备，调配5辆车去A区' },
      { time: '08:00-12:00', plan: '上午配送高峰，重点保障B区、C区' },
      { time: '12:00-14:00', plan: '午间充电调度，安排低电量车辆充电' },
      { time: '14:00-18:00', plan: '下午配送，调配3辆车去D区' },
      { time: '18:00-22:00', plan: '晚班配送，重点保障E区' },
      { time: '22:00-06:00', plan: '夜间充电和维护' }
    ];

    const html = HtmlBuilder.buildPlanCard('明日调度计划', timeSlots);
    return {
      reply: html,
      need_confirm: false
    };
  }

  private handleOpenDoor(vehicles: any[]) {
    // 简化处理，默认打开第一辆待命车辆的门
    const availableVehicle = vehicles.find(v => v.status === 'idle');
    
    if (!availableVehicle) {
      return {
        reply: HtmlBuilder.buildNotice('没有找到可操作的待命车辆。', 'warn'),
        need_confirm: false
      };
    }

    const html = HtmlBuilder.buildInfoCard('车门控制', [
      { label: '车辆', value: availableVehicle.id },
      { label: '位置', value: availableVehicle.loc },
      { label: '电量', value: `${availableVehicle.battery}%` }
    ], 'info');

    return {
      reply: html,
      need_confirm: true,
      affected_vehicles: [availableVehicle.id],
      action_summary: `打开${availableVehicle.id}的车门`,
      action_type: '开门'
    };
  }

  private handleFaultRepair(vehicles: any[]) {
    const faultVehicles = vehicles.filter(v => v.status === 'fault');
    
    if (faultVehicles.length === 0) {
      return {
        reply: HtmlBuilder.buildNotice('目前没有故障车辆。', 'tip'),
        need_confirm: false
      };
    }

    const html = HtmlBuilder.buildInfoCard('故障车辆', faultVehicles.map(v => ({
      label: v.id,
      value: `${v.task} - ${v.loc}`
    })), 'danger');

    return {
      reply: html,
      need_confirm: true,
      affected_vehicles: faultVehicles.map(v => v.id),
      action_summary: `安排${faultVehicles.length}辆故障车辆维修`,
      action_type: '维修'
    };
  }

  private handleBatchCharge(vehicles: any[]) {
    const availableVehicles = vehicles.filter(v => v.status === 'idle');
    
    if (availableVehicles.length === 0) {
      return {
        reply: HtmlBuilder.buildNotice('没有可用的待命车辆进行批量充电。', 'warn'),
        need_confirm: false
      };
    }

    const html = HtmlBuilder.buildBatchPanel(availableVehicles.map(v => v.id));
    return {
      reply: html,
      need_confirm: false
    };
  }

  private handleBatchDispatch(vehicles: any[]) {
    const availableVehicles = vehicles.filter(v => v.status === 'idle');
    
    if (availableVehicles.length === 0) {
      return {
        reply: HtmlBuilder.buildNotice('没有可用的待命车辆进行批量调度。', 'warn'),
        need_confirm: false
      };
    }

    const html = HtmlBuilder.buildBatchPanel(availableVehicles.map(v => v.id));
    return {
      reply: html,
      need_confirm: false
    };
  }

  private handleHelp() {
    const html = `
      <div class="help-content">
        <div class="help-title">🤖 AI智能体功能说明</div>
        <div class="help-section">
          <strong>📊 数据查询：</strong>
          <ul>
            <li>"今日运营数据" - 查看今日配送统计</li>
            <li>"车队状态" - 查看所有车辆状态</li>
            <li>"能耗分析" - 查看能耗数据</li>
          </ul>
        </div>
        <div class="help-section">
          <strong>🚗 车辆操作：</strong>
          <ul>
            <li>"调度派车" - 安排车辆执行任务</li>
            <li>"充电调度" - 安排低电量车辆充电</li>
            <li>"开门" - 打开车辆车门</li>
            <li>"故障处理" - 处理故障车辆</li>
          </ul>
        </div>
        <div class="help-section">
          <strong>📅 计划管理：</strong>
          <ul>
            <li>"明日计划" - 制定明日调度计划</li>
            <li>"批量操作" - 批量处理多辆车</li>
          </ul>
        </div>
      </div>
    `;

    return {
      reply: html,
      need_confirm: false
    };
  }

  private handleDefault() {
    const html = HtmlBuilder.buildNotice('我可以帮您查询车队状态、调度车辆、分析数据等。请告诉我您需要什么帮助？', 'tip');
    return {
      reply: html,
      need_confirm: false
    };
  }

  // 工具函数
  private containsAny(text: string, keywords: string[]): boolean {
    return keywords.some(keyword => text.includes(keyword));
  }

  private addToHistory(role: string, content: string): void {
    this.conversationHistory.push({ role, content });
    
    // 限制历史长度
    if (this.conversationHistory.length > this.maxHistoryLength) {
      this.conversationHistory = this.conversationHistory.slice(-this.maxHistoryLength);
    }
  }

  private getRecentHistory(): Array<{role: string, content: string}> {
    return this.conversationHistory.slice(-6); // 返回最近6条消息
  }

  private delay(ms: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  // 清空对话历史
  clearHistory(): void {
    this.conversationHistory = [];
  }
}

export default AiEngine;