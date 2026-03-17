<template>
  <div class="dashboard-view">
    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card github-card" v-loading="statsLoading">
        <div class="stat-icon" style="background-color: #0969da;">
          <el-icon size="24"><Document /></el-icon>
        </div>
        <div class="stat-info">
          <span class="stat-value">{{ stats.articleCount }}</span>
          <span class="stat-label">文章总数</span>
        </div>
      </div>

      <div class="stat-card github-card" v-loading="statsLoading">
        <div class="stat-icon" style="background-color: #1a7f37;">
          <el-icon size="24"><Folder /></el-icon>
        </div>
        <div class="stat-info">
          <span class="stat-value">{{ stats.categoryCount }}</span>
          <span class="stat-label">分类数量</span>
        </div>
      </div>

      <div class="stat-card github-card" v-loading="statsLoading">
        <div class="stat-icon" style="background-color: #cf222e;">
          <el-icon size="24"><CollectionTag /></el-icon>
        </div>
        <div class="stat-info">
          <span class="stat-value">{{ stats.tagCount }}</span>
          <span class="stat-label">标签数量</span>
        </div>
      </div>
    </div>

    <!-- 快捷操作 -->
    <div class="quick-actions github-card">
      <h3>快捷操作</h3>
      <div class="action-buttons">
        <el-button type="primary" @click="$router.push('/admin/articles/create')">
          <el-icon><Plus /></el-icon>
          写文章
        </el-button>
        <el-button @click="$router.push('/admin/categories')">
          <el-icon><FolderAdd /></el-icon>
          新建分类
        </el-button>
        <el-button @click="$router.push('/admin/tags')">
          <el-icon><CirclePlus /></el-icon>
          新建标签
        </el-button>
      </div>
    </div>

    <!-- 最近文章 -->
    <div class="recent-articles github-card">
      <h3>最近文章</h3>
      <el-table :data="recentArticles" style="width: 100%" v-loading="articlesLoading">
        <el-table-column prop="title" label="标题" min-width="200">
          <template #default="{ row }">
            <router-link :to="`/admin/articles/edit/${row.id}`" class="article-link">
              {{ row.title }}
            </router-link>
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="分类" width="120" />
        <el-table-column prop="viewCount" label="阅读量" width="100" />
        <el-table-column prop="createTime" label="发布时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button link type="primary" @click="$router.push(`/admin/articles/edit/${row.id}`)">
              编辑
            </el-button>
            <el-button link type="danger" @click="deleteArticle(row.id)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { ArticleListVO } from '@/types'
import { getArticleList, deleteArticle as deleteArticleApi } from '@/api/article'
import { getCategoryList } from '@/api/category'
import { getTagList } from '@/api/tag'
import request from '@/utils/request'
import dayjs from 'dayjs'

const stats = ref({
  articleCount: 0,
  categoryCount: 0,
  tagCount: 0
})
const recentArticles = ref<ArticleListVO[]>([])
const statsLoading = ref(false)
const articlesLoading = ref(false)

const formatDate = (date: string) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

const fetchStats = async () => {
  statsLoading.value = true
  try {
    const [articlesRes, categoriesRes, tagsRes] = await Promise.all([
      getArticleList(1, 1),
      getCategoryList(),
      getTagList()
    ])
    stats.value.articleCount = articlesRes.data.total
    stats.value.categoryCount = categoriesRes.data.length
    stats.value.tagCount = tagsRes.data.length
  } catch (error) {
    ElMessage.error('获取统计数据失败')
  } finally {
    statsLoading.value = false
  }
}

const fetchRecentArticles = async () => {
  articlesLoading.value = true
  try {
    const res = await getArticleList(1, 5)
    recentArticles.value = res.data.list
  } catch (error) {
    ElMessage.error('获取最近文章失败')
    recentArticles.value = []
  } finally {
    articlesLoading.value = false
  }
}

const deleteArticle = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定要删除该文章吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteArticleApi(id)
    ElMessage.success('删除成功')
    fetchRecentArticles()
    fetchStats()
  } catch {
    // 取消删除
  }
}

onMounted(() => {
  fetchStats()
  fetchRecentArticles()
})
</script>

<style scoped lang="scss">
.dashboard-view {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 28px;
  font-weight: 600;
  color: var(--color-fg-default);
}

.stat-label {
  font-size: 14px;
  color: var(--color-fg-muted);
}

.quick-actions {
  padding: 20px;

  h3 {
    margin: 0 0 16px 0;
    font-size: 16px;
  }
}

.action-buttons {
  display: flex;
  gap: 12px;
}

.recent-articles {
  padding: 20px;

  h3 {
    margin: 0 0 16px 0;
    font-size: 16px;
  }
}

.article-link {
  color: var(--color-accent-fg);
  text-decoration: none;

  &:hover {
    text-decoration: underline;
  }
}

@media (max-width: 1024px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
