import request from '@/utils/request'

export const getCurrentUser = () => {
  return request.get('/user/info')
}

export const updateUserInfo = (data: {
  nickname?: string
  email?: string
  githubUrl?: string
}) => {
  return request.put('/user/info', data)
}

export const uploadAvatar = (file: File) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/user/avatar', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export const updateAvatar = (avatarUrl: string) => {
  return request.put('/user/avatar', null, {
    params: { avatarUrl }
  })
}
