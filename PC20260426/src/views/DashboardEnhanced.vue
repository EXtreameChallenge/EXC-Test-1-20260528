<template>
  <div class="dashboard">
    <PageHeader title="数据大屏" subtitle="实时监控无人车队运营状态">
      <template #actions>
        <div class="flex items-center gap-3">
          <el-select v-model="timeRange" size="default" style="width: 120px">
            <el-option label="今日" value="today" />
            <el-option label="本周" value="week" />
            <el-option label="本月" value="month" />
          </el-select>
          <el-button size="default" @click="refreshData" :loading="loading">
            <el-icon class="mr-1"><Refresh /></el-icon>刷新
          </el-button>
        </div>
      </template>
    </PageHeader>

    <div class="stats-grid">
      <div v-for="stat in stats" :key="stat.title" class="stat-card" :class="stat.class">
        <div class="stat-content">
          <div class="stat-info">
            <div class="stat-label">{{ stat.title }}</div>
            <div class="stat-value">{{ stat.value }}</div>
            <div class="stat-trend" :class="stat.trendClass">
              <el-icon :size="12"><component :is="stat.trendIcon" /></el-icon>
              <span>{{ stat.trend }}</span>
            </div>
          </div>
          <div class="stat-icon-wrapper" :class="stat.iconClass">
            <el-icon :size="24"><component :is="stat.icon" /></el-icon>
          </div>
        </div>
        <div class="stat-glow" :class="stat.glowClass"></div>
      </div>
    </div>

    <div class="charts-row">
      <div class="chart-card chart-large">
        <div class="chart-header">
          <h3>24小时单量趋势</h3>
          <div class="chart-actions">
            <span class="chart-legend">
              <span class="legend-dot"></span>
              今日单量
            </span>
          </div>
        </div>
        <div ref="ordersChartRef" class="chart-container"></div>
      </div>
      <div class="chart-card chart-small">
        <div class="chart-header">
          <h3>车辆状态分布</h3>
        </div>
        <div ref="statusChartRef" class="chart-container-pie"></div>
      </div>
    </div>

    <div class="charts-row">
      <div class="chart-card">
        <div class="chart-header">
          <h3>区域配送分布</h3>
        </div>
        <div ref="regionChartRef" class="chart-container"></div>
      </div>
      <div class="chart-card">
        <div class="chart-header">
          <h3>能耗分析</h3>
        </div>
        <div ref="energyChartRef" class="chart-container"></div>
      </div>
    </div>

    <div class="bottom-grid">
      <div class="card ai-card">
        <div class="card-header">
          <div class="card-title">
            <div class="ai-icon">
              <el-icon :size="18"><Cpu /></el-icon>
            </div>
            <div>
              <div class="title-text">AI智能分析</div>
              <div class="subtitle-text">基于GLM-4分析</div>
            </div>
          </div>
        </div>
        <div class="ai-content">
          <div v-for="insight in aiInsights" :key="insight.id" class="ai-item">
            <div class="ai-item-icon" :class="insight.type">
              <el-icon><component :is="insight.icon" /></el-icon>
            </div>
            <div class="ai-item-content">
              <div class="ai-item-title">{{ insight.title }}</div>
              <div class="ai-item-desc">{{ insight.description }}</div>
            </div>
          </div>
        </div>
        <div class="ai-input-wrapper">
          <el-input v-model="aiQuery" placeholder="输入问题，AI将为您分析..." size="default">
            <template #append>
              <el-button @click="askAI">分析</el-button>
            </template>
          </el-input>
        </div>
      </div>

      <div class="card">
        <div class="card-header">
          <h3 class="card-title-text">任务动态</h3>
        </div>
        <div class="task-list">
          <div v-for="task in recentTasks" :key="task.id" class="task-item">
            <div class="task-icon" :class="getTaskClass(task.status)">
              <el-icon><component :is="getTaskIcon(task.status)" /></el-icon>
            </div>
            <div class="task-info">
              <div class="task-name">{{ task.name }}</div>
              <div class="task-meta">{{ task.destination }} · {{ task.executeTime }}</div>
            </div>
            <StatusBadge :status="task.status" type="task" />
          </div>
        </div>
        <router-link to="/admin/dispatch/tasks" class="view-more">
          查看全部任务
          <el-icon><ArrowRight /></el-icon>
        </router-link>
      </div>

      <div class="card alert-card">
        <div class="card-header">
          <h3 class="card-title-text">实时告警</h3>
        </div>
        <div class="alert-list">
          <div v-for="alert in alerts" :key="alert.id" class="alert-item" :class="getAlertClass(alert.level)">
            <el-icon><Warning /></el-icon>
            <span class="alert-message">{{ alert.message }}</span>
            <span class="alert-time">{{ alert.time }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="bottom-grid-2">
      <div class="card">
        <div class="card-header">
          <h3 class="card-title-text">车辆排行榜</h3>
        </div>
        <div class="rank-list">
          <div v-for="(vehicle, index) in topVehicles" :key="vehicle.id" class="rank-item">
            <div class="rank-badge" :class="getRankClass(index)">{{ index + 1 }}</div>
            <div class="rank-info">
              <div class="rank-name">{{ vehicle.id }}</div>
              <div class="rank-meta">{{ vehicle.orders }}单 · {{ vehicle.km }}km</div>
            </div>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="card-header">
          <h3 class="card-title-text">系统状态</h3>
        </div>
        <div class="status-list">
          <div class="status-item">
            <span class="status-label">服务器状态</span>
            <Badge variant="green">正常</Badge>
          </div>
          <div class="status-item">
            <span class="status-label">数据库连接</span>
            <Badge variant="green">正常</Badge>
          </div>
          <div class="status-item">
            <span class="status-label">AI服务</span>
            <Badge variant="blue">在线</Badge>
          </div>
          <div class="status-item">
            <span class="status-label">数据同步</span>
            <Badge variant="green">同步中</Badge>
          </div>
        </div>
      </div>

      <div class="card quick-actions-card">
        <div class="card-header">
          <h3 class="card-title-text">快捷操作</h3>
        </div>
        <div class="quick-actions">
          <el-button class="action-btn" @click="$router.push('/admin/dispatch/new')">
            <el-icon class="mr-1"><Plus /></el-icon>新建任务
          </el-button>
          <el-button class="action-btn" @click="$router.push('/admin/fleet/vehicles')">
            <el-icon class="mr-1"><Van /></el-icon>车辆管理
          </el-button>
          <el-button class="action-btn" @click="$router.push('/admin/ai/conversation')">
            <el-icon class="mr-1"><ChatDotRound /></el-icon>AI对话
          </el-button>
          <el-button class="action-btn" @click="$router.push('/admin/mobile/management')">
            <el-icon class="mr-1"><Iphone /></el-icon>移动端管理
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'
import * as echarts from 'echarts'
import {
  Van, Document, CircleCheck, Timer, Lightning, Clock, Check, Loading, CircleClose,
  Refresh, Cpu, Warning, Plus, ChatDotRound, Iphone, ArrowRight,
  ArrowUp, ArrowDown
} from '@element-plus/icons-vue'
import Card from '../components/ui/Card.vue'
import PageHeader from '../components/layout/PageHeader.vue'
import StatusBadge from '../components/features/StatusBadge.vue'
import Badge from '../components/ui/Badge.vue'
import { dashboardStats, hourlyOrders, regionDistribution, vehicleStatusDistribution, tasks, alerts as mockAlerts } from '../data/mock'

const loading = ref(false)
const timeRange = ref('today')
const aiQuery = ref('')

const stats = computed(() => [
  {
    title: '总车辆数',
    value: dashboardStats.totalVehicles,
    icon: Van,
    class: 'stat-primary',
    iconClass: 'icon-primary',
    glowClass: 'glow-primary',
    trend: '+2 较昨日',
    trendClass: 'trend-up',
    trendIcon: ArrowUp
  },
  {
    title: '今日单量',
    value: dashboardStats.todayOrders.toLocaleString(),
    icon: Document,
    class: 'stat-secondary',
    iconClass: 'icon-secondary',
    glowClass: 'glow-secondary',
    trend: '+12.3% 较昨日',
    trendClass: 'trend-up',
    trendIcon: ArrowUp
  },
  {
    title: '完成率',
    value: dashboardStats.completionRate + '%',
    icon: CircleCheck,
    class: 'stat-success',
    iconClass: 'icon-success',
    glowClass: 'glow-success',
    trend: '+0.5%',
    trendClass: 'trend-up',
    trendIcon: ArrowUp
  },
  {
    title: '平均时效',
    value: dashboardStats.avgTime + 'min',
    icon: Timer,
    class: 'stat-warning',
    iconClass: 'icon-warning',
    glowClass: 'glow-warning',
    trend: '-2min',
    trendClass: 'trend-up',
    trendIcon: ArrowDown
  },
  {
    title: '耗电量',
    value: dashboardStats.powerConsumption + 'kWh',
    icon: Lightning,
    class: 'stat-danger',
    iconClass: 'icon-danger',
    glowClass: 'glow-danger',
    trend: '-3.2%',
    trendClass: 'trend-up',
    trendIcon: ArrowDown
  }
])

const recentTasks = computed(() => tasks.slice(0, 5))
const alerts = computed(() => mockAlerts.slice(0, 4))

const topVehicles = ref([
  { id: 'DM-03', orders: 156, km: 892 },
  { id: 'DM-05', orders: 142, km: 756 },
  { id: 'DM-09', orders: 138, km: 823 },
  { id: 'DM-01', orders: 125, km: 645 },
])

const aiInsights = ref([
  { id: 1, type: 'success', icon: CircleCheck, title: '配送效率提升', description: '本周平均配送时效提升8%，建议保持当前调度策略' },
  { id: 2, type: 'warning', icon: Warning, title: '电量预警', description: 'DM-06电量仅剩12%，建议尽快安排充电' },
  { id: 3, type: 'info', icon: Timer, title: '高峰预测', description: '预计14:00-16:00为配送高峰，建议提前调度3辆备用车辆' },
])

const statusChartRef = ref<HTMLElement>()
const ordersChartRef = ref<HTMLElement>()
const regionChartRef = ref<HTMLElement>()
const energyChartRef = ref<HTMLElement>()

let charts: echarts.ECharts[] = []

function initCharts() {
  if (ordersChartRef.value) {
    const chart = echarts.init(ordersChartRef.value)
    chart.setOption({
      tooltip: { trigger: 'axis', backgroundColor: 'rgba(18, 18, 28, 0.95)', borderColor: 'rgba(255,255,255,0.1)', textStyle: { color: '#f8fafc' } },
      grid: { left: '3%', right: '4%', bottom: '3%', top: '15%', containLabel: true },
      xAxis: {
        type: 'category',
        data: hourlyOrders.map(d => d.hour),
        axisLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } },
        axisLabel: { color: '#94a3b8' }
      },
      yAxis: {
        type: 'value',
        axisLine: { show: false },
        splitLine: { lineStyle: { color: 'rgba(255,255,255,0.05)' } },
        axisLabel: { color: '#94a3b8' }
      },
      series: [{
        data: hourlyOrders.map(d => d.count),
        type: 'line',
        smooth: true,
        symbol: 'circle',
        symbolSize: 8,
        lineStyle: { color: '#4a9eff', width: 3 },
        itemStyle: { color: '#4a9eff', borderWidth: 2, borderColor: '#0a0a1a' },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(74, 158, 255, 0.35)' },
            { offset: 1, color: 'rgba(74, 158, 255, 0)' }
          ])
        }
      }]
    })
    charts.push(chart)
  }

  if (statusChartRef.value) {
    const chart = echarts.init(statusChartRef.value)
    chart.setOption({
      tooltip: { trigger: 'item', backgroundColor: 'rgba(18, 18, 28, 0.95)', borderColor: 'rgba(255,255,255,0.1)', textStyle: { color: '#f8fafc' } },
      legend: { bottom: 0, textStyle: { color: '#94a3b8' }, itemGap: 20 },
      series: [{
        type: 'pie',
        radius: ['45%', '75%'],
        center: ['50%', '45%'],
        avoidLabelOverlap: false,
        itemStyle: { borderRadius: 8, borderColor: '#12121c', borderWidth: 3 },
        label: { show: false },
        emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold', color: '#f0f0f8' } },
        data: vehicleStatusDistribution.map(d => ({ name: d.status, value: d.count, itemStyle: { color: d.color } }))
      }]
    })
    charts.push(chart)
  }

  if (regionChartRef.value) {
    const chart = echarts.init(regionChartRef.value)
    chart.setOption({
      tooltip: { trigger: 'axis', backgroundColor: 'rgba(18, 18, 28, 0.95)', borderColor: 'rgba(255,255,255,0.1)', textStyle: { color: '#f8fafc' }, axisPointer: { type: 'shadow' } },
      grid: { left: '3%', right: '4%', bottom: '3%', top: '10%', containLabel: true },
      xAxis: {
        type: 'category',
        data: regionDistribution.map(d => d.region),
        axisLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } },
        axisLabel: { color: '#94a3b8' }
      },
      yAxis: {
        type: 'value',
        axisLine: { show: false },
        splitLine: { lineStyle: { color: 'rgba(255,255,255,0.05)' } },
        axisLabel: { color: '#94a3b8' }
      },
      series: [{
        data: regionDistribution.map(d => ({
          value: d.count,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#c084fc' },
              { offset: 1, color: '#6366f1' }
            ])
          }
        })),
        type: 'bar',
        barWidth: '50%',
        itemStyle: { borderRadius: [6, 6, 0, 0] }
      }]
    })
    charts.push(chart)
  }

  if (energyChartRef.value) {
    const chart = echarts.init(energyChartRef.value)
    chart.setOption({
      tooltip: { trigger: 'axis', backgroundColor: 'rgba(18, 18, 28, 0.95)', borderColor: 'rgba(255,255,255,0.1)', textStyle: { color: '#f8fafc' } },
      legend: { data: ['能耗', '里程'], textStyle: { color: '#94a3b8' }, top: 0 },
      grid: { left: '3%', right: '4%', bottom: '3%', top: '20%', containLabel: true },
      xAxis: {
        type: 'category',
        data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
        axisLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } },
        axisLabel: { color: '#94a3b8' }
      },
      yAxis: [
        { type: 'value', name: 'kWh', axisLine: { show: false }, splitLine: { lineStyle: { color: 'rgba(255,255,255,0.05)' } }, axisLabel: { color: '#94a3b8' } },
        { type: 'value', name: 'km', axisLine: { show: false }, splitLine: { show: false }, axisLabel: { color: '#94a3b8' } }
      ],
      series: [
        { name: '能耗', type: 'bar', data: [120, 132, 101, 134, 90, 80, 110], itemStyle: { color: '#22d3ee', borderRadius: [4, 4, 0, 0] } },
        { name: '里程', type: 'line', yAxisIndex: 1, data: [820, 932, 901, 934, 790, 830, 920], lineStyle: { color: '#fbbf24' }, itemStyle: { color: '#fbbf24' }, symbol: 'circle', symbolSize: 6 }
      ]
    })
    charts.push(chart)
  }
}

function handleResize() {
  charts.forEach(chart => chart?.resize())
}

function getTaskIcon(status: string) {
  const icons: Record<string, any> = { pending: Clock, confirmed: Check, executing: Loading, completed: CircleCheck, cancelled: CircleClose }
  return icons[status] || Document
}

function getTaskClass(status: string) {
  const classes: Record<string, string> = { pending: 'bg-status-orange/20 text-status-orange', confirmed: 'bg-accent-blue/20 text-accent-blue', executing: 'bg-accent-purple/20 text-accent-purple', completed: 'bg-status-green/20 text-status-green', cancelled: 'bg-white/10 text-text-secondary' }
  return classes[status] || 'bg-white/10 text-text-secondary'
}

function getAlertClass(level: string) {
  const classes: Record<string, string> = { critical: 'alert-critical', warning: 'alert-warning', info: 'alert-info' }
  return classes[level] || 'alert-info'
}

function getRankClass(index: number) {
  const classes = ['rank-gold', 'rank-silver', 'rank-bronze']
  return classes[index] || 'rank-default'
}

function refreshData() {
  loading.value = true
  setTimeout(() => { loading.value = false }, 1000)
}

function askAI() {
  if (aiQuery.value.trim()) {
    console.log('AI查询:', aiQuery.value)
    aiQuery.value = ''
  }
}

onMounted(() => {
  initCharts()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  charts.forEach(chart => chart?.dispose())
})
</script>

<style scoped>
.dashboard {
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  background: linear-gradient(135deg, rgba(26, 26, 40, 0.9) 0%, rgba(18, 18, 28, 0.9) 100%);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 16px;
  padding: 20px;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
  border-color: rgba(255, 255, 255, 0.1);
}

.stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  border-radius: 16px 16px 0 0;
}

.stat-primary::before { background: linear-gradient(90deg, #4a9eff, #6366f1); }
.stat-secondary::before { background: linear-gradient(90deg, #c084fc, #a855f7); }
.stat-success::before { background: linear-gradient(90deg, #22c55e, #16a34a); }
.stat-warning::before { background: linear-gradient(90deg, #f59e0b, #d97706); }
.stat-danger::before { background: linear-gradient(90deg, #ef4444, #dc2626); }

.stat-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  position: relative;
  z-index: 1;
}

.stat-label {
  font-size: 13px;
  color: #94a3b8;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #f8fafc;
  line-height: 1.2;
  margin-bottom: 8px;
}

.stat-trend {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
}

.trend-up { color: #22c55e; }
.trend-down { color: #ef4444; }

.stat-icon-wrapper {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon-primary { background: rgba(74, 158, 255, 0.15); color: #4a9eff; }
.icon-secondary { background: rgba(192, 132, 252, 0.15); color: #c084fc; }
.icon-success { background: rgba(34, 197, 94, 0.15); color: #22c55e; }
.icon-warning { background: rgba(245, 158, 11, 0.15); color: #f59e0b; }
.icon-danger { background: rgba(239, 68, 68, 0.15); color: #ef4444; }

.stat-glow {
  position: absolute;
  bottom: -20px;
  right: -20px;
  width: 80px;
  height: 80px;
  border-radius: 50%;
  filter: blur(30px);
  opacity: 0.3;
}

.glow-primary { background: #4a9eff; }
.glow-secondary { background: #c084fc; }
.glow-success { background: #22c55e; }
.glow-warning { background: #f59e0b; }
.glow-danger { background: #ef4444; }

.charts-row {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 20px;
  margin-bottom: 24px;
}

.chart-card {
  background: linear-gradient(135deg, rgba(26, 26, 40, 0.9) 0%, rgba(18, 18, 28, 0.9) 100%);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 16px;
  padding: 20px;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.chart-header h3 {
  font-size: 15px;
  font-weight: 600;
  color: #f8fafc;
}

.chart-legend {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
  color: #94a3b8;
}

.legend-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #4a9eff;
}

.chart-container {
  height: 260px;
}

.chart-container-pie {
  height: 260px;
}

.bottom-grid {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 20px;
  margin-bottom: 24px;
}

.card {
  background: linear-gradient(135deg, rgba(26, 26, 40, 0.9) 0%, rgba(18, 18, 28, 0.9) 100%);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 16px;
  padding: 20px;
}

.card-header {
  margin-bottom: 16px;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.card-title-text {
  font-size: 15px;
  font-weight: 600;
  color: #f8fafc;
}

.ai-icon {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #4a9eff, #6366f1);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.title-text {
  font-size: 14px;
  font-weight: 600;
  color: #f8fafc;
}

.subtitle-text {
  font-size: 12px;
  color: #64748b;
}

.ai-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 16px;
}

.ai-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.05);
  border-radius: 12px;
}

.ai-item-icon {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.ai-item-icon.success { background: rgba(34, 197, 94, 0.15); color: #22c55e; }
.ai-item-icon.warning { background: rgba(245, 158, 11, 0.15); color: #f59e0b; }
.ai-item-icon.info { background: rgba(59, 130, 246, 0.15); color: #3b82f6; }

.ai-item-content {
  flex: 1;
  min-width: 0;
}

.ai-item-title {
  font-size: 13px;
  font-weight: 500;
  color: #f8fafc;
  margin-bottom: 2px;
}

.ai-item-desc {
  font-size: 12px;
  color: #94a3b8;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.ai-input-wrapper {
  margin-top: 12px;
}

.task-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.task-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 12px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 10px;
  transition: background 0.2s;
}

.task-item:hover {
  background: rgba(255, 255, 255, 0.06);
}

.task-icon {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.task-info {
  flex: 1;
  min-width: 0;
}

.task-name {
  font-size: 13px;
  font-weight: 500;
  color: #f8fafc;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.task-meta {
  font-size: 12px;
  color: #94a3b8;
}

.view-more {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.06);
  color: #4a9eff;
  font-size: 13px;
  text-decoration: none;
  transition: color 0.2s;
}

.view-more:hover {
  color: #6bb5ff;
}

.alert-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.alert-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 12px;
  border-radius: 10px;
  font-size: 13px;
}

.alert-critical {
  background: rgba(239, 68, 68, 0.1);
  color: #ef4444;
}

.alert-warning {
  background: rgba(245, 158, 11, 0.1);
  color: #f59e0b;
}

.alert-info {
  background: rgba(59, 130, 246, 0.1);
  color: #3b82f6;
}

.alert-message {
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.alert-time {
  font-size: 11px;
  opacity: 0.7;
  flex-shrink: 0;
}

.bottom-grid-2 {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.rank-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.rank-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 10px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 10px;
}

.rank-badge {
  width: 26px;
  height: 26px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 700;
  color: white;
}

.rank-gold { background: linear-gradient(135deg, #fbbf24, #f59e0b); }
.rank-silver { background: linear-gradient(135deg, #94a3b8, #64748b); }
.rank-bronze { background: linear-gradient(135deg, #d97706, #b45309); }
.rank-default { background: rgba(255, 255, 255, 0.1); color: #94a3b8; }

.rank-info {
  flex: 1;
}

.rank-name {
  font-size: 13px;
  font-weight: 500;
  color: #f8fafc;
}

.rank-meta {
  font-size: 11px;
  color: #94a3b8;
}

.status-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.status-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.status-label {
  font-size: 13px;
  color: #94a3b8;
}

.quick-actions {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.action-btn {
  width: 100%;
  justify-content: flex-start;
}

@media (max-width: 1400px) {
  .stats-grid {
    grid-template-columns: repeat(3, 1fr);
  }

  .bottom-grid,
  .bottom-grid-2 {
    grid-template-columns: 1fr 1fr;
  }
}

@media (max-width: 1024px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .charts-row {
    grid-template-columns: 1fr;
  }

  .bottom-grid,
  .bottom-grid-2 {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
}
</style>