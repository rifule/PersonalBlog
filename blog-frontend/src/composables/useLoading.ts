import { ref } from 'vue'
import type { Ref } from 'vue'

export interface UseLoadingOptions {
  immediate?: boolean
}

export interface UseLoadingReturn {
  loading: Ref<boolean>
  start: () => void
  stop: () => void
  toggle: () => void
  withLoading: <T>(promise: Promise<T>) => Promise<T>
}

export function useLoading(options: UseLoadingOptions = {}): UseLoadingReturn {
  const { immediate = false } = options
  
  const loading = ref(immediate)

  const start = () => {
    loading.value = true
  }

  const stop = () => {
    loading.value = false
  }

  const toggle = () => {
    loading.value = !loading.value
  }

  const withLoading = async <T>(promise: Promise<T>): Promise<T> => {
    start()
    try {
      return await promise
    } finally {
      stop()
    }
  }

  return {
    loading,
    start,
    stop,
    toggle,
    withLoading
  }
}
