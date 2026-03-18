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
      <div class="github-container">
        <div class="content-wrapper">
          <!-- 左侧侧边栏 -->
          <aside class="sidebar-left">
            <CalendarWidget />
          </aside>

          <!-- 主内容 -->
          <div class="main-area">
            <router-view v-slot="{ Component }">
              <transition name="fade" mode="out-in">
                <component :is="Component" />
              </transition>
            </router-view>
          </div>

          <!-- 右侧侧边栏 -->
          <aside class="sidebar-right">
            <UserCard />
            <CategoryList />
            <TagCloud />
          </aside>
        </div>
      </div>
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
import UserCard from '@/components/UserCard.vue'
import CalendarWidget from '@/components/CalendarWidget.vue'
import CategoryList from '@/components/CategoryList.vue'
import TagCloud from '@/components/TagCloud.vue'
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
  padding: 32px 24px;
  margin: 0 auto;
  width: 100%;
  box-sizing: border-box;
}

.content-wrapper {
  display: grid;
  grid-template-columns: 280px 1fr 280px;
  gap: 24px;
  align-items: start;
}

.sidebar-left {
  position: sticky;
  top: 80px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.sidebar-right {
  position: sticky;
  top: 80px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.main-area {
  min-width: 0;
}

@media (max-width: 1400px) {
  .content-wrapper {
    grid-template-columns: 260px 1fr 260px;
  }
}

@media (max-width: 1200px) {
  .content-wrapper {
    grid-template-columns: 1fr 260px;
    gap: 24px;
  }

  .sidebar-left {
    display: none;
  }
}

@media (max-width: 1024px) {
  .content-wrapper {
    grid-template-columns: 1fr;
  }

  .sidebar-left {
    display: none;
  }

  .sidebar-right {
    position: static;
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  }
}

@media (max-width: 768px) {
  .github-container {
    padding: 0 16px;
  }

  .sidebar-right {
    display: flex;
    flex-direction: column;
  }
}

.github-container {
  width: 100%;
  max-width: 100%;
}
</style>
