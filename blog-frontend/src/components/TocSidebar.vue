<template>
  <aside class="toc-sidebar">
    <div class="toc-container">
      <div class="toc-header">
        <div class="toc-title">此页内容</div>
        <div class="toc-progress">
          <div class="progress-bar">
            <div class="progress-fill" :style="{ height: `${scrollProgress}%` }"></div>
          </div>
        </div>
      </div>
      <nav class="toc-nav" ref="tocNavRef">
        <ul class="toc-list">
          <li
            v-for="(item, index) in tocItems"
            :key="item.id"
            :class="[
              'toc-item',
              `toc-level-${item.level}`,
              { 'active': activeId === item.id }
            ]"
            :style="{ '--item-index': index }"
          >
            <a
              :href="`#${item.id}`"
              @click.prevent="scrollToSection(item.id)"
              class="toc-link"
              :title="item.text"
            >
              <span class="toc-dot"></span>
              <span class="toc-text">{{ item.text }}</span>
            </a>
          </li>
        </ul>
      </nav>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick, watch } from 'vue'

interface TocItem {
  id: string
  text: string
  level: number
  element: HTMLElement
}

interface Props {
  containerRef?: HTMLElement
  contentSelector?: string
  headingSelectors?: string[]
  offset?: number
}

const props = withDefaults(defineProps<Props>(), {
  contentSelector: '.markdown-body',
  headingSelectors: () => ['h1', 'h2', 'h3', 'h4', 'h5', 'h6'],
  offset: 100
})

const tocItems = ref<TocItem[]>([])
const activeId = ref('')
const scrollProgress = ref(0)
const tocNavRef = ref<HTMLElement>()

// 生成目录
const generateToc = async () => {
  await nextTick()
  
  const container = props.containerRef || document.querySelector(props.contentSelector)
  if (!container) {
    tocItems.value = []
    return
  }

  const selector = props.headingSelectors.join(', ')
  const headings = container.querySelectorAll(selector)
  
  const items: TocItem[] = []
  headings.forEach((heading, index) => {
    const element = heading as HTMLElement
    const level = parseInt(element.tagName[1])
    const text = element.textContent?.trim() || ''
    const id = `toc-heading-${index}`
    
    // 设置标题的 id
    element.id = id
    
    items.push({
      id,
      text,
      level,
      element
    })
  })

  tocItems.value = items
}

// 滚动到指定章节
const scrollToSection = (id: string) => {
  const element = document.getElementById(id)
  if (element) {
    const top = element.offsetTop - props.offset
    window.scrollTo({
      top,
      behavior: 'smooth'
    })
  }
}

// 计算滚动进度
const calculateScrollProgress = () => {
  if (tocItems.value.length === 0) return

  const scrollTop = window.scrollY
  const docHeight = document.documentElement.scrollHeight - window.innerHeight
  
  if (docHeight <= 0) {
    scrollProgress.value = 0
    return
  }

  // 计算整体阅读进度
  const progress = (scrollTop / docHeight) * 100
  scrollProgress.value = Math.min(Math.max(progress, 0), 100)
}

// 更新当前激活的标题
const updateActiveHeading = () => {
  if (tocItems.value.length === 0) return

  const scrollTop = window.scrollY + props.offset
  let currentId = ''

  // 找到当前可视区域内的标题
  for (let i = tocItems.value.length - 1; i >= 0; i--) {
    const item = tocItems.value[i]
    if (item.element.offsetTop <= scrollTop) {
      currentId = item.id
      break
    }
  }

  // 如果没有找到，默认选中第一个
  if (!currentId && tocItems.value.length > 0) {
    currentId = tocItems.value[0].id
  }

  if (activeId.value !== currentId) {
    activeId.value = currentId
    scrollActiveItemIntoView()
  }
}

// 将激活的目录项滚动到可视区域
const scrollActiveItemIntoView = () => {
  if (!tocNavRef.value) return

  const activeElement = tocNavRef.value.querySelector('.toc-item.active')
  if (activeElement) {
    activeElement.scrollIntoView({
      behavior: 'smooth',
      block: 'nearest'
    })
  }
}

// 滚动事件处理
const handleScroll = () => {
  calculateScrollProgress()
  updateActiveHeading()
}

// 监听容器变化
watch(() => props.containerRef, () => {
  generateToc()
}, { immediate: true })

onMounted(() => {
  generateToc()
  window.addEventListener('scroll', handleScroll, { passive: true })
  handleScroll()
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})

// 暴露方法给父组件
defineExpose({
  generateToc,
  refresh: generateToc
})
</script>

<style scoped lang="scss">
.toc-sidebar {
  width: 280px;
  flex-shrink: 0;
  position: sticky;
  top: 80px;
  height: fit-content;
  max-height: calc(100vh - 100px);
}

.toc-container {
  background-color: var(--color-canvas-subtle);
  border-radius: 12px;
  padding: 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.toc-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--color-border-default);
}

.toc-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-fg-default);
}

.toc-progress {
  display: flex;
  align-items: center;
}

.progress-bar {
  width: 4px;
  height: 24px;
  background-color: var(--color-border-default);
  border-radius: 2px;
  overflow: hidden;
  position: relative;
}

.progress-fill {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  background: linear-gradient(180deg, var(--color-accent-fg) 0%, var(--color-accent-muted) 100%);
  border-radius: 2px;
  transition: height 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 0 8px rgba(var(--color-accent-fg-rgb, 59, 130, 246), 0.4);
}

.toc-nav {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  max-height: calc(100vh - 200px);
  
  &::-webkit-scrollbar {
    width: 4px;
  }
  
  &::-webkit-scrollbar-track {
    background: transparent;
  }
  
  &::-webkit-scrollbar-thumb {
    background-color: var(--color-border-default);
    border-radius: 2px;
  }
}

.toc-list {
  list-style: none;
  padding: 0;
  margin: 0;
  position: relative;
}

.toc-item {
  margin: 2px 0;
  position: relative;
  
  &.active {
    .toc-link {
      color: var(--color-accent-fg);
      background-color: var(--color-accent-subtle);
    }
    
    .toc-dot {
      background-color: var(--color-accent-fg);
      transform: scale(1.2);
      box-shadow: 0 0 0 3px var(--color-accent-subtle);
    }
  }
}

.toc-link {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  padding: 6px 10px;
  color: var(--color-fg-muted);
  text-decoration: none;
  font-size: 13px;
  line-height: 1.5;
  border-radius: 6px;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  min-height: 28px;
  
  &:hover {
    color: var(--color-accent-fg);
    background-color: var(--color-accent-subtle);
    
    .toc-dot {
      background-color: var(--color-accent-fg);
    }
  }
}

.toc-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background-color: var(--color-fg-muted);
  flex-shrink: 0;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  margin-top: 6px;
}

.toc-text {
  word-break: break-all;
  overflow-wrap: break-word;
}

// 层级缩进
.toc-level-1 {
  .toc-link {
    font-weight: 600;
    color: var(--color-fg-default);
  }
}

.toc-level-2 {
  .toc-link {
    padding-left: 14px;
  }
}

.toc-level-3 {
  .toc-link {
    padding-left: 22px;
    font-size: 12px;
  }
}

.toc-level-4,
.toc-level-5,
.toc-level-6 {
  .toc-link {
    padding-left: 30px;
    font-size: 12px;
    color: var(--color-fg-muted);
  }
}

// 响应式
@media (max-width: 1200px) {
  .toc-sidebar {
    display: none;
  }
}
</style>
