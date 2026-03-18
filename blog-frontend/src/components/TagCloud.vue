<template>
  <div class="tag-cloud github-card">
    <h3 class="widget-title">
      <el-icon><CollectionTag /></el-icon>
      标签云
      <span class="anime-decoration"></span>
    </h3>

    <div class="tag-items" v-if="list.length > 0">
      <router-link
        v-for="tag in list"
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
import { onMounted } from 'vue'
import type { Tag } from '@/types'
import { getTagList } from '@/api/tag'
import { useListFetch } from '@/composables/useFetch'

const { list, loading, execute: fetchTags } = useListFetch<Tag>(getTagList, {
  filterEmpty: true,
  emptyField: 'articleCount'
})

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
  transition: var(--transition-smooth);

  &:hover {
    transform: var(--hover-lift);
    box-shadow: var(--widget-hover-glow);
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
