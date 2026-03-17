<template>
  <button
    class="theme-toggle"
    @click="themeStore.toggleTheme"
    :title="themeStore.isDark ? '切换到亮色模式' : '切换到暗色模式'"
  >
    <transition name="rotate" mode="out-in">
      <el-icon v-if="themeStore.isDark" key="moon" size="20"><Moon /></el-icon>
      <el-icon v-else key="sunny" size="20"><Sunny /></el-icon>
    </transition>
  </button>
</template>

<script setup lang="ts">
import { useThemeStore } from '@/stores/theme'

const themeStore = useThemeStore()
</script>

<style scoped lang="scss">
.theme-toggle {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: 1px solid var(--color-border-default);
  background-color: var(--color-canvas-default);
  color: var(--color-fg-default);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
  
  &:hover {
    background-color: var(--color-canvas-subtle);
    border-color: var(--color-accent-fg);
    transform: scale(1.1);
  }
}

.rotate-enter-active,
.rotate-leave-active {
  transition: all 0.3s ease;
}

.rotate-enter-from {
  opacity: 0;
  transform: rotate(-180deg) scale(0.5);
}

.rotate-leave-to {
  opacity: 0;
  transform: rotate(180deg) scale(0.5);
}
</style>
