/**
 * HTML模板构建器
 * 用于生成AI回复的富文本内容
 */

import { Colors, VehicleStatus, VehicleTypes } from './Constants';

export class HtmlBuilder {
  
  // 构建信息卡片
  static buildInfoCard(title: string, items: Array<{label: string, value: any}>, type: string = 'info'): string {
    const iconMap = {
      'info': '📋',
      'success': '✅', 
      'danger': '⚠️',
      'highlight': '💡',
      'purple': '🔮'
    };
    
    const icon = iconMap[type] || '📋';
    
    let itemsHtml = '';
    items.forEach(item => {
      itemsHtml += `
        <div class="info-row">
          <span class="info-label">${item.label}</span>
          <span class="info-value">${item.value}</span>
        </div>
      `;
    });
    
    return `
      <div class="info-card info-card-${type}">
        <div class="info-card-title">${icon} ${title}</div>
        <div class="info-card-body">${itemsHtml}</div>
      </div>
    `;
  }

  // 构建统计卡片
  static buildStatCard(title: string, stats: Array<{label: string, value: any}>, type: string = 'info'): string {
    const colors = [Colors.PRIMARY, Colors.SUCCESS, Colors.WARNING, Colors.ERROR, Colors.SPECIAL, Colors.AUXILIARY];
    
    let statsHtml = '';
    stats.forEach((stat, index) => {
      const color = colors[index % colors.length];
      statsHtml += `
        <div class="stat-item">
          <div class="stat-num" style="color: ${color}">${stat.value}</div>
          <div class="stat-lbl">${stat.label}</div>
        </div>
      `;
    });
    
    return `
      <div class="info-card info-card-${type}">
        <div class="info-card-title">📊 ${title}</div>
        <div class="stat-grid">${statsHtml}</div>
      </div>
    `;
  }

  // 构建标签卡片
  static buildTagCard(title: string, tags: string[], type: string = 'info'): string {
    const colorMap = {
      'info': 'tag-blue',
      'success': 'tag-green',
      'danger': 'tag-red', 
      'highlight': 'tag-orange'
    };
    
    const tagClass = colorMap[type] || 'tag-blue';
    
    let tagsHtml = '';
    tags.forEach(tag => {
      tagsHtml += `<div class="tag-item ${tagClass}">${tag}</div>`;
    });
    
    return `
      <div class="info-card info-card-${type}">
        <div class="info-card-title">🏷️ ${title}</div>
        <div class="tag-list">${tagsHtml}</div>
      </div>
    `;
  }

  // 构建通知卡片
  static buildNotice(text: string, type: string = 'tip'): string {
    const iconMap = {
      'tip': '💡',
      'warn': '⚠️',
      'error': '❌'
    };
    
    const icon = iconMap[type] || '💡';
    
    return `
      <div class="notice-box notice-${type}">
        <div class="notice-icon">${icon}</div>
        <div class="notice-text">${text}</div>
      </div>
    `;
  }

  // 构建进度条
  static buildProgress(label: string, value: number, max: number = 100, color?: string): string {
    const percentage = Math.round((value / max) * 100);
    const progressColor = color || Colors.getBatteryColor(percentage);
    
    return `
      <div class="info-row">
        <span class="info-label">${label}</span>
        <span class="info-value">${percentage}%</span>
        <div class="progress-bar">
          <div class="progress-fill" style="width: ${percentage}%; background-color: ${progressColor}"></div>
        </div>
      </div>
    `;
  }

  // 构建计划卡片
  static buildPlanCard(title: string, timeSlots: Array<{time: string, plan: string}>): string {
    let slotsHtml = '';
    timeSlots.forEach(slot => {
      slotsHtml += `
        <div class="plan-slot">
          <div class="plan-time">${slot.time}</div>
          <div class="plan-desc">${slot.plan}</div>
        </div>
      `;
    });
    
    return `
      <div class="plan-card">
        <div class="plan-title">📅 ${title}</div>
        <div class="plan-slots">${slotsHtml}</div>
      </div>
    `;
  }

  // 构建车辆迷你卡片
  static buildVehicleMiniCard(vehicle: any): string {
    const statusInfo = VehicleStatus.getStatusInfo(vehicle.status);
    const batteryColor = Colors.getBatteryColor(vehicle.battery);
    
    return `
      <div class="v-card-mini">
        <div class="v-ind" style="background-color: ${statusInfo.color}"></div>
        <div class="v-card-mini-name">${vehicle.id}</div>
        <div class="v-card-mini-detail">${vehicle.type} · ${statusInfo.chinese} · ${vehicle.loc}</div>
        <div class="v-card-mini-batt" style="color: ${batteryColor}">${vehicle.battery}%</div>
      </div>
    `;
  }

  // 构建确认卡片
  static buildConfirmCard(cardUid: string, vehicleIds: string[], actionSummary: string, actionType: string, vehicles: any[]): string {
    let vehiclesHtml = '';
    vehicles.forEach(vehicle => {
      if (vehicleIds.includes(vehicle.id)) {
        vehiclesHtml += this.buildVehicleMiniCard(vehicle);
      }
    });
    
    return `
      <div class="confirm-card" data-card-uid="${cardUid}">
        <div class="confirm-title">⚡ 待确认操作</div>
        <div class="confirm-summary">${actionSummary}</div>
        <div class="confirm-vehicles">${vehiclesHtml}</div>
        <div class="confirm-actions">
          <button class="confirm-btn confirm-ok" data-action="confirm-ok" data-card-uid="${cardUid}" data-vehicle-ids="${JSON.stringify(vehicleIds)}" data-action-type="${actionType}">确认执行</button>
          <button class="confirm-btn confirm-cancel" data-action="confirm-cancel" data-card-uid="${cardUid}">取消</button>
        </div>
      </div>
    `;
  }

  // 构建批量操作面板
  static buildBatchPanel(vehicleIds: string[]): string {
    return `
      <div class="batch-panel">
        <div class="batch-title">批量操作</div>
        <div class="batch-vehicles">
          ${vehicleIds.slice(0, 10).map(id => `<span class="batch-vehicle-tag">${id}</span>`).join('')}
          ${vehicleIds.length > 10 ? `<span class="batch-vehicle-tag">+${vehicleIds.length - 10}</span>` : ''}
        </div>
        <div class="batch-actions">
          <button class="batch-btn batch-charge" data-action="batch-charge" data-vehicle-ids="${JSON.stringify(vehicleIds)}">🔋 充电</button>
          <button class="batch-btn batch-dispatch" data-action="batch-dispatch" data-vehicle-ids="${JSON.stringify(vehicleIds)}">🚗 调度</button>
          <button class="batch-btn batch-repair" data-action="batch-repair" data-vehicle-ids="${JSON.stringify(vehicleIds)}">⚠️ 维修</button>
        </div>
      </div>
    `;
  }

  // 构建运营数据卡片
  static buildStatCardForOperation(): string {
    return this.buildStatCard('今日运营数据', [
      { label: '今日配送', value: '1847单' },
      { label: '完成率', value: '98.2%' },
      { label: '平均时效', value: '23分' },
      { label: '故障率', value: '0.3%' }
    ], 'info');
  }

  // 构建能耗概览卡片
  static buildStatCardForEnergy(): string {
    return this.buildStatCard('能耗概览', [
      { label: '总能耗', value: '2,847 kWh' },
      { label: '平均能耗', value: '7.2 kWh/100km' },
      { label: '节能率', value: '+12.3%' },
      { label: '碳排放', value: '1.2吨' }
    ], 'success');
  }

  // 构建车辆状态分组卡片
  static buildVehicleStatusGroups(vehicles: any[]): string {
    const groups = {
      'idle': vehicles.filter(v => v.status === 'idle'),
      'busy': vehicles.filter(v => v.status === 'busy'),
      'charge': vehicles.filter(v => v.status === 'charge'),
      'fault': vehicles.filter(v => v.status === 'fault')
    };
    
    let html = '<div class="vehicle-groups">';
    
    Object.entries(groups).forEach(([status, vehicles]) => {
      if (vehicles.length > 0) {
        const statusInfo = VehicleStatus.getStatusInfo(status);
        html += `
          <div class="status-group">
            <div class="group-title" style="color: ${statusInfo.color}">${statusInfo.chinese} (${vehicles.length}辆)</div>
            <div class="group-vehicles">
              ${vehicles.map(v => `<span class="vehicle-tag">${v.id}</span>`).join('')}
            </div>
          </div>
        `;
      }
    });
    
    html += '</div>';
    return html;
  }

  // 格式化HTML文本（去除危险标签，转换markdown样式）
  static formatHtmlText(text: string): string {
    // 移除危险标签
    let safeText = text
      .replace(/<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi, '')
      .replace(/<style\b[^<]*(?:(?!<\/style>)<[^<]*)*<\/style>/gi, '')
      .replace(/<template\b[^<]*(?:(?!<\/template>)<[^<]*)*<\/template>/gi, '')
      .replace(/on\w+\s*=\s*["'][^"']*["']/gi, '');

    // 转换markdown样式
    safeText = safeText
      .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')  // 加粗
      .replace(/\*(.*?)\*/g, '<em>$1</em>')              // 斜体
      .replace(/###\s*(.*?)(\n|$)/g, '<div class="h3">$1</div>') // 三级标题
      .replace(/\n/g, '<br>');                           // 换行

    return safeText;
  }
}

export default HtmlBuilder;