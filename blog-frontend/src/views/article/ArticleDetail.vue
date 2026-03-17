<template>
  <div class="article-detail-page">
    <div v-if="loading" class="loading-state">
      <el-skeleton :rows="10" animated />
    </div>

    <template v-else>
      <div class="article-layout">
        <!-- 左侧文章内容 -->
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
        <aside class="article-sidebar">
          <div class="toc-container">
            <div class="toc-title">此页内容</div>
            <nav class="toc-nav">
              <ul class="toc-list">
                <li v-for="(item, index) in tocItems" :key="index" :class="['toc-item', `toc-level-${item.level}`, { 'active': activeTocId === item.id }]">
                  <a :href="`#${item.id}`" @click.prevent="scrollToSection(item.id)">{{ item.text }}</a>
                </li>
              </ul>
            </nav>
          </div>
        </aside>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, nextTick, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { Article } from '@/types'
import { getArticleById, getArticleList } from '@/api/article'
import { renderMarkdown } from '@/utils/markdown'
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
  tags: [],
  isTop: 0,
  createTime: '',
  updateTime: ''
})
const nextArticle = ref<Article | null>(null)
const loading = ref(false)
const tocItems = ref<Array<{ id: string; text: string; level: number }>>([])
const activeTocId = ref('')

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

// 生成大纲
const generateToc = () => {
  nextTick(() => {
    if (!articleBody.value) return

    const headings = articleBody.value.querySelectorAll('h1, h2, h3, h4, h5, h6')
    const items: Array<{ id: string; text: string; level: number }> = []

    headings.forEach((heading, index) => {
      const level = parseInt(heading.tagName[1])
      const text = heading.textContent || ''
      const id = `heading-${index}`
      heading.id = id
      items.push({ id, text, level })
    })

    tocItems.value = items
  })
}

// 滚动到指定章节
const scrollToSection = (id: string) => {
  const element = document.getElementById(id)
  if (element) {
    element.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }
}

// 监听滚动，高亮当前章节
const handleScroll = () => {
  if (!articleBody.value || tocItems.value.length === 0) return

  const scrollTop = window.scrollY + 100
  let currentId = ''

  for (const item of tocItems.value) {
    const element = document.getElementById(item.id)
    if (element) {
      const offsetTop = element.offsetTop
      if (scrollTop >= offsetTop) {
        currentId = item.id
      }
    }
  }

  activeTocId.value = currentId
}

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
    // 等待 DOM 更新后再生成大纲
    await nextTick()
    setTimeout(() => {
      generateToc()
    }, 100)
    await fetchNextArticle(id)
  } catch (error) {
    ElMessage.error('获取文章详情失败')
  } finally {
    loading.value = false
  }
}

// 获取下一篇文章
const fetchNextArticle = async (currentId: number) => {
  try {
    const res = await getArticleList(1, 100)
    const articles = res.data.list
    const currentIndex = articles.findIndex((a: Article) => a.id === currentId)
    if (currentIndex !== -1 && currentIndex < articles.length - 1) {
      nextArticle.value = articles[currentIndex + 1]
    } else {
      nextArticle.value = null
    }
  } catch (error) {
    nextArticle.value = null
  }
}

onMounted(() => {
  fetchArticle()
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
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
  max-width: 1400px;
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

// 右侧大纲导航
.article-sidebar {
  width: 280px;
  flex-shrink: 0;
  position: sticky;
  top: 80px;
  height: fit-content;
  max-height: calc(100vh - 100px);
  overflow-y: auto;
}

.toc-container {
  background-color: var(--color-canvas-subtle);
  border-radius: 8px;
  padding: 20px;
}

.toc-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-fg-default);
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--color-border-default);
}

.toc-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.toc-item {
  margin: 4px 0;

  a {
    display: block;
    padding: 6px 12px;
    color: var(--color-fg-muted);
    text-decoration: none;
    font-size: 13px;
    line-height: 1.5;
    border-radius: 4px;
    transition: all 0.2s;

    &:hover {
      color: var(--color-accent-fg);
      background-color: var(--color-accent-subtle);
    }
  }

  &.active a {
    color: var(--color-accent-fg);
    background-color: var(--color-accent-subtle);
    font-weight: 500;
  }
}

.toc-level-1 {
  font-weight: 600;
}

.toc-level-2 {
  padding-left: 12px;
}

.toc-level-3 {
  padding-left: 24px;
}

.toc-level-4,
.toc-level-5,
.toc-level-6 {
  padding-left: 36px;
  font-size: 12px;
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
