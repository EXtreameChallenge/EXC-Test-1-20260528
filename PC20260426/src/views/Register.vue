<template>
  <div class="register-container">
    <div class="bg-layer">
      <div class="bg-gradient"></div>
      <div class="grid-lines"></div>
      <div class="orb orb-1"></div>
      <div class="orb orb-2"></div>
      <div class="orb orb-3"></div>
    </div>

    <div class="register-wrapper">
      <div class="register-card">
        <div class="register-header">
          <h1 class="title">注册账号</h1>
          <p class="subtitle">创建轻行Claw管理平台账号</p>
        </div>

        <el-form :model="registerForm" :rules="rules" ref="registerFormRef" label-position="top" hide-required-asterisk class="register-form">
          <el-form-item prop="username">
            <label class="input-label">用户名</label>
            <div class="input-wrapper" :class="{ focused: usernameFocused }">
              <div class="input-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                  <circle cx="12" cy="7" r="4"/>
                </svg>
              </div>
              <input v-model="registerForm.username" type="text" placeholder="请输入用户名" class="custom-input" @focus="usernameFocused = true" @blur="usernameFocused = false" />
            </div>
          </el-form-item>

          <el-form-item prop="password">
            <label class="input-label">密码</label>
            <div class="input-wrapper" :class="{ focused: passwordFocused }">
              <div class="input-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                  <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                </svg>
              </div>
              <input v-model="registerForm.password" :type="showPassword ? 'text' : 'password'" placeholder="请输入密码" class="custom-input" @focus="passwordFocused = true" @blur="passwordFocused = false" />
              <button type="button" class="toggle-pwd" @click="showPassword = !showPassword">
                <svg v-if="!showPassword" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
                <svg v-else viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"/><line x1="1" y1="1" x2="23" y2="23"/></svg>
              </button>
            </div>
          </el-form-item>

          <el-form-item prop="confirmPassword">
            <label class="input-label">确认密码</label>
            <div class="input-wrapper" :class="{ focused: confirmFocused }">
              <div class="input-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
                </svg>
              </div>
              <input v-model="registerForm.confirmPassword" type="password" placeholder="请再次输入密码" class="custom-input" @focus="confirmFocused = true" @blur="confirmFocused = false" @keyup.enter="handleRegister" />
            </div>
          </el-form-item>

          <el-form-item>
            <button type="button" class="register-btn" :class="{ 'is-loading': loading }" :disabled="loading" @click="handleRegister">
              <span v-if="loading" class="btn-spinner"></span>
              <span v-else>注 册</span>
            </button>
          </el-form-item>
        </el-form>

        <div class="register-footer">
          <span>已有账号？</span>
          <router-link to="/login" class="link">立即登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '../api'

const router = useRouter()
const registerFormRef = ref(null)
const loading = ref(false)
const showPassword = ref(false)
const usernameFocused = ref(false)
const passwordFocused = ref(false)
const confirmFocused = ref(false)

const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度 2-20 位', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 3, message: '密码长度至少 3 位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  if (!registerFormRef.value) return

  await registerFormRef.value.validate(async (valid) => {
    if (!valid) return

    loading.value = true

    try {
      const res = await register(registerForm.username, registerForm.password)
      const result = res.data

      if (result.success) {
        ElMessage.success('注册成功，请登录')
        setTimeout(() => {
          router.push('/login')
        }, 800)
      } else {
        ElMessage.error(result.message)
      }
    } catch (error) {
      ElMessage.error('网络错误，请检查后端是否启动')
      console.error('Error:', error)
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
  background: #060614;
}

.bg-layer {
  position: absolute;
  inset: 0;
  overflow: hidden;
}

.bg-gradient {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(ellipse 80% 60% at 80% 30%, rgba(139, 92, 246, 0.12) 0%, transparent 60%),
    radial-gradient(ellipse 60% 70% at 20% 70%, rgba(74, 158, 255, 0.1) 0%, transparent 60%),
    radial-gradient(ellipse 50% 50% at 50% 10%, rgba(192, 132, 252, 0.06) 0%, transparent 60%);
}

.grid-lines {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(255,255,255,0.015) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255,255,255,0.015) 1px, transparent 1px);
  background-size: 50px 50px;
}

.orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  animation: float 10s ease-in-out infinite;
}

.orb-1 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(139, 92, 246, 0.2) 0%, transparent 70%);
  top: -100px;
  right: -100px;
}

.orb-2 {
  width: 350px;
  height: 350px;
  background: radial-gradient(circle, rgba(74, 158, 255, 0.18) 0%, transparent 70%);
  bottom: -80px;
  left: -80px;
  animation-delay: -4s;
}

.orb-3 {
  width: 250px;
  height: 250px;
  background: radial-gradient(circle, rgba(192, 132, 252, 0.12) 0%, transparent 70%);
  top: 50%;
  left: 40%;
  animation-delay: -6s;
}

@keyframes float {
  0%, 100% { transform: translate(0, 0) scale(1); }
  25% { transform: translate(20px, -30px) scale(1.05); }
  50% { transform: translate(-10px, -50px) scale(0.95); }
  75% { transform: translate(30px, -20px) scale(1.02); }
}

.register-wrapper {
  display: flex;
  width: 520px;
  max-width: 95vw;
  position: relative;
  z-index: 10;
}

.register-card {
  width: 100%;
  padding: 48px 56px;
  background: rgba(15, 15, 35, 0.6);
  border-radius: 24px;
  box-shadow:
    0 0 0 1px rgba(255, 255, 255, 0.08),
    0 25px 80px rgba(0, 0, 0, 0.5),
    0 0 100px rgba(139, 92, 246, 0.08);
  backdrop-filter: blur(40px);
  display: flex;
  flex-direction: column;
  align-items: center;
}

.register-header {
  text-align: center;
  margin-bottom: 36px;
  width: 100%;
}

.title {
  font-size: 28px;
  font-weight: 700;
  color: #f8fafc;
  margin-bottom: 8px;
  background: linear-gradient(135deg, #f8fafc 0%, #c8d4e8 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.subtitle {
  font-size: 14px;
  color: #64748b;
}

.register-form {
  width: 100%;
  max-width: 420px;
}

.input-label {
  display: block;
  font-size: 13px;
  font-weight: 500;
  color: #94a3b8;
  margin-bottom: 10px;
}

.input-wrapper {
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 12px;
  padding: 0 16px;
  height: 50px;
  transition: all 0.25s ease;
  width: 100%;
}

.input-wrapper.focused {
  border-color: rgba(139, 92, 246, 0.5);
  background: rgba(139, 92, 246, 0.06);
  box-shadow: 0 0 0 3px rgba(139, 92, 246, 0.1), 0 4px 16px rgba(0, 0, 0, 0.2);
}

.input-icon {
  color: rgba(255, 255, 255, 0.3);
  display: flex;
  align-items: center;
  margin-right: 12px;
  transition: color 0.25s ease;
  flex-shrink: 0;
}

.input-wrapper.focused .input-icon {
  color: #8b5cf6;
}

.input-icon svg {
  width: 20px;
  height: 20px;
}

.custom-input {
  flex: 1;
  background: transparent;
  border: none;
  outline: none;
  color: #f0f0f8;
  font-size: 14px;
  height: 100%;
  min-width: 0;
}

.custom-input::placeholder {
  color: rgba(255, 255, 255, 0.3);
}

.toggle-pwd {
  background: none;
  border: none;
  cursor: pointer;
  color: rgba(255, 255, 255, 0.3);
  padding: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: color 0.2s;
  border-radius: 6px;
  flex-shrink: 0;
}

.toggle-pwd:hover {
  color: rgba(255, 255, 255, 0.6);
  background: rgba(255, 255, 255, 0.05);
}

.toggle-pwd svg {
  width: 18px;
  height: 18px;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}

:deep(.el-form-item__label) {
  display: none;
}

:deep(.el-form-item__error) {
  color: #f87171;
  font-size: 12px;
  padding-top: 6px;
}

.register-btn {
  width: 100%;
  height: 50px;
  border: none;
  border-radius: 12px;
  background: linear-gradient(135deg, #8b5cf6 0%, #6366f1 50%, #4a9eff 100%);
  background-size: 200% 200%;
  color: white;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
  letter-spacing: 4px;
  box-shadow: 0 4px 16px rgba(139, 92, 246, 0.3);
}

.register-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(139, 92, 246, 0.4);
  background-position: 100% 100%;
}

.register-btn:active:not(:disabled) {
  transform: translateY(0);
}

.register-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.register-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255,255,255,0.2), transparent);
  transition: left 0.5s ease;
}

.register-btn:hover:not(:disabled)::before {
  left: 100%;
}

.btn-spinner {
  display: inline-block;
  width: 22px;
  height: 22px;
  border: 2px solid rgba(255,255,255,0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.register-footer {
  text-align: center;
  margin-top: 28px;
  padding-top: 24px;
  border-top: 1px solid rgba(255, 255, 255, 0.06);
  color: #64748b;
  font-size: 14px;
  width: 100%;
  max-width: 420px;
}

.link {
  color: #8b5cf6;
  text-decoration: none;
  margin-left: 6px;
  font-weight: 500;
  transition: color 0.2s;
}

.link:hover {
  color: #a78bfa;
  text-decoration: underline;
}

@media (max-width: 480px) {
  .register-card {
    padding: 32px 24px;
  }

  .title {
    font-size: 22px;
  }

  .input-wrapper {
    height: 46px;
  }

  .register-btn {
    height: 46px;
  }
}
</style>
