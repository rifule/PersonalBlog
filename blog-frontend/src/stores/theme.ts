import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useThemeStore = defineStore('theme', () => {
  // 主题状态
  const isDark = ref(false)
  const backgroundImage = ref('')
  const backgroundOpacity = ref(0.1)
  
  // 计算属性
  const themeClass = computed(() => isDark.value ? 'dark-theme' : 'light-theme')
  
  // 初始化主题
  const initTheme = () => {
    const savedTheme = localStorage.getItem('theme')
    const savedBg = localStorage.getItem('backgroundImage')
    const savedOpacity = localStorage.getItem('backgroundOpacity')
    
    if (savedTheme) {
      isDark.value = savedTheme === 'dark'
    } else {
      // 检测系统偏好
      isDark.value = window.matchMedia('(prefers-color-scheme: dark)').matches
    }
    
    if (savedBg) {
      backgroundImage.value = savedBg
    }
    
    if (savedOpacity) {
      backgroundOpacity.value = parseFloat(savedOpacity)
    }
    
    applyTheme()
  }
  
  // 切换主题
  const toggleTheme = () => {
    isDark.value = !isDark.value
    applyTheme()
    saveTheme()
  }
  
  // 设置主题
  const setTheme = (dark: boolean) => {
    isDark.value = dark
    applyTheme()
    saveTheme()
  }
  
  // 应用主题到 DOM
  const applyTheme = () => {
    const html = document.documentElement
    if (isDark.value) {
      html.classList.add('dark')
      html.classList.remove('light')
    } else {
      html.classList.add('light')
      html.classList.remove('dark')
    }
  }
  
  // 保存主题到本地存储
  const saveTheme = () => {
    localStorage.setItem('theme', isDark.value ? 'dark' : 'light')
  }
  
  // 设置背景图片
  const setBackgroundImage = (url: string) => {
    backgroundImage.value = url
    localStorage.setItem('backgroundImage', url)
  }
  
  // 清除背景图片
  const clearBackgroundImage = () => {
    backgroundImage.value = ''
    localStorage.removeItem('backgroundImage')
  }
  
  // 设置背景透明度
  const setBackgroundOpacity = (opacity: number) => {
    backgroundOpacity.value = opacity
    localStorage.setItem('backgroundOpacity', opacity.toString())
  }
  
  return {
    isDark,
    themeClass,
    backgroundImage,
    backgroundOpacity,
    initTheme,
    toggleTheme,
    setTheme,
    setBackgroundImage,
    clearBackgroundImage,
    setBackgroundOpacity
  }
})
