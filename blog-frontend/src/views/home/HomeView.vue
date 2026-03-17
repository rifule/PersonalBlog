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
              <el-icon><Folder /></el-icon>
              {{ article.categoryName }}
            </span>
            <span class="meta-item">
              <el-icon><Calendar /></el-icon>
              {{ formatDate(article.createTime) }}
            </span>
          </div>
        </div>

        <p class="article-summary">{{ article.summary }}</p>

        <div class="article-footer">
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
    <div class="pagination-wrapper github-card" v-if="total > 0">
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 30, 50]"
        layout="prev, pager, next"
        prev-text="上一页"
        next-text="下一页"
        @size-change="handlePageChange"
        @current-change="handlePageChange"
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

const formatDate = (date: string) => dayjs(date).format('YYYY-MM-DD')

const fetchArticles = async () => {
  loading.value = true
  try {
    const res = await getArticleList(pageNum.value, pageSize.value)
    articles.value = res.data.list
    total.value = res.data.total
  } catch {
    ElMessage.error('获取文章列表失败')
    articles.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const handlePageChange = () => fetchArticles()

onMounted(() => fetchArticles())
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
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;

  &:hover {
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2), 0 0 16px rgba(100, 200, 255, 0.1);
    transform: translateY(-4px);
    background: rgba(255, 255, 255, 0.05);
    border-color: rgba(100, 200, 255, 0.3);
  }
}

.article-header {
  margin-bottom: 12px;
}

.article-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: nowrap;
  overflow-x: auto;
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

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: var(--color-fg-muted);
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
  padding: 16px 24px;

  :deep(.el-pagination) {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .btn-prev,
    .btn-next {
      padding: 0 16px;
      font-size: 14px;
      color: var(--color-fg-default);
      background: transparent;
      border: 1px solid var(--color-border-default);
      border-radius: 6px;
      height: 32px;
      line-height: 32px;

      &:hover {
        background: var(--color-canvas-subtle);
      }

      &.is-disabled {
        color: var(--color-fg-muted);
        cursor: not-allowed;
      }
    }

    .el-pager {
      display: flex;
      gap: 8px;

      li {
        min-width: 32px;
        height: 32px;
        line-height: 32px;
        border-radius: 6px;
        font-size: 14px;
        color: var(--color-fg-default);
        background: transparent;

        &.is-active {
          background: linear-gradient(135deg, rgba(150, 157, 161, 0.8), rgba(183, 172, 23, 0.8));
          color: hsla(0, 0%, 99%, 0.979);
          box-shadow: 0 0 12px rgba(69, 168, 38, 0.4);
        }

        &:hover:not(.is-active) {
          background: rgba(158, 148, 148, 0.08);
          border-color: rgba(16, 100, 146, 0.3);
        }
      }
    }
  }
}
</style>
