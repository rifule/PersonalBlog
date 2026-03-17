<template>
  <div class="login-page" :class="themeStore.themeClass">
    <!-- 背景图片 -->
    <div class="login-background" v-if="themeStore.backgroundImage">
      <img :src="themeStore.backgroundImage" alt="background" />
    </div>
    
    <div class="login-container">
      <div class="login-box">
        <div class="login-header">
          <el-icon size="48" class="login-logo"><Notebook /></el-icon>
          <h1 class="login-title">博客管理系统</h1>
          <p class="login-subtitle">欢迎回来，请登录您的账号</p>
        </div>
        
        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
          @keyup.enter="handleLogin"
        >
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              placeholder="用户名"
              prefix-icon="User"
              size="large"
            />
          </el-form-item>
          
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="密码"
              prefix-icon="Lock"
              size="large"
              show-password
            />
          </el-form-item>
          
          <el-form-item>
            <el-button
              type="primary"
              size="large"
              class="login-btn"
              :loading="loading"
              @click="handleLogin"
            >
              登录
            </el-button>
          </el-form-item>
        </el-form>
        
        <div class="login-footer">
          <el-button link @click="$router.push('/')">
            <el-icon><ArrowLeft /></el-icon>
            返回首页
          </el-button>
          <ThemeToggle />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { useThemeStore } from '@/stores/theme'
import { useUserStore } from '@/stores/user'
import ThemeToggle from '@/components/ThemeToggle.vue'

const router = useRouter()
const themeStore = useThemeStore()
const userStore = useUserStore()

const loginFormRef = ref<FormInstance>()
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const loginRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      const result = await userStore.loginAction(loginForm)
      if (result.success) {
        ElMessage.success('登录成功')
        router.push('/admin')
      } else {
        ElMessage.error(result.message || '登录失败')
      }
    }
  })
}
</script>

<style scoped lang="scss">
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  background-color: var(--color-canvas-default);
}

.login-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
  
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    opacity: 0.3;
  }
}

.login-container {
  width: 100%;
  max-width: 420px;
  padding: 20px;
}

.login-box {
  background-color: var(--color-canvas-default);
  border: 1px solid var(--color-border-default);
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.login-logo {
  color: var(--anime-primary);
  margin-bottom: 16px;
}

.login-title {
  font-size: 24px;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: var(--color-fg-default);
}

.login-subtitle {
  font-size: 14px;
  color: var(--color-fg-muted);
  margin: 0;
}

.login-form {
  .el-input {
    --el-input-border-color: var(--color-border-default);
    --el-input-hover-border-color: var(--color-accent-fg);
    --el-input-focus-border-color: var(--color-accent-fg);
  }
}

.login-btn {
  width: 100%;
  margin-top: 8px;
}

.login-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid var(--color-border-default);
}
</style>
