import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User, LoginForm } from '@/types'
import { login, getCurrentUser } from '@/api/auth'
import { getFullAvatarUrl } from '@/utils/url'

export const useUserStore = defineStore('user', () => {
  // State
  const token = ref<string>(localStorage.getItem('token') || '')
  const user = ref<User | null>(null)
  const loading = ref(false)
  
  // Getters
  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => user.value?.role === 'ADMIN')
  
  // Actions
  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }
  
  const clearToken = () => {
    token.value = ''
    user.value = null
    localStorage.removeItem('token')
  }
  
  const loginAction = async (form: LoginForm) => {
    loading.value = true
    try {
      const res = await login(form)
      setToken(res.data.token)
      const userData = res.data.user
      // 处理头像 URL
      userData.avatar = getFullAvatarUrl(userData.avatar)
      user.value = userData
      return { success: true }
    } catch (error: any) {
      // 提取后端返回的错误信息
      const message = error?.response?.data?.message 
        || error?.message 
        || '登录失败，请检查用户名和密码'
      return { success: false, message }
    } finally {
      loading.value = false
    }
  }

  const fetchUserInfo = async () => {
    if (!token.value) return
    try {
      const res = await getCurrentUser()
      const userData = res.data
      // 处理头像 URL
      userData.avatar = getFullAvatarUrl(userData.avatar)
      user.value = userData
    } catch (error) {
      // 获取用户信息失败，但不清除 token
      // 静默处理，不输出错误日志
    }
  }
  
  const logout = () => {
    clearToken()
  }

  const updateAvatar = (avatarUrl: string) => {
    if (user.value) {
      user.value.avatar = avatarUrl
    }
  }

  // 初始化时获取用户信息
  if (token.value) {
    fetchUserInfo()
  }

  return {
    token,
    user,
    loading,
    isLoggedIn,
    isAdmin,
    loginAction,
    fetchUserInfo,
    logout,
    updateAvatar
  }
})
