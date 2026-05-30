<template>
  <div class="users-page">
    <div class="page-header">
      <h2>用户管理</h2>
    </div>

    <!-- 搜索表单 -->
    <el-form :model="searchForm" :inline="true" class="search-form">
      <el-form-item label="用户名">
        <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable style="width: 200px" />
      </el-form-item>
      <el-form-item label="用户状态">
        <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 130px">
          <el-option label="启用" :value="1" />
          <el-option label="禁用" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleResetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮区 -->
    <div class="action-bar">
      <el-button type="primary" @click="openAddDialog">新增用户</el-button>
      <el-button type="danger" @click="handleBatchDelete">批量删除</el-button>
    </div>

    <div class="glass-card">
      <el-table :data="pagedUsers" style="width: 100%" v-loading="loading" border stripe @selection-change="handleSelectionChange" :header-cell-style="{ background: 'transparent', color: 'var(--text-secondary)' }" :cell-style="{ background: 'transparent', color: 'var(--text-primary)' }">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="roleKey" label="角色" />
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success" size="small">启用</el-tag>
            <el-tag v-else type="danger" size="small">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="openEditDialog(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :total="total" :page-sizes="[10, 20, 50, 100]" background layout="total, sizes, prev, pager, next, jumper" class="pagination" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </div>

    <!-- 添加/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="formMode === 'add' ? '添加用户' : '编辑用户'" width="500px" :close-on-click-modal="false" @open="handleDialogOpen">
      <el-form :model="form" :rules="formRules" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" :disabled="formMode === 'edit'" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="formMode === 'add'">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saving">
          {{ formMode === 'add' ? '添加' : '保存' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted, nextTick } from 'vue'
import { getUsers, createUser, updateUser, deleteUser, login } from '../../api/index.js'
import { ElMessage, ElMessageBox } from 'element-plus'

const users = ref([])
const loading = ref(false)
const saving = ref(false)
const searchForm = reactive({ username: '', status: '' })
const currentPage = ref(1)
const pageSize = ref(10)
const selectedRows = ref([])
const formMode = ref('add')
const dialogVisible = ref(false)
const formRef = ref(null)
const form = ref({ id: null, username: '', password: '', name: '', email: '', phone: '', status: 1 })

const formRules = reactive({
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }, { min: 2, max: 20, message: '用户名长度2-20个字符', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }, { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }],
  status: [{ required: true, message: '请选择用户状态', trigger: 'change' }]
})

const filteredUsers = computed(() => {
  return users.value.filter(u => {
    const matchName = !searchForm.username || u.username.toLowerCase().includes(searchForm.username.toLowerCase())
    const matchStatus = searchForm.status === '' || u.status === Number(searchForm.status)
    return matchName && matchStatus
  })
})
const total = computed(() => filteredUsers.value.length)
const pagedUsers = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return filteredUsers.value.slice(start, start + pageSize.value)
})

watch(() => searchForm.username, () => { currentPage.value = 1 })
watch(() => searchForm.status, () => { currentPage.value = 1 })

const handleSearch = () => { currentPage.value = 1 }
const handleResetSearch = () => { searchForm.username = ''; searchForm.status = ''; currentPage.value = 1 }
const handleSizeChange = (newSize) => { pageSize.value = newSize; currentPage.value = 1 }
const handleCurrentChange = (newPage) => { currentPage.value = newPage }
const handleSelectionChange = (selection) => { selectedRows.value = selection }

const handleBatchDelete = async () => {
  if (selectedRows.value.length === 0) { ElMessage.warning('请至少选择一条数据进行删除'); return }
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 条数据吗？`, '批量删除确认', { type: 'warning' })
    for (const row of selectedRows.value) { await deleteUser(row.id) }
    ElMessage.success('批量删除成功')
    selectedRows.value = []
    fetchUsers()
  } catch (error) { if (error !== 'cancel') ElMessage.error('批量删除失败') }
}

// 确保有 token，没有就自动登录
async function ensureToken() {
  if (localStorage.getItem('token')) return
  try {
    const res = await login('admin', 'admin123')
    const d = res.data?.data
    if (d?.accessToken) {
      localStorage.setItem('token', d.accessToken)
      if (d.refreshToken) localStorage.setItem('refreshToken', d.refreshToken)
    }
  } catch (e) { console.error('自动登录失败:', e) }
}

async function fetchUsers() {
  loading.value = true
  try {
    await ensureToken()
    const res = await getUsers()
    const payload = res.data
    // payload 可能是 {code:200, data:[...]} 或者直接是 [...]
    const list = payload?.data ?? payload ?? []
    users.value = Array.isArray(list) ? list : []
  } catch (error) {
    console.error('获取用户列表失败:', error)
    ElMessage.error('获取用户列表失败: ' + (error.response?.data?.message || error.message || ''))
  } finally { loading.value = false }
}

const handleDialogOpen = () => { nextTick(() => formRef.value?.clearValidate()) }

const openAddDialog = () => {
  formMode.value = 'add'
  form.value = { id: null, username: '', password: '', name: '', email: '', phone: '', status: 1 }
  dialogVisible.value = true
}

const openEditDialog = (row) => {
  formMode.value = 'edit'
  form.value = { id: row.id, username: row.username, password: '', name: row.name || '', email: row.email || '', phone: row.phone || '', status: row.status }
  dialogVisible.value = true
}

const handleSave = async () => {
  try { await formRef.value.validate() } catch { return }
  saving.value = true
  try {
    if (formMode.value === 'add') {
      await createUser({ username: form.value.username, password: form.value.password, name: form.value.name, email: form.value.email, phone: form.value.phone, status: form.value.status })
      ElMessage.success('用户添加成功')
    } else {
      await updateUser(form.value.id, { name: form.value.name, email: form.value.email, phone: form.value.phone, status: form.value.status })
      ElMessage.success('用户更新成功')
    }
    dialogVisible.value = false
    fetchUsers()
  } catch (error) {
    ElMessage.error(error.response?.data?.message || (formMode.value === 'add' ? '添加失败' : '更新失败'))
  } finally { saving.value = false }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除用户「${row.username}」吗？`, '确认删除', { type: 'warning' })
    await deleteUser(row.id)
    ElMessage.success('用户已删除')
    fetchUsers()
  } catch (error) { if (error !== 'cancel') ElMessage.error(error.response?.data?.message || '删除失败') }
}

onMounted(() => { fetchUsers() })
</script>

<style scoped>
.users-page { max-width: 1200px; animation: fadeIn 0.4s ease; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(12px); } to { opacity: 1; transform: translateY(0); } }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 24px; }
.page-header h2 { font-size: 20px; font-weight: 600; color: var(--text-primary); }
.search-form { background: linear-gradient(135deg, rgba(26, 26, 46, 0.8) 0%, rgba(16, 16, 32, 0.8) 100%); backdrop-filter: blur(24px); border: 1px solid var(--glass-border); border-radius: var(--radius-lg); padding: 20px 24px 4px; margin-bottom: 20px; }
.action-bar { margin-bottom: 20px; }
.glass-card { background: linear-gradient(135deg, rgba(26, 26, 46, 0.95) 0%, rgba(16, 16, 32, 0.95) 100%); backdrop-filter: blur(24px); border: 1px solid var(--glass-border); border-radius: var(--radius-xl); padding: 20px; }
.glass-card :deep(.el-table) { background: transparent !important; }
.pagination { margin-top: 20px; text-align: right; padding: 16px 0 4px; }
</style>
