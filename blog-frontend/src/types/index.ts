// 用户相关
export interface User {
  id: number
  username: string
  nickname?: string
  email: string
  avatar: string
  role: string
  status: number
  createTime: string
  githubUrl?: string
}

export interface LoginForm {
  username: string
  password: string
}

// 文章相关
export interface Article {
  id: number
  title: string
  content: string
  summary: string
  cover: string
  authorId: number
  authorName: string
  categoryId: number
  categoryName: string
  tagIds: number[]
  tags: string[]
  isTop: number
  createTime: string
  updateTime: string
}

export interface ArticleListVO {
  id: number
  title: string
  summary: string
  cover: string
  authorName: string
  categoryName: string
  tagIds: number[]
  tags: string[]
  isTop?: number
  createTime: string
}

// 分类相关
export interface Category {
  id: number
  name: string
  description: string
  sortOrder: number
  articleCount?: number
}

// 标签相关
export interface Tag {
  id: number
  name: string
  color: string
  articleCount?: number
}

// 评论相关
export interface Comment {
  id: number
  articleId: number
  parentId: number | null
  userId: number
  userName: string
  userAvatar: string
  content: string
  status: number
  createTime: string
  children?: Comment[]
}

// API 响应
export interface ApiResponse<T> {
  code: number
  message: string
  data: T
  timestamp: string
}

// 分页
export interface PageResult<T> {
  list: T[]
  total: number
  pageNum: number
  pageSize: number
  pages: number
}
