import request from '@/utils/request'
import type { ApiResponse, User, LoginForm } from '@/types'

interface LoginResponse {
  token: string
  user: User
}

export const login = (data: LoginForm): Promise<ApiResponse<LoginResponse>> => {
  return request.post('/auth/login', data)
}

export const getCurrentUser = (): Promise<ApiResponse<User>> => {
  return request.get('/auth/me')
}
