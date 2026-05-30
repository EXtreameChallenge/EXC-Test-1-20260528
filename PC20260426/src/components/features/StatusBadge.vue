<template>
  <span class="status-badge" :class="[status]">
    <span class="status-dot"></span>
    <slot>{{ label }}</slot>
  </span>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  status: string
  type?: 'vehicle' | 'task' | 'alert'
}>()

const statusLabels: Record<string, string> = {
  standby: '待命',
  delivery: '配送中',
  charging: '充电中',
  fault: '故障',
  online: '在线',
  offline: '离线',
  active: '启用',
  disabled: '禁用',
  pending: '待确认',
  confirmed: '已确认',
  executing: '执行中',
  completed: '已完成',
  cancelled: '已取消'
}

const label = computed(() => statusLabels[props.status] || props.status)
</script>

<style scoped>
.status-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 3px 10px;
  border-radius: 8px;
  font-size: 12px;
  font-weight: 600;
}
.status-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: currentColor;
}
.status-badge.standby, .status-badge.online, .status-badge.active, .status-badge.completed {
  background: rgba(74,222,128,0.12);
  color: var(--green);
}
.status-badge.delivery, .status-badge.executing, .status-badge.confirmed {
  background: rgba(74,158,255,0.12);
  color: var(--blue);
}
.status-badge.charging, .status-badge.pending {
  background: rgba(251,191,36,0.12);
  color: var(--orange);
}
.status-badge.fault, .status-badge.offline, .status-badge.disabled, .status-badge.cancelled {
  background: rgba(248,113,113,0.12);
  color: var(--red);
}
</style>
