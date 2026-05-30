<template>
  <div class="alerts-page">
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">告警列表</h1>
        <p class="page-subtitle">实时监控车辆故障与系统告警</p>
      </div>
      <div class="header-actions">
        <button class="btn btn-secondary" @click="markAllRead"><Check class="btn-icon" />全部已读</button>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card stat-danger">
        <div class="stat-content"><div class="stat-info"><div class="stat-label">严重告警</div><div class="stat-value">{{ stats.critical }}</div></div><div class="stat-icon-wrapper icon-danger"><Warning class="stat-icon-svg" /></div></div>
        <div class="stat-glow glow-danger"></div>
      </div>
      <div class="stat-card stat-warning">
        <div class="stat-content"><div class="stat-info"><div class="stat-label">警告</div><div class="stat-value">{{ stats.warning }}</div></div><div class="stat-icon-wrapper icon-warning"><InfoFilled class="stat-icon-svg" /></div></div>
        <div class="stat-glow glow-warning"></div>
      </div>
      <div class="stat-card stat-primary">
        <div class="stat-content"><div class="stat-info"><div class="stat-label">信息</div><div class="stat-value">{{ stats.info }}</div></div><div class="stat-icon-wrapper icon-primary"><Bell class="stat-icon-svg" /></div></div>
        <div class="stat-glow glow-primary"></div>
      </div>
      <div class="stat-card stat-secondary">
        <div class="stat-content"><div class="stat-info"><div class="stat-label">未读</div><div class="stat-value">{{ stats.unread }}</div></div><div class="stat-icon-wrapper icon-secondary"><Message class="stat-icon-svg" /></div></div>
        <div class="stat-glow glow-secondary"></div>
      </div>
    </div>

    <div class="filter-bar">
      <div class="search-box"><Search class="search-icon" /><input v-model="searchQuery" type="text" placeholder="搜索告警内容..." class="search-input" /><button v-if="searchQuery" class="search-clear" @click="searchQuery = ''"><Close class="clear-icon" /></button></div>
      <el-select v-model="filterLevel" style="width: 120px"><el-option label="全部级别" value="all" /><el-option label="严重" value="critical" /><el-option label="警告" value="warning" /><el-option label="信息" value="info" /></el-select>
      <el-select v-model="filterRead" style="width: 120px"><el-option label="全部" value="all" /><el-option label="未读" value="unread" /><el-option label="已读" value="read" /></el-select>
      <button class="btn btn-secondary" @click="handleReset">重置</button>
    </div>

    <div class="card">
      <div v-for="alert in filteredAlerts" :key="alert.id" class="alert-item" :class="{ unread: !alert.read }" @click="markRead(alert)">
        <div class="alert-dot" :class="'dot-' + alert.level"></div>
        <div class="alert-body">
          <div class="alert-header">
            <span class="badge" :class="levelBadgeClass(alert.level)">{{ levelText(alert.level) }}</span>
            <span class="alert-source">{{ alert.source }}</span>
            <span class="alert-time">{{ alert.time }}</span>
          </div>
          <div class="alert-message">{{ alert.message }}</div>
        </div>
      </div>
      <div v-if="filteredAlerts.length === 0" class="empty-state">暂无告警</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { Check, Search, Close, Warning, InfoFilled, Bell, Message } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import * as api from '../../api/index.js'

const searchQuery = ref('')
const filterLevel = ref('all')
const filterRead = ref('all')

const alerts = ref<any[]>([])

const stats = computed(() => ({ critical: alerts.value.filter(a => a.level === 'critical' || a.level === 'high').length, warning: alerts.value.filter(a => a.level === 'medium').length, info: alerts.value.filter(a => a.level === 'low' || a.level === 'info').length, unread: alerts.value.filter(a => !a.read).length }))

const filteredAlerts = computed(() => {
  let r = alerts.value
  if (searchQuery.value) { const q = searchQuery.value.toLowerCase(); r = r.filter(a => (a.message || a.description || '').toLowerCase().includes(q) || (a.source || a.vehicleId || '').toLowerCase().includes(q)) }
  if (filterLevel.value !== 'all') r = r.filter(a => a.level === filterLevel.value)
  if (filterRead.value !== 'all') r = r.filter(a => filterRead.value === 'unread' ? !a.read : a.read)
  return r
})

async function fetchAlerts() {
  try {
    const res = await api.getAlerts({ page: 1, size: 50 })
    const list = res.data?.data?.list || res.data?.data?.records || res.data?.records || []
    if (Array.isArray(list) && list.length > 0) {
      alerts.value = list.map((a: any) => ({
        id: a.id,
        level: a.level === 'critical' || a.level === 'high' ? 'critical' : a.level === 'medium' ? 'warning' : 'info',
        source: a.vehicleId || a.source || '系统',
        message: a.description || a.message || a.title || '',
        time: a.createdAt ? new Date(a.createdAt).toLocaleString('zh-CN') : '',
        read: a.status === 'resolved' || a.status === 'confirmed'
      }))
    }
  } catch (e: any) {
    console.warn('[Alerts] fetchAlerts failed:', e.message)
  }
}

function levelText(l: string) { return { critical: '严重', warning: '警告', info: '信息' }[l] }
function levelBadgeClass(l: string) { return { critical: 'badge-red', warning: 'badge-yellow', info: 'badge-blue' }[l] || 'badge-default' }
function handleReset() { searchQuery.value = ''; filterLevel.value = 'all'; filterRead.value = 'all' }
function markRead(a: any) { a.read = true }
async function markAllRead() {
  alerts.value.forEach(a => a.read = true)
  try { await api.readAllAlerts() } catch (e: any) {}
  ElMessage.success('已全部标记为已读')
}

onMounted(() => { fetchAlerts() })
</script>

<style scoped>
.alerts-page { animation: fadeIn 0.3s ease; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }

.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.page-title { font-size: 22px; font-weight: 700; color: #f8fafc; margin: 0 0 4px 0; }
.page-subtitle { font-size: 13px; color: #94a3b8; margin: 0; }
.header-actions { display: flex; gap: 12px; }

.btn { display: flex; align-items: center; gap: 6px; padding: 10px 16px; border-radius: 10px; font-size: 13px; font-weight: 500; cursor: pointer; transition: all 0.2s ease; border: none; }
.btn-secondary { background: rgba(255,255,255,0.06); color: #f8fafc; border: 1px solid rgba(255,255,255,0.08); }
.btn-secondary:hover { background: rgba(255,255,255,0.1); }
.btn-icon { width: 16px; height: 16px; }

.stats-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 24px; }
.stat-card { background: linear-gradient(135deg, rgba(26, 26, 40, 0.9) 0%, rgba(18, 18, 28, 0.9) 100%); border: 1px solid rgba(255,255,255,0.06); border-radius: 16px; padding: 20px; position: relative; overflow: hidden; transition: all 0.3s ease; }
.stat-card:hover { transform: translateY(-2px); border-color: rgba(255,255,255,0.1); }
.stat-card::before { content: ''; position: absolute; top: 0; left: 0; right: 0; height: 3px; border-radius: 16px 16px 0 0; }
.stat-danger::before { background: linear-gradient(90deg, #ef4444, #dc2626); }
.stat-warning::before { background: linear-gradient(90deg, #f59e0b, #d97706); }
.stat-primary::before { background: linear-gradient(90deg, #4a9eff, #6366f1); }
.stat-secondary::before { background: linear-gradient(90deg, #c084fc, #a855f7); }
.stat-content { display: flex; justify-content: space-between; align-items: flex-start; position: relative; z-index: 1; }
.stat-label { font-size: 13px; color: #94a3b8; margin-bottom: 8px; }
.stat-value { font-size: 28px; font-weight: 700; color: #f8fafc; line-height: 1.2; }
.stat-icon-wrapper { width: 48px; height: 48px; border-radius: 12px; display: flex; align-items: center; justify-content: center; }
.stat-icon-svg { width: 24px; height: 24px; }
.icon-danger { background: rgba(239,68,68,0.15); color: #ef4444; }
.icon-warning { background: rgba(245,158,11,0.15); color: #f59e0b; }
.icon-primary { background: rgba(74,158,255,0.15); color: #4a9eff; }
.icon-secondary { background: rgba(192,132,252,0.15); color: #c084fc; }
.stat-glow { position: absolute; bottom: -20px; right: -20px; width: 80px; height: 80px; border-radius: 50%; filter: blur(30px); opacity: 0.3; }
.glow-danger { background: #ef4444; }
.glow-warning { background: #f59e0b; }
.glow-primary { background: #4a9eff; }
.glow-secondary { background: #c084fc; }

.filter-bar { display: flex; align-items: center; gap: 12px; padding: 16px 20px; margin-bottom: 20px; background: linear-gradient(135deg, rgba(26,26,40,0.9) 0%, rgba(18,18,28,0.9) 100%); border-radius: 16px; border: 1px solid rgba(255,255,255,0.06); }
.search-box { flex: 1; min-width: 240px; position: relative; display: flex; align-items: center; }
.search-icon { position: absolute; left: 14px; color: #64748b; width: 16px; height: 16px; z-index: 1; }
.search-input { width: 100%; height: 40px; padding: 0 36px 0 40px; background: rgba(255,255,255,0.04); border: 1px solid rgba(255,255,255,0.08); border-radius: 10px; color: #f8fafc; font-size: 13px; outline: none; transition: all 0.2s ease; }
.search-input:focus { border-color: rgba(74,158,255,0.5); background: rgba(74,158,255,0.06); }
.search-input::placeholder { color: #475569; }
.search-clear { position: absolute; right: 10px; background: none; border: none; color: #64748b; cursor: pointer; padding: 4px; display: flex; }
.clear-icon { width: 14px; height: 14px; }

.card { background: linear-gradient(135deg, rgba(26,26,40,0.9) 0%, rgba(18,18,28,0.9) 100%); border: 1px solid rgba(255,255,255,0.06); border-radius: 16px; padding: 20px; display: flex; flex-direction: column; gap: 8px; }

.alert-item { display: flex; align-items: flex-start; gap: 12px; padding: 14px 16px; background: rgba(255,255,255,0.02); border: 1px solid rgba(255,255,255,0.04); border-radius: 12px; cursor: pointer; transition: all 0.2s ease; }
.alert-item:hover { background: rgba(255,255,255,0.04); }
.alert-item.unread { background: rgba(255,255,255,0.04); border-color: rgba(255,255,255,0.08); }

.alert-dot { width: 8px; height: 8px; border-radius: 50%; margin-top: 6px; flex-shrink: 0; }
.dot-critical { background: #ef4444; box-shadow: 0 0 8px #ef4444; }
.dot-warning { background: #f59e0b; }
.dot-info { background: #4a9eff; }

.alert-body { flex: 1; }
.alert-header { display: flex; align-items: center; gap: 10px; margin-bottom: 6px; }
.alert-source { font-size: 12px; color: #94a3b8; font-weight: 500; }
.alert-time { font-size: 11px; color: #64748b; margin-left: auto; }
.alert-message { font-size: 13px; color: #f8fafc; line-height: 1.5; }

.badge { display: inline-flex; padding: 4px 10px; border-radius: 20px; font-size: 12px; font-weight: 500; }
.badge-red { background: rgba(239,68,68,0.15); color: #ef4444; }
.badge-yellow { background: rgba(245,158,11,0.15); color: #f59e0b; }
.badge-blue { background: rgba(74,158,255,0.15); color: #4a9eff; }
.badge-default { background: rgba(255,255,255,0.06); color: #94a3b8; }

.empty-state { text-align: center; padding: 40px 0; color: #64748b; font-size: 13px; }

@media (max-width: 1024px) { .stats-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 768px) { .filter-bar { flex-direction: column; } .search-box { width: 100%; } .stats-grid { grid-template-columns: 1fr; } }
</style>