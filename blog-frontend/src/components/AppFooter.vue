<template>
  <footer class="app-footer">
    <div class="github-container">
      <div class="footer-content">
        <div class="footer-left">
          <p class="copyright">
            © {{ currentYear }} My Blog. All rights reserved.
          </p>
          <p class="powered">
            Powered by 
            <el-link href="https://vuejs.org/" target="_blank">Vue 3</el-link> + 
            <el-link href="https://spring.io/" target="_blank">Spring Boot</el-link>
            <span class="anime-emoji">✨</span>
          </p>
        </div>
        <div class="footer-right">
          <el-link href="#" class="footer-link">关于</el-link>
          <el-link href="#" class="footer-link">友链</el-link>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const currentYear = computed(() => new Date().getFullYear())
</script>

<style scoped lang="scss">
.app-footer {
  padding: 24px 0;
  background-color: var(--color-canvas-subtle);
  border-top: 1px solid var(--color-border-default);
  margin-top: auto;
}

.footer-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}

.footer-left {
  p {
    margin: 4px 0;
    font-size: 13px;
    color: var(--color-fg-muted);
  }
  
  .powered {
    display: flex;
    align-items: center;
    gap: 4px;
  }
  
  .anime-emoji {
    margin-left: 4px;
    animation: bounce 1s infinite;
  }
}

.footer-right {
  display: flex;
  gap: 16px;
}

.footer-link {
  font-size: 13px;
  color: var(--color-fg-muted);
  
  &:hover {
    color: var(--color-accent-fg);
  }
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-3px); }
}

@media (max-width: 768px) {
  .footer-content {
    flex-direction: column;
    text-align: center;
  }
}
</style>
