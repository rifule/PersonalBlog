import request from '@/utils/request'
import type { ApiResponse, Tag } from '@/types'

export const getTagList = (): Promise<ApiResponse<Tag[]>> => {
  return request.get('/tags')
}

export const getTagById = (id: number): Promise<ApiResponse<Tag>> => {
  return request.get(`/tags/${id}`)
}

export const createTag = (data: Partial<Tag>): Promise<ApiResponse<Tag>> => {
  return request.post('/tags', data)
}

export const updateTag = (id: number, data: Partial<Tag>): Promise<ApiResponse<Tag>> => {
  return request.put(`/tags/${id}`, data)
}

export const deleteTag = (id: number): Promise<ApiResponse<void>> => {
  return request.delete(`/tags/${id}`)
}
