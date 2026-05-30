import SockJS from 'sockjs-client'
import Stomp from 'stompjs'

let stompClient = null
let reconnectTimer = null
let isConnected = false
const listeners = {}

export function connectWebSocket(onConnected, onError) {
  if (stompClient && isConnected) {
    if (onConnected) onConnected()
    return
  }

  try {
    const socket = new SockJS('/ws')
    stompClient = Stomp.over(socket)
    stompClient.debug = null

    const token = localStorage.getItem('token')
    const headers = token ? { Authorization: `Bearer ${token}` } : {}

    stompClient.connect(headers, (frame) => {
      isConnected = true
      console.log('[WS] WebSocket connected')

      stompClient.subscribe('/topic/vehicle', (message) => {
        try {
          const data = JSON.parse(message.body)
          emit('vehicle-update', data)
        } catch (e) {
          console.error('[WS] Parse vehicle update error:', e)
        }
      })

      stompClient.subscribe('/topic/alert', (message) => {
        try {
          const data = JSON.parse(message.body)
          emit('alert-update', data)
        } catch (e) {
          console.error('[WS] Parse alert update error:', e)
        }
      })

      stompClient.subscribe('/topic/task', (message) => {
        try {
          const data = JSON.parse(message.body)
          emit('task-update', data)
        } catch (e) {
          console.error('[WS] Parse task update error:', e)
        }
      })

      const token = localStorage.getItem('token')
      if (token) {
        stompClient.subscribe('/user/topic/vehicle', (message) => {
          try {
            const data = JSON.parse(message.body)
            emit('vehicle-update', data)
          } catch (e) {}
        })
        stompClient.subscribe('/user/topic/alert', (message) => {
          try {
            const data = JSON.parse(message.body)
            emit('alert-update', data)
          } catch (e) {}
        })
        stompClient.subscribe('/user/topic/collab', (message) => {
          try {
            const data = JSON.parse(message.body)
            emit('collab-update', data)
          } catch (e) {}
        })
      }

      if (onConnected) onConnected()
    }, (error) => {
      isConnected = false
      console.error('[WS] Connection error:', error)
      if (onError) onError(error)
      scheduleReconnect(onConnected, onError)
    })
  } catch (e) {
    console.error('[WS] Failed to create WebSocket:', e)
    if (onError) onError(e)
    scheduleReconnect(onConnected, onError)
  }
}

function scheduleReconnect(onConnected, onError) {
  if (reconnectTimer) clearTimeout(reconnectTimer)
  reconnectTimer = setTimeout(() => {
    console.log('[WS] Attempting reconnect...')
    connectWebSocket(onConnected, onError)
  }, 5000)
}

export function disconnectWebSocket() {
  if (reconnectTimer) {
    clearTimeout(reconnectTimer)
    reconnectTimer = null
  }
  if (stompClient) {
    try {
      stompClient.disconnect()
    } catch (e) {}
    stompClient = null
  }
  isConnected = false
  Object.keys(listeners).forEach(key => { listeners[key] = [] })
}

export function on(event, callback) {
  if (!listeners[event]) listeners[event] = []
  listeners[event].push(callback)
}

export function off(event, callback) {
  if (!listeners[event]) return
  if (callback) {
    listeners[event] = listeners[event].filter(cb => cb !== callback)
  } else {
    listeners[event] = []
  }
}

function emit(event, data) {
  if (!listeners[event]) return
  listeners[event].forEach(cb => {
    try {
      cb(data)
    } catch (e) {
      console.error(`[WS] Listener error for ${event}:`, e)
    }
  })
}

export function getWsStatus() {
  return isConnected
}
