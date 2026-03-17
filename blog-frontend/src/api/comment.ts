import request from '@/utils/request'
import type { ApiResponse, Comment, PageResult } from '@/types'

export const getCommentsByArticle = (articleId: number, pageNum = 1, pageSize = 10): Promise<ApiResponse<PageResult<Comment>>> => {
  return request.get(`/comments/article/${articleId}`, {
    params: { pageNum, pageSize }
  })
}

export const createComment = (data: Partial<Comment>): Promise<ApiResponse<Comment>> => {
  return request.post('/comments', data)
}

export const deleteComment = (id: number): Promise<ApiResponse<void>> => {
  return request.delete(`/comments/${id}`)
}
