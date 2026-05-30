<template>
  <div class="orders-page">
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">维修工单</h1>
        <p class="page-subtitle">管理车辆故障维修与保养工单</p>
      </div>
      <div class="header-actions">
        <button class="btn btn-primary" @click="showCreate = true"><Plus class="btn-icon" />新建工单</button>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card stat-primary">
        <div class="stat-content"><div class="stat-info"><div class="stat-label">工单总数</div><div class="stat-value">{{ stats.total }}</div></div><div class="stat-icon-wrapper icon-primary"><Document class="stat-icon-svg" /></div></div>
        <div class="stat-glow glow-primary"></div>
      </div>
      <div class="stat-card stat-danger">
        <div class="stat-content"><div class="stat-info"><div class="stat-label">待处理</div><div class="stat-value">{{ stats.pending }}</div></div><div class="stat-icon-wrapper icon-danger"><Warning class="stat-icon-svg" /></div></div>
        <div class="stat-glow glow-danger"></div>
      </div>
      <div class="stat-card stat-warning">
        <div class="stat-content"><div class="stat-info"><div class="stat-label">处理中</div><div class="stat-value">{{ stats.processing }}</div></div><div class="stat-icon-wrapper icon-warning"><SetUp class="stat-icon-svg" /></div></div>
        <div class="stat-glow glow-warning"></div>
      </div>
      <div class="stat-card stat-success">
        <div class="stat-content"><div class="stat-info"><div class="stat-label">已完成</div><div class="stat-value">{{ stats.completed }}</div></div><div class="stat-icon-wrapper icon-success"><CircleCheck class="stat-icon-svg" /></div></div>
        <div class="stat-glow glow-success"></div>
      </div>
    </div>

    <div class="filter-bar">
      <div class="search-box"><Search class="search-icon" /><input v-model="search" type="text" placeholder="搜索工单编号/车辆..." class="search-input" /><button v-if="search" class="search-clear" @click="search = ''"><Close class="clear-icon" /></button></div>
      <el-select v-model="filterStatus" style="width: 120px"><el-option label="全部状态" value="all" /><el-option label="待处理" value="pending" /><el-option label="处理中" value="processing" /><el-option label="已完成" value="completed" /></el-select>
      <el-select v-model="filterLevel" style="width: 120px"><el-option label="全部级别" value="all" /><el-option label="严重" value="critical" /><el-option label="一般" value="normal" /><el-option label="轻微" value="minor" /></el-select>
      <button class="btn btn-secondary" @click="reset">重置</button>
    </div>

    <div class="card">
      <table class="data-table">
        <thead><tr><th style="width:90px">编号</th><th>工单标题</th><th style="width:80px">级别</th><th style="width:100px">状态</th><th style="width:80px">车辆</th><th style="width:80px">负责人</th><th style="width:140px">创建时间</th><th style="width:110px">操作</th></tr></thead>
        <tbody>
          <tr v-for="o in filtered" :key="o.id">
            <td class="td-id">{{ o.id }}</td>
            <td class="td-name">{{ o.title }}</td>
            <td><span class="badge" :class="sevBadgeClass(o.severity)">{{ sevText(o.severity) }}</span></td>
            <td><span class="badge status-badge" :class="stBadgeClass(o.status)"><span class="status-dot"></span>{{ stText(o.status) }}</span></td>
            <td class="td-secondary">{{ o.vehicle }}</td>
            <td class="td-secondary">{{ o.assignee }}</td>
            <td class="td-time">{{ o.createdAt }}</td>
            <td><div class="action-btns"><button class="action-btn view" @click="view(o)" title="查看"><View class="action-icon" /></button><button v-if="o.status === 'pending'" class="action-btn run" @click="process(o)" title="执行"><SetUp class="action-icon" /></button><button v-if="o.status === 'processing'" class="action-btn done" @click="complete(o)" title="完成"><CircleCheck class="action-icon" /></button></div></td>
          </tr>
        </tbody>
      </table>
    </div>

    <el-dialog v-model="showCreate" title="新建维修工单" width="500px">
      <div class="dialog-form">
        <div class="form-item"><label class="form-label">工单标题</label><input v-model="createForm.title" type="text" placeholder="请输入工单标题" class="form-input" /></div>
        <div class="form-row"><div class="form-item"><label class="form-label">关联车辆</label><el-select v-model="createForm.vehicle" class="form-select"><el-option label="V-005 配送先锋五号" value="V-005" /><el-option label="V-006 巡检卫士六号" value="V-006" /></el-select></div><div class="form-item"><label class="form-label">严重级别</label><el-select v-model="createForm.severity" class="form-select"><el-option label="严重" value="critical" /><el-option label="一般" value="normal" /><el-option label="轻微" value="minor" /></el-select></div></div>
        <div class="form-item"><label class="form-label">问题描述</label><textarea v-model="createForm.desc" placeholder="请描述故障情况..." class="form-textarea" rows="3"></textarea></div>
      </div>
      <template #footer><el-button @click="showCreate = false">取消</el-button><el-button type="primary" @click="create">创建</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { Plus, Search, Close, View, SetUp, CircleCheck, Document, Warning } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import * as api from '../../api/index.js'

const search = ref('')
const filterStatus = ref('all')
const filterLevel = ref('all')
const showCreate = ref(false)
const createForm = ref({ title: '', vehicle: '', severity: 'normal', desc: '' })

const orders = ref<any[]>([])

const stats = computed(() => ({ total: orders.value.length, pending: orders.value.filter(o => o.status === 'pending').length, processing: orders.value.filter(o => o.status === 'processing').length, completed: orders.value.filter(o => o.status === 'completed').length }))

const filtered = computed(() => {
  let r = orders.value
  if (search.value) { const q = search.value.toLowerCase(); r = r.filter(o => o.id.toLowerCase().includes(q) || o.title.toLowerCase().includes(q)) }
  if (filterStatus.value !== 'all') r = r.filter(o => o.status === filterStatus.value)
  if (filterLevel.value !== 'all') r = r.filter(o => o.severity === filterLevel.value)
  return r
})

async function fetchOrders() {
  try {
    const res = await api.getWorkOrders({ page: 1, size: 50 })
    const list = res.data?.data?.records || res.data?.records || []
    if (Array.isArray(list) && list.length > 0) {
      orders.value = list.map((o: any) => ({
        id: o.id || o.workOrderId,
        title: o.title || o.description || '',
        severity: o.priority === 'urgent' || o.priority === 'high' ? 'critical' : o.priority === 'medium' ? 'normal' : 'minor',
        status: o.status === 'in_progress' ? 'processing' : o.status === 'done' || o.status === 'completed' ? 'completed' : 'pending',
        vehicle: o.vehicleId || '-',
        assignee: o.assignee || '-',
        createdAt: o.createdAt ? new Date(o.createdAt).toLocaleString('zh-CN') : ''
      }))
    }
  } catch (e: any) {
    console.warn('[Orders] fetchOrders failed:', e.message)
  }
}

function sevText(s: string) { return { critical: '严重', normal: '一般', minor: '轻微' }[s] }
function sevBadgeClass(s: string) { return { critical: 'badge-red', normal: 'badge-yellow', minor: 'badge-blue' }[s] || 'badge-default' }
function stText(s: string) { return { pending: '待处理', processing: '处理中', completed: '已完成' }[s] }
function stBadgeClass(s: string) { return { pending: 'badge-red', processing: 'badge-yellow', completed: 'badge-green' }[s] || 'badge-default' }
function reset() { search.value = ''; filterStatus.value = 'all'; filterLevel.value = 'all' }
function view(o: any) { ElMessage.info(`查看工单: ${o.title}`) }
async function process(o: any) { o.status = 'processing'; try { await api.startWorkOrder(o.id) } catch(e: any) {} ElMessage.success('工单已开始处理') }
async function complete(o: any) { o.status = 'completed'; try { await api.completeWorkOrder(o.id) } catch(e: any) {} ElMessage.success('工单已完成') }
async function create() {
  if (!createForm.value.title) { ElMessage.warning('请填写工单标题'); return }
  try { await api.createWorkOrder({ title: createForm.value.title, vehicleId: createForm.value.vehicle, priority: createForm.value.severity === 'critical' ? 'high' : 'medium', description: createForm.value.desc }) } catch(e: any) {}
  showCreate.value = false; createForm.value = { title: '', vehicle: '', severity: 'normal', desc: '' }; ElMessage.success('工单已创建'); fetchOrders()
}

onMounted(() => { fetchOrders() })
</script>

<style scoped>
.orders-page { animation: fadeIn 0.3s ease; }
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
.stat-danger::before { background: linear-gradient(90deg, #ef4444, #dc2626); }
.stat-warning::before { background: linear-gradient(90deg, #f59e0b, #d97706); }
.stat-success::before { background: linear-gradient(90deg, #22c55e, #16a34a); }
.stat-content { display: flex; justify-content: space-between; align-items: flex-start; position: relative; z-index: 1; }
.stat-label { font-size: 13px; color: #94a3b8; margin-bottom: 8px; }
.stat-value { font-size: 28px; font-weight: 700; color: #f8fafc; line-height: 1.2; }
.stat-icon-wrapper { width: 48px; height: 48px; border-radius: 12px; display: flex; align-items: center; justify-content: center; }
.stat-icon-svg { width: 24px; height: 24px; }
.icon-primary { background: rgba(74,158,255,0.15); color: #4a9eff; }
.icon-danger { background: rgba(239,68,68,0.15); color: #ef4444; }
.icon-warning { background: rgba(245,158,11,0.15); color: #f59e0b; }
.icon-success { background: rgba(34,197,94,0.15); color: #22c55e; }
.stat-glow { position: absolute; bottom: -20px; right: -20px; width: 80px; height: 80px; border-radius: 50%; filter: blur(30px); opacity: 0.3; }
.glow-primary { background: #4a9eff; }
.glow-danger { background: #ef4444; }
.glow-warning { background: #f59e0b; }
.glow-success { background: #22c55e; }

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
.badge-default { background: rgba(100,116,139,0.15); color: #94a3b8; }

.status-badge { align-items: center; gap: 6px; }
.status-dot { width: 6px; height: 6px; border-radius: 50%; display: inline-block; }
.badge-red .status-dot { background: #ef4444; }
.badge-yellow .status-dot { background: #f59e0b; box-shadow: 0 0 6px #f59e0b; }
.badge-green .status-dot { background: #22c55e; }

.action-btns { display: flex; gap: 4px; }
.action-btn { width: 32px; height: 32px; display: flex; align-items: center; justify-content: center; border: none; background: rgba(255,255,255,0.04); border-radius: 8px; cursor: pointer; transition: all 0.15s ease; color: #94a3b8; }
.action-icon { width: 16px; height: 16px; }
.action-btn:hover { background: rgba(255,255,255,0.08); color: #f8fafc; }
.action-btn.view:hover { color: #4a9eff; }
.action-btn.run:hover { color: #f59e0b; }
.action-btn.done:hover { color: #22c55e; }

.dialog-form { display: flex; flex-direction: column; gap: 16px; }
.form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.form-item { display: flex; flex-direction: column; gap: 8px; }
.form-label { font-size: 13px; color: #94a3b8; font-weight: 500; }
.form-input { height: 42px; padding: 0 14px; background: rgba(255,255,255,0.04); border: 1px solid rgba(255,255,255,0.08); border-radius: 10px; color: #f8fafc; font-size: 13px; outline: none; transition: all 0.2s ease; }
.form-input:focus { border-color: rgba(74,158,255,0.5); background: rgba(74,158,255,0.06); }
.form-input::placeholder { color: #475569; }
.form-textarea { padding: 12px 14px; background: rgba(255,255,255,0.04); border: 1px solid rgba(255,255,255,0.08); border-radius: 10px; color: #f8fafc; font-size: 13px; outline: none; resize: vertical; font-family: inherit; }
.form-textarea:focus { border-color: rgba(74,158,255,0.5); }
.form-textarea::placeholder { color: #475569; }
.form-select { width: 100%; }

@media (max-width: 1024px) { .stats-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 768px) { .filter-bar { flex-direction: column; } .search-box { width: 100%; } .stats-grid { grid-template-columns: 1fr; } }
</style>