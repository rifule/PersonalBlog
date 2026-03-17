<template>
  <div class="article-edit">
    <div class="page-header">
      <h2>{{ isEdit ? '编辑文章' : '写文章' }}</h2>
      <div class="header-actions">
        <el-button @click="$router.back()">取消</el-button>
        <el-button type="primary" @click="saveArticle" :loading="saving">
          保存
        </el-button>
      </div>
    </div>

    <div class="edit-form github-card">
      <el-form :model="form" label-position="top">
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="请输入文章标题" size="large" />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分类">
              <el-select v-model="form.categoryId" placeholder="选择分类" style="width: 100%">
                <el-option
                  v-for="category in categories"
                  :key="category.id"
                  :label="category.name"
                  :value="category.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标签">
              <el-select
                v-model="form.tags"
                multiple
                placeholder="选择标签"
                style="width: 100%"
              >
                <el-option
                  v-for="tag in tags"
                  :key="tag.id"
                  :label="tag.name"
                  :value="tag.name"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="摘要">
          <el-input
            v-model="form.summary"
            type="textarea"
            :rows="3"
            placeholder="请输入文章摘要"
          />
        </el-form-item>

        <el-form-item label="内容" class="content-editor">
          <el-tabs v-model="activeTab" class="editor-tabs">
            <el-tab-pane label="编辑" name="edit">
              <el-input
                v-model="form.content"
                type="textarea"
                :rows="30"
                class="markdown-editor"
                placeholder="请输入 Markdown 格式的文章内容"
              />
            </el-tab-pane>
            <el-tab-pane label="预览" name="preview">
              <div class="preview-content markdown-body" v-html="renderedContent"></div>
            </el-tab-pane>
          </el-tabs>
        </el-form-item>
      </el-form>
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
const activeTab = ref('edit')
const categories = ref<Category[]>([])
const tags = ref<Tag[]>([])
const loading = ref(false)

const form = ref<Partial<Article>>({
  title: '',
  content: '',
  summary: '',
  categoryId: undefined,
  tags: []
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
    const article = res.data
    // 将逗号分隔的标签字符串转换为数组
    form.value = {
      ...article,
      tags: article.tags ? article.tags.split(',') : []
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
    // 将标签数组转换为逗号分隔的字符串
    const articleData = {
      ...form.value,
      tags: form.value.tags?.join(',') || ''
    }

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

onMounted(() => {
  fetchCategories()
  fetchTags()
  fetchArticle()
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
  padding: 24px;
}

.content-editor {
  :deep(.el-form-item__content) {
    width: 100%;
  }
}

.editor-tabs {
  width: 100%;

  :deep(.el-tabs__content) {
    min-height: 600px;
  }
}

.markdown-editor {
  :deep(.el-textarea__inner) {
    min-height: 600px !important;
    font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace;
    font-size: 14px;
    line-height: 1.6;
  }
}

.preview-content {
  min-height: 600px;
  max-height: 800px;
  overflow-y: auto;
  padding: 24px;
  border: 1px solid var(--color-border-default);
  border-radius: 6px;
  background-color: var(--color-canvas-default);
}
</style>
