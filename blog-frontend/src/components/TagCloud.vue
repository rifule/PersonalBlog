<template>
  <div class="tag-cloud github-card">
    <h3 class="widget-title">
      <el-icon><CollectionTag /></el-icon>
      标签云
      <span class="anime-decoration"></span>
    </h3>

    <div class="tag-items" v-if="tags.length > 0">
      <router-link
        v-for="tag in tags"
        :key="tag.id"
        :to="`/tag/${tag.id}`"
        class="tag-item"
        :style="{ backgroundColor: tag.color + '20', color: tag.color, borderColor: tag.color + '40' }"
      >
        {{ tag.name }}
        <span class="tag-count">{{ tag.articleCount || 0 }}</span>
      </router-link>
    </div>

    <div v-else-if="loading" class="loading-state">
      <el-skeleton :rows="2" animated />
    </div>

    <div v-else class="empty-state">
      <el-empty description="暂无标签" :image-size="60" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import type { Tag } from '@/types'
import { getTagList } from '@/api/tag'

const tags = ref<Tag[]>([])
const loading = ref(false)

const fetchTags = async () => {
  loading.value = true
  try {
    const res = await getTagList()
    tags.value = res.data
  } catch (error) {
    ElMessage.error('获取标签列表失败')
    tags.value = []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchTags()
})
</script>

<style scoped lang="scss">
.tag-cloud {
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

.tag-items {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag-item {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 13px;
  text-decoration: none;
  border: 1px solid;
  transition: all 0.2s;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    text-decoration: none;
  }
}

.tag-count {
  font-size: 11px;
  opacity: 0.8;
}

.loading-state {
  padding: 16px 0;
}

.empty-state {
  padding: 20px 0;
}
</style>
