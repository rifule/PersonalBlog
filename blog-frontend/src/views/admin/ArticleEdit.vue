<template>
  <div class="article-edit">
    <div class="page-header">
      <h2>{{ isEdit ? '编辑文章' : '写文章' }}</h2>
      <div class="header-actions">
        <el-button @click="$router.back()">取消</el-button>
        <el-button type="primary" @click="saveArticle" :loading="saving">
          {{ isEdit ? '保存' : '发布' }}
        </el-button>
      </div>
    </div>

    <div class="edit-form">
      <!-- 简化表单区域 -->
      <div class="meta-form github-card">
        <el-input
          v-model="form.title"
          placeholder="请输入文章标题"
          class="title-input"
        />

        <div class="meta-row">
          <el-select
            v-model="form.categoryId"
            placeholder="选择分类"
            clearable
            class="meta-select"
          >
            <el-option
              v-for="category in categories"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            />
          </el-select>

          <el-select
            v-model="form.tagIds"
            multiple
            placeholder="选择标签"
            clearable
            collapse-tags
            collapse-tags-tooltip
            class="meta-select"
          >
            <el-option
              v-for="tag in tags"
              :key="tag.id"
              :label="tag.name"
              :value="tag.id"
            >
              <div class="tag-option">
                <span class="tag-color" :style="{ backgroundColor: tag.color || '#909399' }"></span>
                <span>{{ tag.name }}</span>
              </div>
            </el-option>
          </el-select>

          <el-input
            v-model="form.summary"
            placeholder="文章摘要（可选）"
            class="summary-input"
          />
        </div>
      </div>

      <!-- 主要内容编辑区域 - 左右分栏实时预览 -->
      <div class="content-editor github-card">
        <div class="editor-container">
          <div class="editor-pane">
            <div class="pane-header">编辑</div>
            <el-input
              v-model="form.content"
              type="textarea"
              :rows="35"
              class="markdown-editor"
              placeholder="请输入 Markdown 格式的文章内容"
            />
          </div>
          <div class="preview-pane">
            <div class="pane-header">预览</div>
            <div class="preview-content markdown-body" v-html="renderedContent"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { Article, Category, Tag } from '@/types'
import { getArticleById, createArticle, updateArticle } from '@/api/article'
import { getCategoryList } from '@/api/category'
import { getTagList } from '@/api/tag'
import { renderMarkdown } from '@/utils/markdown'

const route = useRoute()
const router = useRouter()

const isEdit = computed(() => !!route.params.id)
const saving = ref(false)
const categories = ref<Category[]>([])
const tags = ref<Tag[]>([])
const loading = ref(false)

const form = ref<Partial<Article> & { tagIds?: number[] }>({
  title: '',
  content: '',
  summary: '',
  categoryId: undefined,
  tags: [],
  tagIds: []
})

const renderedContent = computed(() => {
  return renderMarkdown(form.value.content || '')
})

const fetchArticle = async () => {
  if (!isEdit.value) return

  const id = parseInt(route.params.id as string)
  loading.value = true
  try {
    const res = await getArticleById(id)
    const article = res.data as any
    // 后端返回 tagIds 和 tags，直接使用
    form.value = {
      ...article,
      tagIds: article.tagIds || []
    }
  } catch (error) {
    ElMessage.error('获取文章失败')
  } finally {
    loading.value = false
  }
}

const fetchCategories = async () => {
  try {
    const res = await getCategoryList()
    categories.value = res.data
  } catch (error) {
    ElMessage.error('获取分类列表失败')
    categories.value = []
  }
}

const fetchTags = async () => {
  try {
    const res = await getTagList()
    tags.value = res.data
  } catch (error) {
    ElMessage.error('获取标签列表失败')
    tags.value = []
  }
}

const saveArticle = async () => {
  if (!form.value.title) {
    ElMessage.warning('请输入标题')
    return
  }

  saving.value = true
  try {
    // 构建提交数据，使用 tagIds
    const articleData = {
      title: form.value.title,
      content: form.value.content,
      summary: form.value.summary,
      cover: form.value.cover,
      categoryId: form.value.categoryId,
      tagIds: form.value.tagIds || [],
      isTop: form.value.isTop
    } as any

    if (isEdit.value && route.params.id) {
      await updateArticle(parseInt(route.params.id as string), articleData)
      ElMessage.success('更新成功')
    } else {
      await createArticle(articleData)
      ElMessage.success('创建成功')
    }
    router.push('/admin/articles')
  } catch (error) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

onMounted(async () => {
  await fetchCategories()
  await fetchTags()
  await fetchArticle()
})
</script>

<style scoped lang="scss">
.article-edit {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;

  h2 {
    margin: 0;
    font-size: 20px;
  }

  .header-actions {
    display: flex;
    gap: 12px;
  }
}

.edit-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.meta-form {
  padding: 16px 20px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.title-input {
  :deep(.el-input__wrapper) {
    box-shadow: none;
    background: transparent;
    border: none;
  }

  :deep(.el-input__inner) {
    font-size: 24px;
    font-weight: 600;
  }
}

.meta-row {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.meta-select {
  width: 180px;

  :deep(.el-select__wrapper) {
    background-color: var(--color-canvas-subtle);
  }
}

.tag-option {
  display: flex;
  align-items: center;
  gap: 8px;
}

.tag-color {
  display: inline-block;
  width: 12px;
  height: 12px;
  border-radius: 50%;
  flex-shrink: 0;
}

:deep(.el-select__selected-item) {
  .el-tag {
    background-color: var(--color-canvas-subtle);
    border-color: var(--color-border-default);
    color: var(--color-fg-default);

    .el-tag__close {
      color: var(--color-fg-muted);

      &:hover {
        background-color: var(--color-accent-subtle);
        color: var(--color-accent-fg);
      }
    }
  }
}

.summary-input {
  flex: 1;
  min-width: 200px;

  :deep(.el-input__wrapper) {
    background-color: var(--color-canvas-subtle);
  }
}

.editor-container {
  display: flex;
  gap: 16px;
  height: 700px;
}

.editor-pane,
.preview-pane {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.pane-header {
  padding: 8px 16px;
  font-size: 13px;
  font-weight: 500;
  color: var(--color-fg-muted);
  border-bottom: 1px solid var(--color-border-default);
}

.markdown-editor {
  flex: 1;
  border: none;

  :deep(.el-textarea__inner) {
    height: 100%;
    min-height: 100% !important;
    font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace;
    font-size: 14px;
    line-height: 1.6;
    background-color: var(--color-canvas-subtle);
    border: none;
    border-radius: 0;
    resize: none;
  }
}

.preview-pane {
  .preview-content {
    flex: 1;
    overflow-y: auto;
    padding: 16px;
    background-color: var(--color-canvas-default);
  }
}
</style>
