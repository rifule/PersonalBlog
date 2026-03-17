<template>
  <div class="tag-manage">
    <div class="page-header">
      <h2>标签管理</h2>
      <el-button type="primary" @click="openDialog()">
        <el-icon><Plus /></el-icon>
        新建标签
      </el-button>
    </div>

    <div class="tag-list github-card">
      <el-table :data="tags" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="名称" min-width="150" />
        <el-table-column prop="color" label="颜色" width="120">
          <template #default="{ row }">
            <div class="color-preview" :style="{ backgroundColor: row.color }"></div>
            <span>{{ row.color }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="articleCount" label="文章数" width="100" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="editTag(row)">编辑</el-button>
            <el-button link type="danger" @click="deleteTag(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 新建/编辑对话框 -->
    <el-dialog v-model="showDialog" :title="isEdit ? '编辑标签' : '新建标签'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="form.name" placeholder="请输入标签名称" />
        </el-form-item>
        <el-form-item label="颜色">
          <el-color-picker v-model="form.color" show-alpha />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="saveTag" :loading="saving">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { Tag } from '@/types'
import { getTagList, createTag, updateTag, deleteTag as deleteTagApi } from '@/api/tag'

const tags = ref<Tag[]>([])
const showDialog = ref(false)
const isEdit = ref(false)
const loading = ref(false)
const saving = ref(false)
const form = ref<Partial<Tag>>({
  name: '',
  color: '#409EFF'
})

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

const openDialog = () => {
  isEdit.value = false
  form.value = {
    name: '',
    color: '#409EFF'
  }
  showDialog.value = true
}

const editTag = (tag: Tag) => {
  isEdit.value = true
  form.value = { ...tag }
  showDialog.value = true
}

const saveTag = async () => {
  if (!form.value.name) {
    ElMessage.warning('请输入标签名称')
    return
  }

  saving.value = true
  try {
    if (isEdit.value && form.value.id) {
      await updateTag(form.value.id, form.value)
      ElMessage.success('更新成功')
    } else {
      await createTag(form.value)
      ElMessage.success('创建成功')
    }
    showDialog.value = false
    fetchTags()
  } catch (error) {
    ElMessage.error('操作失败')
  } finally {
    saving.value = false
  }
}

const deleteTag = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定要删除该标签吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteTagApi(id)
    ElMessage.success('删除成功')
    fetchTags()
  } catch {
    // 取消删除
  }
}

onMounted(fetchTags)
</script>

<style scoped lang="scss">
.tag-manage {
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

.tag-list {
  padding: 20px;
}

.color-preview {
  display: inline-block;
  width: 20px;
  height: 20px;
  border-radius: 4px;
  margin-right: 8px;
  vertical-align: middle;
}
</style>
