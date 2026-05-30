import axios from 'axios'

const api = axios.create({
  baseURL: '/api/v1',
  timeout: 15000,
  headers: { 'Content-Type': 'application/json' }
})

api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

api.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      if (!window.location.pathname.includes('/login')) {
        window.location.href = '/login'
      }
    }
    return Promise.reject(error)
  }
)

export function login(username, password) {
  return api.post('/auth/login', { username, password })
}

export function register(username, password, name, phone, role) {
  return api.post('/auth/register', { username, password, name: name || username, phone: phone || '', roleKey: role || 'operator' })
}

export function refreshToken(refreshToken) {
  return api.post('/auth/refresh', { refreshToken })
}

export function getVehicles(params) {
  return api.get('/vehicles', { params })
}

export function getAllVehicles() {
  return api.get('/vehicles/all')
}

export function getVehicle(id) {
  return api.get(`/vehicles/${id}`)
}

export function createVehicle(data) {
  return api.post('/vehicles', data)
}

export function updateVehicle(id, data) {
  return api.put(`/vehicles/${id}`, data)
}

export function updateVehicleStatus(id, status) {
  return api.put(`/vehicles/${id}/status`, { status })
}

export function deleteVehicle(id) {
  return api.delete(`/vehicles/${id}`)
}

export function getTasks(params) {
  return api.get('/tasks', { params })
}

export function getTask(id) {
  return api.get(`/tasks/${id}`)
}

export function createTask(data) {
  return api.post('/tasks', data)
}

export function updateTaskStatus(id, status) {
  return api.put(`/tasks/${id}/status`, { status })
}

export function assignVehicleToTask(taskId, vehicleId) {
  return api.post(`/tasks/${taskId}/assign`, { vehicleId })
}

export function deleteTask(id) {
  return api.delete(`/tasks/${id}`)
}

export function getAlerts(params) {
  return api.get('/alerts', { params })
}

export function confirmAlert(id) {
  return api.put(`/alerts/${id}/confirm`)
}

export function resolveAlert(id) {
  return api.put(`/alerts/${id}/resolve`)
}

export function readAllAlerts() {
  return api.put('/alerts/read-all')
}

export function getWorkOrders(params) {
  return api.get('/work-orders', { params })
}

export function createWorkOrder(data) {
  return api.post('/work-orders', data)
}

export function startWorkOrder(id) {
  return api.put(`/work-orders/${id}/start`)
}

export function completeWorkOrder(id) {
  return api.put(`/work-orders/${id}/complete`)
}

export function getDashboardAnalytics() {
  return api.get('/analytics/dashboard')
}

export function sendAIMessage(message, conversationId) {
  return api.post('/ai/chat', { message, conversationId })
}

export function getConversations(params) {
  return api.get('/ai/conversations', { params })
}

export function getConversationMessages(conversationId) {
  return api.get(`/ai/conversations/${conversationId}/messages`)
}

export function deleteConversation(conversationId) {
  return api.delete(`/ai/conversations/${conversationId}`)
}

export function getChargingStations() {
  return api.get('/energy/charging-stations')
}

export function getEnergyOptimize() {
  return api.get('/energy/optimize')
}

export function getGeofences() {
  return api.get('/geofences')
}

export function getGeofenceViolations() {
  return api.get('/geofences/violations')
}

export function getVehicleRealtime() {
  return api.get('/twin/vehicles/realtime')
}

export function getVehicleTrack(vehicleId, params) {
  return api.get(`/twin/vehicles/${vehicleId}/track`, { params })
}

export function getMaintenanceOverview() {
  return api.get('/maintenance/prediction/overview')
}

export function getMaintenancePredictions(params) {
  return api.get('/maintenance/prediction', { params })
}

export function getCollabEvents(params) {
  return api.get('/collab/events', { params })
}

export function getUnreadCollabCount() {
  return api.get('/collab/events/unread-count')
}

export function sendCollabEvent(data) {
  return api.post('/collab/events', data)
}

export function readCollabEvent(id) {
  return api.put(`/collab/events/${id}/read`)
}

export function readAllCollabEvents() {
  return api.put('/collab/events/read-all')
}

export function optimizeDispatch(data) {
  return api.post('/dispatch/plan/optimize', data)
}

export function executeDispatchPlan(data) {
  return api.post('/dispatch/plan/execute', data)
}

export function getCompanyInfo() {
  return api.get('/company-info')
}

export function updateCompanyInfo(data) {
  return api.put('/company-info', data)
}

export function getUsers() {
  return api.get('/users')
}

export function getUser(id) {
  return api.get(`/users/${id}`)
}

export function createUser(data) {
  return api.post('/users', data)
}

export function updateUser(id, data) {
  return api.put(`/users/${id}`, data)
}

export function deleteUser(id) {
  return api.delete(`/users/${id}`)
}

export function getRoles(params) {
  return api.get('/roles', { params })
}

export function getRole(id) {
  return api.get(`/roles/${id}`)
}

export function createRole(data) {
  return api.post('/roles', data)
}

export function updateRole(id, data) {
  return api.put(`/roles/${id}`, data)
}

export function deleteRole(id) {
  return api.delete(`/roles/${id}`)
}

export default api
