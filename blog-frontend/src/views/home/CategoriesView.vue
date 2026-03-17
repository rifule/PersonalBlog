<template>
  <div class="categories-page">
    <div class="page-header github-card">
      <h2>
        <el-icon><Folder /></el-icon>
        分类
      </h2>
      <p class="page-desc">共 {{ categories.length }} 个分类</p>
    </div>

    <div class="categories-list" v-if="categories.length > 0">
      <div
        v-for="category in categories"
        :key="category.id"
        class="category-section github-card"
      >
        <div class="category-header">
          <router-link :to="`/category/${category.id}`" class="category-name">
            <el-icon><FolderOpened /></el-icon>
            {{ category.name }}
            <span class="article-count">({{ category.articleCount || 0 }} 篇)</span>
          </router-link>
          <p v-if="category.description" class="category-desc">{{ category.description }}</p>
        </div>

        <div class="category-articles" v-if="categoryArticles[category.id]?.length > 0">
          <div
            v-for="article in categoryArticles[category.id]"
            :key="article.id"
            class="article-item"
          >
            <router-link :to="`/article/${article.id}`" class="article-title">
              {{ article.title }}
            </router-link>
            <span class="article-date">{{ formatDate(article.createTime) }}</span>
          </div>
        </div>

        <div class="view-all" v-if="(category.articleCount || 0) > 5">
          <router-link :to="`/category/${category.id}`">
            查看全部 {{ category.articleCount }} 篇文章
            <el-icon><ArrowRight /></el-icon>
          </router-link>
        </div>
      </div>
    </div>

    <div v-else-if="loading" class="loading-state">
      <el-skeleton :rows="5" animated />
    </div>

    <div v-else class="empty-result">
      <el-empty description="暂无分类">
        <el-button @click="$router.push('/')">返回首页</el-button>
      </el-empty>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import type { Category, ArticleListVO } from '@/types'
import { getCategoryList } from '@/api/category'
import { getArticlesByCategory } from '@/api/article'
import dayjs from 'dayjs'

const categories = ref<Category[]>([])
const categoryArticles = ref<Record<number, ArticleListVO[]>>({})
const loading = ref(false)

const formatDate = (date: string) => {
  return dayjs(date).format('YYYY-MM-DD')
}

const fetchCategories = async () => {
  loading.value = true
  try {
    const res = await getCategoryList()
    categories.value = res.data
    // 获取每个分类下的文章
    for (const category of categories.value) {
      await fetchCategoryArticles(category.id)
    }
  } catch (error) {
    ElMessage.error('获取分类列表失败')
    categories.value = []
  } finally {
    loading.value = false
  }
}

const fetchCategoryArticles = async (categoryId: number) => {
  try {
    const res = await getArticlesByCategory(categoryId, 1, 5) // 只获取前5篇
    categoryArticles.value[categoryId] = res.data.list || []
  } catch (error) {
    categoryArticles.value[categoryId] = []
  }
}

onMounted(() => {
  fetchCategories()
})
</script>

<style scoped lang="scss">
.categories-page {
  padding: 0;
}

.page-header {
  padding: 24px;
  margin-bottom: 24px;
  text-align: center;

  h2 {
    margin: 0 0 12px 0;
    font-size: 28px;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    color: var(--color-fg-default);

    .el-icon {
      color: var(--color-accent-fg);
    }
  }

  .page-desc {
    margin: 0;
    color: var(--color-fg-muted);
    font-size: 14px;
  }
}

.categories-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.category-section {
  padding: 24px;

  .category-header {
    margin-bottom: 16px;
    padding-bottom: 16px;
    border-bottom: 1px solid var(--color-border-muted);

    .category-name {
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
      }

      .el-icon {
        color: var(--color-accent-fg);
      }

      .article-count {
        font-size: 14px;
        font-weight: normal;
        color: var(--color-fg-muted);
      }
    }

    .category-desc {
      margin: 0;
      font-size: 14px;
      color: var(--color-fg-muted);
    }
  }

  .category-articles {
    display: flex;
    flex-direction: column;
    gap: 12px;

    .article-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 12px 16px;
      background-color: var(--color-canvas-subtle);
      border-radius: 6px;
      transition: background-color 0.2s;

      &:hover {
        background-color: var(--color-border-muted);
      }

      .article-title {
        color: var(--color-fg-default);
        text-decoration: none;
        font-size: 15px;

        &:hover {
          color: var(--color-accent-fg);
          text-decoration: underline;
        }
      }

      .article-date {
        font-size: 13px;
        color: var(--color-fg-muted);
        white-space: nowrap;
      }
    }
  }

  .view-all {
    margin-top: 16px;
    text-align: right;

    a {
      display: inline-flex;
      align-items: center;
      gap: 4px;
      color: var(--color-accent-fg);
      text-decoration: none;
      font-size: 14px;

      &:hover {
        text-decoration: underline;
      }
    }
  }
}

.loading-state {
  padding: 40px;
}

.empty-result {
  padding: 60px 0;
}
</style>
