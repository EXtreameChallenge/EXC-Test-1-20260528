<template>
  <div class="sync-monitor">
    <div class="page-header">
      <h2>数据同步监控</h2>
      <el-button type="primary" size="small" @click="refreshSync">刷新状态</el-button>
    </div>
    <div class="sync-cards">
      <div v-for="item in syncItems" :key="item.name" class="sync-card glass-card">
        <div class="sync-header">
          <span class="sync-icon">{{ item.icon }}</span>
          <span class="sync-name">{{ item.name }}</span>
          <span class="sync-status" :class="item.status">{{ item.statusText }}</span>
        </div>
        <div class="sync-detail">
          <div class="sync-row"><span>最后同步</span><span>{{ item.lastSync }}</span></div>
          <div class="sync-row"><span>数据量</span><span>{{ item.count }}</span></div>
          <div class="sync-row"><span>延迟</span><span>{{ item.latency }}</span></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const syncItems = ref([
  { icon: '🚗', name: '车辆数据', status: 'ok', statusText: '正常', lastSync: '2秒前', count: '12条', latency: '<50ms' },
  { icon: '📋', name: '任务数据', status: 'ok', statusText: '正常', lastSync: '5秒前', count: '8条', latency: '<80ms' },
  { icon: '⚠️', name: '告警数据', status: 'warn', statusText: '延迟', lastSync: '30秒前', count: '6条', latency: '200ms' },
  { icon: '🔋', name: '能源数据', status: 'ok', statusText: '正常', lastSync: '10秒前', count: '5条', latency: '<60ms' }
])

function refreshSync() {
  syncItems.value.forEach(item => { item.lastSync = '刚刚' })
}
</script>

<style scoped>
.sync-monitor { max-width: 1200px; }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 24px; }
.page-header h2 { font-size: 20px; font-weight: 600; }
.sync-cards { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 16px; }
.glass-card { background: var(--glass); backdrop-filter: blur(24px); border: 1px solid var(--glass-border); border-radius: var(--border-radius); padding: 20px; }
.sync-header { display: flex; align-items: center; gap: 8px; margin-bottom: 12px; }
.sync-icon { font-size: 20px; }
.sync-name { font-size: 15px; font-weight: 600; flex: 1; }
.sync-status { padding: 2px 8px; border-radius: 6px; font-size: 11px; font-weight: 600; }
.sync-status.ok { background: rgba(74,222,128,0.12); color: var(--green); }
.sync-status.warn { background: rgba(251,191,36,0.12); color: var(--orange); }
.sync-detail { display: flex; flex-direction: column; gap: 6px; }
.sync-row { display: flex; justify-content: space-between; font-size: 13px; }
.sync-row span:first-child { color: var(--text-muted); }
</style>
