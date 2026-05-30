import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { login } from '../api/index.js'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import DashboardLayout from '../components/layout/DashboardLayout.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { public: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    meta: { public: true }
  },
  {
    path: '/admin',
    component: DashboardLayout,
    meta: { requiresAuth: true },
    redirect: '/admin/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/DashboardEnhanced.vue')
      },
      {
        path: 'fleet',
        redirect: '/admin/fleet/vehicles'
      },
      {
        path: 'fleet/vehicles',
        name: 'VehicleList',
        component: () => import('../views/fleet/VehicleList.vue')
      },
      {
        path: 'fleet/vehicles/:id',
        name: 'VehicleDetail',
        component: () => import('../views/fleet/VehicleDetail.vue')
      },
      {
        path: 'scene',
        redirect: '/admin/scene/list'
      },
      {
        path: 'scene/list',
        name: 'SceneList',
        component: () => import('../views/scene/SceneList.vue')
      },
      {
        path: 'dispatch',
        redirect: '/admin/dispatch/tasks'
      },
      {
        path: 'dispatch/tasks',
        name: 'TaskList',
        component: () => import('../views/dispatch/TaskList.vue')
      },
      {
        path: 'dispatch/new',
        name: 'NewTask',
        component: () => import('../views/dispatch/NewTask.vue')
      },
      {
        path: 'analytics',
        redirect: '/admin/analytics/overview'
      },
      {
        path: 'analytics/overview',
        name: 'AnalyticsOverview',
        component: () => import('../views/analytics/Overview.vue')
      },
      {
        path: 'ai',
        redirect: '/admin/ai/conversation'
      },
      {
        path: 'ai/conversation',
        name: 'AIConversation',
        component: () => import('../views/ai/Conversation.vue')
      },
      {
        path: 'faults',
        redirect: '/admin/faults/alerts'
      },
      {
        path: 'faults/alerts',
        name: 'FaultAlerts',
        component: () => import('../views/faults/Alerts.vue')
      },
      {
        path: 'faults/orders',
        name: 'FaultOrders',
        component: () => import('../views/faults/Orders.vue')
      },
      {
        path: 'settings',
        redirect: '/admin/settings/users'
      },
      {
        path: 'settings/users',
        name: 'SettingsUsers',
        component: () => import('../views/settings/Users.vue')
      },
      {
        path: 'settings/roles',
        name: 'SettingsRoles',
        component: () => import('../views/settings/Roles.vue')
      },
      {
        path: 'sync',
        redirect: '/admin/sync/monitor'
      },
      {
        path: 'sync/monitor',
        name: 'SyncMonitor',
        component: () => import('../views/sync/SyncMonitor.vue')
      },
      {
        path: 'content',
        redirect: '/admin/content/management'
      },
      {
        path: 'content/management',
        name: 'ContentManagement',
        component: () => import('../views/content/ContentManagement.vue')
      },
      {
        path: 'mobile',
        redirect: '/admin/mobile/management'
      },
      {
        path: 'mobile/management',
        name: 'MobileManagement',
        component: () => import('../views/mobile/MobileManagement.vue')
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    redirect: '/login'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach(async (to, from) => {
  const authStore = useAuthStore()

  // 开发模式：检测假 token 并清除，强制重新登录
  if (import.meta.env.DEV && authStore.isLoggedIn) {
    const token = authStore.token
    // JWT 格式应包含两个 . (header.payload.signature)
    if (!token || token.split('.').length !== 3) {
      authStore.logout()
    }
  }

  if (to.meta.requiresAuth && !authStore.isLoggedIn) {
    if (import.meta.env.DEV) {
      try {
        const res = await login('admin', 'admin123')
        const result = res.data
        const loginData = result?.data
        if (loginData) {
          const token = loginData.accessToken || loginData.token
          if (token) {
            authStore.setAuth(token, {
              username: loginData.user?.username || 'admin',
              name: loginData.user?.name || '管理员',
              role: loginData.user?.roleKey || 'admin'
            })
            if (loginData.refreshToken) {
              localStorage.setItem('refreshToken', loginData.refreshToken)
            }
            return true
          }
        }
      } catch (e) {
        console.warn('开发模式自动登录失败:', e.message)
      }
      return '/login'
    }
    return '/login'
  } else if (to.meta.public && authStore.isLoggedIn) {
    return '/admin'
  }
})

export default router
