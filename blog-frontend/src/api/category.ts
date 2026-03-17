import request from '@/utils/request'
import type { ApiResponse, Category } from '@/types'

export const getCategoryList = (): Promise<ApiResponse<Category[]>> => {
  return request.get('/categories')
}

export const getCategoryById = (id: number): Promise<ApiResponse<Category>> => {
  return request.get(`/categories/${id}`)
}

export const createCategory = (data: Partial<Category>): Promise<ApiResponse<Category>> => {
  return request.post('/categories', data)
}

export const updateCategory = (id: number, data: Partial<Category>): Promise<ApiResponse<Category>> => {
  return request.put(`/categories/${id}`, data)
}

export const deleteCategory = (id: number): Promise<ApiResponse<void>> => {
  return request.delete(`/categories/${id}`)
}
