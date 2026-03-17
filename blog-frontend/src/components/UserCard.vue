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

      <div class="user-links">
        <a :href="userInfo.githubUrl" class="social-link" title="GitHub" v-if="userInfo.githubUrl" target="_blank">
          <el-icon size="20"><Platform /></el-icon>
        </a>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
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
  bio: '热爱编程，热爱生活 ✨',
  email: DEFAULT_EMAIL
})
const stats = ref({
  articleCount: 0,
  categoryCount: 0,
  tagCount: 0
})

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
  fetchUserInfo()
  fetchStats()
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

.social-link {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-color: var(--color-canvas-subtle);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--color-fg-muted);
  transition: all 0.2s;

  &:hover {
    background-color: var(--color-accent-subtle);
    color: var(--color-accent-fg);
    transform: translateY(-2px);
  }
}
</style>
