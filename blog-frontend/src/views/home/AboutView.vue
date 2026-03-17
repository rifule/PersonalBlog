<template>
  <div class="about-view github-card">
    <div class="about-header">
      <el-avatar :size="120" src="/image/sadrudy.jpg" />
      <h1>关于我</h1>
      <p class="subtitle">热爱技术，热爱生活 ✨</p>
    </div>
    
    <div class="about-content">
      <section class="about-section">
        <h2>👋 你好</h2>
        <p>欢迎来到我的博客！我是一名热爱技术的开发者，喜欢探索新技术，分享学习心得。</p>
      </section>
      
      <section class="about-section">
        <h2>🛠️ 技术栈</h2>
        <div class="tech-stack">
          <span class="tech-tag">Java</span>
          <span class="tech-tag">Spring Boot</span>
          <span class="tech-tag">Vue.js</span>
          <span class="tech-tag">MySQL</span>
          <span class="tech-tag">Redis</span>
          <span class="tech-tag">Docker</span>
          <span class="tech-tag">Linux</span>
        </div>
      </section>
      

    </div>
  </div>
</template>

<style scoped lang="scss">
.about-view {
  padding: 40px;
}

.about-header {
  text-align: center;
  margin-bottom: 40px;
  
  .el-avatar {
    border: 4px solid var(--color-canvas-subtle);
    box-shadow: var(--anime-shadow);
  }
  
  h1 {
    margin: 16px 0 8px;
    font-size: 28px;
  }
  
  .subtitle {
    color: var(--color-fg-muted);
    font-size: 16px;
  }
}

.about-content {
  max-width: 600px;
  margin: 0 auto;
}

.about-section {
  margin-bottom: 32px;
  
  h2 {
    font-size: 18px;
    margin-bottom: 16px;
    padding-bottom: 8px;
    border-bottom: 1px solid var(--color-border-default);
  }
  
  p {
    color: var(--color-fg-muted);
    line-height: 1.8;
  }
}

.tech-stack {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.tech-tag {
  padding: 6px 14px;
  background-color: var(--color-accent-subtle);
  color: var(--color-accent-fg);
  border-radius: 16px;
  font-size: 14px;
  font-weight: 500;
}


</style>
