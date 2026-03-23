import request from '@/utils/request'
import type { ApiResponse, Article, ArticleListVO, PageResult } from '@/types'

export const getArticleList = (pageNum = 1, pageSize = 10): Promise<ApiResponse<PageResult<ArticleListVO>>> => {
  return request.get('/articles', {
    params: { pageNum, pageSize }
  })
}

export const getArticleById = (id: number): Promise<ApiResponse<Article>> => {
  return request.get(`/articles/${id}`)
}

export const getArticlesByCategory = (categoryId: number, pageNum = 1, pageSize = 10): Promise<ApiResponse<PageResult<ArticleListVO>>> => {
  return request.get(`/articles/category/${categoryId}`, {
    params: { pageNum, pageSize }
  })
}

export const getArticlesByTag = (tagId: number, pageNum = 1, pageSize = 100): Promise<ApiResponse<PageResult<ArticleListVO>>> => {
  return request.get(`/tags/${tagId}/articles`, {
    params: { pageNum, pageSize }
  })
}

export const searchArticles = (keyword: string, pageNum = 1, pageSize = 10): Promise<ApiResponse<PageResult<ArticleListVO>>> => {
  return request.get('/articles/search', {
    params: { keyword, pageNum, pageSize }
  })
}

export const createArticle = (data: Partial<Article>): Promise<ApiResponse<Article>> => {
  return request.post('/articles', data)
}

export const updateArticle = (id: number, data: Partial<Article>): Promise<ApiResponse<Article>> => {
  return request.put(`/articles/${id}`, data)
}

export const deleteArticle = (id: number): Promise<ApiResponse<void>> => {
  return request.delete(`/articles/${id}`)
}
