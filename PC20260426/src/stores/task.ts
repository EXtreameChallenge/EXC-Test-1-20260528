import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { tasks as mockTasks, type Task } from '../data/mock'
import * as api from '../api/index.js'
import { on as wsOn, off as wsOff } from '../utils/websocket.js'

export const useTaskStore = defineStore('task', () => {
  const taskList = ref<Task[]>([...mockTasks])
  const filterStatus = ref<string>('all')
  const isLoading = ref(false)

  const filteredTasks = computed(() => {
    let result = taskList.value
    if (filterStatus.value !== 'all') {
      result = result.filter(t => t.status === filterStatus.value)
    }
    return result
  })

  const statusCounts = computed(() => {
    const counts = { pending: 0, confirmed: 0, executing: 0, completed: 0, cancelled: 0 }
    taskList.value.forEach(t => {
      counts[t.status]++
    })
    return counts
  })

  async function fetchTasks() {
    isLoading.value = true
    try {
      const res = await api.getTasks({ page: 1, size: 50 })
      const list = res.data?.data?.list || res.data?.data?.records || res.data?.records || []
      if (Array.isArray(list) && list.length > 0) {
        taskList.value = list.map((t: any) => ({
          id: t.id || String(t.taskId),
          name: t.title || t.name || `任务-${t.id}`,
          status: mapBackendTaskStatus(t.status),
          destination: t.destination || t.targetLocation || '',
          cargoType: t.cargoType || t.cargo || '通用',
          vehicleId: t.vehicleId || undefined,
          vehicleName: t.vehicleName || t.vehicleId || undefined,
          executeTime: t.executeTime || t.scheduledTime || '',
          createdAt: t.createdAt ? new Date(t.createdAt).toISOString().slice(0, 16).replace('T', ' ') : ''
        }))
        console.log(`[TaskStore] Loaded ${taskList.value.length} tasks from backend`)
      }
    } catch (e: any) {
      console.warn('[TaskStore] fetchTasks failed, using mock data:', e.message)
    } finally {
      isLoading.value = false
    }
  }

  function mapBackendTaskStatus(status: string): Task['status'] {
    const map: Record<string, Task['status']> = {
      pending: 'pending',
      confirmed: 'confirmed',
      executing: 'executing',
      in_progress: 'executing',
      completed: 'completed',
      done: 'completed',
      cancelled: 'cancelled',
      canceled: 'cancelled'
    }
    return map[status] || 'pending'
  }

  function mapFrontendTaskStatus(status: Task['status']): string {
    const map: Record<string, string> = {
      pending: 'pending',
      confirmed: 'confirmed',
      executing: 'executing',
      completed: 'completed',
      cancelled: 'cancelled'
    }
    return map[status] || status
  }

  const addTask = async (task: Omit<Task, 'id' | 'createdAt'>) => {
    const newTask: Task = {
      ...task,
      id: `T${String(taskList.value.length + 1).padStart(3, '0')}`,
      createdAt: new Date().toISOString().slice(0, 16).replace('T', ' ')
    }
    taskList.value.unshift(newTask)
    try {
      await api.createTask({
        title: newTask.name,
        destination: newTask.destination,
        cargoType: newTask.cargoType,
        vehicleId: newTask.vehicleId,
        status: mapFrontendTaskStatus(newTask.status)
      })
    } catch (e: any) {
      console.warn('[TaskStore] addTask API failed:', e.message)
    }
  }

  const updateTaskStatus = async (id: string, status: Task['status']) => {
    const task = taskList.value.find(t => t.id === id)
    if (task) {
      task.status = status
    }
    try {
      await api.updateTaskStatus(id, mapFrontendTaskStatus(status))
    } catch (e: any) {
      console.warn('[TaskStore] updateTaskStatus API failed:', e.message)
    }
  }

  function handleWsTaskUpdate(data: any) {
    if (!data) return
    fetchTasks()
  }

  function initWebSocket() {
    wsOn('task-update', handleWsTaskUpdate)
  }

  function cleanupWebSocket() {
    wsOff('task-update', handleWsTaskUpdate)
  }

  return {
    taskList,
    filterStatus,
    filteredTasks,
    statusCounts,
    isLoading,
    addTask,
    updateTaskStatus,
    fetchTasks,
    initWebSocket,
    cleanupWebSocket
  }
})
