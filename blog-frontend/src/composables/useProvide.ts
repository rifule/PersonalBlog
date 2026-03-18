import { InjectionKey, provide, inject, Ref } from 'vue'

export interface BlogContext {
  refreshTags: () => void
  refreshCategories: () => void
  refreshArticles: () => void
}

const BlogContextKey: InjectionKey<BlogContext> = Symbol('blog-context')

export function provideBlogContext(context: BlogContext) {
  provide(BlogContextKey, context)
}

export function useBlogContext(): BlogContext {
  const context = inject(BlogContextKey)
  if (!context) {
    throw new Error('useBlogContext must be used within a component that calls provideBlogContext')
  }
  return context
}

export interface LoadingContext {
  globalLoading: Ref<boolean>
  setGlobalLoading: (loading: boolean) => void
}

const LoadingContextKey: InjectionKey<LoadingContext> = Symbol('loading-context')

export function provideLoadingContext(context: LoadingContext) {
  provide(LoadingContextKey, context)
}

export function useLoadingContext(): LoadingContext {
  const context = inject(LoadingContextKey)
  if (!context) {
    throw new Error('useLoadingContext must be used within a component that calls provideLoadingContext')
  }
  return context
}

export interface PaginationContext {
  page: Ref<number>
  pageSize: Ref<number>
  total: Ref<number>
  setPage: (page: number) => void
  setPageSize: (size: number) => void
  reset: () => void
}

const PaginationContextKey: InjectionKey<PaginationContext> = Symbol('pagination-context')

export function providePaginationContext(context: PaginationContext) {
  provide(PaginationContextKey, context)
}

export function usePaginationContext(): PaginationContext {
  const context = inject(PaginationContextKey)
  if (!context) {
    throw new Error('usePaginationContext must be used within a component that calls providePaginationContext')
  }
  return context
}
