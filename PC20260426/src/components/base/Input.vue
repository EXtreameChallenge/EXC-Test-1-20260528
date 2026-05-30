<template>
  <div :class="inputWrapperClasses">
    <label v-if="label" :for="inputId" class="input-label">
      {{ label }}
      <span v-if="required" class="input-required">*</span>
    </label>
    
    <div class="input-container">
      <span v-if="$slots.prefix || prefixIcon" class="input-prefix">
        <slot name="prefix">
          <component v-if="prefixIcon" :is="prefixIcon" />
        </slot>
      </span>
      
      <input
        :id="inputId"
        ref="inputRef"
        :type="type"
        :value="modelValue"
        :placeholder="placeholder"
        :disabled="disabled"
        :readonly="readonly"
        :maxlength="maxlength"
        :class="inputClasses"
        @input="handleInput"
        @focus="handleFocus"
        @blur="handleBlur"
      />
      
      <span v-if="$slots.suffix || suffixIcon || clearable" class="input-suffix">
        <button
          v-if="clearable && modelValue && !disabled"
          type="button"
          class="input-clear"
          @click="handleClear"
        >
          <svg viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
          </svg>
        </button>
        <slot name="suffix">
          <component v-if="suffixIcon" :is="suffixIcon" />
        </slot>
      </span>
    </div>
    
    <p v-if="error" class="input-error">{{ error }}</p>
    <p v-else-if="hint" class="input-hint">{{ hint }}</p>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, useSlots } from 'vue'

interface Props {
  modelValue: string | number
  type?: string
  label?: string
  placeholder?: string
  disabled?: boolean
  readonly?: boolean
  required?: boolean
  clearable?: boolean
  error?: string
  hint?: string
  size?: 'sm' | 'md' | 'lg'
  prefixIcon?: any
  suffixIcon?: any
  maxlength?: number
}

const props = withDefaults(defineProps<Props>(), {
  type: 'text',
  size: 'md',
  disabled: false,
  readonly: false,
  required: false,
  clearable: false
})

const emit = defineEmits<{
  'update:modelValue': [value: string]
  focus: [event: FocusEvent]
  blur: [event: FocusEvent]
  clear: []
}>()

const slots = useSlots()
const inputRef = ref<HTMLInputElement>()
const inputId = `input-${Math.random().toString(36).slice(2, 9)}`
const isFocused = ref(false)

const inputWrapperClasses = computed(() => [
  'input-wrapper',
  `input-${props.size}`,
  {
    'input-has-error': !!props.error,
    'input-disabled': props.disabled,
    'input-focused': isFocused.value
  }
])

const inputClasses = computed(() => [
  'input-field',
  {
    'has-prefix': slots.prefix || props.prefixIcon,
    'has-suffix': slots.suffix || props.suffixIcon || props.clearable
  }
])

function handleInput(event: Event) {
  const target = event.target as HTMLInputElement
  emit('update:modelValue', target.value)
}

function handleFocus(event: FocusEvent) {
  isFocused.value = true
  emit('focus', event)
}

function handleBlur(event: FocusEvent) {
  isFocused.value = false
  emit('blur', event)
}

function handleClear() {
  emit('update:modelValue', '')
  emit('clear')
  inputRef.value?.focus()
}

function focus() {
  inputRef.value?.focus()
}

function blur() {
  inputRef.value?.blur()
}

defineExpose({ focus, blur })
</script>

<style scoped>
.input-wrapper {
  display: flex;
  flex-direction: column;
  gap: var(--space-2);
}

.input-label {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--text-secondary);
}

.input-required {
  color: var(--color-error-500);
  margin-left: var(--space-1);
}

.input-container {
  position: relative;
  display: flex;
  align-items: center;
}

.input-field {
  width: 100%;
  background: var(--glass-light);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  color: var(--text-primary);
  font-family: var(--font-sans);
  font-size: var(--text-base);
  transition: all var(--duration-200) var(--ease-out);
}

.input-field::placeholder {
  color: var(--text-muted);
}

.input-field:hover:not(:disabled) {
  border-color: var(--border-color-strong);
}

.input-field:focus {
  outline: none;
  border-color: var(--color-primary-500);
  box-shadow: 0 0 0 3px rgba(0, 102, 255, 0.15);
}

/* 尺寸 */
.input-sm .input-field {
  height: 32px;
  padding: 0 var(--space-3);
  font-size: var(--text-sm);
  border-radius: var(--radius-md);
}

.input-md .input-field {
  height: 40px;
  padding: 0 var(--space-4);
}

.input-lg .input-field {
  height: 48px;
  padding: 0 var(--space-5);
  font-size: var(--text-lg);
  border-radius: var(--radius-xl);
}

/* 前缀后缀 */
.input-prefix,
.input-suffix {
  position: absolute;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-tertiary);
  pointer-events: none;
}

.input-prefix {
  left: var(--space-4);
}

.input-suffix {
  right: var(--space-4);
  gap: var(--space-2);
}

.has-prefix {
  padding-left: var(--space-10) !important;
}

.has-suffix {
  padding-right: var(--space-10) !important;
}

.input-clear {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 16px;
  height: 16px;
  padding: 0;
  border: none;
  background: none;
  color: var(--text-muted);
  cursor: pointer;
  pointer-events: auto;
  transition: color var(--duration-150) var(--ease-out);
}

.input-clear:hover {
  color: var(--text-secondary);
}

.input-clear svg {
  width: 14px;
  height: 14px;
}

/* 状态 */
.input-has-error .input-field {
  border-color: var(--color-error-500);
}

.input-has-error .input-field:focus {
  box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.15);
}

.input-disabled .input-field {
  opacity: 0.5;
  cursor: not-allowed;
  pointer-events: none;
}

/* 提示信息 */
.input-error {
  font-size: var(--text-sm);
  color: var(--color-error-500);
  margin: 0;
}

.input-hint {
  font-size: var(--text-sm);
  color: var(--text-tertiary);
  margin: 0;
}
</style>
