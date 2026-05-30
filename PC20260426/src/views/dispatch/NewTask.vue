<template>
  <div class="new-task">
    <div class="page-header">
      <el-button @click="$router.back()" text>← 返回</el-button>
      <h2>新建调度任务</h2>
    </div>
    <div class="form-card glass-card">
      <el-form :model="form" label-position="top" class="task-form">
        <el-form-item label="任务名称">
          <el-input v-model="form.name" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="目的地">
          <el-input v-model="form.destination" placeholder="请输入目的地" />
        </el-form-item>
        <el-form-item label="货物类型">
          <el-select v-model="form.cargoType" placeholder="请选择货物类型" style="width:100%">
            <el-option label="快递包裹" value="快递包裹" />
            <el-option label="餐饮外卖" value="餐饮外卖" />
            <el-option label="生活用品" value="生活用品" />
            <el-option label="医疗用品" value="医疗用品" />
            <el-option label="工业零件" value="工业零件" />
          </el-select>
        </el-form-item>
        <el-form-item label="执行时间">
          <el-date-picker v-model="form.executeTime" type="datetime" placeholder="选择执行时间" style="width:100%" />
        </el-form-item>
        <el-form-item label="分配车辆">
          <el-select v-model="form.vehicleId" placeholder="请选择车辆" style="width:100%" clearable>
            <el-option v-for="v in availableVehicles" :key="v.id" :label="`${v.name} (${v.id})`" :value="v.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">创建任务</el-button>
          <el-button @click="$router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useVehicleStore } from '../../stores/vehicle'
import { useTaskStore } from '../../stores/task'

const router = useRouter()
const vehicleStore = useVehicleStore()
const taskStore = useTaskStore()

const availableVehicles = computed(() => vehicleStore.vehicleList.filter(v => v.status === 'standby'))

const form = reactive({
  name: '',
  destination: '',
  cargoType: '',
  executeTime: '',
  vehicleId: ''
})

function handleSubmit() {
  if (!form.name || !form.destination) {
    ElMessage.warning('请填写必要信息')
    return
  }
  taskStore.addTask({
    name: form.name,
    status: 'pending',
    destination: form.destination,
    cargoType: form.cargoType,
    vehicleId: form.vehicleId || undefined,
    vehicleName: form.vehicleId ? availableVehicles.value.find(v => v.id === form.vehicleId)?.name : undefined,
    executeTime: form.executeTime ? new Date(form.executeTime).toLocaleString() : ''
  })
  ElMessage.success('任务创建成功')
  router.push('/admin/dispatch/tasks')
}
</script>

<style scoped>
.new-task { max-width: 700px; }
.page-header { display: flex; align-items: center; gap: 16px; margin-bottom: 24px; }
.page-header h2 { font-size: 20px; font-weight: 600; }
.glass-card { background: var(--glass); backdrop-filter: blur(24px); border: 1px solid var(--glass-border); border-radius: var(--border-radius); padding: 24px; }
</style>
