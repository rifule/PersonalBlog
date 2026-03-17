<template>
  <div class="user-settings">
    <div class="settings-container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1 class="page-title">
          <el-icon><User /></el-icon>
          个人设置
        </h1>
        <p class="page-description">管理您的个人信息、头像和安全设置</p>
      </div>

      <div class="settings-grid">
        <!-- 左侧：头像和基本信息 -->
        <div class="settings-left">
          <!-- 头像卡片 -->
          <el-card class="settings-card avatar-card">
            <template #header>
              <div class="card-header">
                <el-icon><Avatar /></el-icon>
                <span>头像设置</span>
              </div>
            </template>
            <div class="avatar-section">
              <AvatarUpload
                v-model:avatar="userForm.avatar"
                @success="handleAvatarSuccess"
              />
            </div>
          </el-card>

          <!-- 基本信息卡片 -->
          <el-card class="settings-card">
            <template #header>
              <div class="card-header">
                <el-icon><Document /></el-icon>
                <span>基本信息</span>
              </div>
            </template>
            <el-form
              ref="formRef"
              :model="userForm"
              :rules="rules"
              label-position="top"
              class="user-form"
            >
              <el-form-item label="用户名">
                <el-input v-model="userForm.username" disabled>
                  <template #prefix>
                    <el-icon><User /></el-icon>
                  </template>
                </el-input>
              </el-form-item>

              <el-form-item label="昵称" prop="nickname">
                <el-input v-model="userForm.nickname" placeholder="请输入昵称">
                  <template #prefix>
                    <el-icon><Avatar /></el-icon>
                  </template>
                </el-input>
              </el-form-item>

              <el-form-item label="邮箱" prop="email">
                <el-input v-model="userForm.email" placeholder="请输入邮箱">
                  <template #prefix>
                    <el-icon><Message /></el-icon>
                  </template>
                </el-input>
              </el-form-item>

              <el-form-item label="GitHub" prop="githubUrl">
                <el-input v-model="userForm.githubUrl" placeholder="请输入GitHub链接">
                  <template #prefix>
                    <el-icon><Platform /></el-icon>
                  </template>
                </el-input>
              </el-form-item>

              <el-form-item>
                <el-button
                  type="primary"
                  @click="handleSave"
                  :loading="saving"
                  class="save-btn"
                >
                  <el-icon><Check /></el-icon>
                  保存修改
                </el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </div>

        <!-- 右侧：密码修改 -->
        <div class="settings-right">
          <el-card class="settings-card password-card">
            <template #header>
              <div class="card-header">
                <el-icon><Lock /></el-icon>
                <span>安全设置</span>
              </div>
            </template>

            <div class="password-section">
              <h4 class="subsection-title">修改密码</h4>
              <p class="subsection-desc">定期更换密码可以保护您的账户安全</p>

              <el-form
                ref="passwordFormRef"
                :model="passwordForm"
                :rules="passwordRules"
                label-position="top"
                class="password-form"
              >
                <el-form-item label="原密码" prop="oldPassword">
                  <el-input
                    v-model="passwordForm.oldPassword"
                    type="password"
                    placeholder="请输入原密码"
                    show-password
                  >
                    <template #prefix>
                      <el-icon><Key /></el-icon>
                    </template>
                  </el-input>
                </el-form-item>

                <el-form-item label="新密码" prop="newPassword">
                  <el-input
                    v-model="passwordForm.newPassword"
                    type="password"
                    placeholder="请输入新密码"
                    show-password
                  >
                    <template #prefix>
                      <el-icon><Lock /></el-icon>
                    </template>
                  </el-input>
                </el-form-item>

                <el-form-item label="确认密码" prop="confirmPassword">
                  <el-input
                    v-model="passwordForm.confirmPassword"
                    type="password"
                    placeholder="请再次输入新密码"
                    show-password
                  >
                    <template #prefix>
                      <el-icon><CircleCheck /></el-icon>
                    </template>
                  </el-input>
                </el-form-item>

                <el-form-item>
                  <el-button
                    type="primary"
                    @click="handleChangePassword"
                    :loading="changingPassword"
                    class="save-btn"
                  >
                    <el-icon><RefreshRight /></el-icon>
                    修改密码
                  </el-button>
                </el-form-item>
              </el-form>
            </div>

            <el-divider />

            <!-- 主题设置 -->
            <div class="theme-section">
              <h4 class="subsection-title">主题设置</h4>
              <p class="subsection-desc">选择您喜欢的界面主题</p>

              <div class="theme-options">
                <div
                  class="theme-option"
                  :class="{ active: !isDark }"
                  @click="setTheme('light')"
                >
                  <div class="theme-preview light-preview">
                    <div class="preview-header"></div>
                    <div class="preview-content">
                      <div class="preview-line"></div>
                      <div class="preview-line short"></div>
                    </div>
                  </div>
                  <span class="theme-label">浅色主题</span>
                  <el-icon v-if="!isDark" class="check-icon"><Check /></el-icon>
                </div>

                <div
                  class="theme-option"
                  :class="{ active: isDark }"
                  @click="setTheme('dark')"
                >
                  <div class="theme-preview dark-preview">
                    <div class="preview-header"></div>
                    <div class="preview-content">
                      <div class="preview-line"></div>
                      <div class="preview-line short"></div>
                    </div>
                  </div>
                  <span class="theme-label">深色主题</span>
                  <el-icon v-if="isDark" class="check-icon"><Check /></el-icon>
                </div>
              </div>
            </div>
          </el-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import {
  User,
  Avatar,
  Document,
  Lock,
  Message,
  Key,
  CircleCheck,
  Check,
  RefreshRight,
  Platform
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { useThemeStore } from '@/stores/theme'
import { getCurrentUser, updateUserInfo } from '@/api/user'
import { getFullAvatarUrl } from '@/utils/url'
import AvatarUpload from '@/components/AvatarUpload.vue'

const userStore = useUserStore()
const themeStore = useThemeStore()
const formRef = ref<FormInstance>()
const passwordFormRef = ref<FormInstance>()

const isDark = computed(() => themeStore.isDark)

const userForm = ref({
  username: '',
  nickname: '',
  email: '',
  avatar: '',
  githubUrl: ''
})

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const saving = ref(false)
const changingPassword = ref(false)

const rules: FormRules = {
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

const passwordRules: FormRules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.value.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const setTheme = (theme: 'light' | 'dark') => {
  themeStore.setTheme(theme === 'dark')
  ElMessage.success(`已切换到${theme === 'dark' ? '深色' : '浅色'}主题`)
}

const fetchUserInfo = async () => {
  try {
    const res = await getCurrentUser()
    const user = res.data
    userForm.value = {
      username: user.username,
      nickname: user.nickname || '',
      email: user.email || '',
      avatar: getFullAvatarUrl(user.avatar),
      githubUrl: user.githubUrl || ''
    }
  } catch (error) {
    ElMessage.error('获取用户信息失败')
  }
}

const handleAvatarSuccess = (url: string) => {
  userForm.value.avatar = url
  userStore.updateAvatar(url)
}

const handleSave = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      saving.value = true
      try {
        await updateUserInfo({
          nickname: userForm.value.nickname,
          email: userForm.value.email,
          githubUrl: userForm.value.githubUrl
        })
        ElMessage.success('保存成功')
        // 刷新用户信息
        await fetchUserInfo()
      } catch (error) {
        ElMessage.error('保存失败')
      } finally {
        saving.value = false
      }
    }
  })
}

const handleChangePassword = async () => {
  if (!passwordFormRef.value) return

  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      changingPassword.value = true
      try {
        ElMessage.success('密码修改成功')
        passwordForm.value = {
          oldPassword: '',
          newPassword: '',
          confirmPassword: ''
        }
      } catch (error) {
        ElMessage.error('密码修改失败')
      } finally {
        changingPassword.value = false
      }
    }
  })
}

onMounted(() => {
  fetchUserInfo()
})
</script>

<style scoped lang="scss">
.user-settings {
  min-height: 100vh;
  background-color: var(--color-canvas-default);
  padding: 24px;
}

.settings-container {
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 32px;

  .page-title {
    font-size: 28px;
    font-weight: 600;
    color: var(--color-fg-default);
    margin: 0 0 8px 0;
    display: flex;
    align-items: center;
    gap: 12px;

    .el-icon {
      font-size: 32px;
      color: var(--color-accent-emphasis);
    }
  }

  .page-description {
    font-size: 14px;
    color: var(--color-fg-muted);
    margin: 0;
  }
}

.settings-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;

  @media (max-width: 992px) {
    grid-template-columns: 1fr;
  }
}

.settings-left,
.settings-right {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.settings-card {
  background-color: var(--color-canvas-subtle);
  border: 1px solid var(--color-border-default);
  border-radius: 12px;
  transition: all 0.3s;

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  :deep(.el-card__header) {
    padding: 16px 20px;
    border-bottom: 1px solid var(--color-border-default);
  }

  :deep(.el-card__body) {
    padding: 20px;
  }
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: var(--color-fg-default);

  .el-icon {
    font-size: 18px;
    color: var(--color-accent-emphasis);
  }
}

.avatar-card {
  .avatar-section {
    display: flex;
    justify-content: center;
    padding: 20px 0;
  }
}

.user-form,
.password-form {
  .el-form-item {
    margin-bottom: 20px;

    &:last-child {
      margin-bottom: 0;
      margin-top: 24px;
    }
  }

  :deep(.el-form-item__label) {
    color: var(--color-fg-default);
    font-weight: 500;
    padding-bottom: 8px;
  }

  :deep(.el-input__wrapper) {
    background-color: var(--color-canvas-default);
    border-color: var(--color-border-default);
    box-shadow: 0 0 0 1px var(--color-border-default) inset;

    &:hover,
    &:focus-within {
      box-shadow: 0 0 0 1px var(--color-accent-emphasis) inset;
    }
  }

  :deep(.el-input__inner) {
    color: var(--color-fg-default);
    background-color: transparent;

    &::placeholder {
      color: var(--color-fg-muted);
    }
  }

  :deep(.el-input__prefix) {
    color: var(--color-fg-muted);
  }
}

.save-btn {
  width: 100%;
  height: 40px;
  font-size: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.password-card {
  height: fit-content;
}

.password-section {
  margin-bottom: 24px;
}

.subsection-title {
  font-size: 15px;
  font-weight: 600;
  color: var(--color-fg-default);
  margin: 0 0 4px 0;
}

.subsection-desc {
  font-size: 13px;
  color: var(--color-fg-muted);
  margin: 0 0 20px 0;
}

.theme-section {
  .subsection-title {
    margin-bottom: 4px;
  }

  .subsection-desc {
    margin-bottom: 16px;
  }
}

.theme-options {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.theme-option {
  position: relative;
  padding: 16px;
  border: 2px solid var(--color-border-default);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s;
  background-color: var(--color-canvas-default);

  &:hover {
    border-color: var(--color-accent-muted);
  }

  &.active {
    border-color: var(--color-accent-emphasis);
    background-color: var(--color-accent-subtle);
  }
}

.theme-preview {
  width: 100%;
  height: 80px;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 12px;
  border: 1px solid var(--color-border-default);

  .preview-header {
    height: 20px;
  }

  .preview-content {
    padding: 12px;
  }

  .preview-line {
    height: 8px;
    border-radius: 4px;
    margin-bottom: 8px;

    &.short {
      width: 60%;
    }
  }
}

.light-preview {
  background-color: #ffffff;

  .preview-header {
    background-color: #f6f8fa;
    border-bottom: 1px solid #d0d7de;
  }

  .preview-line {
    background-color: #eaeef2;
  }
}

.dark-preview {
  background-color: #0d1117;

  .preview-header {
    background-color: #161b22;
    border-bottom: 1px solid #30363d;
  }

  .preview-line {
    background-color: #21262d;
  }
}

.theme-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: var(--color-fg-default);
  text-align: center;
}

.check-icon {
  position: absolute;
  top: 8px;
  right: 8px;
  font-size: 16px;
  color: var(--color-accent-emphasis);
}

:deep(.el-divider) {
  margin: 24px 0;
  border-color: var(--color-border-default);
}

// 响应式
@media (max-width: 768px) {
  .user-settings {
    padding: 16px;
  }

  .page-header {
    .page-title {
      font-size: 24px;

      .el-icon {
        font-size: 28px;
      }
    }
  }

  .theme-options {
    grid-template-columns: 1fr;
  }
}
</style>
