<template>
  <div class="avatar-upload">
    <div class="avatar-preview">
      <el-avatar :size="120" :src="currentAvatar" class="avatar-image">
        <el-icon :size="60"><User /></el-icon>
      </el-avatar>
      <div class="avatar-overlay" @click="handleUpload">
        <el-icon><Camera /></el-icon>
        <span>更换头像</span>
      </div>
    </div>
    <input
      ref="fileInput"
      type="file"
      accept="image/*"
      style="display: none"
      @change="handleFileChange"
    />
    <p class="upload-tip">支持 JPG、PNG 格式，文件大小不超过 5MB</p>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, Camera } from '@element-plus/icons-vue'
import { uploadAvatar } from '@/api/user'

const props = defineProps<{
  avatar?: string
}>()

const emit = defineEmits<{
  (e: 'update:avatar', url: string): void
  (e: 'success', url: string): void
}>()

const fileInput = ref<HTMLInputElement>()
const uploading = ref(false)

const currentAvatar = computed(() => {
  return props.avatar || ''
})

const handleUpload = () => {
  fileInput.value?.click()
}

const handleFileChange = async (event: Event) => {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]

  if (!file) return

  // 验证文件类型
  if (!file.type.startsWith('image/')) {
    ElMessage.error('请选择图片文件')
    return
  }

  // 验证文件大小（5MB）
  if (file.size > 5 * 1024 * 1024) {
    ElMessage.error('文件大小不能超过 5MB')
    return
  }

  try {
    uploading.value = true
    const res = await uploadAvatar(file)
    const avatarUrl = res.data

    emit('update:avatar', avatarUrl)
    emit('success', avatarUrl)
    ElMessage.success('头像上传成功')
  } catch (error) {
    ElMessage.error('头像上传失败')
  } finally {
    uploading.value = false
    // 清空 input 以便可以再次选择同一文件
    if (fileInput.value) {
      fileInput.value.value = ''
    }
  }
}
</script>

<style scoped lang="scss">
.avatar-upload {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.avatar-preview {
  position: relative;
  cursor: pointer;
  border-radius: 50%;
  overflow: hidden;

  &:hover {
    .avatar-overlay {
      opacity: 1;
    }
  }
}

.avatar-image {
  border: 3px solid var(--color-border-default);
  transition: all 0.3s;
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  opacity: 0;
  transition: opacity 0.3s;
  color: white;
  font-size: 14px;

  .el-icon {
    font-size: 24px;
  }
}

.upload-tip {
  font-size: 12px;
  color: var(--color-fg-muted);
  margin: 0;
}
</style>
