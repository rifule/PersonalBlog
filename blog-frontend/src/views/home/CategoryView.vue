<template>
  <div class="category-view">
    <div class="category-header github-card">
      <h2>
        <el-icon><Folder /></el-icon>
        分类: {{ categoryName }}
      </h2>
      <p class="category-info">该分类下共有 {{ total }} 篇文章</p>
    </div>

    <div class="article-list" v-if="articles.length > 0">
      <article
        v-for="article in articles"
        :key="article.id"
        class="article-card github-card"
      >
        <div class="article-header">
          <router-link :to="`/article/${article.id}`" class="article-title">
            {{ article.title }}
          </router-link>
          <div class="article-meta">
            <span class="meta-item">
              <el-icon><Calendar /></el-icon>
              {{ formatDate(article.createTime) }}
            </span>
          </div>
        </div>

        <p class="article-summary">{{ article.summary }}</p>

        <div class="article-footer">
          <div class="article-tags">
            <span v-for="tag in article.tags" :key="tag" class="tag">{{ tag }}</span>
          </div>
          <router-link :to="`/article/${article.id}`" class="read-more">
            阅读全文
            <el-icon><ArrowRight /></el-icon>
          </router-link>
        </div>
      </article>
    </div>

    <div v-else-if="loading" class="loading-state">
      <el-skeleton :rows="3" animated />
    </div>

    <div v-else class="empty-result">
      <el-empty description="该分类下暂无文章">
        <el-button @click="$router.push('/')">返回首页</el-button>
      </el-empty>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { ArticleListVO } from '@/types'
import { getArticlesByCategory } from '@/api/article'
import { getCategoryById } from '@/api/category'
import dayjs from 'dayjs'

const route = useRoute()
const categoryId = ref(0)
const categoryName = ref('')
const articles = ref<ArticleListVO[]>([])
const total = ref(0)
const loading = ref(false)

const formatDate = (date: string) => {
  return dayjs(date).format('YYYY-MM-DD')
}

const fetchCategoryInfo = async () => {
  try {
    const res = await getCategoryById(categoryId.value)
    categoryName.value = res.data.name
  } catch (error) {
    categoryName.value = '未知分类'
  }
}

const fetchArticles = async () => {
  categoryId.value = parseInt(route.params.id as string)
  if (!categoryId.value) return

  loading.value = true
  try {
    const res = await getArticlesByCategory(categoryId.value)
    articles.value = res.data.list
    total.value = res.data.total
    await fetchCategoryInfo()
  } catch (error) {
    ElMessage.error('获取文章列表失败')
    articles.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

watch(() => route.params.id, fetchArticles)

onMounted(fetchArticles)
</script>

<style scoped lang="scss">
.category-view {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.category-header {
  padding: 20px;

  h2 {
    margin: 0 0 12px 0;
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .category-info {
    margin: 0;
    color: var(--color-fg-muted);
  }
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.article-card {
  padding: 20px;
}

.article-header {
  margin-bottom: 12px;
}

.article-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--color-fg-default);
  text-decoration: none;
  display: block;
  margin-bottom: 8px;

  &:hover {
    color: var(--color-accent-fg);
  }
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
  font-size: 14px;
  color: var(--color-fg-muted);
  line-height: 1.6;
  margin: 0 0 16px 0;
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

.loading-state {
  padding: 40px 0;
}

.empty-result {
  padding: 60px 0;
}
</style>
