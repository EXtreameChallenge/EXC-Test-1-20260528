import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { refreshToken } from '../api'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string>(localStorage.getItem('token') || '')
  const user = ref<{ username: string; name: string; role: string } | null>(
    (() => { try { const u = localStorage.getItem('user'); return u ? JSON.parse(u) : null } catch { return null } })()
  )

  const isLoggedIn = computed(() => !!token.value)

  const setAuth = (authToken: string, userInfo: { username: string; name: string; role: string }) => {
    token.value = authToken
    user.value = userInfo
    localStorage.setItem('token', authToken)
    localStorage.setItem('user', JSON.stringify(userInfo))
  }

  const logout = () => {
    token.value = ''
    user.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    localStorage.removeItem('refreshToken')
  }

  const tryRefreshToken = async () => {
    const rt = localStorage.getItem('refreshToken')
    if (!rt) return false
    try {
      const res = await refreshToken(rt)
      const result = res.data
      if (result.code === 200 || result.success) {
        const data = result.data || {}
        token.value = data.accessToken || data.token || ''
        if (data.refreshToken) {
          localStorage.setItem('refreshToken', data.refreshToken)
        }
        localStorage.setItem('token', token.value)
        return true
      }
    } catch (e) {
      console.error('Token refresh failed:', e)
    }
    return false
  }

  return {
    token,
    user,
    isLoggedIn,
    setAuth,
    logout,
    tryRefreshToken
  }
})
