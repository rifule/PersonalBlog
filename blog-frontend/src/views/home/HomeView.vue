<template>
  <div class="home-view">
    <!-- 文章列表 -->
    <div class="article-list">
      <article
        v-for="article in articles"
        :key="article.id"
        class="article-card github-card"
      >
        <div class="article-header">
          <router-link :to="`/article/${article.id}`" class="article-title">
            {{ article.title }}
            <span v-if="article.isTop" class="top-badge">置顶</span>
          </router-link>
          <div class="article-meta">
            <span class="meta-item">
              <el-icon><Calendar /></el-icon>
              {{ formatDate(article.createTime) }}
            </span>
            <span class="meta-item">
              <el-icon><Folder /></el-icon>
              {{ article.categoryName }}
            </span>
          </div>
        </div>

        <p class="article-summary">{{ article.summary }}</p>

        <div class="article-footer">
          <div class="article-tags">
            <span
              v-for="tag in article.tags"
              :key="tag"
              class="tag"
            >
              {{ tag }}
            </span>
          </div>
          <router-link :to="`/article/${article.id}`" class="read-more">
            阅读全文
            <el-icon><ArrowRight /></el-icon>
          </router-link>
        </div>
      </article>
    </div>

    <!-- 空状态 -->
    <div v-if="articles.length === 0 && !loading" class="empty-state">
      <el-empty description="暂无文章" />
    </div>

    <!-- 分页 -->
    <div class="pagination-wrapper" v-if="total > 0">
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 30, 50]"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import type { ArticleListVO } from '@/types'
import { getArticleList } from '@/api/article'
import dayjs from 'dayjs'

const articles = ref<ArticleListVO[]>([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const loading = ref(false)

const formatDate = (date: string) => {
  return dayjs(date).format('YYYY-MM-DD')
}

const fetchArticles = async () => {
  loading.value = true
  try {
    const res = await getArticleList(pageNum.value, pageSize.value)
    articles.value = res.data.list
    total.value = res.data.total
  } catch (error) {
    ElMessage.error('获取文章列表失败')
    articles.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const handleSizeChange = (val: number) => {
  pageSize.value = val
  fetchArticles()
}

const handleCurrentChange = (val: number) => {
  pageNum.value = val
  fetchArticles()
}

onMounted(() => {
  fetchArticles()
})
</script>

<style scoped lang="scss">
.home-view {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.article-card {
  padding: 24px;
  transition: all 0.3s;

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
  }
}

.article-header {
  margin-bottom: 12px;
}

.article-title {
  font-size: 20px;
  font-weight: 600;
  color: var(--color-fg-default);
  text-decoration: none;
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;

  &:hover {
    color: var(--color-accent-fg);
    text-decoration: none;
  }
}

.top-badge {
  font-size: 12px;
  padding: 2px 8px;
  background: linear-gradient(135deg, var(--anime-primary), var(--anime-secondary));
  color: white;
  border-radius: 12px;
  font-weight: normal;
}

.article-meta {
  display: flex;
  gap: 16px;
  font-size: 13px;
  color: var(--color-fg-muted);
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.article-summary {
  font-size: 15px;
  color: var(--color-fg-muted);
  line-height: 1.7;
  margin: 0 0 16px 0;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.article-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.tag {
  font-size: 12px;
  padding: 2px 8px;
  background-color: var(--color-accent-subtle);
  color: var(--color-accent-fg);
  border-radius: 12px;
}

.read-more {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  color: var(--color-accent-fg);
  text-decoration: none;

  &:hover {
    text-decoration: underline;
  }
}

.empty-state {
  padding: 60px 0;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  padding: 24px 0;
}
</style>
