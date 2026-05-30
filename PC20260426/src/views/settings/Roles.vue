<template>
  <div class="roles-page">
    <div class="page-header">
      <h2>角色管理</h2>
      <el-button type="primary" size="small" @click="openAddDialog">+ 添加角色</el-button>
    </div>

    <!-- 搜索栏 -->
    <div class="toolbar glass-card">
      <el-input v-model="keyword" placeholder="搜索角色标识/名称/描述" clearable style="width: 300px" @clear="fetchRoles" @keyup.enter="fetchRoles">
        <template #prefix><el-icon><Search /></el-icon></template>
      </el-input>
      <el-button @click="fetchRoles">搜索</el-button>
    </div>

    <!-- 角色列表 -->
    <div class="glass-card">
      <el-table :data="roles" style="width: 100%" v-loading="loading" :header-cell-style="{ background: 'transparent', color: 'var(--text-secondary)' }" :cell-style="{ background: 'transparent', color: 'var(--text-primary)' }">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="roleKey" label="角色标识" width="150">
          <template #default="{ row }">
            <el-tag size="small" type="info">{{ row.roleKey }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="roleName" label="角色名称" width="150" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <span class="status-dot" :class="row.status === 1 ? 'active' : 'disabled'"></span>
            {{ row.status === 1 ? '启用' : '禁用' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button text size="small" type="primary" @click="openEditDialog(row)">编辑</el-button>
            <el-button text size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-wrapper" v-if="total > pageSize">
        <el-pagination background layout="prev, pager, next" :total="total" :page-size="pageSize" v-model:current-page="currentPage" @current-change="fetchRoles" />
      </div>
    </div>

    <!-- 添加/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="formMode === 'add' ? '添加角色' : '编辑角色'" width="500px">
      <el-form :model="form" label-width="80px" ref="formRef" :rules="formRules">
        <el-form-item label="角色标识" prop="roleKey">
          <el-input v-model="form.roleKey" :disabled="formMode === 'edit'" placeholder="如 admin、operator" />
        </el-form-item>
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="如 管理员、操作员" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="角色描述" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
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
import { ref, onMounted } from 'vue'
import { getRoles, createRole, updateRole, deleteRole, login } from '../../api/index.js'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'

const roles = ref([])
const loading = ref(false)
const saving = ref(false)
const keyword = ref('')
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(20)
const formMode = ref('add')
const dialogVisible = ref(false)
const formRef = ref(null)
const form = ref({ id: null, roleKey: '', roleName: '', description: '', status: 1 })

const formRules = {
  roleKey: [{ required: true, message: '请输入角色标识', trigger: 'blur' }],
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }]
}

// 确保有 token
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

async function fetchRoles() {
  loading.value = true
  try {
    await ensureToken()
    const res = await getRoles({ page: currentPage.value, size: pageSize.value, keyword: keyword.value || undefined })
    const payload = res.data
    const data = payload?.data ?? payload ?? {}
    roles.value = data.list || data.records || []
    total.value = data.total || 0
  } catch (error) {
    console.error('获取角色列表失败:', error)
    ElMessage.error('获取角色列表失败: ' + (error.response?.data?.message || error.message || ''))
  } finally { loading.value = false }
}

const openAddDialog = () => {
  formMode.value = 'add'
  form.value = { id: null, roleKey: '', roleName: '', description: '', status: 1 }
  dialogVisible.value = true
}

const openEditDialog = (row) => {
  formMode.value = 'edit'
  form.value = { id: row.id, roleKey: row.roleKey, roleName: row.roleName || '', description: row.description || '', status: row.status }
  dialogVisible.value = true
}

const handleSave = async () => {
  if (formRef.value) { try { await formRef.value.validate() } catch { return } }
  saving.value = true
  try {
    if (formMode.value === 'add') {
      await createRole({ roleKey: form.value.roleKey, roleName: form.value.roleName, description: form.value.description, status: form.value.status })
      ElMessage.success('角色添加成功')
    } else {
      await updateRole(form.value.id, { roleName: form.value.roleName, description: form.value.description, status: form.value.status })
      ElMessage.success('角色更新成功')
    }
    dialogVisible.value = false
    fetchRoles()
  } catch (error) {
    ElMessage.error(error.response?.data?.message || (formMode.value === 'add' ? '添加失败' : '更新失败'))
  } finally { saving.value = false }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除角色「${row.roleName}」吗？`, '确认删除', { type: 'warning' })
    await deleteRole(row.id)
    ElMessage.success('角色已删除')
    fetchRoles()
  } catch (error) { if (error !== 'cancel') ElMessage.error(error.response?.data?.message || '删除失败') }
}

onMounted(() => { fetchRoles() })
</script>

<style scoped>
.roles-page { max-width: 1200px; }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 24px; }
.page-header h2 { font-size: 20px; font-weight: 600; }
.toolbar { display: flex; align-items: center; gap: 12px; margin-bottom: 16px; padding: 16px; }
.glass-card { background: var(--glass); backdrop-filter: blur(24px); border: 1px solid var(--glass-border); border-radius: var(--border-radius); padding: 20px; }
.status-dot { display: inline-block; width: 8px; height: 8px; border-radius: 50%; margin-right: 6px; }
.status-dot.active { background: var(--green); }
.status-dot.disabled { background: var(--red); }
.pagination-wrapper { display: flex; justify-content: center; margin-top: 16px; }
</style>
