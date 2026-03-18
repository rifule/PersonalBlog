<template>
  <footer class="app-footer">
    <div class="github-container">
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
  </footer>
</template>

<script setup lang="ts">
const currentYear = new Date().getFullYear()
</script>

<style scoped lang="scss">
.app-footer {
  padding: 24px 0;
  background: var(--decoration-gradient);
  border-top: var(--decoration-border);
  margin-top: auto;
  text-align: center;
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 2px;
    background: linear-gradient(90deg, transparent, var(--anime-primary), var(--anime-secondary), transparent);
  }

  p {
    margin: 4px 0;
    font-size: 13px;
    color: var(--color-fg-muted);
  }

  .powered {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 4px;
  }
}
</style>
