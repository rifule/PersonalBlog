<template>
  <div class="tags-page">
    <div class="page-header github-card">
      <h2>
        <el-icon><CollectionTag /></el-icon>
        标签
      </h2>
      <p class="page-desc">共 {{ tags.length }} 个标签</p>
    </div>

    <div class="tags-cloud" v-if="tags.length > 0">
      <div
        v-for="tag in tags"
        :key="tag.id"
        class="tag-item"
        :style="getTagStyle(tag)"
        @click="$router.push(`/tag/${tag.id}`)"
      >
        <span class="tag-name">{{ tag.name }}</span>
        <span class="tag-count">{{ tag.articleCount || 0 }}</span>
      </div>
    </div>

    <div class="tags-list" v-if="tags.length > 0">
      <div
        v-for="tag in tags"
        :key="tag.id"
        class="tag-section github-card"
      >
        <div class="tag-header">
          <router-link :to="`/tag/${tag.id}`" class="tag-name-link">
            <el-icon><CollectionTag /></el-icon>
            {{ tag.name }}
            <span class="article-count">({{ tag.articleCount || 0 }} 篇)</span>
          </router-link>
        </div>

        <div class="tag-articles" v-if="tagArticles[tag.id]?.length > 0">
          <div
            v-for="article in tagArticles[tag.id]"
            :key="article.id"
            class="article-item"
          >
            <router-link :to="`/article/${article.id}`" class="article-title">
              {{ article.title }}
            </router-link>
            <span class="article-date">{{ formatDate(article.createTime) }}</span>
          </div>
        </div>

        <div class="view-all" v-if="(tag.articleCount || 0) > 5">
          <router-link :to="`/tag/${tag.id}`">
            查看全部 {{ tag.articleCount }} 篇文章
            <el-icon><ArrowRight /></el-icon>
          </router-link>
        </div>
      </div>
    </div>

    <div v-else-if="loading" class="loading-state">
      <el-skeleton :rows="5" animated />
    </div>

    <div v-else class="empty-result">
      <el-empty description="暂无标签">
        <el-button @click="$router.push('/')">返回首页</el-button>
      </el-empty>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import type { Tag, ArticleListVO } from '@/types'
import { getTagList } from '@/api/tag'
import request from '@/utils/request'
import dayjs from 'dayjs'

const tags = ref<Tag[]>([])
const tagArticles = ref<Record<number, ArticleListVO[]>>({})
const loading = ref(false)

const formatDate = (date: string) => {
  return dayjs(date).format('YYYY-MM-DD')
}

const getTagStyle = (tag: Tag) => {
  // 根据文章数量计算字体大小
  const count = tag.articleCount || 0
  const fontSize = Math.max(14, Math.min(24, 14 + count * 2))
  const opacity = Math.max(0.6, Math.min(1, 0.6 + count * 0.05))

  return {
    fontSize: `${fontSize}px`,
    opacity: opacity,
    backgroundColor: tag.color || 'var(--color-accent-subtle)',
    color: tag.color ? '#fff' : 'var(--color-accent-fg)'
  }
}

const fetchTags = async () => {
  loading.value = true
  try {
    const res = await getTagList()
    // 按文章数量排序
    tags.value = res.data.sort((a: Tag, b: Tag) => (b.articleCount || 0) - (a.articleCount || 0))
    // 获取每个标签下的文章
    for (const tag of tags.value) {
      await fetchTagArticles(tag.id)
    }
  } catch (error) {
    ElMessage.error('获取标签列表失败')
    tags.value = []
  } finally {
    loading.value = false
  }
}

const fetchTagArticles = async (tagId: number) => {
  try {
    const res = await request.get(`/tags/${tagId}/articles`, {
      params: { pageNum: 1, pageSize: 5 }
    })
    tagArticles.value[tagId] = res.data.list || []
  } catch (error) {
    tagArticles.value[tagId] = []
  }
}

onMounted(() => {
  fetchTags()
})
</script>

<style scoped lang="scss">
.tags-page {
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

.tags-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  padding: 24px;
  margin-bottom: 24px;
  background-color: var(--color-canvas-default);
  border: 1px solid var(--color-border-default);
  border-radius: 8px;
  justify-content: center;

  .tag-item {
    display: flex;
    align-items: center;
    gap: 6px;
    padding: 8px 16px;
    border-radius: 20px;
    cursor: pointer;
    transition: all 0.3s ease;
    font-weight: 500;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }

    .tag-count {
      font-size: 12px;
      opacity: 0.8;
      background-color: rgba(255, 255, 255, 0.2);
      padding: 2px 8px;
      border-radius: 10px;
    }
  }
}

.tags-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.tag-section {
  padding: 24px;

  .tag-header {
    margin-bottom: 16px;
    padding-bottom: 16px;
    border-bottom: 1px solid var(--color-border-muted);

    .tag-name-link {
      font-size: 20px;
      font-weight: 600;
      color: var(--color-fg-default);
      text-decoration: none;
      display: flex;
      align-items: center;
      gap: 8px;

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
  }

  .tag-articles {
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
