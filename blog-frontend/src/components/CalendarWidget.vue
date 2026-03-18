<template>
  <div class="calendar-widget github-card">
    <div class="calendar-header">
      <h3 class="widget-title">
        <el-icon><Calendar /></el-icon>
        文章日历
        <span class="anime-decoration"></span>
      </h3>
    </div>

    <div class="calendar-body">
      <div class="calendar-nav">
        <el-button circle size="small" @click="prevMonth">
          <el-icon><ArrowLeft /></el-icon>
        </el-button>
        <span class="current-month">{{ currentYear }}年{{ currentMonth + 1 }}月</span>
        <el-button circle size="small" @click="nextMonth">
          <el-icon><ArrowRight /></el-icon>
        </el-button>
      </div>

      <div class="calendar-grid">
        <div class="weekday-header" v-for="day in weekdays" :key="day">
          {{ day }}
        </div>
        <div
          v-for="date in calendarDates"
          :key="date.date"
          class="calendar-day"
          :class="{
            'other-month': !date.isCurrentMonth,
            'today': date.isToday,
            'has-post': date.hasPost
          }"
          @click="selectDate(date)"
        >
          <span class="day-number">{{ date.day }}</span>
          <div v-if="date.hasPost" class="post-indicator"></div>
        </div>
      </div>
    </div>

    <div class="calendar-stats">
      <div class="stat-item">
        <span class="stat-dot" style="background-color: #ebedf0;"></span>
        <span>无文章</span>
      </div>
      <div class="stat-item">
        <span class="stat-dot" style="background-color: #9be9a8;"></span>
        <span>有文章</span>
      </div>
      <div class="stat-item">
        <span class="stat-dot today-dot"></span>
        <span>今天</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import dayjs from 'dayjs'
import request from '@/utils/request'

const weekdays = ['日', '一', '二', '三', '四', '五', '六']

const currentDate = ref(dayjs())
const currentYear = computed(() => currentDate.value.year())
const currentMonth = computed(() => currentDate.value.month())
const postsDates = ref<string[]>([])

// 获取文章日历数据
const fetchCalendarData = async () => {
  try {
    const year = currentYear.value
    const month = currentMonth.value + 1
    const res = await request.get(`/articles/calendar`, {
      params: { year, month }
    })
    postsDates.value = res.data || []
  } catch (error) {
    // 如果接口不存在，静默失败
    postsDates.value = []
  }
}

const calendarDates = computed(() => {
  const year = currentYear.value
  const month = currentMonth.value

  const firstDay = dayjs(new Date(year, month, 1))
  const lastDay = dayjs(new Date(year, month + 1, 0))

  const startOfWeek = firstDay.day()
  const daysInMonth = lastDay.date()

  const dates = []

  // 上个月的日期
  for (let i = startOfWeek - 1; i >= 0; i--) {
    const date = firstDay.subtract(i + 1, 'day')
    dates.push({
      date: date.format('YYYY-MM-DD'),
      day: date.date(),
      isCurrentMonth: false,
      isToday: date.isSame(dayjs(), 'day'),
      hasPost: postsDates.value.includes(date.format('YYYY-MM-DD'))
    })
  }

  // 当前月的日期
  for (let i = 1; i <= daysInMonth; i++) {
    const date = dayjs(new Date(year, month, i))
    dates.push({
      date: date.format('YYYY-MM-DD'),
      day: i,
      isCurrentMonth: true,
      isToday: date.isSame(dayjs(), 'day'),
      hasPost: postsDates.value.includes(date.format('YYYY-MM-DD'))
    })
  }

  // 下个月的日期
  const remainingDays = 42 - dates.length
  for (let i = 1; i <= remainingDays; i++) {
    const date = lastDay.add(i, 'day')
    dates.push({
      date: date.format('YYYY-MM-DD'),
      day: date.date(),
      isCurrentMonth: false,
      isToday: date.isSame(dayjs(), 'day'),
      hasPost: postsDates.value.includes(date.format('YYYY-MM-DD'))
    })
  }

  return dates
})

const prevMonth = () => {
  currentDate.value = currentDate.value.subtract(1, 'month')
}

const nextMonth = () => {
  currentDate.value = currentDate.value.add(1, 'month')
}

const selectDate = (date: any) => {
  // 日期选择功能预留
  // 可在此添加选中日期后的处理逻辑
}

// 监听月份变化，重新获取数据
watch(() => currentMonth.value, fetchCalendarData)

onMounted(() => {
  fetchCalendarData()
})
</script>

<style scoped lang="scss">
.calendar-widget {
  padding: 16px;
}

.calendar-header {
  margin-bottom: 16px;
}

.widget-title {
  font-size: 16px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0;
}

.calendar-nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;

  :deep(.el-button) {
    background: var(--color-canvas-subtle);
    border-color: var(--color-border-default);
    color: var(--color-fg-default);

    &:hover {
      background: var(--color-accent-subtle);
      border-color: var(--color-accent-fg);
      color: var(--color-accent-fg);
    }
  }
}

.current-month {
  font-weight: 600;
  color: var(--color-fg-default);
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 4px;
}

.weekday-header {
  text-align: center;
  font-size: 12px;
  color: var(--color-fg-muted);
  padding: 8px 0;
  font-weight: 500;
}

.calendar-day {
  aspect-ratio: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 6px;
  cursor: pointer;
  transition: var(--transition-smooth);
  position: relative;

  &:hover {
    background: var(--decoration-gradient);
    transform: var(--hover-lift);
  }

  &.other-month {
    color: var(--color-fg-muted);
  }

  &.today {
    background-color: var(--color-accent-subtle);
    border: 1px solid var(--color-accent-fg);

    .day-number {
      color: var(--color-accent-fg);
      font-weight: 600;
    }
  }

  &.has-post {
    background-color: rgba(155, 233, 168, 0.3);

    &:hover {
      background-color: rgba(155, 233, 168, 0.5);
    }
  }
}

.day-number {
  font-size: 13px;
}

.post-indicator {
  width: 4px;
  height: 4px;
  background-color: #40c463;
  border-radius: 50%;
  margin-top: 2px;
}

.calendar-stats {
  display: flex;
  gap: 16px;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid var(--color-border-default);
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: var(--color-fg-muted);
}

.stat-dot {
  width: 10px;
  height: 10px;
  border-radius: 2px;
}

.today-dot {
  background-color: var(--color-accent-subtle);
  border: 1px solid var(--color-accent-fg);
}
</style>
