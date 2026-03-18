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

        <div class="article-tags" v-if="article.tags && article.tags.length > 0">
          <el-tag
            v-for="(tag, index) in article.tags"
            :key="index"
            size="small"
            class="tag-item"
          >
            {{ tag }}
          </el-tag>
        </div>

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
const pageSize = ref(5)
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
  background: var(--article-card-bg);
  border: 1px solid var(--article-card-border);
  border-radius: 12px;
  box-shadow: var(--article-card-shadow);

  &:hover {
    box-shadow: var(--article-card-hover-shadow);
    transform: translateY(-4px);
    background: var(--article-card-hover-bg);
    border-color: var(--article-card-hover-border);
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
  margin: 0 0 12px 0;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 16px;

  :deep(.el-tag) {
    background: var(--color-accent-subtle);
    border-color: var(--color-accent-fg);
    color: var(--color-accent-fg);
  }

  .tag-item {
    border-radius: 12px;
  }
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
      color: var(--pagination-btn-color);
      background: var(--pagination-btn-bg);
      border: 1px solid var(--pagination-btn-border);
      border-radius: 6px;
      height: 32px;
      line-height: 32px;

      &:hover:not(.is-disabled) {
        background: var(--pagination-btn-hover-bg);
      }

      &.is-disabled {
        color: var(--pagination-btn-disabled-color);
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
        color: var(--pagination-btn-color);
        background: var(--pagination-btn-bg);
        border: 1px solid transparent;
        transition: all 0.2s;

        &.is-active {
          background: var(--pagination-active-bg);
          color: var(--pagination-active-color);
          box-shadow: var(--pagination-active-shadow);
        }

        &:hover:not(.is-active) {
          background: var(--pagination-hover-bg);
          border-color: var(--pagination-hover-border);
        }
      }
    }
  }
}
</style>
