<template>
  <header class="app-header">
    <div class="header-content">
      <!-- Logo -->
      <router-link to="/" class="logo">
        <el-icon size="20" class="logo-icon"><Notebook /></el-icon>
        <span class="logo-text">CodeBlog</span>
      </router-link>

      <!-- 搜索框 -->
      <div class="search-box">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索文章..."
          prefix-icon="Search"
          @keyup.enter="handleSearch"
          clearable
        />
      </div>

      <!-- 导航菜单 -->
      <nav class="nav-menu">
        <router-link to="/" class="nav-item" :class="{ active: $route.path === '/' }">
          <span>博客</span>
        </router-link>
        <router-link to="/tags" class="nav-item" :class="{ active: $route.path === '/tags' }">
          <span>标签</span>
        </router-link>
        <router-link to="/categories" class="nav-item" :class="{ active: $route.path === '/categories' }">
          <span>分类</span>
        </router-link>
        <el-dropdown class="nav-item-dropdown">
          <span class="nav-dropdown-trigger">
            <span>工具</span>
            <el-icon><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="openPromptOptimizer">
                <el-icon><MagicStick /></el-icon>
                提示词优化器
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <router-link to="/about" class="nav-item" :class="{ active: $route.path === '/about' }">
          <span>关于我</span>
        </router-link>
      </nav>

      <!-- 右侧操作区 -->
      <div class="header-actions">
        <!-- 主题切换 -->
        <button class="action-btn" @click="toggleTheme">
          <el-icon v-if="isDark"><Sunny /></el-icon>
          <el-icon v-else><Moon /></el-icon>
        </button>

        <!-- GitHub 链接 -->
        <a href="https://github.com/rifule" target="_blank" class="action-btn">
          <svg viewBox="0 0 24 24" width="18" height="18">
            <path fill="currentColor" d="M12 2C6.477 2 2 6.477 2 12c0 4.42 2.87 8.17 6.84 9.5.5.08.66-.23.66-.5v-1.69c-2.77.6-3.36-1.34-3.36-1.34-.46-1.16-1.11-1.47-1.11-1.47-.91-.62.07-.6.07-.6 1 .07 1.53 1.03 1.53 1.03.87 1.52 2.34 1.07 2.91.83.09-.65.35-1.09.63-1.34-2.22-.25-4.55-1.11-4.55-4.92 0-1.11.38-2 1.03-2.71-.1-.25-.45-1.29.1-2.64 0 0 .84-.27 2.75 1.02.79-.22 1.65-.33 2.5-.33.85 0 1.71.11 2.5.33 1.91-1.29 2.75-1.02 2.75-1.02.55 1.35.2 2.39.1 2.64.65.71 1.03 1.6 1.03 2.71 0 3.82-2.34 4.66-4.57 4.91.36.31.69.92.69 1.85V21c0 .27.16.59.67.5C19.14 20.16 22 16.42 22 12A10 10 0 0012 2z"/>
          </svg>
        </a>

        <!-- 用户菜单 -->
        <template v-if="userStore.isLoggedIn">
          <el-dropdown>
            <span class="user-menu">
              <el-avatar :size="28" :src="userStore.user?.avatar" />
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="$router.push('/admin')">
                  <el-icon><Setting /></el-icon>
                  管理后台
                </el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <el-button type="primary" size="small" @click="$router.push('/login')">
            登录
          </el-button>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useThemeStore } from '@/stores/theme'
import { ElMessageBox } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const themeStore = useThemeStore()

const searchKeyword = ref('')
const isDark = computed(() => themeStore.isDark)

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({
      path: '/search',
      query: { keyword: searchKeyword.value.trim() }
    })
  }
}

const openPromptOptimizer = () => {
  window.open('https://prompt.always200.com/#/basic/system', '_blank')
}

const toggleTheme = () => {
  themeStore.toggleTheme()
}

const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    userStore.logout()
    router.push('/')
  } catch {
    // 取消退出
  }
}
</script>

<style scoped lang="scss">
.app-header {
  height: 64px;
  background: #1b1b1f;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.header-content {
  width: 100%;
  height: 100%;
  padding: 0 24px;
  display: flex;
  align-items: center;
  gap: 24px;
  box-sizing: border-box;
}

.logo {
  flex-shrink: 0;
}

.search-box {
  flex: 1;
  max-width: 1000px;
  margin-left: 220px;
}

.nav-menu {
  flex-shrink: 0;
  margin-left: auto;
}

.header-actions {
  flex-shrink: 0;
  margin-left: 16px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
  color: #e64040;
  font-size: 18px;
  font-weight: 600;
  flex-shrink: 0;

  &:hover {
    text-decoration: none;
    opacity: 0.9;
  }

  .logo-icon {
    color: #ff6b6b;
  }
}

.search-box {
  position: relative;

  :deep(.el-input__wrapper) {
    background-color: rgba(255, 255, 255, 0.1);
    border: 1px solid rgba(255, 255, 255, 0.2);
    border-radius: 8px;
    box-shadow: none;
    padding-right: 60px;

    &:hover, &:focus-within {
      background-color: rgba(255, 255, 255, 0.15);
      border-color: rgba(255, 255, 255, 0.3);
    }
  }

  :deep(.el-input__inner) {
    height: 36px;
    color: #fff;
    background: transparent;

    &::placeholder {
      color: rgba(255, 255, 255, 0.5);
    }
  }

  :deep(.el-input__icon) {
    color: rgba(255, 255, 255, 0.5);
  }

  .search-shortcut {
    position: absolute;
    right: 8px;
    top: 50%;
    transform: translateY(-50%);
    font-size: 11px;
    color: rgba(255, 255, 255, 0.4);
    background: rgba(255, 255, 255, 0.1);
    padding: 2px 6px;
    border-radius: 4px;
    pointer-events: none;
  }
}

.nav-menu {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 8px 14px;
  border-radius: 6px;
  color: rgba(255, 255, 255, 0.8);
  text-decoration: none;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s;

  &:hover {
    color: #fff;
    background-color: rgba(255, 255, 255, 0.1);
    text-decoration: none;
  }

  &.active {
    color: #fff;
    background-color: rgba(255, 255, 255, 0.15);
  }
}

.nav-item-dropdown {
  :deep(.el-dropdown__trigger) {
    display: flex;
    align-items: center;
  }
}

.nav-dropdown-trigger {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 8px 14px;
  border-radius: 6px;
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    color: #fff;
    background-color: rgba(255, 255, 255, 0.1);
  }

  .el-icon {
    font-size: 12px;
  }
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-left: 16px;
  padding-left: 16px;
  border-left: 1px solid rgba(255, 255, 255, 0.1);
}

@media (max-width: 1400px) {
  .search-box {
    margin-left: 180px;
  }
}

@media (max-width: 1280px) {
  .search-box {
    margin-left: 24px;
  }
}

@media (max-width: 1200px) {
  .search-box {
    margin-left: 0;
    max-width: 500px;
  }
}

@media (max-width: 1024px) {
  .search-box {
    max-width: 300px;
    margin-left: 0;
  }
}

@media (max-width: 768px) {
  .search-box {
    display: none;
  }

  .nav-menu {
    display: none;
  }
}

.action-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 8px;
  border: none;
  background: transparent;
  color: rgba(255, 255, 255, 0.8);
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    background-color: rgba(255, 255, 255, 0.1);
    color: #fff;
  }
}

.user-menu {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 2px;
  border-radius: 50%;
  transition: background-color 0.2s;

  &:hover {
    background-color: rgba(255, 255, 255, 0.1);
  }
}


</style>
