<template>
  <div class="login-container">
    <div class="bg-layer">
      <div class="bg-gradient"></div>
      <div class="grid-lines"></div>
      <div class="orb orb-1"></div>
      <div class="orb orb-2"></div>
      <div class="orb orb-3"></div>
      <div class="orb orb-4"></div>
    </div>

    <div class="login-wrapper">
      <div class="login-card">
        <div class="login-header">
          <h1 class="title">轻行Claw</h1>
          <p class="subtitle">无人车队智能管理平台</p>
        </div>

        <el-form
          :model="loginForm"
          :rules="rules"
          ref="loginFormRef"
          label-position="top"
          hide-required-asterisk
          class="login-form"
        >
          <el-form-item prop="username">
            <label class="input-label">用户名</label>
            <div class="input-wrapper" :class="{ focused: usernameFocused }">
              <div class="input-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                  <circle cx="12" cy="7" r="4"/>
                </svg>
              </div>
              <input
                v-model="loginForm.username"
                type="text"
                placeholder="请输入用户名"
                class="custom-input"
                @focus="usernameFocused = true"
                @blur="usernameFocused = false"
                @keyup.enter="handleLogin"
              />
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
              <input
                v-model="loginForm.password"
                :type="showPassword ? 'text' : 'password'"
                placeholder="请输入密码"
                class="custom-input"
                @focus="passwordFocused = true"
                @blur="passwordFocused = false"
                @keyup.enter="handleLogin"
              />
              <button type="button" class="toggle-pwd" @click="showPassword = !showPassword">
                <svg v-if="!showPassword" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                  <circle cx="12" cy="12" r="3"/>
                </svg>
                <svg v-else viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"/>
                  <line x1="1" y1="1" x2="23" y2="23"/>
                </svg>
              </button>
            </div>
          </el-form-item>

          <div class="form-options">
            <label class="remember-me">
              <input type="checkbox" v-model="rememberMe" />
              <span class="checkmark"></span>
              <span class="remember-text">记住密码</span>
            </label>
            <a href="#" class="forgot-link">忘记密码？</a>
          </div>

          <el-form-item>
            <button
              type="button"
              class="login-btn"
              :class="{ 'is-loading': loading }"
              :disabled="loading"
              @click="handleLogin"
            >
              <span v-if="loading" class="btn-spinner"></span>
              <span v-else>登 录</span>
            </button>
          </el-form-item>
        </el-form>

        <div class="login-footer">
          <span>还没有账号？</span>
          <router-link to="/register" class="link">立即注册</router-link>
        </div>
      </div>

      <div class="login-decoration">
        <div class="decoration-content">
          <h2>智能车队管理</h2>
          <p>高效、安全、可靠的无人驾驶车队管理系统</p>
          <div class="feature-list">
            <div class="feature-item">
              <div class="feature-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <polyline points="22 12 18 12 15 21 9 3 6 12 2 12"/>
                </svg>
              </div>
              <span>实时监控</span>
            </div>
            <div class="feature-item">
              <div class="feature-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <circle cx="12" cy="12" r="10"/>
                  <polyline points="12 6 12 12 16 14"/>
                </svg>
              </div>
              <span>智能调度</span>
            </div>
            <div class="feature-item">
              <div class="feature-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
                </svg>
              </div>
              <span>安全可靠</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '../api'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const loginFormRef = ref(null)
const loading = ref(false)
const showPassword = ref(false)
const rememberMe = ref(false)
const usernameFocused = ref(false)
const passwordFocused = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 3, message: '密码长度至少 3 位', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  await loginFormRef.value.validate(async (valid) => {
    if (!valid) return

    loading.value = true

    try {
      const res = await login(loginForm.username, loginForm.password)
      const result = res.data

      if (result.code === 200 || result.success) {
        const data = result.data || {}
        const token = data.accessToken || data.token || 'token-' + Date.now()
        const userInfo = data.userInfo || data.user || {}
        
        authStore.setAuth(token, {
          username: userInfo.username || loginForm.username,
          name: userInfo.name || loginForm.username,
          role: userInfo.roleKey || userInfo.role || 'admin'
        })

        if (data.refreshToken) {
          localStorage.setItem('refreshToken', data.refreshToken)
        }

        ElMessage.success('欢迎回来，' + (userInfo.name || loginForm.username))

        setTimeout(() => {
          router.push('/admin/dashboard')
        }, 500)
      } else {
        ElMessage.error(result.message || '登录失败')
      }
    } catch (error) {
      if (error.response?.status === 401) {
        ElMessage.error('用户名或密码错误')
      } else {
        ElMessage.error('网络错误，请检查后端是否启动')
      }
      console.error('Error:', error)
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.login-container {
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
    radial-gradient(ellipse 80% 60% at 20% 30%, rgba(74, 158, 255, 0.12) 0%, transparent 60%),
    radial-gradient(ellipse 60% 70% at 80% 70%, rgba(99, 102, 241, 0.1) 0%, transparent 60%),
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
  background: radial-gradient(circle, rgba(74, 158, 255, 0.2) 0%, transparent 70%);
  top: -100px;
  left: -100px;
  animation-delay: 0s;
}

.orb-2 {
  width: 350px;
  height: 350px;
  background: radial-gradient(circle, rgba(99, 102, 241, 0.18) 0%, transparent 70%);
  bottom: -80px;
  right: -80px;
  animation-delay: -4s;
}

.orb-3 {
  width: 250px;
  height: 250px;
  background: radial-gradient(circle, rgba(192, 132, 252, 0.12) 0%, transparent 70%);
  top: 50%;
  left: 60%;
  animation-delay: -6s;
}

.orb-4 {
  width: 200px;
  height: 200px;
  background: radial-gradient(circle, rgba(34, 211, 238, 0.1) 0%, transparent 70%);
  bottom: 20%;
  left: 5%;
  animation-delay: -2s;
}

@keyframes float {
  0%, 100% { transform: translate(0, 0) scale(1); }
  25% { transform: translate(20px, -30px) scale(1.05); }
  50% { transform: translate(-10px, -50px) scale(0.95); }
  75% { transform: translate(30px, -20px) scale(1.02); }
}

.login-wrapper {
  display: flex;
  width: 950px;
  max-width: 95vw;
  background: rgba(15, 15, 35, 0.6);
  border-radius: 24px;
  overflow: hidden;
  box-shadow:
    0 0 0 1px rgba(255, 255, 255, 0.08),
    0 25px 80px rgba(0, 0, 0, 0.5),
    0 0 100px rgba(74, 158, 255, 0.08);
  backdrop-filter: blur(40px);
  position: relative;
  z-index: 10;
}

.login-card {
  flex: 1;
  padding: 48px 56px;
  background: linear-gradient(180deg, rgba(20, 20, 45, 0.95) 0%, rgba(15, 15, 35, 0.98) 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
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

.login-form {
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
  border-color: rgba(74, 158, 255, 0.5);
  background: rgba(74, 158, 255, 0.06);
  box-shadow: 0 0 0 3px rgba(74, 158, 255, 0.1), 0 4px 16px rgba(0, 0, 0, 0.2);
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
  color: #4a9eff;
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

.form-options {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24px;
  width: 100%;
}

.remember-me {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #64748b;
  font-size: 13px;
  user-select: none;
}

.remember-me input {
  display: none;
}

.remember-me .checkmark {
  width: 18px;
  height: 18px;
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 5px;
  background: rgba(255, 255, 255, 0.04);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
}

.remember-me input:checked + .checkmark {
  background: linear-gradient(135deg, #4a9eff, #6366f1);
  border-color: transparent;
}

.remember-me input:checked + .checkmark::after {
  content: '';
  width: 10px;
  height: 6px;
  border: 2px solid white;
  border-top: none;
  border-right: none;
  transform: rotate(-45deg) translateY(-1px);
}

.forgot-link {
  color: #4a9eff;
  font-size: 13px;
  text-decoration: none;
  transition: color 0.2s;
}

.forgot-link:hover {
  color: #6bb5ff;
}

.login-btn {
  width: 100%;
  height: 50px;
  border: none;
  border-radius: 12px;
  background: linear-gradient(135deg, #4a9eff 0%, #6366f1 50%, #8b5cf6 100%);
  background-size: 200% 200%;
  color: white;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
  letter-spacing: 4px;
  box-shadow: 0 4px 16px rgba(74, 158, 255, 0.3);
}

.login-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(74, 158, 255, 0.4);
  background-position: 100% 100%;
}

.login-btn:active:not(:disabled) {
  transform: translateY(0);
}

.login-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.login-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255,255,255,0.2), transparent);
  transition: left 0.5s ease;
}

.login-btn:hover:not(:disabled)::before {
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

.login-footer {
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
  color: #4a9eff;
  text-decoration: none;
  margin-left: 6px;
  font-weight: 500;
  transition: color 0.2s;
}

.link:hover {
  color: #6bb5ff;
  text-decoration: underline;
}

.login-decoration {
  width: 380px;
  background: linear-gradient(135deg, rgba(0, 102, 255, 0.15) 0%, rgba(139, 92, 246, 0.1) 100%);
  padding: 48px 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.login-decoration::before {
  content: '';
  position: absolute;
  inset: 0;
  background: radial-gradient(ellipse at center, rgba(0, 102, 255, 0.1) 0%, transparent 70%);
}

.login-decoration::after {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(45deg, transparent 40%, rgba(255,255,255,0.03) 50%, transparent 60%);
  animation: shine 8s ease-in-out infinite;
}

@keyframes shine {
  0% { transform: translateX(-100%) rotate(45deg); }
  100% { transform: translateX(100%) rotate(45deg); }
}

.decoration-content {
  position: relative;
  z-index: 1;
  text-align: center;
}

.decoration-content h2 {
  font-size: 24px;
  font-weight: 600;
  color: #f8fafc;
  margin-bottom: 12px;
}

.decoration-content p {
  font-size: 14px;
  color: #94a3b8;
  margin-bottom: 36px;
  line-height: 1.6;
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 14px 18px;
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 12px;
  transition: all 0.25s ease;
}

.feature-item:hover {
  background: rgba(255, 255, 255, 0.06);
  border-color: rgba(74, 158, 255, 0.2);
  transform: translateX(4px);
}

.feature-icon {
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, rgba(74, 158, 255, 0.2), rgba(139, 92, 246, 0.15));
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #4a9eff;
  flex-shrink: 0;
}

.feature-icon svg {
  width: 18px;
  height: 18px;
}

.feature-item span {
  font-size: 14px;
  font-weight: 500;
  color: #e2e8f0;
}

@media (max-width: 900px) {
  .login-wrapper {
    flex-direction: column;
  }

  .login-decoration {
    display: none;
  }

  .login-card {
    padding: 40px 32px;
  }
}

@media (max-width: 480px) {
  .login-card {
    padding: 32px 24px;
  }

  .title {
    font-size: 22px;
  }

  .input-wrapper {
    height: 46px;
  }

  .login-btn {
    height: 46px;
  }
}
</style>
