import { ref } from 'vue'
import type { Ref } from 'vue'

export interface PaginationOptions {
  page?: number
  pageSize?: number
  immediate?: boolean
}

export interface PaginationReturn {
  page: Ref<number>
  pageSize: Ref<number>
  total: Ref<number>
  loading: Ref<boolean>
  reset: () => void
  setPage: (page: number) => void
  setPageSize: (size: number) => void
}

export function usePagination(options: PaginationOptions = {}): PaginationReturn {
  const { page = 1, pageSize = 10 } = options

  const pageNum = ref(page)
  const size = ref(pageSize)
  const total = ref(0)
  const loading = ref(false)

  const reset = () => {
    pageNum.value = 1
    total.value = 0
  }

  const setPage = (newPage: number) => {
    pageNum.value = newPage
  }

  const setPageSize = (newSize: number) => {
    size.value = newSize
    pageNum.value = 1
  }

  return {
    page: pageNum,
    pageSize: size,
    total,
    loading,
    reset,
    setPage,
    setPageSize
  }
}
