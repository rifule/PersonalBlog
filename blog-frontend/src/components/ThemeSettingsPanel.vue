<template>
  <div class="theme-settings-panel">
    <!-- 主题模式 -->
    <div class="setting-section">
      <h4 class="section-title">
        <el-icon><Sunny /></el-icon>
        主题模式
      </h4>
      <div class="theme-options">
        <div
          class="theme-option"
          :class="{ active: !themeStore.isDark }"
          @click="themeStore.setTheme(false)"
        >
          <div class="theme-preview light-preview">
            <div class="preview-header"></div>
            <div class="preview-content"></div>
          </div>
          <span class="theme-label">亮色</span>
        </div>
        <div
          class="theme-option"
          :class="{ active: themeStore.isDark }"
          @click="themeStore.setTheme(true)"
        >
          <div class="theme-preview dark-preview">
            <div class="preview-header"></div>
            <div class="preview-content"></div>
          </div>
          <span class="theme-label">暗色</span>
        </div>
      </div>
    </div>
    
    <!-- 背景图片 -->
    <div class="setting-section">
      <h4 class="section-title">
        <el-icon><Picture /></el-icon>
        背景图片
      </h4>
      <div class="bg-options">
        <div
          v-for="(bg, index) in backgroundOptions"
          :key="index"
          class="bg-option"
          :class="{ active: themeStore.backgroundImage === bg.url }"
          @click="selectBackground(bg.url)"
        >
          <img :src="bg.url" :alt="bg.name" />
          <span class="bg-name">{{ bg.name }}</span>
        </div>
        <div
          class="bg-option"
          :class="{ active: !themeStore.backgroundImage }"
          @click="clearBackground"
        >
          <div class="no-bg">无背景</div>
        </div>
      </div>
      
      <!-- 自定义背景 -->
      <div class="custom-bg">
        <el-input
          v-model="customBgUrl"
          placeholder="输入图片URL"
          clearable
        >
          <template #append>
            <el-button @click="applyCustomBg">应用</el-button>
          </template>
        </el-input>
      </div>
      
      <!-- 背景透明度 -->
      <div class="opacity-control">
        <span>背景透明度</span>
        <el-slider
          v-model="themeStore.backgroundOpacity"
          :min="0"
          :max="0.5"
          :step="0.05"
          @change="themeStore.setBackgroundOpacity($event as number)"
        />
        <span>{{ Math.round(themeStore.backgroundOpacity * 100) }}%</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useThemeStore } from '@/stores/theme'

const themeStore = useThemeStore()
const customBgUrl = ref('')

const backgroundOptions = [
  { name: '星空', url: 'https://images.unsplash.com/photo-1519681393784-d120267933ba?w=1920&q=80' },
  { name: '樱花', url: 'https://images.unsplash.com/photo-1522383225653-ed111181a951?w=1920&q=80' },
  { name: '城市', url: 'https://images.unsplash.com/photo-1514565131-fce0801e5785?w=1920&q=80' },
  { name: '自然', url: 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=1920&q=80' }
]

const selectBackground = (url: string) => {
  themeStore.setBackgroundImage(url)
}

const clearBackground = () => {
  themeStore.clearBackgroundImage()
}

const applyCustomBg = () => {
  if (customBgUrl.value.trim()) {
    themeStore.setBackgroundImage(customBgUrl.value.trim())
    customBgUrl.value = ''
  }
}
</script>

<style scoped lang="scss">
.theme-settings-panel {
  padding: 8px;
}

.setting-section {
  margin-bottom: 24px;
  
  &:last-child {
    margin-bottom: 0;
  }
}

.section-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-fg-default);
  margin: 0 0 12px 0;
  display: flex;
  align-items: center;
  gap: 8px;
}

.theme-options {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.theme-option {
  cursor: pointer;
  border-radius: 8px;
  padding: 8px;
  border: 2px solid transparent;
  transition: all 0.2s;
  
  &:hover {
    background-color: var(--color-canvas-subtle);
  }
  
  &.active {
    border-color: var(--color-accent-fg);
    background-color: var(--color-accent-subtle);
  }
}

.theme-preview {
  width: 100%;
  height: 80px;
  border-radius: 6px;
  overflow: hidden;
  border: 1px solid var(--color-border-default);
  margin-bottom: 8px;
  
  .preview-header {
    height: 20px;
  }
  
  .preview-content {
    height: 60px;
  }
  
  &.light-preview {
    .preview-header {
      background-color: #f6f8fa;
    }
    .preview-content {
      background-color: #ffffff;
    }
  }
  
  &.dark-preview {
    .preview-header {
      background-color: #161b22;
    }
    .preview-content {
      background-color: #0d1117;
    }
  }
}

.theme-label {
  font-size: 13px;
  color: var(--color-fg-default);
  display: block;
  text-align: center;
}

.bg-options {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
  margin-bottom: 16px;
}

.bg-option {
  cursor: pointer;
  border-radius: 8px;
  overflow: hidden;
  border: 2px solid transparent;
  transition: all 0.2s;
  position: relative;
  
  &:hover {
    transform: scale(1.05);
  }
  
  &.active {
    border-color: var(--color-accent-fg);
  }
  
  img {
    width: 100%;
    height: 60px;
    object-fit: cover;
  }
  
  .bg-name {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    background: rgba(0, 0, 0, 0.6);
    color: white;
    font-size: 11px;
    padding: 2px 4px;
    text-align: center;
  }
  
  .no-bg {
    width: 100%;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: var(--color-canvas-subtle);
    font-size: 12px;
    color: var(--color-fg-muted);
  }
}

.custom-bg {
  margin-bottom: 16px;
}

.opacity-control {
  display: flex;
  align-items: center;
  gap: 12px;
  
  span {
    font-size: 13px;
    color: var(--color-fg-muted);
    white-space: nowrap;
  }
  
  .el-slider {
    flex: 1;
  }
}
</style>
