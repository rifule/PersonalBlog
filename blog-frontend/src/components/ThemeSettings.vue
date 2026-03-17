<template>
  <div class="theme-settings">
    <el-drawer
      v-model="visible"
      title="页面设置"
      size="400px"
      :with-header="true"
    >
      <ThemeSettingsPanel />
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import ThemeSettingsPanel from './ThemeSettingsPanel.vue'

const visible = ref(false)

const open = () => {
  visible.value = true
}

defineExpose({
  open
})
</script>
