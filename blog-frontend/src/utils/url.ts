// API 基础 URL
export const API_BASE_URL = 'http://localhost:8080'

/**
 * 处理头像 URL，将相对路径转换为完整 URL
 * @param avatarUrl 头像 URL（可能是相对路径或完整 URL）
 * @returns 完整的头像 URL
 */
export const getFullAvatarUrl = (avatarUrl: string | undefined | null): string => {
  if (!avatarUrl) {
    return ''
  }
  // 如果已经是完整 URL，直接返回
  if (avatarUrl.startsWith('http://') || avatarUrl.startsWith('https://')) {
    return avatarUrl
  }
  // 如果是 /uploads/ 开头的路径（后端上传的头像），拼接完整 URL
  if (avatarUrl.startsWith('/uploads/')) {
    return API_BASE_URL + avatarUrl
  }
  // 其他路径（如 /image/xxx.jpg）直接返回，由前端静态资源处理
  return avatarUrl
}

/**
 * 处理图片 URL，将相对路径转换为完整 URL
 * @param imageUrl 图片 URL（可能是相对路径或完整 URL）
 * @returns 完整的图片 URL
 */
export const getFullImageUrl = (imageUrl: string | undefined | null): string => {
  return getFullAvatarUrl(imageUrl)
}
