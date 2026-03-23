<template>
  <div class="article-detail-page">
    <div v-if="loading" class="loading-state">
      <el-skeleton :rows="10" animated />
    </div>

    <template v-else>
      <div class="article-layout">
        <!-- 左侧关联文章列表 -->
        <aside class="article-list-sidebar">
          <div class="article-list-container">
            <div class="toc-title">相关文章</div>
            <nav class="article-list-nav">
              <ul class="article-list">
                <li v-for="art in allArticles" :key="art.id" :class="['article-item', { 'active': art.id === article.id }]">
                  <router-link :to="`/article/${art.id}`" class="article-link">
                    <span class="article-title">{{ art.title }}</span>
                    <span class="article-meta">{{ formatDate(art.createTime) }}</span>
                  </router-link>
                </li>
              </ul>
              <div v-if="allArticles.length === 0" class="no-related-articles">
                暂无相关文章
              </div>
            </nav>
          </div>
        </aside>

        <!-- 中间文章内容 -->
        <div class="article-main">
          <article class="article-content">
            <header class="article-header">
              <h1 class="article-title">{{ article.title }}</h1>
              <div class="article-meta">
                <span class="meta-item">
                  <el-icon><Calendar /></el-icon>
                  {{ formatDate(article.createTime) }}
                </span>
                <span class="meta-item">
                  <el-icon><Folder /></el-icon>
                  {{ article.categoryName }}
                </span>
                <span class="meta-item" v-if="wordCount > 0">
                  <el-icon><Document /></el-icon>
                  约 {{ wordCount }} 字
                </span>
                <span class="meta-item" v-if="readTime > 0">
                  <el-icon><Timer /></el-icon>
                  大约 {{ readTime }} 分钟
                </span>
              </div>
            </header>

            <div class="article-body markdown-body" v-html="renderedContent" ref="articleBody"></div>

            <!-- 下一篇文章 -->
            <div class="next-article" v-if="nextArticle">
              <div class="next-article-divider">
                <span>下一篇</span>
              </div>
              <router-link :to="`/article/${nextArticle.id}`" class="next-article-card">
                <div class="next-label">继续阅读</div>
                <h3 class="next-title">{{ nextArticle.title }}</h3>
                <p class="next-summary">{{ nextArticle.summary }}</p>
                <div class="next-arrow">
                  <el-icon><ArrowRight /></el-icon>
                </div>
              </router-link>
            </div>
          </article>
        </div>

        <!-- 右侧大纲导航 -->
        <TocSidebar
          ref="tocSidebarRef"
          :container-ref="articleBody"
          :offset="100"
        />
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, nextTick, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { Article, ArticleListVO } from '@/types'
import { getArticleById, getArticlesByTag, getArticleList } from '@/api/article'
import { renderMarkdown } from '@/utils/markdown'
import TocSidebar from '@/components/TocSidebar.vue'
import dayjs from 'dayjs'

const route = useRoute()
const router = useRouter()
const articleBody = ref<HTMLElement>()

const article = ref<Article>({
  id: 0,
  title: '',
  content: '',
  summary: '',
  cover: '',
  authorId: 0,
  authorName: '',
  categoryId: 0,
  categoryName: '',
  tagIds: [],
  tags: [],
  isTop: 0,
  createTime: '',
  updateTime: ''
})
const nextArticle = ref<Article | null>(null)
const allArticles = ref<Article[]>([])
const loading = ref(false)
const tocSidebarRef = ref<InstanceType<typeof TocSidebar>>()

const formatDate = (date: string) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

// 计算字数
const wordCount = computed(() => {
  if (!article.value.content) return 0
  // 移除 Markdown 标记后计算中文字符和英文单词
  const text = article.value.content
    .replace(/[#*`\[\](){}|!>-]/g, '')
    .replace(/\s+/g, '')
  return text.length
})

// 计算阅读时间（按每分钟 300 字计算）
const readTime = computed(() => {
  return Math.ceil(wordCount.value / 300)
})

const renderedContent = computed(() => {
  return renderMarkdown(article.value.content || '')
})

// 监听文章内容变化，刷新大纲
watch(renderedContent, async () => {
  await nextTick()
  tocSidebarRef.value?.refresh()
})

const fetchArticle = async () => {
  const id = parseInt(route.params.id as string)
  if (!id) {
    router.push('/')
    return
  }

  loading.value = true
  try {
    const res = await getArticleById(id)
    article.value = res.data
    await fetchRelatedArticles()
    await fetchNextArticle(id)
  } catch (error) {
    ElMessage.error('获取文章详情失败')
  } finally {
    loading.value = false
  }
}

// 获取关联文章（具有相同标签的文章）
const fetchRelatedArticles = async () => {
  try {
    // 获取当前文章的标签
    const currentTagIds = article.value.tagIds || []
    if (currentTagIds.length === 0) {
      allArticles.value = []
      return
    }

    // 使用第一个标签获取关联文章
    const res = await getArticlesByTag(currentTagIds[0], 1, 100)
    const articles: ArticleListVO[] = res.data.list || []
    
    // 过滤掉当前文章，并限制数量
    const filteredArticles = articles
      .filter((art) => art.id !== article.value.id)
      .slice(0, 20)
    
    allArticles.value = filteredArticles.map((art) => ({
      id: art.id,
      title: art.title,
      content: '',
      summary: art.summary,
      cover: art.cover,
      authorId: 0,
      authorName: art.authorName,
      categoryId: 0,
      categoryName: art.categoryName,
      tagIds: art.tagIds,
      tags: art.tags,
      isTop: art.isTop || 0,
      createTime: art.createTime,
      updateTime: ''
    }))
  } catch (error) {
    allArticles.value = []
  }
}

// 获取下一篇文章
const fetchNextArticle = async (currentId: number) => {
  try {
    const res = await getArticleList(1, 100)
    const articles: ArticleListVO[] = res.data.list
    const currentIndex = articles.findIndex((a: ArticleListVO) => a.id === currentId)
    if (currentIndex !== -1 && currentIndex < articles.length - 1) {
      const nextArt = articles[currentIndex + 1]
      nextArticle.value = {
        id: nextArt.id,
        title: nextArt.title,
        content: '',
        summary: nextArt.summary,
        cover: nextArt.cover,
        authorId: 0,
        authorName: nextArt.authorName,
        categoryId: 0,
        categoryName: nextArt.categoryName,
        tagIds: nextArt.tagIds,
        tags: nextArt.tags,
        isTop: nextArt.isTop || 0,
        createTime: nextArt.createTime,
        updateTime: ''
      }
    } else {
      nextArticle.value = null
    }
  } catch (error) {
    nextArticle.value = null
  }
}

const fetchArticleData = () => {
  fetchArticle()
}

// 滚动到页面顶部
const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
}

onMounted(() => {
  fetchArticleData()
})

watch(() => route.params.id, () => {
  fetchArticleData()
  scrollToTop()
})

</script>

<style scoped lang="scss">
.article-detail-page {
  min-height: 100vh;
  background-color: var(--color-canvas-default);
}

.loading-state {
  padding: 40px;
  max-width: 1200px;
  margin: 0 auto;
}

.article-layout {
  display: flex;
  width: 100%;
  max-width: 100%;
  margin: 0 auto;
  padding: 24px;
  gap: 24px;
}

.article-main {
  flex: 1;
  min-width: 0;
}

.article-content {
  background-color: var(--color-canvas-default);
  padding: 48px 64px;
  min-height: calc(100vh - 48px);
}

.article-header {
  margin-bottom: 48px;
  padding-bottom: 32px;
  border-bottom: 1px solid var(--color-border-default);
}

.article-title {
  font-size: 40px;
  font-weight: 700;
  margin: 0 0 24px 0;
  line-height: 1.3;
  color: var(--color-fg-default);
}

.article-meta {
  display: flex;
  gap: 20px;
  font-size: 14px;
  color: var(--color-fg-muted);
  margin-bottom: 16px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

.article-body {
  font-size: 16px;
  line-height: 1.8;
  color: var(--color-fg-default);

  :deep(h1), :deep(h2), :deep(h3), :deep(h4), :deep(h5), :deep(h6) {
    margin-top: 48px;
    margin-bottom: 24px;
    font-weight: 600;
    line-height: 1.3;
    color: var(--color-fg-default);
    scroll-margin-top: 80px;
  }

  :deep(h1) {
    font-size: 32px;
    border-bottom: 1px solid var(--color-border-default);
    padding-bottom: 16px;
  }

  :deep(h2) {
    font-size: 28px;
  }

  :deep(h3) {
    font-size: 24px;
  }

  :deep(h4) {
    font-size: 20px;
  }

  :deep(p) {
    margin: 16px 0;
  }

  :deep(pre) {
    background-color: var(--color-canvas-subtle);
    padding: 20px;
    border-radius: 8px;
    overflow-x: auto;
    margin: 24px 0;
  }

  :deep(code) {
    font-family: 'SFMono-Regular', Consolas, monospace;
    font-size: 14px;
  }

  :deep(ul), :deep(ol) {
    padding-left: 2em;
    margin: 16px 0;
  }

  :deep(li) {
    margin: 8px 0;
  }

  :deep(blockquote) {
    margin: 24px 0;
    padding: 16px 24px;
    background-color: var(--color-accent-subtle);
    border-left: 4px solid var(--color-accent-fg);
    border-radius: 0 8px 8px 0;
  }

  :deep(img) {
    max-width: 100%;
    border-radius: 8px;
    margin: 24px 0;
  }

  :deep(table) {
    width: 100%;
    border-collapse: collapse;
    margin: 24px 0;

    th, td {
      padding: 12px;
      border: 1px solid var(--color-border-default);
    }

    th {
      background-color: var(--color-canvas-subtle);
      font-weight: 600;
    }
  }
}

// 下一篇文章
.next-article {
  margin-top: 80px;
  padding-top: 48px;
  border-top: 1px solid var(--color-border-default);
}

.next-article-divider {
  text-align: center;
  margin-bottom: 32px;
  position: relative;

  &::before {
    content: '';
    position: absolute;
    top: 50%;
    left: 0;
    right: 0;
    height: 1px;
    background-color: var(--color-border-default);
  }

  span {
    position: relative;
    background-color: var(--color-canvas-default);
    padding: 0 16px;
    color: var(--color-fg-muted);
    font-size: 14px;
  }
}

.next-article-card {
  display: block;
  padding: 32px;
  background-color: var(--color-canvas-subtle);
  border-radius: 12px;
  text-decoration: none;
  transition: all 0.3s ease;
  position: relative;

  &:hover {
    background-color: var(--color-accent-subtle);
    transform: translateY(-2px);

    .next-arrow {
      color: var(--color-accent-fg);
    }
  }
}

.next-label {
  font-size: 12px;
  color: var(--color-fg-muted);
  text-transform: uppercase;
  letter-spacing: 1px;
  margin-bottom: 12px;
}

.next-title {
  font-size: 24px;
  font-weight: 600;
  color: var(--color-fg-default);
  margin: 0 0 12px 0;
  line-height: 1.4;
}

.next-summary {
  font-size: 14px;
  color: var(--color-fg-muted);
  margin: 0;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.next-arrow {
  position: absolute;
  right: 32px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 24px;
  color: var(--color-fg-muted);
  transition: color 0.3s;
}

// 左侧全部文章列表
.article-list-sidebar {
  width: 220px;
  flex-shrink: 0;
  position: static;
  height: auto;
}

.article-list-container {
  background-color: var(--color-canvas-subtle);
  border-radius: 8px;
  padding: 20px;

  .toc-title {
    text-align: center;
    font-size: 16px;
    margin: 8px 0;
    font-weight: 600;
  }
}

.article-list-nav {
  max-height: none;
  overflow-y: visible;
}

.article-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.article-item {
  margin: 8px 0;

  .article-link {
    display: block;
    padding: 10px 12px;
    color: var(--color-fg-default);
    text-decoration: none;
    border-radius: 4px;
    transition: all 0.2s;

    &:hover {
      background-color: var(--color-accent-subtle);
    }
  }

  &.active .article-link {
    background-color: var(--color-accent-subtle);
    color: var(--color-accent-fg);
    font-weight: 500;
  }

  .article-title {
    font-size: 14px;
    line-height: 1.5;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    margin-bottom: 4px;
  }

  .article-meta {
    font-size: 12px;
    color: var(--color-fg-muted);
    display: block;
  }
}

.no-related-articles {
  padding: 20px;
  text-align: center;
  color: var(--color-fg-muted);
  font-size: 14px;
}

// 响应式
@media (max-width: 1200px) {
  .article-sidebar {
    display: none;
  }

  .article-content {
    padding: 32px;
  }
}

@media (max-width: 768px) {
  .article-layout {
    width: 100%;
    max-width: 100%;
    padding: 16px;
  }

  .article-content {
    padding: 24px;
  }

  .article-title {
    font-size: 28px;
  }

  .next-article-card {
    padding: 24px;
  }

  .next-arrow {
    display: none;
  }
}
</style>
