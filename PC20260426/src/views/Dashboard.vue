<template>
  <div>
    <PageHeader title="数据大屏" subtitle="实时监控无人车队运营状态" />

    <!-- Stats Cards -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-3 md:gap-4 mb-6">
      <Card v-for="stat in stats" :key="stat.title" class="flex items-center gap-3 md:gap-4">
        <div
          class="w-10 h-10 md:w-12 md:h-12 rounded-xl flex items-center justify-center flex-shrink-0"
          :class="stat.bgClass"
        >
          <el-icon :size="20" class="md:!size-6" :class="stat.iconClass">
            <component :is="stat.icon" />
          </el-icon>
        </div>
        <div>
          <div class="text-xl md:text-2xl font-bold text-text-primary">{{ stat.value }}</div>
          <div class="text-xs md:text-sm text-text-secondary">{{ stat.title }}</div>
        </div>
      </Card>
    </div>

    <!-- Charts Row -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-3 md:gap-4 mb-6">
      <!-- Vehicle Status Chart -->
      <Card>
        <h3 class="text-sm md:text-base font-semibold text-text-primary mb-3 md:mb-4">车辆状态分布</h3>
        <div ref="statusChartRef" class="h-56 md:h-64" />
      </Card>

      <!-- Hourly Orders Chart -->
      <Card class="lg:col-span-2">
        <h3 class="text-sm md:text-base font-semibold text-text-primary mb-3 md:mb-4">24小时单量趋势</h3>
        <div ref="ordersChartRef" class="h-56 md:h-64" />
      </Card>
    </div>

    <!-- Bottom Row -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-3 md:gap-4">
      <!-- Region Distribution -->
      <Card>
        <h3 class="text-sm md:text-base font-semibold text-text-primary mb-3 md:mb-4">区域配送分布</h3>
        <div ref="regionChartRef" class="h-56 md:h-64" />
      </Card>

      <!-- Task List -->
      <Card class="lg:col-span-2">
        <div class="flex items-center justify-between mb-3 md:mb-4">
          <h3 class="text-sm md:text-base font-semibold text-text-primary">任务动态</h3>
          <router-link to="/dispatch/tasks" class="text-xs md:text-sm text-accent-blue hover:underline">
            查看全部
          </router-link>
        </div>
        <div class="space-y-2 md:space-y-3">
          <div
            v-for="task in recentTasks"
            :key="task.id"
            class="flex items-center justify-between p-2 md:p-3 rounded-xl bg-white/5 hover:bg-white/[0.08] transition-colors"
          >
            <div class="flex items-center gap-2 md:gap-3 min-w-0 flex-1">
              <div
                class="w-8 h-8 md:w-10 md:h-10 rounded-lg flex items-center justify-center flex-shrink-0"
                :class="taskIconBg(task.status)"
              >
                <el-icon :size="16" class="md:!size-[18px]" :class="taskIconColor(task.status)">
                  <component :is="taskIcon(task.status)" />
                </el-icon>
              </div>
              <div class="min-w-0 flex-1">
                <div class="text-xs md:text-sm font-medium text-text-primary truncate">{{ task.name }}</div>
                <div class="text-[10px] md:text-xs text-text-secondary truncate">{{ task.destination }} · {{ task.executeTime }}</div>
              </div>
            </div>
            <StatusBadge :status="task.status" type="task" class="flex-shrink-0 ml-2" />
          </div>
        </div>
      </Card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'
import * as echarts from 'echarts'
import { Van, Document, CircleCheck, Timer, Lightning, Clock, Check, Loading, CircleClose } from '@element-plus/icons-vue'
import Card from '../components/ui/Card.vue'
import PageHeader from '../components/layout/PageHeader.vue'
import StatusBadge from '../components/features/StatusBadge.vue'
import { dashboardStats, hourlyOrders, regionDistribution, vehicleStatusDistribution, tasks } from '../data/mock'
import { useUiStore } from '../stores/ui'

const uiStore = useUiStore()

onMounted(() => {
  uiStore.setPageTitle('数据大屏')
})

const stats = [
  {
    title: '总车辆数',
    value: dashboardStats.totalVehicles,
    icon: Van,
    bgClass: 'bg-accent-blue/20',
    iconClass: 'text-accent-blue'
  },
  {
    title: '今日单量',
    value: dashboardStats.todayOrders,
    icon: Document,
    bgClass: 'bg-accent-purple/20',
    iconClass: 'text-accent-purple'
  },
  {
    title: '完成率',
    value: dashboardStats.completionRate + '%',
    icon: CircleCheck,
    bgClass: 'bg-status-green/20',
    iconClass: 'text-status-green'
  },
  {
    title: '平均时效',
    value: dashboardStats.avgTime + 'min',
    icon: Timer,
    bgClass: 'bg-status-orange/20',
    iconClass: 'text-status-orange'
  },
  {
    title: '耗电量',
    value: dashboardStats.powerConsumption + 'kWh',
    icon: Lightning,
    bgClass: 'bg-status-red/20',
    iconClass: 'text-status-red'
  }
]

const recentTasks = computed(() => tasks.slice(0, 5))

const taskIcon = (status: string) => {
  const icons: Record<string, any> = {
    pending: Clock,
    confirmed: Check,
    executing: Loading,
    completed: CircleCheck,
    cancelled: CircleClose
  }
  return icons[status] || Document
}

const taskIconBg = (status: string) => {
  const bgs: Record<string, string> = {
    pending: 'bg-status-orange/20',
    confirmed: 'bg-accent-blue/20',
    executing: 'bg-accent-purple/20',
    completed: 'bg-status-green/20',
    cancelled: 'bg-white/10'
  }
  return bgs[status] || 'bg-white/10'
}

const taskIconColor = (status: string) => {
  const colors: Record<string, string> = {
    pending: 'text-status-orange',
    confirmed: 'text-accent-blue',
    executing: 'text-accent-purple',
    completed: 'text-status-green',
    cancelled: 'text-text-secondary'
  }
  return colors[status] || 'text-text-secondary'
}

// ECharts
const statusChartRef = ref<HTMLElement>()
const ordersChartRef = ref<HTMLElement>()
const regionChartRef = ref<HTMLElement>()

let statusChart: echarts.ECharts | null = null
let ordersChart: echarts.ECharts | null = null
let regionChart: echarts.ECharts | null = null

const initCharts = () => {
  if (statusChartRef.value) {
    statusChart = echarts.init(statusChartRef.value)
    statusChart.setOption({
      tooltip: { trigger: 'item' },
      legend: { bottom: 0, textStyle: { color: '#a0a0b8' } },
      series: [{
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['50%', '45%'],
        avoidLabelOverlap: false,
        itemStyle: { borderRadius: 8, borderColor: '#0a0a1a', borderWidth: 2 },
        label: { show: false },
        emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold', color: '#f0f0f8' } },
        data: vehicleStatusDistribution.map(d => ({ name: d.status, value: d.count, itemStyle: { color: d.color } }))
      }]
    })
  }

  if (ordersChartRef.value) {
    ordersChart = echarts.init(ordersChartRef.value)
    ordersChart.setOption({
      tooltip: { trigger: 'axis' },
      grid: { left: '3%', right: '4%', bottom: '3%', top: '10%', containLabel: true },
      xAxis: {
        type: 'category',
        data: hourlyOrders.map(d => d.hour),
        axisLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } },
        axisLabel: { color: '#a0a0b8' }
      },
      yAxis: {
        type: 'value',
        axisLine: { show: false },
        splitLine: { lineStyle: { color: 'rgba(255,255,255,0.05)' } },
        axisLabel: { color: '#a0a0b8' }
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
            { offset: 0, color: 'rgba(74, 158, 255, 0.3)' },
            { offset: 1, color: 'rgba(74, 158, 255, 0)' }
          ])
        }
      }]
    })
  }

  if (regionChartRef.value) {
    regionChart = echarts.init(regionChartRef.value)
    regionChart.setOption({
      tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
      grid: { left: '3%', right: '4%', bottom: '3%', top: '5%', containLabel: true },
      xAxis: {
        type: 'category',
        data: regionDistribution.map(d => d.region),
        axisLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } },
        axisLabel: { color: '#a0a0b8' }
      },
      yAxis: {
        type: 'value',
        axisLine: { show: false },
        splitLine: { lineStyle: { color: 'rgba(255,255,255,0.05)' } },
        axisLabel: { color: '#a0a0b8' }
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
        barWidth: '60%',
        itemStyle: { borderRadius: [4, 4, 0, 0] }
      }]
    })
  }
}

const handleResize = () => {
  statusChart?.resize()
  ordersChart?.resize()
  regionChart?.resize()
}

onMounted(() => {
  initCharts()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  statusChart?.dispose()
  ordersChart?.dispose()
  regionChart?.dispose()
})
</script>
