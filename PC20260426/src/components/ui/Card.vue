<template>
  <div class="card" :class="[variant]">
    <div v-if="title" class="card-header">
      <h3>{{ title }}</h3>
      <slot name="header-actions" />
    </div>
    <div class="card-body">
      <slot />
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  title?: string
  variant?: string
}>()
</script>

<style scoped>
.card {
  background: var(--glass);
  backdrop-filter: blur(24px);
  border: 1px solid var(--glass-border);
  border-radius: var(--border-radius);
  padding: 20px;
}
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}
.card-header h3 {
  font-size: 15px;
  font-weight: 600;
}
</style>
