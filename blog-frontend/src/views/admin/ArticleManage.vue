<template>
  <div class="article-manage">
    <div class="page-header">
      <h2>文章管理</h2>
      <el-button type="primary" @click="$router.push('/admin/articles/create')">
        <el-icon><Plus /></el-icon>
        写文章
      </el-button>
    </div>

    <div class="article-list github-card">
      <el-table :data="articles" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" min-width="200">
          <template #default="{ row }">
            <router-link :to="`/admin/articles/edit/${row.id}`" class="article-link">
              {{ row.title }}
            </router-link>
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="分类" width="120" />
        <el-table-column prop="createTime" label="发布时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="$router.push(`/admin/articles/edit/${row.id}`)">
              编辑
            </el-button>
            <el-button link type="danger" @click="deleteArticle(row.id)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="fetchArticles"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { ArticleListVO } from '@/types'
import { getArticleList, deleteArticle as deleteArticleApi } from '@/api/article'
import dayjs from 'dayjs'

const articles = ref<ArticleListVO[]>([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const loading = ref(false)

const formatDate = (date: string) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

const fetchArticles = async () => {
  loading.value = true
  try {
    const res = await getArticleList(pageNum.value, pageSize.value)
    articles.value = res.data.list
    total.value = res.data.total
  } catch (error) {
    ElMessage.error('获取文章列表失败')
    articles.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const deleteArticle = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定要删除该文章吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteArticleApi(id)
    ElMessage.success('删除成功')
    fetchArticles()
  } catch {
    // 取消删除
  }
}

onMounted(fetchArticles)
</script>

<style scoped lang="scss">
.article-manage {
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
}

.article-list {
  padding: 20px;
}

.article-link {
  color: var(--color-accent-fg);
  text-decoration: none;

  &:hover {
    text-decoration: underline;
  }
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
