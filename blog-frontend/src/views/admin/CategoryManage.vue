<template>
  <div class="category-manage">
    <div class="page-header">
      <h2>分类管理</h2>
      <el-button type="primary" @click="openDialog()">
        <el-icon><Plus /></el-icon>
        新建分类
      </el-button>
    </div>

    <div class="category-list github-card">
      <el-table :data="categories" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="名称" min-width="150" />
        <el-table-column prop="description" label="描述" min-width="200" />
        <el-table-column prop="articleCount" label="文章数" width="100" />
        <el-table-column prop="sortOrder" label="排序" width="100" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="editCategory(row)">编辑</el-button>
            <el-button link type="danger" @click="deleteCategory(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 新建/编辑对话框 -->
    <el-dialog v-model="showDialog" :title="isEdit ? '编辑分类' : '新建分类'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" placeholder="请输入分类描述" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="saveCategory" :loading="saving">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { Category } from '@/types'
import { getCategoryList, createCategory, updateCategory, deleteCategory as deleteCategoryApi } from '@/api/category'

const categories = ref<Category[]>([])
const showDialog = ref(false)
const isEdit = ref(false)
const loading = ref(false)
const saving = ref(false)
const form = ref<Partial<Category>>({
  name: '',
  description: '',
  sortOrder: 0
})

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

const openDialog = () => {
  isEdit.value = false
  // 计算默认排序值：取现有分类最大排序值 + 1
  const maxSortOrder = categories.value.length > 0
    ? Math.max(...categories.value.map(c => c.sortOrder || 0))
    : 0
  form.value = {
    name: '',
    description: '',
    sortOrder: maxSortOrder + 1
  }
  showDialog.value = true
}

const editCategory = (category: Category) => {
  isEdit.value = true
  form.value = { ...category }
  showDialog.value = true
}

const saveCategory = async () => {
  if (!form.value.name) {
    ElMessage.warning('请输入分类名称')
    return
  }

  saving.value = true
  try {
    if (isEdit.value && form.value.id) {
      await updateCategory(form.value.id, form.value)
      ElMessage.success('更新成功')
    } else {
      await createCategory(form.value)
      ElMessage.success('创建成功')
    }
    showDialog.value = false
    fetchCategories()
  } catch (error) {
    ElMessage.error('操作失败')
  } finally {
    saving.value = false
  }
}

const deleteCategory = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定要删除该分类吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteCategoryApi(id)
    ElMessage.success('删除成功')
    fetchCategories()
  } catch {
    // 取消删除
  }
}

onMounted(fetchCategories)
</script>

<style scoped lang="scss">
.category-manage {
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

.category-list {
  padding: 20px;
}
</style>
