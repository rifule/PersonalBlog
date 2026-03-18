import { ref } from 'vue'
import type { Ref } from 'vue'

export interface DebounceOptions {
  delay?: number
  immediate?: boolean
}

export function useDebounce<T extends (...args: any[]) => any>(
  fn: T,
  options: DebounceOptions = {}
): (...args: Parameters<T>) => void {
  const { delay = 300 } = options
  let timer: ReturnType<typeof setTimeout> | null = null

  return (...args: Parameters<T>) => {
    if (timer) {
      clearTimeout(timer)
    }
    timer = setTimeout(() => {
      fn(...args)
    }, delay)
  }
}

export function useThrottle<T extends (...args: any[]) => any>(
  fn: T,
  delay: number = 300
): (...args: Parameters<T>) => void {
  let lastTime = 0
  let timer: ReturnType<typeof setTimeout> | null = null

  return (...args: Parameters<T>) => {
    const now = Date.now()
    
    if (now - lastTime >= delay) {
      lastTime = now
      fn(...args)
    } else {
      if (timer) {
        clearTimeout(timer)
      }
      timer = setTimeout(() => {
        lastTime = Date.now()
        fn(...args)
      }, delay - (now - lastTime))
    }
  }
}

export function useStorage<T>(key: string, defaultValue: T) {
  const get = (): T => {
    const stored = localStorage.getItem(key)
    if (stored) {
      try {
        return JSON.parse(stored)
      } catch {
        return stored as any
      }
    }
    return defaultValue
  }

  const set = (value: T) => {
    if (typeof value === 'string') {
      localStorage.setItem(key, value)
    } else {
      localStorage.setItem(key, JSON.stringify(value))
    }
  }

  const remove = () => {
    localStorage.removeItem(key)
  }

  return { get, set, remove }
}

export function useLocalStorage<T>(key: string, defaultValue: T): Ref<T> {
  const { get, set } = useStorage(key, defaultValue)
  const data = ref<T>(get()) as Ref<T>

  const update = (value: T) => {
    data.value = value
    set(value)
  }

  return {
    get value() {
      return data.value
    },
    set value(val: T) {
      update(val)
    }
  } as Ref<T>
}
