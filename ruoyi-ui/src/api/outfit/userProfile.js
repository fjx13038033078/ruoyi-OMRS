import request from '@/utils/request'

// 获取当前用户完整画像信息
export function getUserProfile() {
  return request({
    url: '/outfit/profile',
    method: 'get'
  })
}

// 更新用户身体数据
export function updateBodyInfo(data) {
  return request({
    url: '/outfit/profile/bodyInfo',
    method: 'put',
    data: data
  })
}

// 获取用户风格偏好
export function getUserStyles() {
  return request({
    url: '/outfit/profile/styles',
    method: 'get'
  })
}

// 保存用户风格偏好
export function saveUserStyles(data) {
  return request({
    url: '/outfit/profile/styles',
    method: 'post',
    data: data
  })
}

// 获取用户场景偏好
export function getUserScenes() {
  return request({
    url: '/outfit/profile/scenes',
    method: 'get'
  })
}

// 保存用户场景偏好
export function saveUserScenes(data) {
  return request({
    url: '/outfit/profile/scenes',
    method: 'post',
    data: data
  })
}

