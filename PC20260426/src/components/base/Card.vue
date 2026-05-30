<template>
  <div :class="cardClasses">
    <div v-if="$slots.header || title" class="card-header">
      <div class="card-header-content">
        <h3 v-if="title" class="card-title">{{ title }}</h3>
        <p v-if="subtitle" class="card-subtitle">{{ subtitle }}</p>
        <slot name="header" />
      </div>
      <div v-if="$slots.actions" class="card-actions">
        <slot name="actions" />
      </div>
    </div>
    
    <div class="card-body" :class="{ 'no-padding': noPadding }">
      <slot />
    </div>
    
    <div v-if="$slots.footer" class="card-footer">
      <slot name="footer" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  title?: string
  subtitle?: string
  variant?: 'default' | 'glass' | 'outlined' | 'elevated'
  noPadding?: boolean
  hoverable?: boolean
  clickable?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  variant: 'default',
  noPadding: false,
  hoverable: false,
  clickable: false
})

const cardClasses = computed(() => [
  'card',
  `card-${props.variant}`,
  {
    'card-hoverable': props.hoverable,
    'card-clickable': props.clickable
  }
])
</script>

<style scoped>
.card {
  background: var(--bg-surface);
  border-radius: var(--radius-xl);
  overflow: hidden;
  transition: all var(--duration-200) var(--ease-out);
}

/* 变体 */
.card-default {
  border: 1px solid var(--border-color);
  box-shadow: var(--shadow-sm);
}

.card-glass {
  background: var(--glass-medium);
  border: 1px solid var(--glass-border);
  backdrop-filter: blur(12px);
}

.card-outlined {
  background: transparent;
  border: 1px solid var(--border-color-strong);
}

.card-elevated {
  border: none;
  box-shadow: var(--shadow-lg);
}

/* 状态 */
.card-hoverable:hover {
  border-color: var(--border-color-strong);
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
}

.card-clickable {
  cursor: pointer;
}

.card-clickable:active {
  transform: scale(0.99);
}

/* 头部 */
.card-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: var(--space-4);
  padding: var(--space-5) var(--space-6);
  border-bottom: 1px solid var(--border-color);
}

.card-header-content {
  flex: 1;
  min-width: 0;
}

.card-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin: 0;
  line-height: var(--leading-tight);
}

.card-subtitle {
  font-size: var(--text-sm);
  color: var(--text-tertiary);
  margin-top: var(--space-1);
  margin-bottom: 0;
}

.card-actions {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  flex-shrink: 0;
}

/* 内容 */
.card-body {
  padding: var(--space-6);
}

.card-body.no-padding {
  padding: 0;
}

/* 底部 */
.card-footer {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: var(--space-3);
  padding: var(--space-4) var(--space-6);
  border-top: 1px solid var(--border-color);
  background: var(--glass-light);
}
</style>
