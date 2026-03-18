import { ref } from 'vue'
import type { Ref } from 'vue'

export interface UseApiOptions<T = any> {
  immediate?: boolean
  onSuccess?: (data: T) => void
  onError?: (error: any) => void
}

export interface UseApiReturn<T> {
  data: Ref<T | null>
  loading: Ref<boolean>
  error: Ref<any | null>
  execute: (...args: any[]) => Promise<T | null>
  reset: () => void
}

export function useApi<T = any>(
  apiFn: (...args: any[]) => Promise<any>,
  options: UseApiOptions<T> = {}
): UseApiReturn<T> {
  const { immediate = false, onSuccess, onError } = options

  const data = ref<T | null>(null) as Ref<T | null>
  const loading = ref(false)
  const error = ref<any | null>(null)

  const execute = async (...args: any[]): Promise<T | null> => {
    loading.value = true
    error.value = null

    try {
      const response = await apiFn(...args)
      const result = response.data
      data.value = result

      if (onSuccess) {
        onSuccess(result)
      }

      return result
    } catch (err: any) {
      error.value = err

      if (onError) {
        onError(err)
      }

      return null
    } finally {
      loading.value = false
    }
  }

  const reset = () => {
    data.value = null
    error.value = null
    loading.value = false
  }

  if (immediate) {
    execute()
  }

  return {
    data,
    loading,
    error,
    execute,
    reset
  }
}
