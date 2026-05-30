<template>
  <div class="vehicle-detail">
    <div class="page-header">
      <el-button @click="$router.back()" text>
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="width:16px;height:16px;margin-right:4px"><polyline points="15 18 9 12 15 6"/></svg>
        返回
      </el-button>
      <h2>{{ vehicle?.name || vehicleId }}</h2>
    </div>
    <div v-if="vehicle" class="detail-content">
      <div class="detail-card glass-card">
        <div class="card-header">
          <h3>基本信息</h3>
          <span class="status-tag" :class="vehicle.status">{{ statusMap[vehicle.status] }}</span>
        </div>
        <div class="info-grid">
          <div class="info-item"><span class="label">车辆编号</span><span class="value">{{ vehicle.id }}</span></div>
          <div class="info-item"><span class="label">车型</span><span class="value">{{ vehicle.model }}</span></div>
          <div class="info-item"><span class="label">位置</span><span class="value">{{ vehicle.location }}</span></div>
          <div class="info-item"><span class="label">里程</span><span class="value">{{ vehicle.mileage }} km</span></div>
          <div class="info-item"><span class="label">最后更新</span><span class="value">{{ vehicle.lastUpdate }}</span></div>
        </div>
      </div>
      <div class="detail-card glass-card">
        <div class="card-header"><h3>电量状态</h3></div>
        <div class="battery-display">
          <div class="battery-bar"><div class="battery-fill" :style="{ width: vehicle.battery + '%', background: batteryColor }"></div></div>
          <span class="battery-pct" :style="{ color: batteryColor }">{{ vehicle.battery }}%</span>
        </div>
      </div>
    </div>
    <div v-else class="empty-state">
      <p>未找到车辆信息</p>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useVehicleStore } from '../../stores/vehicle'

const route = useRoute()
const vehicleStore = useVehicleStore()
const vehicleId = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id

const vehicle = computed(() => vehicleStore.getVehicleById(vehicleId))

const statusMap = { standby: '待命', delivery: '配送中', charging: '充电中', fault: '故障' }

const batteryColor = computed(() => {
  const b = vehicle.value?.battery || 0
  return b > 50 ? 'var(--green)' : b > 20 ? 'var(--orange)' : 'var(--red)'
})
</script>

<style scoped>
.vehicle-detail { max-width: 900px; }
.page-header { display: flex; align-items: center; gap: 16px; margin-bottom: 24px; }
.page-header h2 { font-size: 20px; font-weight: 600; }
.glass-card { background: var(--glass); backdrop-filter: blur(24px); border: 1px solid var(--glass-border); border-radius: var(--border-radius); padding: 20px; margin-bottom: 16px; }
.card-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px; }
.card-header h3 { font-size: 15px; font-weight: 600; }
.status-tag { padding: 4px 12px; border-radius: 8px; font-size: 12px; font-weight: 600; }
.status-tag.standby { background: rgba(74,222,128,0.12); color: var(--green); }
.status-tag.delivery { background: rgba(74,158,255,0.12); color: var(--blue); }
.status-tag.charging { background: rgba(251,191,36,0.12); color: var(--orange); }
.status-tag.fault { background: rgba(248,113,113,0.12); color: var(--red); }
.info-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); gap: 16px; }
.info-item { display: flex; flex-direction: column; gap: 4px; }
.info-item .label { font-size: 12px; color: var(--text-muted); }
.info-item .value { font-size: 14px; font-weight: 500; }
.battery-display { display: flex; align-items: center; gap: 12px; }
.battery-bar { flex: 1; height: 8px; background: rgba(255,255,255,0.08); border-radius: 4px; overflow: hidden; }
.battery-fill { height: 100%; border-radius: 4px; transition: width 0.3s; }
.battery-pct { font-size: 20px; font-weight: 700; min-width: 60px; text-align: right; }
.empty-state { text-align: center; padding: 60px; color: var(--text-muted); }
</style>
