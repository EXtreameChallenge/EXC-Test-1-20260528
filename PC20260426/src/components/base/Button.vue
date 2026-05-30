<template>
  <button
    :class="buttonClasses"
    :disabled="disabled || loading"
    :type="nativeType"
    @click="handleClick"
  >
    <span v-if="loading" class="btn-loading">
      <svg class="animate-spin" viewBox="0 0 24 24" fill="none">
        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
        <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"/>
      </svg>
    </span>
    <span v-if="icon && !loading" class="btn-icon">
      <component :is="icon" />
    </span>
    <span v-if="$slots.default" class="btn-content">
      <slot />
    </span>
  </button>
</template>

<script setup lang="ts">
import { computed, useSlots } from 'vue'

interface Props {
  type?: 'primary' | 'secondary' | 'success' | 'warning' | 'danger' | 'ghost' | 'link'
  size?: 'sm' | 'md' | 'lg'
  disabled?: boolean
  loading?: boolean
  block?: boolean
  icon?: any
  nativeType?: 'button' | 'submit' | 'reset'
}

const props = withDefaults(defineProps<Props>(), {
  type: 'primary',
  size: 'md',
  disabled: false,
  loading: false,
  block: false,
  nativeType: 'button'
})

const slots = useSlots()

const emit = defineEmits<{
  click: [event: MouseEvent]
}>()

const buttonClasses = computed(() => {
  const classes = [
    'btn',
    `btn-${props.type}`,
    `btn-${props.size}`,
    {
      'btn-disabled': props.disabled,
      'btn-loading-state': props.loading,
      'btn-block': props.block,
      'btn-icon-only': props.icon && !slots.default
    }
  ]
  return classes.filter(Boolean)
})

function handleClick(event: MouseEvent) {
  if (!props.disabled && !props.loading) {
    emit('click', event)
  }
}
</script>

<style scoped>
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-2);
  font-family: var(--font-sans);
  font-weight: var(--font-medium);
  line-height: 1;
  border-radius: var(--radius-lg);
  transition: all var(--duration-200) var(--ease-out);
  cursor: pointer;
  position: relative;
  overflow: hidden;
  white-space: nowrap;
  user-select: none;
}

.btn::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, rgba(255,255,255,0.1), transparent);
  opacity: 0;
  transition: opacity var(--duration-200) var(--ease-out);
}

.btn:hover::before {
  opacity: 1;
}

.btn:active {
  transform: scale(0.98);
}

/* 尺寸 */
.btn-sm {
  height: 32px;
  padding: 0 var(--space-3);
  font-size: var(--text-sm);
  border-radius: var(--radius-md);
}

.btn-md {
  height: 40px;
  padding: 0 var(--space-4);
  font-size: var(--text-base);
}

.btn-lg {
  height: 48px;
  padding: 0 var(--space-6);
  font-size: var(--text-lg);
  border-radius: var(--radius-xl);
}

/* 类型样式 */
.btn-primary {
  background: linear-gradient(135deg, var(--color-primary-500), var(--color-primary-600));
  color: white;
  border: none;
  box-shadow: var(--shadow-md), var(--glow-primary);
}

.btn-primary:hover {
  box-shadow: var(--shadow-lg), var(--glow-primary);
  transform: translateY(-1px);
}

.btn-secondary {
  background: var(--glass-medium);
  color: var(--text-primary);
  border: 1px solid var(--glass-border);
}

.btn-secondary:hover {
  background: var(--glass-strong);
  border-color: rgba(255, 255, 255, 0.15);
}

.btn-success {
  background: linear-gradient(135deg, var(--color-success-500), var(--color-success-600));
  color: white;
  box-shadow: var(--shadow-md), var(--glow-success);
}

.btn-warning {
  background: linear-gradient(135deg, var(--color-warning-500), var(--color-warning-600));
  color: white;
  box-shadow: var(--shadow-md), var(--glow-warning);
}

.btn-danger {
  background: linear-gradient(135deg, var(--color-error-500), var(--color-error-600));
  color: white;
  box-shadow: var(--shadow-md), var(--glow-error);
}

.btn-ghost {
  background: transparent;
  color: var(--text-secondary);
  border: none;
}

.btn-ghost:hover {
  background: var(--glass-light);
  color: var(--text-primary);
}

.btn-link {
  background: transparent;
  color: var(--color-primary-400);
  border: none;
  padding: 0;
  height: auto;
}

.btn-link:hover {
  color: var(--color-primary-300);
}

/* 状态 */
.btn-disabled {
  opacity: 0.5;
  cursor: not-allowed;
  pointer-events: none;
}

.btn-loading-state {
  cursor: wait;
}

.btn-block {
  width: 100%;
}

.btn-icon-only {
  padding: 0;
  width: 40px;
}

.btn-icon-only.btn-sm {
  width: 32px;
}

.btn-icon-only.btn-lg {
  width: 48px;
}

/* 内部元素 */
.btn-loading,
.btn-icon {
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-loading svg {
  width: 16px;
  height: 16px;
}

.btn-icon {
  font-size: 1.125em;
}

.btn-content {
  display: flex;
  align-items: center;
}
</style>
