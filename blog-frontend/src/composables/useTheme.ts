import { ref, computed, watch } from 'vue'
import type { Ref, ComputedRef } from 'vue'

export type ThemeMode = 'light' | 'dark'

export interface ThemeConfig {
  themeClass: ThemeMode
  backgroundImage: string | null
}

export interface UseThemeReturn {
  theme: Ref<ThemeMode>
  themeClass: ComputedRef<string>
  backgroundImage: Ref<string | null>
  setTheme: (mode: ThemeMode) => void
  toggleTheme: () => void
  setBackgroundImage: (url: string | null) => void
}

const THEME_KEY = 'blog-theme'
const BACKGROUND_KEY = 'blog-background'

export function useTheme(): UseThemeReturn {
  const theme = ref<ThemeMode>(
    (localStorage.getItem(THEME_KEY) as ThemeMode) || 'light'
  )
  
  const backgroundImage = ref<string | null>(
    localStorage.getItem(BACKGROUND_KEY) || null
  )

  const themeClass = computed(() => theme.value)

  const applyTheme = (mode: ThemeMode) => {
    const root = document.documentElement
    
    if (mode === 'dark') {
      root.classList.add('dark')
    } else {
      root.classList.remove('dark')
    }
  }

  const setTheme = (mode: ThemeMode) => {
    theme.value = mode
    localStorage.setItem(THEME_KEY, mode)
    applyTheme(mode)
  }

  const toggleTheme = () => {
    const newTheme = theme.value === 'light' ? 'dark' : 'light'
    setTheme(newTheme)
  }

  const setBackgroundImage = (url: string | null) => {
    backgroundImage.value = url
    if (url) {
      localStorage.setItem(BACKGROUND_KEY, url)
    } else {
      localStorage.removeItem(BACKGROUND_KEY)
    }
  }

  watch(
    theme,
    (newTheme) => {
      applyTheme(newTheme)
    },
    { immediate: true }
  )

  return {
    theme,
    themeClass,
    backgroundImage,
    setTheme,
    toggleTheme,
    setBackgroundImage
  }
}
