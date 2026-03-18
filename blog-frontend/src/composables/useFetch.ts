import { ref, computed, watch } from 'vue'
import type { Ref } from 'vue'

export type FetchMethod = 'get' | 'post' | 'put' | 'delete'

export interface UseFetchOptions<T = any> {
  immediate?: boolean
  initialData?: T
  onSuccess?: (data: T) => void
  onError?: (error: any) => void
  watchParams?: Ref<any>[]
}

export interface UseFetchReturn<T> {
  data: Ref<T | null>
  loading: Ref<boolean>
  error: Ref<any | null>
  execute: (params?: any) => Promise<T | null>
  refresh: () => Promise<void>
}

export function useFetch<T = any>(
  fetcher: (...args: any[]) => Promise<any>,
  options: UseFetchOptions<T> = {}
): UseFetchReturn<T> {
  const {
    immediate = false,
    initialData = null,
    onSuccess,
    onError,
    watchParams = []
  } = options

  const data = ref<T | null>(initialData) as Ref<T | null>
  const loading = ref(false)
  const error = ref<any | null>(null)

  const execute = async (params?: any): Promise<T | null> => {
    loading.value = true
    error.value = null

    try {
      const response = await fetcher(params)
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

  const refresh = async () => {
    await execute()
  }

  watchParams.forEach(param => {
    watch(param, () => {
      execute()
    })
  })

  if (immediate) {
    execute()
  }

  return {
    data,
    loading,
    error,
    execute,
    refresh
  }
}

export function useListFetch<T = any>(
  fetcher: (...args: any[]) => Promise<any>,
  options: UseFetchOptions<T[]> & {
    filterEmpty?: boolean
    emptyField?: string
  } = {}
): UseFetchReturn<T[]> & {
  list: Ref<T[]>
  isEmpty: Ref<boolean>
} {
  const {
    filterEmpty = false,
    emptyField = 'articleCount',
    ...fetchOptions
  } = options

  const fetchResult = useFetch<T[]>(fetcher, {
    ...fetchOptions,
    onSuccess: (data) => {
      if (filterEmpty && Array.isArray(data)) {
        const filtered = data.filter((item: any) => {
          const count = item[emptyField]
          return count !== undefined && count !== null && count > 0
        })
        fetchResult.data.value = filtered
      }
    }
  })

  const list = computed(() => {
    const rawData = fetchResult.data.value
    if (!rawData) return []
    if (filterEmpty) {
      return rawData.filter((item: any) => {
        const count = item[emptyField]
        return count !== undefined && count !== null && count > 0
      })
    }
    return Array.isArray(rawData) ? rawData : []
  })

  const isEmpty = computed(() => list.value.length === 0)

  return {
    ...fetchResult,
    list,
    isEmpty
  }
}
