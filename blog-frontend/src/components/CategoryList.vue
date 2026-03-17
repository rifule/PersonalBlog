<template>
  <div class="category-list github-card">
    <h3 class="widget-title">
      <el-icon><Folder /></el-icon>
      文章分类
      <span class="anime-decoration"></span>
    </h3>

    <div class="category-items" v-if="categories.length > 0">
      <router-link
        v-for="category in categories"
        :key="category.id"
        :to="`/category/${category.id}`"
        class="category-item"
      >
        <span class="category-name">
          <el-icon><FolderOpened /></el-icon>
          {{ category.name }}
        </span>
        <span class="category-count">{{ category.articleCount || 0 }}</span>
      </router-link>
    </div>

    <div v-else-if="loading" class="loading-state">
      <el-skeleton :rows="3" animated />
    </div>

    <div v-else class="empty-state">
      <el-empty description="暂无分类" :image-size="60" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import type { Category } from '@/types'
import { getCategoryList } from '@/api/category'

const categories = ref<Category[]>([])
const loading = ref(false)

const fetchCategories = async () => {
  loading.value = true
  try {
    const res = await getCategoryList()
    categories.value = res.data
  } catch (error) {
    ElMessage.error('获取分类列表失败')
    categories.value = []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchCategories()
})
</script>

<style scoped lang="scss">
.category-list {
  padding: 16px;
}

.widget-title {
  font-size: 16px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0 0 16px 0;
}

.category-items {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.category-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 12px;
  border-radius: 6px;
  color: var(--color-fg-default);
  text-decoration: none;
  transition: all 0.2s;

  &:hover {
    background-color: var(--color-canvas-subtle);
    text-decoration: none;
  }
}

.category-name {
  display: flex;
  align-items: center;
  gap: 8px;

  .el-icon {
    color: var(--color-accent-fg);
  }
}

.category-count {
  font-size: 12px;
  color: var(--color-fg-muted);
  background-color: var(--color-canvas-subtle);
  padding: 2px 8px;
  border-radius: 10px;
}

.loading-state {
  padding: 16px 0;
}

.empty-state {
  padding: 20px 0;
}
</style>
