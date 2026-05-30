import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { vehicles as mockVehicles, type Vehicle } from '../data/mock'
import * as api from '../api/index.js'
import { connectWebSocket, disconnectWebSocket, on as wsOn, off as wsOff } from '../utils/websocket.js'

export const useVehicleStore = defineStore('vehicle', () => {
  const vehicleList = ref<Vehicle[]>([...mockVehicles])
  const filterStatus = ref<string>('all')
  const searchQuery = ref('')
  const isLoading = ref(false)
  const wsConnected = ref(false)

  const filteredVehicles = computed(() => {
    let result = vehicleList.value
    if (filterStatus.value !== 'all') {
      result = result.filter(v => v.status === filterStatus.value)
    }
    if (searchQuery.value) {
      const query = searchQuery.value.toLowerCase()
      result = result.filter(v =>
        v.name.toLowerCase().includes(query) ||
        v.id.toLowerCase().includes(query) ||
        v.location.toLowerCase().includes(query)
      )
    }
    return result
  })

  const statusCounts = computed(() => {
    const counts = { standby: 0, delivery: 0, charging: 0, fault: 0 }
    vehicleList.value.forEach(v => {
      counts[v.status]++
    })
    return counts
  })

  const getVehicleById = (id: string) => {
    return vehicleList.value.find(v => v.id === id)
  }

  async function fetchVehicles() {
    isLoading.value = true
    try {
      const res = await api.getAllVehicles()
      const list = res.data?.data || res.data || []
      if (Array.isArray(list) && list.length > 0) {
        vehicleList.value = list.map((v: any) => ({
          id: v.id,
          name: v.name || v.id,
          model: v.model || 'DM-A1',
          status: mapBackendStatus(v.status),
          battery: v.battery ? Math.round(Number(v.battery)) : 0,
          mileage: v.mileage ? Math.round(Number(v.mileage)) : 0,
          location: v.location || '',
          lastUpdate: v.lastUpdate || v.updatedAt || new Date().toISOString().slice(0, 16).replace('T', ' '),
          driver: '自动'
        }))
        console.log(`[VehicleStore] Loaded ${vehicleList.value.length} vehicles from backend`)
      }
    } catch (e: any) {
      console.warn('[VehicleStore] fetchVehicles failed, using mock data:', e.message)
    } finally {
      isLoading.value = false
    }
  }

  function mapBackendStatus(status: string): Vehicle['status'] {
    const map: Record<string, Vehicle['status']> = {
      standby: 'standby',
      delivering: 'delivery',
      delivery: 'delivery',
      charging: 'charging',
      fault: 'fault',
      idle: 'standby',
      busy: 'delivery'
    }
    return map[status] || 'standby'
  }

  function mapFrontendStatus(status: Vehicle['status']): string {
    const map: Record<string, string> = {
      standby: 'standby',
      delivery: 'delivering',
      charging: 'charging',
      fault: 'fault'
    }
    return map[status] || status
  }

  const updateVehicleStatus = async (id: string, status: Vehicle['status']) => {
    const vehicle = vehicleList.value.find(v => v.id === id)
    if (vehicle) {
      vehicle.status = status
      vehicle.lastUpdate = new Date().toISOString().slice(0, 16).replace('T', ' ')
    }
    try {
      await api.updateVehicleStatus(id, mapFrontendStatus(status))
    } catch (e: any) {
      console.warn('[VehicleStore] updateVehicleStatus API failed:', e.message)
    }
  }

  async function addVehicle(data: any) {
    const res = await api.createVehicle(data)
    await fetchVehicles()
    return res.data
  }

  async function updateVehicle(id: string, data: any) {
    const res = await api.updateVehicle(id, data)
    await fetchVehicles()
    return res.data
  }

  async function removeVehicle(id: string) {
    const res = await api.deleteVehicle(id)
    await fetchVehicles()
    return res.data
  }

  function handleWsVehicleUpdate(data: any) {
    if (!data || !data.vehicleId) return
    const v = vehicleList.value.find(x => x.id === data.vehicleId)
    if (v) {
      const newStatus = mapBackendStatus(data.status)
      if (v.status !== newStatus) {
        v.status = newStatus
        v.lastUpdate = new Date().toISOString().slice(0, 16).replace('T', ' ')
        console.log(`[VehicleStore] WS update: ${v.id} -> ${newStatus}`)
      }
    } else {
      fetchVehicles()
    }
  }

  function initWebSocket() {
    connectWebSocket(
      () => { wsConnected.value = true },
      () => { wsConnected.value = false }
    )
    wsOn('vehicle-update', handleWsVehicleUpdate)
  }

  function cleanupWebSocket() {
    wsOff('vehicle-update', handleWsVehicleUpdate)
    disconnectWebSocket()
    wsConnected.value = false
  }

  return {
    vehicleList,
    filterStatus,
    searchQuery,
    filteredVehicles,
    statusCounts,
    isLoading,
    wsConnected,
    getVehicleById,
    updateVehicleStatus,
    fetchVehicles,
    addVehicle,
    updateVehicle,
    removeVehicle,
    initWebSocket,
    cleanupWebSocket
  }
})
