<template>
  <div class="dashboard-layout">
    <aside class="sidebar" :class="{ collapsed: sidebarCollapsed }">
      <div class="sidebar-header">
        <div class="brand">
          <span class="brand-icon">🦞</span>
          <span v-show="!sidebarCollapsed" class="brand-name">轻行Claw</span>
        </div>
        <button class="collapse-btn" @click="sidebarCollapsed = !sidebarCollapsed">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline v-if="sidebarCollapsed" points="9 18 15 12 9 6" />
            <polyline v-else points="15 18 9 12 15 6" />
          </svg>
        </button>
      </div>

      <!-- el-menu 侧边导航栏 -->
      <el-menu
        :default-active="activeMenu"
        mode="vertical"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        :collapse="sidebarCollapsed"
        :collapse-transition="false"
        @select="handleMenuSelect"
        class="sidebar-menu"
      >
        <el-menu-item index="/admin/dashboard">
          <span>📊</span>
          <template #title>仪表盘</template>
        </el-menu-item>

        <el-sub-menu index="fleet-group">
          <template #title>
            <span>🚗</span>
            <span>车队管理</span>
          </template>
          <el-menu-item index="/admin/fleet/vehicles">车辆管理</el-menu-item>
          <el-menu-item index="/admin/scene/list">场景管理</el-menu-item>
        </el-sub-menu>

        <el-menu-item index="/admin/dispatch/tasks">
          <span>📋</span>
          <template #title>任务调度</template>
        </el-menu-item>

        <el-menu-item index="/admin/analytics/overview">
          <span>📈</span>
          <template #title>数据分析</template>
        </el-menu-item>

        <el-menu-item index="/admin/ai/conversation">
          <span>🤖</span>
          <template #title>AI对话</template>
        </el-menu-item>

        <el-sub-menu index="faults-group">
          <template #title>
            <span>⚠️</span>
            <span>故障管理</span>
          </template>
          <el-menu-item index="/admin/faults/alerts">告警管理</el-menu-item>
          <el-menu-item index="/admin/faults/orders">工单管理</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="settings-group">
          <template #title>
            <span>⚙️</span>
            <span>系统设置</span>
          </template>
          <el-menu-item index="/admin/settings/users">用户管理</el-menu-item>
          <el-menu-item index="/admin/settings/roles">角色管理</el-menu-item>
        </el-sub-menu>

        <el-menu-item index="/admin/sync/monitor">
          <span>🔄</span>
          <template #title>数据同步</template>
        </el-menu-item>

        <el-menu-item index="/admin/content/management">
          <span>📝</span>
          <template #title>内容管理</template>
        </el-menu-item>

        <el-menu-item index="/admin/mobile/management">
          <span>📱</span>
          <template #title>移动端管理</template>
        </el-menu-item>
      </el-menu>
    </aside>

    <div class="main-area">
      <header class="top-header">
        <div class="header-left">
          <h2 class="page-title">{{ currentPageTitle }}</h2>
        </div>
        <div class="header-right">
          <div class="header-badge online">在线</div>
          <el-dropdown trigger="click">
            <div class="user-info">
              <div class="user-avatar">{{ (user?.name || 'A').charAt(0) }}</div>
              <span class="user-name">{{ user?.name || '管理员' }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>
      <main class="content-area">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '../../stores/auth'
import { useVehicleStore } from '../../stores/vehicle'
import { useTaskStore } from '../../stores/task'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()
const vehicleStore = useVehicleStore()
const taskStore = useTaskStore()
const sidebarCollapsed = ref(false)

const user = computed(() => authStore.user)

// 当前激活的菜单项，与路由路径对应
const activeMenu = computed(() => route.path)

// 菜单点击跳转
const handleMenuSelect = (index) => {
  router.push(index)
}

const pageTitles = {
  '/admin/dashboard': '仪表盘',
  '/admin/fleet/vehicles': '车辆管理',
  '/admin/scene/list': '场景管理',
  '/admin/dispatch/tasks': '任务调度',
  '/admin/analytics/overview': '数据分析',
  '/admin/ai/conversation': 'AI对话',
  '/admin/faults/alerts': '告警管理',
  '/admin/faults/orders': '工单管理',
  '/admin/settings/users': '用户管理',
  '/admin/settings/roles': '角色管理',
  '/admin/sync/monitor': '数据同步',
  '/admin/content/management': '内容管理',
  '/admin/mobile/management': '移动端管理'
}

const currentPageTitle = computed(() => {
  return pageTitles[route.path] || '管理后台'
})

function handleLogout() {
  vehicleStore.cleanupWebSocket()
  taskStore.cleanupWebSocket()
  authStore.logout()
  router.push('/login')
}

onMounted(() => {
  vehicleStore.fetchVehicles()
  vehicleStore.initWebSocket()
  taskStore.fetchTasks()
  taskStore.initWebSocket()
})

onUnmounted(() => {
  vehicleStore.cleanupWebSocket()
  taskStore.cleanupWebSocket()
})
</script>

<style scoped>
.dashboard-layout {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

.sidebar {
  width: var(--sidebar-width);
  background: linear-gradient(180deg, rgba(10, 10, 26, 0.98) 0%, rgba(16, 16, 32, 0.98) 100%);
  backdrop-filter: blur(40px);
  border-right: 1px solid var(--glass-border);
  display: flex;
  flex-direction: column;
  transition: width 0.3s cubic-bezier(0.16, 1, 0.3, 1);
  flex-shrink: 0;
  overflow: hidden;
}

.sidebar.collapsed {
  width: 64px;
}

.sidebar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  border-bottom: 1px solid var(--glass-border);
  min-height: var(--header-height);
}

.brand {
  display: flex;
  align-items: center;
  gap: 10px;
}

.brand-icon {
  font-size: 24px;
  flex-shrink: 0;
}

.brand-name {
  font-size: 18px;
  font-weight: 700;
  background: linear-gradient(135deg, #f0f0f8, #a0a0b8);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  white-space: nowrap;
}

.collapse-btn {
  width: 28px;
  height: 28px;
  border-radius: 6px;
  border: none;
  background: var(--glass);
  color: var(--text-muted);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: var(--transition);
}

.collapse-btn:hover {
  background: var(--glass-heavy);
  color: var(--text-primary);
}

.collapse-btn svg {
  width: 16px;
  height: 16px;
}

/* el-menu 样式覆盖 */
.sidebar-menu {
  flex: 1;
  overflow-y: auto;
  border-right: none;
  background: transparent !important;
}

.sidebar-menu:not(.el-menu--collapse) {
  width: 100%;
}

/* 滚动条样式 */
.sidebar-menu::-webkit-scrollbar {
  width: 4px;
}
.sidebar-menu::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 2px;
}

.main-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  min-width: 0;
}

.top-header {
  height: var(--header-height);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  background: rgba(10, 10, 26, 0.8);
  backdrop-filter: blur(40px);
  border-bottom: 1px solid var(--glass-border);
  flex-shrink: 0;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.page-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
}

.header-badge.online {
  background: rgba(74, 222, 128, 0.12);
  color: var(--green);
  border: 1px solid rgba(74, 222, 128, 0.2);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 8px;
  transition: var(--transition);
}

.user-info:hover {
  background: var(--glass);
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--blue), var(--purple));
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 700;
  color: white;
}

.user-name {
  font-size: 13px;
  font-weight: 500;
  color: var(--text-secondary);
}

.content-area {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
  background: var(--bg-primary);
}
</style>
