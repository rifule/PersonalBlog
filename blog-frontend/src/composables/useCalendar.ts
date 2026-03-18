import { ref, computed, watch, Ref } from 'vue'
import dayjs from 'dayjs'

export interface CalendarDate {
  date: string
  day: number
  isCurrentMonth: boolean
  isToday: boolean
  hasPost: boolean
}

export interface UseCalendarOptions {
  initialDate?: dayjs.ConfigType
  onFetchData?: (year: number, month: number) => Promise<string[]>
}

export interface UseCalendarReturn {
  currentDate: Ref<ReturnType<typeof dayjs>>
  currentYear: Ref<number>
  currentMonth: Ref<number>
  calendarDates: Ref<CalendarDate[]>
  postsDates: Ref<string[]>
  prevMonth: () => void
  nextMonth: () => void
  selectDate: (date: CalendarDate) => void
  refresh: () => void
}

export function useCalendar(options: UseCalendarOptions = {}): UseCalendarReturn {
  const { initialDate = dayjs(), onFetchData } = options

  const currentDate = ref(dayjs(initialDate))
  const postsDates = ref<string[]>([])

  const currentYear = computed(() => currentDate.value.year())
  const currentMonth = computed(() => currentDate.value.month())

  const fetchCalendarData = async () => {
    if (!onFetchData) return
    
    try {
      const year = currentYear.value
      const month = currentMonth.value + 1
      const data = await onFetchData(year, month)
      postsDates.value = data
    } catch (error) {
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

    const dates: CalendarDate[] = []

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

  const selectDate = (_date: CalendarDate) => {
    // Reserved for date selection
  }

  const refresh = () => {
    fetchCalendarData()
  }

  watch(() => currentMonth.value, fetchCalendarData)

  return {
    currentDate: currentDate as any,
    currentYear: currentYear as any,
    currentMonth: currentMonth as any,
    calendarDates: calendarDates as any,
    postsDates: postsDates as any,
    prevMonth,
    nextMonth,
    selectDate,
    refresh
  }
}
