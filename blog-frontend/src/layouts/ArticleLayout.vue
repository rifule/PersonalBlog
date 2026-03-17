<template>
  <div class="layout-container" :class="themeStore.themeClass">
    <!-- 背景图片 -->
    <div class="background-container" v-if="themeStore.backgroundImage">
      <img :src="themeStore.backgroundImage" class="background-image" alt="background" />
      <div class="background-overlay"></div>
    </div>

    <!-- 头部导航 -->
    <AppHeader />

    <!-- 主内容区 -->
    <main class="main-content">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>

    <!-- 底部 -->
    <AppFooter />

    <!-- 主题设置面板 -->
    <ThemeSettings />

    <!-- 回到顶部按钮 -->
    <BackToTop />
  </div>
</template>

<script setup lang="ts">
import { useThemeStore } from '@/stores/theme'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import ThemeSettings from '@/components/ThemeSettings.vue'
import BackToTop from '@/components/BackToTop.vue'

const themeStore = useThemeStore()
</script>

<style scoped lang="scss">
.layout-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  position: relative;
}

.main-content {
  flex: 1;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
