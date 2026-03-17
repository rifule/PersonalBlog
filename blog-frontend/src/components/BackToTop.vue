<template>
  <transition name="fade">
    <button
      v-show="visible"
      class="back-to-top"
      @click="scrollToTop"
      title="回到顶部"
    >
      <svg class="progress-ring" width="56" height="56">
        <circle
          class="progress-ring-circle-bg"
          cx="28"
          cy="28"
          r="24"
        />
        <circle
          class="progress-ring-circle"
          cx="28"
          cy="28"
          r="24"
          :stroke-dasharray="circumference"
          :stroke-dashoffset="strokeDashoffset"
        />
      </svg>
      <span class="progress-text" v-if="progress < 100">{{ progress }}%</span>
      <el-icon v-else size="24" class="arrow-icon"><Top /></el-icon>
    </button>
  </transition>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'

const visible = ref(false)
const progress = ref(0)

const radius = 24
const circumference = 2 * Math.PI * radius

const strokeDashoffset = computed(() => {
  return circumference - (progress.value / 100) * circumference
})

const handleScroll = () => {
  const scrollTop = window.scrollY
  const docHeight = document.documentElement.scrollHeight - window.innerHeight
  progress.value = docHeight > 0 ? Math.round((scrollTop / docHeight) * 100) : 0
  visible.value = scrollTop > 100
}

const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  handleScroll()
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped lang="scss">
.back-to-top {
  position: fixed;
  bottom: 40px;
  right: 40px;
  width: 56px;
  height: 56px;
  border-radius: 50%;
  border: none;
  background-color: var(--color-canvas-default);
  color: var(--color-fg-default);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s;
  z-index: 1000;
  padding: 0;
  overflow: hidden;

  &:hover {
    background-color: var(--color-accent-subtle);
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);

    .progress-ring-circle {
      stroke: var(--color-accent-fg);
    }

    .progress-text {
      color: var(--color-accent-fg);
    }
  }
}

.progress-ring {
  position: absolute;
  top: 0;
  left: 0;
  transform: rotate(-90deg);
}

.progress-ring-circle-bg {
  fill: none;
  stroke: var(--color-border-default);
  stroke-width: 3;
}

.progress-ring-circle {
  fill: none;
  stroke: var(--color-accent-emphasis);
  stroke-width: 3;
  stroke-linecap: round;
  transition: stroke-dashoffset 0.1s ease, stroke 0.3s;
}

.progress-text {
  font-size: 12px;
  font-weight: 600;
  color: var(--color-fg-default);
  z-index: 1;
  transition: color 0.3s;
}

.arrow-icon {
  color: var(--color-accent-fg);
  z-index: 1;
  animation: bounce 1.5s infinite;
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-3px); }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s, transform 0.3s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: scale(0.8);
}
</style>
