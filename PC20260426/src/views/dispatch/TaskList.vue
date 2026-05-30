<template>
  <div class="tasks-page">
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">任务列表</h1>
        <p class="page-subtitle">管理无人车调度任务与执行状态</p>
      </div>
      <div class="header-actions">
        <button class="btn btn-primary" @click="$router.push('/admin/dispatch/new')"><Plus class="btn-icon" />新建任务</button>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card stat-primary">
        <div class="stat-content"><div class="stat-info"><div class="stat-label">任务总数</div><div class="stat-value">{{ stats.total }}</div></div><div class="stat-icon-wrapper icon-primary"><List class="stat-icon-svg" /></div></div>
        <div class="stat-glow glow-primary"></div>
      </div>
      <div class="stat-card stat-success">
        <div class="stat-content"><div class="stat-info"><div class="stat-label">执行中</div><div class="stat-value">{{ stats.running }}</div></div><div class="stat-icon-wrapper icon-success"><VideoPlay class="stat-icon-svg" /></div></div>
        <div class="stat-glow glow-success"></div>
      </div>
      <div class="stat-card stat-warning">
        <div class="stat-content"><div class="stat-info"><div class="stat-label">待执行</div><div class="stat-value">{{ stats.pending }}</div></div><div class="stat-icon-wrapper icon-warning"><Clock class="stat-icon-svg" /></div></div>
        <div class="stat-glow glow-warning"></div>
      </div>
      <div class="stat-card stat-secondary">
        <div class="stat-content"><div class="stat-info"><div class="stat-label">已完成</div><div class="stat-value">{{ stats.completed }}</div></div><div class="stat-icon-wrapper icon-secondary"><CircleCheck class="stat-icon-svg" /></div></div>
        <div class="stat-glow glow-secondary"></div>
      </div>
    </div>

    <div class="filter-bar">
      <div class="search-box"><Search class="search-icon" /><input v-model="searchQuery" type="text" placeholder="搜索任务编号/名称..." class="search-input" /><button v-if="searchQuery" class="search-clear" @click="searchQuery = ''"><Close class="clear-icon" /></button></div>
      <el-select v-model="filterStatus" style="width: 120px"><el-option label="全部状态" value="all" /><el-option label="执行中" value="running" /><el-option label="待执行" value="pending" /><el-option label="已完成" value="completed" /><el-option label="已取消" value="cancelled" /></el-select>
      <el-select v-model="filterPriority" style="width: 120px"><el-option label="全部优先级" value="all" /><el-option label="紧急" value="urgent" /><el-option label="高" value="high" /><el-option label="中" value="medium" /><el-option label="低" value="low" /></el-select>
      <button class="btn btn-secondary" @click="handleReset">重置</button>
    </div>

    <div class="card">
      <table class="data-table">
        <thead><tr><th style="width:90px">编号</th><th>任务名称</th><th style="width:90px">优先级</th><th style="width:110px">状态</th><th style="width:90px">执行车辆</th><th style="width:140px">开始时间</th><th style="width:140px">进度</th><th style="width:130px">操作</th></tr></thead>
        <tbody>
          <tr v-for="task in filteredTasks" :key="task.id">
            <td class="td-id">{{ task.id }}</td>
            <td class="td-name">{{ task.name }}</td>
            <td><span class="badge" :class="priorityBadgeClass(task.priority)">{{ priorityText(task.priority) }}</span></td>
            <td><span class="badge status-badge" :class="statusBadgeClass(task.status)"><span class="status-dot"></span>{{ statusText(task.status) }}</span></td>
            <td class="td-secondary">{{ task.vehicle }}</td>
            <td class="td-time">{{ task.startTime }}</td>
            <td><div class="progress-bar"><div class="progress-fill" :style="{ width: task.progress + '%' }"></div><span class="progress-text">{{ task.progress }}%</span></div></td>
            <td><div class="action-btns"><button class="action-btn view" @click="viewTask(task)" title="查看"><View class="action-icon" /></button><button v-if="task.status === 'pending'" class="action-btn run" @click="runTask(task)" title="执行"><VideoPlay class="action-icon" /></button><button v-if="task.status === 'running'" class="action-btn cancel" @click="cancelTask(task)" title="取消"><CloseBold class="action-icon" /></button><button class="action-btn delete" @click="deleteTask(task)" title="删除"><Delete class="action-icon" /></button></div></td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Plus, Search, Close, View, VideoPlay, CloseBold, Delete, List, Clock, CircleCheck } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useTaskStore } from '../../stores/task'

const router = useRouter()
const taskStore = useTaskStore()
const searchQuery = ref('')
const filterStatus = ref('all')
const filterPriority = ref('all')

const taskStatusMap: Record<string, string> = { pending: 'pending', confirmed: 'pending', executing: 'running', completed: 'completed', cancelled: 'cancelled' }

const tasks = computed(() => taskStore.taskList.map(t => ({
  ...t,
  status: taskStatusMap[t.status] || t.status,
  vehicle: t.vehicleName || t.vehicleId || '-',
  startTime: t.executeTime || t.createdAt,
  progress: t.status === 'completed' ? 100 : t.status === 'executing' ? 50 : t.status === 'confirmed' ? 10 : 0,
  priority: 'medium'
})))

const stats = computed(() => ({ total: tasks.value.length, running: tasks.value.filter(t => t.status === 'running').length, pending: tasks.value.filter(t => t.status === 'pending').length, completed: tasks.value.filter(t => t.status === 'completed').length }))

const filteredTasks = computed(() => {
  let r = tasks.value
  if (searchQuery.value) { const q = searchQuery.value.toLowerCase(); r = r.filter(t => t.id.toLowerCase().includes(q) || t.name.toLowerCase().includes(q)) }
  if (filterStatus.value !== 'all') r = r.filter(t => t.status === filterStatus.value)
  if (filterPriority.value !== 'all') r = r.filter(t => t.priority === filterPriority.value)
  return r
})

function priorityText(p: string) { return { urgent: '紧急', high: '高', medium: '中', low: '低' }[p] || p }
function priorityBadgeClass(p: string) { return { urgent: 'badge-red', high: 'badge-yellow', medium: 'badge-blue', low: 'badge-default' }[p] || 'badge-default' }
function statusText(s: string) { return { running: '执行中', pending: '待执行', completed: '已完成', cancelled: '已取消' }[s] || s }
function statusBadgeClass(s: string) { return { running: 'badge-green', pending: 'badge-yellow', completed: 'badge-blue', cancelled: 'badge-default' }[s] || 'badge-default' }
function handleReset() { searchQuery.value = ''; filterStatus.value = 'all'; filterPriority.value = 'all' }
function viewTask(t: any) { router.push(`/admin/dispatch/tasks/${t.id}`) }
async function runTask(t: any) { await taskStore.updateTaskStatus(t.id, 'executing'); ElMessage.success('任务已开始执行') }
async function cancelTask(t: any) { await taskStore.updateTaskStatus(t.id, 'cancelled'); ElMessage.warning('任务已取消') }
async function deleteTask(t: any) { try { await ElMessageBox.confirm(`确定删除任务 "${t.name}" 吗？`, '删除确认', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }); await taskStore.updateTaskStatus(t.id, 'cancelled'); ElMessage.success('任务已删除') } catch {} }

onMounted(() => { taskStore.fetchTasks() })
</script>

<style scoped>
.tasks-page { animation: fadeIn 0.3s ease; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }

.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.page-title { font-size: 22px; font-weight: 700; color: #f8fafc; margin: 0 0 4px 0; }
.page-subtitle { font-size: 13px; color: #94a3b8; margin: 0; }
.header-actions { display: flex; gap: 12px; }

.btn { display: flex; align-items: center; gap: 6px; padding: 10px 16px; border-radius: 10px; font-size: 13px; font-weight: 500; cursor: pointer; transition: all 0.2s ease; border: none; }
.btn-primary { background: linear-gradient(135deg, #4a9eff, #6366f1); color: #fff; }
.btn-primary:hover { opacity: 0.9; }
.btn-secondary { background: rgba(255,255,255,0.06); color: #f8fafc; border: 1px solid rgba(255,255,255,0.08); }
.btn-secondary:hover { background: rgba(255,255,255,0.1); }
.btn-icon { width: 16px; height: 16px; }

.stats-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 24px; }
.stat-card { background: linear-gradient(135deg, rgba(26, 26, 40, 0.9) 0%, rgba(18, 18, 28, 0.9) 100%); border: 1px solid rgba(255,255,255,0.06); border-radius: 16px; padding: 20px; position: relative; overflow: hidden; transition: all 0.3s ease; }
.stat-card:hover { transform: translateY(-2px); border-color: rgba(255,255,255,0.1); }
.stat-card::before { content: ''; position: absolute; top: 0; left: 0; right: 0; height: 3px; border-radius: 16px 16px 0 0; }
.stat-primary::before { background: linear-gradient(90deg, #4a9eff, #6366f1); }
.stat-success::before { background: linear-gradient(90deg, #22c55e, #16a34a); }
.stat-warning::before { background: linear-gradient(90deg, #f59e0b, #d97706); }
.stat-secondary::before { background: linear-gradient(90deg, #c084fc, #a855f7); }
.stat-content { display: flex; justify-content: space-between; align-items: flex-start; position: relative; z-index: 1; }
.stat-label { font-size: 13px; color: #94a3b8; margin-bottom: 8px; }
.stat-value { font-size: 28px; font-weight: 700; color: #f8fafc; line-height: 1.2; }
.stat-icon-wrapper { width: 48px; height: 48px; border-radius: 12px; display: flex; align-items: center; justify-content: center; }
.stat-icon-svg { width: 24px; height: 24px; }
.icon-primary { background: rgba(74,158,255,0.15); color: #4a9eff; }
.icon-success { background: rgba(34,197,94,0.15); color: #22c55e; }
.icon-warning { background: rgba(245,158,11,0.15); color: #f59e0b; }
.icon-secondary { background: rgba(192,132,252,0.15); color: #c084fc; }
.stat-glow { position: absolute; bottom: -20px; right: -20px; width: 80px; height: 80px; border-radius: 50%; filter: blur(30px); opacity: 0.3; }
.glow-primary { background: #4a9eff; }
.glow-success { background: #22c55e; }
.glow-warning { background: #f59e0b; }
.glow-secondary { background: #c084fc; }

.filter-bar { display: flex; align-items: center; gap: 12px; padding: 16px 20px; margin-bottom: 20px; background: linear-gradient(135deg, rgba(26,26,40,0.9) 0%, rgba(18,18,28,0.9) 100%); border-radius: 16px; border: 1px solid rgba(255,255,255,0.06); }
.search-box { flex: 1; min-width: 240px; position: relative; display: flex; align-items: center; }
.search-icon { position: absolute; left: 14px; color: #64748b; width: 16px; height: 16px; z-index: 1; }
.search-input { width: 100%; height: 40px; padding: 0 36px 0 40px; background: rgba(255,255,255,0.04); border: 1px solid rgba(255,255,255,0.08); border-radius: 10px; color: #f8fafc; font-size: 13px; outline: none; transition: all 0.2s ease; }
.search-input:focus { border-color: rgba(74,158,255,0.5); background: rgba(74,158,255,0.06); }
.search-input::placeholder { color: #475569; }
.search-clear { position: absolute; right: 10px; background: none; border: none; color: #64748b; cursor: pointer; padding: 4px; display: flex; }
.clear-icon { width: 14px; height: 14px; }

.card { background: linear-gradient(135deg, rgba(26,26,40,0.9) 0%, rgba(18,18,28,0.9) 100%); border: 1px solid rgba(255,255,255,0.06); border-radius: 16px; padding: 20px; overflow-x: auto; }
.data-table { width: 100%; border-collapse: collapse; }
.data-table th { padding: 12px 16px; text-align: left; font-size: 12px; font-weight: 600; color: #94a3b8; background: rgba(255,255,255,0.02); border-bottom: 1px solid rgba(255,255,255,0.06); white-space: nowrap; }
.data-table td { padding: 14px 16px; font-size: 13px; color: #f8fafc; border-bottom: 1px solid rgba(255,255,255,0.04); }
.data-table tbody tr { transition: background 0.15s ease; }
.data-table tbody tr:hover { background: rgba(255,255,255,0.03); }
.td-id { font-family: monospace; font-size: 12px; color: #94a3b8; }
.td-name { font-weight: 500; }
.td-secondary { color: #94a3b8; }
.td-time { color: #64748b; font-size: 12px; }

.badge { display: inline-flex; padding: 4px 10px; border-radius: 20px; font-size: 12px; font-weight: 500; }
.badge-red { background: rgba(239,68,68,0.15); color: #ef4444; }
.badge-yellow { background: rgba(245,158,11,0.15); color: #f59e0b; }
.badge-blue { background: rgba(74,158,255,0.15); color: #4a9eff; }
.badge-green { background: rgba(34,197,94,0.15); color: #22c55e; }
.badge-purple { background: rgba(139,92,246,0.15); color: #8b5cf6; }
.badge-default { background: rgba(100,116,139,0.15); color: #94a3b8; }

.status-badge { align-items: center; gap: 6px; }
.status-dot { width: 6px; height: 6px; border-radius: 50%; display: inline-block; }
.badge-green .status-dot { background: #22c55e; box-shadow: 0 0 6px #22c55e; }
.badge-yellow .status-dot { background: #f59e0b; }
.badge-blue .status-dot { background: #4a9eff; }
.badge-default .status-dot { background: #64748b; }

.progress-bar { position: relative; width: 100%; height: 20px; background: rgba(255,255,255,0.04); border-radius: 10px; overflow: hidden; }
.progress-fill { height: 100%; background: linear-gradient(90deg, #4a9eff, #8b5cf6); border-radius: 10px; transition: width 0.5s ease; }
.progress-text { position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); font-size: 11px; font-weight: 600; color: #f8fafc; z-index: 1; }

.action-btns { display: flex; gap: 4px; }
.action-btn { width: 32px; height: 32px; display: flex; align-items: center; justify-content: center; border: none; background: rgba(255,255,255,0.04); border-radius: 8px; cursor: pointer; transition: all 0.15s ease; color: #94a3b8; }
.action-icon { width: 16px; height: 16px; }
.action-btn:hover { background: rgba(255,255,255,0.08); color: #f8fafc; }
.action-btn.view:hover { color: #4a9eff; }
.action-btn.run:hover { color: #22c55e; }
.action-btn.cancel:hover { color: #f59e0b; }
.action-btn.delete:hover { color: #ef4444; }

@media (max-width: 1024px) { .stats-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 768px) { .filter-bar { flex-direction: column; } .search-box { width: 100%; } .stats-grid { grid-template-columns: 1fr; } }
</style>