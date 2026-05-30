<template>
  <div class="vehicle-page">
    <PageHeader title="车辆管理" subtitle="实时监控与管理无人车队全部车辆">
      <template #actions>
        <el-button class="btn-glow" @click="openAddDialog">
          <el-icon class="mr-1"><Plus /></el-icon>添加车辆
        </el-button>
      </template>
    </PageHeader>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card stat-primary">
        <div class="stat-content">
          <div class="stat-info">
            <div class="stat-label">车辆总数</div>
            <div class="stat-value">{{ stats.total }}</div>
          </div>
          <div class="stat-icon-wrapper icon-primary"><el-icon :size="24"><Van /></el-icon></div>
        </div>
        <div class="stat-glow glow-primary"></div>
      </div>
      <div class="stat-card stat-success">
        <div class="stat-content">
          <div class="stat-info">
            <div class="stat-label">运行中</div>
            <div class="stat-value">{{ stats.running }}</div>
          </div>
          <div class="stat-icon-wrapper icon-success"><el-icon :size="24"><CircleCheck /></el-icon></div>
        </div>
        <div class="stat-glow glow-success"></div>
      </div>
      <div class="stat-card stat-warning">
        <div class="stat-content">
          <div class="stat-info">
            <div class="stat-label">充电中</div>
            <div class="stat-value">{{ stats.charging }}</div>
          </div>
          <div class="stat-icon-wrapper icon-warning"><el-icon :size="24"><Lightning /></el-icon></div>
        </div>
        <div class="stat-glow glow-warning"></div>
      </div>
      <div class="stat-card stat-danger">
        <div class="stat-content">
          <div class="stat-info">
            <div class="stat-label">故障/维修</div>
            <div class="stat-value">{{ stats.maintenance }}</div>
          </div>
          <div class="stat-icon-wrapper icon-danger"><el-icon :size="24"><Warning /></el-icon></div>
        </div>
        <div class="stat-glow glow-danger"></div>
      </div>
    </div>

    <!-- 搜索表单 -->
    <el-form :model="searchForm" :inline="true" ref="searchFormRef" class="search-form">
      <el-form-item label="车辆搜索" prop="keyword">
        <el-input
          v-model="searchForm.keyword"
          placeholder="搜索车辆编号、名称..."
          clearable
          style="width: 240px"
        />
      </el-form-item>
      <el-form-item label="车辆状态" prop="status">
        <el-select v-model="searchForm.status" placeholder="全部状态" clearable style="width: 130px">
          <el-option label="空闲" value="idle" />
          <el-option label="运行中" value="running" />
          <el-option label="充电中" value="charging" />
          <el-option label="维修中" value="maintenance" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleResetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮区 -->
    <div class="action-bar">
      <el-button type="primary" @click="openAddDialog">新增车辆</el-button>
      <el-button type="danger" @click="handleBatchDelete">批量删除</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table
      :data="pagedVehicles"
      border
      stripe
      @selection-change="handleSelectionChange"
      v-loading="loading"
      :header-cell-style="{ background: 'transparent', color: 'var(--text-secondary)' }"
      :cell-style="{ background: 'transparent', color: 'var(--text-primary)' }"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="vehicleNo" label="编号" width="120" align="center">
        <template #default="{ row }">
          <span class="cell-id">{{ row.vehicleNo || row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="车辆名称" />
      <el-table-column prop="model" label="型号" width="120" />
      <el-table-column prop="displayStatus" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.displayStatus === 'running'" type="success" size="small">运行中</el-tag>
          <el-tag v-else-if="row.displayStatus === 'idle'" type="info" size="small">空闲</el-tag>
          <el-tag v-else-if="row.displayStatus === 'charging'" type="warning" size="small">充电中</el-tag>
          <el-tag v-else-if="row.displayStatus === 'maintenance'" type="danger" size="small">维修中</el-tag>
          <el-tag v-else type="info" size="small">{{ row.displayStatus }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="battery" label="电量" width="140">
        <template #default="{ row }">
          <div class="battery-cell">
            <div class="battery-track">
              <div class="battery-bar" :class="getBatteryClass(row.battery)" :style="{ width: row.battery + '%' }"></div>
            </div>
            <span class="battery-num">{{ row.battery }}%</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="mileage" label="里程" width="120">
        <template #default="{ row }">
          <span class="cell-muted">{{ row.mileage ? row.mileage.toLocaleString() + ' km' : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="location" label="位置" />
      <el-table-column label="操作" width="200" align="center">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="openEditDialog(row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      :page-sizes="[10, 20, 50, 100]"
      background
      layout="total, sizes, prev, pager, next, jumper"
      class="pagination"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <!-- 添加车辆弹窗 -->
    <el-dialog v-model="showAddDialog" title="添加车辆" width="520px" :close-on-click-modal="false" @open="handleAddDialogOpen">
      <el-form :model="addForm" :rules="addRules" ref="addFormRef" label-width="80px">
        <el-form-item label="车辆编号" prop="id">
          <el-input v-model="addForm.id" placeholder="如 V-013" />
        </el-form-item>
        <el-form-item label="车辆名称" prop="name">
          <el-input v-model="addForm.name" placeholder="如 配送车-13" />
        </el-form-item>
        <el-form-item label="车辆型号" prop="model">
          <el-input v-model="addForm.model" placeholder="如 DM-A1" />
        </el-form-item>
        <el-form-item label="初始状态" prop="status">
          <el-select v-model="addForm.status" placeholder="请选择状态">
            <el-option label="空闲" value="standby" />
            <el-option label="配送中" value="delivering" />
            <el-option label="充电中" value="charging" />
            <el-option label="故障" value="fault" />
          </el-select>
        </el-form-item>
        <el-form-item label="电量" prop="battery">
          <el-input v-model.number="addForm.battery" type="number" placeholder="100" />
        </el-form-item>
        <el-form-item label="位置" prop="location">
          <el-input v-model="addForm.location" placeholder="如 A区仓库" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="handleAdd" :loading="submitting">确认添加</el-button>
      </template>
    </el-dialog>

    <!-- 编辑车辆弹窗 -->
    <el-dialog v-model="showEditDialog" title="编辑车辆" width="520px" :close-on-click-modal="false" @open="handleEditDialogOpen">
      <el-form :model="editForm" :rules="editRules" ref="editFormRef" label-width="80px">
        <el-form-item label="车辆编号">
          <el-input :value="editForm.vehicleNo" disabled />
        </el-form-item>
        <el-form-item label="车辆名称" prop="name">
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="车辆型号" prop="model">
          <el-input v-model="editForm.model" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="editForm.status" placeholder="请选择状态">
            <el-option label="空闲" value="standby" />
            <el-option label="配送中" value="delivering" />
            <el-option label="充电中" value="charging" />
            <el-option label="故障" value="fault" />
          </el-select>
        </el-form-item>
        <el-form-item label="电量" prop="battery">
          <el-input v-model.number="editForm.battery" type="number" />
        </el-form-item>
        <el-form-item label="里程" prop="mileage">
          <el-input v-model.number="editForm.mileage" type="number" />
        </el-form-item>
        <el-form-item label="位置" prop="location">
          <el-input v-model="editForm.location" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="handleEdit" :loading="submitting">保存修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted, nextTick } from 'vue'
import { Plus, Van, CircleCheck, Lightning, Warning } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import PageHeader from '../../components/layout/PageHeader.vue'
import { useVehicleStore } from '../../stores/vehicle'

const vehicleStore = useVehicleStore()
const loading = ref(false)
const submitting = ref(false)

// ===== 搜索表单 =====
const searchFormRef = ref(null)
const searchForm = reactive({ keyword: '', status: '' })

// ===== 分页 =====
const currentPage = ref(1)
const pageSize = ref(10)

// ===== 多选 =====
const selectedRows = ref([])

// ===== 弹窗状态 =====
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const addFormRef = ref(null)
const editFormRef = ref(null)

const addForm = ref({ id: '', name: '', model: 'DM-A1', status: 'standby', battery: 100, location: '' })
const editForm = ref({ id: 0, vehicleNo: '', name: '', model: '', status: '', battery: 0, mileage: 0, location: '' })

// ===== 校验规则 =====
const addRules = reactive({
  name: [{ required: true, message: '请输入车辆名称', trigger: 'blur' }],
  model: [{ required: true, message: '请输入车辆型号', trigger: 'blur' }],
  status: [{ required: true, message: '请选择初始状态', trigger: 'change' }]
})

const editRules = reactive({
  name: [{ required: true, message: '请输入车辆名称', trigger: 'blur' }],
  model: [{ required: true, message: '请输入车辆型号', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
})

// ===== 数据计算 =====
const statusMap = { standby: 'idle', delivery: 'running', delivering: 'running', charging: 'charging', fault: 'maintenance' }

const vehicles = computed(() => vehicleStore.vehicleList.map(v => ({
  ...v,
  displayStatus: statusMap[v.status] || v.status
})))

const stats = computed(() => ({
  total: vehicles.value.length,
  running: vehicles.value.filter(v => v.displayStatus === 'running').length,
  charging: vehicles.value.filter(v => v.displayStatus === 'charging').length,
  maintenance: vehicles.value.filter(v => v.displayStatus === 'maintenance').length
}))

// 搜索过滤
const filteredVehicles = computed(() => {
  let result = vehicles.value
  if (searchForm.keyword) {
    const q = searchForm.keyword.toLowerCase()
    result = result.filter(v =>
      (v.vehicleNo || '').toLowerCase().includes(q) || v.name.toLowerCase().includes(q)
    )
  }
  if (searchForm.status) {
    result = result.filter(v => v.displayStatus === searchForm.status)
  }
  return result
})

// 分页数据
const total = computed(() => filteredVehicles.value.length)

const pagedVehicles = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return filteredVehicles.value.slice(start, start + pageSize.value)
})

// 监听搜索条件变化，自动重置页码
watch(() => searchForm.keyword, () => { currentPage.value = 1 })
watch(() => searchForm.status, () => { currentPage.value = 1 })

// ===== 搜索/重置 =====
const handleSearch = () => {
  currentPage.value = 1
  ElMessage.success('搜索完成')
}

const handleResetSearch = () => {
  searchForm.keyword = ''
  searchForm.status = ''
  currentPage.value = 1
}

// ===== 分页事件 =====
const handleSizeChange = (newSize) => {
  pageSize.value = newSize
  currentPage.value = 1
}

const handleCurrentChange = (newPage) => {
  currentPage.value = newPage
}

// ===== 多选 =====
const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

// ===== 批量删除 =====
const handleBatchDelete = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请至少选择一条数据进行删除')
    return
  }
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedRows.value.length} 辆车吗？此操作不可撤销。`,
      '批量删除确认',
      { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
    )
    for (const v of selectedRows.value) {
      await vehicleStore.removeVehicle(v.id)
    }
    ElMessage.success('批量删除成功')
    selectedRows.value = []
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('批量删除失败')
  }
}

// ===== 工具函数 =====
function getBatteryClass(b) {
  if (b > 60) return 'bar-green'
  if (b > 30) return 'bar-orange'
  return 'bar-red'
}

// ===== 添加车辆 =====
function openAddDialog() {
  addForm.value = { id: '', name: '', model: 'DM-A1', status: 'standby', battery: 100, location: '' }
  showAddDialog.value = true
}

function handleAddDialogOpen() {
  nextTick(() => { addFormRef.value?.clearValidate() })
}

async function handleAdd() {
  try {
    await addFormRef.value.validate()
  } catch { return }
  submitting.value = true
  try {
    await vehicleStore.addVehicle({ ...addForm.value, id: addForm.value.id || ('V-' + Date.now()) })
    ElMessage.success('添加成功')
    showAddDialog.value = false
  } catch (e) { ElMessage.error('添加失败') }
  finally { submitting.value = false }
}

// ===== 编辑车辆 =====
function openEditDialog(v) {
  editForm.value = { id: v.id, vehicleNo: v.vehicleNo || v.id, name: v.name, model: v.model, status: v.status, battery: v.battery, mileage: v.mileage || 0, location: v.location }
  showEditDialog.value = true
}

function handleEditDialogOpen() {
  nextTick(() => { editFormRef.value?.clearValidate() })
}

async function handleEdit() {
  try {
    await editFormRef.value.validate()
  } catch { return }
  submitting.value = true
  try {
    await vehicleStore.updateVehicle(editForm.value.id, { name: editForm.value.name, model: editForm.value.model, status: editForm.value.status, battery: editForm.value.battery, mileage: editForm.value.mileage, location: editForm.value.location })
    ElMessage.success('修改成功')
    showEditDialog.value = false
  } catch (e) { ElMessage.error('修改失败') }
  finally { submitting.value = false }
}

// ===== 单条删除 =====
async function handleDelete(v) {
  try {
    await ElMessageBox.confirm(`确定删除车辆「${v.name}」？此操作不可撤销。`, '确认删除', { confirmButtonText: '删除', cancelButtonText: '取消', type: 'warning' })
    await vehicleStore.removeVehicle(v.id)
    ElMessage.success('删除成功')
  } catch (e) { if (e !== 'cancel') ElMessage.error('删除失败') }
}

onMounted(() => { vehicleStore.fetchVehicles() })
</script>

<style scoped>
.vehicle-page {
  animation: fadeIn 0.4s ease;
  padding: 0 0 40px;
}
@keyframes fadeIn { from { opacity: 0; transform: translateY(12px); } to { opacity: 1; transform: translateY(0); } }

/* ====== 统计卡片 ====== */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}
.stat-card {
  background: linear-gradient(135deg, rgba(26, 26, 46, 0.95) 0%, rgba(16, 16, 32, 0.95) 100%);
  border: 1px solid var(--glass-border);
  border-radius: var(--radius-xl);
  padding: 22px 24px;
  position: relative;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}
.stat-card:hover {
  transform: translateY(-3px);
  border-color: var(--border-color-strong);
  box-shadow: var(--shadow-lg);
}
.stat-card::before {
  content: '';
  position: absolute;
  top: 0; left: 0; right: 0;
  height: 3px;
  border-radius: 16px 16px 0 0;
}
.stat-primary::before { background: linear-gradient(90deg, #4a9eff, #6366f1); }
.stat-success::before { background: linear-gradient(90deg, #22c55e, #10b981); }
.stat-warning::before { background: linear-gradient(90deg, #f59e0b, #d97706); }
.stat-danger::before { background: linear-gradient(90deg, #ef4444, #dc2626); }

.stat-content { display: flex; justify-content: space-between; align-items: center; position: relative; z-index: 1; }
.stat-label { font-size: 13px; color: var(--text-secondary); margin-bottom: 8px; letter-spacing: 0.5px; }
.stat-value { font-size: 32px; font-weight: 700; color: var(--text-primary); line-height: 1; }

.stat-icon-wrapper {
  width: 52px; height: 52px;
  border-radius: 14px;
  display: flex; align-items: center; justify-content: center;
}
.icon-primary { background: rgba(74, 158, 255, 0.12); color: var(--blue); }
.icon-success { background: rgba(34, 197, 94, 0.12); color: var(--green); }
.icon-warning { background: rgba(245, 158, 11, 0.12); color: var(--orange); }
.icon-danger { background: rgba(239, 68, 68, 0.12); color: var(--red); }

.stat-glow {
  position: absolute;
  bottom: -30px; right: -30px;
  width: 100px; height: 100px;
  border-radius: 50%;
  filter: blur(40px);
  opacity: 0.25;
  pointer-events: none;
}
.glow-primary { background: var(--blue); }
.glow-success { background: var(--green); }
.glow-warning { background: var(--orange); }
.glow-danger { background: var(--red); }

/* ====== 搜索表单区域 ====== */
.search-form {
  background: linear-gradient(135deg, rgba(26, 26, 46, 0.8) 0%, rgba(16, 16, 32, 0.8) 100%);
  backdrop-filter: blur(24px);
  border: 1px solid var(--glass-border);
  border-radius: var(--radius-lg);
  padding: 20px 24px 4px;
  margin-bottom: 20px;
}

/* ====== 操作按钮区 ====== */
.action-bar {
  margin-bottom: 20px;
}

/* ====== 表格卡片 ====== */
.vehicle-page :deep(.el-table) {
  background: linear-gradient(135deg, rgba(26, 26, 46, 0.95) 0%, rgba(16, 16, 32, 0.95) 100%) !important;
  border-radius: var(--radius-lg);
  overflow: hidden;
  border: 1px solid var(--glass-border) !important;
}

/* ====== 电量条 ====== */
.battery-cell { display: flex; align-items: center; gap: 10px; min-width: 100px; }
.battery-track {
  flex: 1; height: 8px;
  background: rgba(255, 255, 255, 0.06);
  border-radius: 4px;
  overflow: hidden;
}
.battery-bar {
  height: 100%;
  border-radius: 4px;
  transition: width 0.6s cubic-bezier(0.16, 1, 0.3, 1);
}
.bar-green { background: linear-gradient(90deg, #22c55e, #4ade80); }
.bar-orange { background: linear-gradient(90deg, #f59e0b, #fbbf24); }
.bar-red { background: linear-gradient(90deg, #ef4444, #f87171); }
.battery-num { font-size: 12px; font-weight: 600; color: var(--text-secondary); min-width: 36px; }

.cell-id {
  font-family: 'JetBrains Mono', 'Fira Code', monospace;
  font-size: 12px; color: var(--text-tertiary);
  background: var(--glass);
  padding: 4px 10px;
  border-radius: var(--radius-sm);
}
.cell-muted { color: var(--text-tertiary); font-size: 13px; }

/* ====== 分页 ====== */
.pagination {
  margin-top: 20px;
  text-align: right;
  padding: 16px 0;
}

/* ====== 添加车辆按钮 ====== */
.btn-glow {
  background: linear-gradient(135deg, #4a9eff, #6366f1) !important;
  border: none !important;
  color: #fff !important;
  font-weight: 500;
  border-radius: 10px !important;
  padding: 10px 24px !important;
  transition: all 0.3s ease !important;
  box-shadow: 0 4px 16px rgba(74, 158, 255, 0.25);
}
.btn-glow:hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 24px rgba(74, 158, 255, 0.35) !important;
}

/* ====== 响应式 ====== */
@media (max-width: 1200px) {
  .stats-grid { grid-template-columns: repeat(2, 1fr); }
}
@media (max-width: 768px) {
  .stats-grid { grid-template-columns: 1fr; }
}
</style>
