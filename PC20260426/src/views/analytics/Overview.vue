<template>
  <div class="analytics-overview">
    <div class="page-header">
      <h2>运营数据分析</h2>
    </div>
    <div class="stats-row">
      <div v-for="stat in stats" :key="stat.label" class="stat-card glass-card">
        <div class="stat-icon">{{ stat.icon }}</div>
        <div class="stat-info">
          <div class="stat-value" :style="{ color: stat.color }">{{ stat.value }}</div>
          <div class="stat-label">{{ stat.label }}</div>
        </div>
      </div>
    </div>
    <div class="charts-row">
      <div class="chart-card glass-card">
        <h3>每小时订单量</h3>
        <div class="chart-placeholder">
          <div v-for="item in hourlyData" :key="item.hour" class="bar-item">
            <div class="bar" :style="{ height: (item.count / maxCount * 100) + '%', background: 'var(--blue)' }"></div>
            <span class="bar-label">{{ item.hour.slice(0,2) }}</span>
          </div>
        </div>
      </div>
      <div class="chart-card glass-card">
        <h3>区域分布</h3>
        <div class="region-list">
          <div v-for="item in regionData" :key="item.region" class="region-item">
            <span class="region-name">{{ item.region }}</span>
            <div class="region-bar"><div class="region-fill" :style="{ width: (item.count / maxRegion * 100) + '%' }"></div></div>
            <span class="region-count">{{ item.count }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import * as api from '../../api/index.js'

const dashboard = ref({
  totalVehicles: 0,
  todayOrders: 0,
  completionRate: 0,
  avgDeliveryTime: 0,
  totalEnergyConsumption: 0,
  hourlyOrders: [],
  regionDistribution: []
})

const stats = computed(() => [
  { icon: '🚗', value: dashboard.value.totalVehicles, label: '总车辆', color: 'var(--blue)' },
  { icon: '📦', value: dashboard.value.todayOrders, label: '今日订单', color: 'var(--green)' },
  { icon: '✅', value: dashboard.value.completionRate + '%', label: '完成率', color: 'var(--purple)' },
  { icon: '⏱️', value: dashboard.value.avgDeliveryTime + '分', label: '平均时效', color: 'var(--orange)' },
  { icon: '⚡', value: dashboard.value.totalEnergyConsumption + 'kWh', label: '总能耗', color: 'var(--cyan)' }
])

const hourlyData = computed(() => dashboard.value.hourlyOrders)
const maxCount = computed(() => Math.max(...(dashboard.value.hourlyOrders.map(h => h.count) || [1])))
const regionData = computed(() => dashboard.value.regionDistribution)
const maxRegion = computed(() => Math.max(...(dashboard.value.regionDistribution.map(r => r.count) || [1])))

async function fetchDashboard() {
  try {
    const res = await api.getDashboardAnalytics()
    const data = res.data?.data || res.data
    if (data) dashboard.value = { ...dashboard.value, ...data }
  } catch (e) {
    console.warn('[Analytics] fetchDashboard failed:', e.message)
  }
}

onMounted(() => { fetchDashboard() })
</script>

<style scoped>
.analytics-overview { max-width: 1200px; }
.page-header { margin-bottom: 24px; }
.page-header h2 { font-size: 20px; font-weight: 600; }
.stats-row { display: grid; grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); gap: 16px; margin-bottom: 24px; }
.glass-card { background: var(--glass); backdrop-filter: blur(24px); border: 1px solid var(--glass-border); border-radius: var(--border-radius); padding: 20px; }
.stat-card { display: flex; align-items: center; gap: 16px; }
.stat-icon { font-size: 32px; }
.stat-value { font-size: 24px; font-weight: 700; }
.stat-label { font-size: 12px; color: var(--text-muted); margin-top: 2px; }
.charts-row { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.chart-card h3 { font-size: 15px; font-weight: 600; margin-bottom: 16px; }
.chart-placeholder { display: flex; align-items: flex-end; gap: 4px; height: 180px; padding-top: 20px; }
.bar-item { flex: 1; display: flex; flex-direction: column; align-items: center; gap: 4px; height: 100%; justify-content: flex-end; }
.bar { width: 100%; border-radius: 4px 4px 0 0; min-height: 4px; transition: height 0.3s; }
.bar-label { font-size: 10px; color: var(--text-muted); }
.region-list { display: flex; flex-direction: column; gap: 8px; }
.region-item { display: flex; align-items: center; gap: 8px; }
.region-name { font-size: 13px; min-width: 32px; color: var(--text-secondary); }
.region-bar { flex: 1; height: 6px; background: rgba(255,255,255,0.06); border-radius: 3px; overflow: hidden; }
.region-fill { height: 100%; background: var(--blue); border-radius: 3px; transition: width 0.3s; }
.region-count { font-size: 12px; color: var(--text-secondary); min-width: 24px; text-align: right; }
</style>
