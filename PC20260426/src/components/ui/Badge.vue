<template>
  <div class="badge" :class="[type, size]">
    <span v-if="dot" class="badge-dot"></span>
    <slot />
  </div>
</template>

<script setup lang="ts">
defineProps<{
  type?: string
  size?: string
  dot?: boolean
}>()
</script>

<style scoped>
.badge {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 2px 8px;
  border-radius: 6px;
  font-size: 11px;
  font-weight: 600;
}
.badge-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: currentColor;
}
.badge.success { background: rgba(74,222,128,0.12); color: var(--green); }
.badge.warning { background: rgba(251,191,36,0.12); color: var(--orange); }
.badge.danger { background: rgba(248,113,113,0.12); color: var(--red); }
.badge.info { background: rgba(74,158,255,0.12); color: var(--blue); }
.badge.primary { background: rgba(99,102,241,0.12); color: var(--purple); }
.badge.small { padding: 1px 6px; font-size: 10px; }
</style>
