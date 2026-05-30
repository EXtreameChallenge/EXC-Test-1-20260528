<template>
  <div class="page-header">
    <div class="header-left">
      <h2>{{ title }}</h2>
      <span v-if="subtitle" class="subtitle">{{ subtitle }}</span>
    </div>
    <div class="header-right">
      <slot name="actions" />
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  title: string
  subtitle?: string
}>()
</script>

<style scoped>
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24px;
}
.header-left {
  display: flex;
  align-items: baseline;
  gap: 12px;
}
.header-left h2 {
  font-size: 20px;
  font-weight: 600;
}
.subtitle {
  font-size: 13px;
  color: var(--text-muted);
}
.header-right {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>
