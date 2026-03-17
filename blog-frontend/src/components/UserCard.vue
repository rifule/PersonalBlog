<template>
  <div class="user-card github-card">
    <div class="user-cover">
      <div class="cover-gradient"></div>
    </div>
    <div class="user-info">
      <el-avatar :size="80" :src="getAvatarUrl(userInfo.avatar)" class="user-avatar" />
      <h3 class="user-name">{{ userInfo.nickname || userInfo.username }}</h3>

      <div class="user-stats">
        <div class="stat-item">
          <span class="stat-value">{{ stats.articleCount }}</span>
          <span class="stat-label">文章</span>
        </div>
        <div class="stat-item">
          <span class="stat-value">{{ stats.categoryCount }}</span>
          <span class="stat-label">分类</span>
        </div>
        <div class="stat-item">
          <span class="stat-value">{{ stats.tagCount }}</span>
          <span class="stat-label">标签</span>
        </div>
      </div>

      <div class="user-email" v-if="userInfo.email">
        <el-icon><Message /></el-icon>
        <span>{{ userInfo.email }}</span>
      </div>

      <div class="tech-clock">
        <div class="clock-time">{{ currentTime }}</div>
        <div class="clock-date">{{ currentDate }}</div>
        <div class="clock-weekday">{{ currentWeekday }}</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getCurrentUser } from '@/api/auth'
import { getCategoryList } from '@/api/category'
import { getTagList } from '@/api/tag'
import { getArticleList } from '@/api/article'
import { getFullAvatarUrl } from '@/utils/url'

interface UserInfo {
  username: string
  nickname?: string
  avatar: string
  email?: string
  githubUrl?: string
}

const DEFAULT_AVATAR = '/image/sadrudy.jpg'
const DEFAULT_EMAIL = '1437178240@qq.com'

const userInfo = ref<UserInfo>({
  username: '伊苏',
  avatar: DEFAULT_AVATAR,
  email: DEFAULT_EMAIL
})
const stats = ref({
  articleCount: 0,
  categoryCount: 0,
  tagCount: 0
})

const currentTime = ref('')
const currentDate = ref('')
const currentWeekday = ref('')
let timer: number | null = null

const weekDays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']

const updateTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleTimeString('en-US', { 
    hour: '2-digit', 
    minute: '2-digit',
    hour12: false 
  })
  currentDate.value = now.toLocaleDateString('zh-CN', { 
    year: 'numeric', 
    month: '2-digit', 
    day: '2-digit' 
  }).replace(/\//g, '-')
  currentWeekday.value = weekDays[now.getDay()]
}

const fetchUserInfo = async () => {
  try {
    const res = await getCurrentUser()
    const user = res.data
    // 使用后端返回的数据，如果没有则使用默认值
    userInfo.value = {
      username: user.username || '伊苏',
      nickname: user.nickname,
      avatar: user.avatar || DEFAULT_AVATAR,
      email: user.email || DEFAULT_EMAIL,
      githubUrl: user.githubUrl
    }
  } catch (error) {
    // 如果未登录，使用默认信息
    userInfo.value = {
      username: '伊苏',
      avatar: DEFAULT_AVATAR,
      email: DEFAULT_EMAIL
    }
  }
}

// 导出工具函数供模板使用
const getAvatarUrl = (avatar: string | undefined) => {
  return getFullAvatarUrl(avatar)
}

const fetchStats = async () => {
  try {
    const [articlesRes, categoriesRes, tagsRes] = await Promise.all([
      getArticleList(1, 1),
      getCategoryList(),
      getTagList()
    ])
    stats.value = {
      articleCount: articlesRes.data.total,
      categoryCount: categoriesRes.data.length,
      tagCount: tagsRes.data.length
    }
  } catch (error) {
    ElMessage.error('获取统计数据失败')
  }
}

onMounted(() => {
  updateTime()
  timer = window.setInterval(updateTime, 1000)
  fetchUserInfo()
  fetchStats()
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
    timer = null
  }
})
</script>

<style scoped lang="scss">
.user-card {
  overflow: hidden;
}

.user-cover {
  height: 80px;
  background: var(--anime-gradient);
  position: relative;

  .cover-gradient {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 40px;
    background: linear-gradient(to bottom, transparent, var(--color-canvas-default));
  }
}

.user-info {
  padding: 0 16px 16px;
  text-align: center;
  margin-top: -40px;
  position: relative;
}

.user-avatar {
  border: 4px solid var(--color-canvas-default);
  box-shadow: var(--anime-shadow);
}

.user-name {
  margin: 12px 0 4px;
  font-size: 18px;
  font-weight: 600;
}

.user-bio {
  margin: 0;
  font-size: 14px;
  color: var(--color-fg-muted);
}

.user-stats {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin: 16px 0;
  padding: 16px 0;
  border-top: 1px solid var(--color-border-default);
  border-bottom: 1px solid var(--color-border-default);
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.stat-value {
  font-size: 18px;
  font-weight: 600;
  color: var(--color-fg-default);
}

.stat-label {
  font-size: 12px;
  color: var(--color-fg-muted);
}

.user-email {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  margin: 12px 0;
  font-size: 13px;
  color: var(--color-fg-muted);

  .el-icon {
    font-size: 14px;
  }
}

.user-links {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 12px;
}

.tech-clock {
  margin-top: 16px;
  padding: 16px;
  background: linear-gradient(135deg, rgba(64, 158, 255, 0.1) 0%, rgba(103, 58, 183, 0.1) 100%);
  border: 1px solid var(--color-border-default);
  border-radius: 8px;
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 2px;
    background: linear-gradient(90deg, transparent, #409eff, #673ab7, transparent);
    animation: scan 2s linear infinite;
  }

  @keyframes scan {
    0% {
      transform: translateX(-100%);
    }
    100% {
      transform: translateX(100%);
    }
  }
}

.clock-time {
  font-size: 32px;
  font-weight: 700;
  font-family: 'SF Mono', 'Monaco', 'Inconsolata', monospace;
  background: linear-gradient(90deg, #409eff, #673ab7);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-align: center;
  letter-spacing: 2px;
}

.clock-date {
  font-size: 14px;
  color: var(--color-fg-muted);
  text-align: center;
  margin-top: 8px;
  font-family: 'SF Mono', 'Monaco', 'Inconsolata', monospace;
}

.clock-weekday {
  font-size: 12px;
  color: var(--color-fg-muted);
  text-align: center;
  margin-top: 4px;
  padding: 4px 12px;
  background: var(--color-canvas-subtle);
  border-radius: 4px;
  display: inline-block;
  width: 100%;
  box-sizing: border-box;
}
</style>
